/*
Queries the postgresql catalog and returns a list of
tables and views matching the given table and or schema name fragment.
*/

select
    ns.nspname as schema_name,
    tv.relname as tv_name
from 
    pg_class tv 
join
    pg_namespace ns
        on tv.relnamespace = ns.oid
where 
        tv.relkind in ('r','v')
    and
        ns.nspname like ?
    and 
        tv.relname like ?
order by
	tv.relname, ns.nspname
;
