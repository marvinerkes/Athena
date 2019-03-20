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

package de.progme.athena.example;

import de.progme.athena.example.builder.MySQLExample;
import de.progme.athena.example.builder.SQLiteExample;
import de.progme.athena.example.serialization.mysql.MySQLSerializationExample;
import de.progme.athena.example.serialization.sql.SQLiteSerializationExample;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marvin Erkes on 11.10.2015.
 */
public class Main {

    private static final String HOST = "localhost";

    private static final int PORT = 3306;

    private static final String USER = "root";

    private static final String PASSWORD = "";

    public static void main(String[] args) {

        List<Example> examples = new ArrayList<>();
        examples.add(new MySQLExample());
        examples.add(new SQLiteExample());
        examples.add(new MySQLSerializationExample());
        examples.add(new SQLiteSerializationExample());


        for (Example example : examples) {
            System.out.println("======== Starting " + example.getClass().getSimpleName() + " ========");
            example.setup(HOST, PORT, USER, PASSWORD);
            example.execute();
        }

        System.out.println("========== Finished all examples ==========");
    }
}
