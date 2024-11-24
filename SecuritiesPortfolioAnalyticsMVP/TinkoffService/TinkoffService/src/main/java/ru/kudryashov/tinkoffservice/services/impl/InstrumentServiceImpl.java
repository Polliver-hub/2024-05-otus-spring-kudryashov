package ru.kudryashov.tinkoffservice.services.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import ru.kudryashov.tinkoffservice.annotation.Profiled;
import ru.kudryashov.tinkoffservice.dto.FigisDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentPriceDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentsDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentsPricesDto;
import ru.kudryashov.tinkoffservice.dto.TickersDto;
import ru.kudryashov.tinkoffservice.exceptions.InstrumentException;
import ru.kudryashov.tinkoffservice.mappers.InstrumentMapper;
import ru.kudryashov.tinkoffservice.services.InstrumentService;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static ru.tinkoff.piapi.core.utils.DateUtils.timestampToString;
import static ru.tinkoff.piapi.core.utils.MapperUtils.quotationToBigDecimal;


@Service
@RequiredArgsConstructor
@Slf4j
public class InstrumentServiceImpl implements InstrumentService {

    private final InvestApi investApi;

    private final InstrumentMapper instrumentMapper;

    private Set<String> classCodes = new HashSet<>();

    @Override
    @Profiled
    public InstrumentsDto getAllStocks(int page, int size) {
        var shares = investApi.getInstrumentsService().getAllSharesSync();
        var dtos = shares.stream().map(instrumentMapper::shareTinkoffToDto).toList();
        log.info("!!!!!!! size: {}", dtos.size());
        // Выполняем пагинацию
        int fromIndex = Math.min(page * size, dtos.size());
        int toIndex = Math.min(fromIndex + size, dtos.size());
        // Если запрашиваемая страница выходит за пределы
        if (fromIndex >= dtos.size()) {
            return new InstrumentsDto(Collections.emptyList());
        }
        // Возвращаем подмножество списка
        List<InstrumentDto> paginatedDtos = dtos.subList(fromIndex, toIndex);
        return new InstrumentsDto(paginatedDtos);
    }

    @Override
    @Profiled
    public InstrumentsDto getAllInstruments(int page, int size) {
        var instruments = investApi.getInstrumentsService().getAssetsSync();
        List<InstrumentDto> result = instruments.stream()
                .map(instrumentMapper::assetToDto)
                .flatMap(List::stream)
                .toList();
        log.info("size {}", result.size());
        // Выполняем пагинацию
        int fromIndex = Math.min(page * size, result.size());
        int toIndex = Math.min(fromIndex + size, result.size());
        return new InstrumentsDto(result.subList(fromIndex, toIndex));
    }

    @Override
    @Profiled
    public List<String> getAllClassCodes() {
        if (classCodes.isEmpty()) {
            return takeAllClassCodesByTinkoff().stream().toList();
        }
        return classCodes.stream().toList();
    }

    @Override
    @Profiled
    public InstrumentsDto getInstrumentsById(String id) {
        var instrumentsTinkoff = investApi.getInstrumentsService().findInstrumentSync(id);
        var resultList = instrumentsTinkoff.stream().map(instrumentMapper::instrumentShortToDto).toList();
        return new InstrumentsDto(resultList);
    }

    @Override
    public InstrumentDto getInstrumentByFigi(String figi) {
        try {
            var finderInstr = investApi.getInstrumentsService().getInstrumentByFigiSync(figi);
            return instrumentMapper.instrumentTinkoffToDto(finderInstr);
        } catch (ApiRuntimeException ex) {
            throw InstrumentException.notFoundByFigi(figi);
        }
    }

    @Override
    public InstrumentsDto getInstrumentsByFigis(FigisDto figis) {
        List<CompletableFuture<Instrument>> instruments = new ArrayList<>();
        figis.getFigis().forEach(figi -> instruments.add(getInstrumentByFigiAsync(figi)));
        List<InstrumentDto> instrumentDtos = instruments.stream()
                .map(future -> {
                    try {
                        return future.join(); // Выполнение CompletableFuture
                    } catch (CompletionException ex) {
                        log.warn("Ошибка при выполнении CompletableFuture: {}", ex.getCause().getMessage());
                        return null; // Возвращаем null вместо выбрасывания исключения
                    }
                })
                .filter(Objects::nonNull)
                .map(instrumentMapper::instrumentTinkoffToDto)
                .toList();
        return new InstrumentsDto(instrumentDtos);
    }

    @Override
    @Profiled
    public InstrumentPriceDto getPriceByTicker(String ticker) {
        var figiByTicker = getInstrumentByTicker(ticker).getFigi();
        var lastPrice = investApi.getMarketDataService().getLastPricesSync(Collections.singleton(figiByTicker));
        for (var price : lastPrice) {
            var figi = price.getFigi();
            var priceResult = quotationToBigDecimal(price.getPrice());
            var time = timestampToString(price.getTime());
            log.info("цены закрытия торговой сессии по инструменту {}, цена: {}, дата совершения торгов: {}",
                    figi, priceResult, time);
        }
        return new InstrumentPriceDto(figiByTicker, quotationToBigDecimal(lastPrice.getFirst().getPrice()));
    }

