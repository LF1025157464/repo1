package com.ydy.sys.controller.collection_info;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ydy.common.constan.ErrorMsg;
import com.ydy.common.exception.QingException;
import com.ydy.sys.entity.CollectionInfo;
import com.ydy.sys.entity.Result;
import com.ydy.sys.entity.UserInfo;
import com.ydy.sys.service.collection_info.CollectionInfoService;
import com.ydy.sys.service.user_info.UserInfoService;

@Controller
@RequestMapping("portal/collection_info")
public class CollectionInfoAction {

	@Autowired
	private CollectionInfoService collectionInfoService;
	@Autowired
	private UserInfoService userInfoService;
	/**
	 * 添加视频收藏
	 * @throws QingException
	 */
	@RequestMapping("save.json")
	@ResponseBody
	public Result save(
			HttpServletRequest request,
			@RequestParam(value="mediaId") String mediaId,
			@RequestParam(value="videoId") String videoId,
			@RequestParam(value="userId") String userId,
			@RequestParam(value="userToken") String userToken) throws QingException {
		if (StringUtils.isEmpty(userToken)) {
			throw new QingException(ErrorMsg.ERROR_100012);
		}
		
		// 判断session
		HttpSession session  = request.getSession();
		// 从session中取出用户身份信息
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");
		
		if (userInfo==null) {
			userInfo = userInfoService.getUserInfoByUserToken(userToken);
			// 将用户信息保存进session
			request.getSession().setAttribute("userInfo", userInfo);
		}

		CollectionInfo collectionInfo = new CollectionInfo();
		collectionInfo.setMediaId(mediaId);
		collectionInfo.setUserId(userInfo.getId());
		collectionInfo.setVideoId(videoId);
		//收藏之前先判断是否已经收藏
		int count = collectionInfoService.countByMediaIdAndUserId(collectionInfo.getMediaId(),																collectionInfo.getUserId(),														collectionInfo.getVideoId());
		if (count>0) {
			throw new QingException(ErrorMsg.ERROR_X00001);
		}else{
			collectionInfoService.save(collectionInfo);
			return Result.success();
		}
		
		
	}
	
	/**
	 * 获取收藏列表
	 * @param request
	 * @param userToken 用户凭证
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws QingException
	 */
	@RequestMapping("get_collection_list.json")
	@ResponseBody
	public Result getCollectionList(
			HttpServletRequest request,
			@RequestParam(value="userToken") String userToken,
			@RequestParam(value="pageNum", defaultValue="1") int pageNum,
			@RequestParam(value="pageSize", defaultValue="5") int pageSize) throws QingException {
		
		if (StringUtils.isEmpty(userToken)) {
			throw new QingException(ErrorMsg.ERROR_100012);
		}
		
		// 判断session
		HttpSession session  = request.getSession();
		// 从session中取出用户身份信息
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");
		
		if (userInfo==null) {
			userInfo = userInfoService.getUserInfoByUserToken(userToken);
			// 将用户信息保存进session
			request.getSession().setAttribute("userInfo", userInfo);
		}
		
		// pageHelper分页插件
		// 只需要在查询之前调用，传入当前页码，以及每一页显示多少条
		PageHelper.startPage(pageNum, pageSize);
		List<Map<String, Object>> list = collectionInfoService.listCollection(userInfo.getId());
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		return Result.success().add("pageInfo", pageInfo);
	}
	
	/**
	 * 删除收藏的视频
	 * @param request
	 * @param mediaId 媒体主键
	 * @param userToken 用户凭证
	 * @return
	 * @throws QingException
	 */
	@RequestMapping("delete_collection.json")
	@ResponseBody
	public Result deleteCollection(
			HttpServletRequest request,
			@RequestParam(required=false, value="mediaId") String mediaId,
			@RequestParam(value="userToken") String userToken) throws QingException {
		
		if (StringUtils.isEmpty(userToken)) {
			throw new QingException(ErrorMsg.ERROR_100012);
		}

		// 判断session
		HttpSession session  = request.getSession();
		// 从session中取出用户身份信息
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");
		
		if (userInfo==null) {
			userInfo = userInfoService.getUserInfoByUserToken(userToken);
			// 将用户信息保存进session
			request.getSession().setAttribute("userInfo", userInfo);
		}
		
		CollectionInfo collectionInfo = new CollectionInfo();
		collectionInfo.setMediaId(mediaId);
		collectionInfo.setUserId(userInfo.getId());
		
		collectionInfoService.delete(collectionInfo);
		
		return Result.success();
	}
}
