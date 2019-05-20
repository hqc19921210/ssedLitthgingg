package com.heqichao.springBootDemo.module.entity;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.heqichao.springBootDemo.base.entity.BaseEntity;
import com.heqichao.springBootDemo.base.util.StringUtil;

@Component("alarm_setting")
public class AlarmSetting extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4267380782792439579L;
	
	private String name;
	private Integer modelId;
	private String modelName;
	private Integer attrId;
	private String attrName;
	private String alramType;
	private String alramTypeName;
	private String dataType;
	private String dataValue;
	private String dataA;
	private String dataB;
	private String dataEum;
	private String dataStatus;
	private String expression;
	private String actFlag;
	
	public AlarmSetting() {
		
	}
	public AlarmSetting(Map map) {
		super.id = StringUtil.getIntegerByMap(map,"id");
		this.name = StringUtil.getStringByMap(map,"name");
		this.modelId = StringUtil.getIntegerByMap(map,"modelId");
		this.attrId = StringUtil.getIntegerByMap(map,"attrId");
		this.alramType = StringUtil.getStringByMap(map,"alramType");
		this.dataA = StringUtil.getStringByMap(map,"dataA");
		this.dataB = StringUtil.getStringByMap(map,"dataB");
		this.dataEum = StringUtil.getStringByMap(map,"dataEum");
		this.dataStatus = StringUtil.getStringByMap(map,"dataStatus");
		
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getModelId() {
		return modelId;
	}
	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}
	
	public Integer getAttrId() {
		return attrId;
	}
	public void setAttrId(Integer attrId) {
		this.attrId = attrId;
	}
	public String getAlramType() {
		return alramType;
	}
	public void setAlramType(String alramType) {
		this.alramType = alramType;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getDataValue() {
		return dataValue;
	}
	public void setDataValue(String dataValue) {
		this.dataValue = dataValue;
	}
	public String getDataStatus() {
		return dataStatus;
	}
	public void setDataStatus(String dataStatus) {
		this.dataStatus = dataStatus;
	}
	public String getAttrName() {
		return attrName;
	}
	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}
	public String getModelName() {
		return modelName;
	}
	public void setModelName(String modelName) {
		this.modelName = modelName;
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
	
	public String getDataEum() {
		return dataEum;
	}
	public void setDataEum(String dataEum) {
		this.dataEum = dataEum;
	}
	
	public String getExpression() {
		return expression;
	}
	public void setExpression(String expression) {
		this.expression = expression;
	}
	
	public String getActFlag() {
		return actFlag;
	}
	public void setActFlag(String actFlag) {
		this.actFlag = actFlag;
	}
	public String getAlramTypeName() {
		return alramTypeName;
	}
	public String getAlramTypeFmt() {
		String newName=null;
		if(this.alramTypeName !=null) {
			newName=this.alramTypeName;
			if("BAB".equals(this.alramType)) {
				newName=newName.replaceAll("B", ", "+this.dataB);
				
			}else if("EUM".equals(this.alramType)) {
				newName=newName.replaceAll("A", " "+this.dataEum);
				
			}else {
				newName=newName.replaceAll("B", " "+this.dataB);
				
			}
			newName=newName.replaceAll("A", " "+this.dataA);
			
		}
		return newName;
	}
	public void setAlramTypeName(String alramTypeName) {
		this.alramTypeName = alramTypeName;
	}
	
	
	
}
