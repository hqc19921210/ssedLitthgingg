package com.heqichao.springBootDemo.module.control;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.heqichao.springBootDemo.base.control.BaseController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.vo.EquipmentVO;
import com.heqichao.springBootDemo.module.service.DataLogService;
import com.heqichao.springBootDemo.module.service.LiteAppService;

@RestController
@RequestMapping(value = "/service")
public class LiteAppController extends BaseController{

	@Autowired
	private LiteAppService liteAppService;
	
	@Autowired
	private DataLogService dataLogService;
	
	//旧版本 数据变化回调
	@RequestMapping(value = "/nbiotCallback/{callurl:^[A-Za-z0-9]+$}")
	ResponeResult liteNaCallback(@RequestBody Object map) throws Exception {
		Map<String, String> req = liteAppService.liteNaDataChangedCallback(map.toString());
		dataLogService.saveDataLog(req.get("devid"), req.get("data"), req.get("srcdata"), EquipmentService.EQUIPMENT_NB);
		return new ResponeResult();
	}
	
	//数据变化回调
	@RequestMapping(value = "/nbiotCallback/dataChanged/{callurl:^[A-Za-z0-9]+$}")
	ResponeResult liteNaDataChangedCallback(@RequestBody Object map) throws Exception {
		Map<String, String> req = liteAppService.liteNaDataChangedCallback(map.toString());
		dataLogService.saveDataLog(req.get("devid"), req.get("data"), req.get("srcdata"), EquipmentService.EQUIPMENT_NB);
		return new ResponeResult();
	}
	
	//下发命令通知回调
	@RequestMapping(value = "/nbiotCallback/command/{callurl:^[A-Za-z0-9]+$}")
	ResponeResult liteNaCommandCallback(@RequestBody Object map) throws Exception {
		liteAppService.liteNaCommandCallback(map.toString());
		return new ResponeResult();
	}
	
	@RequestMapping(value = "/queryLiteApps")
	ResponeResult queryLiteApps() throws Exception {
		return new ResponeResult(liteAppService.queryLiteApps());
	}
	
	@RequestMapping(value = "/addLiteApp")
	ResponeResult addLiteApp() throws Exception {
		return liteAppService.addLiteApp();
	}
	
	@RequestMapping(value = "/updLiteApp")
	ResponeResult updLiteApp() throws Exception {
		return liteAppService.updLiteApp();
	}
	
	@RequestMapping(value = "/resetSecret")
	ResponeResult resetSecret() throws Exception {
		return liteAppService.resetSecret();
	}

	@RequestMapping(value = "/deleteLiteApp")
	ResponeResult deleteLiteApp() throws Exception {
		Integer id =getIntegerParam("eid");
		return liteAppService.deleteAppByID(id);
	}
	
	@RequestMapping(value = "/subLiteDataChg")
	ResponeResult subLiteDataChg() throws Exception {
		return liteAppService.subLiteDataChg();
	}

	@RequestMapping(value = "/getAppSeleList" )
    public ResponeResult getAppSeleList() throws Exception {
    	return liteAppService.getAppSelectList();
    }
	//测试命令下发
	@RequestMapping(value = "/postCommand")
	ResponeResult postCommand() throws Exception {
		 Map map = getParamMap();
        Integer aid =getIntegerParam("aid");
        Map params = (Map) map.get("params");
        System.out.println(map.toString());
		return new ResponeResult(liteAppService.postCommand(aid,params));
	}
	
	//下发命令
	@RequestMapping(value = "/postCommandList")
	ResponeResult postCommandList() throws Exception {
		Map map = getParamMap();
		EquipmentVO info = JSONObject.parseObject(JSONObject.toJSONString(map.get("info")), EquipmentVO.class);
        List<Boolean> selectlist =new ArrayList<>();
        Object listJson = map.get("selectlist");
        List<Map> cmdlist =new ArrayList<>();
        Object cmdlistJson = map.get("cmdlist");
        try{
        	selectlist = JSONArray.parseArray(listJson.toString(),boolean.class);
            cmdlist = JSONArray.parseArray(cmdlistJson.toString(),Map.class);
        }catch (Exception e){
            e.printStackTrace();
        }
		return liteAppService.postCommandList(info, selectlist, cmdlist);
	}
	
	@RequestMapping(value = "/getCommandLogByDevId")
	ResponeResult getCommandLogByDevId() throws Exception {
		return new ResponeResult(liteAppService.getCommandLogByDevId());
	}
}

