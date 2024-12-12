package ru.kudryashov.moexservice.dto;

import ru.kudryashov.moexservice.model.Stock;
import lombok.*;

import java.util.List;

@Value
public class StocksDto {
    List<Stock> stocks;
}
