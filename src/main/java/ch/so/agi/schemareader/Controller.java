package ch.so.agi.schemareader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.so.agi.schemareader.dbclients.DbClientMap;
import ch.so.agi.schemareader.model.tableinfo.TableAndFieldInfo;
import ch.so.agi.schemareader.model.tablelisting.TableListing;
import ch.so.agi.schemareader.query.TableInfoQuery;
import ch.so.agi.schemareader.query.TableListingQuery;


@RestController()
public class Controller {
	
	@Autowired
	DbClientMap dbClients;
	
    @RequestMapping("/{db}/{schema}/{table}")
    public TableAndFieldInfo queryTableInfo(
    		@PathVariable(required = true) String db,
    		@PathVariable(required = true) String schema,
    		@PathVariable(required = true) String table){
    	
    	JdbcTemplate dbClient = dbClients.getClient(db);
    	
    	TableAndFieldInfo tci = TableInfoQuery.queryTableInfo(dbClient, schema, table);
    	
    	return tci;
    }
    
    @RequestMapping("/{db}")
    public TableListing listMatchingTables(
    			@PathVariable String db,
	    		@RequestParam(name = "schema", required = false) String schemaNameFragment,
	    		@RequestParam(name = "table", required = false) String tableNameFragment
    		){  
    	  	
  	   	JdbcTemplate dbClient = dbClients.getClient(db);
    	TableListing res = TableListingQuery.queryTables(dbClient, schemaNameFragment, tableNameFragment);
    	
    	return res;
    } 
    
    
}

