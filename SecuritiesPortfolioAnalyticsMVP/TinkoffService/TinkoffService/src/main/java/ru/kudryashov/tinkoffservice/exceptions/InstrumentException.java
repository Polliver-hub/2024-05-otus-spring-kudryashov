package ru.kudryashov.tinkoffservice.exceptions;

import ru.kudryashov.tinkoffservice.constants.ErrorConstants;

public class InstrumentException extends RuntimeException {

    private InstrumentException(String message) {
        super(message);
    }

    public static InstrumentException notFoundByTicker(String ticker) {
        return new InstrumentException(String.format(ErrorConstants.INSTRUMENT_NOT_FOUND_BY_TICKER, ticker));
    }

    public static InstrumentException notFoundByFigi(String figi) {
        return new InstrumentException(String.format(ErrorConstants.INSTRUMENT_NOT_FOUND_BY_FIGI, figi));
    }
}
