package com.heqichao.springBootDemo.module.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.heqichao.springBootDemo.module.entity.CommandLog;
import com.heqichao.springBootDemo.module.entity.LiteApplication;
import com.heqichao.springBootDemo.module.liteNA.JsonUtil;
import com.heqichao.springBootDemo.module.liteNA.LiteNAStringUtil;
import com.heqichao.springBootDemo.module.liteNA.MyNbiotCommands;
import com.heqichao.springBootDemo.module.liteNA.MyNbiotSubscription;
import com.heqichao.springBootDemo.module.mapper.LiteAppMapper;
import com.heqichao.springBootDemo.module.liteNA.Constant;
import com.heqichao.springBootDemo.module.liteNA.Crc16Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.util.AesUtil;
import com.heqichao.springBootDemo.base.util.PageUtil;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;

/**
 * 
 * @author Muzzy XU.
 *
 */

@Service
@Transactional
public class LiteAppServiceImp implements LiteAppService {
	Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LiteAppMapper liteAppMapper;
	

	@Override
	public PageInfo queryLiteApps() {
		Map map = RequestContext.getContext().getParamMap();
    	String deviceId = StringUtil.getStringByMap(map,"appId");
    	String name = StringUtil.getStringByMap(map,"appName");
    	PageUtil.setPage();
        PageInfo pageInfo = new PageInfo(liteAppMapper.queryLiteApps(
        		ServletUtil.getSessionUser().getId(),
        		ServletUtil.getSessionUser().getParentId(),
        		ServletUtil.getSessionUser().getCompetence(),
        		deviceId,name
        		));
		return pageInfo;
	}
	
