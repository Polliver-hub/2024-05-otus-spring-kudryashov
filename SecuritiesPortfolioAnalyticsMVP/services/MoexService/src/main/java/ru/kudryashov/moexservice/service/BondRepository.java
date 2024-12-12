package ru.kudryashov.moexservice.service;

import ru.kudryashov.moexservice.dto.BondDto;
import ru.kudryashov.moexservice.exception.LimitRequestsException;
import ru.kudryashov.moexservice.moexclient.MoexApiClient;
import ru.kudryashov.moexservice.parser.Parser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BondRepository {
    private final MoexApiClient moexApiClient;

    private final Parser bondsParser;

    @Cacheable(value = "corps")
    public List<BondDto> getCorporateBonds() {
        log.info("Getting corporate bonds from Moex");
        String xmlFromMoex = moexApiClient.getCorporateBondsFromMoex();
        List<BondDto> bonds = bondsParser.parse(xmlFromMoex);
        if (bonds.isEmpty()) {
            log.error("Moex isn't answering for getting corporate bonds.");
            throw new LimitRequestsException("Moex isn't answering for getting corporate bonds.");
        }
        return bonds;
    }

    @Cacheable(value = "govs")
    public List<BondDto> getGovBonds() {
        log.info("Getting government bonds from Moex");
        String xmlFromMoex = moexApiClient.getGovermentBondsFromMoex();
        List<BondDto> bonds = bondsParser.parse(xmlFromMoex);
        if (bonds.isEmpty()) {
            log.error("Moex isn't answering for getting government bonds.");
            throw new LimitRequestsException("Moex isn't answering for getting government bonds.");
        }
        return bonds;
    }
}
