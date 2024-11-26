package ru.kudryashov.tinkoffservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class InstrumentsPricesDto {

    private List<InstrumentPriceDto> prices;

}
