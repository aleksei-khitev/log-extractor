package ru.akhitev.log.extractor;

import java.nio.file.Path;
import java.util.Comparator;

public class LoggerFileNamesComparator  implements Comparator<Path> {
    private static final String LOG_FILE_SUFFIX = ".log";

    @Override
    public int compare(Path p1, Path p2) {
        String name1 = p1.getFileName().toString();
        String name2 = p2.getFileName().toString();
        String commonPart = getCommonPart(name1, name2);
        String name1Remains = name1.substring(commonPart.length() - 1);
        String name2Remains = name2.substring(commonPart.length() - 1);
        return compare(name1Remains, name2Remains);
    }

    private String getCommonPart(String name1, String name2) {
        String commonPart = null;
        for (int i = 0; i < name1.length(); i++) {
            String partOfName = name1.substring(0, i);
            if (name2.contains(partOfName)) {
                commonPart = partOfName;
            } else {
                break;
            }
        }
        return commonPart;
    }

    private int compare(String name1Remains, String name2Remains) {
        if (LOG_FILE_SUFFIX.equals(name1Remains)) {
            return 1;
        } else if (LOG_FILE_SUFFIX.equals(name2Remains)) {
            return -1;
        }
        if (name1Remains.length() != name2Remains.length()) {
            return Comparator.comparingInt(String::length).compare(name1Remains, name2Remains);
        }
        try {
            int name1RemainsInt = Integer.parseInt(name1Remains);
            int name2RemainsInt = Integer.parseInt(name2Remains);
            return Integer.compare(name1RemainsInt, name2RemainsInt);
        } catch (NumberFormatException e) {
            return name1Remains.compareTo(name2Remains);
        }
    }
}
