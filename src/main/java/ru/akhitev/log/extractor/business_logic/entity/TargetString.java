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

import java.util.List;

/**
 * This is a string we want to find with different options how this can be written.
 * @author Aleksei Khitev
 */
public class TargetString {
    private final String name;
    private final List<String> waysToWriting;

    public TargetString(String name, List<String> waysToWriting) {
        this.name = name;
        this.waysToWriting = waysToWriting;
    }

    public String getName() {
        return name;
    }

    public List<String> getWaysToWriting() {
        return waysToWriting;
    }
}
