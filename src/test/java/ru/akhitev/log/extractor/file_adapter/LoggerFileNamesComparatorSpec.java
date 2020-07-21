package ru.akhitev.log.extractor.file_adapter;

import org.junit.Before;
import org.junit.Test;
import ru.akhitev.log.extractor.file_adapter.LoggerFileNamesComparator;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class LoggerFileNamesComparatorSpec {
    private LoggerFileNamesComparator loggerFileNamesComparator;

    @Before
    public void setUp() {
        loggerFileNamesComparator = new LoggerFileNamesComparator();
    }

    @Test
    public void given_logFiles_when_differenceInLastNumber_then_theyComparedCorrectly() {
        Path log1 = Paths.get("some_log.2020-01-28.1.log");
        Path log2 = Paths.get("some_log.2020-01-28.2.log");
        int compare = loggerFileNamesComparator.compare(log1, log2);
        assertEquals(-1, compare);
    }

    @Test
    public void given_logFiles_when_oneHasEnding11AndAnoher101_then_theyComparedCorrectly() {
        Path log1 = Paths.get("some_log.2020-01-28.11.log");
        Path log2 = Paths.get("some_log.2020-01-28.101.log");
        int compare = loggerFileNamesComparator.compare(log1, log2);
        assertEquals(-1, compare);
    }

    @Test
    public void given_logFiles_when_firstIsShortAndAnotherWithNumber_then_shortLast() {
        Path log1 = Paths.get("some_log.log");
        Path log2 = Paths.get("some_log.2020-01-28.2.log");
        int compare = loggerFileNamesComparator.compare(log1, log2);
        assertEquals(1, compare);
    }

    @Test
    public void given_logFiles_when_firstWithNumberAndAnotherIsShort_then_shortLast() {
        Path log1 = Paths.get("some_log.2020-01-28.2.log");
        Path log2 = Paths.get("some_log.log");
        int compare = loggerFileNamesComparator.compare(log1, log2);
        assertEquals(-1, compare);
    }
}
