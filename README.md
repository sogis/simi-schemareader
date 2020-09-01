# schemareader

Schemareader (ModelReader) für SIMI. Liest die Schemainformationen einer Tabelle oder View aus der Geotabelle aus, und
gibt die Informationen an SIMI zurück.

## Konfiguration des resultierenden Docker Image

## Endpunkte

## Weiterentwicklung

* Da Deployment-Pipeline --> SQL's direkt im Code
* "Integration" Tests im Sinne der Controller-Methoden. Mit Client von ausserhalb bringt wenig mehr - damit testen wir nur "battle proven" Spring Boot Standardfunktionalität

## Konfiguration

{"dbs":[{"key":"pub","url":"jdbc:postgresql://localhost:5432/postgres","user":"postgres","pass":"postgres"},{"key":"edit","url":"jdbc:postgresql://localhost:5432/postgres","user":"postgres","pass":"postgres"}]}

## Interner Aufbau

Packages und deren Bedeutung

* **ch.so.agi.schemareader:** Root-Package mit Spring Controller, Application
* **ch.so.agi.schemareader.config:** Hilfsklassen zum Einlesen der Spring Boot Konfiguration (z.B: aus ENV SPRING_APPLICATION_JSON)
* **ch.so.agi.schemareader.dbclients:** Hilfsklassen zur Bereitstellung der DB-Verbindungen auf die abzufragenden Datenbanken (Pub, Edit, ...)
* **ch.so.agi.schemareader.model.*:** Enthält als Model die Datentransfer-Objekte (DTO) der Services. Die DTO sind das Bindeglied zwischen Resultset und ausgegebener JSON-Response.
* **ch.so.agi.schemareader.query: Enthält den Code zur Abfrage des Postgresql-Kataloges - sprich die Business-Logik des Service. 
* **ch.so.agi.schemareader.util: Umfasst mehrfach verwendete statische Hilfsfunktionen.