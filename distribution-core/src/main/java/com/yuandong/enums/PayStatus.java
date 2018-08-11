package com.yuandong.enums;


public enum PayStatus {
	noPayment,
	partialPayment,
	allPayment;
	
	public int toCode(){
		return this.ordinal();
	}
	
}
