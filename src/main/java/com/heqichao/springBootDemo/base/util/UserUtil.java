package com.heqichao.springBootDemo.base.util;

import com.heqichao.springBootDemo.base.service.UserService;

/**
 * Created by heqichao on 2018-12-2.
 */
public class UserUtil {

    /**
     * 判断当前用户是否有增改删权限
     * @return
     */
    public static boolean hasCRDPermission(){
        Integer cmp =ServletUtil.getSessionUser().getCompetence();
        if(cmp !=null ) {
            //管理员查询所有
            if (UserService.ROOT.equals(cmp) || UserService.CUSTOMER.equals(cmp)) {
                return true;
            }
        }
        return false;
    }
}
