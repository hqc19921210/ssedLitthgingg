package com.heqichao.springBootDemo.base.service;

import com.heqichao.springBootDemo.base.mapper.EquGroupsMapper;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.entity.GroupsEntity;
import com.heqichao.springBootDemo.base.entity.User;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;
import com.heqichao.springBootDemo.base.util.TreeUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Map;

/**
 * @author Muzzy Xu.
 * 
 */


@Service
@Transactional
public class GroupsServiceImpl implements GroupsService {
	
    @Autowired
    private EquGroupsMapper gMapper ;
    


    @Override
    public ResponeResult getGroups () {
    	User user = ServletUtil.getSessionUser();
    	Integer competence = user.getCompetence();
    	Integer uid;
    	if(competence == 4) {//判断是否为用户权限
    		uid=user.getParentId();
    	}else {
    		uid = user.getId();
    	}
    	List<GroupsEntity> res = gMapper.getGroups(uid);
    	List<GroupsEntity> treeList = TreeUtil.listToTree(res);//构造树结构
    	return new ResponeResult(treeList);
    	
    }
    @Override
    public ResponeResult insertGroup() {
    	Map map = RequestContext.getContext().getParamMap();
    	User user = ServletUtil.getSessionUser();
    	Integer cmp = user.getCompetence();
    	Integer uid = user.getId();
    	Integer treeRoot = StringUtil.getIntegerByMap(map,"gId");
    	String name = StringUtil.getStringByMap(map,"name");
    	Integer gSort = StringUtil.getIntegerByMap(map,"grpSort");
    	gSort = gSort==null ? 1 :gSort;
    	if(uid == null || cmp == 4 ||treeRoot == null ||name == null) {
    		return new ResponeResult(true,"Add Group Input Error!","errorMsg");
    	}
    	try {
    		gMapper.insertGroups(name, treeRoot, uid, uid, gSort);
    		return new ResponeResult();
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponeResult(true,e.getMessage(),"errorMsg");
		}
    }
    
    @Override
    public ResponeResult updateGroup() {
    	Map map = RequestContext.getContext().getParamMap();
    	User user = ServletUtil.getSessionUser();
    	Integer cmp = user.getCompetence();
    	Integer uid = user.getId();
    	Integer gid = StringUtil.getIntegerByMap(map,"id");
    	Integer treeRoot = StringUtil.getIntegerByMap(map,"gId");
    	String name = StringUtil.getStringByMap(map,"name");
    	Integer gSort = StringUtil.getIntegerByMap(map,"grpSort");
    	gSort = gSort==null ? 1 :gSort;
    	if(uid == null || cmp == 4 ||treeRoot == null ||name == null ||gid == null) {
    		return new ResponeResult(true,"Edit Group Input Error!","errorMsg");
    	}
    	try {
    		gMapper.updateGroups(name, gid,treeRoot, uid, gSort);
    		return new ResponeResult();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponeResult(true,e.getMessage(),"errorMsg");
    	}
    }
    @Override
    public ResponeResult deleteGroup() {
    	Map map = RequestContext.getContext().getParamMap();
    	User user = ServletUtil.getSessionUser();
    	Integer cmp = user.getCompetence();
    	Integer uid = user.getId();
    	Integer gid = StringUtil.getIntegerByMap(map,"id");
    	if(uid == null || cmp == 4 ||gid == null) {
    		return new ResponeResult(true,"Delete Group Input Error!","errorMsg");
    	}
    	if(gMapper.checkIfEquOnNode(uid,gid)) {
    		return new ResponeResult(true,"该分组或其子分组存在设备，无法删除","errorMsg");
    	}
    	try {
    		if(gMapper.deleteGroups(uid,gid)>0) {
    			return new ResponeResult();
    		}else {
    			return new ResponeResult(true,"删除失败","errorMsg");
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    		return new ResponeResult(true,e.getMessage(),"errorMsg");
    	}
    }

}
