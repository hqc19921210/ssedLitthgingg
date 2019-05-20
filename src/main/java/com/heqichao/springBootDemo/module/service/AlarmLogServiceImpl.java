package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.*;
import com.heqichao.springBootDemo.module.entity.AlarmLog;
import com.heqichao.springBootDemo.module.mapper.AlarmLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by heqichao on 2018-12-16.
 */
@Service
@Transactional
public class AlarmLogServiceImpl implements AlarmLogService {
    @Autowired
    private AlarmLogMapper alarmLogMapper;

    @Autowired
    private EquipmentService equipmentService;
    @Override
    public void save(List<AlarmLog> logs) {
        if(logs!=null && logs.size()>0){
            alarmLogMapper.save(logs);
        }

    }

    @Override
    public void deleteAlarmLog(String... devId) {
        if(!UserUtil.hasCRDPermission()){
            throw new ResponeException("没有该权限操作！");
        }
        if(devId!=null && devId.length>0){
            Date date =new Date();
            List<String > ids = Arrays.asList(devId);
            alarmLogMapper.updateDeleteStatus(DELETE_STATUS,ids,date);
        }
    }

    @Override
    public void updateNormalStatus(String devId, List<Integer> attrIds,Date date ,Map newValueMap) {
        if(StringUtil.isNotEmpty(devId) && attrIds!=null && attrIds.size()>0 ){
            for(Integer attrId : attrIds){
                alarmLogMapper.updateStatus(NOIMAL_STATUS,date,devId,attrId,ALARM_STATUS, (String) newValueMap.get(attrId));
            }

        }

    }

    @Override
    public PageInfo queryAlarmLog(String devId, Integer attrId,String status, String startTime, String endTime) {

        List<String> devIdList =getUserDevIds();
        if(devIdList!=null && devIdList.size()>0){
            PageUtil.setPage();
            return new PageInfo(alarmLogMapper.queryAlarmLogByDevIdAttrId(devIdList,devId,attrId, status,  startTime,  endTime));
        }else{
            return new PageInfo(new ArrayList());
        }

    }

    @Override
    public void updateAlarm(Map map) {
        String record =StringUtil.getStringByMap(map,"record");
        String status =StringUtil.getStringByMap(map,"status");
        Integer id =StringUtil.getIntegerByMap(map,"id");
        if(id!=null){
            alarmLogMapper.updateAlarm(status,new Date(),record,id);
        }

    }


    @Override
    public Map queryAlarm(Map param) {
        Map map=new HashMap();
        List<String> devIdList =getUserDevIds();
        if(devIdList!=null && devIdList.size()>0){
          //查找系统的报警总数
            int alarmCount =0;
            List<Map> list = alarmLogMapper.queryAlarm(devIdList,ALARM_STATUS,null,false);
            if(list!=null){
                alarmCount=list.size();
            }else{
                list =new ArrayList<>();
            }
            map.put("alarmCount",alarmCount);
            map.put("alarmList",list);

            //查找30秒内的变化数据
            String queryChange =  StringUtil.getStringByMap(param,"queryChange");
            if(StringUtil.isNotEmpty(queryChange) && "TRUE".equals(queryChange)){
                //忽略手动修改的状态数据
                list = alarmLogMapper.queryAlarm(devIdList,null, DateUtil.addSecond(new Date(),-30),true);
                if(list!=null && list.size()>0){
                    map.put("chaneList",list);
                }
            }

        }
        return map;
    }
    
    // Muzzy 获取报警最新5条记录
    @Override
    public List<Map>  queryAlarmNewestFive() {
    	Integer udid = ServletUtil.getSessionUser().getId();
    	Integer pid = ServletUtil.getSessionUser().getParentId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
        //这里不知道为啥会有污染
        return alarmLogMapper.queryAlarmNewestFive(udid,pid,cmp);
    }

    @Override
    public List<Map> queryCountByTimeType( String start,String end) {
        List<String> devIdList =getUserDevIds();
        if(devIdList!=null && devIdList.size()>0){

            return alarmLogMapper.queryCountByDay(devIdList,start,end);


            /*String type ="";
            //查询7天内
            if("servenDay".equals(timeType)){
                Date endDate =DateUtil.getEndTime(new Date());
                Date startDate =DateUtil.addDay(endDate,-7);//1号的23:59:59 到8号的23:59:59
                return alarmLogMapper.queryCountByDay(devIdList,startDate,endDate);
            }
           else  if("year".equals(timeType)){
                type="%Y";
            }else if("month".equals(timeType)){
                type="%m";
            }else if("day".equals(timeType)){
                type="%j";
            }
            if(StringUtil.isNotEmpty(type)){
                return alarmLogMapper.queryCountByTimeType(devIdList,type);
            }*/
        }
        return new ArrayList();
    }

    @Override
    public List<Map> queryCountByDay( Date start, Date end) {
        return null;
    }
    // End Muzzy

    private List<String> getUserDevIds(){
        List<Map<String, String>> devList = equipmentService.getUserEquipmentIdList(ServletUtil.getSessionUser().getId());
        List<String> devIds =new ArrayList<>();
        if(devList!=null && devList.size()>0) {
            for (Map m : devList) {
                String devId = (String) m.get("dev_id");
                if (StringUtil.isNotEmpty(devId)) {
                    devIds.add(devId);
                }
            }
        }
        return  devIds;
    }
}
