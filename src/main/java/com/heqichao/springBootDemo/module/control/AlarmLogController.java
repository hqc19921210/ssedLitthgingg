package com.heqichao.springBootDemo.module.control;

import com.heqichao.springBootDemo.base.control.BaseController;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.service.AlarmLogService;
import com.heqichao.springBootDemo.module.service.DataLogService;
import com.heqichao.springBootDemo.module.service.ModelAttrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-12-1.
 */
@RestController
@RequestMapping(value = "/service")
public class AlarmLogController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(AlarmLogController.class);

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private DataLogService dataLogService;

    @Autowired
    private ModelAttrService modelAttrService;

    @Autowired
    private AlarmLogService alarmLogService;

    @RequestMapping(value = "/updateAlarm")
    ResponeResult updateAlarm() {
        alarmLogService.updateAlarm(getParamMap());
        return new ResponeResult();
    }

    @RequestMapping(value = "/queryAlarm")
    ResponeResult queryAlarm() {
        return new ResponeResult(alarmLogService.queryAlarm(getParamMap()));
    }

    @RequestMapping(value = "/queryAlarmByTimeType")
    ResponeResult queryAlarmByTimeType() {
        Map param =getParamMap();
        String start= (String) param.get("start");
        String end= (String) param.get("end");
        if(StringUtil.isNotEmpty(end)){
            end=end+" 23:59:59";
        }
        return new ResponeResult(alarmLogService.queryCountByTimeType(start,end));
    }

    @RequestMapping(value = "/queryAlarmLog")
    ResponeResult queryAlarmLog() {
        Map map =new HashMap();
        Map param =getParamMap();
        String devId =(String) param.get("devId");
        Integer attrId=getIntegerParam("attrId");
        String start= (String) param.get("start");
        String status= (String) param.get("status");
        String end= (String) param.get("end");
        String initOption= (String) param.get("initOption");
        if(StringUtil.isNotEmpty(end)){
            end=end+" 23:59:59";
        }
        //初始化
        //设备列表 初始化设备id为第一个
        if(StringUtil.isEmpty(devId) || "TRUE".equals(initOption)){
            List<Map<String, String>> devList = equipmentService.getUserEquipmentIdList(ServletUtil.getSessionUser().getId());
            if(devList!=null && devList.size()>0){
                Map<String, String> devMap =devList.get(0);
                if(StringUtil.isEmpty(devId)){
                   // devId=devMap.get("dev_id");
                  //  map.put("devId",devId);
                }
            }
            map.put("devList",devList);
        }
        //属性列表 初始化属性key为第一个
        if(attrId == null || "TRUE".equals(initOption)){
            List<ModelAttr> attrList =new ArrayList<>();
            Equipment equipment  =equipmentService.getEquipmentInfo(devId);
            if(equipment!=null && equipment.getModelId() !=null){
                attrList =modelAttrService.queryAttrByModelId(equipment.getModelId());
                if(attrList!=null && attrList.size()>0){
                  //  attrId=attrList.get(0).getId();
                  //  map.put("attrId",attrId);
                }
            }
            map.put("attrList",attrList);
        }

        map.put("data",alarmLogService.queryAlarmLog(devId,attrId,status,start,end));
        return new ResponeResult(map);
    }
    
    @RequestMapping(value = "/queryAlarmNewestFive")
    ResponeResult queryAlarmNewestFive() {
        return new ResponeResult(alarmLogService.queryAlarmNewestFive());
    }

}
