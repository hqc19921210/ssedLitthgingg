package com.heqichao.springBootDemo.module.control;

import com.heqichao.springBootDemo.base.control.BaseController;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.service.DataLogService;
import com.heqichao.springBootDemo.module.service.ModelAttrService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by heqichao on 2018-12-1.
 */
@RestController
@RequestMapping(value = "/service")
public class DataLogController extends BaseController{

    private Logger logger = LoggerFactory.getLogger(ModelController.class);

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private DataLogService dataLogService;

    @Autowired
    private ModelAttrService modelAttrService;


    @RequestMapping(value = "/queryEquAttrLog")
    ResponeResult queryEquAttrLog() {
        Map map =new HashMap();
        Map param =getParamMap();
        String devId =(String) param.get("devId");
       Integer  attrId =StringUtil.getIntegerByMap(param,"attrId");
        String  initOption=(String) param.get("initOption");

        //初始化
        //设备列表 初始化设备id为第一个
        if(StringUtil.isEmpty(devId) || (StringUtil.isNotEmpty(initOption) && "TRUE".equals(initOption.toUpperCase())) ){
            List<Map<String, String>> devList = equipmentService.getUserEquipmentIdList(ServletUtil.getSessionUser().getId());
            if(devList!=null && devList.size()>0){
                Map<String, String> devMap =devList.get(0);
                if(StringUtil.isEmpty(devId)){
                    devId=devMap.get("dev_id");
                }
            }
            map.put("devList",devList);
        }
        //属性列表 初始化属性key为第一个
        if(attrId==null || (StringUtil.isNotEmpty(initOption) && "TRUE".equals(initOption.toUpperCase()))){
            List<ModelAttr> attrList =new ArrayList<>();
            Equipment equipment  =equipmentService.getEquipmentInfo(devId);
            if(equipment!=null && equipment.getModelId() !=null){
                attrList =modelAttrService.queryByModelId(equipment.getModelId());
                if(attrList!=null && attrList.size()>0){

                    if(attrId==null){
                        ModelAttr attr =attrList.get(0);
                        attrId=attr.getId();
                        map.put("unit",attr.getUnit());
                        map.put("dataType",attr.getDataType());
                        map.put("attrKey",attr.getAttrName());
                    }else{
                        for(ModelAttr modelAttr : attrList){
                            if(attrId.equals( modelAttr.getId())){
                                map.put("unit",modelAttr.getUnit());
                                map.put("dataType",modelAttr.getDataType());
                                map.put("attrKey",modelAttr.getAttrName());
                                break;
                            }
                        }
                    }

                }
            }
            map.put("attrList",attrList);
        }


        String start= (String) param.get("start");
        String end= (String) param.get("end");
        if(StringUtil.isNotEmpty(end)){
            end=end+" 23:59:59";
        }
        map.putAll(dataLogService.queryEquAttrLog(devId,attrId,start,end));

        map.put("devId",devId);
        map.put("attrId",attrId);

        return new ResponeResult(map);
    }



    @RequestMapping(value = "/deleteAllDataLog")
    ResponeResult deleteAllDataLog() {
        List<Map<String, String>> devList = equipmentService.getUserEquipmentIdList(ServletUtil.getSessionUser().getId());
        if(devList!=null && devList.size()>0){
            String[] devids =new String[devList.size()];
            for(int i=0;i<devList.size();i++){
                Map<String, String> devMap=devList.get(i);
                devids[i]=devMap.get("dev_id");
            }
            dataLogService.deleteDataLog(devids);
        }

        return new ResponeResult();
    }
    
    @RequestMapping(value = "/queryDataLog")
    ResponeResult queryDataLog() {
    	
    	return new ResponeResult(dataLogService.queryDataLog());
    }
    
    
}
