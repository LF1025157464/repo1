package com.ydy.sys.entity;

/**
 * 视频收藏表
 * @author dengyan.yao
 *
 */
public class CollectionInfo {
	private String id;		// 主键
	private String mediaId;	// 媒体表主键
	private String videoId;	//视频表主键
	public String getVideoId() {
		return videoId;
	}
	public void setVideoId(String videoId) {
		this.videoId = videoId;
	}
	private String userId;	// 用户id
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMediaId() {
		return mediaId;
	}
	public void setMediaId(String mediaId) {
		this.mediaId = mediaId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

}
