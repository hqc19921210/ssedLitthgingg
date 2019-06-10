package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.param.ApplicationContextUtil;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.service.UserService;
import com.heqichao.springBootDemo.base.util.*;
import com.heqichao.springBootDemo.module.entity.Model;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.liteNA.LiteNAStringUtil;
import com.heqichao.springBootDemo.module.mapper.ModelMapper;
import com.heqichao.springBootDemo.module.model.AttrEnum;
import com.heqichao.springBootDemo.module.model.ModelUtil;
import com.heqichao.springBootDemo.module.vo.ModelAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by heqichao on 2018-11-19.
 */
@Service
@Transactional
public class ModelServiceImpl implements ModelService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ModelAttrService modelAttrService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private AlarmSettingService alarmSettingService;

    @Override
    public Integer saveOrUpdateModel(Integer modelId,String modelName,List<Map> attrs,List<Map> cmdlst,String deleteIds) {
        Date date = new Date();
        if(!UserUtil.hasCRDPermission() ){
            throw new ResponeException("没有该权限操作！");
        }
        if(StringUtil.isEmpty(modelName)){
            throw new ResponeException("请输入模板名称！");
        }
        Integer count = modelMapper.queryCountByModelName(modelId,modelName);
        if(count>0){
            throw new ResponeException("模板名称 :"+modelName+" 已存在！");
        }

        Integer userId =ServletUtil.getSessionUser().getId();
        //保存
        Model model =new Model(modelName,userId,date);
        if(modelId==null){
             modelMapper.saveModel(model);
             modelId=model.getId();
        }else{
            //已存在更新名称
            modelMapper.updateModelName(modelId,modelName,userId,date);
        }
        if(attrs!=null && attrs.size()>0){
            checkHeartBeatProxy(attrs);
            List<ModelAttr> saveList =new ArrayList<>();
            List<ModelAttr> updateList =new ArrayList<>();
            for(int i=0;i< attrs.size();i++){
                Map map=attrs.get(i);
                ModelAttr modelAttr =new ModelAttr();
                BeanUtil.copyProperties(modelAttr,map);
                if("ENUMERATION_TYPE".equals(modelAttr.getDataType()) ) {
                	if(modelAttr.getExpression() == null) {
                		throw new ResponeException("枚举型枚举公式为空");
                	}else {
                		String exp = modelAttr.getExpression();
                		exp=exp.replaceAll(" ", "");// 去除所有空格
                		exp=LiteNAStringUtil.trimFirstAndLastChar(exp, ",");// 去除前后,
                		modelAttr.setExpression(exp);
                	}
                }
                modelAttr.setOrderNo(i+1);
                modelAttr.setModelId(modelId);
                modelAttr.setAddDate(date);
                modelAttr.setAddUid(userId);
                modelAttr.setUdpDate(date);
                modelAttr.setUdpUid(userId);
                if(modelAttr.getId()==null){
                    saveList.add(modelAttr);
                }else{
                    updateList.add(modelAttr);
                }
            }
            modelAttrService.saveModelAttr(saveList);
            modelAttrService.updateModelAttr(updateList);

        }
        if(cmdlst!=null && cmdlst.size()>0){
        	List<ModelAttr> saveCMDList =new ArrayList<>();
        	List<ModelAttr> updateCMDList =new ArrayList<>();
        	for(int i=0;i< cmdlst.size();i++){
        		Map cmdMap=cmdlst.get(i);
        		ModelAttr modelCMDAttr =new ModelAttr();
        		BeanUtil.copyProperties(modelCMDAttr,cmdMap);
        		if(AttrEnum.ENUMERATION_TYPE.getType().equals(modelCMDAttr.getDataType()) ) {
                	if(modelCMDAttr.getExpression() == null) {
                		throw new ResponeException("下发枚举型枚举公式为空");
                	}else {
                		String exp = modelCMDAttr.getExpression();
                		exp=exp.replaceAll(" ", "");// 去除所有空格
                		exp=LiteNAStringUtil.trimFirstAndLastChar(exp, ",");// 去除前后,
                		modelCMDAttr.setExpression(exp);
                	}
                }
        		modelCMDAttr.setOrderNo(i+1);
        		modelCMDAttr.setModelId(modelId);
        		modelCMDAttr.setAddDate(date);
        		modelCMDAttr.setAddUid(userId);
        		modelCMDAttr.setUdpDate(date);
        		modelCMDAttr.setUdpUid(userId);
        		if(modelCMDAttr.getId()==null){
        			saveCMDList.add(modelCMDAttr);
        		}else{
        			updateCMDList.add(modelCMDAttr);
        		}
        	}
        	modelAttrService.saveCMDModelAttr(saveCMDList);
        	modelAttrService.updateCMDModelAttr(updateCMDList);
        	
        }
        if(StringUtil.isNotEmpty(deleteIds)){
            String[] ids =deleteIds.split(",");
            List<Integer> intIds =new ArrayList<>();
            try{
                if(ids!=null && ids.length>0){
                    for(String id :ids){
                        Integer i =Integer.parseInt(id);
                        intIds.add(i);
                    }
                }
                modelAttrService.deleteByAttrId(intIds);
            }catch (Exception e){}

        }
        return modelId;
    }

    /**
     * 检查是否为心跳协议
     */
    private void checkHeartBeatProxy(List<Map> attrs){
        String mess ="当前模板格式与心跳协议冲突！请重新设置";
        /*
        if(attrs!=null && attrs.size()==1){
            String valueType = (String) attrs.get(0).get("valueType");
            String dataType = (String) attrs.get(0).get("dataType");
            if(AttrEnum.ALARM_TYPE.getType().equals(dataType) || AttrEnum.SWITCH_TYPE.getType().equals(dataType) || AttrEnum.ENUMERATION_TYPE.getType().equals(dataType)){
                throw new ResponeException(mess);
            }else if(AttrEnum.INT_TYPE__TWO_SIGNED.getType().equals(dataType)){
                if(AttrEnum.INT_TYPE__TWO_SIGNED.getSubType().equals(valueType) || AttrEnum.INT_TYPE__TWO_UNSIGNED.getSubType().equals(valueType)){
                    throw new ResponeException(mess);
                }
            }
        }
        */
        final int[] dataByteLen = {0};

        if(CollectionUtil.isNotEmpty(attrs)){
            attrs.stream().forEach(item ->{
                String valueType = (String) item.get("valueType");
                String dataType = (String) item.get("dataType");
                AttrEnum attrEnum =AttrEnum.getAttrByType(dataType,valueType);
                if(attrEnum!=null){
                    dataByteLen[0] = dataByteLen[0] + attrEnum.getLength();
                    if(attrEnum.equals(AttrEnum.WAVE_TYPE)){
                        //波形数据的数据区长度为两字节 要额外加1
                        dataByteLen[0] = dataByteLen[0] +1;
                    }
                }
            });
            if(ModelUtil.isHeartData(dataByteLen[0] + ModelUtil.DATA_FRONT_BYTE_LENGTH + ModelUtil.DATA_BACK_BYTE_LENGTH )){
                throw new ResponeException(mess);
            }
        }

    }

    @Override
    public Map queryModelAndAttrsByModelId(Integer modelId) {
        Map map =new HashMap();
        if(modelId!=null){
            List<Model> models =modelMapper.queryByModelId(modelId);
            List<ModelAttr> attrs =modelAttrService.queryByModelId(modelId);
            List<ModelAttrVo> cmds =modelAttrService.queryCMDAttrByModelId(modelId);
            if(models !=null && models.size()>0){
                map.put("model",models.get(0));
            }else{
                map.put("model",new HashMap<>());
            }
            if(attrs!=null && attrs.size()>0){
                map.put("attrs",attrs);
            }else{
                map.put("attrs",new ArrayList<>());
            }
            if(attrs!=null && cmds.size()>0){
            	map.put("cmds",cmds);
            }else{
            	map.put("cmds",new ArrayList<>());
            }
        }
        return map;
    }

    @Override
    public void deleteByModelId(Integer modelId) {
        if(!UserUtil.hasCRDPermission()){
            throw new ResponeException("没有该权限操作！");
        }
        if(modelId!=null){
            modelMapper.deleteByModelId(modelId);
            //删除所有的属性
            modelAttrService.deleteByModelId(modelId);
            //删除报警设置
            alarmSettingService.deleteByModelId(modelId);
        }
    }

    @Override
    public Map queryType() {
        Map map =new HashMap();
        map.put("TYPE",ModelUtil.getTypeNames());
        map.put("SUBTYPE",ModelUtil.getIntSubTypeNames());
        return map;
    }

    @Override
    public PageInfo queryUserPageModel(String modelName) {
        List<Model> list =new ArrayList<>();
        List<Map> returnList =new ArrayList<>();
        Integer cmp =ServletUtil.getSessionUser().getCompetence();
        Integer userId =ServletUtil.getSessionUser().getId();
        PageInfo pageInfo =null;
        if(cmp !=null ) {
            //管理员查询所有
            if (UserService.ROOT.equals(cmp)) {
                PageUtil.setPage();
                pageInfo=new PageInfo(modelMapper.queryAll(modelName));
            }else if (UserService.CUSTOMER.equals(cmp)){
                List<Integer> userList =new ArrayList<>();
                userList.add(userId);
                PageUtil.setPage();
                pageInfo=new PageInfo(modelMapper.queryByUserIds(userList,modelName));
            }else if(UserService.VISTOR.equals(cmp)){
                Integer parentId =equipmentService.getUserParent(userId);
                List<Integer> userList =new ArrayList<>();
                userList.add(userId);
                userList.add(parentId);
                PageUtil.setPage();
                pageInfo=new PageInfo(modelMapper.queryByUserIds(userList,modelName));
            }
        }
        list=pageInfo.getList();
        if(list!=null && list.size()>0){
            //补充人员名字
            for(Model model :list){
                Map map =new HashMap();
                BeanUtil.copyProperties(map,model);
                map.put("addName",UserCache.getUserName(model.getAddUid()));
                map.put("udpName",UserCache.getUserName(model.getUdpUid()));
                returnList.add(map);
            }
        }
        pageInfo.setList(returnList);
        return pageInfo;
    }

    @Override
    public Map<String, Integer> queryUserModel() {
        List<Model> list =new ArrayList<>();
        Integer cmp =ServletUtil.getSessionUser().getCompetence();
        Integer userId =ServletUtil.getSessionUser().getId();
        if(cmp !=null ) {
            //管理员查询所有
            if (UserService.ROOT.equals(cmp)) {
                PageUtil.setPage();
                list=modelMapper.queryAll("");
            }else if(UserService.CUSTOMER.equals(cmp)){
                List<Integer> userList =new ArrayList<>();
                userList.add(userId);
                PageUtil.setPage();
                list= modelMapper.queryByUserIds(userList,"");
            }else if(UserService.VISTOR.equals(cmp)){
                Integer parentId =equipmentService.getUserParent(userId);
                List<Integer> userList =new ArrayList<>();
                userList.add(userId);
                userList.add(parentId);
                PageUtil.setPage();
                list= modelMapper.queryByUserIds(userList,"");
            }
        }
        // Edit By Muzzy
//        List<Map> returnList =new ArrayList<>();
//        if(list!=null && list.size()>0){
//            for(Model model : list){
//                Map map =new HashMap();
//                map.put(model.getId(),model.getModelName());
//                returnList.add(map);
//            }
//        }
//        return returnList;
        Map<String, Integer> res = new HashMap<>();
        if(list!=null && list.size()>0){
        	res =  list.stream().collect(
				Collectors.toMap(Model::getModelName,Model::getId, (k1,k2)->k1)
			);
        }
        return res;
        // End Muzzy
    }
    //  Muzzy
    @Override
    public Map<String, Integer> queryUserModelByUid() {
    	Map map = RequestContext.getContext().getParamMap();
    	String uid = StringUtil.getStringByMap(map,"uid");
    	List<Model> list =modelMapper.queryByUserId(uid);
    	Map<String, Integer> res = new HashMap<>();
    	if(list!=null && list.size()>0){
    		res =  list.stream().collect(
    				Collectors.toMap(Model::getModelName,Model::getId, (k1,k2)->k1)
    				);
    	}
    	return res;
    }
    // End Muzzy
    
    // Muzzy
    @Override
    public ModelAttr getUserAttrByAttrId() {
    	Map map = RequestContext.getContext().getParamMap();
    	Integer mid = StringUtil.getIntegerByMap(map,"aid");
    	ModelAttr res = modelAttrService.getUserAttrByAttrId(mid);
    	return res;
    }
    
    @Override
    public Map<String, Integer> queryUserAttr() {
    	Map map = RequestContext.getContext().getParamMap();
    	Integer mid = StringUtil.getIntegerByMap(map,"mid");
    	List<ModelAttr> list =new ArrayList<>();
    	list=modelAttrService.queryAttrByModelId(mid);
//    	Map<String, Object> optionEntity = new HashMap<>();
    	Map<String, Integer> optionTree = new HashMap<>();
    	//对应对象
//    	if(list!=null && list.size()>0){
//    		optionEntity =  list.stream().collect(
//    				Collectors.toMap(ModelAttr::getStringModelId,Function.identity(), (k1,k2)->k1)//Function.identity() 是实体本身，即ModelAttr
//    				);
//    	}
    	//下拉树
    	if(list!=null && list.size()>0){
    		optionTree =  list.stream().collect(
    				Collectors.toMap(ModelAttr::getAttrName,ModelAttr::getId, (k1,k2)->k1)
    				);
    	}
    	return optionTree;
    }
    
    
    // End Muzzy

    @Override
    public Map<String,List> queryExportInfo(Integer model) {
       Map<String,List> map =null;
        Integer cmp =ServletUtil.getSessionUser().getCompetence();
        Integer userId =ServletUtil.getSessionUser().getId();
        List<Integer> userList =new ArrayList<>();
        if(cmp !=null ) {
            //管理员查询所有
            if (UserService.ROOT.equals(cmp)) {
                userList=null;
            }else if(UserService.CUSTOMER.equals(cmp)){
                userList.add(userId);
            }else if(UserService.VISTOR.equals(cmp)){
                Integer parentId =equipmentService.getUserParent(userId);
                userList.add(userId);
                userList.add(parentId);
            }
           List<Map> mapList = modelMapper.queryExportInfo(userList,model);
            if(mapList!=null && mapList.size()>0){
                map = new HashMap<String,List>();
                for(Map m :mapList){
                    //翻译
                    m.put("data_type",ModelUtil.getNameByType((String) m.get("data_type")));
                    m.put("value_type",ModelUtil.getSubeNameByTyp((String) m.get("value_type")));
                    String modelId = (String) m.get("model_id");
                    String modelName = (String) m.get("model_name");
                    if(map.get(modelId+EXCEL_NAME_SPLIT+modelName) == null){
                        map.put(modelId+EXCEL_NAME_SPLIT+modelName,new ArrayList<Map>());
                    }
                    map.get(modelId+EXCEL_NAME_SPLIT+modelName).add(m);
                }
            }
        }
        return map;
    }

    @Override
    public void saveImport(Map map) {
        if(!UserUtil.hasCRDPermission() ){
            throw new ResponeException("没有该权限操作！");
        }
        if(map==null || map.size()<1){
            return;
        }
        Iterator entries = map.entrySet().iterator();
        ModelService modelService = (ModelService) ApplicationContextUtil.getApplicationContext().getBean("modelServiceImpl");
        while (entries.hasNext()) {
            Map.Entry entry = (Map.Entry) entries.next();
            String modelName = (String) entry.getKey();
            List<Map> attrList =new ArrayList<>();
            if(entry.getValue() instanceof ArrayList){
                List attrs= (List) entry.getValue();
                if(attrs!=null && attrs.size()>1){
                    //去掉第一行标题
                    for(int i=1;i<attrs.size();i++){
                        List<String> row = (List<String>) attrs.get(i);
                        Map rowMap =  CollectionUtil.listStringTranToMap(row,code,true);
                        attrList.add(rowMap);
                    }
                }
                checkAttr(modelName,attrList);
                modelService.saveOrUpdateModel(null,modelName,attrList,null,null);
            }
        }
    }



    private void checkAttr(String modelName,List<Map> attrList){
        Map nameMap =new HashMap();
        if(modelName.length()>20){
            throw new ResponeException("模板名称长度不允许超过20："+modelName);
        }

        if(attrList!=null && attrList.size()>0){
            for(int i=0;i<attrList.size();i++){
                Map map =attrList.get(i);
                String name = (String) map.get("attrName");
                if(StringUtil.isEmpty(name)){
                    throw new ResponeException("属性名称不允许为空");
                }
                if(nameMap.get(name)!=null){
                    throw new ResponeException("属性名称不允许重复："+name);
                }
                nameMap.put(name,new Object());
                if(name.length()> 20){
                    throw new ResponeException("属性名称长度不允许超过20："+modelName);
                }

                String dataTypeName = (String) map.get("dataType");
                if(StringUtil.isEmpty(dataTypeName)){
                    throw new ResponeException("数据类型不允许为空");
                }
                String dataType =ModelUtil.getTypeByName(dataTypeName);
                if(StringUtil.isEmpty(dataType)){
                    throw new ResponeException("无效的数据类型："+dataTypeName);
                }else{
                    map.put("dataType",dataType);
                }

                if(AttrEnum.WAVE_TYPE.getType().equals(dataType) && i != attrList.size()-1){
                    throw new ResponeException("波形只能设置在最后！"+dataTypeName);
                }

                String valueTypeName = (String) map.get("valueType");
                if(StringUtil.isNotEmpty(valueTypeName)){
                    String valueType =ModelUtil.getSubTypeByName(valueTypeName);
                    map.put("valueType",valueType);
                }

                String numberFormat = (String) map.get("numberFormat");
                if(StringUtil.isNotEmpty(numberFormat)){
                    try{
                        int num = Integer.parseInt(numberFormat);
                        if(num>9 || num <0){
                            throw new ResponeException("小数位数在0到9之间");
                        }
                        map.put("numberFormat",num);
                    }catch (Exception e){
                        throw new ResponeException("无效的小数位数："+numberFormat);
                    }
                }

                String unit = (String) map.get("unit");
                if(StringUtil.isNotEmpty(unit)){
                    if(unit.length()> 20){
                        throw new ResponeException("单位长度不允许超过20："+unit);
                    }
                }

                String expression = (String) map.get("expression");
                if(StringUtil.isNotEmpty(expression)){
                    if(unit.length()> 20){
                        throw new ResponeException("公式长度不允许超过20："+expression);
                    }
                }

                String memo = (String) map.get("memo");
                if(StringUtil.isNotEmpty(expression)){
                    if(unit.length()> 20){
                        throw new ResponeException("备注长度不允许超过20："+memo);
                    }
                }
            }
        }

    }

}
