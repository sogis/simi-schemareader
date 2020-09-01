package ch.so.agi.schemareader.query;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import ch.so.agi.schemareader.model.tableinfo.ColumnInfo;
import ch.so.agi.schemareader.model.tableinfo.TableAndColumnInfo;
import ch.so.agi.schemareader.model.tableinfo.TableInfo;
import ch.so.agi.schemareader.util.Util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class TableInfoQuery {
	
	private static final String TABLE_QUERY_FILE = "classpath:sql/tableinfo_table.sql";
	private static String TABLE_QUERY = null;
	
	private static final String COLUMNS_QUERY_FILE = "classpath:sql/tableinfo_columns.sql";
	private static String COLUMNS_QUERY = null;

	private JdbcTemplate dbClient;
	private String schemaName;
	private String tableName;
	
	public static TableAndColumnInfo queryTableInfo(JdbcTemplate dbClient, String schemaName, String tableName) {
		
		TableInfoQuery queryExec = new TableInfoQuery(dbClient, schemaName, tableName);
		
		TableInfo ti = queryExec.queryTableInfo();
		List<ColumnInfo> cols = queryExec.queryColumns();
		
		TableAndColumnInfo tci = new TableAndColumnInfo();
		tci.setTableInfo(ti);
		tci.setColumns(cols);
		
		return tci;
	}
	
	private TableInfoQuery(JdbcTemplate dbClient, String schemaName, String tableName) {
		
		if(dbClient == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Argument dbClient must not be null");
					
		if(StringUtils.isBlank(schemaName))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Argument schemaName must not be blank");
		
		if(StringUtils.isBlank(tableName))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Argument tableName must not be blank");
		
		if(TABLE_QUERY == null)
			TABLE_QUERY = Util.loadUtf8(TABLE_QUERY_FILE);
		
		if(COLUMNS_QUERY == null)
			COLUMNS_QUERY = Util.loadUtf8(COLUMNS_QUERY_FILE);
		
		this.dbClient = dbClient;		
		this.schemaName = schemaName;
		this.tableName = tableName;
	}
	
	private TableInfo queryTableInfo() {
			
 		List<TableInfo> list = null;
 		
		list = dbClient.query(
				TABLE_QUERY, 
				new String[] {schemaName, tableName},
				new BeanPropertyRowMapper<TableInfo>(TableInfo.class)
				);
		
		if(list != null && list.size() > 1)
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR, "Table info query returned more that one result row");
		
		return list.get(0);
	}
	
	private List<ColumnInfo> queryColumns() {
		
 		List<ColumnInfo> list = null;
 		
		list = dbClient.query(
				COLUMNS_QUERY, 
				new String[] {schemaName, tableName},
				new BeanPropertyRowMapper<ColumnInfo>(ColumnInfo.class)
				);
		
		return list;
	}
}

