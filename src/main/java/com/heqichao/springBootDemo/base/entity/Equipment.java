package com.heqichao.springBootDemo.base.entity;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.heqichao.springBootDemo.base.util.StringUtil;

/**
 * @author Muzzy Xu.
 */
@Component("equipments")
public class Equipment extends BaseEntity  {


	private static final long serialVersionUID = -1596449961625624849L;
	private String name;//设备名称
	private String devId;//设备编号
	private String typeCd;//设备类型
	private String typeName;//设备类型
	private Integer modelId;//模板id
	private Integer groupId;
    private Integer groupAdmId;
    private Integer appId;//应用id
    private String modelName;//模板名称
    private String groupName;//分组名称
    private String appName;//应用名称
    private String verification;//验证码
    private String supportCode;//厂商ID
    private String supporter;//厂商名称
    private Integer uid;//所属用户
    private String uName;//所属用户名称
    private String site;//经纬度
    private String address;//位置
    private String remark;
    private String online;//在离线
    private String valid;//有效标志
    private boolean validCMD;//是否存在下发命令
    
    public Equipment() {
    	
    }
    public Equipment(Map map) {
    	super.id = StringUtil.getIntegerByMap(map,"id");
    	this.name = StringUtil.getStringByMap(map,"name");
    	this.devId = StringUtil.getStringByMap(map,"devId");
    	this.groupId =StringUtil.getIntegerByMap(map,"groupId");
    	this.modelId = StringUtil.getIntegerByMap(map,"modelId");
    	this.typeCd = StringUtil.getStringByMap(map,"typeCd");
    	this.appId = StringUtil.getIntegerByMap(map,"appId");
    	this.verification = StringUtil.getStringByMap(map,"verification");
    	this.supportCode = StringUtil.getStringByMap(map,"supportCode");
    	this.supporter = StringUtil.getStringByMap(map,"supporter");
    	this.site = StringUtil.getStringByMap(map,"site");
    	this.address = StringUtil.getStringByMap(map,"address");
    	this.remark = StringUtil.getStringByMap(map,"remark");
    	this.uid = StringUtil.getIntegerByMap(map,"uid");
    }
    
    public Equipment(Map map,String type) {
    	this.name = StringUtil.getStringByMap(map,"name");
    	this.devId = StringUtil.getStringByMap(map,"devId");
    	this.typeName =StringUtil.getStringByMap(map,"typeName");
    	this.modelName = StringUtil.getStringByMap(map,"modelName");
    	this.typeCd = type;
    	this.groupName = StringUtil.getStringByMap(map,"groupName");
    	this.verification = StringUtil.getStringByMap(map,"verification");
    	this.supportCode = StringUtil.getStringByMap(map,"supportCode");
    	this.supporter = StringUtil.getStringByMap(map,"supporter");
    	this.site = StringUtil.getStringByMap(map,"site");
    	this.appName = StringUtil.getStringByMap(map,"appName");
    	this.remark = StringUtil.getStringByMap(map,"remark");
    	this.uName = StringUtil.getStringByMap(map,"uName");
    }
    
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public String getTypeCd() {
		return typeCd;
	}
	public void setTypeCd(String typeCd) {
		this.typeCd = typeCd;
	}
	public Integer getModelId() {
		return modelId;
	}
	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getGroupAdmId() {
		return groupAdmId;
	}
	public void setGroupAdmId(Integer groupAdmId) {
		this.groupAdmId = groupAdmId;
	}
	public Integer getAppId() {
		return appId;
	}
	public void setAppId(Integer appId) {
		this.appId = appId;
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
	public String getVerification() {
		return verification;
	}
	public void setVerification(String verification) {
		this.verification = verification;
	}
	public String getSupportCode() {
		return supportCode;
	}
	public void setSupportCode(String supportCode) {
		this.supportCode = supportCode;
	}
	public String getSupporter() {
		return supporter;
	}
	public void setSupporter(String supporter) {
		this.supporter = supporter;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getOnline() {
		return online;
	}
	public void setOnline(String online) {
		this.online = online;
	}
	public String getValid() {
		return valid;
	}
	public void setValid(String valid) {
		this.valid = valid;
	}
	public String getuName() {
		return uName;
	}
	public void setuName(String uName) {
		this.uName = uName;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public boolean isValidCMD() {
		return validCMD;
	}
	public void setValidCMD(boolean validCMD) {
		this.validCMD = validCMD;
	}
    
    
	

}
