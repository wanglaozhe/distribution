package com.yuandong.vo.weixin;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;

import com.yuandong.util.MD5Util;

/**
 * 所有需要进行微信签名的参数对象继承此基类获得计算签名的方法
 * @author Administrator
 *
 */
public abstract class BaseSignObject {

	/**
	 * 获取微信签名
	 * @param weixinKey key为商户平台设置的密钥key 
	 * @return
	 */
	public String getSignValue(String weixinKey){
		BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(this);
		Field[] fields = getClass().getDeclaredFields(); //获取实体类的所有属性，返回Field数组
		Map<String,String> map = new HashMap<>();
	    for(int j=0 ; j<fields.length ; j++){ //遍历所有属性
	    	String name = fields[j].getName(); //获取属性的名字
	    	Object value = wrapper.getPropertyValue(name);
	    	if(value != null && StringUtils.isNotBlank(value.toString())){
	    		map.put(name, value.toString());
	    	}
	    }
	    StringBuffer sb = new StringBuffer();
	    List<String> keys = map.keySet().stream().sorted((s1,s2)->s1.compareTo(s2)).collect(Collectors.toList());
	    for(String key : keys){
	    	//PaySignVo有一个属性名为package_，本应为package，但是作为java关键字不能使用。所以这里替换回来
	    	String name = "package_".equals(key)?"package":key;
	    	if(sb.length() == 0){
	    		sb.append(name).append("=").append(map.get(key));
	    	}else{
	    		sb.append("&").append(name).append("=").append(map.get(key));
	    	}
	    }
	    String stringSignTemp = sb.append("&key=").append(weixinKey).toString();
	    String sign=MD5Util.md5Hex(stringSignTemp).toUpperCase(); //注：MD5签名方式
	    return sign;
	}
}
