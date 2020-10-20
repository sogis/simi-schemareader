package ch.so.agi.schemareader.model.tableinfo;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class TableAndFieldInfo {
	
	@NotNull
	@Valid
	private TableInfo tableInfo;
	
	@NotEmpty
	private List<@Valid FieldInfo> fields;	
	
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

