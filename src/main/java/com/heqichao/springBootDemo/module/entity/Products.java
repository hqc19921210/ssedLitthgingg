package com.heqichao.springBootDemo.module.entity;

import org.springframework.stereotype.Component;

import com.heqichao.springBootDemo.base.entity.BaseEntity;

/**
 * @author Muzzy Xu.
 * @date 2019/06/12
 */
@Component("products")
public class Products extends BaseEntity  {


	/**
	 * 
	 */
	private static final long serialVersionUID = -1814874138441833933L;
	
	private String name;//产品名称
	private String typeCd;//设备类型
	private Integer modelId;//对应模板
	private Integer appId;//对应应用
	private Integer vuId;//指定用户
	private String remark;//描述
	private String valid;//有效标志：N正常，D删除
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getModelId() {
		return modelId;
	}
	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public Integer getVuId() {
		return vuId;
	}
	public void setVuId(Integer vuId) {
		this.vuId = vuId;
	}
	public String getTypeCd() {
		return typeCd;
	}
	public void setTypeCd(String typeCd) {
		this.typeCd = typeCd;
	}
    
    

}
