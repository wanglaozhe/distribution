package com.yuandong.util;

import java.util.Date;

import javax.validation.Valid;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import com.thoughtworks.xstream.XStream;
import com.yuandong.common.config.WeChatConfig;
import com.yuandong.vo.weixin.PaySignVo;
import com.yuandong.vo.weixin.UnifiedOrderVo;

@Component
public class WeixinApiUtils {
	
	private static final Logger logger = LoggerFactory.getLogger(WeixinApiUtils.class);
	
	public static final String SUCCESS = "success";
	
	private static WeChatConfig weChatConfig;
	@Autowired
	public void setWeChatConfig(WeChatConfig weChatConfig) {
		WeixinApiUtils.weChatConfig = weChatConfig;
	}

	// 凭证获取（GET）
	private final static String GET_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
	//获取openId接口
	private final static String GET_OPENTID_URL = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
	//统一下单URL地址：
	private final static String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	/**
	 * 获取微信小程序登录openId
	 * @param code
	 * @return 返回说明
				//正常返回的JSON数据包
				{
				      "openid": "OPENID",
				      "session_key": "SESSIONKEY",
				}
				
				//满足UnionID返回条件时，返回的JSON数据包
				{
				    "openid": "OPENID",
				    "session_key": "SESSIONKEY",
				    "unionid": "UNIONID"
				}
				//错误时返回JSON数据包(示例为Code无效)
				{
				    "errcode": 40029,
				    "errmsg": "invalid code"
				}
	 */
	public final static JSONObject getOpenIdByCode(String code){
		String url = GET_OPENTID_URL.replace("APPID", weChatConfig.getAppid())
				                    .replace("SECRET", weChatConfig.getAppsecret())
				                    .replace("JSCODE", code);
		return HttpClientUtils.httpRequest(url, RequestMethod.POST.toString(), null);
	}
	
	/**
	 * 微信统一下单接口
	 * @param vo
	 * @return 
				返回结果 :
					返回状态码 return_code 是 String(16) SUCCESS 
					SUCCESS/FAIL 
					此字段是通信标识，非交易标识，交易是否成功需要查看result_code来判断
				 
					返回信息 return_msg 否 String(128) 签名失败 
					返回信息，如非空，为错误原因 
				
					签名失败 
				
					参数格式校验错误
				 
				
				以下字段在return_code为SUCCESS的时候有返回 :
					小程序ID appid 是 String(32) wx8888888888888888 调用接口提交的小程序ID 
					商户号 mch_id 是 String(32) 1900000109 调用接口提交的商户号 
					设备号 device_info 否 String(32) 013467007045764 自定义参数，可以为请求支付的终端设备号等 
					随机字符串 nonce_str 是 String(32) 5K8264ILTKCH16CQ2502SI8ZNMTM67VS 微信返回的随机字符串 
					签名 sign 是 String(32) C380BEC2BFD727A4B6845133519F3AD6 微信返回的签名值，详见签名算法 
					业务结果 result_code 是 String(16) SUCCESS SUCCESS/FAIL 
					错误代码 err_code 否 String(32) SYSTEMERROR 详细参见下文错误列表 
					错误代码描述 err_code_des 否 String(128) 系统错误 错误信息描述 
				
				以下字段在return_code 和result_code都为SUCCESS的时候有返回 :
					交易类型 trade_type 是 String(16) JSAPI 交易类型，取值为：JSAPI，NATIVE，APP等，说明详见参数规定 
					预支付交易会话标识 prepay_id 是 String(64) wx201410272009395522657a690389285100 微信生成的预支付会话标识，用于后续接口调用中使用，该值有效期为2小时 
					二维码链接 code_url 否 String(64) URl：weixin：//wxpay/s/An4baqw trade_type为NATIVE时有返回，用于生成二维码，展示给用户进行扫码支付 

	 */
	public final static JSONObject unifiedOrder(@Valid UnifiedOrderVo vo){
		vo.setSign(null);
		String sign = vo.getSignValue(weChatConfig.getKey());
		vo.setSign(sign);//设置签名
		return HttpClientUtils.httpRequest(UNIFIED_ORDER_URL, RequestMethod.POST.toString(), StringUtil.buildBeanToXml(vo));
	}
	
	/**
	 * 获取paySign,再在小程序页面调用wx.requestPayment(OBJECT)发起微信支付
	 * @param prepayId 预支付交易会话标识 prepay_id，微信统一下单接口返回
	 * @return paySign
	 *        
	 */
	public static PaySignVo getPaySignVo(String prepayId){
		PaySignVo vo = new PaySignVo();
		vo.setAppid(weChatConfig.getAppid());
		vo.setPackage_("prepay_id="+prepayId);
		String paySign=vo.getSignValue(weChatConfig.getKey());
		vo.setPaySign(paySign);
		return vo;
	}
	
	
	public static void main(String[] args){
		System.out.println(GET_OPENTID_URL.replace("APPID", "666666"));
		System.out.println(RequestMethod.POST.toString());
		UnifiedOrderVo vo = new UnifiedOrderVo();
		vo.setAppid("appid");
		System.out.println(JSONObject.fromObject(vo).toString());
		
	    System.out.println(vo.getSignValue(""));

		String stringA="appid=wxd930ea5d5a258f4f&body=test&device_info=1000&mch_id=10000100&nonce_str=ibuaiVcKdpRxkhJA"; 
		String stringSignTemp=stringA+"&key=192006250b4c09247ec02edce69f6a2d"; //注：key为商户平台设置的密钥key 
		String sign=MD5Util.md5Hex(stringSignTemp).toUpperCase();//="9A0A8659F005D6984697E2CA0A9CF3B7" //注：MD5签名方式
		System.out.println(sign);
		System.out.println(new Date().getTime());
		
	     System.out.println(StringUtil.buildBeanToXml(vo));
	     
	     String xml = StringUtil.buildBeanToXml(vo);
	     System.out.println(JSONObject.fromObject(StringUtil.buildJsonfromXML(xml)));
	     
	     

	}
}
