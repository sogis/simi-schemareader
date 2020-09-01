package ch.so.agi.schemareader.model.tableinfo;

import java.util.List;

public class TableAndColumnInfo {
	
	private TableInfo tableInfo;
	private List<ColumnInfo> columns;	
	
	public TableInfo getTableInfo() {
		return tableInfo;
	}

	public void setTableInfo(TableInfo tableInfo) {
		this.tableInfo = tableInfo;
	}
	
	public List<ColumnInfo> getColumns() {
		return columns;
	}

	public void setColumns(List<ColumnInfo> columns) {
		this.columns = columns;
	}
}

