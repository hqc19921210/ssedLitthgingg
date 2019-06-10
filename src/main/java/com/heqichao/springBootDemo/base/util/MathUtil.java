package com.heqichao.springBootDemo.base.util;

import com.heqichao.springBootDemo.module.model.AttrEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.text.NumberFormat;

/**
 * Created by heqichao on 2018-11-20.
 */
public class MathUtil {
    //数据转换异常
    public static final String CHANGE_ERROR="CHANGE_ERROR";
    static Logger logger = LoggerFactory.getLogger(AttrEnum.class);

    static ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

    public static String getFloat(String src){
        String result=CHANGE_ERROR;
        try{
            float data =Float.intBitsToFloat(Integer.parseInt(src,16));
            result=data+"";
        }catch (Exception e){
            logger.error("数据转换异常 ："+src,e);
        }
        return result;
    }


    public static String expressAndFormat(String expression,Integer num ,String data){
        String result="";
        if(StringUtil.isEmpty(data)){
            return result;
        }
        if(StringUtil.isNotEmpty(expression)){
            expression=replaceExpression(expression, data);
            try {
                Object a =  jse.eval(expression);
                data =a.toString();
            } catch (Exception e) {
                logger.error("计算表达式有误 ："+expression,e);
            }
        }
        int format=0;
        if(num!=null && num>0){
            format=num;
        }
        NumberFormat nbf= NumberFormat.getInstance();
        nbf.setMinimumFractionDigits(format);
        double d =Double.parseDouble(data);
        result=nbf.format(d);
        //去除逗号
        result=result.replaceAll(",","");
        return result;
    }

    private static String replaceExpression(String expression,String data){
       return expression.replace("x",data).replace("X",data);
    }
    /**
     * 二字节有符合整数（参数由高位到低位排列）
     * @param a 最高位
     * @param b 最低位
     * @return
     */
    public static String convertTwoBytesToSignedInt (String a, String b){
        String result=CHANGE_ERROR;
        try{
            byte b1 = (byte) Long.parseLong(a.toUpperCase(),16);
            byte b2 = (byte) Long.parseLong(b.toUpperCase(),16);
            int data= convertTwoBytesToSignedInt(b2,b1);
            result=data+"";
        }catch (Exception e){
            logger.error("数据转换异常 ："+a+b,e);
        }
        return result;
    }

    /**
     * 二字节无符合整数（参数由高位到低位排列）
     * @param a 最高位
     * @param b 最低位
     * @return
     */
    public static String convertTwoBytesToUnSignedInt (String a, String b){
        String result=CHANGE_ERROR;
        try{
            byte b1 = (byte) Long.parseLong(a.toUpperCase(),16);
            byte b2 = (byte) Long.parseLong(b.toUpperCase(),16);
            int data= convertTwoBytesToUnSignedInt(b2,b1);
            result=data+"";
        }catch (Exception e){
            logger.error("数据转换异常 ："+a+b,e);
        }
        return result;
    }
    
    /**
     * 四字节有符合整数（参数由高位到低位排列）
     * @param a 最高位
     * @param b
     * @param c
     * @param d 最低位
     * @return
     */
    public static String convertFourBytesToSignedInt(String a,String b,String c,String d){
        String result=CHANGE_ERROR;
        try{
            byte b1 = (byte) Long.parseLong(a.toUpperCase(),16);
            byte b2 = (byte) Long.parseLong(b.toUpperCase(),16);
            byte b3 = (byte) Long.parseLong(c.toUpperCase(),16);
            byte b4 = (byte) Long.parseLong(d.toUpperCase(),16);
            int data= convertFourBytesToSignedInt(b4,b3,b2,b1);
            result=data+"";
        }catch (Exception e){
            logger.error("数据转换异常 ："+a+b+c+d,e);
        }
        return result;
    }

    /**
     * 四字节无符合整数（参数由高位到低位排列）
     * @param a 最高位
     * @param b
     * @param c
     * @param d 最低位
     * @return
     */
    public static String convertFourBytesToUnSignedInt (String a,String b,String c,String d){
        String result=CHANGE_ERROR;
        try{
            byte b1 = (byte) Long.parseLong(a.toUpperCase(),16);
            byte b2 = (byte) Long.parseLong(b.toUpperCase(),16);
            byte b3 = (byte) Long.parseLong(c.toUpperCase(),16);
            byte b4 = (byte) Long.parseLong(d.toUpperCase(),16);
            long data= convertFourBytesToUnSignedInt(b4,b3,b2,b1);
            result=data+"";
        }catch (Exception e){
            logger.error("数据转换异常 ："+a+b+c+d,e);
        }
        return result;
    }


    /**
     * 二字节有符合整数（参数由低位到高位排列）
     * @param b1
     * @param b2
     * @return
     */
    private static int convertTwoBytesToSignedInt (byte b1, byte b2)      // signed
    {
        return (b2 << 8) | (b1 & 0xFF);
    }

    /**
     * 四字节有符合整数（参数由低位到高位排列）
     * @param b1
     * @param b2
     * @return
     */
    private static int convertFourBytesToSignedInt (byte b1, byte b2, byte b3, byte b4)
    {
        return (b4 << 24) | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    /**
     * 二字节无符合整数（参数由低位到高位排列）
     * @param b1
     * @param b2
     * @return
     */
    private static int convertTwoBytesToUnSignedInt (byte b1, byte b2)      // unsigned
    {
        return (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    /**
     * 四字节无符合整数（参数由低位到高位排列）
     * @param b1
     * @param b2
     * @return
     */
    private static long convertFourBytesToUnSignedInt (byte b1, byte b2, byte b3, byte b4)
    {
        return (long) (b4 & 0xFF) << 24 | (b3 & 0xFF) << 16 | (b2 & 0xFF) << 8 | (b1 & 0xFF);
    }

    // 任意长度
    private static int toSignedInt(byte[] bRefArr) {
        int iOutcome = 0;
        byte bLoop;
        for (int i = 0; i < bRefArr.length; i++) {
            bLoop = bRefArr[i];
            iOutcome += (bLoop & 0xFF) << (8 * i);
        }
        return iOutcome;
    }

    public static void main(String[] args) {
       /* String ex=" (x +1.1 )*2";
        double data = (double) 2.2;
        try {
            Object o =  jse.eval(replaceExpression(ex,data+""));
            System.out.println(o.toString());
        } catch (Exception t) {
            t.printStackTrace();
        }

        System.out.println((2.2+1.1)*2);*/

    }
}
