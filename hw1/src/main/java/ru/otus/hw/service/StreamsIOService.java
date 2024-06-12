package ru.otus.hw.service;

public class StreamsIOService implements IOService {

    @Override
    public void printLine(String s) {
        System.out.println(s);
    }

    @Override
    public void printFormattedLine(String s, Object... args) {
        System.out.printf(s + "%n", args);
    }
}
