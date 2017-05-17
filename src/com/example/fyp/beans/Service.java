package com.example.fyp.beans;

import java.io.Serializable;
import java.math.BigDecimal;

public class Service implements Serializable {

	private java.lang.Integer id;
	private java.lang.String serviceName;
	private BigDecimal price;
	private java.lang.String picture;
	private java.lang.String description;
	private java.lang.Integer typeID;
	private java.util.Date createTime;
	private java.lang.String createBy;
	private java.util.Date updateTime;
	private java.lang.String updateBy;
	private java.lang.Integer state;
	private java.lang.Integer number = 0;

	public java.lang.Integer getId() {
		return this.id;
	}

	public void setId(java.lang.Integer id) {
		this.id = id;
	}

	public java.lang.String getServiceName() {
		return this.serviceName;
	}

	public void setServiceName(java.lang.String serviceName) {
		this.serviceName = serviceName;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public java.lang.String getPicture() {
		return this.picture;
	}

	public void setPicture(java.lang.String picture) {
		this.picture = picture;
	}

	public java.lang.String getDescription() {
		return this.description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	public java.lang.Integer getTypeID() {
		return this.typeID;
	}

	public void setTypeID(java.lang.Integer typeID) {
		this.typeID = typeID;
	}

	public java.util.Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(java.util.Date createTime) {
		this.createTime = createTime;
	}

	public java.lang.String getCreateBy() {
		return this.createBy;
	}

	public void setCreateBy(java.lang.String createBy) {
		this.createBy = createBy;
	}

	public java.util.Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(java.util.Date updateTime) {
		this.updateTime = updateTime;
	}

	public java.lang.String getUpdateBy() {
		return this.updateBy;
	}

	public void setUpdateBy(java.lang.String updateBy) {
		this.updateBy = updateBy;
	}

	public java.lang.Integer getState() {
		return this.state;
	}

	public void setState(java.lang.Integer state) {
		this.state = state;
	}

	public java.lang.Integer getNumber() {
		return number;
	}

	public void setNumber(java.lang.Integer number) {
		this.number = number;
	}
}
