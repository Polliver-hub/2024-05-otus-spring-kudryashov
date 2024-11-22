package ru.kudryashov.tinkoffservice.services.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import ru.kudryashov.tinkoffservice.annotation.Profiled;
import ru.kudryashov.tinkoffservice.dto.InstrumentDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentsDto;
import ru.kudryashov.tinkoffservice.dto.TickersDto;
import ru.kudryashov.tinkoffservice.exceptions.InstrumentException;
import ru.kudryashov.tinkoffservice.mappers.InstrumentMapper;
import ru.kudryashov.tinkoffservice.services.InstrumentService;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.core.InvestApi;
import ru.tinkoff.piapi.core.exception.ApiRuntimeException;

import java.math.BigDecimal;
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
        Set<String> classCodes = new HashSet<>();
        var allInstruments = investApi.getInstrumentsService().getAssetsSync();
        allInstruments.forEach(asset ->
            asset.getInstrumentsList().forEach(assetInstrument -> classCodes.add(assetInstrument.getClassCode())));
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
    @Profiled
    public InstrumentDto getInstrumentByTicker(String ticker) {
        try {
            var finderInstr = investApi.getInstrumentsService().getInstrumentByTickerSync(ticker, "TQBR");
            return instrumentMapper.instrumentTinkoffToDto(finderInstr);
        } catch (ApiRuntimeException ex) {
            throw InstrumentException.notFoundByTicker(ticker);
        }
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
    @Profiled
    public BigDecimal getPriceByTicker(String ticker) {
        var figiByTicker = investApi.getInstrumentsService().getInstrumentByTickerSync(ticker, "TQBR").getFigi();
        var lastPrice = investApi.getMarketDataService().getLastPricesSync(Collections.singleton(figiByTicker));
        for (var price : lastPrice) {
            var figi = price.getFigi();
            var priceResult = quotationToBigDecimal(price.getPrice());
            var time = timestampToString(price.getTime());
            log.info("цены закрытия торговой сессии по инструменту {}, цена: {}, дата совершения торгов: {}",
                    figi, priceResult, time);
        }
        return quotationToBigDecimal(lastPrice.getFirst().getPrice());
    }

    @Override
    @Profiled
    public InstrumentsDto getInstrumentsByTickers(TickersDto tickers) {
        List<CompletableFuture<Instrument>> instruments = new ArrayList<>();
        tickers.getTickers()
                .forEach(ticker ->
                        instruments.add(investApi.getInstrumentsService().getInstrumentByTicker(ticker, "TQBR")));
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

    @Async
    public CompletableFuture<Instrument> getInstrumentAsync(String ticker) {
        return investApi.getInstrumentsService().getInstrumentByTicker(ticker, "TQBR");
    }
}
