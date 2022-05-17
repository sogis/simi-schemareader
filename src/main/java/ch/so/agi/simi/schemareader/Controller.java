package ch.so.agi.simi.schemareader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.so.agi.simi.schemareader.dbclients.DbClientFactory;
import ch.so.agi.simi.schemareader.model.tableinfo.TableAndFieldInfo;
import ch.so.agi.simi.schemareader.model.tablelisting.TableListing;
import ch.so.agi.simi.schemareader.query.TableInfoQuery;
import ch.so.agi.simi.schemareader.query.TableListingQuery;


@RestController()
public class Controller {
	
	@Autowired
	DbClientFactory dbClients;

	@RequestMapping("/{db}/{schema}/{table}")
    public TableAndFieldInfo queryTableInfo(
    		@PathVariable(required = true) String db,
    		@PathVariable(required = true) String schema,
    		@PathVariable(required = true) String table){

    	JdbcTemplate dbClient = dbClients.getClient(db, schema);
    	
    	TableAndFieldInfo tci = TableInfoQuery.queryTableInfo(dbClient, table);
    	
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

