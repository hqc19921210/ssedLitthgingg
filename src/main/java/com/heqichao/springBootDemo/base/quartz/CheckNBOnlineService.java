package com.heqichao.springBootDemo.base.quartz;

import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.DateUtil;
import com.heqichao.springBootDemo.module.service.DataLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * Created by heqichao on 2018-12-9.
 */
@Component
public class CheckNBOnlineService {

    @Autowired
    private DataLogService dataLogService;

    @Autowired
    private EquipmentService equipmentService;

    @Scheduled(cron = "0 0 0/12 * * ? ")
    public void timerToNow(){
        Date nowDate =new Date();
        //12小时前
        Date date = DateUtil.addMinute(nowDate,-12 * 60);

        //查找12小时内没接收到消息的在线NB设备
        List<String> devIds =dataLogService.checkOffLineDev(EquipmentService.EQUIPMENT_NB,EquipmentService.ON_LINE,date);
        if(devIds!=null && devIds.size()>0){
            //更新设备为下线
            equipmentService.updateOnlineStatus(EquipmentService.OFF_LINE,devIds,nowDate);
        }
        //注：每次接收到数据则会更新设备为在线
    }
}
