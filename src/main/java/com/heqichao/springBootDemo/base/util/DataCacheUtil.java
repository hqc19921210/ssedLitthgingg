package com.heqichao.springBootDemo.base.util;

import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.param.ApplicationContextUtil;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.vo.EquipmentVO;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.service.DataLogService;
import com.heqichao.springBootDemo.module.service.ModelAttrService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 缓存工具类
 * Created by heqichao on 2019-5-21.
 */
public class DataCacheUtil {

    /**
     * 缓存默认有效时间(s)
     */
    private static long EXPIRE_TIME = 24 * 60 * 60 ;
    
    /**
     * 所有系统缓存的前缀
     */
    private static String SSET_CACHE_KEY_ ="SSET_";

    /**
     * 获取最新设备数据 缓存Key
     */
    public static String LATEST_DATADETAIL = "LATEST_DATA_";

    /**
     * 设备信息缓存
     */
    public static String EQUIPMENT_INFO ="EQUIPMENT_";

    /**
     * 模板属性缓存
     */
    public static String MODEL_ATTR ="MODEL_ATTR_";

    /**
     * 缓存放入 永久
     * @param  prefix 前缀
     * @param key   键
     * @param value 值
     * @return true成功 false 失败
     */
    public static boolean setForever(String prefix,String key ,Object value ){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(prefix)){
            return false;
        }
        return  RedisUtil.set(SSET_CACHE_KEY_+prefix+key,value);
    }

    /**
     * 缓存放入 过期时间为系统默认时间
     * @param  prefix 前缀
     * @param key   键
     * @param value 值
     * @return true成功 false 失败
     */
    public static boolean set(String prefix,String key ,Object value ){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(prefix)){
            return false;
        }
        return  RedisUtil.set(SSET_CACHE_KEY_+prefix+key,value,EXPIRE_TIME);
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
     * 使用hashmap结构存放map 对象， 过期时间为系统默认时间
     * @param  prefix 前缀
     * @param key
     * @param map
     */
    public static void putHash(String prefix,String key , Map map){
        if(StringUtil.isEmpty(key) || StringUtil.isEmpty(prefix)){
            return ;
        }
       RedisUtil.hmset(SSET_CACHE_KEY_+prefix+key,map,EXPIRE_TIME);
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
    public static EquipmentVO getEquipmentCache(String devId){
        if(StringUtil.isNotEmpty(devId)){
            Object obj = DataCacheUtil.get(EQUIPMENT_INFO,devId);
            if(obj!=null){
                return (EquipmentVO) obj;
            }else{
                EquipmentService equipmentService =  ApplicationContextUtil.getApplicationContext().getBean(EquipmentService.class);
                EquipmentVO equipment =equipmentService.getEquipmentInfo(devId);
                if(equipment != null){
                    DataCacheUtil.set(EQUIPMENT_INFO,devId,equipment,EXPIRE_TIME);
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

    /**
     *  获取设备的模型属性缓存
     * @param modelId
     * @return
     */
    public static List<ModelAttr> getModelAttrRListCache(Integer modelId){
        if(modelId != null) {
            Object obj = DataCacheUtil.get(MODEL_ATTR, modelId.toString());
            if (obj != null) {
                return (List<ModelAttr>) obj;
            } else {
                ModelAttrService modelAttrService =  ApplicationContextUtil.getApplicationContext().getBean(ModelAttrService.class);
                List<ModelAttr> modelAttrList =modelAttrService.queryByModelId(modelId);
                if(CollectionUtil.isNotEmpty(modelAttrList)){
                    DataCacheUtil.set(MODEL_ATTR,modelId.toString(),modelAttrList,EXPIRE_TIME);
                }
                return modelAttrList;
            }
        }
       return new ArrayList<>();
    }

    /**
     * 删除模板属性缓存
     * @param modelId
     */
    public static void removeModelAttrRListCache(Integer modelId){
        if(modelId!=null){
            del(MODEL_ATTR,modelId.toString());
        }
    }

    /**
     * 获取最新的接收数据缓存
     * @param devId
     * @return
     */
    public static  List<Map> getLatestDataCache(String devId){
        Object data= DataCacheUtil.get(DataCacheUtil.LATEST_DATADETAIL,devId);
        if(data == null){
            DataLogService dataLogService =  ApplicationContextUtil.getApplicationContext().getBean(DataLogService.class);
            List<Map> dataDetails = dataLogService.queryLastestDataDetail(devId);
            if(CollectionUtil.isNotEmpty(dataDetails)){
                DataCacheUtil.set(DataCacheUtil.LATEST_DATADETAIL,devId,dataDetails,EXPIRE_TIME);
                return dataDetails;
            }
        }else{
            return (List<Map>) data;
        }
        return Collections.EMPTY_LIST;
    }

    /**
     * 删除最新的接收数据缓存
     * @param devId
     */
    public static void removeLatestDataCache(String... devId){
        if(devId != null && devId.length >0 ){
            del(LATEST_DATADETAIL,devId);
        }
    }
}
