package com.yuandong.util;

import net.sf.json.JSON;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.thoughtworks.xstream.XStream;



public class StringUtil {
	
	/**
	 * 获取随机字符串
	 * @param numberFlag 是否纯数字
	 * @param length 长度
	 * @return
	 */
	public static String getRandomCode(boolean numberFlag, int length){
        String retStr = "";
        String strTable = numberFlag ? "0123456789" : "0123456789abcdefghjkmnpqrstuvwxyz";
        int len = strTable.length();
        boolean bDone = true;
        do {
            retStr = "";
            int count = 0;
            for (int i = 0; i < length; i++) {
                double dblR = Math.random() * len;
                int intR = (int) Math.floor(dblR);
                char c = strTable.charAt(intR);
                if (('0' <= c) && (c <= '9')) {
                    count++;
                }
                retStr += strTable.charAt(intR);
            }
            if (count >= 2) {
                bDone = false;
            }
        } while (bDone);

        return retStr;
    }

	/**
	 * 将对象转为xml
	 * @param vo
	 * @return
	 */
	public final static String buildBeanToXml(Object vo){
		XStream xstream = new XStream(); 
		xstream.alias("xml", vo.getClass());
		return xstream.toXML(vo);
	}
	
	/**
	 * 将xml转为json
	 * @param vo
	 * @return
	 */
	public final static JSON buildJsonfromXML(String xml){
		XMLSerializer xmlSerializer = new XMLSerializer(); 
        JSON json = xmlSerializer.read(xml);
		return json; 
	}
	
	/** 
	* 判断是否是json结构 
	*/ 
	public static boolean isJson(String value) { 
		try { 
			JSONObject.fromObject(value);
		} catch (Exception e) { 
			return false; 
		} 
		return true; 
	} 
	 
	/** 
	* 判断是否是xml结构 
	*/ 
	public static boolean isXML(String value) { 
		try { 
			DocumentHelper.parseText(value); 
		} catch (DocumentException e) { 
			return false; 
		} 
		return true; 
	} 
}
