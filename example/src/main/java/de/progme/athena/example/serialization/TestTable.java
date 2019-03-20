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

package de.progme.athena.example.serialization;

import de.progme.athena.db.serialization.annotation.Column;
import de.progme.athena.db.serialization.annotation.Table;

/**
 * Created by Marvin Erkes on 12.10.2015.
 *
 * Represents a template class to use with the serialization manager.
 */
@Table(name = "test", options = { Table.Option.CREATE_IF_NOT_EXISTS })
public class TestTable {

    @Column(name = "other_id", options = { Column.Option.PRIMARY_KEY, Column.Option.AUTO_INCREMENT })
    private int id;

    @Column(name = "other_name")
    private String name;

    @Column(name = "other_uuid")
    private String uuid;

    /**
     * You need a default constructor
     */
    public TestTable() {

    }

    /**
     * Constructor to fill the fields.
     *
     * @param id an id.
     * @param name a name.
     * @param uuid an uuid as string.
     */
    public TestTable(int id, String name, String uuid) {

        this.id = id;
        this.name = name;
        this.uuid = uuid;
    }

    @Override
    public String toString() {

        return "TestTable{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}
