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

package de.progme.athena.db.serialization;

import de.progme.athena.Athena;
import de.progme.athena.db.DBResult;
import de.progme.athena.db.DBRow;
import de.progme.athena.db.Type;
import de.progme.athena.db.serialization.annotation.Column;
import de.progme.athena.db.serialization.annotation.Table;
import de.progme.athena.db.serialization.exception.SQLSerializationException;
import de.progme.athena.query.core.*;
import de.progme.athena.query.core.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Marvin Erkes on 11.10.2015.
 *
 * Represents a class that can be used to serialize classes to eg. tables etc.
 */
public class SerializationManager {

    /**
     * The Athena instance.
     */
    private final Athena athena;

    /**
     * Creates a new instance from SerializationManager with the athena instance.
     *
     * @param athena the Athena instance.
     */
    public SerializationManager(Athena athena) {

        this.athena = athena;
    }

    /**
     * Creates a SQL table from the given template class.
     * The template class need the Table annotation and field need the Column annotation.
     *
     * @param clazz the template class.
     * @return true if ot was successful otherwise false.
     */
    public boolean create(Class<?> clazz) {

        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new SQLSerializationException("Table annotation is not present!");
        }

        Table table = clazz.getAnnotation(Table.class);
        Table.Option[] options = table.options();

        CreateQuery.Builder builder = new CreateQuery.Builder();
        builder.create(table.name());

        if(Table.Option.CREATE_IF_NOT_EXISTS.isOption(options)) {
            builder.ifNotExists(true);
        }

