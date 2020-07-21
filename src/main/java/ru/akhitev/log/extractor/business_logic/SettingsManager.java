package ru.akhitev.log.extractor.business_logic;

import ru.akhitev.log.extractor.business_logic.entity.TargetString;

import java.util.List;

public interface SettingsManager {
    List<TargetString> targetStrings();
    Integer additionalLinesToPrint();
}
