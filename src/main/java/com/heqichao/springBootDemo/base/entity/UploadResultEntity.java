package com.heqichao.springBootDemo.base.entity;

import org.springframework.stereotype.Component;

@Component("upload_result")
public class UploadResultEntity extends BaseEntity {

	private static final long serialVersionUID = -9095640443323759206L;
	
	private String resKey;//所属上传号
	private Integer resIndex;//行号
	private Integer resStatus;//结果
	private String errReason;//错误原因
	
	
	public String getResKey() {
		return resKey;
	}
	public void setResKey(String resKey) {
		this.resKey = resKey;
	}
	
	public Integer getResIndex() {
		return resIndex;
	}
	public void setResIndex(Integer resIndex) {
		this.resIndex = resIndex;
	}
	
	public Integer getResStatus() {
		return resStatus;
	}
	public void setResStatus(Integer resStatus) {
		this.resStatus = resStatus;
	}
	public String getErrReason() {
		return errReason;
	}
	public void setErrReason(String errReason) {
		this.errReason = errReason;
	}
	
	
}
