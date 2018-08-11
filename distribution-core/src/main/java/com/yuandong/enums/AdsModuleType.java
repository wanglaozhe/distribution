package com.yuandong.enums;

/**
 * 广告类型
 * @author Administrator
 *
 */
public enum AdsModuleType {

	Rotation;//0:多图
	
	public int toCode(){
		return this.ordinal();
	}
}
