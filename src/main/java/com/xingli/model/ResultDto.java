/**	
 * <br>
 * Copyright 2011 IFlyTek. All rights reserved.<br>
 * <br>			 
 * Package: com.iflytek.ywq.base.utils.dto <br>
 * FileName: ResultDto.java <br>
 * <br>
 * @version
 * @author hyzha
 * @created 2014年9月2日
 * @last Modified 
 * @history
 */

package com.xingli.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 结果DTO
 * 
 * @author hyzha
 * @lastModified
 * @history
 */
@ApiModel(description = "反馈封装类")
public class ResultDto {

	/**
	 * 数据列表的长度
	 */
	private Integer totalRows;

	/**
	 * 总页数
	 */
	private Integer totalPages;

	/**
	 * 当前页码
	 */
	private Integer currentPage;

	/**
	 * 每页包含的记录数
	 */
	private Integer pageRecorders;

	
	/**
	 * 需要传回页面的数据
	 */
	private Object data;


	@ApiModelProperty(notes = "数据列表的总记录数")
	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	@ApiModelProperty(notes = "总页数")
	public Integer getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(Integer totalPages) {
		this.totalPages = totalPages;
	}

	@ApiModelProperty(notes = "当前页码")
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	@ApiModelProperty(notes = "每页包含的记录数")
	public Integer getPageRecorders() {
		return pageRecorders;
	}

	public void setPageRecorders(Integer pageRecorders) {
		this.pageRecorders = pageRecorders;
	}

	@ApiModelProperty(notes = "内容对象")
	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}
	
}
