/*
Fragt die notwendigen Informationen zu einer Tabelle oder View eines Schemas aus dem
postgresql Katalog ab.
Das Schema muss als Search-Path gesetzt sein.
*/

with

table_view_base as  (
  select
    ns.oid as tv_schema_oid,
    ns.nspname as tv_schema_name,
    tv.oid as tv_oid,
    tv.relname as tv_name,
    (tv.relkind = 'v') as db_view
  from
    pg_class tv
  join
    pg_namespace ns
      on tv.relnamespace = ns.oid
  where
    relkind in ('r','v')
),

table_pk as ( -- pk attribut einer tabelle, sofern pk genau ein attribut umfasst
  select
    con.conrelid as table_oid,
    att.attname as pkattr_name
  from
    pg_constraint con
  join
    pg_attribute att
      on con.conrelid = att.attrelid and con.conkey[1] = att.attnum
  where
      con.contype = 'p'
    and
      array_length(con.conkey, 1) = 1
),

table_view_desc as (
  SELECT
    objoid AS tv_oid,
    description AS tv_desc
  FROM
    pg_catalog.pg_description
  where
    objsubid = 0
),

table_view_all as (
  select
    tv_schema_name as schema_name,
    tv_name as tv_name,
    db_view,
    tv_desc as description,
    pkattr_name as pk_field
  from
    table_view_base
  left join
    table_pk
      on table_view_base.tv_oid = table_pk.table_oid
  left join
    table_view_desc
      on table_view_base.tv_oid = table_view_desc.tv_oid
)

select
  *
from
  table_view_all
where
    tv_name = ?
  and
    schema_name = trim(split_part(current_setting('search_path'), ',', 1)) -- Defensiv: Funktioniert, selbst wenn der Suchpfad mehrere Schmemanamen umfasst.
;