package com.heqichao.springBootDemo.base.control;

import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.ExcelReader;
import com.heqichao.springBootDemo.base.util.FileUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Muzzy Xu.
 * 
 */


@RestController
@RequestMapping(value = "/service")
public class EquipmentController extends BaseController{

    @Autowired
    private EquipmentService eService;

    
    @RequestMapping(value = "/getEquipments")
    public ResponeResult getUsers() {
    	return new ResponeResult(eService.queryEquipmentList());
    }
    @RequestMapping(value = "/getEquById")
    public ResponeResult getEquById() {
    	return new ResponeResult(eService.getEquById());
    }
    @RequestMapping(value = "/getEquPage")
    public ResponeResult queryEquipmentPage() {
    	return new ResponeResult(eService.queryEquipmentPage());
    }
    
    @RequestMapping(value = "/getEquAll")
    public List<String> getEquipmentIdListAll() {
    	return eService.getEquipmentIdListAll();
    }
    @RequestMapping(value = "/getEquEditInfo")
    public ResponeResult getEquEdit() {
    	return eService.getEquEditById();
    }
    
    @RequestMapping(value = "/addEqu" )
    @ResponseBody
    public ResponeResult addEqu(@RequestBody Map map) throws Exception {
        return eService.insertEqu(map);
    }
    @RequestMapping(value = "/editEqu" )
    @ResponseBody
    public ResponeResult editEqu(@RequestBody Map map) throws Exception {
    	return eService.editEqu(map);
    }
    
    @RequestMapping(value = "/delEqu" )
    @ResponseBody
    public ResponeResult delEqu(@RequestBody Map map) throws Exception {
    	return eService.deleteEquByID(map);
    }
    @RequestMapping(value = "/delEquAll" )
    @ResponseBody
    public ResponeResult delEquAll(@RequestBody Map map) throws Exception {
    	return eService.deleteEquAll(map);
    }
    
    // 设备下拉kv
    @RequestMapping(value = "/getEquSelectList" )
    public ResponeResult getCompanySeleList() throws Exception {
    	return eService.getEquSelectList();
    }
    // 根据应用id获取设备下拉kv
    @RequestMapping(value = "/getEquSelectListByAppId" )
    public ResponeResult getEquSelectListByAppId() throws Exception {
    	return eService.getEquSelectListByAppId();
    }
    /*
     * 
     * Excel导出
     */
    @RequestMapping(value = "/exprLora" )
    public void exprLora() throws Exception {
    	 eService.exportEquipments("Lora", "L", eService.titleLora, eService.codeLora);
    }
    
    @RequestMapping(value = "/exprNbiot" )
    public void exprNbiot() throws Exception {
    	eService.exportEquipments("Nbiot", "N", eService.titleNbiot, eService.codeNbiot);
    }
    
    
    @RequestMapping(value = "/exprGprs" )
    public void exprGprs() throws Exception {
    	eService.exportEquipments("GPRS", "G", eService.titleGPRS, eService.codeGPRS);
    }
    // 日志导出传参
    @RequestMapping(value = "/setDataLogParam" )
    @ResponseBody
    public ResponeResult setDataLogParam(@RequestBody Map map) throws Exception {
    	return eService.setDataLogParam(map);
    }
    
    @RequestMapping(value = "/exprDataLogByParam" )
    public void exprDataLogByParam() throws Exception {
    	eService.exportDataLogByParam();
    }
    
    // 数据点导出传参
    @RequestMapping(value = "/setDataDownloadParam" )
    @ResponseBody
    public ResponeResult setDataDownloadParam(@RequestBody Map map) throws Exception {
    	return eService.setDataDownloadParam(map);
    }
    
    @RequestMapping(value = "/exprDataDownloadByParam" )
    public void exprDataDownloadByParam() throws Exception {
    	eService.exprDataDownloadByParam();
    }
    
    /*
     * 
     * Excel导入
     */
    @RequestMapping(value = "/importLora")
    ResponeResult importLora(@RequestParam("file") MultipartFile multipartFile){
        File file =FileUtil.createTempDownloadFile( System.currentTimeMillis()+".xml");
        String res= null;
        try {
            multipartFile.transferTo(file);

            Map map =ExcelReader.readFile(file);
            res=eService.saveUploadImport(map, eService.codeLora, "L");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            FileUtil.deleteFile(file);
        }

        return new ResponeResult(res);
    }
    @RequestMapping(value = "/importNbiot")
    ResponeResult importNbiot(@RequestParam("file") MultipartFile multipartFile){
    	File file =FileUtil.createTempDownloadFile( System.currentTimeMillis()+".xml");
    	String res= null;
    	try {
    		multipartFile.transferTo(file);
    		Map map =ExcelReader.readFile(file);
    		res=eService.saveUploadImport(map, eService.codeNbiot, "N");
    	} catch (IOException e) {
    		e.printStackTrace();
    	}finally {
    		FileUtil.deleteFile(file);
    	}
    	
    	return new ResponeResult(res);
    }
    @RequestMapping(value = "/getUploadResult")
    public ResponeResult getUploadResult() {
    	return new ResponeResult(eService.getUploadResult());
    }

}
