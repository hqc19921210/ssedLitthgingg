package com.heqichao.springBootDemo.module.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.entity.User;
import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.param.ApplicationContextUtil;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.service.UserService;
import com.heqichao.springBootDemo.base.util.Base64Encrypt;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.base.util.UserUtil;
import com.heqichao.springBootDemo.module.entity.*;
import com.heqichao.springBootDemo.module.mapper.DataDetailMapper;
import com.heqichao.springBootDemo.module.mapper.DataLogMapper;
import com.heqichao.springBootDemo.module.model.AlarmEnum;
import com.heqichao.springBootDemo.module.model.AttrEnum;
import com.heqichao.springBootDemo.module.model.ModelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * Created by heqichao on 2018-11-28.
 */
@Service
@Transactional
public class DataLogServiceImpl implements DataLogService {

    @Autowired
    private ModelAttrService modelAttrService;
    @Autowired
    private DataLogMapper dataLogMapper;
    @Autowired
    private DataDetailMapper dataDetailMapper;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private AlarmLogService alarmLogService;

    @Autowired
    private AlarmSettingService alarmSettingService;

    @Autowired
    private UserService userService;

    @Override
    public List<DataLog> queryDataLog(){
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(cmp==2) {
    		return dataLogMapper.queryDataLog();
    		
    	}
    	return null;
    }
    
    @Override
    public void saveDataLog(String devId,String data, String srcData,String devType){
        Date date =new Date();
        //报警恢复映射
        Map alramNewValue =new HashMap();
        //去除前后的主数据
        String mainData="";
        DataLog dataLog =new DataLog();
        List<DataDetail> dataDetails =new ArrayList<>();
        List<AlarmLog> alarmLogs =new ArrayList<>();
        //要解除报警的属性ID
        List<Integer> normalAttrId =new ArrayList<>();
        try{
            dataLog.setSrcData(srcData);
            dataLog.setData(data);
            dataLog.setAddDate(date);
            dataLog.setUdpDate(date);
            dataLog.setDevId(devId);
            dataLog.setDevType(devType);
            //先查找该设备所绑定的模板属性
            Equipment equipment = equipmentService.getEquipmentInfo(devId);
            Integer modId=null;
            List<ModelAttr> attrList =new ArrayList<>();
            if(equipment!=null && equipment.getModelId() !=null){
                modId= equipment.getModelId();
                attrList = modelAttrService.queryByModelId(modId);
            }
            if(equipment==null || modId ==null || attrList == null || attrList.size() < 1) {
                //没有数据或模板则返回
                dataLog.setDataStatus(DataLogService.ERROR_STATUS);
                return;
            }
            if(StringUtil.isNotEmpty(devId) && StringUtil.isNotEmpty(data)){
                //0.判断是否心跳 长度为7字节
               /* if(data.length() == 14){
                    dataLog.setDataStatus(DataLogService.ENABLE_STATUS);
                    return;
                }*/
                //0.判断是否心跳 长度为7字节
                if( ModelUtil.isHeartData(data.length()/2)){
                    dataLog.setDataStatus(DataLogService.ENABLE_STATUS);
                    return;
                }
                //默认去除前面没用3字节
                int frontByte =ModelUtil.DATA_FRONT_BYTE_LENGTH;
                //去除后面没用2字节
                int backByte=ModelUtil.DATA_BACK_BYTE_LENGTH;

                //判断数据是否有波形 aabbccddeeffgghh
                //1. 先获取cc的值（数据区长度校验）
                Integer dtaLenInt = Math.toIntExact(Long.parseLong(data.substring(4,6), 16));
                //2.获取数据区字节长度 ddeeff（减去开头跟结尾）
                int dataSrcLen =(data.length()-frontByte*2-backByte*2)/2;
                if(dtaLenInt==dataSrcLen){
                    //无波形
                }else {
                    //3.取ccdd的值
                    dtaLenInt = Math.toIntExact(Long.parseLong(data.substring(4,8), 16));
                    //4..获取数据区字节长度 eeff（减去开头跟结尾）
                    dataSrcLen =(data.length()-(frontByte+1)*2-backByte*2)/2;
                    if(dtaLenInt==dataSrcLen){
                        //有波形的 去除前面没用4字节
                        frontByte=4;
                    }else{
                        //异常数据
                        dataLog.setDataStatus(DataLogService.ERROR_STATUS);
                        return;
                    }
                }
                //去除前后的主数据
                mainData=data.substring(frontByte*2,data.length()-backByte*2);
                dataLog.setMainData(mainData);
                dataLog.setDataStatus(DataLogService.ENABLE_STATUS);
                String content="";

                //查找报警设置
                Map<Integer,AlarmSetting>  settingMap =alarmSettingService.queryEnableByModelId(equipment.getModelId());
                for(int i=0;i<attrList.size();i++){
                    ModelAttr attr=attrList.get(i);
                    AttrEnum attrEnum =AttrEnum.getAttrByType(attr.getDataType(),attr.getValueType());
                    if(attrEnum==null){
                        continue;
                    }
                    if(!attrEnum.getType().equals(AttrEnum.WAVE_TYPE.getType())){
                        //如果不是波形 要截取对应长度
                        content=mainData.substring(0,attrEnum.getLength()*2);//获取处理数据
                        mainData=mainData.substring(attrEnum.getLength()*2,mainData.length());//主数据去掉处理数据
                    }else{
                        content=mainData;
                    }
                    DataDetail dataDetail =new DataDetail();
                    dataDetail.setOrderNo(i);
                    dataDetail.setDataSrc(content);
                    //计算
                    String res =ModelUtil.getData(attr,content);
                    dataDetail.setAddDate(date);
                    dataDetail.setUdpDate(date);
                    dataDetail.setDataName(attr.getAttrName());
                    dataDetail.setDataType(attrEnum.getType());
                    dataDetail.setDataValue(res);
                    dataDetail.setAttrId(attr.getId());
                    dataDetail.setDevId(devId);
                    dataDetail.setUnit(attr.getUnit());
                    dataDetail.setDataStatus(DataLogService.ENABLE_STATUS);
                    dataDetails.add(dataDetail);

                    //检查报警
                    AlarmSetting setting =settingMap.get(attr.getId());
                    if(setting!=null){
                        AlarmEnum alarmEnum =AlarmEnum.getEnumByCode(setting.getAlramType());
                        if(alarmEnum!=null){
                            if(alarmEnum.execute(setting,res)){
                                //保存报警
                                AlarmLog log =new AlarmLog();
                                log.setDevId(devId);
                                log.setDevType(devType);
                                log.setAlramType(setting.getAlramType());
                                log.setSettingId(setting.getId());
                                log.setAttrId(attr.getId());
                                log.setUnit(attr.getUnit());
                                log.setDataValue(res);
                                log.setModelId(modId);
                                log.setAddDate(date);
                                log.setUdpDate(date);
                                log.setDataStatus(AlarmLogService.ALARM_STATUS);
                                alarmLogs.add(log);
                            }else{
                                //解除报警
                                alramNewValue.put(attr.getId(),res);
                                normalAttrId.add(attr.getId());
                            }
                        }
                    }
                }
            }
        }catch (Exception e){
            dataLog.setDataStatus(DataLogService.ERROR_STATUS);
            //有其中一条解析出错就忽略所有？
            dataDetails=new ArrayList<>();
        }finally {
            //更新设备为在线
            List<String> devIds=new ArrayList<>();
            devIds.add(devId);
            equipmentService.updateOnlineStatus(EquipmentService.ON_LINE,devIds,date);
            //保存解析数据
            dataLogMapper.save(dataLog);
            if(dataDetails.size()>0){
                for(DataDetail dataDetail :dataDetails){
                    dataDetail.setLogId(dataLog.getId());
                }
                dataDetailMapper.save(dataDetails);
            }
            //保存报警数据
            if(alarmLogs.size()>0){
                for(AlarmLog log :alarmLogs){
                    log.setLogId(dataLog.getId());
                }
                alarmLogService.save(alarmLogs);
            }

            //解除报警数据
            alarmLogService.updateNormalStatus(devId,normalAttrId,date,alramNewValue);
        }
    }


