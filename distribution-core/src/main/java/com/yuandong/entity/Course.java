package com.yuandong.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMin;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

import com.yuandong.common.support.BaseEntity;

/**
 * 课程
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "T_COURSE")
public class Course extends BaseEntity<String> {
	@NotEmpty(message = "name不能为空")
	@Column(updatable=true,nullable = false)
	private String name;
	
//	@NotEmpty(message = "teacherId不能为空")
//	@Column(updatable=true,nullable = false)
//	private String teacherId;
	
	@NotEmpty(message = "picUrl不能为空")
	@Column(updatable=true,nullable = false)
	private String picUrl;//封面Url
	
	//课程名简介（名称，讲师，内容，目录，价格等）
	@NotEmpty(message = "description不能为空")
	@Column(updatable=true,nullable = false,length=2000)
	private String description;
	
	//课程详细图文介绍 
	@NotEmpty(message = "content不能为空")
	@Column(updatable=true,nullable = false,length=4000)
	private String content;
	
	
	@NotEmpty(message = "price不能为空")
	@DecimalMin(value="0",message="price必须为正数")
	@Column(updatable=true,nullable = false)
	private BigDecimal price;
	
	@NotEmpty(message = "recommentMoney不能为空")
	@DecimalMin(value="0",message="recommentMoney必须为正数")
	@Column(updatable=true,nullable = false)
	private BigDecimal recommentMoney;
	
	/**
	 * 是否参加了免费课程活动（免费课程分享后可获得视频地址播放地址以及账号密码）
	 * 如果是免费课程，分享页面不显示支付按钮，分享成功分别下个已支付订单实现
	 */
	private boolean isFree;
	
	@NotEmpty(message = "url不能为空")
	@Column(updatable=true,nullable = false)
	private String url;//课程兑换地址
	
	//课程 卡号与密码可考虑随机生成
		@NotEmpty(message = "code不能为空")
		@Column(updatable=false,nullable = false,length=32)
		private String code;//课程卡号
		
		@NotEmpty(message = "pwd不能为空")
		@Column(updatable=true,nullable = false,length=32)
		private String pwd;//课程兑换密码
}
