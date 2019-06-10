package com.heqichao.springBootDemo.module.wechat.untils;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.X509HostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONObject;
import com.heqichao.springBootDemo.module.wechat.entity.AccessToken;

public class WechatUntils {
	
	private static final String APPID = "wxdca63037b8e0c41f";
	private static final String APPSECRET = "d27f5977de57d32821ad89350df699f1";
	private static final String UPLOAD_URL = "https://api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
	private static final String ACCESS_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=SECRET";
	private static final String CODE_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
	private static final String MESSAGE_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
	private static final String TEMPLATE_ID = "friE6E5oXH5qE1UhHIxm52n0hjPEz1fHqBwAlfDPASg";
	private static final String MESSAGE_CALLBACK_URL = "http://iot.szsset.com/wechatLogin.html";
	private static final String REFRESH_URL = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
	private static final String WEB_TOKEN = "W";
	private static final String BASE_TOKEN = "B";
	
	
	public static AccessToken getAccessToken() throws Exception{
		String url = ACCESS_URL.replace("APPID", APPID).replace("SECRET",APPSECRET);
		//获取access_token
		 AccessToken res = JSONObject.parseObject(sendHttpsRequest(url,null), AccessToken.class);
		 res.setTokenType(BASE_TOKEN);
		return res;
	}
	
	public static AccessToken getCodeToken(String code) throws Exception{
		String url = CODE_URL.replace("APPID", APPID).replace("SECRET",APPSECRET).replace("CODE",code);
		//获取openid
		AccessToken res = JSONObject.parseObject(sendHttpsRequest(url,null), AccessToken.class);
		res.setTokenType(WEB_TOKEN);
		return res;
	}
	
	public static AccessToken refreshToken(String refreshCode) throws Exception{
		String url = REFRESH_URL.replace("APPID", APPID).replace("REFRESH_TOKEN",refreshCode);
		//刷新网页授权
		AccessToken res = JSONObject.parseObject(sendHttpsRequest(url,null), AccessToken.class);
		res.setTokenType(WEB_TOKEN);
		return res;
	}
	
	public static String postMessgae(String accessToken,String openid,String aSettingName,String equName,Date time,String pointName,String pointValue) throws Exception{
		String url = MESSAGE_URL.replace("ACCESS_TOKEN", accessToken);
		HttpEntity param = createMessageEntity( openid, aSettingName, equName, time, pointName, pointValue);
		return sendHttpsRequest(url,param);
	}

	
//	public static JSONObject httpsRequest(String requestUrl, String requestMethod, String outputStr) {
//        JSONObject jsonObject = null;
//        try {
//            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
//            TrustManager[] tm = {new MyX509TrustManager()};//SSL
//            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");//SSL
//            sslContext.init(null, tm, new java.security.SecureRandom());//SSL
//            // 从上述SSLContext对象中得到SSLSocketFactory对象
//            SSLSocketFactory ssf = sslContext.getSocketFactory();//SSL
// 
//            URL url = new URL(requestUrl);
//            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//            //HttpURLConnection  con= url.openConnection();
////            conn.setSSLSocketFactory(ssf);//SSL
//            //2017年9月15日16:21:40
//            //新设置的，可能是一直在链接导致的程序死亡
//            conn.setConnectTimeout(10 * 1000);
//            conn.setReadTimeout(50 * 1000);
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            // 设置请求方式（GET/POST）
//            conn.setRequestMethod(requestMethod);
// 
//            // 当outputStr不为null时向输出流写数据
//            if (null != outputStr) {
//                OutputStream outputStream = conn.getOutputStream();
//                // 注意编码格式
//                outputStream.write(outputStr.getBytes("UTF-8"));
//                outputStream.close();
//            }
// 
//            // 从输入流读取返回内容
//            InputStream inputStream = conn.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String str = null;
//            StringBuffer buffer = new StringBuffer();
//            while ((str = bufferedReader.readLine()) != null) {
//                buffer.append(str);
//            }
// 
//            // 释放资源
//            bufferedReader.close();
//            inputStreamReader.close();
//            inputStream.close();
//            inputStream = null;
//            conn.disconnect();
// 
//            jsonObject = JSONObject.parseObject(buffer.toString());
//        } catch (ConnectException ce) {
//            System.out.println("连接超时：{}" + ce);
//        } catch (Exception e) {
//            System.out.println("https请求异常：{}" + e);
//            //log.error("https请求异常：{}", e);
//        }
//        return jsonObject;
//
//	}
	
