package ch.so.agi.schemareader.model.tableinfo;

import java.util.List;

public class TableAndFieldInfo {
	
	private TableInfo tableInfo;
	private List<FieldInfo> fields;	
	
	public TableInfo getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}
	
	public List<FieldInfo> getFields() {
		return fields;
	}

	public void setFields(List<FieldInfo> columns) {
		this.fields = columns;
	}
}

