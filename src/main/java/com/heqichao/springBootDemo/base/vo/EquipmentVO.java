package com.heqichao.springBootDemo.base.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.heqichao.springBootDemo.base.entity.Equipment;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentVO extends Equipment {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1089094094553594745L;

	private String typeName;//设备类型
	private String modelName;//模板名称
    private String groupName;//分组名称
    private String appName;//应用名称
    private String proName;//产品名称
    private String uName;//所属用户名称
    private boolean validCMD;//是否存在下发命令
    private Integer validWechat;//是否绑定微信
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public boolean isValidCMD() {
		return validCMD;
	}
	public void setValidCMD(boolean validCMD) {
		this.validCMD = validCMD;
	}
	public Integer getValidWechat() {
		return validWechat;
	}
	public void setValidWechat(Integer validWechat) {
		this.validWechat = validWechat;
	}
    
    
}
