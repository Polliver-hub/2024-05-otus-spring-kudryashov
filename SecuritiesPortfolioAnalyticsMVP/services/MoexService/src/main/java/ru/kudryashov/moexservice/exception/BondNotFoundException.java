package ru.kudryashov.moexservice.exception;

public class BondNotFoundException extends RuntimeException {
        public BondNotFoundException(String m) {
            super(m);
        }
    }