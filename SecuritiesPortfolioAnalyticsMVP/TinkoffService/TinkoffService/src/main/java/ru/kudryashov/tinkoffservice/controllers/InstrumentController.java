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

    @GetMapping("/instruments/tickers/{ticker}")
    public ResponseEntity<InstrumentDto> getInstrumentByTicker(@PathVariable String ticker) {
        return ResponseEntity.ok(instrumentService.getInstrumentByTicker(ticker));
    }

    @GetMapping("/classCodes")
    public ResponseEntity<List<String>> getAllClassCodesTinkoffApi() {
        return ResponseEntity.ok(instrumentService.getAllClassCodes());
    }

    @GetMapping("/instruments/figis/{figi}")
    public ResponseEntity<InstrumentDto> getInstrumentByFigi(@PathVariable String figi) {
        return ResponseEntity.ok(instrumentService.getInstrumentByFigi(figi));
    }

    @GetMapping("/instruments/price/ticker/{ticker}")
    public ResponseEntity<InstrumentPriceDto> getInstrumentPriceByTicker(@PathVariable String ticker) {
        return ResponseEntity.ok(instrumentService.getPriceByTicker(ticker));
    }

    @GetMapping("/instruments/price/figi/{figi}")
    public ResponseEntity<InstrumentPriceDto> getInstrumentPriceByFigi(@PathVariable String figi) {
        return ResponseEntity.ok(instrumentService.getPriceByFigi(figi));
    }

    @GetMapping("/instruments/stocks")
    public ResponseEntity<InstrumentsDto> getAllStocks(@RequestParam(defaultValue = "0") int page,
                                                       @RequestParam(defaultValue = "10") int size) {
        // Проверка входных параметров
        if (page < 0 || size <= 0) {
            return ResponseEntity.badRequest().body(null); // Возвращаем ошибку 400, если параметры некорректны
        }
        return ResponseEntity.ok(instrumentService.getAllStocks(page, size));
    }

    @PutMapping("/instruments/ticker")
    public ResponseEntity<InstrumentsDto> getInstrumentsByTickers(@RequestBody TickersDto tickers) {
        return ResponseEntity.ok(instrumentService.getInstrumentsByTickers(tickers));
    }

    @PutMapping("/instruments/figi")
    public ResponseEntity<InstrumentsDto> getInstrumentsByFigis(@RequestBody FigisDto figis) {
        return ResponseEntity.ok(instrumentService.getInstrumentsByFigis(figis));
    }

    @PutMapping("/instruments/price/figi")
    public ResponseEntity<InstrumentsPricesDto> getInstrumentsPricesByFigis(@RequestBody FigisDto figis) {
        return ResponseEntity.ok(instrumentService.getPricesByFigis(figis));
    }

    @GetMapping("/instruments/{id}")
    public ResponseEntity<InstrumentsDto> getInstrumentsById(@PathVariable String id) {
        return ResponseEntity.ok(instrumentService.getInstrumentsById(id));
    }

    @GetMapping("/instruments")
    public ResponseEntity<InstrumentsDto> getAllInstruments(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(instrumentService.getAllInstruments(page, size));
    }

}