package com.yuandong.common.exception;

import java.util.HashMap;
import java.util.Map;

/**
 * 系统异常类
 * @author chenqi 2016/4/13
 */
public class SystemException extends RuntimeException {

	/**
	 * 用户临时描述的异常
	 */
	public static final byte CUSTOMER_EXCEPTION = 0;

	/**
	 * 非法nodeCode值,找不到对应数据源，严重异常
	 */
	public static final byte ILLEGAL_NODECODE = 1;
	/**
	 * 未初始化数据源,重大异常
	 */
	public static final byte NO_INIT_DATABASE = 2;

	/**
	 * 缺少参数
	 */
	public static final byte MISS_PARAM = 3;

	/**
	 * 加载dubbo消费者失败，请查询dubbo-consumer.xml是否配置
	 */
	public static final byte DUBBO_CUSUMMER_UNLOAD = 4;

	/**
	 * 反射调用get或is方法获取属性值异常
	 */
	public static final byte REFLECT_GET_FIELDVALUE_ERROR = 5;

	/**
	 * 试图获取一个未经@SqlTable声明的对象所对应的数据库表名，唯有@SqlTable声明的对象才能获取数据库对应表名
	 */
	public static final byte ISNOT_ANOTATION_SQLTABLE = 6;
	/**
	 * 试图获取一个未经@SqlColumn声明的属性所对应的数据库字段名，唯有@SqlColumn声明的属性才能获取数据库对应字段名
	 */
	public static final byte ISNOT_ANOTATION_SQLCOLUMN = 7;
	/**
	 * 严重异常，未定义主键，包括未标记@Key或@Key fields值为空
	 */
	public static final byte ISNOT_ANOTATION_KEY = 8;
	/**
	 * 获取类未定义得属性字段异常
	 */
	public static final byte FIELD_NODEF_IN_CLASS =9;

	/**
	 * 无服务
	 */
	public static final byte NO_SERVER_AVAILABLE= 10;


	public static final Map<Byte,String> errorMsgs = new HashMap<Byte, String>();
	static {
		errorMsgs.put(ILLEGAL_NODECODE,"非法nodeCode值,找不到对应数据源，严重异常");
		errorMsgs.put(NO_INIT_DATABASE,"未初始化数据源,重大异常");
		errorMsgs.put(MISS_PARAM,"缺失参数");
		errorMsgs.put(DUBBO_CUSUMMER_UNLOAD,"加载dubbo消费者失败，请查询dubbo-consumer.xml是否配置");
		errorMsgs.put(REFLECT_GET_FIELDVALUE_ERROR,"反射调用get或is方法获取属性值异常，确认该属性是否含有get或is方法");
		errorMsgs.put(ISNOT_ANOTATION_SQLTABLE,"试图获取一个未经@SqlTable声明的对象所对应的数据库表名，唯有@SqlTable声明的对象才能获取数据库对应表名");
		errorMsgs.put(ISNOT_ANOTATION_SQLCOLUMN,"试图获取一个未经@SqlColumn声明的属性所对应的数据库字段名，唯有@SqlColumn声明的属性才能获取数据库对应字段名");
		errorMsgs.put(ISNOT_ANOTATION_KEY,"严重异常，未定义主键，包括未标记@Key或@Key fields值为空");
		errorMsgs.put(FIELD_NODEF_IN_CLASS,"获取类未定义的属性字段异常");
		errorMsgs.put(NO_SERVER_AVAILABLE,"无服务，请检查nodeCode或serverId参数是否正确，或检查服务是否启动");
	}

	private byte errorCode;

	public SystemException(byte errorCode) {
		super(new StringBuffer().append("异常代号：")
				.append(errorCode)
				.append(",异常描述：")
				.append(errorMsgs.get(errorCode)).toString());
		this.errorCode = errorCode;
	}

	public SystemException( String msg) {
		super(new StringBuffer()
				.append(",异常描述：")
				.append(msg).toString());
	}
	
	public SystemException(byte errorCode, String msg) {
		super(new StringBuffer().append("异常代号：")
				.append(errorCode)
				.append(",异常描述：")
				.append(msg).toString());
		this.errorCode = errorCode;
	}

	public byte getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(byte errorCode) {
		this.errorCode = errorCode;
	}

}
