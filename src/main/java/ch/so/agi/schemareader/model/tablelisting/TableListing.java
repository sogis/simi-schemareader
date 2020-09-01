package ch.so.agi.schemareader.model.tablelisting;

import java.util.List;

public class TableListing {
	
	private List<TableShortInfo> tableViewList;
	private Integer truncatedTo;
	
	public TableListing(List<TableShortInfo> tableViewList, Integer truncatedTo) {
		this.tableViewList = tableViewList;
		this.truncatedTo = truncatedTo;
	}	
	
	public List<TableShortInfo> getTableViewList() {
		return tableViewList;
	}
	
	public Integer getTruncatedTo() {
		return truncatedTo;
	}	
}
