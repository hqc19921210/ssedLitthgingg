package com.heqichao.springBootDemo.module.model;

import com.heqichao.springBootDemo.base.util.MathUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.liteNA.LiteNAStringUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.NumberFormat;
import java.util.*;

/**
 * Created by heqichao on 2018-11-20.
 */
public enum AttrEnum implements AttrAnalyze{

    /**
     * 数值型
     */
    INT_TYPE__TWO_UNSIGNED("INT_TYPE","数值型","TWO_UNSIGNED","2字节无符号整数",2){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                    char[] srcContext = context.toCharArray();
                    String d1=""+srcContext[0]+srcContext[1];
                    String d2=""+srcContext[2]+srcContext[3];
                    String data = MathUtil.convertTwoBytesToUnSignedInt(d1,d2);
                    if(modelAttr ==null){
                       return data;
                    }
                    result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumberFormat(),data);
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    },

    INT_TYPE__TWO_SIGNED("INT_TYPE","数值型","TWO_SIGNED","2字节有符号整数",2){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                    char[] srcContext = context.toCharArray();
                    String d1=""+srcContext[0]+srcContext[1];
                    String d2=""+srcContext[2]+srcContext[3];
                    String data=MathUtil.convertTwoBytesToSignedInt(d1,d2);
                    if(modelAttr ==null){
                        return data;
                    }
                    result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumberFormat(),data);
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    },

    INT_TYPE__FOUR_NOSIGNED_ABCD("INT_TYPE","数值型","FOUR_NOSIGNED_ABCD","4字节无符号整数(AB CD)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                    char[] srcContext = context.toCharArray();
                    String d1=""+srcContext[0]+srcContext[1];
                    String d2=""+srcContext[2]+srcContext[3];
                    String d3=""+srcContext[4]+srcContext[5];
                    String d4=""+srcContext[6]+srcContext[7];
                    String data=MathUtil.convertFourBytesToUnSignedInt(d1,d2,d3,d4);
                    if(modelAttr ==null){
                        return data;
                    }
                    result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumberFormat(),data);
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    },
    INT_TYPE__FOUR_NOSIGNED_CDAB("INT_TYPE","数值型","FOUR_NOSIGNED_CDAB","4字节无符号整数(CD AB)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                    char[] srcContext = context.toCharArray();
                    String d1=""+srcContext[0]+srcContext[1];
                    String d2=""+srcContext[2]+srcContext[3];
                    String d3=""+srcContext[4]+srcContext[5];
                    String d4=""+srcContext[6]+srcContext[7];
                    String data=MathUtil.convertFourBytesToUnSignedInt(d3,d4,d1,d2);
                    if(modelAttr ==null){
                        return data;
                    }
                    result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumberFormat(),data);
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    },

    INT_TYPE__FOUR_SIGNED_ABCD("INT_TYPE","数值型","FOUR_SIGNED_ABCD","4字节有符号整数(AB CD)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                    char[] srcContext = context.toCharArray();
                    String d1=""+srcContext[0]+srcContext[1];
                    String d2=""+srcContext[2]+srcContext[3];
                    String d3=""+srcContext[4]+srcContext[5];
                    String d4=""+srcContext[6]+srcContext[7];
                    String data=MathUtil.convertFourBytesToSignedInt(d1,d2,d3,d4);
                    if(modelAttr ==null){
                        return data;
                    }
                    result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumberFormat(),data);
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    },

    INT_TYPE__FOUR_SIGNED_CDAB("INT_TYPE","数值型","FOUR_SIGNED_CDAB","4字节有符号整数(CD AB)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                    char[] srcContext = context.toCharArray();
                    String d1=""+srcContext[0]+srcContext[1];
                    String d2=""+srcContext[2]+srcContext[3];
                    String d3=""+srcContext[4]+srcContext[5];
                    String d4=""+srcContext[6]+srcContext[7];
                    String data=MathUtil.convertFourBytesToSignedInt(d3,d4,d1,d2);
                    if(modelAttr ==null){
                        return data;
                    }
                    result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumberFormat(),data);
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    },
    INT_TYPE__FOURFLOAT_ABCD("INT_TYPE","数值型","FOURFLOAT_ABCD","4字节浮点型(AB CD)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                    char[] srcContext = context.toCharArray();
                    String d1=""+srcContext[0]+srcContext[1];
                    String d2=""+srcContext[2]+srcContext[3];
                    String d3=""+srcContext[4]+srcContext[5];
                    String d4=""+srcContext[6]+srcContext[7];
                    String data= MathUtil.getFloat(d1+d2+d3+d4);
                    if(modelAttr ==null){
                        return data;
                    }
                    result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumberFormat(),data);
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    },
    INT_TYPE__FOURFLOAT_CDAB("INT_TYPE","数值型","FOURFLOAT_CDAB","4字节浮点型(CD AB)",4){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                    char[] srcContext = context.toCharArray();
                    String d1=""+srcContext[0]+srcContext[1];
                    String d2=""+srcContext[2]+srcContext[3];
                    String d3=""+srcContext[4]+srcContext[5];
                    String d4=""+srcContext[6]+srcContext[7];
                    String data= MathUtil.getFloat(d3+d4+d1+d2);
                    if(modelAttr ==null){
                        return data;
                    }
                    result = MathUtil.expressAndFormat(modelAttr.getExpression(),modelAttr.getNumberFormat(),data);
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    },

    ALARM_TYPE("ALARM_TYPE","报警型",2){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                  /*  if("0000".equals(context)){
                        result="正常";
                    }else if("0001".equals(context)){
                        result="报警";
                    }else{
                        result=DATA_ERROR;
                    }*/
                    if("0000".equals(context) || "0001".equals(context)){
                        result=context;
                    }else{
                        result=DATA_ERROR;
                    }
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    },

    SWITCH_TYPE("SWITCH_TYPE","开关型",2){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                    /*if("0000".equals(context)){
                        result="开";
                    }else if("0001".equals(context)){
                        result="关";
                    }else{
                        result=DATA_ERROR;
                    }*/
                    if("0000".equals(context) || "0001".equals(context)){
                        result=context;
                    }else{
                        result=DATA_ERROR;
                    }
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    },
    
    ENUMERATION_TYPE("ENUMERATION_TYPE","枚举型",2){
    	@Override
    	public String execute(ModelAttr modelAttr, String context) {
    		String result="";
    		try{
    			if(!check(context)){
    				result=DATA_ERROR;
    			}else{
    				String[] eLst = modelAttr.getExpression().split(",");
    				for(int i = 0; i<eLst.length; i++) {
    					if(LiteNAStringUtil.int2HexString(i).equals(context)) {
    						result=eLst[i];
    						//匹配后跳出
    						break;
    					}
    				}
    				//无匹配则报错
    				if("".equals(result)){
    					result=DATA_ERROR;
    				}
    			}
    		}catch (Exception e){
    			result=DATA_ERROR;
    		}
    		
    		return result;
    	}
    },

    DATE_TYPE("DATE_TYPE","时间型",6){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                    char[] srcContext = context.toCharArray();
                    result= "20"+srcContext[0]+srcContext[1]+"-"+srcContext[2]+srcContext[3]+"-"+srcContext[4]+srcContext[5]+" "+srcContext[6]+srcContext[7]+":"+srcContext[8]+srcContext[9]+":"+srcContext[10]+srcContext[11];
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    },



    WAVE_TYPE("WAVE_TYPE","波形",null){
        @Override
        public String execute(ModelAttr modelAttr, String context) {
            //波形可能会没有
            if(StringUtil.isEmpty(context)){
                return "";
            }
            String result="";
            try{
                if(!check(context)){
                    result=DATA_ERROR;
                }else{
                    char[] srcContext = context.toCharArray();
                    //统一保留一位小数
                    NumberFormat nbf= NumberFormat.getInstance();
                    nbf.setMinimumFractionDigits(1);

                    for(int i=0;i<srcContext.length;i=i+4){
                        String data="";
                        //按照2字节有符号整形解析
                        //小端计算
                        String d1=""+srcContext[i+0]+srcContext[i+1];
                        String d2=""+srcContext[i+2]+srcContext[i+3];
                        data=MathUtil.convertTwoBytesToSignedInt(d1,d2);
                        //按照原逻辑解析
                  /*
                  double range =100;
                  double peakDouble =(double)Long.parseLong(srcContext[i]+srcContext[i+1]+srcContext[i+2]+srcContext[i+3]+"",16);
                    if(peakDouble >32768){
                        data= nbf.format((peakDouble-65536)/1000*range);
                    }else{
                        data= nbf.format(peakDouble/1000*range);
                    }*/
                        result=result+data+",";
                    }
                    if(StringUtil.isNotEmpty(result) && result.lastIndexOf(",") == result.length()-1){
                        result=result.substring(0,result.length()-1);
                    }
                }
            }catch (Exception e){
                result=DATA_ERROR;
            }

            return result;
        }
    }
    ;

    //主类型
    private String type;
    //子类型
    private String subType;
    //主类型名字
    private String typeName;
    //子类型名字
    private String subTypeName;
    //解析字节长度
    private Integer length;

    public Integer getLength() {
        return length == null ? 0 : length ;
    }

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public String getTypeName() {
        return typeName;
    }

    public String getSubTypeName() {
        return subTypeName;
    }

    //异常数据
    private static final String DATA_ERROR="ERR";
    private static Map<String,AttrEnum> typeEnumMap=new HashMap<String,AttrEnum>();

    static Logger logger = LoggerFactory.getLogger(AttrEnum.class);
    private AttrEnum(String type,String typeName,String subType,String subTypeName,Integer length){
        this.type=type;
        this.subType=subType;
        this.subTypeName=subTypeName;
        this.typeName=typeName;
        this.length=length;
    }
    private AttrEnum(String type,String typeName,Integer length){
        this.type=type;
        this.subType=null;
        this.subTypeName=null;
        this.typeName=typeName;
        this.length=length;
    }

    public static void main(String[] args) {
       // System.out.println( Float.intBitsToFloat(Integer.parseInt("3F9D70A4",16)));
        //System.out.println( Float.intBitsToFloat(Integer.parseInt("A4709D3F",16)));
        System.out.println(INT_TYPE__FOUR_SIGNED_CDAB.execute(null,"00000001"));
    }

    boolean check(String context){
        boolean flag =true;
        if(StringUtil.isEmpty(context)){
            flag = false;
        }else{
            if(this.getLength() >0 ){
                if(context.length() != this.getLength() * 2){
                    flag=false;
                }
            }
        }
        return flag;
    }

    /**
     * 根据key获取属性枚举
     * @return
     */
    public static AttrEnum getAttrByType(String type,String subType){
        if(StringUtil.isEmpty(type)){
            return null;
        }
        String key =type;
        if(StringUtil.isNotEmpty(subType)){
            key=type+"__"+subType;
        }
        return AttrEnum.valueOf(key);
    }



}
