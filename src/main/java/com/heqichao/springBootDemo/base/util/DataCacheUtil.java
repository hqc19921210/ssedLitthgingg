package com.heqichao.springBootDemo.base.util;

import java.util.Map;

/**
 * 缓存工具类
 * Created by heqichao on 2019-5-21.
 */
public class DataCacheUtil {

    /**
     * 缓存前缀
     */
    private static String SSET_CACHE_KEY_ ="SSET_";

    public static String LASTEST_DATADETAIL = "LASTEST_DATA";

    /**
     * 缓存放入 永久
     * @param  prefix 前缀
     * @param key   键
     * @param value 值
     * @return true成功 false 失败
     */
    public static boolean set(String prefix,String key ,Object value ){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(prefix)){
            return false;
        }
        return  RedisUtil.set(SSET_CACHE_KEY_+prefix+key,value);
    }

    /**
     * 普通缓存放入并设置过期时间
     * @param  prefix 前缀
     * @param key   键
     * @param value 值
     * @param expireTime  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static boolean set(String prefix,String key ,Object value , long expireTime){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(prefix)){
            return false;
        }
        return  RedisUtil.set(SSET_CACHE_KEY_+prefix+key,value,expireTime);
    }

    /**
     * 普通缓存获取
     * @param  prefix 前缀
     * @param key 键
     * @return 值
     */
    public static  Object get(String prefix,String key){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(prefix)){
            return null;
        }
        return RedisUtil.get(SSET_CACHE_KEY_+prefix+key);
    }

    /**
     * 删除缓存
     * @param  prefix 前缀
     * @param key 可以传一个值 或多个
     */
    public static void  del(String prefix,String... key){
        if(key !=null && key.length >0){
            for(int i=0;i<key.length;i++){
                key[i]=SSET_CACHE_KEY_+prefix+key[i];
            }
            RedisUtil.del(key);
        }

    }

    /**
     * 使用hashmap结构存放map 对象
     * @param  prefix 前缀
     * @param key
     * @param map
     */
    public static void putHash(String prefix,String key , Map map){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(prefix)){
            return ;
        }
       RedisUtil.hmset(SSET_CACHE_KEY_+prefix+key,map);
    }

    /**
     * 使用hashmap结构存放map 对象
     * @param  prefix 前缀
     * @param key
     * @param map
     * @param expireTime 过期时间(秒)
     */
    public static void putHash(String prefix,String key , Map map,long expireTime){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(prefix)){
            return ;
        }
        RedisUtil.hmset(SSET_CACHE_KEY_+prefix+key,map,expireTime);
    }

    /**
     * 获取整个map对象
     * @param  prefix 前缀
     * @param key
     * @return
     */
    public static Map getHash(String prefix,String key){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(prefix)){
            return null;
        }
        return RedisUtil.hmget(SSET_CACHE_KEY_+prefix+key);
    }

    /**
     * 获取map对象中的某一键值
     * @param  prefix 前缀
     * @param key
     * @param item
     * @return
     */
    public static Object getHash(String prefix,String key,String item){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(prefix)){
            return null;
        }
        return RedisUtil.hget(SSET_CACHE_KEY_+prefix+key,item);
    }
}
