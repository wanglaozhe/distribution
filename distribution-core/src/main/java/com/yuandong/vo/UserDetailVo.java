package com.yuandong.vo;

import java.util.List;

import lombok.Data;

import com.yuandong.entity.Role;
import com.yuandong.entity.User;

@Data
public class UserDetailVo extends User{

	private static final long serialVersionUID = 7792500818667583724L;
	
	private List<Role> roles;
	
}
