package ch.so.agi.schemareader.model.tableinfo;

import java.util.Hashtable;

import ch.so.agi.schemareader.model.tablelisting.TableShortInfo;

public class TableInfo extends TableShortInfo {
	
	private String description;
	private String pkColumn;
	private Hashtable<String, ColumnInfo> columns;
	
	public TableInfo initWithDummies() {
		
		super.initWithDummies();
		
		description = "lorem ipsum table fuu";
		pkColumn = "id";
		
				
		columns = new Hashtable<>();
		columns.put("id", new ColumnInfo().initWithDummies());
		columns.put("geo", new ColumnInfo().initWithDummies());
		
		return this;
	}	
	
	
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
	
	public Hashtable<String, ColumnInfo> getColumns() {
		return columns;
	}


	public void setColumns(Hashtable<String, ColumnInfo> columns) {
		this.columns = columns;
	}

}
