package ch.so.agi.schemareader.model.tableinfo;

/*
 * Information to a column of the table described with the TableInfo class.
 */
public class ColumnInfo {
	
	private String name;
	private boolean mandatory;
	private String type;
	private Integer length;
	private String description;
	private String geoColType;
	private String geoColSrOrg;
	private Integer geoColSrID;
	
	public ColumnInfo initWithDummies() {
		this.name = "myCol";
		this.mandatory = true;
		this.type = "myType";
		this.length = 256;
		this.description = "lorem ipsum";
		this.geoColType = "POINT";
		this.geoColSrOrg = "EPSG";
		this.geoColSrID = 2056;
		
		return this;
	}	
	
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
	public String getGeoColType() {
		return geoColType;
	}
	public void setGeoColType(String geoColType) {
		this.geoColType = geoColType;
	}
	public String getGeoColSrOrg() {
		return geoColSrOrg;
	}
	public void setGeoColSrOrg(String geoColSrOrg) {
		this.geoColSrOrg = geoColSrOrg;
	}
	public Integer getGeoColSrId() {
		return geoColSrID;
	}
	public void setGeoColSrId(Integer geoColSrID) {
		this.geoColSrID = geoColSrID;
	}
}
