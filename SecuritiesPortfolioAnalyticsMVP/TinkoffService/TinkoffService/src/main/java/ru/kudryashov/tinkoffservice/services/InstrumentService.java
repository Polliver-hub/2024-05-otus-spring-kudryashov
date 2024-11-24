package ru.kudryashov.tinkoffservice.services;

import ru.kudryashov.tinkoffservice.dto.FigisDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentPriceDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentsDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentsPricesDto;
import ru.kudryashov.tinkoffservice.dto.TickersDto;

import java.util.List;

public interface InstrumentService {
    InstrumentsDto getAllStocks(int page, int size);

    InstrumentsDto getAllInstruments(int page, int size);

    List<String> getAllClassCodes();

    InstrumentsDto getInstrumentsById(String id);

    InstrumentDto getInstrumentByTicker(String ticker);

    InstrumentDto getInstrumentByFigi(String figi);

    InstrumentsDto getInstrumentsByFigis(FigisDto figis);

    InstrumentPriceDto getPriceByTicker(String ticker);

    InstrumentPriceDto getPriceByFigi(String figi);

    InstrumentsPricesDto getPricesByFigis(FigisDto figis);

    InstrumentsDto getInstrumentsByTickers(TickersDto tickers);
}
