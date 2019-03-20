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

package de.progme.athena.builder.update;

import de.progme.athena.db.serialization.Condition;
import de.progme.athena.query.core.UpdateQuery;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by Marvin Erkes on 13.08.2015.
 */
public class UpdateQueryTest {

    @Test
    public void testUpdateQueryOneSet() {

        String expected = "UPDATE test SET name=? WHERE id=?;";
        String actual = new UpdateQuery.Builder()
                .update("test")
                .set("name", "Jacky")
                .where(new Condition("id", Condition.Operator.EQUAL, "1"))
                .build().sql();

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateQueryMultipleSet() {

        String expected = "UPDATE test SET name=?,uuid=? WHERE id=?;";
        String actual = new UpdateQuery.Builder()
                .update("test")
                .set("name", "Jacky")
                .set("uuid", "0000")
                .where(new Condition("id", Condition.Operator.EQUAL, "1"))
                .build().sql();

        assertEquals(expected, actual);
    }

    @Test
    public void testUpdateQueryOperator() {

        String expected = "UPDATE test SET name=? WHERE id<=?;";
        String actual = new UpdateQuery.Builder()
                .update("test")
                .set("name", "Jacky")
                .where(new Condition("id", Condition.Operator.LESS_EQUAL, "1"))
                .build().sql();

        assertEquals(expected, actual);
    }
}
