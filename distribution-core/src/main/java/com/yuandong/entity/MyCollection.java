package com.yuandong.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import lombok.Data;

import com.yuandong.common.support.BaseEntity;

/**
 * 我的课程收藏
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "T_My_COLLECTION")
public class MyCollection extends BaseEntity<String> {
	@NotEmpty(message = "openId不能为空")
	@Column(updatable=false,nullable = false)
	private String openId;
	
	@NotEmpty(message = "courceId不能为空")
	@Column(updatable=false,nullable = false)
	private String courceId;
}
