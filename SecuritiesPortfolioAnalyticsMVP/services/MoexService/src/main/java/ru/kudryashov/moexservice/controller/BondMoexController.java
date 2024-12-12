package ru.kudryashov.moexservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kudryashov.moexservice.dto.BondDto;
import ru.kudryashov.moexservice.dto.FigiesDto;
import ru.kudryashov.moexservice.dto.StocksDto;
import ru.kudryashov.moexservice.dto.StocksPricesDto;
import ru.kudryashov.moexservice.dto.TickersDto;
import ru.kudryashov.moexservice.service.BondService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bonds")
@RequiredArgsConstructor
public class BondMoexController {
    private final BondService bondService;

    @GetMapping("/share/{ticker}")
    public List<BondDto> getShareByTiker(@PathVariable String ticker) {
        return bondService.getShareByTicker(ticker);
    }

    @GetMapping("/instrument/{ticker}")
    public List<BondDto> getInstrumentByTiker(@PathVariable String ticker) {
        return bondService.getInstrumentByTiker(ticker);
    }

    @GetMapping("/markets")
    public String getInfoMarkets() {
        return bondService.getMarkents();
    }

    @GetMapping("/boards")
    public String getBoards() {
        return bondService.getBoards();
    }

    @GetMapping("/columns")
    public String getColumnsInfo() {
        return bondService.getColumnsInfo();
    }


    @PostMapping("/")
    public StocksDto getBondsFromMoex(@RequestBody TickersDto tickersDto) {
        return bondService.getBondsFromMoex(tickersDto);
    }

    @PostMapping("/prices")
    public StocksPricesDto getPricesByFigies(@RequestBody FigiesDto figiesDto) {
        return bondService.getPricesByFigies(figiesDto);
    }
}
