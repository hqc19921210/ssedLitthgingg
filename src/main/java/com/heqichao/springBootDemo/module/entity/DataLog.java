package com.heqichao.springBootDemo.module.entity;

import com.heqichao.springBootDemo.base.entity.BaseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by heqichao on 2018-11-27.
 */
@Component("data_log")
public class DataLog extends BaseEntity {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1988924524280495317L;

	//设备ID
    private String devId;

    //接收到的主数据内容（原文）
    private String srcData;

    //接收到的主数据内容（已转码）
    private String data;

    //接收到的主数据内容(已去除前后没用信息，已转码）
    private String mainData;

    //数据状态
    private String dataStatus;

    //设备类型
    private String devType;


    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getDevId() {
        return devId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public String getSrcData() {
        return srcData;
    }

    public void setSrcData(String srcData) {
        this.srcData = srcData;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getMainData() {
        return mainData;
    }

    public void setMainData(String mainData) {
        this.mainData = mainData;
    }
}
