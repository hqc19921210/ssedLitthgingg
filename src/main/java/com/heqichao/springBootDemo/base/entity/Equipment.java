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
	private Integer modelId;//模板id
	private Integer groupId;
    private Integer groupAdmId;//管理员看到的分组
    private Integer appId;//应用id
    private Integer proId;//产品id
    private String verification;//验证码
    private String supportCode;//厂商ID
    private String supporter;//厂商名称
    private Integer uid;//所属用户
    private String site;//经纬度
    private String address;//位置
    private String remark;
    private String secRemark;// 灰色备注
    private String online;//在离线
    private String valid;//有效标志
    
    
    public Equipment() {
    	
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
	
	public String getSecRemark() {
		return secRemark;
	}
	public void setSecRemark(String secRemark) {
		this.secRemark = secRemark;
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
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getProId() {
		return proId;
	}

	public void setProId(Integer proId) {
		this.proId = proId;
	}
	
    
    
	

}
