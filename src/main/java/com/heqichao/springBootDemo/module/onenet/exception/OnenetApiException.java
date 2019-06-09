package com.heqichao.springBootDemo.module.onenet.exception;

/**
 * Created by Yuzhou on 2017/3/21.
 */
public class OnenetApiException extends RuntimeException {
	 private String message = null;
	    @Override
		public String getMessage() {
		return message;
	}
		public OnenetApiException( String message) {
	        this.message = message;
	    }
}
