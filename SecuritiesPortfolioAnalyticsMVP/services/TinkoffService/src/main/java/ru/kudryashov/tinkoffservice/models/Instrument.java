package ru.kudryashov.tinkoffservice.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import ru.kudryashov.tinkoffservice.constants.CommonConstants;

@Value
@AllArgsConstructor
@Builder
public class Instrument {

    String ticker;

    String figi;

    String name;

    String type;

    Currency currency;

    String source = CommonConstants.SOURCE_API;
}
