package com.heqichao.springBootDemo.module.onenet.api.cmds;

import java.util.HashMap;
import java.util.Map;

import com.heqichao.springBootDemo.module.onenet.OneNetConfig;
import com.heqichao.springBootDemo.module.onenet.api.AbstractAPI;
import com.heqichao.springBootDemo.module.onenet.exception.OnenetApiException;
import com.heqichao.springBootDemo.module.onenet.http.HttpGetMethod;
import com.heqichao.springBootDemo.module.onenet.request.RequestInfo;
import com.heqichao.springBootDemo.module.onenet.response.BasicResponse;
import com.heqichao.springBootDemo.module.onenet.response.cmds.CmdsResponse;
import org.apache.http.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class QueryCmdsStatus extends AbstractAPI {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	private HttpGetMethod HttpMethod;
	private  String cmdUuid;
	
	
	/**
	 * @param cmdUuid:命令id,String
	 * @param key:masterkey或者设备apikey
	 */
	public QueryCmdsStatus(String cmdUuid,String key) {
		this.cmdUuid = cmdUuid;
		this.key=key;
		this.method= RequestInfo.Method.GET;
		this.HttpMethod=new HttpGetMethod(method);

        Map<String, Object> headmap = new HashMap<String, Object>();
        headmap.put("api-key", key);
        HttpMethod.setHeader(headmap);
		//todo change url
        this.url = OneNetConfig.getUrl() + "/cmds/" + cmdUuid;
        HttpMethod.setcompleteUrl(url,null);
	}


	public BasicResponse<CmdsResponse> executeApi() {
		BasicResponse response=null;
		try {
			HttpResponse httpResponse=HttpMethod.execute();
			response = mapper.readValue(httpResponse.getEntity().getContent(), BasicResponse.class);
			response.setJson(mapper.writeValueAsString(response));
			Object newData = mapper.readValue(mapper.writeValueAsString(response.getDataInternal()), CmdsResponse.class);
			response.setData(newData);
			return response;
		} catch (Exception e) {
			logger.error("json error {}", e.getMessage());
			throw new OnenetApiException(e.getMessage());
		}finally {
			try {
				HttpMethod.httpClient.close();
			} catch (Exception e) {
				logger.error("http close error: {}", e.getMessage());
				throw new OnenetApiException(e.getMessage());
			}
		}
		
	}

}
