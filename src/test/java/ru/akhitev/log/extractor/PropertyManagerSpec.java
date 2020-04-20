package ru.akhitev.log.extractor;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.arrayContainingInAnyOrder;
import static org.hamcrest.Matchers.equalTo;

public class PropertyManagerSpec {
    private PropertyManager propertyManager;

    @Before
    public void setUp() {
        File file = new File(PropertyManagerSpec.class.getClassLoader().getResource("extractor.properties").getFile());
        String absolutePath = file.getAbsolutePath();
        propertyManager = new PropertyManager(absolutePath);
    }

    @Test
    public void given_fileWithProperties_when_stringsToFind_then_returnCorrectValue() {
        assertThat(propertyManager.stringsToFind(),
                arrayContainingInAnyOrder("[-1503132657];;=-1503132657,", "[-734557214];;=-734557214,", "EditSystemDialogController - Запрос UPDATE;;CreateSystemDialogController - Запрос INSERT INTO"));
    }

    @Test
    public void given_fileWithProperties_when_logFilesPathString_then_returnCorrectValue() {
        assertThat(propertyManager.logFilesPathString(), equalTo("/development/study/log-extractor/src/test/resources/logsForTest"));
    }

    @Test
    public void given_fileWithProperties_when_folderForExtractedFiles_then_returnCorrectValue() {
        assertThat(propertyManager.folderForExtractedFiles(), equalTo("/development/study/log-extractor/tmp"));
    }

    @Test
    public void given_fileWithProperties_when_additionalLinesToPrint_then_returnCorrectValue() {
        assertThat(propertyManager.additionalLinesToPrint(), equalTo(4));
    }
}
