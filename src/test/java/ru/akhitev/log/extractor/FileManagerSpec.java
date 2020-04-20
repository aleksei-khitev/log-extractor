package ru.akhitev.log.extractor;

import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Path;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

public class FileManagerSpec {
    private FileManager fileManager;

    @Before
    public void setUp() {
        File file = new File(PropertyManagerSpec.class.getClassLoader().getResource("logsForTest").getFile());
        String absolutePath = file.getAbsolutePath();
        fileManager = new FileManager(absolutePath);
    }

    @Test
    public void given_folderWithLogs_when_currentLogAndlogsWithDateAndNumber_then_giveCorrectlySortedList() {
        assertThat(fileManager.sortedLogFiles().stream().map(path -> path.getFileName().toString()).collect(Collectors.toList()),
                contains("galaxy_map.2020-04-02.0.log", "galaxy_map.2020-04-03.0.log", "galaxy_map.2020-04-04.0.log", "galaxy_map.2020-04-07.0.log", "galaxy_map.log"));
    }
}
