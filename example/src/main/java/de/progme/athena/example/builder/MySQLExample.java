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
import de.progme.athena.db.Function;
import de.progme.athena.db.serialization.Condition;
import de.progme.athena.db.settings.AthenaSettings;
import de.progme.athena.example.Example;
import de.progme.athena.query.core.*;
import de.progme.athena.query.core.*;

import java.util.UUID;

/**
 * Created by Marvin Erkes on 11.10.2015.
 *
 * Represents an example usage for MySQL and Athenas builder system.
 */
public class MySQLExample implements Example {

    /**
     * The Athena instance.
     */
    private Athena athena;

    @Override
    public void setup(String host, int port, String user, String password) {

        // The default Athena database type is MYSQL so you don't need to set it especially
        athena = new Athena(new AthenaSettings.Builder()
                .host(host)
                .port(port)
                .user(user)
                .password(password)
                .database("test")
                .poolSize(10)
                .poolName("Athena-Test")
                .queryTimeout(1000)
                .build());

        // Connect to the Database
        athena.connect();

        System.out.println("Connected!");
    }

    @Override
    public void execute() {

        // Create a new table 'test' with 'id', 'name' and 'uuid'
        athena.execute(new CreateQuery.Builder()
                .create("test")
                .primaryKey("id")
                .value("id", "int", "auto_increment")
                .value("name", "varchar(255)")
                .value("uuid", "varchar(255)")
                .build());

        // Insert some data
        for (int i = 0; i < 2; i++) {
            athena.execute(new InsertQuery.Builder()
                    .into("test")
                    .values("0", "Jack", UUID.randomUUID().toString())
                    .build());
        }

        selectAll(athena);

        System.out.println("Changing...");

        // Update table 'test' and set an new 'name' and 'uuid' where the 'id' is '1'
        athena.execute(new UpdateQuery.Builder()
                .update("test")
                .set("name", "Jacky")
                .set("uuid", "0000")
                .where(new Condition("id", Condition.Operator.EQUAL, "1"))
                .build());

        selectAll(athena);

        System.out.println("Deleting...");

        // Sync
        athena.execute(new DeleteQuery.Builder().from("test").where(new Condition("id", Condition.Operator.EQUAL, "1")).build());

        // Async
        //athena.dispatch(() -> athena.execute(new DeleteQuery.Builder().from("test").where(new Condition("id", Condition.Operator.EQUAL, "1")).build()));

        // Function example
        DBResult result = athena.query(new SelectQuery.Builder().select("*").from("test").function(new Function(Function.Type.AVG, "id", "average")).build());
        System.out.println("Average from 'id': " + result.row(0).get("average"));

        // SQL JOIN usage example
        //athena.query(new SelectQuery.Builder().select("*").from("test").join(new Join("test_other", "test_other.id", "test.id")).build());

        selectAll(athena);

        // You can get also the raw SQL query string from a builder
        //new DropQuery.Builder().drop("test").build().sql();

        // Drop table 'test'
        athena.execute(new DropQuery.Builder()
                .drop("test")
                .build());

        athena.close();
    }

    private void selectAll(Athena athena) {

        // Select all from table 'test'
        DBResult result = athena.query(new SelectQuery.Builder()
                .select("*")
                .from("test")
                .build());

        // Iterate through the rows and print these out
        for (DBRow row : result.rows()) {
            int id = row.get("id");
            String customName = row.get("name");
            String uuid = row.get("uuid");
            System.out.println(id + " : " + customName + " : " + uuid);
        }
    }
}
