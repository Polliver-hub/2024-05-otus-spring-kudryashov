package ru.kudryashov.userportfoliosservice.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import ru.kudryashov.userportfoliosservice.clients.TinkoffServiceClient;
import ru.kudryashov.userportfoliosservice.dto.InstrumentDto;

@RestController
@RequiredArgsConstructor
public class InstrumentController {

    private final TinkoffServiceClient tinkoffClient;

    @GetMapping("/api/v1/instruments/{ticker}")
    public InstrumentDto getInstrumentByTicker(@PathVariable String ticker) {
        var response = tinkoffClient.getInstrumentByTicker(ticker);
        InstrumentDto instrument = null;
        if (response.getStatusCode().is2xxSuccessful()) {
            instrument = response.getBody();
        }
        return instrument;
    }
}
