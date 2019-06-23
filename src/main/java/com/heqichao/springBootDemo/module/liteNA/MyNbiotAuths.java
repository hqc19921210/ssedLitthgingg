package com.heqichao.springBootDemo.module.liteNA;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.heqichao.springBootDemo.base.util.AesUtil;
import com.heqichao.springBootDemo.module.entity.LiteApplication;

/**
 * 
 * @author Muzzy Xu.
 * @date 2019/03/20
 */
public class MyNbiotAuths {

	 Logger logger = LoggerFactory.getLogger(getClass());
	
	 /**
	  * 登录获取token
	  * 
	  * @param httpsUtil 接入证书校验的http
	  * @param appId 应用id
	  * @param secret 应用密匙
	  * @param port 应用接入端口
	  * @return
	  * @throws Exception
	  */
	@SuppressWarnings("unchecked")
	public String login(HttpsUtil httpsUtil,LiteApplication app) throws Exception {
		
		String urlLogin = Constant.APP_AUTH.replace("SERVER:PORT", app.getPlatformIp());
		
		Map<String, String> paramLogin = new HashMap<>();
		paramLogin.put("appId", app.getAppId());
		paramLogin.put("secret", AesUtil.aesDecrypt(app.getSecret()));
		
		StreamClosedHttpResponse responseLogin = httpsUtil.doPostFormUrlEncodedGetStatusLine(urlLogin, paramLogin);
		
		logger.info("app auth success,return accessToken:");
		logger.info(responseLogin.getStatusLine().toString());
		logger.info(responseLogin.getContent());
		
		Map<String, String> data = new HashMap<>();
		data = JsonUtil.jsonString2SimpleObj(responseLogin.getContent(), data.getClass());
		return data.get("accessToken");
	}
}
