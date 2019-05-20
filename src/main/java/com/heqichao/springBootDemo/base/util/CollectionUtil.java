package com.heqichao.springBootDemo.base.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-12-9.
 */
public class CollectionUtil {

    /**
     * 判断集合是否为空
     * @param collection 集合
     * @return
     */
    public static boolean isEmpty(Collection collection){
        return collection == null || collection.isEmpty();
    }

    /**
     * 判断集合是否为非空
     * @param collection 集合
     * @return
     */
    public static boolean isNotEmpty(Collection collection){
        return !isEmpty(collection);
    }

    /**
     * 将List<String>转为map
     * @param list
     * @param keys
     * @param camel 是否使用驼峰
     * @return
     */
    public  static Map listStringTranToMap(List<String> list,String[] keys,boolean camel){
        Map map=new HashMap();
        if(list== null || list.size()<1){
            return map;
        }
        if(keys== null || keys.length<1){
            return map;
        }
        if(list.size() != keys.length){
            return map;
        }
        for(int i=0;i<list.size();i++){
            String key =keys[i];
            if(camel){
                key =StringUtil.camelName(key,false);
                map.put(key,list.get(i));
            }
        }
        return map;
    }
}
