package com.heqichao.springBootDemo.module.liteNA;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heqichao.springBootDemo.module.entity.LiteApplication;

/**
 * 电信云所有订阅功能
 * @author Muzzy Xu.
 * @date 2019/03/22
 *
 */
public class MyNbiotSubscription {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 订阅数据变化
	 * @param app
	 * @return
	 * @throws Exception
	 */
	public  String subLiteDataChg(LiteApplication app) throws Exception {
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

        HttpResponse httpResponse_deviceDataChanged = httpsUtil.doPostJson(app.getSubscribeNotifycation(), header_deviceDataChanged, jsonRequest_deviceDataChanged);

        String bodySubscribe_deviceDataChanged = httpsUtil.getHttpResponseBody(httpResponse_deviceDataChanged);

        logger.info("SubscribeNotification: " + httpResponse_deviceDataChanged.getStatusLine().toString());
        logger.info(bodySubscribe_deviceDataChanged);
        
        return httpResponse_deviceDataChanged.getStatusLine().toString();
	}
	
//	public static void main(String[] args) throws Exception {
//		LiteApplication app = new LiteApplication();
//		app.setPlatformIp("180.101.147.89:8743");
//		app.setAppId("zAfzULyvwTLvolphgWsrvgRfNo4a");
//		app.setSecret("C00396FFAF30B441B8980466FD8FC5571E180B05698D0EFA96BE422FEB8F897B");
//		app.setCallbackUrl("http://120.78.181.134:8082/service/nbiotCallback/dataChanged/zAfzULyvwTLvolphgWsrvgRfNo4a");
//		app.setSubscribeNotifycation("https://180.101.147.89:8743/iocm/app/sub/v1.1.0/subscribe");
//		MyNbiotSubscription m = new MyNbiotSubscription();
//		m.subLiteDataChg(app);
//	}
}
