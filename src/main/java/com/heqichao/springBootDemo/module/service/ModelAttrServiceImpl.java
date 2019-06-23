package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.base.entity.Equipment;
import com.heqichao.springBootDemo.base.exception.ResponeException;
import com.heqichao.springBootDemo.base.service.EquipmentService;
import com.heqichao.springBootDemo.base.util.UserUtil;
import com.heqichao.springBootDemo.module.entity.ModelAttr;
import com.heqichao.springBootDemo.module.mapper.ModelAttrMapper;
import com.heqichao.springBootDemo.module.vo.ModelAttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-11-19.
 */
@Service
@Transactional
public class ModelAttrServiceImpl implements ModelAttrService {

    @Autowired
    private ModelAttrMapper modelAttrMapper;
    @Autowired
    private EquipmentService equipmentService;

    @Override
    public void saveModelAttr(List<ModelAttr> list) {
        if(list!=null && list.size()>0) {
            modelAttrMapper.saveModelAttr(list);
        }
    }

    @Override
    public void updateModelAttr(List<ModelAttr> list) {
        if(list!=null && list.size()>0){
            for(ModelAttr attr:list){
                modelAttrMapper.updateModelById(attr);
            }
        }
    }
    @Override
    public void saveCMDModelAttr(List<ModelAttr> list) {
    	if(list!=null && list.size()>0) {
    		modelAttrMapper.saveCMDModelAttr(list);
    	}
    }
    
    @Override
    public void updateCMDModelAttr(List<ModelAttr> list) {
    	if(list!=null && list.size()>0){
    		for(ModelAttr attr:list){
    			modelAttrMapper.updateCMDModelById(attr);
    		}
    	}
    }

    @Override
    public List<ModelAttr> queryByModelId(Integer modelId) {
        if(modelId != null){
            return modelAttrMapper.queryByModelId(modelId);
        }
        return new ArrayList<>();
    }
    
    //查询命令模板列表
    @Override
    public List<ModelAttrVo> queryCMDAttrByModelId(Integer modelId) {
    	return modelAttrMapper.queryCMDAttrByModelId(modelId);
    }
    
    //查询命令模板列表格式化
    @Override
    public Map<String,Object> queryCMDAttrByModelIdFMT(String equId) {
    	Map<String,Object> rs = new HashMap<String,Object>();
    	Equipment equ = equipmentService.getEquById(equId);
    	rs.put("info", equ);
    	rs.put("cmdlst", modelAttrMapper.queryCMDAttrByModelIdFMT(equ.getModelId()));
    	return rs;
    }
    
    // Muzzy
    @Override
    public List<ModelAttr> queryAttrByModelId(Integer modelId) {
    	return modelAttrMapper.queryAttrByModelId(modelId);
    }
    
    @Override
    public ModelAttr getUserAttrByAttrId(Integer modelId) {
    	return modelAttrMapper.getUserAttrByAttrId(modelId);
    }
    // End Muzzy 

    @Override
    public void deleteByModelId(Integer modelId) {
        if(!UserUtil.hasCRDPermission()){
            throw new ResponeException("没有该权限操作！");
        }
        modelAttrMapper.deleteByModelId(modelId);

    }

    @Override
    public void deleteByAttrId(List<Integer> ids) {
        if(!UserUtil.hasCRDPermission()){
            throw new ResponeException("没有该权限操作！");
        }
        if(ids!=null && ids.size()>0){
            modelAttrMapper.deleteById(ids);
        }

    }
}
