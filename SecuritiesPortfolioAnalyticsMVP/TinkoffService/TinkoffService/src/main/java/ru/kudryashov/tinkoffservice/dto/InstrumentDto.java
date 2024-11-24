package ru.kudryashov.tinkoffservice.dto;

import lombok.Builder;
import lombok.Value;
import ru.kudryashov.tinkoffservice.constants.CommonConstants;
import ru.kudryashov.tinkoffservice.models.Currency;

@Value
@Builder
public class InstrumentDto {

    String ticker;

    String figi;

    String name;

    String type;

    String classCode;

    Currency currency;

    String source = CommonConstants.SOURCE_API;
}