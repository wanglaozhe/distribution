package com.yuandong.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import org.hibernate.validator.constraints.NotEmpty;
import org.hibernate.validator.constraints.Range;

import com.yuandong.common.support.BaseEntity;

/**
 * 广告模块
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "T_ADS_MODULE")
public class AdsModule extends BaseEntity<String> {

	@NotEmpty(message = "name不能为空")
	@Column(updatable=true,nullable = false)
	private String name;
	
	@NotEmpty(message = "name不能为空")
	@Column(updatable=true,unique=true, nullable = false)
	private String moduleCode;
	
	@NotEmpty(message = "type不能为空")
	@Column(updatable=true,nullable = false)
	private String type;//广告类型，枚举：AdsModuleType
}
