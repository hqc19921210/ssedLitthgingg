package com.heqichao.springBootDemo.module.wechat.untils;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

public class UserAgentUtil {

	/**
	    * 判断 移动端/PC端
	    * @Title: isMobile
	    * @param request
	    * @return: boolean
	    */
	   public static boolean isMobile(HttpServletRequest request) {
	      List<String> mobileAgents = Arrays.asList("ipad", "iphone os", "rv:1.2.3.4", "ucweb", "android", "windows ce", "windows mobile");
	      String ua = request.getHeader("User-Agent").toLowerCase();
	      for (String sua : mobileAgents) {
	         if (ua.indexOf(sua) > -1) {
	            return true;//手机端
	         }
	      }
	      return false;//PC端
	   }
	   
	   /**
	    * 是否微信浏览器
	    * @Title: isWechat
	    * @param request
	    * @return: boolean
	    */
	   public static boolean isWechat(HttpServletRequest request) {
	      String ua = request.getHeader("User-Agent").toLowerCase();
	         if (ua.indexOf("micromessenger") > -1) {
	            return true;//微信
	         }
	      return false;//非微信手机浏览器
	      
	   }
	   
//	   public static void main(String[] args) {
//	      List<String> mobileAgents = Arrays.asList("ipad", "iphone os", "rv:1.2.3.4", "ucweb", "android", "windows ce", "windows mobile");
//	      String ua = "mozilla/5.0 (windows nt 10.0; win64; x64) applewebkit/537.36 (khtml, like gecko) chrome/65.0.3325.146 safari/537.36".toLowerCase();
//	      for (String sua : mobileAgents) {
//	         if (ua.indexOf(sua) > -1) {
//	            System.out.println("移动端");
//	            return;
//	         }
//	      }
//	      System.out.println("PC端");
//	   }
}
