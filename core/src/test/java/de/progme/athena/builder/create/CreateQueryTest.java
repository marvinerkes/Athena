/*
 * Copyright (c) 2017 "Marvin Erkes"
 *
 * This file is part of Athena.
 *
 * Athena is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.progme.athena.builder.create;

import de.progme.athena.query.core.CreateQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Marvin Erkes on 13.08.2015.
 */
public class CreateQueryTest {

    @Test
    public void testCreateQuery() {

        String expected = "CREATE TABLE IF NOT EXISTS test (id int auto_increment,name varchar(255),uuid varchar(255),PRIMARY KEY (id));";
        String actual = new CreateQuery.Builder()
                .create("test")
                .ifNotExists(true)
                .primaryKey("id")
                .value("id", "int", "auto_increment")
                .value("name", "varchar(255)")
                .value("uuid", "varchar(255)")
                .build().sql();

        assertEquals(expected, actual);
    }

    @Test
    public void testCreateQueryWithoutExists() {

        String expected = "CREATE TABLE test (id int auto_increment,name varchar(255),uuid varchar(255),PRIMARY KEY (id));";
        String actual = new CreateQuery.Builder()
                .create("test")
                .ifNotExists(false)
                .primaryKey("id")
                .value("id", "int", "auto_increment")
                .value("name", "varchar(255)")
                .value("uuid", "varchar(255)")
                .build().sql();

        assertEquals(expected, actual);
    }
}
