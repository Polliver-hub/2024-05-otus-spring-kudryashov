package ru.kudryashov.moexservice.parser;


import ru.kudryashov.moexservice.dto.BondDto;

import java.util.List;

public interface Parser {
    List<BondDto> parse(String ratesAsString);
}
