package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.param.ResponeResult;

public interface ProductsService {
	// 正常
	static final String NORMAL = "N"; 
	// 删除
	static final String DELETE = "D";
	PageInfo queryProducts();
	ResponeResult getProductsList();
	ResponeResult deleteProductsById(Integer id);
	ResponeResult editProduct();
	
	
	
	
	

}
