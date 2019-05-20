package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.module.entity.AlarmLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-12-16.
 */
public interface AlarmLogService {
    //正常数据
    public static final String NOIMAL_STATUS="N";
    //报警数据
    public static final String ALARM_STATUS="A";
    //已删除额数据
    public static final String DELETE_STATUS="D";
    void save(List<AlarmLog> logs);

    void deleteAlarmLog(String... devId);
    /**
     * 更新为正常数据
     * @param devId
     * @param attrId
     */
    void updateNormalStatus(String devId,List<Integer> attrId,Date date ,Map newValueMap);

    PageInfo queryAlarmLog(String devId,Integer attrId,String status, String startTime, String endTime);

    void updateAlarm(Map map);
    /**
     * 首页 查找最新 的报警记录
     * @return
     */
    Map queryAlarm(Map map);

	List<Map> queryAlarmNewestFive();

    List<Map> queryCountByTimeType( String start,String end);

    //查找某个时间段内的统计
    List<Map> queryCountByDay( Date start,Date end);
}
