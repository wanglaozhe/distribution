package com.yuandong.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;

import com.yuandong.common.support.BaseEntity;

/**
 * 管理员用户表
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "T_USER")
public class User extends BaseEntity<String>{
	@NotEmpty(message = "用户名不能为空")
    @Size(min = 4 , max = 18, message = "用户名应设为4至18位")
	@Column(unique = true,updatable=false,nullable = false)
    private String userName;
    private String email;
    private Integer sex;
    @Column(nullable = false)
    @NotEmpty(message = "密码不能为空")
//    @Size(min = 6 , max = 18, message = "密码应设为6至18位")
    private String password;
}
