## Athena
Athena is a **MySQL** and **SQLite** util library with integrated [HikariCP](http://brettwooldridge.github.io/HikariCP/) connection pool.
One of the features is the query builder system with the builder design pattern.
You don't have to write the complete SQL-Query string down anymore. You can instead use a cool and easy builder API.

Another big feature is the serialization system. You can create classes, add some annotations and then you can easy for example execute the create method like this: athena.serializationManager().create(SomeClass.class);
The table is automatically created in your database.

With the simple async API from Athena you can query your database or anything else async without blocking your main thread.
But you can also submit an async operation and wait with the Future to end.

You can now also get the row SQL query from all builders with the sql() method after you have called build() with a query builder.

The only thing that is different to MySQL is that you have to use the 'database' method in the settings builder as a path to a '.db' file and set the type to 'Type.SQLITE' with the settings builder.
The directories to the '.db' file are created from Athena automatically.

**Installation**
- Install [Maven 3](http://maven.apache.org/download.cgi)
- Clone/Download this repo
- Install it with: ```mvn clean install```

**Maven dependency**
```xml
<dependency>
    <groupId>de.progme</groupId>
    <artifactId>athena-core</artifactId>
    <version>1.1.1-SNAPSHOT</version>
</dependency>
```

If you don't use maven you can download a release version and include it in your project.

## Features

- connection pool
- query builder system
- raw sql queries
- aggregate functions
- joins
- serialization system (with annotations)
- simple API
- lightweight
- async API
- easy query result managing

## Examples

**Query builder**

- [MySQL](https://github.com/MarvinErkes/Athena/blob/master/example/src/main/java/de/progme/athena/example/builder/MySQLExample.java)
- [SQLite](https://github.com/MarvinErkes/Athena/blob/master/example/src/main/java/de/progme/athena/example/builder/SQLiteExample.java)

**Serialization**

- [MySQL](https://github.com/MarvinErkes/Athena/blob/master/example/src/main/java/de/progme/athena/example/serialization/mysql/MySQLSerializationExample.java)
- [SQLite](https://github.com/MarvinErkes/Athena/blob/master/example/src/main/java/de/progme/athena/example/serialization/sql/SQLiteSerializationExample.java)
- [Example template class](https://github.com/MarvinErkes/Athena/blob/master/example/src/main/java/de/progme/athena/example/serialization/TestTable.java)

## License
Licensed under the GNU General Public License, Version 3.0.
