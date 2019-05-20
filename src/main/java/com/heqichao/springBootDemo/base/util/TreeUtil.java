package com.heqichao.springBootDemo.base.util;

import java.util.ArrayList;
import java.util.List;

import com.heqichao.springBootDemo.base.entity.GroupsEntity;

public class TreeUtil {

	//将list集合转成树形结构的list集合
	public static List<GroupsEntity> listToTree(List<GroupsEntity> list) {
		// 结果集
        List<GroupsEntity> treeList = new ArrayList<GroupsEntity>();
        
        // 先找到所有的一级菜单
        for (int i = 0; i < list.size(); i++) {
            // 一级菜单没有parentId
            if (list.get(i).getPid()==1) {
            	treeList.add(list.get(i));
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (GroupsEntity tree : treeList) {
        	tree.setChildren(findChildren(tree.getId(), list));
        }
        return treeList;
    }
 
    private static List<GroupsEntity> findChildren(Integer id, List<GroupsEntity> list) {
        // 子菜单集
        List<GroupsEntity> childList = new ArrayList<>();
        for (GroupsEntity tree : list) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (tree.getPid()!=1) {
                if (tree.getPid()==id) {
                    childList.add(tree);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (GroupsEntity tree : childList) {
                // 递归
        	tree.setChildren(findChildren(tree.getId(), list));
        } 
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }
}
