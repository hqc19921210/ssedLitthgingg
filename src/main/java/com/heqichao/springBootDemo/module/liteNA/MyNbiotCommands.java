package com.heqichao.springBootDemo.module.liteNA;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.pagehelper.StringUtil;
import com.heqichao.springBootDemo.module.entity.LiteApplication;

/**
 * 
 * @author Muzzy Xu.
 * @date 2019/03/20
 *
 */
public class MyNbiotCommands {

	Logger logger = LoggerFactory.getLogger(getClass());
	
	/**
	 * 命令下发
	 * @param paramPostAsynCmd
	 * @param appId 应用id
	 * @param secret 应用密匙
	 * @param port 应用接入端口
	 * @return
	 * @throws Exception
	 */
	public String postCommand(Map<String, Object> paramPostAsynCmd,LiteApplication app) throws Exception {
		if(StringUtil.isEmpty(app.getPlatformIp())) {
			return "无法获取应用ID，命令下发失败";
		}
		HttpsUtil httpsUtil = new HttpsUtil();
		MyNbiotAuths myNbiotAuthUntils = new MyNbiotAuths();//新建login对象
        httpsUtil.initSSLConfigForTwoWay();//检查目录下的证书，二段校验

        // Authentication，get token
        String accessToken = myNbiotAuthUntils.login(httpsUtil,app);

        //Please make sure that the following parameter values have been modified in the Constant file.
        String urlPostAsynCmd = Constant.POST_ASYN_CMD.replace("SERVER:PORT", app.getPlatformIp());
        
        String jsonRequest = JsonUtil.jsonObj2Sting(paramPostAsynCmd);
                
        Map<String, String> header = new HashMap<>();
        header.put(Constant.HEADER_APP_KEY, app.getAppId());
        header.put(Constant.HEADER_APP_AUTH, "Bearer" + " " + accessToken);
        
        HttpResponse responsePostAsynCmd = httpsUtil.doPostJson(urlPostAsynCmd, header, jsonRequest);

        String responseBody = httpsUtil.getHttpResponseBody(responsePostAsynCmd);
        
        logger.info("PostAsynCommand, response content:");
        logger.info(responsePostAsynCmd.getStatusLine().toString());
        logger.info(responseBody);
        if(responseBody != null) {
        	return responseBody;
        }
        return "命令下发失败";
        
	}
}
