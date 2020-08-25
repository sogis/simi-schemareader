package ch.so.agi.schemareader.config;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties
public class DbConfig {
    
    private List<Datasource> dbs = new ArrayList<Datasource>();
        
    public List<Datasource> getDbs() {
    	assertValidConfig();
    	    	
        return dbs;
    }

    public void setDbs(List<Datasource> datasources) {
        this.dbs = datasources;
    }
    
    private void assertValidConfig() {
    	for(Datasource ds:dbs) {
    		if(ds == null) {
    			throw new RuntimeException("Config json array 'dbs' contains empty datasource");
    		}
    		
    		ds.assertValidConfig();
    	}
    }
    
    public static class Datasource {
    	
        private String key;
        private String url;
        private String user;
        private String pass;
        
        private void assertValidConfig() {
        	// Did not find out, why the standard hibernate validators did not work --> self implemented
        	assertNotBlank("key", key);
        	assertNotBlank("url", url);
        	assertNotBlank("user", user);
        	assertNotBlank("pass", pass);
        }
        
        private static void assertNotBlank(String key, String val) {
        	if(val == null || val.trim().length() == 0) {
        		throw new RuntimeException(
        				MessageFormat.format("Config must contain \"{0}\" object with non blank value.", key)
        				);
        	}
        }

        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
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

        public String getPass() {
            return pass;
        }
        public void setPass(String password) {
            this.pass = password;
        }
        
		@Override
		public String toString() {
			return "Datasource [key=" + key + ", url=" + url + ", user=" + user + ", pass=" + pass + "]";
		}

    }
}
