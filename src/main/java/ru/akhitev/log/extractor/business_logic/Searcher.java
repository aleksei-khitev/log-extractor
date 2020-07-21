/*
 * Log Extractor is an organizer for a developer and other IT-specialists.
 * Copyright (c) 2017 Aleksei Khitev (Хитёв Алексей Юрьевич).
 *
 * This file is part of IT-Organizer
 *
 * Log Extractor is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * Log Extractor is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Foobar.  If not, see <http://www.gnu.org/licenses/>.
 */
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
