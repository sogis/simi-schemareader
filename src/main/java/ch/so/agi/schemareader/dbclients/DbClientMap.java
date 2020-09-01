package ch.so.agi.schemareader.dbclients;

import java.text.MessageFormat;
import java.util.Hashtable;
import java.util.Map;

import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import ch.so.agi.schemareader.config.DbConfig;
import ch.so.agi.schemareader.config.DbConfig.Datasource;

@Component
public class DbClientMap {
	
	@Autowired
	DbConfig configs; 
		
	private Map<String, JdbcTemplate> clients = null;
	
	public JdbcTemplate getClient(String dbIdentifier) {
		
		if(clients == null) {
			initDbClients();
		}
		
		JdbcTemplate res = clients.get(dbIdentifier);
		
		if(res == null)
			throw new ResponseStatusException(
				HttpStatus.INTERNAL_SERVER_ERROR,
				MessageFormat.format("Could not find db with key \"{0}\".", dbIdentifier)
			);
		
		return res;
	}
	
	private void initDbClients(){
		this.clients = new Hashtable<>();
	
		for(Datasource ds : configs.getDbs()) {
			JdbcTemplate template = createDbClientForConfig(ds);
			
			clients.put(ds.getKey(), template);
		}
	}
	
	private static JdbcTemplate createDbClientForConfig(Datasource ds) {
		
    	SimpleDriverDataSource dataSource = new SimpleDriverDataSource();    	
    	
    	String driverClassName = "org.postgresql.Driver";
    	Driver driver = null;
        try {
            driver = (Driver)Class.forName(driverClassName).getDeclaredConstructor().newInstance();
        }
        catch(Exception e){
            throw new ResponseStatusException(
            		HttpStatus.INTERNAL_SERVER_ERROR,
            		"Could not find and load jdbc Driver Class " + driverClassName, e);
        }
    	
        dataSource.setDriver(driver);
        dataSource.setUrl(ds.getUrl());
        dataSource.setUsername(ds.getUser());
        dataSource.setPassword(ds.getPass());
 
        return new JdbcTemplate(dataSource);
	}
}