    @Override
    public InstrumentPriceDto getPriceByFigi(String figi) {
        var lastPrice = investApi.getMarketDataService().getLastPricesSync(Collections.singleton(figi));
        return new InstrumentPriceDto(figi, quotationToBigDecimal(lastPrice.getFirst().getPrice()));
    }

    @Override
    @Profiled
    public InstrumentsPricesDto getPricesByFigis(FigisDto figis) {
        var listCompletableFuture = investApi.getMarketDataService().getLastPrices(figis.getFigis());
        var listPrices = listCompletableFuture.join();
        List<InstrumentPriceDto> instrumentPrices = listPrices.stream()
                .filter(lastPrice -> isNotEmpty(lastPrice.getFigi())
                        && isNotEmpty(lastPrice.getPrice().toString()))
                .map(lastPrice -> new InstrumentPriceDto(lastPrice.getFigi(),
                        quotationToBigDecimal(lastPrice.getPrice())))
                .toList();
        return new InstrumentsPricesDto(instrumentPrices);
    }

    /**
     * Получает информацию об инструмене по тикеру.
     *
     * <p>Для тикера отправляется запрос через соответствующий сервис Tinkoff API для получения информации об инструменте.
     * Это происходит n-раз асинхронно для каждого classCode из сета {@link #classCodes}.
     * Если возникает ошибка при обработке тикера, вместо объекта инструмента добавляется {@code null}.
     * Результат преобразуется в объект {@code InstrumentsDto}, содержащий список DTO инструментов.</p>
     *
     * @param ticker тикер
     * @return объект {@link InstrumentDto}, содержащий найденный инструмент в формате DTO
     * @throws InstrumentException если инструмент по тикеру не найден
     */
    @Override
    @Profiled
    public InstrumentDto getInstrumentByTicker(String ticker) {
        List<CompletableFuture<Instrument>> instruments = new ArrayList<>();
        for (String classCode : classCodes) {
            instruments.add(investApi.getInstrumentsService().getInstrumentByTicker(ticker, classCode));
        }
        return instruments.stream()
                .map(future -> {
                    try {
                        return future.join();
                    } catch (CompletionException ex) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(instrumentMapper::instrumentTinkoffToDto)
                .findFirst().orElseThrow(() -> InstrumentException.notFoundByTicker(ticker));
    }

    /**
     * Получает информацию об инструментах по списку тикеров.
     *
     * <p>Для каждого тикера из переданного объекта {@code TickersDto} вызывается
     * соответствующий сервис Tinkoff API для получения информации об инструменте.
     * Если возникает ошибка при обработке тикера, вместо объекта инструмента добавляется {@code null}.
     * Результат преобразуется в объект {@code InstrumentsDto}, содержащий список DTO инструментов.</p>
     *
     * @param tickers объект {@link TickersDto}, содержащий список тикеров для поиска
     * @return объект {@link InstrumentsDto}, содержащий список найденных инструментов в формате DTO
     * @throws InstrumentException если список тикеров пуст или не удаётся получить данные
     */
    @Override
    @Profiled
    public InstrumentsDto getInstrumentsByTickers(TickersDto tickers) {
        List<InstrumentDto> instrumentDtos = new ArrayList<>();
        tickers.getTickers().forEach(ticker -> {
            try {
                InstrumentDto instrumentDto = getInstrumentByTicker(ticker);
                instrumentDtos.add(instrumentDto);
            } catch (InstrumentException ex) {
                log.warn("ticker {} not found", ticker);
            }
        });
        return new InstrumentsDto(instrumentDtos);
    }

    private Set<String> takeAllClassCodesByTinkoff() {
        Set<String> classCodesTinkoff = new HashSet<>();
        var allInstruments = investApi.getInstrumentsService().getAssetsSync();
        allInstruments.forEach(asset ->
                asset.getInstrumentsList().forEach(assetInstrument -> classCodesTinkoff.add(assetInstrument.getClassCode())));
        return classCodesTinkoff;
    }

    @Async
    @Profiled
    public CompletableFuture<Instrument> getInstrumentByFigiAsync(String figi) {
        return investApi.getInstrumentsService().getInstrumentByFigi(figi);
    }

    /**
     * Инициализирует поле {@code classCodes} при запуске приложения.
     * Этот метод вызывается автоматически после создания бина Spring.
     * Использует метод {@link #takeAllClassCodesByTinkoff()} для получения всех classCode.
     */
    @PostConstruct
    private void initializeClassCodes() {
        log.info("Initializing classCodes...");
        this.classCodes = takeAllClassCodesByTinkoff();
        log.info("Initialization complete. ClassCodes size: {}", classCodes.size());
    }

    /**
     * Каждый час обновляет поле {@code classCodes}.
     * Использует метод {@link #takeAllClassCodesByTinkoff()} для получения актуальных classCode.
     * Этот метод выполняется автоматически благодаря аннотации {@code @Scheduled}.
     */
    @Scheduled(cron = "0 0 * * * ?")
    public void refreshClassCodes() {
        log.info("Refreshing classCodes...");
        this.classCodes = takeAllClassCodesByTinkoff();
        log.info("Refresh complete. ClassCodes size: {}", classCodes.size());
    }

    private static boolean isNotEmpty(String value) {
        return value != null && !value.trim().isEmpty();
    }
}
