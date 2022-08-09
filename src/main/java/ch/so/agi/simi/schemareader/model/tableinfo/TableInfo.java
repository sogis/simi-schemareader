package ch.so.agi.simi.schemareader.model.tableinfo;

import ch.so.agi.simi.schemareader.model.tablelisting.TableShortInfo;

public class TableInfo extends TableShortInfo {
	
	private String description;
	private String pkField;
	private boolean  dbView;

	public boolean isDbView() {
		return dbView;
	}

	public void setDbView(boolean dbView) {
		this.dbView = dbView;
	}
		
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPkField() {
		return pkField;
	}
	public void setPkField(String pkField) {
		this.pkField = pkField;
	}
}
