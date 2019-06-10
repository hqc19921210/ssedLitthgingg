package com.heqichao.springBootDemo.module.onenet.api.cmds;

import java.util.HashMap;
import java.util.Map;

import com.heqichao.springBootDemo.module.onenet.OneNetConfig;
import com.heqichao.springBootDemo.module.onenet.api.AbstractAPI;
import com.heqichao.springBootDemo.module.onenet.exception.OnenetApiException;
import com.heqichao.springBootDemo.module.onenet.http.HttpGetMethod;
import com.heqichao.springBootDemo.module.onenet.request.RequestInfo;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QueryCmdsRespApi extends AbstractAPI {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private HttpGetMethod HttpMethod;
	private String cmdUuid;

	/**
	 * @param cmdUuid:命令id,String
	 * @param key:masterkey或者设备apikey
	 */
	public QueryCmdsRespApi(String cmdUuid, String key) {
		this.cmdUuid = cmdUuid;
		this.key = key;
		this.method = RequestInfo.Method.GET;
		this.HttpMethod = new HttpGetMethod(method);

		Map<String, Object> headmap = new HashMap<String, Object>();
		headmap.put("api-key", key);
		HttpMethod.setHeader(headmap);
		//todo change url
		this.url = OneNetConfig.getUrl() + "/cmds/" + cmdUuid + "/resp";
		HttpMethod.setcompleteUrl(url, null);
	}

	public String executeApi() {
		String resp = null;
		try {
			HttpResponse httpResponse = HttpMethod.execute();
			resp = EntityUtils.toString(httpResponse.getEntity());
			return resp;
		} catch (Exception e) {
			logger.error("json error {}", e.getMessage());
			throw new OnenetApiException(e.getMessage());
		} finally {
			try {
				HttpMethod.httpClient.close();
			} catch (Exception e) {
				logger.error("http close error: {}", e.getMessage());
				throw new OnenetApiException(e.getMessage());
			}
		}
		
	}
}
