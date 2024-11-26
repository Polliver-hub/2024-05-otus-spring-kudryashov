package ru.kudryashov.tinkoffservice.mappers;

import org.springframework.stereotype.Component;
import ru.kudryashov.tinkoffservice.dto.InstrumentDto;
import ru.kudryashov.tinkoffservice.models.Currency;
import ru.tinkoff.piapi.contract.v1.Asset;
import ru.tinkoff.piapi.contract.v1.Instrument;
import ru.tinkoff.piapi.contract.v1.InstrumentShort;
import ru.tinkoff.piapi.contract.v1.Share;

import java.util.ArrayList;
import java.util.List;

@Component
public class InstrumentMapper {

    public InstrumentDto instrumentTinkoffToDto(Instrument instrument) {
        return InstrumentDto.builder()
                .ticker(instrument.getTicker())
                .type(instrument.getInstrumentType())
                .name(instrument.getName())
                .figi(instrument.getFigi())
                .classCode(instrument.getClassCode())
                .currency(Currency.valueOf(instrument.getCurrency().toUpperCase()))
                .build();
    }

    public InstrumentDto shareTinkoffToDto(Share share) {
        return InstrumentDto.builder()
                .ticker(share.getTicker())
                .figi(share.getFigi())
                .name(share.getName())
                .type(share.getShareType().name())
                .classCode(share.getClassCode())
                .currency(Currency.valueOf(share.getCurrency().toUpperCase()))
                .build();
    }

    public InstrumentDto instrumentShortToDto(InstrumentShort instrumentShort) {
        return InstrumentDto.builder()
                .ticker(instrumentShort.getTicker())
                .figi(instrumentShort.getFigi())
                .name(instrumentShort.getName())
                .type(instrumentShort.getInstrumentType())
                .classCode(instrumentShort.getClassCode())
                .currency(null)
                .build();
    }

    public List<InstrumentDto> assetToDto(Asset asset) {
        List<InstrumentDto> instrumentDtoList = new ArrayList<>();

        asset.getInstrumentsList().forEach(assetInstrument ->
                instrumentDtoList.add(InstrumentDto.builder()
                        .name(asset.getName())
                        .ticker(assetInstrument.getTicker())
                        .figi(assetInstrument.getFigi())
                        .type(assetInstrument.getInstrumentType())
                        .classCode(assetInstrument.getClassCode())
                        .currency(null)
                        .build()));
        return instrumentDtoList;
    }
}
