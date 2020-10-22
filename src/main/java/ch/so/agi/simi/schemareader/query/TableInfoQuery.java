package ch.so.agi.simi.schemareader.query;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import ch.so.agi.simi.schemareader.model.tableinfo.FieldInfo;
import ch.so.agi.simi.schemareader.model.tableinfo.TableAndFieldInfo;
import ch.so.agi.simi.schemareader.model.tableinfo.TableInfo;
import ch.so.agi.simi.schemareader.util.Util;

import java.text.MessageFormat;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class TableInfoQuery {
	
	private static final String TABLE_QUERY_FILE = "classpath:sql/tableinfo_table.sql";
	private static String TABLE_QUERY = null;
	
	private static final String COLUMNS_QUERY_FILE = "classpath:sql/tableinfo_fields.sql";
	private static String COLUMNS_QUERY = null;

	private JdbcTemplate dbClient;
	private String schemaName;
	private String tableName;
	
	public static TableAndFieldInfo queryTableInfo(JdbcTemplate dbClient, String schemaName, String tableName) {
		
		TableInfoQuery queryExec = new TableInfoQuery(dbClient, schemaName, tableName);
		
		TableInfo ti = queryExec.queryTableInfo();
		List<FieldInfo> fields = queryExec.queryFields();
		
		TableAndFieldInfo tci = new TableAndFieldInfo();
		tci.setTableInfo(ti);
		tci.setFields(fields);
		
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
		
		if(list == null || list.size() == 0)
			throw new ResponseStatusException(
					HttpStatus.NOT_FOUND, "Table info query returned no rows");
		
		if(list != null && list.size() > 1)
			throw new ResponseStatusException(
					HttpStatus.INTERNAL_SERVER_ERROR, "Table info query returned more that one result row");
		
		return list.get(0);
	}
	
	private List<FieldInfo> queryFields() {
		
 		List<FieldInfo> list = null;
 		
		list = dbClient.query(
				COLUMNS_QUERY, 
				new String[] {schemaName, tableName},
				new BeanPropertyRowMapper<FieldInfo>(FieldInfo.class)
				);
		
		return list;
	}
}

