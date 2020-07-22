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
package ru.akhitev.log.extractor.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import ru.akhitev.log.extractor.business_logic.ResultOutputManager;
import ru.akhitev.log.extractor.business_logic.Searcher;
import ru.akhitev.log.extractor.business_logic.SourceManager;
import ru.akhitev.log.extractor.file_adapter.PropertyManager;
import ru.akhitev.log.extractor.file_adapter.ResultOutputFileManager;
import ru.akhitev.log.extractor.file_adapter.SourceFileManager;

public class MainController {
    @FXML private Button search;
    private Searcher searcher;

    @FXML
    public void initialize() {
        search.setOnAction(this::doSearch);
    }

    private void doSearch(ActionEvent event) {
        PropertyManager propertyManager;
        propertyManager = new PropertyManager("/development/study/log-extractor/console-app/src/test/resources/extractor.properties");
        SourceManager sourceManager = new SourceFileManager(propertyManager.logFilesPathString());
        ResultOutputManager resultOutputManager = new ResultOutputFileManager(propertyManager.folderForExtractedFiles());
        searcher = new Searcher(propertyManager, sourceManager, resultOutputManager);
        searcher.search();
    }
}
