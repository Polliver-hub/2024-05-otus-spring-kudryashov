package ru.kudryashov.tinkoffservice.dto;

import lombok.Value;

import java.util.List;

@Value
public class InstrumentsDto {
    List<InstrumentDto> instruments;
}
