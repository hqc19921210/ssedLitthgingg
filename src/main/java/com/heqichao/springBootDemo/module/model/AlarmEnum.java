package com.heqichao.springBootDemo.module.model;

import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.AlarmSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by heqichao on 2018-12-16.
 */
@Component
public enum  AlarmEnum implements AlarmAnalyze{
    SOFF("SOFF","开关型_关") {
        @Override
        public boolean execute(AlarmSetting setting, String value) {
            if(StringUtil.isNotEmpty(value)){
                return  "0001".equals(value)?true:false;
            }
            return false;
        }
    }
    ,SON("SON","开关型_开") {
        @Override
        public boolean execute(AlarmSetting setting, String value) {
            if(StringUtil.isNotEmpty(value)){
                return  "0000".equals(value)?true:false;
            }
            return false;
        }
    }
    ,EUM("SON","开关型_开") {
    	@Override
    	public boolean execute(AlarmSetting setting, String value) {
    		if(StringUtil.isNotEmpty(value)){
    			return  setting.getDataEum().equals(value)?true:false;
    		}
    		return false;
    	}
    }
    ,LA("LA","数值低于A") {
        @Override
        public boolean execute(AlarmSetting setting, String value) {
            if(setting!=null &&  StringUtil.isNotEmpty(value) && StringUtil.isNotEmpty(setting.getDataA()) ){
                try{
                        BigDecimal v =new BigDecimal(value);
                        BigDecimal a =new BigDecimal(setting.getDataA());
                        return   v.subtract(a).doubleValue() <0;
                }catch (Exception e){
                    logger.error("判断报警异常",e);
                }
            }
            return false;
        }
    }
    ,HB("HB","数值高于B") {
        @Override
        public boolean execute(AlarmSetting setting, String value) {
            if(setting!=null &&  StringUtil.isNotEmpty(value)  && StringUtil.isNotEmpty(setting.getDataB())){
                try{
                        BigDecimal v =new BigDecimal(value);
                        BigDecimal b =new BigDecimal(setting.getDataB());
                        return   v.subtract(b).doubleValue() >0;
                }catch (Exception e){
                    logger.error("判断报警异常",e);
                }
            }
            return false;
        }
    }
    ,BAB("BAB","数值介于AB之间") {
        @Override
        public boolean execute(AlarmSetting setting, String value) {
            if(setting!=null &&  StringUtil.isNotEmpty(value) && StringUtil.isNotEmpty(setting.getDataA()) && StringUtil.isNotEmpty(setting.getDataB())){
                try{
                        BigDecimal v =new BigDecimal(value);
                        BigDecimal a =new BigDecimal(setting.getDataA());
                        BigDecimal b =new BigDecimal(setting.getDataB());
                        return  v.subtract(b).doubleValue() <0 &&  v.subtract(a).doubleValue() >0;
                }catch (Exception e){
                    logger.error("判断报警异常",e);
                }
            }
            return false;
        }
    }
    ,OAB("OAB","数值高于B或低于A") {
        @Override
        public boolean execute(AlarmSetting setting, String value) {
            if(setting!=null &&  StringUtil.isNotEmpty(value) && StringUtil.isNotEmpty(setting.getDataA()) && StringUtil.isNotEmpty(setting.getDataB())){
                try{
                        BigDecimal v =new BigDecimal(value);
                        BigDecimal a =new BigDecimal(setting.getDataA());
                        BigDecimal b =new BigDecimal(setting.getDataB());
                        return  v.subtract(a).doubleValue() <0 || v.subtract(b).doubleValue() >0;
                }catch (Exception e){
                    logger.error("判断报警异常",e);
                }
            }
            return false;
        }
    }
    ,EA("EA","数值等于A") {
        @Override
        public boolean execute(AlarmSetting setting, String value) {
            if(setting!=null&&  StringUtil.isNotEmpty(value) && StringUtil.isNotEmpty(setting.getDataA()) ){
                try{
                        BigDecimal v =new BigDecimal(value);
                        BigDecimal a =new BigDecimal(setting.getDataA());
                        return !(Math.abs(v.subtract(a).doubleValue())> 0) ;
                }catch (Exception e){
                    logger.error("判断报警异常",e);
                }
            }
            return false;
        }
    }
    ;

    private String code;
    private String name;

    AlarmEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }


    public static AlarmEnum getEnumByCode(String code){
        if(StringUtil.isNotEmpty(code)){
            return AlarmEnum.valueOf(code);
        }
        return null;
    }
    static Logger logger = LoggerFactory.getLogger(AlarmEnum.class);
}
