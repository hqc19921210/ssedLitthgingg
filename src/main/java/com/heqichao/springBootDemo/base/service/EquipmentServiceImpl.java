package com.heqichao.springBootDemo.base.service;

import com.heqichao.springBootDemo.base.mapper.EquipmentMapper;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.entity.ParamObject;
import com.heqichao.springBootDemo.base.entity.UploadResultEntity;
import com.heqichao.springBootDemo.base.entity.User;
import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.util.CollectionUtil;
import com.heqichao.springBootDemo.base.util.ExcelWriter;
import com.heqichao.springBootDemo.base.util.FileUtil;
import com.heqichao.springBootDemo.base.util.PageUtil;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.entity.DataDetail;
import com.heqichao.springBootDemo.module.mqtt.MqttUtil;
import com.heqichao.springBootDemo.module.service.DataLogService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

/**
 * @author Muzzy Xu.
 * 
 */


@Service
@Transactional(rollbackFor = { Exception.class })
public class EquipmentServiceImpl implements EquipmentService {
	@Autowired
	private DataLogService dataLogService;
    @Autowired
    private EquipmentMapper eMapper ;

    @Override
    public PageInfo queryEquipmentList() {
    	Map map = RequestContext.getContext().getParamMap();
    	String eid = StringUtil.getStringByMap(map,"eid");
    	Integer gid = StringUtil.getIntegerByMap(map,"gid");
		if(gid !=null && -1 == gid){
			gid =null;
		}
    	String type = StringUtil.getStringByMap(map,"type");
    	String seleStatus = StringUtil.getStringByMap(map,"seleStatus");
    	PageUtil.setPage();
        PageInfo pageInfo = new PageInfo(eMapper.getEquipments(
        		ServletUtil.getSessionUser().getCompetence(),
        		ServletUtil.getSessionUser().getId(),
        		ServletUtil.getSessionUser().getParentId(),
        		gid,eid,type,seleStatus
        		));
    	return pageInfo;
    }
    
    //列表展示核心查询
    @Override
    public PageInfo queryEquipmentPage() {
    	Map map = RequestContext.getContext().getParamMap();
    	String eid = StringUtil.getStringByMap(map,"eid");
    	Integer gid = StringUtil.getIntegerByMap(map,"gid");
		if(gid !=null && -1 == gid){
			gid =null;
		}
    	String type = StringUtil.getStringByMap(map,"type");
    	String seleStatus = StringUtil.getStringByMap(map,"seleStatus");
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
		Integer uid = ServletUtil.getSessionUser().getId();
		Integer pid = ServletUtil.getSessionUser().getParentId();
		PageUtil.setPage();
		PageInfo pageInfo = new PageInfo(eMapper.getEquipmentsForDevLstOrderBy(cmp,uid,pid,gid,eid,type,seleStatus));
		List<Map<String,Object>> newLst = new ArrayList<Map<String,Object>>();
//    	List<Equipment> eLst = eMapper.getEquipmentsForDevLstOrderBy(cmp,uid,pid,gid,eid,type,seleStatus);
    	List<Equipment> eLst = (List<Equipment>)pageInfo.getList();
    	List<DataDetail> pLst =eMapper.queryDetailPointList(eLst,cmp,uid,pid);
//    	// 将list转为Map,这里key一定要为唯一值
//        Map<String, Equipment> eLstMap = eLst.stream().collect(
//                Collectors.toMap(e -> e.getDevId(),
//                        e -> e));
//     // 设备匹配数据点
//        newLst = pLst.stream().map(p -> {
//            return toEquipmentShow(eLstMap.get(p.getDevId()), p);
//        }).collect(Collectors.toList());
    	for (Equipment equ : eLst) {
    		Map<String,Object> newClu= new HashMap<String,Object>();
    		newClu.put("name", equ.getName());
    		newClu.put("devId", equ.getDevId());
    		newClu.put("type", equ.getTypeName());
    		newClu.put("online", equ.getOnline());
    		List<DataDetail> currPoint = new ArrayList<>();
    		try {
    			for (DataDetail point : pLst) {
    				if(equ.getDevId().equals(point.getDevId())) {
    					currPoint.add(point);
    				}
    			}
    			
    		}catch (Exception e) {
    			e.printStackTrace();
    			
			}
    		newClu.put("dataPoints",currPoint);
    		newLst.add(newClu);
    	}
//        PageInfo pageInfo = new PageInfo(newLst);
    	pageInfo.setList(newLst);
        return pageInfo;
    }
    public Map<String,Object> toEquipmentShow(Equipment equ,DataDetail point){
    	Map<String,Object> newClu= new HashMap<String,Object>();
    	if(equ == null) {
    		return newClu;
    	}
    	newClu.put("name", equ.getName());
		newClu.put("devId", equ.getDevId());
		newClu.put("type", equ.getTypeName());
		newClu.put("online", equ.getOnline());
		newClu.put("dataPoints",point);
    	return newClu;
    }
    /**
     * 根据uid查找所有设备
     */
    @Override
    public List<Map<String,String>> getUserEquipmentIdList(Integer uid) {
    	return eMapper.getUserEquipmentIdList(uid);
    }
    /**
     * 根据uid查找父客户
     */
    @Override
    public Integer getUserParent(Integer uid) {
    	return eMapper.getUserParent(uid);
    }
    /**
     * 根据dev_id查找设备信息
     */
    @Override
    public Equipment getEquipmentInfo(String  devId) {
    	return eMapper.getEquById(devId);
    }
    
