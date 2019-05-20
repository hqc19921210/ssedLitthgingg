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
public class CheckMqttOnlineService {

    @Autowired
    private DataLogService dataLogService;

    @Autowired
    private EquipmentService equipmentService;

    @Scheduled(cron = "0 0/25 * * * ?")
    public void timerToNow(){
        Date nowDate =new Date();
        //25分钟前
        Date date = DateUtil.addMinute(nowDate,-25);

        //查找25分钟内没接收到消息的在线lora设备
        List<String> devIds =dataLogService.checkOffLineDev(EquipmentService.EQUIPMENT_LORA,EquipmentService.ON_LINE,date);
        if(devIds!=null && devIds.size()>0){
            //更新设备为下线
            equipmentService.updateOnlineStatus(EquipmentService.OFF_LINE,devIds,nowDate);
        }
        //注：每次接收到数据则会更新设备为在线
    }
}