        List<String> primaryKeys = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if(field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                Column.Option[] rowOptions = column.options();
                String name = column.name().equals("") ? field.getName() : column.name();

                Column.Type type = Column.Type.typeFromClass(field.getType());

                if (type == Column.Type.UNKNOWN) {
                    continue;
                }

                if (Column.Option.PRIMARY_KEY.isOption(rowOptions)) {
                    primaryKeys.add(name);
                }

                List<String> rowOptionStrings = new ArrayList<>();
                rowOptionStrings.add(type.sqlName());
                for (Column.Option option : rowOptions) {
                    if (option == Column.Option.PRIMARY_KEY) {
                        continue;
                    }

                    if (athena.type() == Type.SQLITE && option == Column.Option.AUTO_INCREMENT) {
                        continue;
                    }

                    rowOptionStrings.add(option.sql());
                }

                builder.value(name, rowOptionStrings.toArray(new String[rowOptionStrings.size()]));
            }
        }

        primaryKeys.forEach(builder::primaryKey);

        return athena.execute(builder.build());
    }

    /**
     * Selects data from the table given in the template class, the limit, the sort order and conditions.
     * The template class need the Table annotation and fields need the Column annotation.
     *
     * @param clazz the template class.
     * @param limit the limit. -1 if no limit is needed.
     * @param order the sort order.
     * @param conditions the conditions for the SQL SELECT.
     * @return a automatically filled list of the template class type with field set.
     */
    public <T> List<T> select(Class<T> clazz, int limit, Order order, Condition... conditions) {

        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new SQLSerializationException("Table annotation is not present!");
        }

        Table table = clazz.getAnnotation(Table.class);

        SelectQuery.Builder builder = new SelectQuery.Builder();

        for (Field field : clazz.getDeclaredFields()) {
            if(field.isAnnotationPresent(Column.class)) {
                Column column = field.getAnnotation(Column.class);
                String name = column.name().equals("") ? field.getName() : column.name();
                builder.select(name);
            }
        }

        builder.from(table.name());

        if(limit > 0) {
            builder.limit(limit);
        }

        for (Condition condition : conditions) {
            builder.where(condition);
        }

        if(order != null) {
            builder.orderBy(order);
        }

        DBResult result = athena.query(builder.build());

        List<T> resultList = new ArrayList<>();

        try {
            for (DBRow row : result.rows()) {
                T classInstance = clazz.newInstance();

                for (Field field : classInstance.getClass().getDeclaredFields()) {
                    if(field.isAnnotationPresent(Column.class)) {
                        if(Modifier.isFinal(field.getModifiers())) {
                            continue;
                        }

                        Column column = field.getAnnotation(Column.class);
                        String name = column.name().equals("") ? field.getName() : column.name();

                        if(!row.hasKey(name)) {
                            continue;
                        }

                        field.setAccessible(true);
                        field.set(classInstance, row.getObject(name));
                        field.setAccessible(false);
                    }
                }

                resultList.add(classInstance);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return resultList;
    }

    /**
     * Selects data from the table given in the template class, the limit is -1, the sort order and conditions.
     * The template class need the Table annotation and fields need the Column annotation.
     *
     * @param clazz the template class.
     * @param order the sort order.
     * @param conditions the conditions for the SQL SELECT.
     * @return a automatically filled list of the template class type with field set.
     */
    public <T> List<T> select(Class<T> clazz, Order order, Condition... conditions) {

        return select(clazz, -1, order, conditions);
    }

    /**
     * Selects data from the table given in the template class, the limit, no order and conditions.
     * The template class need the Table annotation and fields need the Column annotation.
     *
     * @param clazz the template class.
     * @param limit the limit. -1 if no limit is needed.
     * @param conditions the conditions for the SQL SELECT.
     * @return a automatically filled list of the template class type with field set.
     */
    public <T> List<T> select(Class<T> clazz, int limit, Condition... conditions) {

        return select(clazz, limit, null, conditions);
    }

    /**
     * Selects data from the table given in the template class, the limit is -1, no order and conditions.
     * The template class need the Table annotation and fields need the Column annotation.
     *
     * @param clazz the template class.
     * @param conditions the conditions for the SQL SELECT.
     * @return a automatically filled list of the template class type with field set.
     */
    public <T> List<T> select(Class<T> clazz, Condition... conditions) {

        return select(clazz, -1, null, conditions);
    }

    /**
     * Drops a SQL table from the given template class.
     * The template class need the Table annotation.
     *
     * @param clazz the template class.
     * @return true if ot was successful otherwise false.
     */
    public boolean drop(Class<?> clazz) {

        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new SQLSerializationException("Table annotation is not present!");
        }

        Table table = clazz.getAnnotation(Table.class);

        return athena.execute(new DropQuery.Builder().drop(table.name()).build());
    }

    /**
     * Inserts the field of the given class as an object into the SQL table from the given class.
     * The template class need the Table annotation and fields need the Column annotation.
     *
     * @param object the class as an object.
     * @return true if ot was successful otherwise false.
     */
    public boolean insert(Object object) {

        Class<?> clazz = object.getClass();

        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new SQLSerializationException("Table annotation is not present!");
        }

        Table table = clazz.getAnnotation(Table.class);

        InsertQuery.Builder builder = new InsertQuery.Builder();
        builder.into(table.name());

        try {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    String name = column.name().equals("") ? field.getName() : column.name();

                    if(athena.type() == Type.SQLITE && Column.Option.AUTO_INCREMENT.isOption(column.options())) {
                        continue;
                    }

                    builder.column(name);
                    field.setAccessible(true);
                    builder.value(field.get(object).toString());
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return athena.execute(builder.build());
    }

    /**
     * Deletes rows from the SQL table given in the template class and the conditions.
     * The template class need the Table annotation.
     *
     * @param clazz the template class.
     * @param conditions the conditions.
     * @return true if ot was successful otherwise false.
     */
    public boolean delete(Class<?> clazz, Condition... conditions) {

        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new SQLSerializationException("Table annotation is not present!");
        }

        Table table = clazz.getAnnotation(Table.class);

        DeleteQuery.Builder builder = new DeleteQuery.Builder();
        builder.from(table.name());

        for (Condition condition : conditions) {
            builder.where(condition);
        }

        return athena.execute(builder.build());
    }

    /**
     * Updates an SQL table and sets the columns in the setColumns array to the values from
     * the field in the template class where the primary key from the template class matches.
     *
     * The template class need the Table annotation and fields need the Column annotation.
     *
     * @param object the template class as an object.
     * @param setColumns the columns that should be updated.
     * @param conditions the extra conditions.
     * @return true if ot was successful otherwise false.
     */
    public boolean update(Object object, String[] setColumns, Condition... conditions) {

        Class<?> clazz = object.getClass();

        if (!clazz.isAnnotationPresent(Table.class)) {
            throw new SQLSerializationException("Table annotation is not present!");
        }

        Table table = clazz.getAnnotation(Table.class);

        UpdateQuery.Builder builder = new UpdateQuery.Builder();
        builder.update(table.name());

        HashMap<String, String> primaryKeys = new HashMap<>();

        try {
            for (Field field : clazz.getDeclaredFields()) {
                if(field.isAnnotationPresent(Column.class)) {
                    Column column = field.getAnnotation(Column.class);
                    Column.Option[] columnOptions = column.options();
                    String name = column.name().equals("") ? field.getName() : column.name();

                    if(Column.Option.PRIMARY_KEY.isOption(columnOptions)) {
                        field.setAccessible(true);
                        primaryKeys.put(name, field.get(object).toString());
                        field.setAccessible(false);
                        continue;
                    }

                    if(setColumns != null) {
                        boolean contains = false;

                        for (String setColumn : setColumns) {
                            if (setColumn.equals(name)) {
                                contains = true;
                            }
                        }

                        if(!contains) {
                            continue;
                        }
                    }

                    field.setAccessible(true);
                    builder.set(name, field.get(object).toString());
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        if(conditions.length == 0) {
            for (Map.Entry<String, String> entry : primaryKeys.entrySet()) {
                builder.where(new Condition(entry.getKey(), Condition.Operator.EQUAL, entry.getValue()));
            }
        }else {
            for (Condition condition : conditions) {
                builder.where(condition);
            }
        }

        return athena.execute(builder.build());
    }

    /**
     * Updates all columns from an SQL table where the extra conditions matches.
     * The template class need the Table annotation and fields need the Column annotation.
     *
     * @param object the template class as object.
     * @param conditions the conditions.
     * @return true if ot was successful otherwise false.
     */
    public boolean update(Object object, Condition... conditions) {

        return update(object, null, conditions);
    }
}
