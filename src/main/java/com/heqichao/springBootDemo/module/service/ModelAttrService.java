package com.heqichao.springBootDemo.module.service;

import com.heqichao.springBootDemo.module.entity.ModelAttr;

import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2018-11-19.
 */
public interface ModelAttrService {

    void saveModelAttr(List<ModelAttr> list);
    void updateModelAttr(List<ModelAttr> list);
    List<ModelAttr> queryByModelId(Integer modelId);
    void deleteByModelId(Integer modelId);


    void deleteByAttrId(List<Integer> ids);

	List<ModelAttr> queryAttrByModelId(Integer modelId);
	void saveCMDModelAttr(List<ModelAttr> list);
	void updateCMDModelAttr(List<ModelAttr> list);
	List<ModelAttr> queryCMDAttrByModelId(Integer modelId);
	Map<String, Object> queryCMDAttrByModelIdFMT(String equId);
	ModelAttr getUserAttrByAttrId(Integer modelId);

}
