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

package de.progme.athena.example.builder;

import de.progme.athena.Athena;
import de.progme.athena.db.DBResult;
import de.progme.athena.db.DBRow;
import de.progme.athena.db.Type;
import de.progme.athena.db.serialization.Condition;
import de.progme.athena.db.settings.AthenaSettings;
import de.progme.athena.example.Example;
import de.progme.athena.query.core.*;
import de.progme.athena.query.core.*;

import java.io.File;
import java.util.List;

/**
 * Created by Marvin Erkes on 12.10.2015.
 *
 * Represents an example usage for SQLite and Athenas builder system.
 */
public class SQLiteExample implements Example {

    /**
     * The Athena instance.
     */
    private Athena athena;

    @Override
    public void setup(String host, int port, String user, String password) {

        // Important when you want to use SQLite is that you need to specify the type
        // The other thing to remember is tha the database name is now the path to the .db file
        athena = new Athena(new AthenaSettings.Builder()
                .database("data/test.db")
                .type(Type.SQLITE)
                .build());

        athena.connect();
    }

    @Override
    public void execute() {

        System.out.println("Creating table..");
        athena.execute(new CreateQuery.Builder().create("test").value("id", "INTEGER").value("name", "STRING").primaryKey("id").build());

        System.out.println("Inserting data..");
        for (int i = 0; i < 6; i++) {
            athena.execute(new InsertQuery.Builder().into("test").columns("name").values("Peter" + i).build());
        }

        select(athena);

        System.out.println("Deleting data..");
        athena.execute(new DeleteQuery.Builder().from("test").where(new Condition("id", Condition.Operator.GREATER, "4")).build());

        select(athena);

        System.out.println("Dropping table..");
        athena.execute(new DropQuery.Builder().drop("test").build());

        athena.close();

        new File("data/test.db").delete();
        new File("data").delete();
    }

    private void select(Athena lite) {

        DBResult result = lite.query(new SelectQuery.Builder().from("test").select("*").build());
        System.out.println("Selecting..");

        List<DBRow> rows = result.rows();
        for (DBRow dbRow : rows) {
            System.out.println(dbRow.get("id") + ":" + dbRow.get("name"));
        }
    }
}
