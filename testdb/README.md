# /testdb 

Enthält den Code für die Initialisierung der Docker Testdatenbank für die "Jar-Integrationstests"

Der entsprechende Docker-Container wird mit der Ausführung des Gradle-Task "initTestDb":
* Zuerst als leere Postgres-DB (mit Postgis) gestartet
* Anschliessend werden mittels ili2pg und Testmodellen in der Datenbank Schema (ohne Daten) angelegt, welche von den Jar-Integrationstests benötigt werden.

Die Jar-Integrationstest werden mittels Gradle-Task "test" mittels build.gradle des Mutterprojektes ausgelöst.  