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
