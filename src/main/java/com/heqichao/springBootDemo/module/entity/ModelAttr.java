package com.heqichao.springBootDemo.module.entity;

import com.heqichao.springBootDemo.base.entity.BaseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.stereotype.Component;

/**
 * Created by heqichao on 2018-11-19.
 */
@Component("model_attr")
public class ModelAttr extends BaseEntity {
    /**
	 * 
	 */
	private static final long serialVersionUID = -3452535801781802193L;
	//模板ID
    private Integer modelId;
    //属性名
    private String attrName;
    //数据类型
    private String dataType;
   //数值类型
    private String valueType;


    //小数位数
    private Integer numberFormat;
    //整型最大值
    private Integer numMax;
    //整型最小值
    private Integer numMin;
    //单位
    private String unit;
    //公式
    private String expression;
    //备注
    private String memo;
    //排序
    private int orderNo;
    

    
    

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public Integer getModelId() {
        return modelId;
    }

    public void setModelId(Integer modelId) {
        this.modelId = modelId;
    }

    public String getAttrName() {
        return attrName;
    }

    public void setAttrName(String attrName) {
        this.attrName = attrName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getValueType() {
        return valueType;
    }

    public void setValueType(String valueType) {
        this.valueType = valueType;
    }
    
    




	public Integer getNumberFormat() {
        return numberFormat;
    }

    public void setNumberFormat(Integer numberFormat) {
        this.numberFormat = numberFormat;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }



	public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }


    
}
