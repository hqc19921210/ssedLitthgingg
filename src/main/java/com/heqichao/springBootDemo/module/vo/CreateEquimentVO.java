package com.heqichao.springBootDemo.module.vo;

import com.heqichao.springBootDemo.module.entity.LiteApplication;

/**
 * @author Muzzy Xu.
 * @date 2019/06/23
 */
public class CreateEquimentVO extends LiteApplication{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6917485361733930653L;

	private String verifyCode;
	private String deviceType;
	private String appModel;
	private String manufacturerId;
	private String devId;//修改设备devid
	private String devName;//修改设备名
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}
	public String getManufacturerId() {
		return manufacturerId;
	}
	public void setManufacturerId(String manufacturerId) {
		this.manufacturerId = manufacturerId;
	}
	public String getAppModel() {
		return appModel;
	}
	public void setAppModel(String appModel) {
		this.appModel = appModel;
	}
	public String getDevId() {
		return devId;
	}
	public void setDevId(String devId) {
		this.devId = devId;
	}
	public String getDevName() {
		return devName;
	}
	public void setDevName(String devName) {
		this.devName = devName;
	}
	
}
