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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.akhitev.log.extractor.business_logic.SettingsManager;
import ru.akhitev.log.extractor.business_logic.entity.TargetString;

import java.io.File;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

public class PropertyManager implements SettingsManager {
    private static final String DEFAULT_PROPERTIES_FILE = "extractor.properties";
    private static final String STRING_ARRAYS_TO_FIND_SEPARATOR = ";:;";
    static final String STRINGS_TO_FIND_SEPARATOR = ";;";
    private static Logger logger = LoggerFactory.getLogger(PropertyManager.class);
    private final Properties properties;

    public PropertyManager() {
        properties = loadProperties(new File(DEFAULT_PROPERTIES_FILE));
    }
    public PropertyManager(String filePath) {
        properties = loadProperties(new File(filePath));
    }

    @Override
    public List<TargetString> targetStrings() {
        return Arrays.stream(properties.getProperty("strings.to.find").split(STRING_ARRAYS_TO_FIND_SEPARATOR))
                .map(stringToFind -> {
                    String[] splitStringToFind = stringToFind.split(STRINGS_TO_FIND_SEPARATOR);
                    if (splitStringToFind.length > 0) {
                        List<String> waysToWriting = new ArrayList<>();
                        Collections.addAll(waysToWriting, splitStringToFind);
                        return new TargetString(splitStringToFind[0], waysToWriting);
                    } else {
                        return null;
                    }
                }).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public Integer additionalLinesToPrint() {
        return Integer.parseInt(properties.getProperty("additional.lines.to.print"));
    }

    public String logFilesPathString()  {
        return properties.getProperty("folder.with.log.files");
    }

    public String folderForExtractedFiles() {
        return properties.getProperty("folder.for.extraction.files");
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
