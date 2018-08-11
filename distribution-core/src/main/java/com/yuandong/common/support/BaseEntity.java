package com.yuandong.common.support;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 统一定义id的entity基类.
 * 
 * 基类统一定义id的属性名称、数据类型、列名映射及生成策略.
 * Oracle需要每个Entity独立定义id的SEQUCENCE时，不继承于本类而改为实现一个Idable的接口。
 * 
 * @author chenqi
 */
// JPA 基类的标识
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity<ID extends Serializable> implements Serializable {

	private static final long serialVersionUID = -8199619426989870310L;
	/*
	@Null 限制只能为null
	@NotNull 限制必须不为null
	@AssertFalse 限制必须为false
	@AssertTrue 限制必须为true
	@DecimalMax(value) 限制必须为一个不大于指定值的数字
	@DecimalMin(value) 限制必须为一个不小于指定值的数字
	@Digits(integer,fraction) 限制必须为一个小数，且整数部分的位数不能超过integer，小数部分的位数不能超过fraction
	@Future 限制必须是一个将来的日期
	@Max(value) 限制必须为一个不大于指定值的数字
	@Min(value) 限制必须为一个不小于指定值的数字
	@Pattern(value) 限制必须符合指定的正则表达式
	@Size(max,min) 限制字符长度必须在min到max之间
	@Past 验证注解的元素值（日期类型）比当前时间早
	@NotEmpty 验证注解的元素值不为null且不为空（字符串长度不为0、集合大小不为0）
	@NotBlank 验证注解的元素值不为空（不为null、去除首位空格后长度为0），不同于@NotEmpty，@NotBlank只应用于字符串且在比较时会去除字符串的空格
	@Email 验证注解的元素值是Email，也可以通过正则表达式和flag指定自定义的email格式 
	*/
	/**
	 * UUID
	 */
	@Id
	@Column(name="id")
    @GenericGenerator(name="system-uuid", strategy="uuid") 
	@GeneratedValue(generator="system-uuid")
	private ID id;
	
	/**
     * 记录创建人标识，记录用户的UUID
     */
    @CreatedBy
	@Column(updatable=false, name="create_by")
	private String createBy;
    
    /**
     * 记录创建日期
     */
	@CreatedDate
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(updatable=false, name="create_date")
	private Date createDate;
	
	/**
	 * 记录最后更新人标识，记录用户的UUID
	 */
	@LastModifiedBy
	@Column(name="update_by")
	private String updateBy;
	
	/**
	 * 记录最后更新日期
	 */
	@LastModifiedDate
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name="update_date")
	private Date updateDate;
	
	@Version
	@Column(name="version")
	private Long version;
	
	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}
	
	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public Long getVersion() {
		return version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Override
	public String toString() {
		return "BaseEntity [id=" + id + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BaseEntity other = (BaseEntity) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
	
	
}
