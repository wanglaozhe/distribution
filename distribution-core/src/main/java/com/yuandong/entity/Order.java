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
 * 订单表
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "T_Order")
public class Order extends BaseEntity<String>{

	@NotEmpty(message = "orderCode不能为空")
	@Column(unique = true,updatable=false,nullable = false)
	private String orderCode;
	
	//此处保存课程名即可
	@NotEmpty(message = "orderName不能为空")
	private String orderName;
	
	@NotEmpty(message = "buyerOpenId不能为空")
	@Column(updatable=false,nullable = false,length=32)
	private String buyerOpenId;//下单人openId
	
	@NotEmpty(message = "buyerNickName不能为空")
	private String buyerNickName;
	
	@NotEmpty(message = "buyerHeaderUrl不能为空")
	private String buyerHeaderUrl;
	
	@Column(updatable=false,length=32)
	private String agentOpenId;//代理人(推荐人)openId
	
	@DecimalMin(value="0",message="price必须为正数")
	@Column(updatable=false)
	private BigDecimal price;//订单价格
	
	@DecimalMin(value="0",message="price必须为正数")
	private BigDecimal paidPrice;//已支付价格
	
	//推荐购买获得的奖励金额
	@DecimalMin(value="0",message="recommentMoney必须为正数")
	@Column(updatable=false)
	private BigDecimal recommentMoney;
	
	//已兑付的推荐金额
	@DecimalMin(value="0",message="payableMoney必须为正数")
	@Column(updatable=true)
	private BigDecimal payableMoney;
	
	
	
	@NotEmpty(message = "payStatus不能为空")
	@Column(updatable=true,nullable = false)
	private String payStatus;//枚举：PayStatus值
	
	//订单课程Id
	@NotEmpty(message = "courseId不能为空")
	@Column(updatable=false,nullable = false,length=32)
	private String courseId;
	
	//课程 卡号与密码可考虑随机生成
	@NotEmpty(message = "code不能为空")
	@Column(updatable=false,nullable = false,length=32)
	private String code;//课程卡号
	
	@NotEmpty(message = "pwd不能为空")
	@Column(updatable=true,nullable = false,length=32)
	private String pwd;//课程兑换密码
	
}
