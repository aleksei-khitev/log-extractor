package ru.akhitev.log.extractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class FileManager {
    private final Path logFilesPath;

    FileManager(String logFilesPathString) {
        this.logFilesPath = validateAndConvertLogFilesPath(logFilesPathString);
    }

    List<Path> sortedLogFiles() {
        try {
            return Files.list(logFilesPath)
                    .sorted(Comparator.comparingInt(p -> p.getFileName().toString().length()))
                    .sorted((p1, p2) -> new LoggerFileNamesComparator().compare(p1, p2))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
