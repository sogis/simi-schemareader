# Enthält den Code für die Erzeugung des Docker-Image für die Unit-Tests

Das Image wird nicht bei jedem Build des Schemareader erstellt. Es wird mittels des build.gradle dieses Verzeichnisses
erstellt und auf hub.docker gestellt. Das Build-Skript bezieht das image vom dockerhub, sofern es lokal noch nicht
vorhanden ist.

## DDL Skripte

Die DDL-Skripte werden mehrheitlich mittels ili2pg erzeugt, punktuell auch manuell. Entsprechende Gliederung in die
Unterverzeichnisse:
* ddl/ili: Mittels Modellen und ili2pg erstellte Skripte
* ddl/man: Manuell erstellte Skripte  