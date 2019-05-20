package com.heqichao.springBootDemo.base.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.heqichao.springBootDemo.base.entity.BaseEntity;
import com.heqichao.springBootDemo.base.exception.ResponeException;
//import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.Converter;




/**
 * @author husq
 * @Description:Bean工具类
 * @date: 2014-11-17
 */
//@SuppressWarnings({ "rawtypes", "unchecked", "deprecation" })
public class BeanUtil {

	private static String SYSTEM_ENCODING = "UTF-8";
	static {
		ConvertUtils.register(new Converter() {
			@Override
			public Object convert(Class type, Object value) {
				if (value instanceof Date) {
					return value;
				}
				return DateUtil.parasDate((String) value);
			}
		}, Date.class);
		
		/**
		 * 添加String数组转换器
		 * 用途：用来解决 原对象传来的是一个String值 里面有下划线 复制到目标对象的某个数组属性里面 会将带有下划线的String值拆分成数组的问题
		 * (eg 原对象Map中有属性staffIds,此时前台只选中了一人并且id为‘111_222’，传过来的值自然而然的变成String，目标对象为param中有 属性staffIds是String[]类型，
		 * 此时用beanutil的copy时会发现 原来的'111_222'会变 ['111','222']问题)
		 */
		ConvertUtils.register(new Converter() {
			@Override
			public Object convert(Class type, Object value) {
				if (value instanceof String[]) {
					return value;
				}
				return new String[]{(String) value};
			}
		}, new String[]{}.getClass());
	}

