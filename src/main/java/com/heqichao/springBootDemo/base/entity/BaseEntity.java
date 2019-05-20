package com.heqichao.springBootDemo.base.entity;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by heqichao on 2018-7-1.
 */
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;


    //主键
    protected Integer id;

    //保存时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    protected Date addDate;

    //更新时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    protected Date udpDate;

    //保存人
    protected Integer addUid;

    //更新人
    protected Integer udpUid;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }
	public Date getAddDate() {
		return addDate;
	}
	public void setAddDate(Date addDate) {
		this.addDate = addDate;
	}
	public Date getUdpDate() {
		return udpDate;
	}
	public void setUdpDate(Date udpDate) {
		this.udpDate = udpDate;
	}
	public Integer getAddUid() {
		return addUid;
	}
	public void setAddUid(Integer addUid) {
		this.addUid = addUid;
	}
	public Integer getUdpUid() {
		return udpUid;
	}
	public void setUdpUid(Integer udpUid) {
		this.udpUid = udpUid;
	}

    
}