	/**
	 * 发送HTTPS请求
	 * 
	 * @param 要访问的HTTPS地址,POST访问的参数Map对象
	 * @return  返回响应值
	 * */
	public static final String sendHttpsRequest(String url, HttpEntity params) {
		String responseContent = null;
		HttpClient httpClient = new DefaultHttpClient();
		//创建TrustManager
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {}
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		//这个好像是HOST验证
		X509HostnameVerifier hostnameVerifier = new X509HostnameVerifier() {
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
			public void verify(String arg0, SSLSocket arg1) throws IOException {}
			public void verify(String arg0, String[] arg1, String[] arg2) throws SSLException {}
			public void verify(String arg0, X509Certificate arg1) throws SSLException {}
		};
		try {
			//TLS1.0与SSL3.0基本上没有太大的差别，可粗略理解为TLS是SSL的继承者，但它们使用的是相同的SSLContext
			SSLContext ctx = SSLContext.getInstance("TLS");
			//使用TrustManager来初始化该上下文，TrustManager只是被SSL的Socket所使用
			ctx.init(null, new TrustManager[] { xtm }, null);
			//创建SSLSocketFactory
			SSLSocketFactory socketFactory = new SSLSocketFactory(ctx);
			socketFactory.setHostnameVerifier(hostnameVerifier);
			//通过SchemeRegistry将SSLSocketFactory注册到我们的HttpClient上
			httpClient.getConnectionManager().getSchemeRegistry().register(new Scheme("https", socketFactory, 443));
			HttpPost httpPost = new HttpPost(url);
			List<NameValuePair> formParams = new ArrayList<NameValuePair>(); // 构建POST请求的表单参数
			if(params != null) {
//				for (Map.Entry<String, String> entry : params.entrySet()) {
//					formParams.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
//				}
				httpPost.setEntity(params);
			}else {
				httpPost.setEntity(new UrlEncodedFormEntity(formParams, "UTF-8"));
			}
			HttpResponse response = httpClient.execute(httpPost);
			HttpEntity entity = response.getEntity(); // 获取响应实体
			if (entity != null) {
				responseContent = EntityUtils.toString(entity, "UTF-8");
			}
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 关闭连接,释放资源
			httpClient.getConnectionManager().shutdown();
		}
		return responseContent;
	}
	public static HttpEntity createMessageEntity(String openid,String aSettingName,String equName,Date time,String pointName,String pointValue){
		String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(time);
		StringBuffer sb = new StringBuffer();
		sb.append("{\"first\":{\"value\":\"尊敬的客户，你的设备发生了报警！\",\"color\":\"#173177\"},\"keyword1\":{\"value\":\"");
		sb.append(aSettingName);
		sb.append("\",\"color\":\"#173177\"},\"keyword2\":{\"value\":\"");
		sb.append(equName);
		sb.append("\",\"color\":\"#173177\"},\"keyword3\": {\"value\":\"");
		sb.append(date);
		sb.append("\",\"color\":\"#173177\"},\"remark\":{\"value\":\"触发报警数据点：");
		sb.append(pointName);
		sb.append("，触发报警值：");
		sb.append(pointValue);
		sb.append("\",\"color\":\"#173177\"}}");
		Map dataMap = JSONObject.parseObject(sb.toString(), Map.class);
		
		JSONObject jsonParam = new JSONObject();  
		jsonParam.put("touser", openid);
		jsonParam.put("template_id", TEMPLATE_ID);
		jsonParam.put("url", MESSAGE_CALLBACK_URL);
		jsonParam.put("data", dataMap);
		
        StringEntity entity = new StringEntity(jsonParam.toString(),"utf-8");//解决中文乱码问题    
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");
        
		return entity;
	}
	
