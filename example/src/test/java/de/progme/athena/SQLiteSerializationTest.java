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

package de.progme.athena;

import de.progme.athena.db.Type;
import de.progme.athena.db.serialization.Condition;
import de.progme.athena.db.serialization.SerializationManager;
import de.progme.athena.db.settings.AthenaSettings;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

/**
 * Created by Marvin Erkes on 11.10.2015.
 */
public class SQLiteSerializationTest {

    public static void main(String[] args) {

        Athena athena = new Athena(new AthenaSettings.Builder().database("data/test.db").type(Type.SQLITE).build());
        athena.connect();

        athena.dispatch(() -> System.out.println("looool, ich bin async, haha"));

        SerializationManager serializationManager = athena.serializationManager();

        serializationManager.create(TestTable.class);
        // CREATE TABLE IF NOT EXISTS table (id INTEGER AUTO_INCREMENT, name varchar(255), PRIMARY KEY (id));

        for (int i = 0; i < 10; i++) {
            serializationManager.insert(new TestTable(0, "Peter" + i, UUID.randomUUID().toString()));
        }

        selectAll(serializationManager);

        serializationManager.delete(TestTable.class, new Condition("id", Condition.Operator.GREATER_EQUAL, "6"));

        selectAll(serializationManager);

        serializationManager.update(new TestTable(4, "Miau", "hallo xD"));

        selectAll(serializationManager);

        try {
            athena.submit(() -> serializationManager.delete(TestTable.class, new Condition("id", Condition.Operator.LESS_EQUAL, "3"))).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        selectAll(serializationManager);

        serializationManager.drop(TestTable.class);

        athena.close();
    }

    public static void selectAll(SerializationManager serializationManager) {

        System.out.println("Selecting..");
        serializationManager.select(TestTable.class).forEach(System.out::println);
    }
}
