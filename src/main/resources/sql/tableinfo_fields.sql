/*
Fragt die notwendigen Informationen zu den Attributen einer Tabelle oder View aus dem
postgresql Katalog ab.
Das Schema muss als Search-Path gesetzt sein.
*/

WITH

ili_enums AS (
  SELECT
    tablename,
    columnname
  FROM
    t_ili2db_column_prop
  WHERE
      tag = 'ch.ehi.ili2db.typeKind'
    AND
      setting = 'ENUM'
),

att_base AS (
  SELECT
    nspname AS schema_name,
    relname AS tv_name,
    attrelid AS tv_oid,
    attnum AS att_num,
    attname AS att_name,
    attnotnull AS att_mandatory,
    typname AS att_typ,
    NULLIF(information_schema._pg_char_max_length(atttypid, atttypmod), -1) AS att_length,
    (ie.columnname IS NOT NULL) AS att_ili_enum
  FROM
    pg_namespace
  JOIN
    pg_class
      ON  pg_namespace.oid = pg_class.relnamespace
  JOIN
    pg_attribute
      ON pg_class.oid = pg_attribute.attrelid
  JOIN
    pg_type
      ON pg_attribute.atttypid = pg_type.oid
  LEFT JOIN
    ili_enums ie
      ON relname = ie.tablename AND attname = ie.columnname
  WHERE
      attnum > 0 --exclude system COLUMNS
),

geo_att AS (
  SELECT
    f_table_schema AS geo_schema_name,
    f_table_name AS geo_table_name,
    f_geometry_column AS geo_att_name,
    "type" AS geo_col_type,
    refsys.auth_srid AS geo_col_sr_id,
    refsys.auth_name AS geo_col_sr_org
  FROM
    public.geometry_columns gc
  JOIN
    public.spatial_ref_sys refsys
      ON gc.srid = refsys.srid
),

att_desc AS (
  SELECT
    objoid AS tv_oid,
    objsubid AS att_num,
    description AS att_desc
  FROM
    pg_description
),

att_all AS (
  SELECT
    schema_name,
    tv_name,
    att_name as name,
    att_mandatory as mandatory,
    att_typ as type,
    att_length as length,
    att_desc as description,
    att_ili_enum AS ili_enum,
    geo_col_type as geo_field_type,
    geo_col_sr_id as geo_field_sr_id,
    geo_col_sr_org as geo_field_sr_org
  FROM
    att_base
  LEFT JOIN
    att_desc
      ON att_base.tv_oid = att_desc.tv_oid
      AND att_base.att_num = att_desc.att_num
  LEFT JOIN
    geo_att
      ON att_base.schema_name = geo_att.geo_schema_name
      AND att_base.tv_name = geo_att.geo_table_name
      AND att_base.att_name = geo_att_name
)

SELECT
  *
FROM
  att_all
WHERE
    tv_name = ?
  AND
    schema_name = trim(split_part(current_setting('search_path'), ',', 1)) -- Defensiv: Funktioniert, selbst wenn der Suchpfad mehrere Schmemanamen umfasst.
;