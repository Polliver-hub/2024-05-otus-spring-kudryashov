package ru.kudryashov.tinkoffservice.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.kudryashov.tinkoffservice.dto.FigisDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentPriceDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentsDto;
import ru.kudryashov.tinkoffservice.dto.InstrumentsPricesDto;
import ru.kudryashov.tinkoffservice.dto.TickersDto;
import ru.kudryashov.tinkoffservice.services.InstrumentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class InstrumentController {

    private final InstrumentService instrumentService;

    @GetMapping("/api/v1/instruments/tickers/{ticker}")
    public ResponseEntity<InstrumentDto> getInstrumentByTicker(@PathVariable String ticker) {
        return ResponseEntity.ok(instrumentService.getInstrumentByTicker(ticker));
    }

    @GetMapping("/api/v1/classCodes")
    public ResponseEntity<List<String>> getAllClassCodesTinkoffApi() {
        return ResponseEntity.ok(instrumentService.getAllClassCodes());
    }

    @GetMapping("/api/v1/instruments/figis/{figi}")
    public ResponseEntity<InstrumentDto> getInstrumentByFigi(@PathVariable String figi) {
        return ResponseEntity.ok(instrumentService.getInstrumentByFigi(figi));
    }

    @GetMapping("/api/v1/instruments/price/ticker/{ticker}")
    public ResponseEntity<InstrumentPriceDto> getInstrumentPriceByTicker(@PathVariable String ticker) {
        return ResponseEntity.ok(instrumentService.getPriceByTicker(ticker));
    }

    @GetMapping("/api/v1/instruments/price/figi/{figi}")
    public ResponseEntity<InstrumentPriceDto> getInstrumentPriceByFigi(@PathVariable String figi) {
        return ResponseEntity.ok(instrumentService.getPriceByFigi(figi));
    }

    @GetMapping("/api/v1/instruments/stocks")
    public ResponseEntity<InstrumentsDto> getAllStocks(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        // Проверка входных параметров
        if (page < 0 || size <= 0) {
            return ResponseEntity.badRequest().body(null); // Возвращаем ошибку 400, если параметры некорректны
        }
        return ResponseEntity.ok(instrumentService.getAllStocks(page, size));
    }

    @PutMapping("/api/v1/instruments/tickers")
    public ResponseEntity<InstrumentsDto> getInstrumentsByTickers(@RequestBody TickersDto tickers) {
        return ResponseEntity.ok(instrumentService.getInstrumentsByTickers(tickers));
    }

    @PutMapping("/api/v1/instruments/figis")
    public ResponseEntity<InstrumentsDto> getInstrumentsByFigis(@RequestBody FigisDto figis) {
        return ResponseEntity.ok(instrumentService.getInstrumentsByFigis(figis));
    }

    @PutMapping("/api/v1/instruments/prices/figi")
    public ResponseEntity<InstrumentsPricesDto> getInstrumentsPricesByFigis(@RequestBody FigisDto figis) {
        return ResponseEntity.ok(instrumentService.getPricesByFigis(figis));
    }

    @GetMapping("/api/v1/instruments/{id}")
    public ResponseEntity<InstrumentsDto> getInstrumentsById(@PathVariable String id) {
        return ResponseEntity.ok(instrumentService.getInstrumentsById(id));
    }

    @GetMapping("/api/v1/instruments")
    public ResponseEntity<InstrumentsDto> getAllInstruments(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(instrumentService.getAllInstruments(page, size));
    }

}
