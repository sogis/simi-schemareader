package ch.so.agi.simi.schemareader.dbclients;

import java.text.MessageFormat;
import java.util.List;

import ch.so.agi.simi.schemareader.util.Util;
import org.postgresql.Driver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import ch.so.agi.simi.schemareader.config.DbConfig;
import ch.so.agi.simi.schemareader.config.DbConfig.Datasource;

@Component
public class DbClientFactory {
	
	@Autowired
	DbConfig configs;

	public JdbcTemplate getClient(String dbIdentifier) {
		return getClient(dbIdentifier, null);
	}

	public JdbcTemplate getClient(String dbIdentifier, String schemaName) {

		Datasource ds = dsForIdentifier(dbIdentifier);

		if(schemaName != null){
			ds = deriveSchemaDs(ds, schemaName);
		}

		JdbcTemplate t = createDbClientForConfig(ds);
		return t;
	}

	private Datasource deriveSchemaDs(Datasource dbDataSource, String schema) {
		Datasource derived = dbDataSource.deepCopy();
		derived.setUrl(derived.getUrl() + "?currentSchema=" + schema);
		return derived;
	}

	private Datasource dsForIdentifier(String dbIdentifier) {
		Datasource res  = null;

		if(configs == null || configs.getDbs() == null || configs.getDbs().size() == 0) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
					"Schemareader configuration has no configured db connections - deployment must be checked");
		}

		List<Datasource> dataSources = configs.getDbs();

		for(Datasource ds : dataSources) {
			if(ds.getKey().equals(dbIdentifier)){
				res = ds;
				break;
			}
		}

		if(res == null){
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, MessageFormat.format(
					"Requested db identifier \"{0}\" is not configured. Schemareader service has {1} db's configured in the service",
					dbIdentifier,
					dataSources.size()
			));
		}

		return res;
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