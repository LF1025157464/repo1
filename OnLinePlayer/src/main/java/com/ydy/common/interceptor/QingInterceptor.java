package com.ydy.common.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ydy.sys.entity.UserInfo;



/**
 * 拦截器
 */
public class QingInterceptor implements HandlerInterceptor {

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		// 获取请求的url
		String url = request.getRequestURI();
		
		// 放行链接
		if (url.indexOf("login")>=0
				|| url.indexOf("portal")>=0
				|| url.indexOf("api/")>=0) {
			return true;
		}
		
		// 判断session
		HttpSession session = request.getSession();
		// 从session中取出用户身份信息
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");

		if (userInfo!=null && "会员用户".equals(userInfo.getGroupName())) {
			return true;
		}

		// session存在并且是管理员时，放行
		if (userInfo!=null && "管理员".equals(userInfo.getGroupName())) {
			return true;
		}
		
		// 执行这里表示用户身份需要认证，跳转登陆页面
		request.getRequestDispatcher("/WEB-INF/page/admin/login.jsp").forward(request, response);
		
		return false;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		
	}

	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		
	}

}
