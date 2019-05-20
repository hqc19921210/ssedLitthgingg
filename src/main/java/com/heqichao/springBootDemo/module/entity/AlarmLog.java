package com.heqichao.springBootDemo.module.entity;

import com.heqichao.springBootDemo.base.entity.BaseEntity;
import org.springframework.stereotype.Component;

/**
 * Created by heqichao on 2018-11-19.
 */
@Component("alarm_log")
public class AlarmLog extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1031545717859719314L;
	//设备ID
    private String devId;
    //模板id
    private Integer modelId;
    //属性Id
    private Integer attrId;

    private Integer settingId;

    private String alramType;

    //数据值
    private String  dataValue;


    //报警后恢复正常时的数据值
    private String  newValue;

    //日志id
    private Integer  logId;

    //单位
    private String  unit;

    private String devType;

    //数据状态
    private String dataStatus;

    //手动处理时的记录
    
    // Muzzy 
    private String modelName;
    private String attrName;
    private String paramValue;
    private String statusName;
    private String dataA;
	private String dataB;
    // End Muzzy


    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public String getAlramType() {
        return alramType;
    }

    public void setAlramType(String alramType) {
        this.alramType = alramType;
    }

    private String record;

    public Integer getSettingId() {
        return settingId;
    }

    public void setSettingId(Integer settingId) {
        this.settingId = settingId;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public String getDataStatus() {
        return dataStatus;
    }

    public void setDataStatus(String dataStatus) {
        this.dataStatus = dataStatus;
    }

    public String getDevType() {
        return devType;
    }

    public void setDevType(String devType) {
        this.devType = devType;
    }

    public String getDevId() {
        return devId;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public void setDevId(String devId) {
        this.devId = devId;
    }

    public Integer getAttrId() {
        return attrId;
    }

    public void setAttrId(Integer attrId) {
        this.attrId = attrId;
    }

    public String getDataValue() {
        return dataValue;
    }

    public void setDataValue(String dataValue) {
        this.dataValue = dataValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    // Muzzy
	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getParamValue() {
		return paramValue;
	}

	public void setParamValue(String paramValue) {
		this.paramValue = paramValue;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}
	public String getDataA() {
		return dataA;
	}

	public void setDataA(String dataA) {
		this.dataA = dataA;
	}

	public String getDataB() {
		return dataB;
	}

	public void setDataB(String dataB) {
		this.dataB = dataB;
	}
	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getAlramTypeFmt() {
		String newName=null;
		if(this.paramValue !=null) {
			newName=this.paramValue;
			if("BAB".equals(this.alramType)) {
				
				newName=newName.replaceAll("B", ", "+this.dataB);
			}else {
				newName=newName.replaceAll("B", " "+this.dataB);
				
			}
			newName=newName.replaceAll("A", " "+this.dataA);
			
		}
		return newName;
	}
    // End Muzzy
}
