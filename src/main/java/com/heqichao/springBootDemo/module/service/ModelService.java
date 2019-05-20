package com.heqichao.springBootDemo.module.service;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.module.entity.ModelAttr;

import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-11-19.
 */
public interface ModelService {

    String[] title = new String[]{"名称","数据类型","数值类型","小数位数","单位","公式","备注"};
    String[] code = new String[]{"attr_name","data_type","value_type","number_format", "unit","expression","memo"};


    Map queryModelAndAttrsByModelId(Integer modelId);
    void deleteByModelId(Integer modelId);

    String EXCEL_NAME_SPLIT="&_&_&";
    /**
     * 获取数据类型和数值类型数据
     * @return
     */
    Map queryType();

    /**
     * 分页查询模板列表
     * @param modelName 模糊查询：模板名称
     * @return
     */
    PageInfo queryUserPageModel(String modelName);

    /**
     * 查找当前用户所拥有的模板
     * @return
     */
//    List<Map> queryUserModel();
    Map<String, Integer> queryUserModel();

    /**
     * 导出模板
     * @param modelId ==null 所有
     * @return
     */
    Map<String,List> queryExportInfo(Integer modelId);

    void saveImport(Map map);
	Map<String, Integer> queryUserAttr();
	Map<String, Integer> queryUserModelByUid();
	Integer saveOrUpdateModel(Integer modelId, String modelName, List<Map> attrs, List<Map> cmdlst, String deleteIds);
	ModelAttr getUserAttrByAttrId();
}
