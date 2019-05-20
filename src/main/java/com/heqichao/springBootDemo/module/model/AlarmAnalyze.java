package com.heqichao.springBootDemo.module.model;

import com.heqichao.springBootDemo.module.entity.AlarmSetting;

/**
 * Created by heqichao on 2018-12-16.
 */
public interface AlarmAnalyze {
    /**
     * 判断是否触发报警
     * @param setting
     * @param value
     * @return
     */
    boolean execute(AlarmSetting setting, String value);
}
