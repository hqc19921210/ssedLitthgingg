package com.heqichao.springBootDemo.base.service;

import com.heqichao.springBootDemo.base.entity.SendRequestLogEntity;
import com.heqichao.springBootDemo.base.mapper.SendRequestLogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by heqichao on 2019-6-9.
 */
@Service
public class SendRequestLogServiceImpl implements SendRequestLogService {
    @Autowired
    private SendRequestLogMapper sendRequestLogMapper;

    @Override
    public void saveLog(SendRequestLogEntity logEntity) {
        sendRequestLogMapper.save(logEntity);
    }
}
