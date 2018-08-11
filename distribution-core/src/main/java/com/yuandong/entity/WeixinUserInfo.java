package com.yuandong.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lombok.Data;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import com.yuandong.common.support.BaseEntity;

/**
 * 小程序用户表（包括代理）
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "T_WEIXIN_USER_INFO")
public class WeixinUserInfo extends BaseEntity<String> {
	
	@NotEmpty(message = "openid不能为空")
	@Column(unique = true,nullable = false,length=64)
	private String openId;
	
	//代理真是姓名
	@NotEmpty(message = "realName不能为空")
	@Column(unique = false,updatable=true)
	private String realName;
	
	@Min(value=1,message="年龄最小不能小于1")
	@Max(value=120,message="年龄最大不能超过120")
	private int age;
	
	@Email
	@Column(unique = true)
    private String email;
	
	@Column(unique = true,length=11)
	@javax.validation.constraints.Pattern(regexp="^(1)\\d{10}$")
	private String mobile;
	
	private String address;
	
	//以下是微信信息，登录的时候获取，可自主修改
	@Column(length=32)
	private String nickName;
	
	//枚举SexType 性别 0：未知、1：男、2：女
	private int sex; 
	//头像url
	private String avatarUrl;
	private String country;
	private String province;
	private String city;
	private String language;
	
	//是否是代理
	private boolean agent;
	
}
