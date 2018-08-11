package com.yuandong.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

import com.yuandong.common.support.BaseEntity;

/**
 * 用户反馈表
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "T_FEEDBACK")
public class Feedback extends BaseEntity<String> {
	
	@NotEmpty(message = "openId不能为空")
	@Column(updatable=false,nullable = false)
	private String openId;
	
	@NotEmpty(message = "content不能为空")
	@Column(updatable=true,nullable = false,length=2000)
	private String content;

}