    @Override
    public Equipment getEquById() {
    	Map map = RequestContext.getContext().getParamMap();
    	String devId = StringUtil.getStringByMap(map,"devId");
    	return eMapper.getEquById(devId);
    }
    
    //传参获取设备信息
    @Override
    public Equipment getEquById(String devId) {
    	return eMapper.getEquById(devId);
    }

	@Override
	public List<String> queryByTypeAndOnline(String type_cd, String online) {
		return eMapper.queryByTypeAndOnline(type_cd, online);
	}

	@Override
	public void updateOnlineStatus(String online, List<String> list, Date date) {
		if(list==null || list.size()<1){
			return;
		}
		eMapper.updateOnlineStatus(online,list,date);
	}

	/**
     * 查找所有设备dev_id
     */
    @Override
    public List<String> getEquipmentIdListAll() {
    	return eMapper.getEquipmentIdListAll();
    }
    // 根据杆塔ID设置状态
    @Override
    public void setEquStatus(String eid,String status) {
    	 eMapper.setEquStatus(eid,status);
    }
    // 根据状态获取有效设备杆塔ID数组
    @Override
    public List<String> getEquipmentByStatus(String status) {
    	return eMapper.getEquipmentByStatus(status);
    }

	@Override
	public int updateRange(String eid, Integer range) {
		return eMapper.updateRange(eid, range);
	}

	@Override
	public Integer queryRange(String eid) {
		return eMapper.queryRange(eid);
	}

