package ru.kudryashov.userportfoliosservice.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kudryashov.userportfoliosservice.dto.InstrumentDto;

@FeignClient(name = "TinkoffServiceClient", url = "${spring.feign.clients.tinkoff-service.url}")
public interface TinkoffServiceClient {

    @GetMapping("/instruments/tickers/{ticker}")
    ResponseEntity<InstrumentDto> getInstrumentByTicker(@PathVariable("ticker") String ticker);
}
