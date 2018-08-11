package com.yuandong.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.hibernate.validator.constraints.NotEmpty;

import com.yuandong.common.support.BaseEntity;

import lombok.Data;

@Data
@Entity
@Table(name = "T_ROLE_RESOURCE")
public class RoleResource extends BaseEntity<String>{
	@NotEmpty(message = "roleId不能为空")
	@Column(updatable=false,nullable = false)
	private String roleId;
	@NotEmpty(message = "resourceId不能为空")
	@Column(updatable=false,nullable = false)
	private String resourceId;

}
