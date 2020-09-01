package ch.so.agi.schemareader.model.tableinfo;

import ch.so.agi.schemareader.model.tablelisting.TableShortInfo;

public class TableInfo extends TableShortInfo {
	
	private String description;
	private String pkColumn;
		
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPkColumn() {
		return pkColumn;
	}
	public void setPkColumn(String pkColumn) {
		this.pkColumn = pkColumn;
	}
}
