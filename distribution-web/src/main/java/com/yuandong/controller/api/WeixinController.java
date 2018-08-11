package com.yuandong.controller.api;

import javax.validation.Valid;

import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.yuandong.common.anotation.WeChatArgument;
import com.yuandong.common.webcontext.WebContextFactory;
import com.yuandong.entity.WeixinUserInfo;
import com.yuandong.util.ResultGenerator;
import com.yuandong.util.WeixinApiUtils;
import com.yuandong.vo.RestResult;
import com.yuandong.vo.weixin.PaySignVo;
import com.yuandong.vo.weixin.UnifiedOrderVo;

@RestController
@RequestMapping("/api/weixin")
@Validated
public class WeixinController {
	
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ResultGenerator generator;



    /**
     * 获取小程序用户登录后openId
     * @param code 用户登录凭证（有效期五分钟）。开发者需要在开发者服务器后台调用 api，使用 code 换取 openid 和 session_key 等信息
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/getOpenIdByCode", method = RequestMethod.POST)
    public RestResult save(@Valid WeixinUserInfo userInfo,String code) throws Exception{
    	String openid = "";
    	JSONObject jsonObject = WeixinApiUtils.getOpenIdByCode(code);
    	if (null != jsonObject) {
    		openid = jsonObject.getString("openid");
    	}
    	userInfo.setOpenId(openid);
    	/*
    	// 接收用户昵称，修改编码格式
    	String nickName = request.getParameter("nickName");
    	nickName = new String(nickName.getBytes("ISO-8859-1"), "UTF-8");
    	userInfo.setNickName(nickName);


    	// 接收所在国家
    	String country = request.getParameter("country");
    	country = new String(country.getBytes("ISO-8859-1"), "UTF-8");
    	userInfo.setCountry(country);


    	// 接收所在省份
    	String province = request.getParameter("province");
    	province = new String(province.getBytes("ISO-8859-1"), "UTF-8");
    	userInfo.setProvince(province);


    	// 接收所在城市
    	String city = request.getParameter("city");
    	city = new String(city.getBytes("ISO-8859-1"), "UTF-8");
    	userInfo.setCity(city);
	    */
// 保存小程序登录信息
//    	if (uis.findlist(userInfo) == null) {
//    		uis.save(userInfo);
//    	}
        return generator.getSuccessResult(openid);
    }

    
    @RequestMapping(value="/getPaySign.json", method = RequestMethod.POST)
    public RestResult getPaySign(@WeChatArgument UnifiedOrderVo vo) throws Exception{
//    	System.out.println(WebContextFactory.getWebContext().getRequest().getRemoteAddr());
//    	System.out.println(WebContextFactory.getWebContext().getRequest().getRemoteHost());
//    	System.out.println(WebContextFactory.getWebContext().getRequest().getRemotePort());
    	JSONObject json = WeixinApiUtils.unifiedOrder(vo);
    	if(WeixinApiUtils.SUCCESS.equals(json.getString("return_code")) && WeixinApiUtils.SUCCESS.equals(json.getString("result_code"))){
    		PaySignVo paySignVo = WeixinApiUtils.getPaySignVo(json.getString("prepay_id"));
    		return generator.getSuccessResult(paySignVo);
    	}else{
    		return generator.getFailResult(json.getString("return_msg"));
//    		return generator.getFailResult("微信统一下单接口调用异常");
    	}
    	
//    	PaySignVo paySignVo = WeixinApiUtils.getPaySignVo("wx2017033010242291fcfe0db70013231072");
//    	System.out.println(JSONObject.fromObject(paySignVo).toString());
//		return generator.getSuccessResult(paySignVo);
    }
    
    /**
     * 微信支付成功回调同步成功后返回的xml信息
     */
    public static final String OK_XML  = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>";
    
    /**
     * 微信支付成功后回调地址，同步支付信息，注意线程安全
     * @param vo
     * @return
     * @throws Exception
     */
    @RequestMapping(value="/payResultHandle.json", method = RequestMethod.POST)
    public String payResultHandle(@WeChatArgument UnifiedOrderVo vo) throws Exception{
    	
    	return OK_XML;
    }
   
}
