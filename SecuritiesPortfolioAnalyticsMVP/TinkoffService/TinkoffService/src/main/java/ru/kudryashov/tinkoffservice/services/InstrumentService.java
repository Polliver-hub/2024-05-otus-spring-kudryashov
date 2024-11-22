package ru.kudryashov.tinkoffservice.services;

import ru.kudryashov.tinkoffservice.dto.InstrumentDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentsDto;
import ru.kudryashov.tinkoffservice.dto.TickersDto;

import java.math.BigDecimal;
import java.util.List;

public interface InstrumentService {
    InstrumentsDto getAllStocks(int page, int size);

    InstrumentsDto getAllInstruments(int page, int size);

    List<String> getAllClassCodes();

    InstrumentsDto getInstrumentsById(String id);

    InstrumentDto getInstrumentByTicker(String ticker);

    InstrumentDto getInstrumentByFigi(String figi);

    BigDecimal getPriceByTicker(String ticker);

    InstrumentsDto getInstrumentsByTickers(TickersDto tickers);
}
