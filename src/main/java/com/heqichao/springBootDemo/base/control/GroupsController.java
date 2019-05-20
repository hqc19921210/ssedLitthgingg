package com.heqichao.springBootDemo.base.control;

import com.heqichao.springBootDemo.base.param.ResponeResult;
import com.heqichao.springBootDemo.base.service.GroupsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Muzzy Xu.
 * 
 */


@RestController
@RequestMapping(value = "/service")
public class GroupsController extends BaseController{

    @Autowired
    private GroupsService gService;

    
    @RequestMapping(value = "/getEquGroups")
    public ResponeResult getGroups() {
    	return gService.getGroups();
    }
    
    @RequestMapping(value = "/addGroup")
    public ResponeResult addGroups() {
    	return gService.insertGroup();
    }
    
    @RequestMapping(value = "/editGroup")
    public ResponeResult editGroups() {
    	return gService.updateGroup();
    }
    
    @RequestMapping(value = "/delGroup")
    public ResponeResult delGroups() {
    	return gService.deleteGroup();
    }

}
