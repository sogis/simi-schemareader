package ch.so.agi.schemareader.model.tablelisting;

public class TableShortInfo {
	
	private String schema;
	private String tableOrView;
	
	public TableShortInfo initWithDummies() {
		schema = "mySchema";
		tableOrView = "myTable";
		
		return this;
	}	
	
	
	public String getSchema() {
		return schema;
	}
	public void setSchema(String schema) {
		this.schema = schema;
	}
	public String getTableOrView() {
		return tableOrView;
	}
	public void setTableOrView(String tableOrView) {
		this.tableOrView = tableOrView;
	}
}

