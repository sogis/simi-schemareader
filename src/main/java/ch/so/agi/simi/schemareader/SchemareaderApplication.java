package ch.so.agi.simi.schemareader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class SchemareaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchemareaderApplication.class, args);
	}

}
