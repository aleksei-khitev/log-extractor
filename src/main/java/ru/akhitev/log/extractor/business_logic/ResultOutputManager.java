package ru.akhitev.log.extractor.business_logic;

public interface ResultOutputManager {
    void startNewTargetOutput(String targetStringName);
    void write(String line);
    void writeSourceName(String sourceName);
    void writeFindingSeparator();
    void writeSourceTerminator();
}
