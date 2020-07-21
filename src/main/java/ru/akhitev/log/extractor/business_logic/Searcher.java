package ru.akhitev.log.extractor.business_logic;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.akhitev.log.extractor.business_logic.entity.Source;
import ru.akhitev.log.extractor.business_logic.entity.TargetString;

import java.util.List;
import java.util.function.BiConsumer;

public class Searcher {
    private static Logger logger = LoggerFactory.getLogger(Searcher.class);
    private final SettingsManager settingsManager;
    private final SourceManager sourceManager;
    private final ResultOutputManager resultOutputManager;
    private int additionalLinesToPrintCounter;

    public Searcher(SettingsManager settingsManager, SourceManager sourceManager, ResultOutputManager resultOutputManager) {
        this.settingsManager = settingsManager;
        this.sourceManager = sourceManager;
        this.resultOutputManager = resultOutputManager;
    }

    public void search() {
        // TODO: parallel
        settingsManager.targetStrings()
                .stream()
                .peek(targetString -> logger.info("Searching for: {} with waysToWriting: {}", targetString.getName(), targetString.getWaysToWriting()))
                .forEach(this::processStringToFind);
    }

    private void processStringToFind(TargetString targetString) {
        sourceManager.resetIterator();
        resultOutputManager.startNewTargetOutput(targetString.getName());
        if (settingsManager.additionalLinesToPrint() == null || settingsManager.additionalLinesToPrint() < 1) {
            iterateAndSearch(this::printJustFoundLines, targetString);
        } else {
            iterateAndSearch(this::printFoundAndAdditionalLines, targetString);
        }
    }

    private void iterateAndSearch(BiConsumer<String, List<String>> searchAndPrintConsumer, TargetString targetString) {
        while(sourceManager.hasNext()) {
            printLines(sourceManager.next(), searchAndPrintConsumer, targetString);
        }
    }

    private void printLines(Source source, BiConsumer<String, List<String>> searchAndPrintConsumer, TargetString targetString) {
        resultOutputManager.writeSourceName(source.name());
        source.lines().forEach(line -> searchAndPrintConsumer.accept(line, targetString.getWaysToWriting()));
        resultOutputManager.writeSourceTerminator();
    }

    private void printJustFoundLines(String line, List<String> waysToWriting) {
        if (isFound(line, waysToWriting)) {
            resultOutputManager.write(line);
            resultOutputManager.writeFindingSeparator();
        }
    }

    private void printFoundAndAdditionalLines(String line, List<String> waysToWriting) {
        if (isFound(line, waysToWriting)) {
            resultOutputManager.write(line);
            resetAdditionalLinesToPrintCounter();
        } else {
            int additionalLinesToPrintCounter = getAndDecrementAdditionalLinesToPrintCounter();
            if (additionalLinesToPrintCounter != 0) {
                resultOutputManager.write(line);
                if (additionalLinesToPrintCounter == 1) {
                    resultOutputManager.writeFindingSeparator();
                }
            }
        }
    }

    // TODO: could be optimized?
    private boolean isFound(String line, List<String> stringsToSearch) {
        for (String stringToSearch : stringsToSearch) {
            if (line.contains(stringToSearch)) {
                return true;
            }
        }
        return false;
    }

    private void resetAdditionalLinesToPrintCounter() {
        this.additionalLinesToPrintCounter = settingsManager.additionalLinesToPrint();
    }

    private int getAndDecrementAdditionalLinesToPrintCounter() {
        if (additionalLinesToPrintCounter == 0) {
            return additionalLinesToPrintCounter;
        } else {
            return additionalLinesToPrintCounter--;
        }
    }
}
