package com.ydy.sys.entity;

import java.io.Serializable;

/**
 * 用户组表
 * @author dengyan.yao
 *
 */
public class GroupInfo implements Serializable {
	private String id;		//主键
	private String name;	//用户组名称
	private String power;   //用户权限值
	private String type;	//该用户组是否为系统内置（system代表内置的，不可修改）
	private Double size; 		//该用户组下用户视频空间大小
	private String sort;	// 排序

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPower() {return power;}
	public void setPower(String power) {
		this.power = power;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

	public Double getSize() {
		return size;
	}

	public void setSize(Double size) {
		this.size = size;
	}

	public String getSort() {
		return sort;
	}
	public void setSort(String sort) {
		this.sort = sort;
	}
	
}
