package ru.kudryashov.moexservice.service;

import ru.kudryashov.moexservice.dto.BondDto;
import ru.kudryashov.moexservice.dto.FigiesDto;
import ru.kudryashov.moexservice.dto.StockPrice;
import ru.kudryashov.moexservice.dto.StocksDto;
import ru.kudryashov.moexservice.dto.StocksPricesDto;
import ru.kudryashov.moexservice.dto.TickersDto;
import ru.kudryashov.moexservice.exception.BondNotFoundException;
import ru.kudryashov.moexservice.model.Currency;
import ru.kudryashov.moexservice.model.Stock;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.kudryashov.moexservice.moexclient.MoexApiClient;
import ru.kudryashov.moexservice.parser.Parser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class BondService {
    private final MoexApiClient moexClient;
    private final BondRepository bondRepository;
    private final Parser parser;


    public List<BondDto> getShareByTicker(String ticker) {
        String resopnseMoex = moexClient.getShareByTicker(ticker);
        log.info(resopnseMoex);
        return parser.parse(resopnseMoex);
    }

    public List<BondDto> getInstrumentByTiker(String ticker) {
        String resopnseMoex = moexClient.getInstrumentByTicker(ticker);
        log.info(resopnseMoex);
        return parser.parse(resopnseMoex);
    }

    public String getMarkents() {
        return moexClient.getMarkents();
    }

    public String getColumnsInfo() {
        return moexClient.getInfoAboutColumns();
    }

    public String getBoards() {
        return moexClient.getBoards();
    }

    public StocksDto getBondsFromMoex(TickersDto tickersDto) {
        log.info("Request for tickers {}", tickersDto.getTickers());
        List<String> tickers = new ArrayList<>(tickersDto.getTickers());

//        log.info("!!!" + moexClient.getAnySecurityGroup());

        List<BondDto> allBonds = new ArrayList<>();
        allBonds.addAll(bondRepository.getGovBonds());
        allBonds.addAll(bondRepository.getCorporateBonds());
        List<BondDto> resultBonds = allBonds.stream()
                .filter(b -> tickers.contains(b.getTicker()))
                .toList();

        List<Stock> stocks = resultBonds.stream().map(b -> {
            return Stock.builder()
                    .ticker(b.getTicker())
                    .name(b.getName())
                    .figi(b.getTicker())
                    .type("Bond")
                    .currency(Currency.RUB)
                    .source("MOEX")
                    .build();
        }).toList();
        return new StocksDto(stocks);
    }

    public StocksPricesDto getPricesByFigies(FigiesDto figiesDto) {
        log.info("Request for figies {}", figiesDto.getFigies());
        List<String> figies = new ArrayList<>(figiesDto.getFigies());
        List<BondDto> allBonds = new ArrayList<>();
        allBonds.addAll(bondRepository.getGovBonds());
        allBonds.addAll(bondRepository.getCorporateBonds());
        figies.removeAll(allBonds.stream().map(b -> b.getTicker()).collect(Collectors.toList()));
        if (!figies.isEmpty()) {
            throw new BondNotFoundException(String.format("Bonds %s not found.", figies));
        }
        List<StockPrice> prices = allBonds.stream()
                .filter(b -> figiesDto.getFigies().contains(b.getTicker()))
                .map(b -> new StockPrice(b.getTicker(), b.getPrice() * 10))
                .collect(Collectors.toList());
        return new StocksPricesDto(prices);
    }


}