	@Override
    public ResponeResult insertEqu(Map map) {
    	Equipment equ = new Equipment(map);
    	Integer uid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	Integer gid = equ.getGroupId();
    	if(equ.getName() == null ||equ.getDevId() == null || uid == null || cmp == 4) {
    		return new ResponeResult(true,"Add Equipment Input Error!","errorMsg");
    	}
    	if(eMapper.duplicatedEid(equ.getDevId(),equ.getUid())) {
    		return new ResponeResult(true,"设备编号重复","errorMsg");
    	}
    	if(cmp ==2) {
    		equ.setGroupAdmId(gid);
    		equ.setGroupId(1);
    	}else {
    		equ.setGroupAdmId(1);
    		equ.setGroupId(gid);
    	}
		equ.setAddUid(uid);
		equ.setValid("N");
		if(eMapper.insertEquipment(equ)>0) {
			if("L".equals(equ.getTypeCd())) {
				List<String> mqId = new ArrayList<String>();
				mqId.add(equ.getDevId());
				try {
					MqttUtil.subscribeTopicMes(mqId);
					return new ResponeResult();
				} catch (Exception e) {
					e.printStackTrace();
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return new ResponeResult(true,"创建错误，mqtt注册失败","errorMsg");
				}
			}else {
				return new ResponeResult();
			}
		}
    	return  new ResponeResult(true,"Add Equipment fail","errorMsg");
    }
	@Override
	public ResponeResult editEqu(Map map) {
		Equipment equ = new Equipment(map);
		Integer uid = ServletUtil.getSessionUser().getId();
		Integer cmp = ServletUtil.getSessionUser().getCompetence();
		Integer gid = equ.getGroupId();
		if(equ.getId()==null ||equ.getName() == null ||equ.getDevId() == null || uid == null || cmp == 4) {
			return new ResponeResult(true,"Edit Equipment Input Error!","errorMsg");
		}
		String oId = eMapper.getEquIdOld(equ.getId());
		boolean chgDevId = !oId.equals(equ.getDevId());//true为修改了设备编号
		if(chgDevId&&eMapper.duplicatedEid(equ.getDevId(),equ.getUid())) {
			return new ResponeResult(true,"设备编号重复","errorMsg");
		}
		if(cmp ==2) {
    		equ.setGroupAdmId(gid);
    		equ.setGroupId(1);
    	}else {
    		equ.setGroupAdmId(1);
    		equ.setGroupId(gid);
    	}
		equ.setUdpUid(uid);
		equ.setValid("N");
		if(eMapper.editEquipment(equ)>0) {
			if(chgDevId&&"L".equals(equ.getTypeCd())) {
				List<String> omqId = new ArrayList<String>();
				omqId.add(oId);
				try {
					MqttUtil.unSubscribeTopicMes(omqId);
					List<String> mqId = new ArrayList<String>();
					mqId.add(equ.getDevId());
					MqttUtil.subscribeTopicMes(mqId);
					return new ResponeResult();
				} catch (Exception e) {
					e.printStackTrace();
					 TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					 return new ResponeResult(true,"修改错误，mqtt注册失败","errorMsg");
				}
			}else {
				return new ResponeResult();
			}
		}
		return  new ResponeResult(true,"Edit Equipment fail","errorMsg");
	}
	@Override
	public ResponeResult getEquEditById() {
		Map map = RequestContext.getContext().getParamMap();
		String devId = StringUtil.getStringByMap(map,"devId");
		Integer id = StringUtil.getIntegerByMap(map,"id");
		Integer uid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(cmp == 4) {
    		return new ResponeResult(true,"无编辑权限","errorMsg");
    		
    	}
    	if(cmp != 2 && !eMapper.duplicatedEid(devId,uid)) {
    		return new ResponeResult(true,"无编辑权限","errorMsg");
    		
    	}
		return new ResponeResult(eMapper.getEquEditById(devId,id));
	}
    
