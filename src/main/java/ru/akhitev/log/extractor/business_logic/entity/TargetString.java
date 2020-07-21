package ru.akhitev.log.extractor.business_logic.entity;

import java.util.List;

public class TargetString {
    private final String name;
    private final List<String> waysToWriting;

    public TargetString(String name, List<String> waysToWriting) {
        this.name = name;
        this.waysToWriting = waysToWriting;
    }

    public String getName() {
        return name;
    }

    public List<String> getWaysToWriting() {
        return waysToWriting;
    }
}
