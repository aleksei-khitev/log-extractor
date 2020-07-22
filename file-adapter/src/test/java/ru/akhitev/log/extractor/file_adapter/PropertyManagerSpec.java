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

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;

public class PropertyManagerSpec {
    private PropertyManager propertyManager;

    @Before
    public void setUp() {
        File file = new File(PropertyManagerSpec.class.getClassLoader().getResource("extractor.properties").getFile());
        String absolutePath = file.getAbsolutePath();
        propertyManager = new PropertyManager(absolutePath);
    }

    @Test
    public void given_fileWithProperties_when_logFilesPathString_then_returnCorrectValue() {
        assertThat(propertyManager.logFilesPathString(), Matchers.equalTo("/development/study/log-extractor/src/test/resources/logsForTest"));
    }

    @Test
    public void given_fileWithProperties_when_folderForExtractedFiles_then_returnCorrectValue() {
        assertThat(propertyManager.folderForExtractedFiles(), Matchers.equalTo("/development/study/log-extractor/tmp"));
    }

    @Test
    public void given_fileWithProperties_when_additionalLinesToPrint_then_returnCorrectValue() {
        assertThat(propertyManager.additionalLinesToPrint(), Matchers.equalTo(4));
    }
}
