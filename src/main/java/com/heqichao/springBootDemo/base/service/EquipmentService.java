package com.heqichao.springBootDemo.base.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.entity.UploadResultEntity;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.vo.EquipmentVO;
import com.heqichao.springBootDemo.module.entity.DataDetail;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Muzzy Xu.
 * 
 */


public interface EquipmentService {
	//设备只有在线和离线线状态，所以把故障状态也改为下线状态B
	String NORMAL = "N";
	String BREAKDOWN = "B";

	String ON_LINE="1";
	String OFF_LINE="0";
	Integer UPLOAD_SUCCESS=1;
	Integer UPLOAD_FAIL=0;

	String EQUIPMENT_LORA="L";
	String EQUIPMENT_NB="N";
	String EQUIPMENT_GPRS="G";


	 String[] titleLora = new String[]{"设备名称","设备编号","设备类型","数据模板","所属分组","所属用户","验证码","厂商ID","厂商名称","经纬度","备注"};
     String[] codeLora = new String[]{"name","dev_id","typeName","model_name", "groupName","uName","verification","support_code","supporter","site","remark"};
     String[] titleNbiot = new String[]{"设备名称","设备编号","设备类型","数据模板","所属分组","所属应用","所属用户","验证码","厂商ID","厂商名称","经纬度","备注"};
 	String[] codeNbiot = new String[]{"name","dev_id","typeName","model_name", "groupName","app_name","uName","verification","support_code","supporter","site","remark"};
 	String[] titleGPRS = new String[]{"设备名称","设备编号","设备类型","数据模板","所属分组","所属用户","验证码","厂商ID","厂商名称","经纬度","备注"};
	String[] codeGPRS = new String[]{"name","dev_id","typeName","model_name", "groupName","uName","verification","support_code","supporter","site","remark"};
	String[] titleDataLog = new String[]{"接收时间","设备编号","接收数据","接收结果","数据状态"};
	String[] codeDataLog = new String[]{"add_date","dev_id","src_data","dev_type","data_status"};
	PageInfo queryEquipmentList();

	List<Map<String, String>> getUserEquipmentIdList(Integer uid);

	List<Map<String, String>> getUserEquipmentIdListByProdId(Integer prodId);

	ResponeResult insertEqu(Map map);

	ResponeResult deleteEquByID(Map map);


	void setEquStatus(String devId, String status);

	List<String> getEquipmentByStatus(String status);

	/**
	 * 更新设备的量程
	 * @param eid
	 * @param range
	 * @return
	 */
	int updateRange(String eid, Integer range);

	/**
	 * 查询设备的量程
	 * @param eid
	 * @return
	 */
	Integer queryRange(String eid);

	Integer getUserParent(Integer uid);

	EquipmentVO getEquipmentInfo(String devId);

	List<String> getEquipmentIdListAll();

	PageInfo queryEquipmentPage();

	Equipment getEquById();


	/**
	 * 根据类型、在线状态查找设备ID
	 * @param type_cd
	 * @param online
	 * @return
	 */
	List<String> queryByTypeAndOnline( String type_cd, String online);

	ResponeResult getEquEditById();

	ResponeResult editEqu(Map map);


	/**
	 * 更新设备在线离线状态
	 * @param online
	 * @param list
	 * @param date
	 */
	void updateOnlineStatus(String online , List<String> list,Date date);

	void exportEquipments(String typeName, String type, String[] header, String[] key);


	String saveUploadImport(Map map, String[] typecode, String type);

	List<UploadResultEntity> getUploadResult();

	ResponeResult setDataLogParam(Map map);

	void exportDataLogByParam();

	ResponeResult getEquSelectList();

	ResponeResult setDataDownloadParam(Map map);

	void exprDataDownloadByParam();

	ResponeResult getEquSelectListByAppId();

	Equipment getEquById(String devId);

	/**
	 * 更新设备数据点时间
	 * @param list
	 * @param addDate
	 */
	void updateEquDataPointDate(List<DataDetail> list, Date addDate);

	/**
	 * 查找设备在某时间段内的新增数
	 * @param list
	 * @param startDay
	 * @param endDay
	 * @return
	 */
	Map queryAddEquCount(Date startDay,Date endDay,String type);
}
