package com.heqichao.springBootDemo.base.util;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.heqichao.springBootDemo.base.param.ApplicationContextUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

/**
 * redis
 */
public  class RedisUtil {

    private static volatile RedisTemplate<String, Object> redisTemplate;
    private static RedisTemplate<String, Object> getRedisTrmplate(){
        if(redisTemplate== null){
            synchronized (RedisUtil.class){
                if(redisTemplate == null){
                    redisTemplate =(RedisTemplate<String, Object>) ApplicationContextUtil.getBean("redisTemplate");
                }
            }
        }
        return redisTemplate;
    }
    
    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    private static boolean expire(String key, long time) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            if (time > 0) {
                getRedisTrmplate().expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回代表为永久有效
     */
    public static  long getExpire(String key) {
        if(StringUtil.isEmpty(key)){
            return 0L;
        }
        return getRedisTrmplate().getExpire(key, TimeUnit.SECONDS);

    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public static  boolean hasKey(String key) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            return getRedisTrmplate().hasKey(key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */

    @SuppressWarnings("unchecked")
    public static  void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 0) {
                getRedisTrmplate().delete(key[0]);
            } else {
                getRedisTrmplate().delete(CollectionUtils.arrayToList(key));
            }
        }
    }


    // ============================String=============================

    /**
     * 普通缓存获取
     * @param key 键
     * @return 值
     */
    public static  Object get(String key) {
        if(StringUtil.isEmpty(key)){
            return null;
        }
        return  getRedisTrmplate().opsForValue().get(key);
    }


    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public static  boolean set(String key, Object value) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            getRedisTrmplate().opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public static  boolean set(String key, Object value, long time) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
                if (time > 0) {
                    getRedisTrmplate().opsForValue().set(key, value, time, TimeUnit.SECONDS);
                } else {
                    set(key, value);
                }
                return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public static  long incr(String key, long delta) {

        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        if(StringUtil.isEmpty(key)){
            return 0L;
        }
        return getRedisTrmplate().opsForValue().increment(key, delta);
    }


    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public static  long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        if(StringUtil.isEmpty(key)){
            return 0;
        }
        return getRedisTrmplate().opsForValue().increment(key, -delta);
    }


    // ================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public static  Object hget(String key, String item) {
        return StringUtil.isNotEmpty(key) && StringUtil.isNotEmpty(item) ? getRedisTrmplate().opsForHash().get(key, item) : null ;
    }


    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public static  Map<Object, Object> hmget(String key) {
        if(StringUtil.isEmpty(key)){
            return null;
        }
        return getRedisTrmplate().opsForHash().entries(key);
    }


    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public static  boolean hmset(String key, Map<String, Object> map) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            getRedisTrmplate().opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */

    public static  boolean hmset(String key, Map<String, Object> map, long time) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            getRedisTrmplate().opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */

    public static  boolean hset(String key, String item, Object value) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            getRedisTrmplate().opsForHash().put(key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */

    public static  boolean hset(String key, String item, Object value, long time) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            getRedisTrmplate().opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */

    public static  void hdel(String key, Object... item) {
        if(StringUtil.isEmpty(key)){
            return ;
        }
        getRedisTrmplate().opsForHash().delete(key, item);
    }


    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */

    public static  boolean hHasKey(String key, String item) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        return getRedisTrmplate().opsForHash().hasKey(key, item);

    }


    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于)
     * @return
     */
    public static  double hincr(String key, String item, double by) {
        if(StringUtil.isEmpty(key)){
            return 0D;
        }
        return getRedisTrmplate().opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于)
     * @return
     */
    public static  double hdecr(String key, String item, double by) {
        if(StringUtil.isEmpty(key)){
            return 0D;
        }
        return getRedisTrmplate().opsForHash().increment(key, item, -by);
    }

    // ============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public static  Set<Object> sGet(String key) {
        if(StringUtil.isEmpty(key)){
            return null;
        }
        try {
            return getRedisTrmplate().opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static  boolean sHasKey(String key, Object value) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            return getRedisTrmplate().opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public static  long sSet(String key, Object... values) {
        if(StringUtil.isEmpty(key)){
            return 0L;
        }
        try {
            return getRedisTrmplate().opsForSet().add(key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */

    public static  long sSetAndTime(String key, long time, Object... values) {
        if(StringUtil.isEmpty(key)){
            return 0L;
        }
        try {
            Long count = getRedisTrmplate().opsForSet().add(key, values);
            if (time > 0) {
                expire(key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }


    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */

    public static  long sGetSetSize(String key) {
        if(StringUtil.isEmpty(key)){
            return 0L;
        }
        try {
            return getRedisTrmplate().opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }

    }


    /**
     * 移除值为value的
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 移除的个数
     */

    public static  long setRemove(String key, Object... values) {
        if(StringUtil.isEmpty(key)){
            return 0L;
        }
        try {
            Long count = getRedisTrmplate().opsForSet().remove(key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0到 -1 代表所有值
     * @return
     */

    public static  List<Object> lGet(String key, long start, long end) {
        if(StringUtil.isEmpty(key)){
            return null;
        }
        try {
            return getRedisTrmplate().opsForList().range(key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */

    public static  long lGetListSize(String key) {
        if(StringUtil.isEmpty(key)){
            return 0L;
        }
        try {
            return getRedisTrmplate().opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引 index>= 0时， 0 表头，1 第二个元素，依次类推；index<0 时，-1 表尾，-2 倒数第二个元素，依次类推
     * @return
     */
    public static  Object lGetIndex(String key, long index) {
        if(StringUtil.isEmpty(key)){
            return null;
        }
        try {
            return getRedisTrmplate().opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static  boolean lSet(String key, Object value) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            getRedisTrmplate().opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */

    public static  boolean lSet(String key, Object value, long time) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            getRedisTrmplate().opsForList().rightPush(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public static  boolean lSet(String key, List<Object> value) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            getRedisTrmplate().opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */

    public static  boolean lSet(String key, List<Object> value, long time) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            getRedisTrmplate().opsForList().rightPushAll(key, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public static  boolean lUpdateIndex(String key, long index, Object value) {
        if(StringUtil.isEmpty(key)){
            return false;
        }
        try {
            getRedisTrmplate().opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public static  long lRemove(String key, long count, Object value) {
        if(StringUtil.isEmpty(key)){
            return 0L;
        }
        try {
            Long remove = getRedisTrmplate().opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }
    }
}