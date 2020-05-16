package com.ydy.sys.entity;

/**
 * 视频播放地址表
 * @author dengyan.yao
 *
 */
public class VideoInfo {
	private String videoId;		// 主键
	private String mediaId;		// 媒体信息主键
	private String title;		// 该集标题
	private String image;		// 该集封面
	private String url;			// 视频播放地址
	private String playerId;	// 播放器id
	private String status;		// 状态，是否禁用。1代表正常，0代表禁用
	private String remark;		// 视频简介
	private String power;		// 视频播放权限值
	private String updateTime;	// 发布时间
	private String viewCount;		// 该视频播放总量
	private String biaoti;		// 媒体信息名称
	private String play;		// 播放内容
	
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getPlayerId() {
		return playerId;
	}
	public void setPlayerId(String playerId) {
		this.playerId = playerId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getPower() {
		return power;
	}
	public void setPower(String power) {
		this.power = power;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getViewCount() {
		return viewCount;
	}
	public void setViewCount(String viewCount) {
		this.viewCount = viewCount;
	}
	public String getBiaoti() {
		return biaoti;
	}
	public void setBiaoti(String biaoti) {
		this.biaoti = biaoti;
	}
	public String getPlay() {
		return play;
	}
	public void setPlay(String play) {
		this.play = play;
	}
	@Override
	public String toString() {
		return "VideoInfo [videoId=" + videoId + ", mediaId=" + mediaId + ", title=" + title + ", image=" + image
				+ ", url=" + url + ", playerId=" + playerId + ", status=" + status + ", remark=" + remark + ", power="
				+ power + ", updateTime=" + updateTime + ", viewCount=" + viewCount + ", biaoti=" + biaoti + ", play="
				+ play + "]";
	}
	
}
