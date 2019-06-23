package com.heqichao.springBootDemo.module.vo;

import java.io.Serializable;

public class AddDeviceVO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7533025831177214567L;

	private String deviceId;
	private String verifyCode;
	private String psk;
	private String timeout;
	private String status;
	private String errorContext;
	
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getVerifyCode() {
		return verifyCode;
	}
	public void setVerifyCode(String verifyCode) {
		this.verifyCode = verifyCode;
	}
	public String getPsk() {
		return psk;
	}
	public void setPsk(String psk) {
		this.psk = psk;
	}
	public String getTimeout() {
		return timeout;
	}
	public void setTimeout(String timeout) {
		this.timeout = timeout;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getErrorContext() {
		return errorContext;
	}
	public void setErrorContext(String errorContext) {
		this.errorContext = errorContext;
	}
	
	
}
