package ru.akhitev.log.extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

import static ru.akhitev.log.extractor.PropertyManager.STRINGS_TO_FIND_SEPARATOR;

public class Launcher {
    private static Logger logger = LoggerFactory.getLogger(Launcher.class);
    private final PropertyManager propertyManager;
    private FileManager fileManager;

    private Launcher(String[] args) {
        if (args != null && args.length > 0) {
            propertyManager = new PropertyManager(args[0]);
        } else {
            propertyManager = new PropertyManager();
        }
        fileManager = new FileManager(propertyManager.logFilesPathString());
    }

    public static void main(String[] args) {
        Launcher launcher = new Launcher(args);
        launcher.launch();
        logger.info("Process finished.");
    }

    private void launch() {
        for (String currentStringToFind : propertyManager.stringsToFind()) {
            String [] stringsToFind = currentStringToFind.split(STRINGS_TO_FIND_SEPARATOR);
            logger.info("Searching for: {}", currentStringToFind);
            processStringToFind(stringsToFind);
        }
    }

    private void processStringToFind(String[] stringsToFind) {
        if (!Files.exists(Paths.get(propertyManager.folderForExtractedFiles()))) {
            try {
                Files.createDirectories(Paths.get(propertyManager.folderForExtractedFiles()));
            } catch (IOException e) {
                logger.error("Error while creating paths directories", e);
            }
        }
        String fileName = String.format("%s/%s.txt", propertyManager.folderForExtractedFiles(), stringsToFind[0]);
        if (new File(fileName).exists()) {
            if (!new File(fileName).delete()) {
                throw new RuntimeException("Cannot delete " + fileName, null);
            }
        }
        try (FileWriter fileWriter = new FileWriter(fileName);
             BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
             PrintWriter printWriter = new PrintWriter(bufferedWriter)) {
            LogSearcher logSearcher;
            logSearcher = new LogSearcher(fileManager.sortedLogFiles(), stringsToFind, printWriter, propertyManager.additionalLinesToPrint());
            logSearcher.search();
        } catch (IOException e) {
            logger.error("Error at file operation", e);
        }
    }
}
