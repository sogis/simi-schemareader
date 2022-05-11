# schemareader

Schemareader (ModelReader) für SIMI. Liest die Schemainformationen einer Tabelle oder View aus der Geotabelle aus, und
gibt die Informationen an SIMI zurück.

# Starten des Docker-Image

## Konfiguration

Die Konfiguration der vom schemareader auslesbaren Datenbanken erfolgt über die Umgebungsvariable SPRING_APPLICATION_JSON. 
Im folgenden Beispiel sind die beiden Datenbanken "pub" und "edit" kongiuriert

```json
{
  "dbs": [
    {
      "key": "edit",
      "url": "jdbc:postgresql://localhost:5432/edit_db",
      "user": "postgres",
      "pass": "postgres"
    },
    {
      "key": "pub",
      "url": "jdbc:postgresql://localhost:5432/pub_db",
      "user": "postgres",
      "pass": "postgres"
    }
  ]
}
```

Hinweise: 
* Um Json-Strings ohne Einrückung / Zeilenumbrüche abzuleiten eignet sich beispielsweise der [Json formatter](https://jsonformatter.org/).
* Beispiel einer Verwendung: [docker-compose.yml](docker/docker-compose.yml) des Integrationstests des generierten Docker Image.

## Endpunkte

### http://localhost:8080/[key]?schema=*fuu*&table=*bar*

Listet alle Tabellen und oder Views der Datenbank mit entsprechendem [key] auf. In der obig beschriebenen Konfiguration sind die key's "edit" und "pub" konfiguriert.
* Die in einem Schema enthalten sind, welches im Schemanamen den Teilbegriff "fuu" enthält
* Deren Name den Teilbegriff "bar" enthält.

Einer der URL-Parameter "schema", "table" muss mindestens angegeben werden. Beim Weglassen der Wildcards (*) wird nach genau übereinstimmenden Begriffen gesucht.

**Beispiel-Resultat** des Aufrufes http://localhost:8080/postgis?schema=pub

```json
{
  "tableViewList": [
    {
      "schemaName": "public",
      "tvName": "geography_columns"
    },
    {
      "schemaName": "public",
      "tvName": "geometry_columns"
    },
    {
      "schemaName": "public",
      "tvName": "raster_columns"
    },
    {
      "schemaName": "public",
      "tvName": "raster_overviews"
    },
    {
      "schemaName": "public",
      "tvName": "spatial_ref_sys"
    }
  ],
  "truncatedTo": null
}
```

Die "truncatedTo"-Zahl gibt an, auf wieviele Zeilen die Rückgabe limitiert wurde. "null", falls keine Limitation erfolgte.

### http://localhost:8080/[key]/[schemaname]/[tablename]

Gibt die Detailinformationen der durch den Pfad [key]/[schemaname]/[tablename] identifizierten Tabelle oder View zurück.

**Beispiel-Resultat** des Aufrufes http://localhost:8080/postgis/tiger/county

```json
{
  "tableInfo": {
    "schemaName": "tiger",
    "description": null,
    "pkField": "cntyidfp",
    "tvName": "county"
  },
  "fields": [
    {
      "name": "gid",
      "mandatory": true,
      "type": "int4",
      "length": null,
      "description": null,
      "geoFieldType": null,
      "geoFieldSrOrg": null,
      "geoFieldSrId": null
    },
    {
      "name": "name",
      "mandatory": false,
      "type": "varchar",
      "length": 100,
      "description": null,
      "geoFieldType": null,
      "geoFieldSrOrg": null,
      "geoFieldSrId": null
    },
    {
      "name": "the_geom",
      "mandatory": false,
      "type": "geometry",
      "length": null,
      "description": null,
      "geoFieldType": "MULTIPOLYGON",
      "geoFieldSrOrg": "EPSG",
      "geoFieldSrId": 4269
    },
    {
      "...": "..."
    }
  ]
}
```


# (Weiter)entwicklung

* Da Deployment-Pipeline --> SQL's direkt im Code
* "Integration" Tests im Sinne der Controller-Methoden. Mit Client von ausserhalb bringt wenig mehr - damit testen wir nur "battle proven" Spring Boot Standardfunktionalität

## Lokal Testen / Starten

Die Datenbank-Abhängigkeit wurde nicht gemockt, sondern mittels postgis docker image automatisiert. Vor Ausführung der auto. Tests darum die DB hochfahren und initialisieren mittels `./gradlew testdb:initTestDb`.

Beim lokalen Starten (`./gradlew bootRun`) muss sichergestellt werden, dass die ENV **SPRING_APPLICATION_JSON** korrekt gesetzt ist. ENV-Inhalt zum Verbinden auf die mittels `./gradlew testdb:initTestDb` erzeugte Test-DB:

```json
{
	"dbs": [{
		"key": "postgis",
		"url": "jdbc:postgresql:postgres",
		"user": "postgres",
		"pass": "postgres"
	}]
}
```

Aufruf der URL http://localhost:8080/postgis/tiger/addr im Browser liefert die Antwort:

```json
{
	"tableInfo": {
		"schemaName": "tiger",
		"description": null,
		"pkField": "gid",
		"tvName": "addr"
	},
	"fields": [{
		"name": "gid",
		"mandatory": true,
		"type": "int4",
		"length": null,
		"description": null,
		"geoFieldType": null,
		"geoFieldSrOrg": null,
		"geoFieldSrId": null
	}, {
		"...": "..."
	}]
}
```

## Interner Aufbau

Packages und deren Bedeutung

* **ch.so.agi.schemareader:** Root-Package mit Spring Controller, Application
* **ch.so.agi.schemareader.config:** Hilfsklassen zum Einlesen der Spring Boot Konfiguration (z.B: aus ENV SPRING_APPLICATION_JSON)
* **ch.so.agi.schemareader.dbclients:** Hilfsklassen zur Bereitstellung der DB-Verbindungen auf die abzufragenden Datenbanken (Pub, Edit, ...)
* **ch.so.agi.schemareader.model.\*:** Enthält als Model die Datentransfer-Objekte (DTO) der Services. Die DTO sind das Bindeglied zwischen Resultset und ausgegebener JSON-Response.
* **ch.so.agi.schemareader.query:** Enthält den Code zur Abfrage des Postgresql-Kataloges - sprich die Business-Logik des Service. 
* **ch.so.agi.schemareader.util:** Umfasst mehrfach verwendete statische Hilfsfunktionen.

# Auslesen Aufzähltyp

## t_ili2db_column_prop V 4.7.0

|tablename|subtype|columnname|tag|setting|
|---------|-------|----------|---|-------|
|attrtypes||charmax|ch.ehi.ili2db.textKind|MTEXT|
|charlength||charmax|ch.ehi.ili2db.textKind|MTEXT|
|attrtypes|attrtypes|codedvalue|ch.ehi.ili2db.enumDomain|SO_SrAttrTypes.TestEnum|
|charlength||char255|ch.ehi.ili2db.textKind|MTEXT|
|attrtypes||char255|ch.ehi.ili2db.textKind|MTEXT|

## t_ili2db_column_prop V 4.3.1

|tablename|subtype|columnname|tag|setting|
|---------|-------|----------|---|-------|
|attrtypes||charmax|ch.ehi.ili2db.textKind|MTEXT|
|charlength||charmax|ch.ehi.ili2db.textKind|MTEXT|
|charlength||char255|ch.ehi.ili2db.textKind|MTEXT|
|attrtypes||char255|ch.ehi.ili2db.textKind|MTEXT|
