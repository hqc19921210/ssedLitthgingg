package com.heqichao.springBootDemo.base.service;

import com.heqichao.springBootDemo.base.mapper.UserMapper;
import com.heqichao.springBootDemo.base.param.RequestContext;
import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.github.pagehelper.PageInfo;
import com.heqichao.springBootDemo.base.entity.User;
import com.heqichao.springBootDemo.base.util.PageUtil;
import com.heqichao.springBootDemo.base.util.ServletUtil;
import com.heqichao.springBootDemo.base.util.StringUtil;

import com.heqichao.springBootDemo.base.util.UserCache;
import com.heqichao.springBootDemo.module.wechat.entity.AccessToken;
import com.heqichao.springBootDemo.module.wechat.untils.WechatUntils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Muzzy Xu.
 * 
 */


@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public PageInfo queryUsersList() {
    	Map map = RequestContext.getContext().getParamMap();
    	String account = StringUtil.getStringByMap(map,"account");
    	String company = StringUtil.getStringByMap(map,"company");
    	Integer seleCompetence = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"seleCompetence"));
    	PageUtil.setPage();
        PageInfo pageInfo = new PageInfo(userMapper.getUsers(
        		ServletUtil.getSessionUser().getCompetence(),
        		ServletUtil.getSessionUser().getId(),
        		account,company,seleCompetence));
    	return pageInfo;
    }
    
	@Override
    public ResponeResult insertUser(Map map) {
		User user = new User(map);
    	Integer uid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	boolean checkAcc = userMapper.duplicatedAccount(user.getAccount(),user.getCompany());
    	if(		user.getAccount() == null || user.getParentId() == null || user.getCompany() == null ||
    			user.getPassword() == null ||user.getContact() == null || user.getPhone() == null ||
    			user.getCompetence() == null || checkAcc || uid == null || cmp == UserService.VISTOR) {
    		String errorMsg = "Create User Input Error!";
    		if(checkAcc) {
    			 errorMsg = "用户名或公司名称重复";
    		}
    		return new ResponeResult(true,errorMsg,"errorMsg");
    	}else {
    		user.setUpadteUID(uid);
    		if(userMapper.insertUser(user)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Create user fail","errorMsg");
    }
    
    @Override
    public ResponeResult updateUserPassword(Map map) {
    	String srcPwd = StringUtil.getStringByMap(map,"srcPwd");
    	String newPwd = StringUtil.getStringByMap(map,"newPwd");
    	Integer uid = userMapper.checkPassword(ServletUtil.getSessionUser().getAccount(), srcPwd);
    	if(srcPwd == null || newPwd == null || uid == null) {
    		return new ResponeResult(true,"Password Input Error!","errorMsg");
    	}else {
    		if(userMapper.updateUserPassword(uid,uid,newPwd)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Updated password fail","errorMsg");
    }
    
    @Override
    public ResponeResult updateUserInfo(Map map) {
    	User user = new User(map);
    	Integer uid = ServletUtil.getSessionUser().getId();
    	if(user.getCompany() == null || user.getContact() == null || user.getPhone() == null || uid == null) {
    		return new ResponeResult(true,"UserInfo Input Error!","errorMsg");
    	}else {
			UserCache.cleanUserCache();
    		user.setId(uid);
    		if(userMapper.updateUserInfo(user)>0) {
    			User newUser = ServletUtil.getSessionUser();
    			newUser.setCompany(user.getCompany());
    			newUser.setContact(user.getContact());
    			newUser.setPhone(user.getPhone());
    			newUser.setFax(user.getFax());
    			newUser.setEmail(user.getEmail());
    			newUser.setSite(user.getSite());
    			newUser.setRemark(user.getRemark());
    			ServletUtil.setSessionUser(newUser);
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Updated UserInfo fail","errorMsg");
    }
    @Override
    public ResponeResult updateUserById(Map map) {
    	User user = new User(map);
    	Integer uid = ServletUtil.getSessionUser().getId();
    	if(user.getCompany() == null || user.getParentId() == null || user.getId() == null || uid == null) {
    		return new ResponeResult(true,"UserInfo Input Error!","errorMsg");
    	}else {
    		user.setUpadteUID(uid);
    		if(userMapper.updateUserInfo(user)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Updated UserInfo fail","errorMsg");
    }


	@Override
	public User querById(Integer uid) {
		return userMapper.getUserInfoById(uid);
	}

	@Override
    public ResponeResult getCompanySelectList() {
    	Integer uid = ServletUtil.getSessionUser().getId();
    	Integer cmp = ServletUtil.getSessionUser().getCompetence();
    	if(uid==null) {
    		return new ResponeResult(true,"页面过期请刷新页面","errorMsg");
    	}
		Map<String, Integer> res =  userMapper.getCompanySelectList(uid,cmp).stream().collect(
						Collectors.toMap(User::getCompany,User::getId, (k1,k2)->k1)
					);
		if(res.size()>0) {
			return new ResponeResult(res);
		}
    	return  new ResponeResult(false,"");
    }
    
    @Override
    public ResponeResult updateUserPasswordByID(Map map) {
    	String newPwd = StringUtil.getStringByMap(map,"newPwd");
    	Integer uid = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"uid"));
    	Integer udid = ServletUtil.getSessionUser().getId();
    	if( newPwd == null || uid == null) {
    		return new ResponeResult(true,"Password Input Error!","errorMsg");
    	}else {
    		if(userMapper.updateUserPassword(uid,udid,newPwd)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Updated password fail","errorMsg");
    }
    
    @Override
    public ResponeResult deleteUserByID(Map map) {
    	Integer uid = StringUtil.objectToInteger(StringUtil.getStringByMap(map,"uid"));
    	Integer udid = ServletUtil.getSessionUser().getId();
    	if(  uid == null || udid == null) {
    		return new ResponeResult(true,"Delete fail!","errorMsg");
    	}else {
			UserCache.cleanUserCache();
    		if(userMapper.delUserById(uid,udid)>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"Delete user fail","errorMsg");
    }
    @Override
    public ResponeResult updateOpenIdById(Map map) throws Exception {
    	String code = StringUtil.getStringByMap(map,"code");
    	Integer uid = ServletUtil.getSessionUser().getId();
    	AccessToken token = WechatUntils.getCodeToken(code);
    	token.setAddUid(uid);
    	token.setUdpUid(uid);
    	if(  uid == null ||token == null) {
    		return new ResponeResult(true,"绑定获取用户信息失败","errorMsg");
    	}else {
    		userMapper.insertAccessToken(token);
    		if(token.getId() != null && userMapper.updateOpenIdById(uid,token.getId())>0) {
    			return new ResponeResult();
    		}
    	}
    	return  new ResponeResult(true,"绑定失败","errorMsg");
    }
    
    @Override
    public AccessToken getTokenByOpenId(Integer openId) throws Exception {
    	String reToken = userMapper.getRefreshToken(openId);
    	return  WechatUntils.refreshToken(reToken);
    }

    
}
