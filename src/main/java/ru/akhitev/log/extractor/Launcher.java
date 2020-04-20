package ru.akhitev.log.extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Arrays.*;
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
        stream(propertyManager.stringsToFind())
                .peek(currentStringToFind -> logger.info("Searching for: {}", currentStringToFind))
                .map(currentStringToFind -> currentStringToFind.split(STRINGS_TO_FIND_SEPARATOR))
                .forEach(this::processStringToFind);
    }

    private void processStringToFind(String[] stringsToFind) {
        fileManager.executeForOutputFile(stringsToFind,
                propertyManager.folderForExtractedFiles(),
                printWriter -> {
                    LogSearcher logSearcher;
                    logSearcher = new LogSearcher(fileManager.sortedLogFiles(), stringsToFind, printWriter, propertyManager.additionalLinesToPrint());
                    logSearcher.search();
                });
    }
}
