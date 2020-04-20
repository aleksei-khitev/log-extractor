package ru.akhitev.log.extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

class FileManager {
    private static Logger logger = LoggerFactory.getLogger(FileManager.class);
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

    void executeForOutputFile(String[] stringsToFind, String outputFolder, Consumer<PrintWriter> consumer) {
        String fileName = String.format("%s/%s.txt", outputFolder, stringsToFind[0]);
        try {
            createFolderIfNotExist(outputFolder);
            removeFileIfExist(fileName);
            try (FileWriter fileWriter = new FileWriter(fileName);
                 BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                 PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
                consumer.accept(printWriter);
            } catch (IOException e) {
                logger.error("Error at file operation", e);
            }
        } catch (IOException e) {
            logger.error("Error at file operation", e);
        }
    }

    private void createFolderIfNotExist(String outputFolder) throws IOException {
        if (!Files.exists(Paths.get(outputFolder))) {
            Files.createDirectories(Paths.get(outputFolder));
        }
    }

    private void removeFileIfExist(String fileName) {
        if (new File(fileName).exists()) {
            if (!new File(fileName).delete()) {
                throw new RuntimeException("Cannot delete " + fileName, null);
            }
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
