
package com.heqichao.springBootDemo.module.liteNA;

import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class LiteNAStringUtil {
static Logger logger = LoggerFactory.getLogger(LiteNAStringUtil.class);

    public static boolean strIsNullOrEmpty(String s) {
        return (null == s || s.trim().length() < 1);
    }
    
    /**
	 * NB接受数据后解析数据 
	 */
    public static Map<String,String> NBReturnFmt(String callJSON){
		JSONObject jsonObject = changeMse(callJSON);
		JSONObject service = jsonObject.getJSONObject("service");
		String deviceId =jsonObject.getString("deviceId");
		JSONObject data=service.getJSONObject("data");
		//分割data内容，多于1个属性会解析错误
		String dataFmt = data.toJSONString().split(":")[1];
		dataFmt =dataFmt.replaceAll("\"", "");
		dataFmt =dataFmt.replaceAll("}", "");
		
		Map<String,String> res = new HashMap<String, String>();
		res.put("devid", deviceId);
		res.put("data", dataFmt);
		res.put("srcdata", callJSON);
		return res;
	
	}
    
    private static  JSONObject changeMse(String mes){
		mes=mes.replaceAll("=",":");
		mes=mes.replaceAll("\"","");
		mes=mes.replaceAll(" ","");
		String newMes ="";
		char[] mesList =mes.toCharArray();
		for(char c :mesList){
			if('{' == c ){
			newMes = newMes+c+"\"";
			}else  if('}' == c){
			newMes = newMes+"\""+c;
			}else if(':' == c || ',' == c){
			newMes = newMes+"\""+c+"\"";
			}else{
			newMes=newMes+c;
			}
		}
		newMes= newMes.replace("\"{\"","{\"");
		newMes= newMes.replace("\"}\"","\"}");
		JSONObject jsonObject = JSONObject.parseObject(newMes);
		return jsonObject;
    }
    
    /**
     * UTC时间字符串转本地时间字符串
     * @param str
     * @return
     */
    public static String dateUTC2LocalString(String str) {
	//    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
	    SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		SimpleDateFormat localFormater = (SimpleDateFormat) DateFormat.getDateTimeInstance();//解决date.toLocaleString()过时
		Date date=null;
		try {
			date=simpleDateFormat.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return localFormater.format(date);
    }
    
    /**
    * 去除首尾指定字符
    * @param str 字符串
    * @param element 指定字符
    * @return
    */
    public static String trimFirstAndLastChar(String str, String element){
        boolean beginIndexFlag = true;
        boolean endIndexFlag = true;
        do{
            int beginIndex = str.indexOf(element) == 0 ? 1 : 0;
            int endIndex = str.lastIndexOf(element) + 1 == str.length() ? str.lastIndexOf(element) : str.length();
            str = str.substring(beginIndex, endIndex);
            beginIndexFlag = (str.indexOf(element) == 0);
            endIndexFlag = (str.lastIndexOf(element) + 1 == str.length());
        } while (beginIndexFlag || endIndexFlag);
        return str;
    }
    
    public static String getStringKeyByValueInHashMap(Map map,String value) {
	    String res = "";
	    @SuppressWarnings("rawtypes")
	    Iterator entries = map.entrySet().iterator(); 
	    while (entries.hasNext()) { 
	      @SuppressWarnings("rawtypes")
	      Map.Entry entry = (Map.Entry) entries.next(); 
	      if(value.equals(entry.getValue().toString())){
	    	  res = entry.getKey().toString();
	        } }
	    return res;   
    
    }
    
    /**
     * Long 转成六字节byte数组 ABCDEF排列
     * */
    public static  byte[] long_byte6_ABCDEF(Long id){
       byte[] arr=new byte[6];
       arr[5]=(byte)((id>>0*8)&0xff);
       arr[4]=(byte)((id>>1*8)&0xff);
       arr[3]=(byte)((id>>2*8)&0xff);
       arr[2]=(byte)((id>>3*8)&0xff);
       arr[1]=(byte)((id>>4*8)&0xff);
       arr[0]=(byte)((id>>5*8)&0xff);
       
       return arr;
   }
    /**
     * int 转成四字节byte数组 ABCD排列
     * */
    public static  byte[] int_byte4_ABCD(int id){
	    //int是32位   4个字节  创建length为4的byte数组
	    byte[] arr=new byte[4];
	    arr[3]=(byte)((id>>0*8)&0xff);
	    arr[2]=(byte)((id>>1*8)&0xff);
	    arr[1]=(byte)((id>>2*8)&0xff);
	    arr[0]=(byte)((id>>3*8)&0xff);
	    
	    return arr;
    }
    
    /**
     * int 转成四字节byte数组 CDAB排列
     * */
    public static  byte[] int_byte4_CDAB(int id){
	    //int是32位   4个字节  创建length为4的byte数组
	    byte[] arr=new byte[4];
	    arr[1]=(byte)((id>>0*8)&0xff);
	    arr[0]=(byte)((id>>1*8)&0xff);
	    arr[3]=(byte)((id>>2*8)&0xff);
	    arr[2]=(byte)((id>>3*8)&0xff);
	    
	    return arr;
    }
    /**
     * int 转成两字节byte数组 AB排列
     * */
    public static  byte[] int_byte2_AB(int id){
	    //创建length为2的byte数组
	    byte[] arr=new byte[2];
	   //字节组低位到高位排列
	    arr[1]=(byte)((id>>0*8)&0xff);
	    arr[0]=(byte)((id>>1*8)&0xff);
	    
	    return arr;
    }
    
    /**
     * int 转成单字节byte数组 
     * */
    public static  byte[] int_byte(int id){
	    //创建length为2的byte数组
	    byte[] arr=new byte[1];
	    //字节组低位到高位排列
	    arr[0]=(byte)((id>>0*8)&0xff);
	    
	    return arr;
    }
    
    /**
     * 字节数组转16进制字符串
     */
    public static String bytes2HexString(byte[] b) {
        String r = "";
        
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            r += hex.toUpperCase();
        }
        
        return r;
    }
    
    /**
     * int转2字节16进制
     * @param i
     * @return
     */
    public static String int2HexString(int i) {
	    byte[] b = int_byte2_AB(i);
	    return bytes2HexString(b);
    }
    
    /**
     * 二进制字符串转十六进制字符
     * @param bstring
     * @return
     */
    public static String bString2HexString(String bstring) {
	    Integer len = bstring.length();
	    byte[] byt = null;
	    if(len>32) {//6字节
	    BigInteger i = new BigInteger(bstring,2); //Integer 装不下，用BigInteger
	    byt = long_byte6_ABCDEF(i.longValue());
	    
	    }else if(len>16) {//四字节
	    Integer i = Integer.valueOf(bstring,2);
	    byt = int_byte4_ABCD(i);
	    
	    }else {//2字节
	    Integer i = Integer.valueOf(bstring,2);
	    byt = int_byte2_AB(i);
    
    }
    
    return bytes2HexString(byt);
    }
    
    /**
     * 根据应用id和回调类型获取回调URL
     * @param appId
     * @param notifyType
     * @return
     */
    public static String getCallBackURLByType(String appId,String notifyType) {
	    String appFmt = appId.replaceAll("_", "");
	    StringBuffer sb = new StringBuffer();
	    sb.append(Constant.CALLBACK_BASE_URL);
	    sb.append(notifyType);
	    sb.append(appFmt);
	    return sb.toString();
    }
    
     /**
      * ASCII码转换为16进制
      * @param str
      * @return
      */
	  public static String convertStringToHex(String str){
	 
		  char[] chars = str.toCharArray();
		 
		  StringBuffer hex = new StringBuffer();
		  for(int i = 0; i < chars.length; i++){
		    hex.append(Integer.toHexString((int)chars[i]));
		  }
		 
		  return hex.toString();
	  }
 
	  /**
	   * 16进制转换为ASCII
	   * @param hex
	   * @return
	   */
	  public static String convertHexToString(String hex){
	 
		  StringBuilder sb = new StringBuilder();
		  //49204c6f7665204a617661 split into two characters 49, 20, 4c...
		  for( int i=0; i<hex.length()-1; i+=2 ){
		      //grab the hex in pairs
		      String output = hex.substring(i, (i + 2));
		      //convert hex to decimal
		      int decimal = Integer.parseInt(output, 16);
		      //convert the decimal to character
		      sb.append((char)decimal);
		  }
	 
		  return sb.toString();
	  }
	  
	  
     
//    public static void main(String[] args) {
//    Map mCallback = JSONObject.parseObject("{'result':{'resultCode':'DELIVERED'},'deviceId':'16da4e0e-d460-4d00-bc0f-445f7953eec7','commandId':'4ee0637f20bf44568e07202db0acb03c'}", Map.class);
//    Map m = JSONObject.parseObject(mCallback.get("result").toString(), Map.class);
//    String appFmt = "2ix4vRy1fqb,FN0ZN_Qi,uTO__wBdzLka,";
//    String exp = "mm ,yy, ";
//exp=exp.replaceAll(exp, " ");
//System.out.println(exp);
//}
}
