package com.yuandong.enums;
/**
 * 0：未知、1：男、2：女
 * @author Administrator
 *
 */
public enum SexType {
	Unknow,//0
	Man,//1:男
	Women;//2:女
	
	public int toCode(){
		return this.ordinal();
	}
}
