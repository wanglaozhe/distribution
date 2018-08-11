package com.yuandong.argumentresolver;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import com.yuandong.common.anotation.WeChatArgument;
import com.yuandong.common.config.WeChatConfig;
import com.yuandong.util.HttpClientUtils;
import com.yuandong.util.StringUtil;

@Component
public class WeChatArgumentResolver implements HandlerMethodArgumentResolver {
    @Autowired
    private WeChatConfig weChatConfig;
    /**
     * 标志出支持的方法参数的类型
     * @param parameter
     * @return
     */
    public boolean supportsParameter(MethodParameter parameter) {
//        Class<?> clazz = parameter.getParameterType();
//        return clazz== WeChatArgument.class;
        return parameter.getParameterAnnotation(WeChatArgument.class) != null; 
    }
    /**
     * 向controllor方法中注入参数的实际方法
     * @param parameter
     * @param mavContainer
     * @param webRequest
     * @param binderFactory
     * @return
     * @throws Exception
     */
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
//        HttpServletResponse response = webRequest.getNativeResponse(HttpServletResponse.class);
        Object obj = parameter.getParameterType().newInstance();  
        BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(obj);
        
        Field[] fields = parameter.getParameterType().getDeclaredFields(); 
        Set<String> set = new HashSet<>();  
        for (Field field : fields) { 
        	set.add(field.getName());  
            if("appid".equalsIgnoreCase(field.getName())){
            	wrapper.setPropertyValue(field.getName(),weChatConfig.getAppid());  
            }else if("mch_id".equalsIgnoreCase(field.getName())){
            	wrapper.setPropertyValue(field.getName(),weChatConfig.getMchId());  
            }
        }  
  
       
        
        String requestBody = HttpClientUtils.getRequestBodyByRequest(request);
        if(StringUtils.isNotBlank(requestBody) && StringUtil.isJson(requestBody)){
        	JSONObject json = JSONObject.fromObject(requestBody);
        	 Iterator iterator = json.keys();
        	 while(iterator.hasNext()){
        		 String key = (String) iterator.next();
    	         if(set.contains(key)){
    		         wrapper.setPropertyValue(key,json.get(key));
    	         }
        	 }
        	
        }
        
        for (Iterator<String> paramNames = webRequest.getParameterNames(); paramNames.hasNext(); ) {  
            String paramName = paramNames.next();
            if(set.contains(paramName)){
	            Object o = webRequest.getParameter(paramName) ;  
	            wrapper.setPropertyValue(paramName,o);  
            }
        } 
        
        return obj;  
    }


}