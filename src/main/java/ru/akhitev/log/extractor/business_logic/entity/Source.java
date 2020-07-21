package ru.akhitev.log.extractor.business_logic.entity;

import java.util.stream.Stream;

public class Source {
    private final String name;
    private final Stream<String> lines;

    public Source(String name, Stream<String> lines) {
        this.name = name;
        this.lines = lines;
    }

    public String name() {
        return name;
    }

    public Stream<String> lines() {
        return lines;
    }
}
