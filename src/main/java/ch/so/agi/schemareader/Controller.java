package ch.so.agi.schemareader;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ch.so.agi.schemareader.dbclients.DbClientMap;
import ch.so.agi.schemareader.model.tableinfo.TableInfo;
import ch.so.agi.schemareader.model.tablelisting.TableShortInfo;


@RestController
public class Controller {
	
	@Autowired
	DbClientMap dbClients;
	

    @RequestMapping("/{db}/{schema}/{table}")
    public TableInfo queryTableInfo(){
    	
    	TableInfo ti = new TableInfo();
    	ti.initWithDummies();
    	
    	return ti;
    }
    
    @RequestMapping("/{db}")
    public List<TableShortInfo> listMatchingTables(
	    		@RequestParam(name = "schema", required = false) String schemaNameFragment,
	    		@RequestParam(name = "table") String tableNameFragment
    		){
    	
    	JdbcTemplate temp = dbClients.getClient("pub");
    	
    	TableShortInfo tsi = new TableShortInfo();
    	tsi.initWithDummies();
    	
    	ArrayList<TableShortInfo> list = new ArrayList<>();
    	list.add(tsi);
    	list.add(tsi);
    	
    	return list;
    }
    
    
}

