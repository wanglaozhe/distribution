package com.yuandong.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(HttpClientUtils.class);

	private static class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
				throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return null;
		}
	}
	
	/**
	 * 模拟http/https请求
	 * @param requestUrl 
	 * @param requestMethod 设置请求方式（GET/POST）
	 * @param outputStr post提交内容
	 * @return
	 */
	public static JSONObject httpRequest(String requestUrl,
			String requestMethod, String outputStr) {
		JSONObject jsonObject = null;
		HttpURLConnection conn = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			// 创建SSLContext对象，并使用我们指定的信任管理器初始化
			TrustManager[] tm = { new TrustAnyTrustManager() };
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();

			URL url = new URL(requestUrl);
			conn = (HttpURLConnection)url.openConnection();
			if(conn instanceof HttpsURLConnection){
				((HttpsURLConnection)conn).setSSLSocketFactory(ssf);
			}

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);
			// 设置请求方式（GET/POST）
			conn.setRequestMethod(requestMethod);

			// 当outputStr不为null时向输出流写数据
			if (null != outputStr) {
				outputStream = conn.getOutputStream();
				// 注意编码格式
				outputStream.write(outputStr.getBytes("UTF-8"));
				outputStream.flush();
			}

			// 从输入流读取返回内容
			inputStream = conn.getInputStream();
			inputStreamReader = new InputStreamReader(
					inputStream, "utf-8");
			bufferedReader = new BufferedReader(
					inputStreamReader);
			String str = null;
			StringBuffer buffer = new StringBuffer();
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}
			
			if(StringUtil.isJson(buffer.toString())){
				jsonObject = JSONObject.fromObject(buffer.toString());
			}else if(StringUtil.isXML(buffer.toString())){
				jsonObject = JSONObject.fromObject(StringUtil.buildJsonfromXML((buffer.toString())));
			}
		} catch (ConnectException ce) {
			logger.error(ce.getMessage(),ce);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}finally {
			// 释放资源
			if(outputStream != null){
				try {
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(bufferedReader != null){
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inputStreamReader != null){
				try {
					inputStreamReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inputStream != null){
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(inputStream != null){
				try {
					inputStream.close();
					inputStream = null;
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(conn != null){
				conn.disconnect();
			}			
		}
		return jsonObject;
	}
	
	public static String getRequestBodyByRequest(HttpServletRequest request) throws IOException {
		InputStream is = null;
		InputStreamReader ir = null;
		BufferedReader br = null;
		try {
			is = request.getInputStream();
			ir = new InputStreamReader(is);
			br = new BufferedReader(ir);
			String line = null;
			StringBuilder sb = new StringBuilder();
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
			if(sb.length() > 0){
				String reqBody = URLDecoder.decode(sb.toString(), "UTF-8");
				reqBody = reqBody.substring(reqBody.indexOf("{"));
				return reqBody;
			}
			return null;
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(ir != null){
				try {
					ir.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}


	public static String urlEncodeUTF8(String source) {
		String result = source;
		try {
			result = java.net.URLEncoder.encode(source, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args){
//		System.out.println(StringUtil.isJson("{device_info:\"aaa\", body:\"bbb\"}"));
//		System.out.println(StringUtil.isXML("<xml></xml>"));
		System.out.println(httpRequest("http://m.test.com.cn/api/weixin/getPaySign.json", "POST", "{device_info:'aaa', body:'bbb'}"));
//		System.out.println(httpRequest("http://m.test.com.cn/scripts/user/edit.js", "GET", "{device_info:\"aaa\", body:\"bbb\"}"));
	}
}
