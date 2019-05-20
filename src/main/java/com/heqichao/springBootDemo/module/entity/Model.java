package com.heqichao.springBootDemo.module.entity;

import com.heqichao.springBootDemo.base.entity.BaseEntity;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by heqichao on 2018-11-17.
 */
@Component("model")
public class Model extends BaseEntity{
    /**
	 * 
	 */
	private static final long serialVersionUID = 6900356724930931191L;
	//模板名字
    private String modelName;

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }
    
    public Model() {
    	
    }


    public Model(String modelName, Integer userId, Date date) {
        this.modelName = modelName;
        this.addUid=userId;
        this.udpUid=userId;
        this.addDate=date;
        this.udpDate=date;
    }

}
