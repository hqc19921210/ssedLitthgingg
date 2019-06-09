package com.heqichao.springBootDemo.base.entity;

import org.springframework.stereotype.Component;

/**
 * Created by heqichao on 2019-6-9.
 */

@Component("send_request_log")
public class SendRequestLogEntity extends BaseEntity {
    private static final long serialVersionUID = -909564044337592088L;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求参数
     */
    private String param;

    /**
     * 响应结果
     */
    private String respone;

    /**
     * 其他备注
     */
    private String memo;

    /**
     * 状态 请求成功失败
     */
    private String status;

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    public String getRespone() {
        return respone;
    }

    public void setRespone(String respone) {
        this.respone = respone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
