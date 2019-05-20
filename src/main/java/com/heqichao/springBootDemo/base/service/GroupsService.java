package com.heqichao.springBootDemo.base.service;

import com.heqichao.springBootDemo.base.param.ResponeResult;

/**
 * @author Muzzy Xu.
 * 
 */


public interface GroupsService {
	Integer ADMINCMP = 2;
	ResponeResult getGroups();
	ResponeResult insertGroup();
	ResponeResult updateGroup();
	ResponeResult deleteGroup();



}
