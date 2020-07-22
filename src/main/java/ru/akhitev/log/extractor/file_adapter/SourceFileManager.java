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

import ru.akhitev.log.extractor.business_logic.SourceManager;
import ru.akhitev.log.extractor.business_logic.entity.Source;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.stream.Collectors;

public class SourceFileManager implements SourceManager {
    private final Path logFilesPath ;
    private final LoggerFileNamesComparator fileNamesComparator;
    private Iterator<Path> sourceFilesIterator;


    public SourceFileManager(String logFilesPathString) {
        logFilesPath = validateAndConvertLogFilesPath(logFilesPathString);
        fileNamesComparator = new LoggerFileNamesComparator();
        resetIterator();
    }

    @Override
    public void resetIterator() {
        try {
            sourceFilesIterator = Files.list(logFilesPath)
                    .sorted(Comparator.comparingInt(p -> p.getFileName().toString().length()))
                    .sorted(fileNamesComparator::compare)
                    .collect(Collectors.toList()).iterator();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Source next() {
        Path sourceFilePath = sourceFilesIterator.next();
        Source source;
        try {
            source = new Source(sourceFilePath.getFileName().toString(), Files.lines(sourceFilePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return source;
    }

    @Override
    public boolean hasNext() {
        return sourceFilesIterator.hasNext();
    }

    private Path validateAndConvertLogFilesPath(String logFilesPathString) {
        if (logFilesPathString != null) {
            Path logFilesPath =  Paths.get(logFilesPathString);
            if (logFilesPath.toFile().isDirectory()) {
                return logFilesPath;
            }
        }
        // TODO: add possibility to use files
        throw new IllegalArgumentException("Path should be a directory", null);
    }
}
