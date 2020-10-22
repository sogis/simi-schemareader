package ch.so.agi.simi.schemareader.model.tablelisting;

import java.util.List;

public class TableListing {
	
	private List<TableShortInfo> tableViewList;
	private Integer truncatedTo;
	
	public List<TableShortInfo> getTableViewList() {
		return tableViewList;
	}
	
	public Integer getTruncatedTo() {
		return truncatedTo;
	}	
	
	public void setTableViewList(List<TableShortInfo> tableViewList) {
		this.tableViewList = tableViewList;
	}

	public void setTruncatedTo(Integer truncatedTo) {
		this.truncatedTo = truncatedTo;
	}
}
