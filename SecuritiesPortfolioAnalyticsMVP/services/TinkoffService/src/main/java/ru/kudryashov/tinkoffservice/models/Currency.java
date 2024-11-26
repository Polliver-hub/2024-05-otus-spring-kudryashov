package ru.kudryashov.tinkoffservice.models;

public enum Currency {
    RUB("RUB"),
    USD("USD"),
    EUR("EUR"),
    GBP("GBP"),
    HKD("HKD"),
    CHF("CHF"),
    JPY("JPY"),
    CNY("CNY"),
    TRY("TRY"),
    NOK("NOK"),
    KZT("KZT"),
    SEK("SEK");

    private String currency;

    Currency(String currency) {
        this.currency = currency;
    }
}
