package com.heqichao.springBootDemo.base.util;

import com.heqichao.springBootDemo.base.entity.User;
import com.heqichao.springBootDemo.base.param.ApplicationContextUtil;
import com.heqichao.springBootDemo.base.service.UserService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by heqichao on 2018-12-4.
 */
@Component
public class UserCache {
    /**
     * 缓存
     */
    private static Map<Integer,String> userMap =new HashMap<>();

    /**
     * 根据用户ID获取用户名
     * @param userId
     * @return
     */
    public static String getUserName(Integer userId){
        if(userId==null){
            return "";
        }
        String userName =userMap.get(userId);
        if(StringUtil.isEmpty(userName)){
            //缓存中没有则重新放置
            UserService userService = (UserService) ApplicationContextUtil.getApplicationContext().getBean("userServiceImpl");
            User user= userService.querById(userId);
            if(user!=null){
                userName=user.getAccount();
                userMap.put(userId,userName);
            }else{
                userName="";
            }
        }
        return userName;
    }

    /**
     * 清除用户缓存
     */
    public static void cleanUserCache(){
        userMap=new HashMap<>();
    }
}
