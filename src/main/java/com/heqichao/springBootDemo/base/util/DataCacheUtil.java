package com.heqichao.springBootDemo.base.util;

import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.param.ApplicationContextUtil;
import com.heqichao.springBootDemo.base.service.EquipmentService;

import java.util.Map;

/**
 * 缓存工具类
 * Created by heqichao on 2019-5-21.
 */
public class DataCacheUtil {

    /**
     * 所有系统缓存的前缀
     */
    private static String SSET_CACHE_KEY_ ="SSET_";

    /**
     * 获取最新设备数据 缓存Key
     */
    public static String LASTEST_DATADETAIL = "LASTEST_DATA_";

    /**
     * 设备信息缓存
     */
    public static String EQUIPMENT_INFO ="EQUIPMENT_";

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

    /**
     * 获取设备缓存
     * @param devId
     * @return
     */
    public static Equipment getEquipmentCache(String devId){
        if(StringUtil.isNotEmpty(devId)){
            Object obj = DataCacheUtil.get(EQUIPMENT_INFO,devId);
            if(obj!=null){
                return (Equipment) obj;
            }else{
                EquipmentService equipmentService = (EquipmentService) ApplicationContextUtil.getApplicationContext().getBean("equipmentServiceImpl");
                Equipment equipment =equipmentService.getEquipmentInfo(devId);
                if(equipment != null){
                    DataCacheUtil.set(EQUIPMENT_INFO,devId,equipment);
                    return equipment;
                }
            }

        }
        return null;
    }

    /**
     * 删除设备缓存
     * @param devId
     */
    public static void removeEquipmentCache(String devId){
        del(EQUIPMENT_INFO,devId);
    }
}
