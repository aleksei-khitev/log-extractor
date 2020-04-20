package ru.akhitev.log.extractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.Properties;

class PropertyManager {
    private static final String DEFAULT_PROPERTIES_FILE = "extractor.properties";
    private static final String STRING_ARRAYS_TO_FIND_SEPARATOR = ";:;";
    static final String STRINGS_TO_FIND_SEPARATOR = ";;";
    private static Logger logger = LoggerFactory.getLogger(PropertyManager.class);
    private final Properties properties;

    PropertyManager() {
        properties = loadProperties(new File(DEFAULT_PROPERTIES_FILE));
    }

    PropertyManager(String filePath) {
        properties = loadProperties(new File(filePath));
    }

    String logFilesPathString()  {
        return properties.getProperty("folder.with.log.files");
    }

    String[] stringsToFind() {
        return properties.getProperty("strings.to.find").split(STRING_ARRAYS_TO_FIND_SEPARATOR);
    }

    String folderForExtractedFiles() {
        return properties.getProperty("folder.for.extraction.files");
    }

    Integer additionalLinesToPrint() {
        return Integer.parseInt(properties.getProperty("additional.lines.to.print"));
    }

    private Properties loadProperties(File propertiesFile) {
        Properties properties = new Properties();
        try (Reader propertyReader = new FileReader(propertiesFile)) {
            properties.load(propertyReader);
        } catch (Throwable t) {
            logger.error("Error at properties reading", t);
        }
        logger.info("Loaded properties: {}", properties);
        return properties;
    }
}