	/**
	 * 添加应用
	 */
	@Override
    public ResponeResult addLiteApp() {
		Map map = RequestContext.getContext().getParamMap();
		LiteApplication app = new LiteApplication(map);
    	Integer uid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	Integer oid = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"seleCompany"));
    	if(app.getAppId() == null || uid == null || cmp == 4) {
    		return new ResponeResult(true,"Add Application Input Error!","errorMsg");
    	}
    	if(liteAppMapper.duplicatedAid(app.getAppId())) {
    		return new ResponeResult(true,"应用Id重复","errorMsg");
    	}
    	if(cmp == 2 && oid == null) {
    		return new ResponeResult(true,"Add Application Input Error!","errorMsg");
		}else {
    		if(cmp == 2) {
    			app.setOwnId(oid);
    		}else {
    			app.setOwnId(uid);
    		}
    		String appAuth = "https://"+app.getPlatformIp()+"/iocm/app/sec/v1.1.0/login";
    		String callbackUrl = "http://120.78.181.134:8082/service/nbiotCallback/dataChanged/"+app.getAppId().replaceAll("-", "");
    		String postAsynCmd = "https://"+app.getPlatformIp()+"/iocm/app/cmd/v1.4.0/deviceCommands";
    		String subscribeNotifycation = "https://"+app.getPlatformIp()+"/iocm/app/sub/v1.1.0/subscribe";
    		app.setAppAuth(appAuth);
    		app.setCallbackUrl(callbackUrl);
    		app.setPostAsynCmd(postAsynCmd);
    		app.setSubscribeNotifycation(subscribeNotifycation);
    		app.setUpdateUid(uid);
    		app.setStatus("N");
    		app.setSecret(AesUtil.aesEncrypt(app.getSecret()));
    		if(liteAppMapper.insertLiteApplication(app)>0) {
					return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Add Application fail","errorMsg");
    }
	
	@Override
	public ResponeResult updLiteApp() {
		Map map = RequestContext.getContext().getParamMap();
		LiteApplication app = new LiteApplication(map);
		Integer uid = ServletUtil.getSessionUser().getId();
		Integer cmp = ServletUtil.getSessionUser().getCompetence();
		if(app.getAppId() == null || uid == null || cmp == 4) {
			return new ResponeResult(true,"Update Application Input Error!","errorMsg");
		}
		if(liteAppMapper.duplicatedEidByUpd(app.getAppId(),app.getId())) {
			return new ResponeResult(true,"应用Id重复","errorMsg");
		}
		String appAuth = "https://"+app.getPlatformIp()+"/iocm/app/sec/v1.1.0/login";
		String callbackUrl = "http://120.78.181.134:8082/service/nbiotCallback/"+app.getAppId().replaceAll("-", "");
		String postAsynCmd = "https://"+app.getPlatformIp()+"/iocm/app/cmd/v1.4.0/deviceCommands";
		String subscribeNotifycation = "https://"+app.getPlatformIp()+"/iocm/app/sub/v1.1.0/subscribe";
		app.setAppAuth(appAuth);
		app.setCallbackUrl(callbackUrl);
		app.setPostAsynCmd(postAsynCmd);
		app.setSubscribeNotifycation(subscribeNotifycation);
		app.setUpdateUid(uid);
		if(liteAppMapper.updateLiteEquipment(app)>0) {
			return new ResponeResult();
		}
		return  new ResponeResult(true,"Update Application fail","errorMsg");
	}
	
	/**
	 * 重置应用密匙
	 */
	@Override
	public ResponeResult resetSecret() {
		Map map = RequestContext.getContext().getParamMap();
		LiteApplication app = new LiteApplication(map);
		Integer uid = ServletUtil.getSessionUser().getId();
		Integer cmp = ServletUtil.getSessionUser().getCompetence();
		if(app.getId() == null ||app.getSecret() == null || uid == null || cmp == 4) {
			return new ResponeResult(true,"Reset Input Error!","errorMsg");
		}
		app.setSecret(AesUtil.aesEncrypt(app.getSecret()));
		app.setUpdateUid(uid);
		if(liteAppMapper.resetAppSecret(app)>0) {
			return new ResponeResult();
		}
		return  new ResponeResult(true,"Reset fail","errorMsg");
	}

	@Override
    public ResponeResult deleteAppByID(Integer id) {
    	Integer udid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(  udid == null || cmp == 4) {
    		return new ResponeResult(true,"Delete fail!","errorMsg");
    	}else {
    		if(liteAppMapper.deleteById(id,udid)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Delete Application fail","errorMsg");
    }
	/**
	 * 获取应用下拉kv
	 */
	@Override
    public ResponeResult getAppSelectList() {
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	Integer uid = ServletUtil.getSessionUser().getId();
    	if(uid == null || cmp != 2) {
    		return  new ResponeResult(false,"");
    	}
		Map<String, Integer> res =  liteAppMapper.getAppSelectList().stream().collect(
						Collectors.toMap(LiteApplication::getAppName,LiteApplication::getId, (k1,k2)->k1)
					);
		return new ResponeResult(res);
    }
	
	/**
	 * 命令下发
	 */
	@Override
	public String postCommand(Integer aid,Map params) throws Exception {
		MyNbiotCommands myNbiotCommandUntils = new MyNbiotCommands();
		LiteApplication lapp = liteAppMapper.getAppInfo(aid);//获取应用信息
		// 构建post实体
        ObjectNode paras = JsonUtil.convertObject2ObjectNode("{\""+params.get("paramName")+"\":\""+params.get("paramValue")+"\"}");
      
        Map<String, Object> paramCommand = new HashMap<>();
        paramCommand.put("serviceId", params.get("serviceId"));
        paramCommand.put("method", params.get("method"));
        paramCommand.put("paras", paras);      
        
        Map<String, Object> paramPostAsynCmd = new HashMap<>();
        paramPostAsynCmd.put("deviceId", params.get("commandEid"));
        paramPostAsynCmd.put("command", paramCommand);
        paramPostAsynCmd.put("callbackUrl", lapp.getCallbackUrl());
        
        return myNbiotCommandUntils.postCommand(paramPostAsynCmd, lapp);
	}
	
	//页面下发命令
	@Override
	public ResponeResult postCommandList(Map info,List<Boolean> selectlist,List<Map> cmdlist) throws Exception {
		if(info==null || selectlist==null || selectlist.size()==0 || cmdlist==null || cmdlist.size()==0) {
			return new ResponeResult(true,"没找到可下发命令","errorMsg");
		}
		
		try {
			//校验组装下发命令十六进制字符串和数据标记
			StringBuffer sbData = new StringBuffer();//数据标记
			StringBuffer sbRes = new StringBuffer();//下发命令
			Map<String,Object> mLog = new HashMap<String,Object>();//组装下发内容
			for(int i=0;i<selectlist.size();i++) {
				if(selectlist.get(i) != null && selectlist.get(i)) {//有勾选
					sbData.insert(0, "1");//数据标记
					Map entity = cmdlist.get(i);//命令行属性entity
					String dataType = (String) entity.get("dataType");//数据类型
					String contextName = (String) entity.get("attrName");//下发内容名称
					if("DATE_TYPE".equals(dataType)) {
						String param = (String)entity.get("paramDate");
						if(param.length()==14) {
							param = param.substring(2, 14);//截掉头，剩下12位
							sbRes.append(param);
							mLog.put(contextName, LiteNAStringUtil.dateUTC2LocalString(entity.get("param").toString()));//下发内容转本地日期格式
						}else {
							return new ResponeResult(true,"时间日期输入不正确","errorMsg");
						}
						continue;
					}
					if("SWITCH_TYPE".equals(dataType)) {
						boolean param = Boolean.parseBoolean((String) entity.get("param"));
						if(param) {
							sbRes.append("0001");
							mLog.put(contextName, "开");//下发内容
						}else {
							sbRes.append("0000");
							mLog.put(contextName, "关");//下发内容
						}
						continue;
					}
					if("ENUMERATION_TYPE".equals(dataType)) {
						Integer param = (Integer)entity.get("paramValue");
						String res = entity.get("paramKey").toString();
						sbRes.append(LiteNAStringUtil.bytes2HexString(LiteNAStringUtil.int_byte2_AB(param)));
						mLog.put(contextName, res);//下发内容
						continue;
					}
					if("INT_TYPE".equals(dataType)) {
						String valueType=(String)entity.get("valueType");//值类型
						Integer numMin = (Integer)entity.get("numMin") == null ? 0 : (Integer)entity.get("numMin");//最小值
						Integer param = (Integer)entity.get("param");
						if("TWO_UNSIGNED".equals(valueType)) {
							Integer numMax = (Integer)entity.get("numMax") == null ? 65536 : (Integer)entity.get("numMax");
							if(param == null || param.compareTo(numMax) > 0 || param.compareTo(numMin) < 0) {
								return new ResponeResult(true,"两字节整型输入不正确","errorMsg");
							}
							sbRes.append(LiteNAStringUtil.bytes2HexString(LiteNAStringUtil.int_byte2_AB(param)));//
							mLog.put(contextName, entity.get("param"));//下发内容
							continue;
						}
						if("FOUR_NOSIGNED_ABCD".equals(valueType)) {
							Integer numMax=(Integer)entity.get("numMax") == null ? 2147483647 : (Integer)entity.get("numMax");
							if(param == null || param.compareTo(numMax) > 0 || param.compareTo(numMin) < 0) {
								return new ResponeResult(true,"四字节整型输入不正确","errorMsg");
							}
							sbRes.append(LiteNAStringUtil.bytes2HexString(LiteNAStringUtil.int_byte4_ABCD(param)));//
							mLog.put(contextName, entity.get("param"));//下发内容
							continue;
						}
						if("FOUR_NOSIGNED_CDAB".equals(valueType)) {
							Integer numMax = (Integer)entity.get("numMax") == null ? 2147483647 : (Integer)entity.get("numMax");
							if(param == null || param.compareTo(numMax) > 0 || param.compareTo(numMin) < 0) {
								return new ResponeResult(true,"四字节整型输入不正确","errorMsg");
							}
							sbRes.append(LiteNAStringUtil.bytes2HexString(LiteNAStringUtil.int_byte4_CDAB(param)));//
							mLog.put(contextName, entity.get("param"));//下发内容
							continue;
						}
					}
					
				}else {
					sbData.insert(0, "0");
				}
			}
			//modbus命令组装
			String sbDataToString = LiteNAStringUtil.bString2HexString(sbData.toString());//标志位，大于65535使用四字节
			int count = (sbDataToString.length()+sbRes.length())/2;//总字节数
			String countData = LiteNAStringUtil.bytes2HexString(LiteNAStringUtil.int_byte(count));//十六进制总字节数，单字节
			String halfData = LiteNAStringUtil.bytes2HexString(LiteNAStringUtil.int_byte2_AB(count/2));//十六进制寄存器数，两字节
			sbRes.insert(0, sbDataToString);
			sbRes.insert(0, countData);
			sbRes.insert(0, halfData);
			sbRes.insert(0, "01100001");
			String crcString = Crc16Util.getRestultByString(sbRes.toString());//计算校验位
			MyNbiotCommands myNbiotCommandUntils = new MyNbiotCommands();
			LiteApplication lapp = liteAppMapper.getAppInfo((Integer)info.get("appId"));//获取应用信息
			// 构建post实体
			ObjectNode paras = JsonUtil.convertObject2ObjectNode("{\"sset_cmd\":\""+crcString+"\"}");
			
			Map<String, Object> paramCommand = new HashMap<>();
			paramCommand.put("serviceId", "sset_service");
			paramCommand.put("method", "sset_cmd");
			paramCommand.put("paras", paras);      
			
			Map<String, Object> paramPostAsynCmd = new HashMap<>();
			paramPostAsynCmd.put("deviceId", info.get("devId"));
			paramPostAsynCmd.put("command", paramCommand);
			paramPostAsynCmd.put("callbackUrl", LiteNAStringUtil.getCallBackURLByType(lapp.getAppId(), Constant.CALLBACK_COMMAND_URL));
			String callback = myNbiotCommandUntils.postCommand(paramPostAsynCmd, lapp);
			//下发历史
			
			CommandLog cmdlog = new CommandLog();
			cmdlog.setDevId(info.get("devId").toString());
			cmdlog.setCallback(callback);
			cmdlog.setContext(mLog.toString());
			cmdlog.setAddUid(ServletUtil.getSessionUser().getId());
			try {
				Map mCallback = JSONObject.parseObject(callback, Map.class);
				cmdlog.setCmdStatus(mCallback.get("status").toString());
				cmdlog.setCmdId(mCallback.get("commandId").toString());
			} catch (Exception e) {
				e.printStackTrace();
				cmdlog.setCmdStatus(LiteAppService.FAILED);
				
			}
			liteAppMapper.insertCommandLog(cmdlog);
			return new ResponeResult();
			
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			// TODO: handle exception
			return new ResponeResult(true,"系统错误，请联系技术员","errorMsg");
		}
	}
	/**
	 * 订阅数据变化
	 */
	@Override
	public ResponeResult subLiteDataChg() throws Exception {
		Map map = RequestContext.getContext().getParamMap();
		Integer id = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"id"));
		Integer udid = ServletUtil.getSessionUser().getId();
		LiteApplication app = liteAppMapper.getAppInfo(id);//获取应用信息
		if(  app.getAppId() == null || udid == null ) {
			return new ResponeResult(true,"Subscribe fail!","errorMsg");
		}
		try {
			MyNbiotSubscription sub = new MyNbiotSubscription();
			if("HTTP/1.1 201 Created".equals(sub.subLiteDataChg(app))) {
				return  new ResponeResult();
			}
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return  new ResponeResult(true,"Subscribe fail!","errorMsg");
	}
	
	/**
	 * 查询命令历史
	 */
	@Override
	public PageInfo getCommandLogByDevId() {
		Map map = RequestContext.getContext().getParamMap();
    	String devId = StringUtil.getStringByMap(map,"devId");
    	PageUtil.setPage();
        PageInfo pageInfo = new PageInfo(liteAppMapper.getCommandLogByDevId(devId));
		return pageInfo;
	}
	
	/**
	 * 下发命令通知回调
	 */
	@Override
	public void liteNaCommandCallback(String str) {
		logger.info("接收下发回调JSON："+str);
		try {
			//正常回调格式：{"result":{"resultCode":"DELIVERED"},"deviceId":"16da4e0e-d460-4d00-bc0f-445f7953eec7","commandId":"4ee0637f20bf44568e07202db0acb03c"}
			Map mCallback = JSONObject.parseObject(str, Map.class);
			Map mResult = JSONObject.parseObject(mCallback.get("result").toString(), Map.class);
			CommandLog cmdlog = new CommandLog();
			cmdlog.setCmdId(mCallback.get("commandId").toString());
			cmdlog.setRequest(str);
			cmdlog.setCmdStatus(mResult.get("resultCode").toString());
			liteAppMapper.updateCommandLog(cmdlog);
			
		} catch (Exception e) {
			e.printStackTrace();
			
		}finally {
			//插入订阅记录表
			liteAppMapper.insertSubscriptionLog(str, Constant.COMMAND_RSP);
		}
	}
	/**
	 * 数据变化回调
	 */
	@Override
	public Map<String, String> liteNaDataChangedCallback(String str) {
		logger.info("接收数据变化JSON："+str);
			//插入订阅记录表
		liteAppMapper.insertSubscriptionLog(str, Constant.DEVICE_DATA_CHANGED);
		return LiteNAStringUtil.NBReturnFmt(str);
	}
	
}
