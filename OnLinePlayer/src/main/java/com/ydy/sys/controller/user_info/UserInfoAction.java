package com.ydy.sys.controller.user_info;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ydy.sys.entity.*;
import com.ydy.sys.service.media_info.MediaInfoService;
import com.ydy.sys.service.type_info.TypeInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ydy.common.constan.ErrorMsg;
import com.ydy.common.exception.QingException;
import com.ydy.common.util.MD5;
import com.ydy.sys.service.template_info.TemplateInfoService;
import com.ydy.sys.service.user_info.UserInfoService;
import com.ydy.sys.service.web_info.WebInfoService;

@Controller
@RequestMapping("portal/user_info")
public class UserInfoAction {

	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private WebInfoService webInfoService;
	@Autowired
	private TemplateInfoService templateInfoService;
	@Autowired
	private TypeInfoService typeInfoService;
	@Autowired
	private MediaInfoService mediaInfoService;
	
	/**
	 * 用户注册
	 * @throws Exception 
	 */
	@RequestMapping("register.json")
	@ResponseBody
	public Result register(HttpServletRequest request) throws Exception {

		//注册用户，返回用户信息
		Map<String, Object> info = userInfoService.register(request);
		
		return Result.success().add("info", info);
	}
	
	/**
	 * 用户注册时，邮箱验证
	 * @param request
	 * @param userToken 用户凭证
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("register_email.json")
	@ResponseBody
	public Result registerEmail(
			HttpServletRequest request,
			@RequestParam(value="userToken") String userToken) throws Exception {
		
		if (StringUtils.isEmpty(userToken)) {
			throw new QingException(ErrorMsg.ERROR_100011);
		}
		//通过用户名查询用户
		UserInfo userInfo = userInfoService.getUserByUserToken(userToken);
		//向用户邮箱发送验证码
		userInfoService.sendEmail(userInfo, "注册邮箱验证", "register");
		
		return Result.success();
	}
	
	/**
	 * 邮箱验证，激活账号
	 * @param request
	 * @param identifyingCode 验证码
	 * @param userToken 用户凭证
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("validate_email.json")
	@ResponseBody
	public Result validateEmail(
			HttpServletRequest request,
			@RequestParam(value="identifyingCode") String identifyingCode,
			@RequestParam(value="userToken") String userToken) throws Exception {

		// 校验验证码
		if (StringUtils.isEmpty(identifyingCode)) {
			throw new QingException(ErrorMsg.ERROR_100013);
		}
		
		// 校验用户凭证
		if (StringUtils.isEmpty(userToken)) {
			throw new QingException(ErrorMsg.ERROR_100011);
		}

		// 取出用户身份信息
		UserInfo userInfo = userInfoService.getUserByUserToken(userToken);
		
		UserInfo userInfo2 = new UserInfo();
		userInfo2.setId(userInfo.getId());
		userInfo2.setStatus("1");	// 激活
		
		userInfoService.validateEmail(userInfo2, identifyingCode);
		
		// 将用户信息保存进session
		request.getSession().setAttribute("userInfo", userInfo);
		
		return Result.success();
	}
	
	/**
	 * 用户登录
	 * @throws Exception
	 */
	@RequestMapping("login.json")
	@ResponseBody
	public Result login(HttpServletRequest request) throws Exception {
		
		Map<String, Object> info = userInfoService.login(request);
		
		return Result.success().add("info", info);
	}
	
	/**
	 * 用户注销，清除session
	 */
	@RequestMapping("logout.json")
	@ResponseBody
	public Result login(HttpSession session) {
		// 清除session
		session.invalidate();
		
		return Result.success();
	}



	/**
	 * 我的视频列表
	 *
	 * @param map
	 * @return
	 */
	@RequestMapping("media.action")
	public String listNormal(ModelMap map, HttpServletRequest request) throws QingException {

		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);

		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");

		String userToken = "";
		Cookie[] cookieArr = request.getCookies();
		if (cookieArr!=null && cookieArr.length>0) {
			for (int i=0; i<cookieArr.length; i++) {
				Cookie cookie = cookieArr[i];
				if ("userToken".equals(cookie.getName())) {
					try {
						userToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						break;
					}
					break;
				}
			}
		}

		if (StringUtils.isEmpty(userToken)) {
			// 跳转到登录页面
			return "portal/pc/template/" + templatePC + "/user/login_page";
		}

		// 判断session
		HttpSession session  = request.getSession();
		// 从session中取出用户身份信息
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");