    @Override
    public ResponeResult deleteEquByID(Map map) {
    	Integer eid = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"eid"));
    	String devId = StringUtil.getStringByMap(map,"devId");
    	Integer udid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(  eid == null || udid == null || cmp == 4) {
    		return new ResponeResult(true,"Delete fail!","errorMsg");
    	}else {
			if(StringUtil.isNotEmpty(devId)){
				dataLogService.deleteDataLog(devId);
			}
    		if(eMapper.delEquById(eid,udid)>0) {
    			return new ResponeResult();
    		}

    	}
    	return  new ResponeResult(true,"Delete Equipment fail","errorMsg");
    }
    
    @Override
    public void exportEquipments(String typeName,String type,String[] header,String[] key) {
    	Integer uid = ServletUtil.getSessionUser().getId();
    	Integer pid = ServletUtil.getSessionUser().getParentId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	List<Map<String,Object>> lst = eMapper.getEquipmentsForExport(cmp, uid, pid, type);
    	if(lst!=null){

            FileOutputStream fos = null;
            File file =null;
            try{
                file = FileUtil.createTempDownloadFile("导出"+typeName+"设备.xls");
                fos= new FileOutputStream(file);
                // 声明一个工作薄
                HSSFWorkbook workbook = ExcelWriter.createWorkBook();
                ExcelWriter.export(workbook,typeName,header,lst,key);
                workbook.write(fos);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(fos!=null){
                    try {
                        fos.close();
                       // logger.info("文件地址:"+file.getPath());
                        download(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }
    
    @Override
    public ResponeResult getEquSelectList() {
    	Integer uid = ServletUtil.getSessionUser().getId();
    	Integer pid = ServletUtil.getSessionUser().getParentId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(uid==null) {
    		return new ResponeResult(true,"页面过期请刷新页面","errorMsg");
    	}
		Map<String, String> res =  eMapper.getEquSelectList(cmp,uid,pid).stream().collect(
						Collectors.toMap(Equipment::getName,Equipment::getDevId, (k1,k2)->k1)
					);
		if(res.size()>0) {
			return new ResponeResult(res);
		}
    	return  new ResponeResult(false,"");
    }
    
    @Override
    public ResponeResult getEquSelectListByAppId() {
    	Map map = RequestContext.getContext().getParamMap();
    	Integer aid = StringUtil.getIntegerByMap(map,"aid");
    	Integer uid = ServletUtil.getSessionUser().getId();
    	Integer pid = ServletUtil.getSessionUser().getParentId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(uid==null) {
    		return new ResponeResult(true,"页面过期请刷新页面","errorMsg");
    	}
    	Map<String, String> res =  eMapper.getEquSelectListByAppId(aid,cmp,uid,pid).stream().collect(
    			Collectors.toMap(Equipment::getName,Equipment::getDevId, (k1,k2)->k1)
    			);
    	if(res.size()>0) {
    		return new ResponeResult(res);
    	}
    	return  new ResponeResult(false,"");
    }
    
    @Override
    public ResponeResult setDataLogParam(Map map) {
    	String eid = StringUtil.getStringByMap(map,"eid");
    	if(StringUtil.isEmpty(eid)) {
    		return new ResponeResult(true,"没选中设备","errorMsg");
    	}
    	ParamObject param = new ParamObject();
    	param.setExcelParam(eid);
    	ServletUtil.setSessionParam(param);
    	return new ResponeResult();
    }
    @Override
    public ResponeResult setDataDownloadParam(Map map) {
    	String eid = StringUtil.getStringByMap(map,"eid");
    	String start= StringUtil.getStringByMap(map,"start");
        String end= StringUtil.getStringByMap(map,"end");
        if(StringUtil.isNotEmpty(end)){
            end=end+" 23:59:59";
        }
    	if(StringUtil.isEmpty(eid)) {
    		return new ResponeResult(true,"没选中设备","errorMsg");
    	}
    	ParamObject param = new ParamObject();
    	param.setDataPointExcelParam(eid);
    	param.setDataPointExcelStart(start);
    	param.setDataPointExcelEnd(end);
    	ServletUtil.setSessionParam(param);
    	return new ResponeResult();
    }
    
    @Override
    public void exportDataLogByParam() {
    	if(ServletUtil.getSessionUser().getCompetence()!=2) {
    		return;
    	}
    	List<Map<String,Object>> lst = eMapper.getDataLogForExport(ServletUtil.getSessionParam().getExcelParam());
    	if(lst!=null){
    		
    		FileOutputStream fos = null;
    		File file =null;
    		try{
    			file = FileUtil.createTempDownloadFile("DataLog"+System.currentTimeMillis()+".xls");
    			fos= new FileOutputStream(file);
    			// 声明一个工作薄
    			HSSFWorkbook workbook = ExcelWriter.createWorkBook();
    			ExcelWriter.export(workbook,"DataLog",titleDataLog,lst,codeDataLog);
    			workbook.write(fos);
    		}catch (Exception e){
    			e.printStackTrace();
    		}finally {
    			if(fos!=null){
    				try {
    					fos.close();
    					// logger.info("文件地址:"+file.getPath());
    					download(file);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    		
    		
    	}
    }
    @Override
    public void exprDataDownloadByParam() {
    	if(ServletUtil.getSessionUser().getCompetence()!=2) {
    		return;
    	}
    	List<Map<String,Object>> lst = eMapper.getDataDownloadForExport(
    			ServletUtil.getSessionParam().getDataPointExcelParam(),
    			ServletUtil.getSessionParam().getDataPointExcelStart(),
    			ServletUtil.getSessionParam().getDataPointExcelEnd());
    	if(lst.size()>0){
    		String colName = eMapper.getDataDownloadColumn(ServletUtil.getSessionParam().getDataPointExcelParam());
    		String[] colLst = colName.split(",");
    		FileOutputStream fos = null;
    		File file =null;
    		try{
    			file = FileUtil.createTempDownloadFile("DataPiont_"+System.currentTimeMillis()+".xls");
    			fos= new FileOutputStream(file);
    			// 声明一个工作薄
    			HSSFWorkbook workbook = ExcelWriter.createWorkBook();
    			ExcelWriter.export(workbook,"DataPiont",colLst,lst,colLst);
    			workbook.write(fos);
    		}catch (Exception e){
    			e.printStackTrace();
    		}finally {
    			if(fos!=null){
    				try {
    					fos.close();
    					// logger.info("文件地址:"+file.getPath());
    					download(file);
    				} catch (IOException e) {
    					e.printStackTrace();
    				}
    			}
    		}
    		
    		
    	}
    }
    @Override
    public String saveUploadImport(Map map,String[] typecode,String type) {
    	Integer uid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
        if(cmp==4){
            throw new ResponeException("无编辑权限");
        }
        if(map==null || map.size()<1){
        	throw new ResponeException("文件为空");
        }
        Iterator entries = map.entrySet().iterator();
        String resKey = type+"_"+System.currentTimeMillis();
        //循环工作簿
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            if(entry.getValue() instanceof ArrayList){
                List attrs= (List) entry.getValue();
                if(attrs!=null && attrs.size()>1){
                    //去掉第一行标题
                    for(int i=1;i<attrs.size();i++){
                        List<String> row = (List<String>) attrs.get(i);
                        //获取行数据Map
                        Map rowMap =  CollectionUtil.listStringTranToMap(row,typecode,true);
                        Equipment equ = new Equipment(rowMap,type);
                        checkUploadRow(equ, uid, cmp, i+1, resKey);
                    }
                }
            }
        }
        return resKey;
    }
    
    @Override
    public List<UploadResultEntity> getUploadResult() {
    	Map map = RequestContext.getContext().getParamMap();
    	String key = StringUtil.getStringByMap(map,"reskey");
    	List<UploadResultEntity> res = eMapper.getUploadResult(key);
    	return res;
    }

    
    public void checkUploadRow(Equipment equ,Integer uid,Integer cmp,Integer index,String resKey ) {
    	UploadResultEntity res = new UploadResultEntity();
    	res.setResKey(resKey);
    	res.setAddUid(uid);
    	if(StringUtil.isEmpty(equ.getName())) {
    		res.setResIndex(index);
    		res.setResStatus(UPLOAD_FAIL);
    		res.setErrReason("设备名称为空");
    		eMapper.insertUploadResult(res);
    		return;
    	}
    	if(StringUtil.isEmpty(equ.getDevId())) {
    		res.setResIndex(index);
    		res.setResStatus(UPLOAD_FAIL);
    		res.setErrReason("设备编号为空");
    		eMapper.insertUploadResult(res);
    		return;
    	}
    	if(StringUtil.isEmpty(equ.getModelName())) {
    		res.setResIndex(index);
    		res.setResStatus(UPLOAD_FAIL);
    		res.setErrReason("数据模板为空");
    		eMapper.insertUploadResult(res);
    		return;
    	}
    	if(StringUtil.isEmpty(equ.getGroupName())) {
    		res.setResIndex(index);
    		res.setResStatus(UPLOAD_FAIL);
    		res.setErrReason("所属分组为空");
    		eMapper.insertUploadResult(res);
    		return;
    	}
    	if(StringUtil.isEmpty(equ.getuName())) {
    		res.setResIndex(index);
    		res.setResStatus(UPLOAD_FAIL);
    		res.setErrReason("所属用户为空");
    		eMapper.insertUploadResult(res);
    		return;
    	}
    	if("N".equals(equ.getTypeCd())) {
    		if(StringUtil.isEmpty(equ.getAppName())) {
    			res.setResIndex(index);
    			res.setResStatus(UPLOAD_FAIL);
    			res.setErrReason("所属应用为空");
    			eMapper.insertUploadResult(res);
    			return;
    		}
    		
    		Integer appId = eMapper.getAppIdByName(equ.getAppName(), uid);
    		if(appId==null) {
        		res.setResIndex(index);
        		res.setResStatus(UPLOAD_FAIL);
        		res.setErrReason("此用户没有该所属应用");
        		eMapper.insertUploadResult(res);
        		return;
        	}
    		equ.setAppId(appId);
    	}
    	Integer currId = eMapper.getUserIdByName(equ.getuName());
    	if(cmp==3 && uid!=currId) {
    		res.setResIndex(index);
    		res.setResStatus(UPLOAD_FAIL);
    		res.setErrReason("操作账户无法为该用户添加设备");
    		eMapper.insertUploadResult(res);
    		return;
    	}
    	if(eMapper.duplicatedEid(equ.getDevId(),currId)) {
    		res.setResIndex(index);
    		res.setResStatus(UPLOAD_FAIL);
    		res.setErrReason("设备编号重复");
    		eMapper.insertUploadResult(res);
    		return;
    	}
    	Integer modelId = eMapper.getModelIdByName(equ.getModelName(), currId);
    	if(modelId==null) {
    		res.setResIndex(index);
    		res.setResStatus(UPLOAD_FAIL);
    		res.setErrReason("此用户没有该数据模板");
    		eMapper.insertUploadResult(res);
    		return;
    	}
    	Integer groupId = eMapper.getGroupIdByName(equ.getGroupName(), currId);
    	if(groupId==null) {
    		groupId=1;
    		res.setErrReason("此用户没有该设备分组，分配至默认分组");
    	}

		if(cmp ==2) {
			equ.setGroupAdmId(groupId);
		}else {
			equ.setGroupAdmId(1);
		}
    	equ.setUid(currId);
    	equ.setModelId(modelId);
    	equ.setGroupId(groupId);
    	equ.setAddUid(uid);
    	equ.setValid("N");
    	res.setResIndex(index);
		res.setResStatus(UPLOAD_SUCCESS);
    	eMapper.insertUploadResult(res);
    	eMapper.insertEquipment(equ);
    }
    
    protected void download(File file){
        if(file == null){
            return;
        }
        BufferedInputStream fis = null;
        BufferedOutputStream toClient = null;
        try {
            HttpServletResponse response=RequestContext.getContext().getResponse();
            fis = new BufferedInputStream(new FileInputStream(file));
            byte[] buffer = new byte[fis.available()];
            fis.read(buffer);
            // 清空response
            response.reset();
            toClient = new BufferedOutputStream(response.getOutputStream());
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String(file.getName().getBytes("gb2312"), "ISO8859-1"));
            toClient.write(buffer);
            toClient.flush();
        }catch (Exception err){
            throw new ResponeException(err);
        } finally{
            try {
                if (toClient != null) {
                    toClient.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                if (fis != null) {
                    fis.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            FileUtil.deleteFile(file);
        }
    }

}
