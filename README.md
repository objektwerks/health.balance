Health Balance
--------------
>Health balance distributed app using Scala3, ScalaFX, Jsoniter and PostgreSql.

Build
-----
1. sbt clean compile

Test
----
1. sbt clean test

Server Run
----------
1. sbt server/run

Client Run
----------
1. sbt client/run

Package Server
--------------
1. sbt server/universal:packageBin
2. see server/target/universal

Client Assembly
---------------
>To build for a "mac", "m1', "win" or "linux" os target:
1. sbt -Dtarget="mac" clean test assembly copyAssemblyJar
2. sbt -Dtarget="m1" clean test assembly copyAssemblyJar
3. sbt -Dtarget="win" clean test assembly copyAssemblyJar
4. sbt -Dtarget="linux" clean test assembly copyAssemblyJar

Execute Client
--------------
>To execute an assembled jar locally:
1. java -jar .assembly/pool-balance-mac-0.3.jar
2. java -jar .assembly/pool-balance-m1-0.3.jar
3. java -jar .assembly/pool-balance-win-0.3.jar
4. java -jar .assembly/pool-balance-linux-0.3.jar

Deploy
------
>Consider these options:
1. [jDeploy](https://www.npmjs.com/package/jdeploy)
2. [Conveyor](https://hydraulic.software/index.html)

Features
--------
1. consumables
2. expendables
3. measurables
4. observables
5. dashboard

Use Cases
---------
1. add | update consumables
2. add | update expendables
3. add | update measurables
4. add | update observables
5. view dashboard

Entity
------
* Profile 1 --> * Consumable, Expendable, Measurable, Observable
* Consumable <-- Food, Liquid, Sunshine, FreshAir
* Expendable <-- Exercise, Sleep
* Measurable <-- BloodPressure, Pulse, Height, Weight, Glucose
* Observable <-- Mood, Stress

Postgresql
----------
1. config:
    1. on osx intel: /usr/local/var/postgres/postgresql.config : listen_addresses = ‘localhost’, port = 5432
    2. on osx m1: /opt/homebrew/var/postgres/postgresql.config : listen_addresses = ‘localhost’, port = 5432
2. run:
    1. brew services start postgresql
3. logs:
    1. on osx intel: /usr/local/var/log/postgres.log
    2. on m1: /opt/homebrew/var/log/postgres.log

Database
--------
>Example database url: postgresql://localhost:5432/healthbalance?user=mycomputername&password=healthbalance"
1. psql postgres
2. CREATE DATABASE healthbalance OWNER [your computer name];
3. GRANT ALL PRIVILEGES ON DATABASE healthbalance TO [your computer name];
4. \l
5. \q
6. psql healthbalance
7. \i ddl.sql
8. \q

DDL
---
>Alternatively run: psql -d healthbalance -f ddl.sql
1. psql healthbalance
2. \i ddl.sql
3. \q

Drop
----
1. psql postgres
2. drop database healthbalance;
3. \q

Environment
-----------
>The following environment variables must be defined:
* export HEALTH_BALANCE_HOST="127.0.0.1"
* export HEALTH_BALANCE_PORT=7171

* export HEALTH_BALANCE_POSTGRESQL_URL="jdbc:postgresql://localhost:5432/healthbalance"
* export HEALTH_BALANCE_POSTGRESQL_USER="yourusername"
* export HEALTH_BALANCE_POSTGRESQL_PASSWORD="healthbalance"
* export HEALTH_BALANCE_POSTGRESQL_DRIVER="org.postgresql.ds.PGSimpleDataSource"
* export HEALTH_BALANCE_POSTGRESQL_DB_NAME="healthbalance"
* export HEALTH_BALANCE_POSTGRESQL_HOST="127.0.0.1"
* export HEALTH_BALANCE_POSTGRESQL_PORT=5432

* export HEALTH_BALANCE_EMAIL_HOST="your-email.provider.com"
* export HEALTH_BALANCE_EMAIL_ADDRESS="your-email@provider.com"
* export HEALTH_BALANCE_EMAIL_PASSWORD="your-email-password"

Resources
---------
1. [JavaFX](https://openjfx.io/index.html)
2. [ScalaFX](http://www.scalafx.org/)
3. [ScalikeJdbc](http://scalikejdbc.org/)
4. [jDeploy](https://www.jdeploy.com/)
5. [JavaFX Tutorial](https://jenkov.com/tutorials/javafx/index.html)

License
-------
>Copyright (c) [2023] [Objektwerks]

>Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    * http://www.apache.org/licenses/LICENSE-2.0

>Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.