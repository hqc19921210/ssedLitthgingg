package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.module.entity.DataDetail;
import com.heqichao.springBootDemo.module.entity.DataLog;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-11-28.
 */
public interface DataLogService {

    //有效
    String ENABLE_STATUS="N";
    //无效
    String UN_ENABLE_STATUS="D";
    //无效
    String ERROR_STATUS="ERR";

    //sset用户ID
    Integer SSET_USER_ID =13;
    /**
     * 保存数据
     * @param devId 设备ID
     * @param mesage 所接收到的数据内容(密文，未解析)
     * @param devId devType 设备类型
     */
    void saveDataLog(String devId,String mesage,String devType);

    /**
     *  保存数据
     * @param devId 设备ID
     * @param data 已解析的数据内容（16进制码）
     * @param srcData 所接收到的数据内容(密文，未解析)
     *  @param devId devType 设备类型
     */
    void saveDataLog(String devId,String data, String srcData,String devType);

    List<DataDetail> queryDataDetail(String devId, Integer attrId, String startTime, String endTime);

    void deleteDataLog(String... devId);

    Map queryEquAttrLog(String devId, Integer attrId, String startTime, String endTime);

    /**
     * 查找在线的lora设备中在date时间内没接收到的数据
     * @param type
     * @param onLine
     * @param date
     * @return
     */
    List<String> checkOffLineDev(String type, String onLine, Date date);

    /**
     * 查找在线线的lora设备
     * @param type
     * @param date
     * @return
     */
    List<String> checkOnLineDev(String type,  Date date);

	List<DataLog> queryDataLog();

    void save(List<DataDetail> dataDetails ,String devId);

    /**
     * 获取设备的最新数据
     * @param devId
     * @return
     */
    List<Map>  getLastestDetail(String devId);


    /**
     * 发送到onenet
     * @param devId 设备ID
     * @param devType 设备类型
     * @param data 数据
     * @param dataLogId dataLogId
     */
    void sendToOneNet(String devId,String devType,String data,Integer dataLogId);

    /**
     * 检查是否发送到onenet
     * @param devId
     * @param devType
     * @return
     */
    boolean checkSendForOneNet(String devId,String devType);

    Map queryDataLogTable(String devId,String startTime, String endTime);

    List<Map> queryLastestDataDetail(String devId);

    /**
     * 根据属性值 属性名 和设备ID查找最新一条接收信息的记录
     * @param devId
     * @param dataName
     * @param dataValue
     * @return
     */
    DataLog queryLatestDataLog(String devId,String dataName,String dataValue);
}
