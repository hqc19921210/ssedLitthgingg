package com.heqichao.springBootDemo.module.service;

import java.util.List;
import java.util.Map;

import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.vo.EquipmentVO;

public interface LiteAppService {
	// 正常
	static final String NORMAL = "N"; 
	// 删除
	static final String DELETE = "D";
	
	/**
	 * 发送中
	 */
	static final String PENDDING = "PENDDING";
	/**
	 * 表示未下发
	 */
	static final String DEFAULT = "DEFAULT";
	/**
	 * 表示命令已经过期
	 */
	static final String EXPIRED = "EXPIRED";
	/**
	 * 表示命令已经成功执行
	 */
	static final String SUCCESSFUL = "SUCCESSFUL";
	/**
	 * 表示命令执行失败
	 */
	static final String FAILED = "FAILED";
	/**
	 * 表示命令下发执行超时
	 */
	static final String TIMEOUT = "TIMEOUT";
	/**
	 * 表示命令已经被撤销执行
	 */
	static final String CANCELED = "CANCELED";
	
	
	PageInfo queryLiteApps();
	ResponeResult addLiteApp();
	ResponeResult deleteAppByID(Integer id);
	ResponeResult updLiteApp();
	ResponeResult resetSecret();
	ResponeResult subLiteDataChg() throws Exception;
	ResponeResult getAppSelectList();
	String postCommand(Integer aid, Map params) throws Exception;
	PageInfo getCommandLogByDevId();
	void liteNaCommandCallback(String str);
	Map<String, String> liteNaDataChangedCallback(String str);
	ResponeResult postCommandList(EquipmentVO info, List<Boolean> selectlist, List<Map> cmdlist) throws Exception;

}
