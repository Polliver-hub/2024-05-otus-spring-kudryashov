package ru.kudryashov.userportfoliosservice.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class InstrumentDto {

    String ticker;

    String figi;

    String name;

    String type;

    String classCode;

    String currency;

    String source;
}
