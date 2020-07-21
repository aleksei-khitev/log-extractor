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
