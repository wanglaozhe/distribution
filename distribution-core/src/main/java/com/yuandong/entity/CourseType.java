package com.yuandong.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

import com.yuandong.common.support.BaseEntity;

/**
 * 课程类型
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "T_COURSE_TYPE")
public class CourseType extends BaseEntity<String> {
	
	@NotEmpty(message = "name不能为空")
	@Column(unique = true,updatable=true,nullable = false)
	private String name;
	
//	private String parentId;
}
