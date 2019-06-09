package com.heqichao.springBootDemo.module.onenet.api;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.heqichao.springBootDemo.module.onenet.request.RequestInfo;

public abstract class AbstractAPI <T>{
	public String key;
	public String url;
	public RequestInfo.Method method;
    public ObjectMapper mapper = new ObjectMapper();
}
