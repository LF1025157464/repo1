package com.ydy.sys.dao.video_info;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ydy.sys.entity.MediaInfo;
import org.apache.ibatis.annotations.Param;

import com.ydy.sys.entity.VideoInfo;


public interface IVideoInfoDAO {

	/**
	 * 根据媒体信息主键，查询该视频下的所有播放集数，从小到大排序（状态正常的）
	 * @param mediaId 媒体信息主键
	 * @return
	 */
	List<VideoInfo> listByMediaId(@Param("mediaId") String mediaId);

	
	/**
	 * 根据主键查询信息
	 * @param videoId 主键
	 * @return
	 */
	VideoInfo selectById(String videoId);

	/**
	 * 插入一条新数据
	 * @param videoInfo
	 * @return
	 */
	int insert(VideoInfo videoInfo);

	/**
	 * 更新一条数据
	 * @param videoInfo
	 * @return
	 */
	int update(VideoInfo videoInfo);

	/**
	 * 查询该视频下的第一集
	 * @param mediaId 媒体信息主键
	 * @return
	 */
	VideoInfo selectByMediaIdFirst(String mediaId);
	
	/**
	 * 查询该视频下的最新一集
	 * @param mediaId 媒体信息主键
	 * @return
	 */
	VideoInfo selectByMediaIdLast(String mediaId);

	/**
	 * 查询使用该播放器的视频个数
	 * @param playerId
	 * @return
	 */
	int countByPlayerId(String playerId);

	/**
	 * 更新统一封面
	 * @param mediaId 媒体信息主键
	 * @param imgUrl 图片地址
	 * @return
	 */
	int updateImage(@Param("mediaId") String mediaId, @Param("image") String image);

	/**
	 * 根据媒体主键和第几集，查询是否存在既存数据
	 * @param mediaId 媒体主键
	 * @param num 第几集
	 * @return
	 */
	VideoInfo selectByMediaIdAndNum(@Param("mediaId") String mediaId, @Param("num") String num);

	/**
	 * 批量更新视频状态
	 * @param videoIdArr 主键数组
	 * @param status 状态
	 * @return
	 */
	int batchUpdateStatus(@Param("videoIdArr") String[] videoIdArr, @Param("status") String status);

	/**
	 * 批量删除
	 * @param videoIdArr 主键数组
	 * @return
	 */
	int batchDelete(@Param("videoIdArr") String[] videoIdArr);

	/**
	 * 根据视频主键查询视频播放源
	 * @param videoId
	 * @return
	 */
	VideoInfo selectVideoPlayById(String videoId);

	/**
	 * 更新统一权限值
	 * @param mediaId
	 * @param power
	 */
	int updatePower(@Param("mediaId") String mediaId, @Param("power") String power);



	/**
	 * 批量更新视频的状态
	 * @param param
	 * @return
	 */
	int batchUpdate(Map<String, Object> param);

	/**
	 * 清空视频点击量
	 * @param videoInfo
	 */
	int clearViewCount(VideoInfo videoInfo);

	/**
	 * 获取视频信息（前台播放页面专用）
	 * @param videoId 视频主键
	 * @return
	 */
	VideoInfo selectByIdWithPortal(String videoId);

	/**
	 * 根据媒体主键数组，批量删除视频信息
	 * @param mediaIdArr
	 * @return
	 */
	int batchDeleteByMediaId(@Param("mediaIdArr") String[] mediaIdArr);
	
	/**
	 * 插入一条新的数据到video_info
	 * @param alterSql
	 * @return
	 */
	int insertVideo(VideoInfo alterSql);

	/**
	 * 获取播放量最多的视频
	 * @return
	 * @param typeId
	 * @param date
	 */
    List<VideoInfo> listHotVideo(MediaInfo mediaInfo);


	/**
	 * 获取最新视频
	 * @param
	 * @param
	 */
	List<VideoInfo> listNewVideo();


	/**
	 * 获取最新视频
	 * @param
	 * @param
	 */
	List<VideoInfo> listNewVideoByType(int typeId);

    VideoInfo selectVideoByMediaId(String mediaId);

    //不分种类，获取播放量最多
	List<VideoInfo> listHotVideo2(String date);

	List<VideoInfo> listCommentVideoByType(int typeId);

	List<VideoInfo> listNewVideoByType2(int typeId);
}