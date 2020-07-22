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
import ru.akhitev.log.extractor.business_logic.ResultOutputManager;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ResultOutputFileManager implements ResultOutputManager {
    private static Logger logger = LoggerFactory.getLogger(ResultOutputFileManager.class);
    private final String outputFolder;
    private FileWriter fileWriter;
    private BufferedWriter bufferedWriter;
    private PrintWriter printWriter;

    public ResultOutputFileManager(String outputFolder) {
        this.outputFolder = outputFolder;
    }

    @Override
    public void startNewTargetOutput(String targetStringName) {
        try {
            String fileName = String.format("%s/%s.txt", outputFolder, targetStringName);
            createFolderIfNotExist(outputFolder);
            removeFileIfExist(fileName);
            closeWriter();
            createWriter(fileName);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(String line) {
        printWriter.println(line);
        printWriter.flush();
    }

    @Override
    public void writeSourceName(String sourceName) {
        printWriter.println(sourceName);
        printWriter.println("--------------------------------------");
        printWriter.flush();
    }

    @Override
    public void writeFindingSeparator() {
        printWriter.println("...");
        printWriter.flush();
    }

    @Override
    public void writeSourceTerminator() {
        printWriter.println("======================================");
        printWriter.flush();
    }

    private void createWriter(String fileName) throws IOException {
        fileWriter = new FileWriter(fileName);
        bufferedWriter = new BufferedWriter(fileWriter);
        printWriter = new PrintWriter(bufferedWriter);
    }

    private void closeWriter() throws IOException {
        if (printWriter != null || bufferedWriter != null || fileWriter != null) {
            printWriter.close();
            bufferedWriter.close();
            fileWriter.close();
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
}
