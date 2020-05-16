package com.ydy.sys.controller.user_profile_info;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ydy.common.constan.ErrorMsg;
import com.ydy.common.exception.QingException;
import com.ydy.sys.entity.QiniuInfo;
import com.ydy.sys.entity.Result;
import com.ydy.sys.entity.UserInfo;
import com.ydy.sys.entity.UserProfileInfo;
import com.ydy.sys.service.qiniu_info.QiniuInfoService;
import com.ydy.sys.service.template_info.TemplateInfoService;
import com.ydy.sys.service.user_info.UserInfoService;
import com.ydy.sys.service.user_profile_info.UserProfileInfoService;

@Controller
@RequestMapping("portal/user_profile_info")
public class UserProfileInfoAction {

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private UserProfileInfoService userProfileInfoService;
	@Autowired
	private QiniuInfoService qiniuInfoService;
	@Autowired
	private TemplateInfoService templateInfoService;
	
	/**
	 * 跳转修改头像页面
	 * @return
	 */
	@RequestMapping("change_avatar.action")
	public String changeAvatar() {
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");
				
		return "portal/pc/template/" + templatePC + "/user/personal/avatar";
	}
	
	/**
	 * 保存用户头像
	 * @param request
	 * @param avatar
	 * @param userToken
	 * @return
	 * @throws IOException
	 * @throws QingException
	 */
	@RequestMapping("save_avatar.json")
	@ResponseBody
	public Result saveAvatar(
			HttpServletRequest request,
			@RequestParam(value="avatar") String avatar,
			@RequestParam(value="userToken") String userToken) throws IOException, QingException {
		
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
		
		// 将base64头像上传到七牛云
		QiniuInfo qiniuInfo = qiniuInfoService.selectByType("touxiang");
		avatar = qiniuInfoService.uploadAvatar(avatar, qiniuInfo);
		
		// 保存用户头像
		UserProfileInfo userProfileInfo = new UserProfileInfo();
		//保存七牛雲下的头像地址
		userProfileInfo.setAvatar(avatar);
		userProfileInfo.setUserId(userInfo.getId());
		
		userProfileInfoService.save(userProfileInfo);
		
		// 重新设置session
		userInfo = userInfoService.getUserInfoByUserToken(userToken);
		request.getSession().setAttribute("userInfo", userInfo);
		
		return Result.success();
	}
}
