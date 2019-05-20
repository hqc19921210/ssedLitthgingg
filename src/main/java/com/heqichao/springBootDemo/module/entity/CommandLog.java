package com.heqichao.springBootDemo.module.entity;

import com.heqichao.springBootDemo.base.entity.BaseEntity;
import org.springframework.stereotype.Component;

/**
 * @author MuzzyXu.
 * @date 2019-4-14.
 */
@Component("command_log")
public class CommandLog extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3293955663533464067L;
	
	private String devId;//设备DevID
    private String cmdId;//电信云返回下发id
    private String cmdStatus;//下发状态
    private String callback;//下发返回信息
    private String request;//订阅返回信息
    private String context;//格式化的下发内容
    private String valid;
    
    public CommandLog() {
    	
    }


    
	public String getDevId() {
		return devId;
	}



	public void setDevId(String devId) {
		this.devId = devId;
	}



	public String getCmdId() {
		return cmdId;
	}

	public void setCmdId(String cmdId) {
		this.cmdId = cmdId;
	}

	public String getCmdStatus() {
		return cmdStatus;
	}

	public void setCmdStatus(String cmdStatus) {
		this.cmdStatus = cmdStatus;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getRequest() {
		return request;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getValid() {
		return valid;
	}

	public void setValid(String valid) {
		this.valid = valid;
	}

    
}
