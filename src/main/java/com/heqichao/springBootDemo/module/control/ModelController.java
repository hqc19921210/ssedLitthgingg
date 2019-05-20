package com.heqichao.springBootDemo.module.control;

import com.alibaba.fastjson.JSONArray;
import com.heqichao.springBootDemo.base.control.BaseController;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.util.*;
import com.heqichao.springBootDemo.module.service.ModelAttrService;
import com.heqichao.springBootDemo.module.service.ModelService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * Created by heqichao on 2018-11-19.
 */
@RestController
@RequestMapping(value = "/service")
public class ModelController extends BaseController{

    @Autowired
    private ModelService modelService;

    @Autowired
    private ModelAttrService modelAttrService;


    private  Logger logger = LoggerFactory.getLogger(ModelController.class);

    @RequestMapping(value = "/queryModelType")
    ResponeResult queryTyps() {
        return new ResponeResult(modelService.queryType());
    }

  /*  @RequestMapping(value = "/deleteModelAttr")
    ResponeResult deleteModelAttr() {
        ResponeResult responeResult =new ResponeResult();
        Integer id = getIntegerParam("id");
        if(id!=null){
            modelAttrService.deleteByAttrId(id);
        }else{
            responeResult.setSuccess(false);
            responeResult.setMessage("删除数据异常，请稍后重试");
        }
        return responeResult;
    }*/

    @RequestMapping(value = "/saveOrUpdateModel")
    ResponeResult saveOrUpdateModel(){
        Map map = getParamMap();
        Integer modelId =getIntegerParam("id");
        String modelName = (String) map.get("modelName");
        String deleteIds = (String) map.get("deleteIds");
        List<Map> list =new ArrayList<>();
        String listJson =(String) map.get("list");
        List<Map> cmdlist =new ArrayList<>();
        String cmdlistJson =(String) map.get("cmdlist");
        try{
            list = JSONArray.parseArray(listJson,Map.class);
            cmdlist = JSONArray.parseArray(cmdlistJson,Map.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponeResult(modelService.saveOrUpdateModel(modelId,modelName,list,cmdlist,deleteIds));
    }

    @RequestMapping(value = "/queryAllByModelId")
    ResponeResult queryAllByModelId(){
        ResponeResult responeResult =new ResponeResult();
        Integer modelId = getIntegerParam("modelId");
        if(modelId!=null){
            responeResult.setResultObj(modelService.queryModelAndAttrsByModelId(modelId));
        }else{
            responeResult.setSuccess(false);
            responeResult.setMessage("获取数据异常，请稍后重试");
        }
        return responeResult;
    }
    
    @RequestMapping(value = "/queryCMDModelByDevlId")
    ResponeResult queryCMDModelByDevlId(){
    	ResponeResult responeResult =new ResponeResult();
    	Map map = getParamMap();
    	String devId = (String)map.get("devId");
		if(devId!=null){
    		responeResult.setResultObj(modelAttrService.queryCMDAttrByModelIdFMT(devId));
    	}else{
    		responeResult.setSuccess(false);
    		responeResult.setMessage("获取数据异常，请稍后重试");
    	}
    	return responeResult;
    }

    /**
     * 分页查询模板列表
     * @return
     */
    @RequestMapping(value = "/queryModelList")
    ResponeResult queryModelList(){
        Map map = getParamMap();
        return new ResponeResult(modelService.queryUserPageModel((String) map.get("model_name")));
    }

    @RequestMapping(value = "/deleteModel")
    ResponeResult deleteModel() {
        ResponeResult responeResult =new ResponeResult();
        Map map = getParamMap();
        Integer id = getIntegerParam("id");
        if(id!=null){
            modelService.deleteByModelId(id);
        }else{
            responeResult.setSuccess(false);
            responeResult.setMessage("删除模板数据异常，请稍后重试");
        }
        return responeResult;
    }

    /**
     * 查找当前用户所拥有的模板(用户设备选择模板)
     * @return
     */
    @RequestMapping(value = "/queryUserModel")
    ResponeResult queryUserModel(){
        return new ResponeResult(modelService.queryUserModel());
    }
    /**
     * 查找选中用户所拥有的模板
     * @return
     */
    @RequestMapping(value = "/queryUserModelById")
    ResponeResult queryUserModelById(){
    	return new ResponeResult(modelService.queryUserModelByUid());
    }
    
    /**
     * 查找当前用户所拥有的数据点
     * @return
     */
    @RequestMapping(value = "/queryUserAttr")
    ResponeResult queryUserAttr(){
    	return new ResponeResult(modelService.queryUserAttr());
    }
    
    /**
     * 根据数据点id获取数据点属性
     * @return
     */
    @RequestMapping(value = "/getUserAttrByAttrId")
    ResponeResult getUserAttrByAttrId(){
    	return new ResponeResult(modelService.getUserAttrByAttrId());
    }



    @RequestMapping(value = "/exportAllUserModel")
    void exportAllUserModel(){

       Map<String,List> map = modelService.queryExportInfo(null);
        export(map,"模板");

    }

    @RequestMapping(value = "/exportModel")
    void exportModel(){
        Integer integer =getIntegerParam("id");
        String modelName = (String) getParamMap().get("name");
        if(integer!=null){
            Map<String,List> map = modelService.queryExportInfo(integer);
            export(map,modelName);
        }


    }


    private void export(Map<String,List> map,String fileName){
        if(map!=null){

            FileOutputStream fos = null;
            File file =null;
            try{
                Iterator entries = map.entrySet().iterator();
                file = FileUtil.createTempDownloadFile(fileName+".xls");
                fos= new FileOutputStream(file);
                // 声明一个工作薄
                HSSFWorkbook workbook = ExcelWriter.createWorkBook();
                while (entries.hasNext()) {
                    Map.Entry entry = (Map.Entry) entries.next();
                    String key = (String) entry.getKey();
                    List<Map> attrList = (List<Map>) entry.getValue();
                    String modelName = key.split(ModelService.EXCEL_NAME_SPLIT)[1];
                    ExcelWriter.export(workbook,modelName,ModelService.title,attrList,ModelService.code);
                }
                workbook.write(fos);
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                if(fos!=null){
                    try {
                        fos.close();
                        download(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


        }
    }

    @RequestMapping(value = "/importModel")
    ResponeResult importModel(@RequestParam("file") MultipartFile multipartFile){
        File file =FileUtil.createTempDownloadFile( System.currentTimeMillis()+".xml");
        try {
            multipartFile.transferTo(file);

            Map map =ExcelReader.readFile(file);
            modelService.saveImport(map);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            FileUtil.deleteFile(file);
        }

        return new ResponeResult();
    }

}
