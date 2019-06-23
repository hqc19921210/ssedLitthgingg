package com.heqichao.springBootDemo.module.liteNA;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.StringUtil;
import com.heqichao.springBootDemo.module.entity.LiteApplication;
import com.heqichao.springBootDemo.module.vo.AddDeviceVO;
import com.heqichao.springBootDemo.module.vo.CreateEquimentVO;

/**
 * 电信云所有订阅功能
 * @author Muzzy Xu.
 * @date 2019/03/22
 *
 */
public class MyNbiotSubscription {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 设备注册
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public  AddDeviceVO deviceCredentials(CreateEquimentVO params) throws Exception {
		if(StringUtil.isEmpty(params.getPlatformIp())||StringUtil.isEmpty(params.getAppId())) {
			return null;
		}
		String appId=params.getAppId();
		String createURL = Constant.DEVICE_CREDENTIALS.replace("SERVER:PORT", params.getPlatformIp()).replace("S_APPID", appId);
		
		// Two-Way Authentication
        HttpsUtil httpsUtil = new HttpsUtil();
        httpsUtil.initSSLConfigForTwoWay();
        MyNbiotAuths auth = new MyNbiotAuths();
        String accessToken = auth.login(httpsUtil,params);
        
        /*
         * params
         */
        Map<String, Object> deviceInfoDTO = new HashMap<>();
        deviceInfoDTO.put("deviceType", params.getDeviceType());
        deviceInfoDTO.put("model", params.getAppModel());
        deviceInfoDTO.put("manufacturerId", params.getManufacturerId());
        Map<String, Object> paramSubscribe_deviceDataChanged = new HashMap<>();
        paramSubscribe_deviceDataChanged.put("verifyCode", params.getVerifyCode());
        paramSubscribe_deviceDataChanged.put("nodeId", params.getVerifyCode());
        
        paramSubscribe_deviceDataChanged.put("deviceInfo", deviceInfoDTO);

        String jsonRequest_deviceDataChanged = JsonUtil.jsonObj2Sting(paramSubscribe_deviceDataChanged);

        /*
         * header
         */
        Map<String, String> header_deviceDataChanged = new HashMap<>();
        header_deviceDataChanged.put(Constant.HEADER_APP_KEY, appId);
        header_deviceDataChanged.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);

        HttpResponse httpResponse_deviceDataChanged = httpsUtil.doPostJson(createURL, header_deviceDataChanged, jsonRequest_deviceDataChanged);

        String bodySubscribe_deviceDataChanged = httpsUtil.getHttpResponseBody(httpResponse_deviceDataChanged);
        
        String resp = httpResponse_deviceDataChanged.getStatusLine().toString();
        logger.info("DeviceCredentials: " + resp);
        logger.info(bodySubscribe_deviceDataChanged);
        String [] arr = resp.split("\\s+");
        AddDeviceVO res = JSONObject.parseObject(bodySubscribe_deviceDataChanged, AddDeviceVO.class);
        res.setStatus(arr[1]);
        res.setErrorContext(bodySubscribe_deviceDataChanged);
        return res;
	}
	/**
	 * 设备信息修改
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public  void deviceModify(CreateEquimentVO params) throws Exception {
		String appId=params.getAppId();
		String createURL = Constant.DEVICE_MODIFY.replace("SERVER:PORT", params.getPlatformIp()).replace("S_APPID", appId).replace("S_DEVICEID", params.getDevId());
		
		// Two-Way Authentication
		HttpsUtil httpsUtil = new HttpsUtil();
		httpsUtil.initSSLConfigForTwoWay();
		MyNbiotAuths auth = new MyNbiotAuths();
		String accessToken = auth.login(httpsUtil,params);
		
		/*
		 * params
		 */
		Map<String, Object> paramSubscribe_deviceDataChanged = new HashMap<>();
		paramSubscribe_deviceDataChanged.put("name", params.getDevName());
		
		
		String jsonRequest_deviceDataChanged = JsonUtil.jsonObj2Sting(paramSubscribe_deviceDataChanged);
		
		/*
		 * header
		 */
		Map<String, String> header_deviceDataChanged = new HashMap<>();
		header_deviceDataChanged.put(Constant.HEADER_APP_KEY, appId);
		header_deviceDataChanged.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
		
		HttpResponse httpResponse_deviceDataChanged = httpsUtil.doPutJson(createURL, header_deviceDataChanged, jsonRequest_deviceDataChanged);
		
		String bodySubscribe_deviceDataChanged = httpsUtil.getHttpResponseBody(httpResponse_deviceDataChanged);
		
		String resp = httpResponse_deviceDataChanged.getStatusLine().toString();
		logger.info("DeviceModify: " + resp);
		logger.info(bodySubscribe_deviceDataChanged);
	}
	/**
	 * 订阅数据变化
	 * @param app
	 * @return
	 * @throws Exception
	 */
	public  String subLiteDataChg(LiteApplication app) throws Exception {
		if(StringUtil.isEmpty(app.getPlatformIp())) {
			return "无法获取应用ID，订阅失败";
		}
		String subURL = Constant.SUBSCRIBE_NOTIFYCATION.replace("SERVER:PORT", app.getPlatformIp());
		// Two-Way Authentication
		HttpsUtil httpsUtil = new HttpsUtil();
		httpsUtil.initSSLConfigForTwoWay();
		String appId=app.getAppId();
		MyNbiotAuths auth = new MyNbiotAuths();
		String accessToken = auth.login(httpsUtil,app);
		
		/*
		 * subscribe deviceDataChanged notification
		 */
		Map<String, Object> paramSubscribe_deviceDataChanged = new HashMap<>();
		paramSubscribe_deviceDataChanged.put("notifyType", Constant.DEVICE_DATA_CHANGED);
		paramSubscribe_deviceDataChanged.put("callbackurl", app.getCallbackUrl());
		
		String jsonRequest_deviceDataChanged = JsonUtil.jsonObj2Sting(paramSubscribe_deviceDataChanged);
		
		Map<String, String> header_deviceDataChanged = new HashMap<>();
		header_deviceDataChanged.put(Constant.HEADER_APP_KEY, appId);
		header_deviceDataChanged.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
		
		HttpResponse httpResponse_deviceDataChanged = httpsUtil.doPostJson(subURL, header_deviceDataChanged, jsonRequest_deviceDataChanged);
		
		String bodySubscribe_deviceDataChanged = httpsUtil.getHttpResponseBody(httpResponse_deviceDataChanged);
		
		logger.info("SubscribeNotification: " + httpResponse_deviceDataChanged.getStatusLine().toString());
		logger.info(bodySubscribe_deviceDataChanged);
		
		return httpResponse_deviceDataChanged.getStatusLine().toString();
	}
	
	public static void main(String[] args) throws Exception {
		CreateEquimentVO app = new CreateEquimentVO();
		app.setPlatformIp("180.101.147.89:8743");
		app.setAppId("zAfzULyvwTLvolphgWsrvgRfNo4a");
		app.setSecret("C00396FFAF30B441B8980466FD8FC5571E180B05698D0EFA96BE422FEB8F897B");
		app.setDevId("a3702fd5-c579-4c90-830f-59955fb58806");
		app.setDevName("233");
		app.setCallbackUrl("http://120.78.181.134:8082/service/nbiotCallback/dataChanged/zAfzULyvwTLvolphgWsrvgRfNo4a");
		app.setSubscribeNotifycation("https://180.101.147.89:8743/iocm/app/sub/v1.1.0/subscribe");
		MyNbiotSubscription m = new MyNbiotSubscription();
		m.deviceModify(app);
	}
}