    @Override
    public void saveDataLog(String devId, String mes,String devType) {
        //转译数据
        if(StringUtil.isNotEmpty(devId) && StringUtil.isNotEmpty(mes)){
            String srcData ="";
            try{
                JSONObject jsonObject = JSON.parseObject(mes);
                //只解析data
                srcData =jsonObject.getString("data");
            }catch (Exception e){
                srcData=mes;
            }
            String data="";
            try{
                String[] transDatas = Base64Encrypt.decodeToHexStr(srcData);
                if(transDatas==null || transDatas.length<1){
                    return;
                }
                data=StringUtil.getString(transDatas,"");
            }catch (Exception e){
                data="";
            }
            DataLogService dataLogService= (DataLogService) ApplicationContextUtil.getApplicationContext().getBean("dataLogServiceImpl");
            dataLogService.saveDataLog(devId,data,mes,devType);
        }


    }

    @Override
    public List<DataDetail> queryDataDetail(String devId, Integer attrId, String startTime, String endTime) {
        return dataDetailMapper.queryDetail(devId,attrId,ENABLE_STATUS,startTime,endTime);
    }

    @Override
    public void deleteDataLog(String... devId) {
        if(!UserUtil.hasCRDPermission()){
            throw new ResponeException("没有该权限操作！");
        }
        if(devId!=null && devId.length>0){
            Date date =new Date();
           List<String > ids = Arrays.asList(devId);
            dataLogMapper.updateStatus(UN_ENABLE_STATUS,ids,date);
            dataDetailMapper.updateStatus(UN_ENABLE_STATUS,ids,date);
            alarmLogService.deleteAlarmLog(devId);
        }
    }

    @Override
    public Map queryEquAttrLog(String devId, Integer attrId, String startTime, String endTime) {
        Map map= new HashMap();
        if(StringUtil.isNotEmpty(devId) && attrId!=null){
            map.put("log",queryDataDetail(devId, attrId, startTime, endTime));
            Equipment equipment =equipmentService.getEquipmentInfo(devId);
            if(equipment.getUid()!=null){
                User user = userService.querById(equipment.getUid());
                if(user!=null){
                    map.put("equipUserName",user.getAccount());
                }else{
                    map.put("equipUserName","");
                }
            }
            map.put("equip",equipment);
        }
        return map;
    }

    @Override
    public List<String> checkOffLineDev(String type, String onLine, Date date) {
        return dataDetailMapper.checkOffLineDev(type, onLine, date);
    }

    @Override
    public List<String> checkOnLineDev(String type, Date date) {
        return dataDetailMapper.checkOnLineDev(type, date);
    }
}
