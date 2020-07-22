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
package ru.akhitev.log.extractor.business_logic.entity;

import java.util.stream.Stream;

/**
 * The source where to find strings.
 * It can be log file or web page with files collected from different servers and nodes.
 *
 * @author Aleksei Khitev
 */
public class Source {
    /**
     * The name of the source.
     * This will be written to the result output to show where to look into for more info if needed.
     */
    private final String name;
    /** Lines which will be iterated and compared with needed string. */
    private final Stream<String> lines;

    public Source(String name, Stream<String> lines) {
        this.name = name;
        this.lines = lines;
    }

    public String name() {
        return name;
    }

    public Stream<String> lines() {
        return lines;
    }
}
