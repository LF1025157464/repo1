package com.ydy.sys.controller.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ydy.common.exception.QingException;
import com.ydy.sys.entity.Result;
import com.ydy.sys.entity.VideoInfo;
import com.ydy.sys.service.media_info.MediaInfoService;
import com.ydy.sys.service.video_info.VideoInfoService;

@Controller
@RequestMapping("api")
public class ApiAction {

	@Autowired
	private MediaInfoService mediaInfoService;
	@Autowired
	private VideoInfoService videoInfoService;
	/**
	 * 获取该视频的信息
	 * @param videoId 视频主键
	 * @return
	 */
	@RequestMapping("get_video_info.json")
	@ResponseBody
	public Result getVideoInfo(
			HttpServletRequest request,
			@RequestParam(value="videoId") String videoId) {
		
		VideoInfo videoInfo = videoInfoService.selectById(videoId);
		
		return Result.success().add("videoInfo", videoInfo);
	}

	/**
	 * 获取该视频的推荐列表
	 * @param mediaId 媒体信息的主键
	 * @return
	 */
	@RequestMapping("get_video_list.json")
	@ResponseBody
	public Result getVideoList(
			@RequestParam(value="mediaId") String mediaId) throws QingException {

		//获取该视频所属用户的其他公开视频
		Map<String, Object> map = mediaInfoService.selectByMediaId(mediaId);
		String userId = (String) map.get("user_id");
		List<Map<String,Object>> list = mediaInfoService.selectMediaByUserId(userId);

		//List<VideoInfo> list = videoInfoService.listByMediaId(mediaId);

		return Result.success().add("list", list);
	}

	/**
	 * 获取视频地址
	 * @param videoId 媒体信息的主键
	 * @return
	 */
	@RequestMapping("get_video_play.json")
	@ResponseBody
	public Result getVideoPlay(
			@RequestParam(value="videoId") String videoId) {
		
		String videoPlay = videoInfoService.selectVideoPlayById(videoId);
		
		return Result.success().add("videoPlay", videoPlay);
	}
	
	/**
	 * 根据主键，获取媒体信息
	 * @param mediaId 媒体信息的主键
	 * @return
	 * @throws QingException
	 */
	@RequestMapping("get_media_info.json")
	@ResponseBody
	public Result getMediaInfo(
			@RequestParam(value="mediaId") String mediaId) throws QingException {
		
		Map<String, Object> info = mediaInfoService.selectByMediaId(mediaId);
		
		return Result.success().add("info", info);
	}
	
	/**
	 * 搜索
	 * @param keyWord 关键词
	 * @return
	 * @throws QingException
	 */
	@RequestMapping("search.json")
	@ResponseBody
	public Result search(
			@RequestParam(value="keyWord") String keyWord,
			@RequestParam(value="pageNum", defaultValue="1") int pageNum,
			@RequestParam(value="pageSize", defaultValue="10") int pageSize) throws QingException {
		
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		
		// pageHelper分页插件
		// 只需要在查询之前调用，传入当前页码，以及每一页显示多少条
		PageHelper.startPage(pageNum, pageSize);
		List<String> mediaIdList = mediaInfoService.search(keyWord);
		PageInfo<String> pageInfo = new PageInfo<String>(mediaIdList);
		
		if (mediaIdList!=null && mediaIdList.isEmpty()==false) {
			int len = mediaIdList.size();
			for (int i=0; i<len; i++) {
				String mediaId = mediaIdList.get(i);
				// 获取每一条媒体信息的主要数据
				Map<String, Object> mediaMap = mediaInfoService.selectByMediaId(mediaId);
				list.add(mediaMap);
			}
		}
		
		return Result.success()
				.add("pageInfo", pageInfo)
				.add("list", list)
				;
	}

	/**
	 * 获取学习区最新视频
	 * @return
	 * @throws QingException
	 */
	@RequestMapping("get_new_xx_video_list.json")
	@ResponseBody
	public Result getNewVideoListByStudy(@RequestParam(value = "typeId") int typeId,
										 @RequestParam(value = "method") String method)throws QingException {
        List<VideoInfo> list = null;
        if("new".equals(method)){
			//获取最新的视频
			list = videoInfoService.listNewVideo(typeId);
		}else if("hot".equals(method)){
			//获取播放最多视频
            list = videoInfoService.listHotVideo(typeId);
		}else if("comment".equals(method)){
        	//获取评论最多的视频
			list = videoInfoService.listCommentVideo(typeId);
		}

		return Result.success().add("list", list);
	}

	/**
	 * 查询更多最新视频
	 * @return
	 * @throws QingException
	 */
	@RequestMapping("get_new_video_list.json")
	@ResponseBody
	public Result getNewVideoListByAmusement(@RequestParam(value = "typeId") int typeId)
			throws QingException {

		List<VideoInfo> list = videoInfoService.listNewVideo2(typeId);

		return Result.success().add("list", list);
	}


	/**
	 * 获取最热门视频（本月播放量最高）
	 * @return
	 * @throws QingException
	 */
	@RequestMapping("get_hot_video_list.json")
	@ResponseBody
	public Result getHotVideoList()
			throws QingException {



		List<VideoInfo> list = videoInfoService.listHotVideo(0);

		return Result.success().add("list", list);
	}
}
