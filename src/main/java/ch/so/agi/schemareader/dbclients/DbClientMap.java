package ch.so.agi.schemareader.dbclients;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.postgresql.Driver;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Component;

@Component
@Configuration
@ConfigurationProperties(prefix = "dbs")
public class DbClientMap {
		
	private List<DatasourceConfig> datasources = new ArrayList<DatasourceConfig>(); // auto-mapped from spring config
	private Map<String, JdbcTemplate> clients = null;
	
	public JdbcTemplate getClient(String dbIdentifier) {
		
		if(clients == null) {
			initDbClients();
		}
		
		JdbcTemplate res = clients.get(dbIdentifier);
		
		if(res == null)
			throw new RuntimeException(
				MessageFormat.format("Could not find db with key '{1}'.", dbIdentifier)
			);
		
		return res;
	}
	
	private void initDbClients(){
		this.clients = new Hashtable<>();
		
		clients.put("pub", new JdbcTemplate());
		
		/*
		for(DatasourceConfig ds : datasources) {
			JdbcTemplate template = createDbClientForConfig(ds);
			
			clients.put(ds.ident, template);
		}
		*/
	}
	
	private static JdbcTemplate createDbClientForConfig(DatasourceConfig ds) {
		
    	SimpleDriverDataSource dataSource = new SimpleDriverDataSource();    	
    	
    	String driverClassName = "org.postgresql.Driver";
    	Driver driver = null;
        try {
            driver = (Driver)Class.forName(driverClassName).getDeclaredConstructor().newInstance();
        }
        catch(Exception e){
            throw new RuntimeException("Could not find and load jdbc Driver Class " + driverClassName, e);
        }
    	
        dataSource.setDriver(driver);
        dataSource.setUrl(ds.getUrl());
        dataSource.setUsername(ds.getUser());
        dataSource.setPassword(ds.getPassword());
 
        return new JdbcTemplate(dataSource);
	}
	      
    public List<DatasourceConfig> getDatasources() {
        return datasources;
    }

    public void setDatasources(List<DatasourceConfig> datasources) {
        this.datasources = datasources;
    }
    
    public static class DatasourceConfig {

    	//@NotBlank
        private String ident;
		private String url;
        private String user;
        private String password;
        
        public String getIdent() {
			return ident;
		}
		public void setIdent(String identifier) {
			this.ident = identifier;
		}
        public String getUrl() {
            return url;
        }
        public void setUrl(String url) {
            this.url = url;
        }
        public String getUser() {
            return user;
        }
        public void setUser(String user) {
            this.user = user;
        }
        public String getPassword() {
            return password;
        }
        public void setPassword(String password) {
            this.password = password;
        }

		@Override
		public String toString() {
			return "Datasource [ident=" + ident + ", url=" + url + ", user=" + user + ", password=" + password + "]";
		}

    }

}