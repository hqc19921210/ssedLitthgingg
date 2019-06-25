package com.heqichao.springBootDemo.base.quartz;

import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.CollectionUtil;
import com.heqichao.springBootDemo.base.util.DateUtil;
import com.heqichao.springBootDemo.module.entity.DataLog;
import com.heqichao.springBootDemo.module.service.DataLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2019-6-25.
 */
@Component
public class SendNBRecoverDataToOnenet {

    //用来临时存放最新一条记录的时间
    private static Map<String ,Long> dateMap =new HashMap<>();


    @Autowired
    private DataLogService dataLogService;

    @Autowired
    private EquipmentService equipmentService;

    @Scheduled(cron = "0 0/3 * * * ?")
    public void timerToNow(){
        //定时任务时间，3分钟
        int limitTime =3;
        Date nowDate =new Date();
        //查找所有sset 的NB 设备
        List<String> devIdList =equipmentService.queryDevIdByUidAndType(DataLogService.SSET_USER_ID,EquipmentService.EQUIPMENT_NB);
        if(CollectionUtil.isNotEmpty(devIdList)){
            for(String devId : devIdList){
                //获取最新的接收记录
                List<Map> dataList =dataLogService.getLastestDetail(devId);
                if(CollectionUtil.isNotEmpty(dataList)){
                    Date updDate = (Date) dataList.get(0).get("udpDate");
                    if(updDate == null){
                        continue;
                    }

                    //如果记录是4分钟前的数据 且 电容状态不为“正常”
                    // 就发一条最近的电容状态为正常的数据 到 onenet
                    if(DateUtil.minBetween(nowDate,updDate) >= limitTime){

                        //判断是否发送过这个时间点的数据
                        Long snedTime = dateMap.get(devId);
                        if(snedTime != null && snedTime.equals(updDate.getTime())){
                            //这条记录已经发送过了 就不再发送
                            continue;
                        }
                        //放置最新一条记录的时间到缓存
                        dateMap.put(devId,updDate.getTime());
                        for(Map dataMap:dataList){
                            String dataName = (String) dataMap.get("dataName");
                            String dataValue = (String) dataMap.get("dataValue");
                            if("电容状态".equals(dataName) && !"正常".equals(dataValue)){
                               DataLog dataLog = dataLogService.queryLatestDataLog(devId,"电容状态","正常");
                                if(dataLog != null){
                                    //发送到onenet
                                    dataLogService.sendToOneNet(devId,EquipmentService.EQUIPMENT_NB,dataLog.getData(),dataLog.getId());
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }
}
