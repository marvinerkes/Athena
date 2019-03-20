package de.progme.athena;/*
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

import de.progme.athena.db.DBResult;
import de.progme.athena.db.DBRow;
import de.progme.athena.db.serialization.Condition;
import de.progme.athena.db.settings.AthenaSettings;
import de.progme.athena.query.core.*;
import de.progme.athena.query.core.*;

/**
 * Created by Marvin Erkes on 11.08.2015.
 */
public class ConnectionTest {

    public static void main(String[] args) {

        Athena athena = new Athena(new AthenaSettings.Builder()
                .host("localhost")
                .port(3306)
                .user("root")
                .password("")
                .database("athena")
                .poolSize(10)
                .poolName("Athena-Test")
                .build());

        athena.connect();

        System.out.println("Connected!");

        athena.execute(new CreateQuery.Builder()
                .create("test")
                .primaryKey("id")
                .value("id", "int", "auto_increment")
                .value("name", "varchar(255)")
                .value("uuid", "varchar(255)")
                .build());

        for (int i = 0; i < 2; i++) {
            athena.execute(new InsertQuery.Builder()
                    .into("test")
                    .values("0", "Jack", "000000-000000-000000")
                    .build());
        }

        selectAll(athena);

        System.out.println("Changing...");

        athena.execute(new UpdateQuery.Builder()
                .update("test")
                .set("name", "Jacky")
                .set("uuid", "0000")
                .where(new Condition("id", Condition.Operator.EQUAL, "1"))
                .build());

        selectAll(athena);

        System.out.println("Deleting...");

        athena.execute(new DeleteQuery.Builder().from("test").where(new Condition("id", Condition.Operator.EQUAL, "1")).build());

        selectAll(athena);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        athena.execute(new DropQuery.Builder()
                .drop("test")
                .build());

        System.out.println("Finished!");
    }

    private static void selectAll(Athena athena) {

/*        try {

            List<Long> times = new ArrayList<>();
            for (int i = 0; i < 1; i++) {
                long start = System.currentTimeMillis();

                DBResult dbResult = athena.select(new SelectQuery.Builder()
                        .select("*")
                        .from("test")
                        .build());

                for (DBRow row : dbResult.rows()) {
                    System.out.println("ID: " + row.get("id") + " Name: " + row.get("name") + " UUID: " + row.get("uuid"));
                }
                dbResult.close();

                times.add(System.currentTimeMillis() - start);
            }

            long total = 0;
            for (Long time : times) {
                total += time;
            }
            System.out.println("Average: " + total / times.size());


*//*            ResultSet set = dbResult.resultSet();
            while (set.next()) {
                System.out.println("ID: " + set.getInt("id") +
                        " Name: " + set.getString("name") +
                        " UUID: " + set.getString("uuid"));
            }*//*

        } catch (SQLException e) {
            e.printStackTrace();
        }*/

/*        athena.select(new SelectQuery.Builder()
                .select("*")
                .from("test")
                .build(), new AthenaConsumer<DBResult>() {
            @Override
            public void accept(DBResult result) {
                try {
                    for (DBRow row : result.rows()) {
                        System.out.println("Name: " + row.get("name"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });*/


        DBResult result = athena.query(new SelectQuery.Builder()
                .select("*")
                .from("test")
                .build());

        for (DBRow row : result.rows()) {
            int id = row.get("id");
            String customName = row.get("name");
            String uuid = row.get("uuid");
            System.out.println(id + " : " + customName + " : " + uuid);
        }

/*        try {
            List<TestTable> result = athena.select(new SelectQuery.Builder()
                    .select("*")
                    .from("test")
                    .limit(2)
                    .build(), TestTable.class);
            result.forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }
}
