package com.ydy.sys.service.video_info;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ydy.common.constan.ErrorMsg;
import com.ydy.common.exception.QingException;
import com.ydy.sys.dao.media_info.IMediaInfoDAO;
import com.ydy.sys.dao.video_info.IVideoInfoDAO;
import com.ydy.sys.entity.MediaInfo;
import com.ydy.sys.entity.VideoInfo;

@Service("VideoInfoService")
public class VideoInfoService {
	@Autowired
	private IVideoInfoDAO iVideoInfoDAO;
	@Autowired
	private IMediaInfoDAO iMediaInfoDAO;
	/**
	 * 根据主键查询信息
	 * @param videoId 主键
	 * @return
	 */
	public VideoInfo selectById(String videoId) {
		return iVideoInfoDAO.selectById(videoId);
	}
	
	/**
	 * 根据主键查询信息（前台播放页面专用）
	 * @param videoId 主键
	 * @return
	 */
	public VideoInfo selectByIdWithPortal(String videoId) {
		// 获取视频信息（前台播放页面专用）
		VideoInfo videoInfo = iVideoInfoDAO.selectByIdWithPortal(videoId);
		if (videoInfo!=null) {
			// 播放总量自增
			int nViewCount = Integer.parseInt(videoInfo.getViewCount());
			nViewCount++;
			// 更新播放次数
			VideoInfo videoInfo2 = new VideoInfo();
			videoInfo2.setVideoId(videoInfo.getVideoId());
			videoInfo2.setViewCount(String.valueOf(nViewCount));
			
			iVideoInfoDAO.update(videoInfo2);
		}
		return videoInfo;
	}

	/**
	 * 视频播放地址保存
	 * @param videoInfo
	 */
	public void save(VideoInfo videoInfo) {
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String now = formatter.format(currentTime);
		
		// 2.0 如果播放总量没填的话，则默认为1
		if (StringUtils.isEmpty(videoInfo.getViewCount())) {
			videoInfo.setViewCount("1");
		}
		
		// 3.0 保存视频信息
		// 判断是新增还是更新
		if (StringUtils.isEmpty(videoInfo.getVideoId())) {
			// 新增
			videoInfo.setStatus("1");
			videoInfo.setUpdateTime(now);
			iVideoInfoDAO.insert(videoInfo);
		} else {
			// 更新
			iVideoInfoDAO.update(videoInfo);
		}
		
		// 4.0 更新对应的媒体的时间、状态等
		MediaInfo mediaInfo = new MediaInfo();
		mediaInfo.setMediaId(videoInfo.getMediaId());
		mediaInfo.setUpdateTime(currentTime);
		
		iMediaInfoDAO.updateMedia(mediaInfo);
	}

	/**
	 * 更新统一封面
	 * @param mediaId 媒体信息主键
	 * @param image 图片地址
	 */
	public void updateImage(String mediaId, String image) {
		iVideoInfoDAO.updateImage(mediaId, image);
	}

	/**
	 * 批量更新排序
	 * @param videoIdArr 主键数组
	 * @param sortArr 排序数组
	 */
	public void updateSort(String[] videoIdArr, String[] sortArr) {
		for (int i=0; i<videoIdArr.length; i++) {
			VideoInfo videoInfo = new VideoInfo();
			videoInfo.setVideoId(videoIdArr[i]);
			
			iVideoInfoDAO.update(videoInfo);
		}
	}

	/**
	 * 批量更新视频状态
	 * @param videoIdArr 主键数组
	 * @param status 状态
	 */
	public void batchUpdateStatus(String[] videoIdArr, String status) {
		iVideoInfoDAO.batchUpdateStatus(videoIdArr, status);
	}

	/**
	 * 批量删除视频
	 * @param videoIdArr 主键数组
	 * @throws QingException
	 */
	public void batchDelete(String[] videoIdArr) throws QingException {
		for (int i=0; i<videoIdArr.length; i++) {
			// 只有已经禁用的视频才可以被删除
			VideoInfo videoInfo = iVideoInfoDAO.selectById(videoIdArr[i]);
			if ("1".equals(videoInfo.getStatus())) {
				throw new QingException(ErrorMsg.ERROR_600005);
			}
		}
		iVideoInfoDAO.batchDelete(videoIdArr);
	}

	/**
	 * 根据视频主键查询视频播放源（电脑端）
	 * @param videoId
	 * @return
	 */
	public String selectVideoPlayById(String videoId) {
		// 1.0 获取视频信息 
		VideoInfo videoInfo = iVideoInfoDAO.selectVideoPlayById(videoId);
		String url =videoInfo.getUrl();

		// 3.0 判断视频地址是否存在
		if (StringUtils.isEmpty(url)) {
			url = "";
		} else {
			if (StringUtils.isEmpty(videoInfo.getUrl())) {
				// 3.1 不存在时，不返回播放内容
				url = "";
			} else {
				return url;
			}
		}

		return url;
	}

	/**
	 * 更新统一权限值
	 * @param mediaId
	 * @param power
	 */
	public void updatePower(String mediaId, String power) {
		iVideoInfoDAO.updatePower(mediaId, power);
	}


	/**
	 * 清空视频点击量
	 * @param videoInfo
	 */
	public void clearViewCount(VideoInfo videoInfo) {
		iVideoInfoDAO.clearViewCount(videoInfo);
	}


	/**
	 * 获取播放量最多的视频
	 */
    public List<VideoInfo> listHotVideo(int typeId) {
    	//获取一个月前时间
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance();
		c.setTime(new Date());
		c.add(Calendar.MONTH, -1);
		Date time = c.getTime();
		String date = format.format(c.getTime());
		if(typeId == 0){
            return iVideoInfoDAO.listHotVideo2(date);
        }else{
		    MediaInfo mediaInfo = new MediaInfo();
		    mediaInfo.setUpdateTime(time);
		    mediaInfo.setTypeId(typeId);
            return iVideoInfoDAO.listHotVideo(mediaInfo);
        }
    }

	public VideoInfo selectVideoByMediaId(String mediaId) {
    	return iVideoInfoDAO.selectVideoByMediaId(mediaId);
	}

	/**
	 * 获取最新视频的六个视频
	 * @param
	 * @param
	 * @return
	 * @throws QingException
	 */
	public List<VideoInfo> listNewVideo() throws QingException {


        return iVideoInfoDAO.listNewVideo();

	}

    /**
     * 获取最新视频的六个视频
     * @param
     * @param
     * @return
     * @throws QingException
     */
    public List<VideoInfo> listNewVideo(int typeId) throws QingException {

		return iVideoInfoDAO.listNewVideoByType(typeId);

    }

	/**
	 * 获取评论最多的视频
	 * @param typeId
	 * @return
	 */
	public List<VideoInfo> listCommentVideo(int typeId) {
		return iVideoInfoDAO.listCommentVideoByType(typeId);
	}

	public List<VideoInfo> listNewVideo2(int typeId) {
		return iVideoInfoDAO.listNewVideoByType2(typeId);
	}

	public List<VideoInfo> listByMediaId(String mediaId) {
		return null;
	}
}
