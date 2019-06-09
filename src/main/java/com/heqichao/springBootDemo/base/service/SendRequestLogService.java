package com.heqichao.springBootDemo.base.service;

import com.heqichao.springBootDemo.base.entity.SendRequestLogEntity;

/**
 * Created by heqichao on 2019-6-9.
 */
public interface SendRequestLogService {
    /**
     * 请求响应失败
     */
    String RESPONE_FAILURE ="F";

    /**
     * 请求响应成功
     */
    String RESPONE_SUCCESS ="S";

    /**
     * 保持http发送请求
     * @param logEntity
     */
    void saveLog(SendRequestLogEntity logEntity);
}
