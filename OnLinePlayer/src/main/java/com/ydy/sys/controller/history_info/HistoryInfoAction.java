package com.ydy.sys.controller.history_info;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import com.ydy.sys.entity.HistoryInfo;
import com.ydy.sys.entity.Result;
import com.ydy.sys.entity.UserInfo;
import com.ydy.sys.service.history_info.HistoryInfoService;
import com.ydy.sys.service.user_info.UserInfoService;

@Controller
@RequestMapping("portal/history_info")
public class HistoryInfoAction {

	@Autowired
	private HistoryInfoService historyInfoService;
	@Autowired
	private UserInfoService userInfoService;
	
	/**
	 * 保存用户的播放记录
	 * @throws QingException
	 */
	@RequestMapping("save.json")
	@ResponseBody
	public void save(
			HttpServletRequest request,
			@RequestParam(value="videoId") String videoId,
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
		
		HistoryInfo historyInfo = new HistoryInfo();
		historyInfo.setVideoId(videoId);
		historyInfo.setUserId(userInfo.getId());
		
		historyInfoService.save(historyInfo);
		
	}
	
	/**
	 * 获取最新的5条历史记录
	 * @param request
	 * @param userToken
	 * @return
	 * @throws QingException
	 */
	@RequestMapping("get_history_5.json")
	@ResponseBody
	public Result getHistory5(
			HttpServletRequest request,
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
		
		List<Map<String, Object>> list = historyInfoService.listHistory5(userInfo.getId());
		return Result.success().add("list", list);
	}
	
	/**
	 * 获取历史记录列表
	 * @param request
	 * @param userToken 用户凭证
	 * @param pageNum
	 * @param pageSize
	 * @return
	 * @throws QingException
	 */
	@RequestMapping("get_history_list.json")
	@ResponseBody
	public Result getHistoryList(
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
		List<Map<String, Object>> list = historyInfoService.listHistory(userInfo.getId());
		PageInfo<Map<String, Object>> pageInfo = new PageInfo<Map<String, Object>>(list);
		
		return Result.success().add("pageInfo", pageInfo);
	}
	
	/**
	 * 删除历史记录
	 * @param request
	 * @param videoId 视频主键
	 * @param userToken 用户凭证
	 * @return
	 * @throws QingException
	 */
	@RequestMapping("delete_history.json")
	@ResponseBody
	public Result deleteHistory(
			HttpServletRequest request,
			@RequestParam(required=false, value="videoId") String videoId,
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
		
		HistoryInfo historyInfo = new HistoryInfo();
		historyInfo.setVideoId(videoId);
		historyInfo.setUserId(userInfo.getId());
		
		historyInfoService.delete(historyInfo);
		
		return Result.success();
	}
	/**
	 * 删除历史记录
	 * @param request
	 * @param videoId 视频主键
	 * @param userToken 用户凭证
	 * @return
	 * @throws QingException
	 */
	@RequestMapping("delete_history.action")
	@ResponseBody
	public void deleteHistory2(
			HttpServletRequest request,HttpServletResponse response,
			@RequestParam(required=false, value="videoId") String videoId,
			@RequestParam(value="userToken") String userToken) throws QingException, ServletException, IOException {

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

		HistoryInfo historyInfo = new HistoryInfo();
		historyInfo.setVideoId(videoId);
		historyInfo.setUserId(userInfo.getId());

		historyInfoService.delete(historyInfo);

		request.getRequestDispatcher("/portal/user_info/history.action").forward(request,response );
	}
}
