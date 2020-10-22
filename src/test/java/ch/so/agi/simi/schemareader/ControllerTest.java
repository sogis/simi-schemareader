package ch.so.agi.simi.schemareader;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import ch.so.agi.simi.schemareader.model.tableinfo.FieldInfo;
import ch.so.agi.simi.schemareader.model.tableinfo.TableAndFieldInfo;
import ch.so.agi.simi.schemareader.model.tablelisting.TableListing;
import ch.so.agi.simi.schemareader.query.TableInfoQuery;

@SpringBootTest(properties="spring.application.json={\"dbs\":["
		+ "{\"key\":\"valid1\",\"url\":\"jdbc:postgresql://localhost:5432/postgres\",\"user\":\"postgres\",\"pass\":\"postgres\"},"
		+ "{\"key\":\"valid2\",\"url\":\"jdbc:postgresql://localhost:5432/postgres\",\"user\":\"postgres\",\"pass\":\"postgres\"}"
		+ "]}"
		)
@AutoConfigureMockMvc
public class ControllerTest {
	
	private static final String SEARCH_URL = "/{db}";
	private static final String INFO_URL = "/{db}/{schema}/{table}";
	private static final String DB_IDENT_VALID1 = "valid1";
	private static final String DB_IDENT_VALID2 = "valid2";
		
	@Autowired
	private MockMvc client;
	
	@Autowired
	private ObjectMapper mapper;
	
	private Validator _validator;
	
	@Test
	void searchWithTableAndSchemaHintOnDb1_OK() throws Exception {
		
	    client.perform(get(SEARCH_URL, DB_IDENT_VALID1)
	            .contentType("application/json")
	            .param("table", "geo")
	    		.param("schema", "geo"))
	            .andExpect(status().isOk());
	}
	
	@Test
	void searchWithTableAndSchemaHintOnDb2_OK() throws Exception {
		
	    client.perform(get(SEARCH_URL, DB_IDENT_VALID2)
	            .contentType("application/json")
	            .param("table", "geo")
	    		.param("schema", "geo"))
	            .andExpect(status().isOk());
	}
	
	@Test
	void searchWithTableHintOnly_OK() throws Exception {
		
	    client.perform(get(SEARCH_URL, DB_IDENT_VALID1)
	            .contentType("application/json")
	            .param("table", "geo"))
	            .andExpect(status().isOk());
		
	}
	
	@Test
	void searchWithSchemaHintOnly_OK() throws Exception {
		
	    client.perform(get(SEARCH_URL, DB_IDENT_VALID1)
	            .contentType("application/json")
	            .param("schema", "geo"))
	            .andExpect(status().isOk());
		
	}
	
