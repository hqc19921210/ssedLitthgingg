package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.module.entity.AlarmSetting;

import java.util.Map;

/**
 * @author Muzzy Xu.
 * 
 */


public interface AlarmSettingService {
	//有效
	public static final String ENABLE_STATUS="N";
	//无效
	public static final String UN_ENABLE_STATUS="D";

	PageInfo queryAlarmSettingAll();

	ResponeResult delAlarmSetting(Map map);

	ResponeResult addAlarmSetting(Map map);

	ResponeResult editAlarmSetting(Map map);

	/**
	 * 根据模板ID查找报警设置
	 * @param modelId
	 * @return
	 */
	Map<Integer,AlarmSetting> queryEnableByModelId(Integer modelId);

	void deleteByModelId( Integer modelId);
}
