package com.ydy.sys.entity;

import java.sql.Timestamp;

/**
 * 视频文件上传的信息
 * @author dengyan.yao
 *
 */
public class UpLoadVideoInfo {
    private long fileId;
    private String titleOrig;//原文件名
    private String titleAlter;//修改后文件名
    private String size;//文件大小
    private String type;//文件类型
    private String path;//文件保存路径
    private Timestamp uploadTime;//文件上传时间
    private String fileNamedirs;//保存视频的绝对路径
	public String getFileNamedirs() {
		return fileNamedirs;
	}
	public void setFileNamedirs(String fileNamedirs) {
		this.fileNamedirs = fileNamedirs;
	}
	public long getFileId() {
		return fileId;
	}
	public void setFileId(long fileId) {
		this.fileId = fileId;
	}
	public String getTitleOrig() {
		return titleOrig;
	}
	public void setTitleOrig(String titleOrig) {
		this.titleOrig = titleOrig;
	}
	public String getTitleAlter() {
		return titleAlter;
	}
	public void setTitleAlter(String titleAlter) {
		this.titleAlter = titleAlter;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Timestamp getUploadTime() {
		return uploadTime;
	}
	public void setUploadTime(Timestamp uploadTime) {
		this.uploadTime = uploadTime;
	}
    
}
