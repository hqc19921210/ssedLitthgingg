package com.heqichao.springBootDemo.module.service;

import java.util.HashMap;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.alibaba.fastjson.JSONObject;
import com.heqichao.springBootDemo.module.entity.LightningLog;
import com.heqichao.springBootDemo.module.entity.LiteApplication;
import com.heqichao.springBootDemo.module.entity.LiteLog;
import com.heqichao.springBootDemo.module.mapper.LiteAppMapper;
import com.heqichao.springBootDemo.module.mapper.LiteLogMapper;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.service.UserService;
import com.heqichao.springBootDemo.base.util.PageUtil;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.module.liteNA.Constant;
import com.heqichao.springBootDemo.module.liteNA.HttpsUtil;
import com.heqichao.springBootDemo.module.liteNA.JsonUtil;
import com.heqichao.springBootDemo.module.liteNA.MyNbiotCommands;
import com.heqichao.springBootDemo.module.liteNA.StreamClosedHttpResponse;


@Service
@Transactional
public class LiteNAServiceImp implements LiteNAService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	private static String map;  
	private static Integer DEFAULE_RANGE =100000;

	@Autowired
	private LiteLogMapper liteLogMapper;
	
	@Autowired
    private LiteEquService liteEquService;
	
	
	@Override
	public Object getDataChange() throws Exception {
        return map;
	}
	

	@Override
	public List<LiteLog> queryAll() {
		return liteLogMapper.queryAll();
	}
	@Override
	public PageInfo queryLites() {
		Map map = RequestContext.getContext().getParamMap();
    	String deviceId = StringUtil.getStringByMap(map,"deviceId");
    	String start = StringUtil.getStringByMap(map,"start");
    	String end = StringUtil.getStringByMap(map,"end");
    	if(StringUtil.isNotEmpty(end)){
            end=end+" 23:59:59";
        }
    	PageUtil.setPage();
        PageInfo pageInfo = new PageInfo(liteLogMapper.queryLites(
        		ServletUtil.getSessionUser().getId(),
        		ServletUtil.getSessionUser().getParentId(),
        		ServletUtil.getSessionUser().getCompetence(),
        		deviceId,start,end
        		));
		return pageInfo;
	}

	@Override
	public void deleteAll() {
		liteLogMapper.deleteAll();
	}

	@Override
	public void chg() {
		String mes =RequestContext.getContext().getParamMap().toString();
//		logger.info("调用回调url传入参数："+mes);
		this.map=mes;

		JSONObject jsonObject =changeMse(mes);
		JSONObject service =jsonObject.getJSONObject("service");
		String deviceId =jsonObject.getString("deviceId");
		JSONObject data=service.getJSONObject("data");
//		String currenState= (String) data.get("Curren_state");
		String eventTime= (String) service.get("eventTime");
		//20180821T022524Z
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date=null;
		try {
			date=simpleDateFormat.parse(eventTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        localFormater.setTimeZone(TimeZone.getDefault());
        Date localTime=null;
        try {
			 localTime = localFormater.parse(localFormater.format(date.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LiteLog log =new LiteLog(mes,data.toJSONString(),localTime);
		log.setDeviceId(deviceId);
		liteLogMapper.saveLog(log);

	}
	
	/**
	 * 新版本NB接受数据后解析数据
	 */
	@Override
	public Map<String,String> NBReturnFmt(Object map){
		String mes =map.toString();
		logger.info("调用回调url传入参数："+mes);
		
		JSONObject jsonObject =changeMse(mes);
		JSONObject service =jsonObject.getJSONObject("service");
		String deviceId =jsonObject.getString("deviceId");
		JSONObject data=service.getJSONObject("data");
		String dataFmt = data.toJSONString().split(":")[1];
		dataFmt =dataFmt.replaceAll("\"", "");
		dataFmt =dataFmt.replaceAll("}", "");
		Map<String,String> res = new HashMap<String, String>();
		res.put("devid", deviceId);
		res.put("data", dataFmt);
		res.put("srcdata", mes);
		return res;
		
	}
	@Override
	public void chg(Object map) {
		String mes =map.toString();
		logger.info("调用回调url传入参数："+mes);
		this.map=mes;
		
		JSONObject jsonObject =changeMse(mes);
		JSONObject service =jsonObject.getJSONObject("service");
		String deviceId =jsonObject.getString("deviceId");
		JSONObject data=service.getJSONObject("data");
		String eventTime= (String) service.get("eventTime");
		//20180821T022524Z
		SimpleDateFormat simpleDateFormat =new SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'");
		simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date date=null;
		try {
			date=simpleDateFormat.parse(eventTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		SimpleDateFormat localFormater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		localFormater.setTimeZone(TimeZone.getDefault());
		Date localTime=null;
		try {
			localTime = localFormater.parse(localFormater.format(date.getTime()));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			String currenState= (String) data.get("LightningParameter");
			saveTransData(currenState,deviceId,localTime);
		} catch (Exception e) {
			e.printStackTrace();
		}
		LiteLog log =new LiteLog(mes,data.toJSONString(),localTime);
		log.setDeviceId(deviceId);
		liteLogMapper.saveLog(log);
		
	}

	private static  JSONObject changeMse(String mes){
		mes=mes.replaceAll("=",":");
		mes=mes.replaceAll("\"","");
		mes=mes.replaceAll(" ","");
		String newMes ="";
		char[] mesList =mes.toCharArray();
		for(char c :mesList){
			if('{' == c ){
				newMes = newMes+c+"\"";
			}else  if('}' == c){
				newMes = newMes+"\""+c;
			}else if(':' == c || ',' == c){
				newMes = newMes+"\""+c+"\"";
			}else{
				newMes=newMes+c;
			}
		}
		newMes= newMes.replace("\"{\"","{\"");
		newMes= newMes.replace("\"}\"","\"}");
		JSONObject jsonObject = JSONObject.parseObject(newMes);
		return jsonObject;
	}
	
	
	
	@Override
    public PageInfo queryNBLightLog() {
        List<String> list =getMyDevList();
        List<LightningLog> res=new ArrayList<LightningLog>();
        if(list!=null && list.size()>0){
            PageUtil.setPage();
            Map map = RequestContext.getContext().getParamMap();
            String functionCode= (String) map.get("functionCode");
            String devEUI= (String) map.get("devEUI");
            String start= (String) map.get("start");
            String end= (String) map.get("end");
            if(StringUtil.isNotEmpty(end)){
                end=end+" 23:59:59";
            }
            res= liteLogMapper.queryLightningLogByDevIds(list,devEUI,end,start,functionCode);
        }

        PageInfo pageInfo = new PageInfo(res);
        return pageInfo;

    }
	private List<String > getMyDevList(){
        List<String> list =new ArrayList<String>();
        Integer cmp =ServletUtil.getSessionUser().getCompetence();
        if(cmp !=null ){
            //管理员查询所有
            if(UserService.ROOT.equals(cmp)){
                list=liteEquService.getNBEquipmentIdListAll();

            } else  if(UserService.CUSTOMER.equals(cmp)){  //用户查自己设备
                list=liteEquService.getUserNBEquipmentIdList(ServletUtil.getSessionUser().getId());
            } else {
            	list=liteEquService.getUserNBEquipmentIdListByParent(ServletUtil.getSessionUser().getId());
            }
        }
        return list;
    }
	/*
	 * 保存解析后的数据
	 */
	public void saveTransData(String currData,String deviceId,Date time) {
		LightningLog log =new LightningLog();

        double range =DEFAULE_RANGE;
//        Integer ra= equipmentService.queryRange(devEUI);
//        if(ra!=null && ra >0){
//            range=ra;
//        }
        range=range/1000; //单位KA

        log.setDevEUI(deviceId);
        log.setData(currData);
        log.setTime(time);
        String[] transDatas =StringUtil.string2StringArray(currData);
        if(transDatas.length == 27) {
		 log.setDevicePath(transDatas[0]);  //设备地址
         log.setFunctionCode(transDatas[1]);//功能码
         log.setDataLen(transDatas[2]);//数据区长度
         log.setLigntningCount(Integer.parseInt(transDatas[3]+transDatas[4],16));//雷击次数
         log.setfPort(Integer.parseInt(transDatas[5]+transDatas[6],16));//脱扣状态
         String ligntningTime="20"+transDatas[7]+"-"+transDatas[8]+"-"+transDatas[9]+" "+transDatas[10]+":"+transDatas[11]+":"+transDatas[12];
         log.setLigntningTime(ligntningTime); //雷击时间

         //统一保留一位小数
         NumberFormat nbf= NumberFormat.getInstance();
         nbf.setMinimumFractionDigits(1);

         double peakDouble =(double)Long.parseLong(transDatas[13]+transDatas[14],16);
         if(peakDouble >32768){
             log.setPeakValue(nbf.format((peakDouble-65536)/1000*range) +"KA");//电流峰值
         }else{
             log.setPeakValue(nbf.format(peakDouble/1000*range) +"KA");//电流峰值
         }

         double effect=(double) Long.parseLong(transDatas[15]+transDatas[16],16);
         log.setEffectiveValue(nbf.format(effect/1000*range) +"KA"); //电流有效值

         double wave=(double)Long.parseLong(transDatas[17]+transDatas[18],16);
         log.setWaveHeadTime(nbf.format(wave/10)+"uS");//电流波头时间

         double half =(double)Long.parseLong(transDatas[19]+transDatas[20],16);
         log.setHalfPeakTime(nbf.format(half/10)+"uS"); //电流半峰值时间

         double actionTime =(double)Long.parseLong(transDatas[21]+transDatas[22],16);
         log.setActionTime(nbf.format(actionTime/10)+"uS");//电流作用时间

         double energy =(double)Long.parseLong(transDatas[23]+transDatas[24],16);
         log.setEnergy(nbf.format(energy/1000*range)+"KA.uS"); //能量

         //后两位作校验
         log.setStatus("N");
         int up=liteLogMapper.saveLightningLog(log);
         if(up>0) {
        	 logger.info("NB雷击记录成功");
         }else {
        	 logger.error("NB雷击错误参数："+currData+";解析长度"+transDatas.length);
         }
         }else {
        	 logger.error("NB雷击错误参数："+currData+";解析长度"+transDatas.length);
         }
	}
	
	/**
	 * 下发命令参数构建
	 * @param paramlst
	 * @return ObjectNode
	 * @throws Exception
	 */
	public ObjectNode paramList2JsonParam(List<Map<String,Object>> paramlst) throws Exception {
		if(paramlst.size()>0) {
			StringBuffer sb = new StringBuffer();
			sb.append("{\"");
			for(int i = 0; i < paramlst.size(); i++) {
				Map<String,Object> paramMap = paramlst.get(i);
				sb.append(paramMap.get("paramName"));
				sb.append("\":\"");
				sb.append(paramMap.get("paramValue"));
				sb.append("\"");
				if(i<paramlst.size()) {
					sb.append(",");
				}
			}
			sb.append("}");
			return JsonUtil.convertObject2ObjectNode(sb.toString());
		}
		return null;
	}


	@Override
	public String liangPost(Map vmap) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
