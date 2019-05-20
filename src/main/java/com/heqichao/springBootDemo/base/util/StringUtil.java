package com.heqichao.springBootDemo.base.util;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Created by heqichao on 2018-2-12.
 */
public class StringUtil {

    public static boolean isEmpty(String context){
        if(context==null || "".equals(context)){
            return true;
        }
        return  false;
    }

    public static boolean isNotEmpty(String context){
        return !isEmpty(context);
    }
    
    // Add by Muzzy Xu.
    public static Integer objectToInteger(Object obj) {
    	if(obj == null) {
    		return null;
    	}
    	try {
    		return Integer.valueOf(obj.toString());
		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
    }
    
    public static Integer getIntegerByMap(Map map,String key) {
    	if(map == null || map.size() == 0) {
    		return null;
    	}
    	try {
    		return Integer.valueOf(map.get(key).toString().trim());
    	} catch (Exception e) {
//			e.printStackTrace();
    		return null;
    	}
    }


	public static Float getFloatByMap(Map map,String key) {
		if(map == null || map.size() == 0) {
			return null;
		}
		try {
			return Float.valueOf(map.get(key).toString().trim());
		} catch (Exception e) {
//			e.printStackTrace();
			return null;
		}
	}

    public static BigDecimal getBigDecimalByMap(Map map,String key) {
    	if(map.size() == 0) {
    		return null;
    	}
    	try {
    		return new BigDecimal(map.get(key).toString().trim());
    	} catch (Exception e) {
//			e.printStackTrace();
    		return null;
    	}
    }

    
    public static String getStringByMap(Map map,String key) {
    	if(map.size() == 0) {
    		return null;
    	}
    	try {
    		return map.get(key).toString();
    	} catch (Exception e) {
//    		e.printStackTrace();
    		return null;
    	}
    }
    
    /**
     * 字符串转字符串数组（每两位字符）
     * @param str
     * @return String[]
     */
    public static String[] string2StringArray(String str) {
	    int m=str.length()/2;
	    if(m*2<str.length()){
	    	m++;
	    }
	    String[] strs=new String[m];
	    int j=0;
	    for(int i=0;i<str.length();i++){
		    if(i%2==0){
		    	strs[j]=""+str.charAt(i);
		    }else{
		    	strs[j]=strs[j]+str.charAt(i);
		    	j++;
		    }
	    }
	    return strs;
	}

	/**
	 * 合并字符串数组
	 * @param str
	 * @param frex 分割符
	 * @return
	 */
	public static String getString(String[] str,String frex){
		String returnStr ="";
		if(str!=null && str.length >0){
			for(String s :str){
				returnStr=returnStr+s+frex;
			}
			if(StringUtil.isNotEmpty(frex)){
				returnStr=returnStr.substring(0,returnStr.length()-frex.length());
			}
		}
		return  returnStr;
	}



	/**
	 * 将下划线大写方式命名的字符串转换为驼峰式。如果转换前的下划线大写方式命名的字符串为空，则返回空字符串。</br>
	 * 例如：HELLO_WORLD->HelloWorld
	 * @param name 转换前的下划线大写方式命名的字符串
	 * @return 转换后的驼峰式命名的字符串
	 */
	public static String camelName(String name, boolean isUpperCase) {
		StringBuilder result = new StringBuilder();
		// 快速检查
		if (name == null || name.isEmpty()) {
			// 没必要转换
			return "";
		} else if (!name.contains("_")) {
			// 不含下划线，仅将首字母小写
			return toCase(name.substring(0, 1), isUpperCase) + name.substring(1);
		}
		// 用下划线将原始字符串分割
		String camels[] = name.split("_");
		for (String camel :  camels) {
			// 跳过原始字符串中开头、结尾的下换线或双重下划线
			if (StringUtil.isEmpty(camel)) {
				continue;
			}
			// 处理真正的驼峰片段
			if (result.length() == 0) {
				// 第一个驼峰片段，全部字母都小写
				result.append(toCase(camel.substring(0, 1), isUpperCase));
				if(camel.length()>1){
					result.append(camel.substring(1).toLowerCase());
				}
			} else {
				// 其他的驼峰片段，首字母大写
				result.append(camel.substring(0, 1).toUpperCase());
				if(camel.length()>1){
					result.append(camel.substring(1).toLowerCase());
				}
			}
		}
		return result.toString();
	}

	private static String toCase(String s, boolean isUpperCase){
		if(isUpperCase){
			return s.toUpperCase();
		}else{
			return s.toLowerCase();
		}
	}
	
}