	/**
	 * 将实体对象复制到Object对象
	 * 
	 * @param desObj 目标对象
	 * @param orig 实体对象
	 */
	private static void copyBeanProperties(Object desObj, Object orig) {
		try {
			Field[] fields = orig.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				// Class type = field.getType();
				Object value = field.get(orig);
			 if (desObj instanceof Map) {
					((Map) desObj).put(field.getName(), value);
				}
			}
			if (orig.getClass().getSuperclass().equals(BaseEntity.class)) {
				BaseEntity baseEntity = (BaseEntity) orig;
				 if (desObj instanceof Map) {
					((Map) desObj).put("id", baseEntity.getId());
					((Map) desObj).put("addDate", baseEntity.getAddDate());
					((Map) desObj).put("addUid", baseEntity.getAddUid());
					((Map) desObj).put("udpDate", baseEntity.getUdpDate());
					((Map) desObj).put("udpUid", baseEntity.getUdpUid());
				}
			}else if (BaseEntity.class.isAssignableFrom(orig.getClass().getSuperclass())) {
				BaseEntity baseEntity = (BaseEntity) orig;
				 if (desObj instanceof Map) {
					((Map) desObj).put("id", baseEntity.getId());
					 ((Map) desObj).put("addDate", baseEntity.getAddDate());
					 ((Map) desObj).put("addUid", baseEntity.getAddUid());
					 ((Map) desObj).put("udpDate", baseEntity.getUdpDate());
					 ((Map) desObj).put("udpUid", baseEntity.getUdpUid());
				}
			}
		} catch (Exception e) {
			throw new ResponeException("复制对象异常", e);
		}
	}

	/**
	 * 复制对象属性
	 * 
	 * @param dest 目标对象
	 * @param orig 源对象
	 */
	public static void copyProperties(Object dest, Object orig) {
		try {
			if ((dest == null) || (orig == null)) {
				return;
			}
			if ((dest instanceof Map)) {
				copyBeanProperties(dest, orig);
			} else {
				//org.springframework.beans.BeanUtils.copyProperties(orig,dest);
				BeanUtils.copyProperties(dest, orig);
			}
		} catch (Exception e) {
		
			throw new ResponeException("复制对象异常", e);
		}
	}
	
	/**
	 * 复制对象属性（包含父类）
	 * @param dest
	 * @param orig
	 */
	public static void copyAllProperties(Object dest, Object orig) {
		try {
			if ((dest == null) || (orig == null)) {
				return;
			}
			
			Field[] fields = orig.getClass().getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
				Field field = fields[i];
				field.setAccessible(true);
				Object value = field.get(orig);
				if (dest instanceof Map) {
					((Map) dest).put(field.getName(), value);
				}
			}
			if (orig.getClass().getSuperclass().equals(BaseEntity.class)) {
				BaseEntity baseEntity = (BaseEntity) orig;
				Object value = baseEntity.getId();
				 if (dest instanceof Map) {
					((Map) dest).put("id", value);
				}
			} else if (BaseEntity.class.isAssignableFrom(orig.getClass().getSuperclass())) {
				BaseEntity baseEntity = (BaseEntity) orig;
				Object value = baseEntity.getId();
				if (dest instanceof Map) {
					((Map) dest).put("id", value);
				}
			}
			
			Class superclass = orig.getClass().getSuperclass();
			
			while(superclass!=null ){
				
				Field[] superfields = superclass.getDeclaredFields();
				for (int i = 0; i < superfields.length; i++) {
					Field field = superfields[i];
					field.setAccessible(true);
					Object value = field.get(orig);
					if (dest instanceof Map) {
						((Map) dest).put(field.getName(), value);
					}
				}
				superclass = superclass.getSuperclass();
			}
		} catch (Exception e) {
			
			throw new ResponeException("复制对象异常", e);
		}
	}
	
	
	
	/**
	 * 将实体对象转换为html参数形式
	 * 
	 * @param obj 实体对象
	 */
	public static String objToParamString(Object obj) {
		return objToString(obj, "&");
	}

	/**
	 * 将实体对象转换为字符串形式
	 * 
	 * @param obj 实体对象
	 * @param splitStr 分割字符
	 */
	public static String objToString(Object obj, String splitStr) {
		Map map = new HashMap();
		if (obj instanceof Map) {
			map = (Map) obj;
		} else {
			copyProperties(map, obj);
		}
		return objToString(map, splitStr);
	}

	/**
	 * 将Map对象转换为字符串形式
	 * 
	 * @param params
	 *            实体对象
	 * @param splitStr
	 *            分割字符
	 */
	private static String objToString(Map params, String splitStr) {
		StringBuffer stringBuffer = new StringBuffer();
		for (Object key : params.keySet()) {
			Object value = params.get(key);
			if (value == null) {
				value = "";
			}
			try {
				if (value instanceof String) {
					stringBuffer.append(key).append("=").append(URLEncoder.encode((String) value, SYSTEM_ENCODING)).append("&");
				} else if (value instanceof String[]) {
					String[] array = (String[]) value;
					for (int i = 0; i < array.length; i++) {
						stringBuffer.append(key).append("=").append(URLEncoder.encode(array[i], SYSTEM_ENCODING)).append("&");
					}
				} else if (value instanceof Date) {
					stringBuffer.append(key).append("=").append(URLEncoder.encode(DateUtil.dateToString((Date) value), SYSTEM_ENCODING)).append("&");
				} else {
					stringBuffer.append(key).append("=").append(URLEncoder.encode(value.toString(), SYSTEM_ENCODING)).append("&");
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				//\u79d1\u5357\u516c\u53f8
				e.printStackTrace();
			}
		}
		if (stringBuffer.length() > 0) {
			stringBuffer.deleteCharAt(stringBuffer.length() - 1);
		}
		return stringBuffer.toString();
	}

	/**
	 * 将Obj对象转换为Map
	 * 
	 * @param obj 实体对象
	 */
	public static Map<String, Object> objectToMap(Object obj) {
		if (obj == null) {
			return null;
		}
		Map<String, Object> map = new HashMap<String, Object>();

		Field[] declaredFields = obj.getClass().getDeclaredFields();
		try {
			for (Field field : declaredFields) {
				field.setAccessible(true);
				map.put(field.getName(), field.get(obj));
			}
		} catch (Exception e) {
			
			throw new ResponeException("复制对象异常", e);
		}
		return map;
	}

	/**
	 * map 对象转换成java对象
	 * 
	 * @param map map对象，其中key对应java对象的field字段
	 * @param entity 对应的java实体类
	 * @param <T> 对象Class
	 * @return 转换后的java对象
	 */
	public static <T> T map2Entity(Map map, Class<T> entity) {
		T obj = null;
		try {
			obj = entity.newInstance();
			BeanUtil.copyProperties(obj, map);
		} catch (Exception e) {
			
			throw new ResponeException("复制对象异常", e);
		}
		return obj;
	}

	/**
	 * map 列表 对象转换成java对象列表
	 * 
	 * @param list map对象列表，其中map中的key对应java对象的field字段
	 * @param entity 对应的java实体类
	 * @param <T> 对象Class
	 * @return 转换后的java对象
	 */
	public static <T> List<T> map2EntityList(List<Map> list, Class<T> entity) {
		List<T> resultList = new ArrayList<T>();
		try {
			for (Map map : list) {
				resultList.add(map2Entity(map, entity));
			}
		} catch (Exception e) {
			
			throw new ResponeException("复制对象异常", e);
		}
		return resultList;
	}
}