	@Test
	void searchGeo_3TableInfo_OK() throws Exception {
		
		MvcResult mvcResult = client.perform(get(SEARCH_URL, DB_IDENT_VALID1)
	            .contentType("application/json")
	            .param("table", "geo")
	    		.param("schema", "geo"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		TableListing listing = mapper.readValue(mvcResult.getResponse().getContentAsString(), TableListing.class);		
		assertEquals(listing.getTableViewList().size(), 3, "Search pattern *geo*.*geo* must return 3 table matches");
	}
	
	@Test
	void searchWithoutHints_BadRequest() throws Exception {
		
	    client.perform(get(SEARCH_URL, DB_IDENT_VALID1)
	            .contentType("application/json"))
	            .andExpect(status().isBadRequest());

	}
	
	@Test
	void searchOnUnknownDb_BadRequest() throws Exception {
		
	    client.perform(get(SEARCH_URL, "non_existing")
	            .contentType("application/json")
	            .param("table", "geo"))
	            .andExpect(status().isBadRequest());

	}
	
	// *************************** TableInfo Tests ************************************************
	
	@Test
	void infoWithClassComment_OK() throws Exception {
		
		MvcResult mvcResult = client.perform(get(INFO_URL, DB_IDENT_VALID1, "comments", "clscomment")
	            .contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		TableAndFieldInfo tfi = mapTfiFromResult(mvcResult);
		assertTrue(StringUtils.isNotBlank(tfi.getTableInfo().getDescription()), "Table comment must not be null");
	}
	
	@Test
	void infoWithFieldComment_OK() throws Exception {
		
		MvcResult mvcResult = client.perform(get(INFO_URL, DB_IDENT_VALID1, "comments", "attrcomment")
	            .contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		TableAndFieldInfo tfi = mapTfiFromResult(mvcResult);
		
		boolean found = false;
		
		for(FieldInfo fi : tfi.getFields()) {
						
			if(StringUtils.isNotBlank(fi.getDescription()))
				found = true;
			
		}
		assertTrue(found, "Table contains field with description");
	}
	
	@Test
	void infoWithPrimaryKeyField_OK() throws Exception {
		
		MvcResult mvcResult = client.perform(get(INFO_URL, DB_IDENT_VALID1, "pkey", "withpk")
	            .contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		TableAndFieldInfo tfi = mapTfiFromResult(mvcResult);
	
		String pkFieldName = tfi.getTableInfo().getPkField();
		assertNotNull(pkFieldName, "pkFieldName must not be null for table 'WithPk'");		
	}
	
	@Test
	void infoWithNoPrimaryKeyField_OK() throws Exception {
		
		MvcResult mvcResult = client.perform(get(INFO_URL, DB_IDENT_VALID1, "pkey", "withoutpk")
	            .contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		TableAndFieldInfo tfi = mapTfiFromResult(mvcResult);
		
		String pkFieldName = tfi.getTableInfo().getPkField();
		assertTrue(pkFieldName == null, "pkFieldName must be null for table 'WithoutPk'");		
	}
	
	@Test
	void infoNoGeoField_OK() throws Exception {
		
		MvcResult mvcResult = client.perform(get(INFO_URL, DB_IDENT_VALID1, "geo", "geo0")
	            .contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		TableAndFieldInfo tfi = mapTfiFromResult(mvcResult);
		
		assertGeoFieldCount(tfi, 0);
	}
	
	@Test
	void infoOneGeoField_OK() throws Exception {
		
		MvcResult mvcResult = client.perform(get(INFO_URL, DB_IDENT_VALID1, "geo", "geo1")
	            .contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		TableAndFieldInfo tfi = mapTfiFromResult(mvcResult);
		
		assertGeoFieldCount(tfi, 1);
	}
	
	@Test
	void infoTwoGeoField_OK() throws Exception {
		
		MvcResult mvcResult = client.perform(get(INFO_URL, DB_IDENT_VALID1, "geo", "geo2")
	            .contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		TableAndFieldInfo tfi = mapTfiFromResult(mvcResult);
		
		assertGeoFieldCount(tfi, 2);
	}
		
	@Test
	void infoFieldTypes_areValid() throws Exception {
		
		MvcResult mvcResult = client.perform(get(INFO_URL, DB_IDENT_VALID1, "attr_types", "attrtypes")
	            .contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		TableAndFieldInfo tfi = mapTfiFromResult(mvcResult);
		
		Map<String, String> map = new Hashtable<>();
		map.put("t_id", "int8");
		map.put("int32", "int4");
		map.put("charmax", "text");
		map.put("char255", "varchar");		
		map.put("myuid", "uuid");
		map.put("dateonly", "date");
		map.put("datewithtime", "timestamp");	
		
		int numFieldMatch = 0;
		for(FieldInfo fi:tfi.getFields()) {
			String expectedType = map.get(fi.getName());
			
			if(expectedType != null) {
				assertEquals(expectedType, fi.getType());
				numFieldMatch++;
			}	
		}
		
		assertEquals(map.size(), numFieldMatch, "Fieldnames in unittest and testdb do not match");
	}
	
	@Test
	void infoFieldNullabilities_areValid() throws Exception {
		
		MvcResult mvcResult = client.perform(get(INFO_URL, DB_IDENT_VALID1, "attr_types", "mandopt")
	            .contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		TableAndFieldInfo tfi = mapTfiFromResult(mvcResult);
		
		int numFieldMatch = 0;
		
		for(FieldInfo fi:tfi.getFields()) {
			
			if("mand".equals(fi.getName())) {
				assertTrue(fi.isMandatory(), "Field 'mand' must be mandatory");
				numFieldMatch++;
			}
			else if ("opt".equals(fi.getName())) {
				assertFalse(fi.isMandatory(), "Field 'opt' must not be mandatory");
				numFieldMatch++;
			}
		}
		
		assertEquals(2, numFieldMatch, "Fieldnames in unittest and testdb do not match");
	}

	@Test
	void infoFieldLengths_areValid() throws Exception {
		
		MvcResult mvcResult = client.perform(get(INFO_URL, DB_IDENT_VALID1, "attr_types", "charlength")
	            .contentType("application/json"))
	            .andExpect(status().isOk())
	            .andReturn();
		
		TableAndFieldInfo tfi = mapTfiFromResult(mvcResult);
		
		int numFieldMatch = 0;
		
		for(FieldInfo fi:tfi.getFields()) {
			
			if("char255".equals(fi.getName())) {
				assertEquals(Integer.valueOf(255), fi.getLength(), "FieldInfo for field 'char255' must return length 255");
				numFieldMatch++;
			}
			else if ("charmax".equals(fi.getName())) {
				assertNull(fi.getLength(), "Fieldinfo for field 'charmax' must not have a field length");
				numFieldMatch++;
			}
		}	
		
		assertEquals(2, numFieldMatch, "Fieldnames in unittest and testdb do not match");
	}
	
	private void assertGeoFieldCount(TableAndFieldInfo tfi, int numGeoFields) {
		
		int geoCount = 0;
		
		for(FieldInfo fi:tfi.getFields()) {
			if(fi.getGeoFieldType() != null)
				geoCount++;
		}
		
		assertEquals(geoCount, numGeoFields, 
				MessageFormat.format("Expected {0} geometry fields in table, but got {1}", numGeoFields, geoCount)
				);
	}
	
	private TableAndFieldInfo mapTfiFromResult(MvcResult result) throws Exception {
		TableAndFieldInfo ti = mapper.readValue(result.getResponse().getContentAsString(), TableAndFieldInfo.class);
		assertValid(ti);
		
		return ti;
	}
	
	private Validator validator() {
		if(_validator == null) {
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			_validator = factory.getValidator();
		}
		return _validator;
	}
	
	private void assertValid(TableAndFieldInfo tfi) {
		
		Set<ConstraintViolation<TableAndFieldInfo>> violations = validator().validate(tfi);
		
		if(violations == null || violations.size() == 0)
			return;
		
		List<String> messages = new ArrayList<>();		
		
		for (ConstraintViolation<TableAndFieldInfo> violation : violations) {
		    messages.add(violation.getMessage()); 
		}
		
		throw new RuntimeException(
				MessageFormat.format("Bean validation for TableAndFieldInfo failed: {0}", messages.toString())
				);
	}
}