	/**
	 * 文件上传
	 * @param filePath
	 * @param accessToken
	 * @param type
	 * @return
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws NoSuchProviderException
	 * @throws KeyManagementException
	 */
	public static String upload(String filePath, String accessToken,String type) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, KeyManagementException {
		File file = new File(filePath);
		if (!file.exists() || !file.isFile()) {
			throw new IOException("文件不存在");
		}

		String url = UPLOAD_URL.replace("ACCESS_TOKEN", accessToken).replace("TYPE",type);
		
		URL urlObj = new URL(url);
		//连接
		HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

		con.setRequestMethod("POST"); 
		con.setDoInput(true);
		con.setDoOutput(true);
		con.setUseCaches(false); 

		//设置请求头信息
		con.setRequestProperty("Connection", "Keep-Alive");
		con.setRequestProperty("Charset", "UTF-8");

		//设置边界
		String BOUNDARY = "----------" + System.currentTimeMillis();
		con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

		StringBuilder sb = new StringBuilder();
		sb.append("--");
		sb.append(BOUNDARY);
		sb.append("\r\n");
		sb.append("Content-Disposition: form-data;name=\"file\";filename=\"" + file.getName() + "\"\r\n");
		sb.append("Content-Type:application/octet-stream\r\n\r\n");

		byte[] head = sb.toString().getBytes("utf-8");

		//获得输出流
		OutputStream out = new DataOutputStream(con.getOutputStream());
		//输出表头
		out.write(head);

		//文件正文部分
		//把文件已流文件的方式 推入到url中
		DataInputStream in = new DataInputStream(new FileInputStream(file));
		int bytes = 0;
		byte[] bufferOut = new byte[1024];
		while ((bytes = in.read(bufferOut)) != -1) {
			out.write(bufferOut, 0, bytes);
		}
		in.close();

		//结尾部分
		byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");//定义最后数据分隔线

		out.write(foot);

		out.flush();
		out.close();

		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = null;
		String result = null;
		try {
			//定义BufferedReader输入流来读取URL的响应
			reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String line = null;
			while ((line = reader.readLine()) != null) {
				buffer.append(line);
			}
			if (result == null) {
				result = buffer.toString();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				reader.close();
			}
		}

		JSONObject jsonObj = JSONObject.parseObject(result);
		System.out.println(jsonObj);
		String typeName = "media_id";
		if(!"image".equals(type)){
			typeName = type + "_media_id";
		}
		String mediaId = jsonObj.getString(typeName);
		return mediaId;
	}
	
	public static void main(String[] args) throws Exception {
		 AccessToken dataMap = JSONObject.parseObject("{\"access_token\":\"22_A-2sFkb2moRGZi3UneJ0AHO5nQo61vApfPXrBv-KJwefncPsJRWLczb0_Z3X_LryfFclr7fT5ocCuSb5t1c8Bg\",\"expires_in\":7200,\"refresh_token\":\"22_UmXWe6DMyYcC3fxmj31-ISO_SY7tsKmrvbfyxEHK6I14PtYItGahLhOAIZagVod8M8Kh64c2Vk4WKC2HhXDr6w\",\"openid\":\"oKTFut2AfPkcBzn3dQ8JJcwi5xcs\",\"scope\":\"snsapi_base\"}", AccessToken.class);
//		System.out.println(postMessgae("22_SrgJqpBGlTcKHzGUiT9rMMcfICiOJLGtU0qOt5WHr7dxCFYmjPEZBSwWzx2Atzwwq5QTUOs6NMK--U2B7JTEDR9oKNZ2DA3CvzOmPDn_R3BzReIOEj9ELPqeSUnLD_UTdBecLOdy2T7LsBp0OJIiAHAKFE",
//				"oKTFut2AfPkcBzn3dQ8JJcwi5xcs",
//				"测试1","test","test2","A","B"));
//		System.out.println(refreshToken("22_UmXWe6DMyYcC3fxmj31-ISO_SY7tsKmrvbfyxEHK6I14PtYItGahLhOAIZagVod8M8Kh64c2Vk4WKC2HhXDr6w"));
//		System.out.println(getAccessToken());
		 System.out.println(dataMap.getAccessToken());
	}
	
}
