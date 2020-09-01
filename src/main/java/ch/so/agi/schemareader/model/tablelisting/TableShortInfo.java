package ch.so.agi.schemareader.model.tablelisting;

public class TableShortInfo {
	
	private String schemaName;
	private String tableOrViewName;	
	
	public String getSchemaName() {
		return schemaName;
	}
	public void setSchemaName(String schema) {
		this.schemaName = schema;
	}
	public String getTvName() {
		return tableOrViewName;
	}
	public void setTvName(String tvName) {
		this.tableOrViewName = tvName;
	}
}

