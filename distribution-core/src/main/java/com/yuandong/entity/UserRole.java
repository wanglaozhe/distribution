package com.yuandong.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

import com.yuandong.common.support.BaseEntity;

@Data
@Entity
@Table(name = "T_USER_ROLE")
public class UserRole extends BaseEntity<String>{
	
	@NotEmpty(message = "userId不能为空")
	@Column(updatable=false,nullable = false)
	private String userId;
	
	@NotEmpty(message = "roleId不能为空")
	@Column(updatable=false,nullable = false)
	private String roleId;

	public UserRole(String userId, String roleId) {
		super();
		this.userId = userId;
		this.roleId = roleId;
	}
	
	public UserRole() {
		super();
	}
}
