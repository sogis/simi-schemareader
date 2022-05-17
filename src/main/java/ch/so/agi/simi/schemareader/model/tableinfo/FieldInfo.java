package ch.so.agi.simi.schemareader.model.tableinfo;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/*
 * Information to a column of the table described with the TableInfo class.
 */
public class FieldInfo {
	
	@NotNull
	private String name;
	
	private boolean mandatory;
	
	@NotBlank
	private String type;

	private String iliEnumName;
	
	private Integer length;
	
	private String description;
	
	private String geoFieldType;
	private String geoFieldSrOrg;
	private Integer geoFieldSrId;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isMandatory() {
		return mandatory;
	}
	public void setMandatory(boolean mandatory) {
		this.mandatory = mandatory;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Integer getLength() {
		return length;
	}
	public void setLength(Integer length) {
		this.length = length;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getGeoFieldType() {
		return geoFieldType;
	}

	public void setGeoFieldType(String geoFieldType) {
		this.geoFieldType = geoFieldType;
	}

	public String getGeoFieldSrOrg() {
		return geoFieldSrOrg;
	}

	public void setGeoFieldSrOrg(String geoFieldSrOrg) {
		this.geoFieldSrOrg = geoFieldSrOrg;
	}

	public Integer getGeoFieldSrId() {
		return geoFieldSrId;
	}

	public void setGeoFieldSrId(Integer geoFieldSrId) {
		this.geoFieldSrId = geoFieldSrId;
	}

	public String getIliEnumName() {
		return iliEnumName;
	}

	public void setIliEnumName(String iliEnumName) {
		this.iliEnumName = iliEnumName;
	}
}