		if (userInfo==null) {
			try {
				userInfo = userInfoService.getUserInfoByUserToken(userToken);
				// 将用户信息保存进session
				request.getSession().setAttribute("userInfo", userInfo);
			} catch (QingException e) {
				// 用户凭证是伪造的，跳转到登录页面
				return "portal/pc/template/" + templatePC + "/user/login_page";
			}
		}
		return "portal/pc/template/" + templatePC + "/user/list_media";
	}

	/**
	 * 播放记录
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("history.action")
	public String record(ModelMap map, HttpServletRequest request) throws UnsupportedEncodingException {
		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);
		
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");
		
		String userToken = "";
		Cookie[] cookieArr = request.getCookies();
		if (cookieArr!=null && cookieArr.length>0) {
			for (int i=0; i<cookieArr.length; i++) {
				Cookie cookie = cookieArr[i];
				if ("userToken".equals(cookie.getName())) {
					try {
						userToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						break;
					}
					break;
				}
			}
		}
		
		if (StringUtils.isEmpty(userToken)) {
			// 跳转到登录页面
			return "portal/pc/template/" + templatePC + "/user/login_page";
		}
		
		// 判断session
		HttpSession session  = request.getSession();
		// 从session中取出用户身份信息
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");
		
		if (userInfo==null) {
			try {
				userInfo = userInfoService.getUserInfoByUserToken(userToken);
				// 将用户信息保存进session
				request.getSession().setAttribute("userInfo", userInfo);
			} catch (QingException e) {
				// 用户凭证是伪造的，跳转到登录页面
				return "portal/pc/template/" + templatePC + "/user/login_page";
			}
		}
		
		return "portal/pc/template/" + templatePC + "/user/history";
	}
	
	/**
	 * 我的收藏
	 */
	@RequestMapping("fav.action")
	public String fav(ModelMap map, HttpServletRequest request) {
		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);

		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");

		String userToken = "";
		Cookie[] cookieArr = request.getCookies();
		if (cookieArr!=null && cookieArr.length>0) {
			for (int i=0; i<cookieArr.length; i++) {
				Cookie cookie = cookieArr[i];
				if ("userToken".equals(cookie.getName())) {
					try {
						userToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						break;
					}
					break;
				}
			}
		}

		if (StringUtils.isEmpty(userToken)) {
			// 跳转到登录页面
			return "portal/pc/template/" + templatePC + "/user/login_page";
		}

		// 判断session
		HttpSession session  = request.getSession();
		// 从session中取出用户身份信息
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");

		if (userInfo==null) {
			try {
				userInfo = userInfoService.getUserInfoByUserToken(userToken);
				// 将用户信息保存进session
				request.getSession().setAttribute("userInfo", userInfo);
			} catch (QingException e) {
				// 用户凭证是伪造的，跳转到登录页面
				return "portal/pc/template/" + templatePC + "/user/login_page";
			}
		}

		return "portal/pc/template/" + templatePC + "/user/fav";
	}
	
	/**
	 * 个人设置
	 */
	@RequestMapping("accountset.action")
	public String accountset(ModelMap map, HttpServletRequest request) {
		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);
		
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");
		
		String userToken = "";
		Cookie[] cookieArr = request.getCookies();
		if (cookieArr!=null && cookieArr.length>0) {
			for (int i=0; i<cookieArr.length; i++) {
				Cookie cookie = cookieArr[i];
				if ("userToken".equals(cookie.getName())) {
					try {
						userToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						break;
					}
					break;
				}
			}
		}
		
		if (StringUtils.isEmpty(userToken)) {
			// 跳转到登录页面
			return "portal/pc/template/" + templatePC + "/user/login_page";
		}
		
		// 判断session
		HttpSession session  = request.getSession();
		// 从session中取出用户身份信息
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");
		
		if (userInfo==null) {
			try {
				userInfo = userInfoService.getUserInfoByUserToken(userToken);
				// 将用户信息保存进session
				request.getSession().setAttribute("userInfo", userInfo);
			} catch (QingException e) {
				// 用户凭证是伪造的，跳转到登录页面
				return "portal/pc/template/" + templatePC + "/user/login_page";
			}
		}

		return "portal/pc/template/" + templatePC + "/user/personal/accountset";
	}
	
	/**
	 * 用户自己修改密码
	 * @param request
	 * @param passWord 新密码
	 * @param userToken 用户凭证
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("change_pass_word.json")
	@ResponseBody
	public Result changePassWord(
			HttpServletRequest request,
			@RequestParam(value="passWord") String passWord,
			@RequestParam(value="userToken") String userToken) throws Exception {
		
		if (StringUtils.isEmpty(userToken)) {
			throw new QingException(ErrorMsg.ERROR_100012);
		}
		
		if (StringUtils.isEmpty(passWord)) {
			throw new QingException(ErrorMsg.ERROR_100006);
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
		
		// 设置用户修改密码信息
		UserInfo userInfo2 = new UserInfo();
		userInfo2.setId(userInfo.getId());
		userInfo2.setPassWord(MD5.md5(passWord));
		
		userInfoService.changePassWord(userInfo2);
		
		return Result.success();
	}
	
	/**
	 * 用户换绑邮箱时，向旧邮箱发送验证码
	 * @param request
	 * @param userToken 用户凭证
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("send_email.json")
	@ResponseBody
	public Result sendEmail(
			HttpServletRequest request,
			@RequestParam(value="userToken") String userToken) throws Exception {
		
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

		userInfoService.sendEmail(userInfo, "换绑邮箱验证", "change_email");
		
		return Result.success();
	}
	
	/**
	 * 用户自己修改邮箱
	 * @param request
	 * @param email 新的邮箱地址
	 * @param identifyingCode 验证码
	 * @param userToken 用户凭证
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("change_email.json")
	@ResponseBody
	public Result changeEmail(
			HttpServletRequest request,
			@RequestParam(value="email") String email,
			@RequestParam(value="identifyingCode") String identifyingCode,
			@RequestParam(value="userToken") String userToken) throws Exception {
		
		// 校验邮箱
		if (StringUtils.isEmpty(email)) {
			throw new QingException(ErrorMsg.ERROR_100008);
		}
		
		// 校验验证码
		if (StringUtils.isEmpty(identifyingCode)) {
			throw new QingException(ErrorMsg.ERROR_100013);
		}
		
		// 校验用户凭证
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
		
		// 设置用户修改邮箱信息
		if (email.equals(userInfo.getEmail())) {
			throw new QingException(ErrorMsg.ERROR_100016);
		}
		
		UserInfo userInfo2 = new UserInfo();
		userInfo2.setId(userInfo.getId());
		userInfo2.setEmail(email);
		
		userInfoService.changeEmail(userInfo2, identifyingCode);
		
		// 重新将用户信息保存进session
		userInfo = userInfoService.getUserInfoByUserToken(userToken);
		request.getSession().setAttribute("userInfo", userInfo);
		
		return Result.success();
	}
	
	/**
	 * 用户找回密码，发送邮箱验证码
	 * @param email 用户填写的邮箱地址
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("find_pwd_email.json")
	@ResponseBody
	public Result findPwdEmail(@RequestParam(value="email") String email) throws Exception {
		
		userInfoService.findPwdEmail(email);
		
		return Result.success();
	}
	
	/**
	 * 用户找回密码，校验验证码
	 * @param email
	 * @param identifyingCode
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("find_pwd_code.json")
	@ResponseBody
	public Result findPwdCode(
			@RequestParam(value="email") String email,
			@RequestParam(value="identifyingCode") String identifyingCode) throws Exception {
		
		userInfoService.findPwdCode(email, identifyingCode);
		
		return Result.success();
	}
	
	/**
	 * 用户自己设置新的密码
	 * @param request
	 * @param email 邮箱地址
	 * @param identifyingCode 邮箱验证码
	 * @param passWord 新密码
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("set_new_pass_word.json")
	@ResponseBody
	public Result setNewPassWord(
			HttpServletRequest request,
			@RequestParam(value="email") String email,
			@RequestParam(value="identifyingCode") String identifyingCode,
			@RequestParam(value="passWord") String passWord) throws Exception {

		userInfoService.setNewPassWord(email, identifyingCode, passWord);
		
		return Result.success();
	}

	/**
	 * 跳转用户上传视频页面
	 * @return
	 */
	@RequestMapping("upload.action")
	public String toUploadMedia(HttpServletRequest request,ModelMap map){
		System.out.println("上传视频");
		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);

		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");

		String userToken = "";
		Cookie[] cookieArr = request.getCookies();
		if (cookieArr!=null && cookieArr.length>0) {
			for (int i=0; i<cookieArr.length; i++) {
				Cookie cookie = cookieArr[i];
				if ("userToken".equals(cookie.getName())) {
					try {
						userToken = URLDecoder.decode(cookie.getValue(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						break;
					}
					break;
				}
			}
		}

		if (StringUtils.isEmpty(userToken)) {
			// 跳转到登录页面
			return "portal/pc/template/" + templatePC + "/user/login_page";
		}

		// 判断session
		HttpSession session  = request.getSession();
		// 从session中取出用户身份信息
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");

		if (userInfo==null) {
			try {
				userInfo = userInfoService.getUserInfoByUserToken(userToken);
				// 将用户信息保存进session
				request.getSession().setAttribute("userInfo", userInfo);
			} catch (QingException e) {
				// 用户凭证是伪造的，跳转到登录页面
				return "portal/pc/template/" + templatePC + "/user/login_page";
			}
		}

		//获取视频类型
		List<TypeInfo> typeList = typeInfoService.list();
		map.put("typeList", typeList);
		return "portal/pc/template/" + templatePC + "/user/upload";// 站点信息

	}
}
