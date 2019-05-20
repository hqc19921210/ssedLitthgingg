package com.heqichao.springBootDemo.module.wechat.untils;

import java.net.Socket;
import java.security.Principal;
import java.security.PrivateKey;
import java.security.cert.CertificateException;

import javax.net.ssl.X509KeyManager;
import javax.security.cert.X509Certificate;

public class MyX509TrustManager implements X509KeyManager {
	 
    // 检查客户端证书
    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }
 
    // 检查服务器端证书
    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }
 
    // 返回受信任的X509证书数组
    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

	@Override
	public String chooseClientAlias(String[] arg0, Principal[] arg1, Socket arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String chooseServerAlias(String arg0, Principal[] arg1, Socket arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public java.security.cert.X509Certificate[] getCertificateChain(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getClientAliases(String arg0, Principal[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PrivateKey getPrivateKey(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getServerAliases(String arg0, Principal[] arg1) {
		// TODO Auto-generated method stub
		return null;
	}

}