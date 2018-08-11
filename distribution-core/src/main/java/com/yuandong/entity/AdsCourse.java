package com.yuandong.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import com.yuandong.common.support.BaseEntity;
/**
 * 课程装修
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "T_ADS_COURSE")
public class AdsCourse extends BaseEntity<String>{
	
	private String moduleCode;//分类标志
	
	private String courseId;
	
	
	

}
