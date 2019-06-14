package com.heqichao.springBootDemo.module.service;

import java.util.Map;
import java.util.stream.Collectors;

import com.heqichao.springBootDemo.module.entity.Products;
import com.heqichao.springBootDemo.module.mapper.ProductsMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.util.PageUtil;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;

/**
 * 
 * @author Muzzy XU.
 * @date 2019/06/12
 */

@Service
@Transactional
public class ProductsServiceImp implements ProductsService {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ProductsMapper productsMapper;
	

	@Override
	public PageInfo queryProducts() {
		Integer cmp = ServletUtil.getSessionUser().getCompetence();
		if(cmp!=2) {
    		return null;
    	}
		Map map = RequestContext.getContext().getParamMap();
    	String name = StringUtil.getStringByMap(map,"name");
    	Integer modelId = StringUtil.getIntegerByMap(map,"modelId");
    	Integer appId = StringUtil.getIntegerByMap(map,"appId");
    	PageUtil.setPage();
        PageInfo pageInfo = new PageInfo(productsMapper.queryProducts(
        		name,modelId,appId
        		));
		return pageInfo;
	}
	
	/**
	 * 添加修改产品
	 */
	@Override
    public ResponeResult editProduct() {
		Map map = RequestContext.getContext().getParamMap();
		String edit = StringUtil.getStringByMap(map,"edit");
		Products pro = JSONObject.parseObject(JSONObject.toJSONString(map), Products.class);
    	Integer uid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(pro.getName() == null || uid == null || cmp != 2) {
    		return new ResponeResult(true,"Add Product Input Error!","errorMsg");
    	}
    	pro.setUdpUid(uid);	
    	if(edit.equals("A")) {
    		if(productsMapper.duplicatedProductsName(pro.getName())) {
    			return new ResponeResult(true,"产品名称重复","errorMsg");
    		}
    		if(productsMapper.insertProducts(pro)>0) {
    			return new ResponeResult();
    		}
    		
    	}else if(edit.equals("U")) {
    		if(productsMapper.updateProducts(pro)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Add Product fail","errorMsg");
    }
	

	@Override
    public ResponeResult deleteProductsById(Integer id) {
    	Integer udid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(  udid == null || cmp != 2) {
    		return new ResponeResult(true,"Delete fail!","errorMsg");
    	}else {
    		if(productsMapper.deleteProductsById(id,udid)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Delete Product fail","errorMsg");
    }
	/**
	 * 获取产品下拉kv
	 */
	@Override
    public ResponeResult getProductsList() {
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	Integer uid = ServletUtil.getSessionUser().getId();
    	Integer pid = ServletUtil.getSessionUser().getParentId();
    	if(uid == null || cmp == null) {
    		return  new ResponeResult(false,"");
    	}
		Map<String, Integer> res =  productsMapper.getProductsList(uid,pid,cmp).stream().collect(
						Collectors.toMap(Products::getName,Products::getId, (k1,k2)->k1)
					);
		return new ResponeResult(res);
    }
	
	
}
