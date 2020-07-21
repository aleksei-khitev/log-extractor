package ru.akhitev.log.extractor.business_logic;

import ru.akhitev.log.extractor.business_logic.entity.Source;

import java.util.Iterator;

public interface SourceManager extends Iterator<Source> {
    @Override Source next();
    @Override boolean hasNext();
    void resetIterator();
}
