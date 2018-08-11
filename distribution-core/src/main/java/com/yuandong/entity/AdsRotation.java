package com.yuandong.entity;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.Data;

import com.yuandong.common.support.BaseEntity;
/**
 * 轮播图
 * @author Administrator
 *
 */
@Data
@Entity
@Table(name = "T_ADS_ROTATION")
public class AdsRotation extends BaseEntity<String>{
	
	private String moduleCode;//分类标志
	
	private String name;
	
	private String hrefUrl;//跳转地址
	
	private String imgUrl;//图片地址
	
	

}
