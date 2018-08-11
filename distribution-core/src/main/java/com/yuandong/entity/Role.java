package com.yuandong.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

import com.yuandong.common.support.BaseEntity;

@Data
@Entity
@Table(name = "T_ROLE")
public class Role extends BaseEntity<String>{
	@NotEmpty(message = "角色名不能为空")
	@Column(unique = true,nullable = false)
	private String name;
    
}
