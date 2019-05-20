package com.heqichao.springBootDemo.module.control;

import java.util.Map;

import com.heqichao.springBootDemo.base.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.module.service.DataLogService;
import com.heqichao.springBootDemo.module.service.LiteNAService;

@RestController
@RequestMapping(value = "/service")
public class LiteNAController {

	@Autowired
	private LiteNAService liteNAService;
	@Autowired
	private DataLogService dataLogService;
	
	@RequestMapping(value = "/getDataChange")
    ResponeResult getDataChange() throws Exception {
        return new ResponeResult(liteNAService.getDataChange());
    }
	@RequestMapping(value = "/liangPost")
	ResponeResult liangPost(@RequestBody Map map) throws Exception {
		System.out.println(map.toString());
		return new ResponeResult(liteNAService.liangPost(map));
	}
	@RequestMapping(value = "/liteNaCallback2")
	ResponeResult liteNaCallback2() throws Exception {
		liteNAService.chg();
		return new ResponeResult();
	}
	
//	@RequestMapping(value = "/nbiotCallback/{callurl:^[A-Za-z0-9]+$}")
//	ResponeResult liteNaCallback(@RequestBody Object map) throws Exception {
////		liteNAService.chg(map);
//		Map<String, String> req = liteNAService.NBReturnFmt(map);
//		dataLogService.saveDataLog(req.get("devid"), req.get("data"), req.get("srcdata"), EquipmentService.EQUIPMENT_NB);
//		return new ResponeResult();
//	}

	@RequestMapping(value = "/queryLiteAll")
	ResponeResult queryAll() throws Exception {
		return new ResponeResult(liteNAService.queryAll());
	}
	
	@RequestMapping(value = "/queryLites")
	ResponeResult queryLites() throws Exception {
		return new ResponeResult(liteNAService.queryLites());
	}

	@RequestMapping(value = "/deleteLiteAll")
	ResponeResult deleteAll() throws Exception {
		liteNAService.deleteAll();
		return new ResponeResult();
	}
	
	@RequestMapping(value = "/queryNBLightLog")
    ResponeResult queryLightLogs() {
        return new ResponeResult(liteNAService.queryNBLightLog());
    }
}
