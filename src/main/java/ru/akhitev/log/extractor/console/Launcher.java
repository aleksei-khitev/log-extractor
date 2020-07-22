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
package ru.akhitev.log.extractor.console;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.akhitev.log.extractor.file_adapter.PropertyManager;
import ru.akhitev.log.extractor.file_adapter.ResultOutputFileManager;
import ru.akhitev.log.extractor.file_adapter.SourceFileManager;
import ru.akhitev.log.extractor.business_logic.ResultOutputManager;
import ru.akhitev.log.extractor.business_logic.Searcher;
import ru.akhitev.log.extractor.business_logic.SourceManager;

public class Launcher {
    private static Logger logger = LoggerFactory.getLogger(Launcher.class);
    private Searcher searcher;

    public Launcher(String[] args) {
        PropertyManager propertyManager;
        if (args != null && args.length > 0) {
            propertyManager = new PropertyManager(args[0]);
        } else {
            propertyManager = new PropertyManager();
        }
        SourceManager sourceManager = new SourceFileManager(propertyManager.logFilesPathString());
        ResultOutputManager resultOutputManager = new ResultOutputFileManager(propertyManager.folderForExtractedFiles());
        searcher = new Searcher(propertyManager, sourceManager, resultOutputManager);
    }

    public static void main(String[] args) {
        Launcher launcher = new Launcher(args);
        launcher.launch();
    }

    private void launch() {
        try {
            searcher.search();
        } catch (Throwable t) {
            logger.error(t.getMessage(), t);
        }
    }
}
