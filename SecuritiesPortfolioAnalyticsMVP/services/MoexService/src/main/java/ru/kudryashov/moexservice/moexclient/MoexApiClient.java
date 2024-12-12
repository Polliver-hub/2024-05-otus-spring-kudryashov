package ru.kudryashov.moexservice.moexclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "moexclient", url = "${moex.url}", configuration = FeignConfig.class)
public interface MoexApiClient {

    @GetMapping("iss/engines/stock/markets/bonds/boards/TQCB/" +
            "securities.xml?iss.meta=off&iss.only=securities&securities.columns=SECID,PREVADMITTEDQUOTE,SHORTNAME")
    String getCorporateBondsFromMoex();

    @GetMapping("iss/engines/stock/markets/bonds/boards/TQOB/securities.xml?iss.meta=off&iss.only=securities")
    String getGovermentBondsFromMoex();

    @GetMapping("iss/engines/stock/markets/shares/securities/{ticker}.xml?iss.meta=off&iss.only=securities")
    String getShareByTicker(@PathVariable String ticker);

    @GetMapping("/iss/engines/stock/markets")
    String getMarkents();

    @GetMapping("/iss/engines/stock/markets/bonds/boards.xml")
    String getBoards();

    @GetMapping("iss/securities/{ticker}.xml")
    String getInstrumentByTicker(@PathVariable String ticker);

//    @GetMapping("iss/engines/stock/markets/shares/securities/columns.xml")
//    String getInfoAboutColumns();

    @GetMapping("iss/engines/stock/markets/shares/securities/columns.xml")
    String getInfoAboutColumns();
}
