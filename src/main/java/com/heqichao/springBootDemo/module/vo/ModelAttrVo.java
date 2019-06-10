package com.heqichao.springBootDemo.module.vo;

import com.heqichao.springBootDemo.module.entity.ModelAttr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by heqichao on 2019-5-26.
 */
public class ModelAttrVo extends ModelAttr {
    //数值类型名称
    private String valueName;
    //数据类型名称
    private String dataName;
    private Map<String,Integer> eumSelect;

    public List<Map<String, Object>> getEumSelect() {
        if(getExpression() == null) {
            return new ArrayList<Map<String, Object>>();
        }
        List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
        String[] strLst = getExpression().split(",");
        for (int i = 0; i < strLst.length; i++) {
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("key", strLst[i]);
            map.put("value", i);
            res.add(map);
        }
        return res;
        // 解决多线程问题的ListToMap方法
//		List<String> strLst = Arrays.asList(this.expression.split(","));
//		Map<String, Integer> res = IntStream.range(0, strLst.size())
//                .boxed()
//                .collect(Collectors.toMap(strLst::get, Function.identity()));
    }

    public void setEumSelect(Map<String, Integer> eumSelect) {
        this.eumSelect = eumSelect;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    public String getDataName() {
        return dataName;
    }

    public void setDataName(String dataName) {
        this.dataName = dataName;
    }
}
