package com.heqichao.springBootDemo.base.entity;

/**
 * 
 * @author Muzzy
 * 
 *
 */
public class ParamObject  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6072019142836242310L;

	private String excelParam;
	private String dataPointExcelParam;
	private String dataPointExcelStart;
	private String dataPointExcelEnd;
	
	public ParamObject() {
		
	}
	
	public void clear() {
		this.excelParam = null;
		this.dataPointExcelParam = null;
		this.dataPointExcelStart = null;
		this.dataPointExcelEnd = null;
	}
	

	public String getExcelParam() {
		return excelParam;
	}

	public void setExcelParam(String excelParam) {
		this.excelParam = excelParam;
	}

	public String getDataPointExcelParam() {
		return dataPointExcelParam;
	}

	public void setDataPointExcelParam(String dataPointExcelParam) {
		this.dataPointExcelParam = dataPointExcelParam;
	}

	public String getDataPointExcelStart() {
		return dataPointExcelStart;
	}

	public void setDataPointExcelStart(String dataPointExcelStart) {
		this.dataPointExcelStart = dataPointExcelStart;
	}

	public String getDataPointExcelEnd() {
		return dataPointExcelEnd;
	}

	public void setDataPointExcelEnd(String dataPointExcelEnd) {
		this.dataPointExcelEnd = dataPointExcelEnd;
	}
	
	
}
