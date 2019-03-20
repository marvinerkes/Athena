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

import de.progme.athena.db.DBResult;
import de.progme.athena.db.DBRow;
import de.progme.athena.db.Type;
import de.progme.athena.db.serialization.Condition;
import de.progme.athena.db.settings.AthenaSettings;
import de.progme.athena.query.core.*;
import de.progme.athena.query.core.*;

import java.util.List;

/**
 * Created by Marvin Erkes on 29.09.2015.
 */
public class SQLiteTest {

    public static void main(String[] args) {

        Athena athena = new Athena(new AthenaSettings.Builder().database("data/test.db").type(Type.SQLITE).build());
        athena.connect();

        athena.execute(new CreateQuery.Builder().create("test").value("id", "INTEGER").value("name", "STRING").primaryKey("id").build());

        for (int i = 0; i < 6; i++) {
            athena.execute(new InsertQuery.Builder().into("test").columns("name").values("Peter" + i).build());
        }

        select(athena);

        athena.execute(new DeleteQuery.Builder().from("test").where(new Condition("id", Condition.Operator.GREATER, "4")).build());

        select(athena);

        athena.execute(new DropQuery.Builder().drop("test").build());
    }

    private static void select(Athena lite) {

        DBResult result = lite.query(new SelectQuery.Builder().from("test").select("*").build());
        System.out.println("Selected:");

        List<DBRow> row = result.rows();
        for (DBRow dbRow : row) {
            System.out.println(dbRow.get("id") + ":" + dbRow.get("name"));
        }
    }
}
