package com.yuandong.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

import com.yuandong.common.support.BaseEntity;

@Data
@Entity
@Table(name = "T_RESOURCE")
public class Resource extends BaseEntity<String> {
	@NotEmpty(message = "用户名不能为空")
	@Column(unique = true,nullable = false)
	private String name;
	
	private String url;
	
	private String parentId;
	
	private boolean isMenu;
}
