package ru.kudryashov.userportfoliosservice.dto;

import lombok.Value;

@Value
public class PositionDto {

    String ticker;

    Integer quantity;
}
