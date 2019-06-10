package com.heqichao.springBootDemo.module.wechat.entity;

import com.heqichao.springBootDemo.base.entity.BaseEntity;

public class AccessToken extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6233109655009190022L;
	
	private String openid;
	private String accessToken;
	private String refreshToken;
	private Long expiresIn;//过期时间
	private String tokenType;//token类型 B：基础token，W:网页授权token
	
	
	
	public String getOpenid() {
		return openid;
	}
	public void setOpenid(String openid) {
		this.openid = openid;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getRefreshToken() {
		return refreshToken;
	}
	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}
	public Long getExpiresIn() {
		return expiresIn;
	}
	public void setExpiresIn(Long expiresIn) {
		this.expiresIn = expiresIn;
	}
	public String getTokenType() {
		return tokenType;
	}
	public void setTokenType(String tokenType) {
		this.tokenType = tokenType;
	}
	
	
	
	

}
