package com.heqichao.springBootDemo.module.vo;

import com.heqichao.springBootDemo.module.entity.Products;

public class ProductsVo extends Products {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1440272427629391009L;

	private String modelName;
	private String appName;
	private String company;
	private String typeName;
	
	
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	
}
