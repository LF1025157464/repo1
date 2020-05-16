package com.ydy.sys.entity;

import java.util.Date;

/**
 * 媒体信息表
 * @author dengyan.yao
 *
 */
public class MediaInfo {
	private String mediaId;	// 媒体信息主键
	private int typeId;	// 所属分类
	private String status;	// 状态，是否已删除至回收站。1代表正常
	private String haibao;	// 海报
	private String dafengmian;// 大封面
	private String fengmian;// 小封面
	private String biaoti;	// 标题
	private String bieming;	// 别名
	private String jianjie;	// 简介
	private String tag;		// 标签
	private int playId;		//主演
	private String url;		//url
	private Date updateTime;
	private String userId;
	private String size;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getPlayId() {
		return playId;
	}

	public void setPlayId(int playId) {
		this.playId = playId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}

	public int getTypeId() {
		return typeId;
	}

	public void setTypeId(int typeId) {
		this.typeId = typeId;
	}

	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getHaibao() {
		return haibao;
	}
	public void setHaibao(String haibao) {
		this.haibao = haibao;
	}
	public String getDafengmian() {
		return dafengmian;
	}
	public void setDafengmian(String dafengmian) {
		this.dafengmian = dafengmian;
	}
	public String getFengmian() {
		return fengmian;
	}
	public void setFengmian(String fengmian) {
		this.fengmian = fengmian;
	}
	public String getBiaoti() {
		return biaoti;
	}
	public void setBiaoti(String biaoti) {
		this.biaoti = biaoti;
	}
	public String getBieming() {
		return bieming;
	}
	public void setBieming(String bieming) {
		this.bieming = bieming;
	}
	public String getJianjie() {
		return jianjie;
	}
	public void setJianjie(String jianjie) {
		this.jianjie = jianjie;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
