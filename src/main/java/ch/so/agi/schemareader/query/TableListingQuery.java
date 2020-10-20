package ch.so.agi.schemareader.query;

import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.server.ResponseStatusException;

import ch.so.agi.schemareader.model.tablelisting.TableListing;
import ch.so.agi.schemareader.model.tablelisting.TableShortInfo;
import ch.so.agi.schemareader.util.Util;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class TableListingQuery {
	
	private static final int MAX_RESPONSE_COUNT = 15;
	
	private static final String QUERY_FILE = "classpath:sql/table_list.sql";
	private static String QUERY = null;

	private JdbcTemplate dbClient;
	private String schemaHint;
	private String tableHint;
	
	public static TableListing queryTables(JdbcTemplate dbClient, String schemaHint, String tableHint) {
		
		TableListingQuery queryExec = new TableListingQuery(dbClient, schemaHint, tableHint);
		
		TableListing listing = queryExec.queryCatalogue();
		
		return listing;
	}
	
	private TableListingQuery(JdbcTemplate dbClient, String schemaHint, String tableHint) {
		
		if(dbClient == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Argument dbClient must not be null");
					
		if(StringUtils.isBlank(schemaHint) && StringUtils.isBlank(tableHint))
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Either schema or table url params must be given and not blank");
		
		if(QUERY == null)
			QUERY = Util.loadUtf8(QUERY_FILE);
		
		this.dbClient = dbClient;
		
		if(tableHint == null)
			tableHint = "";
		
		this.tableHint = tableHint;
		
		if(schemaHint == null)
			schemaHint = "";
		
		this.schemaHint = schemaHint;
	}
	
	private TableListing queryCatalogue() {
			
		List<TableShortInfo> list = null;
		Integer truncatedTo = null;
		
		list = dbClient.query(
						QUERY, 
						new String[] {"%" + schemaHint + "%", "%" + tableHint + "%"},
						new BeanPropertyRowMapper<TableShortInfo>(TableShortInfo.class)
						);
		
		if(list.size() > MAX_RESPONSE_COUNT) {
			list = list.subList(0, MAX_RESPONSE_COUNT);
			truncatedTo = MAX_RESPONSE_COUNT;
		}
		
		TableListing tl = new TableListing();
		tl.setTableViewList(list);
		tl.setTruncatedTo(truncatedTo);
		
		return tl;
	}
}

