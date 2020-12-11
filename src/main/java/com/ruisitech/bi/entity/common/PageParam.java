package com.ruisitech.bi.entity.common;


public class PageParam {
	/**
	 * 分页参数
	 */
	private Integer total;
	private Integer page;  //页数， 从1开始
	private Integer rows;

	//搜索
	private String search;

	public Integer getTotal() {
		if(total==null){
			total = 0;
		}
		return total;
	}
	public void setTotal(Integer total) {
		this.total = total;
	}
	
	public Integer getPage() {
		if(page == null){
			page = 1;
		}
		return page;
	}
	public void setPage(Integer page) {
		this.page = page;
	}
	public Integer getRows() {
		if(rows == null){
			rows = 10;
		}
		return rows;
	}
	public void setRows(Integer rows) {
		this.rows = rows;
	}
	@Override
	public String toString() {
		return "PageParam [total=" + total + ", pageIndex=" + page
				+ ", pageSize=" + rows + "]";
	}

	public String getSearch() {
		return search;
	}
}
