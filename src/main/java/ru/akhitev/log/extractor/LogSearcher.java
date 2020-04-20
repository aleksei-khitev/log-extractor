package ru.akhitev.log.extractor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.function.Consumer;

class LogSearcher {
    private final List<Path> logFiles;
    private final String[] stringsToSearch;
    private int additionalLinesToPrint;
    private int additionalLinesToPrintCounter;
    private final PrintWriter printWriter;

    LogSearcher(List<Path> logFiles, String[] stringsToSearch, PrintWriter printWriter, int additionalLinesToPrint) {
        this(logFiles, stringsToSearch, printWriter);
        this.additionalLinesToPrint = additionalLinesToPrint;
    }

    private LogSearcher(List<Path> logFiles, String[] stringsToSearch, PrintWriter printWriter) {
        this.logFiles = logFiles;
        this.stringsToSearch = stringsToSearch;
        this.printWriter = printWriter;
    }

    void search() {
        if (additionalLinesToPrint == 0) {
            iterateAndSearch(this::printJustFoundLines);
        } else {
            iterateAndSearch(this::printFoundAndAdditionalLines);
        }

    }

    private void iterateAndSearch(Consumer<String> printConsumer) {
        for (Path currentLogFile : logFiles) {
            printLines(currentLogFile, printConsumer);
        }
    }

    private void printLines(Path currentLogFile, Consumer<String> printConsumer) {
        try {
            printWriter.println(currentLogFile.getFileName().toString());
            printWriter.println("--------------------------------------");
            Files.lines(currentLogFile)
                    .forEach(printConsumer);
            printWriter.println("======================================");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void printJustFoundLines(String line) {
        if (isFound(line, stringsToSearch)) {
            printWriter.println(line);
            printWriter.println("...");
        }
    }

    private void printFoundAndAdditionalLines(String line) {
        if (isFound(line, stringsToSearch)) {
            printWriter.println(line);
            resetAdditionalLinesToPrintCounter();
        } else {
            int additionalLinesToPrintCounter = getAndDecrementAdditionalLinesToPrintCounter();
            if (additionalLinesToPrintCounter != 0) {
                printWriter.println(line);
                if (additionalLinesToPrintCounter == 1) {
                    printWriter.println("...");
                }
            }
        }
    }

    private boolean isFound(String line, String[] stringsToSearch) {
        for (String stringToSearch : stringsToSearch) {
            if (line.contains(stringToSearch)) {
                return true;
            }
        }
        return false;
    }

    private void resetAdditionalLinesToPrintCounter() {
        this.additionalLinesToPrintCounter = additionalLinesToPrint;
    }

    private int getAndDecrementAdditionalLinesToPrintCounter() {
        if (additionalLinesToPrintCounter == 0) {
            return additionalLinesToPrintCounter;
        } else {
            return additionalLinesToPrintCounter--;
        }
    }
}
