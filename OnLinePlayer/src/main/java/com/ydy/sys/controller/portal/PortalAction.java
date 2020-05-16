package com.ydy.sys.controller.portal;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ydy.common.exception.QingException;
import com.ydy.sys.entity.ChannelInfo;
import com.ydy.sys.entity.NavInfo;
import com.ydy.sys.entity.SeoInfo;
import com.ydy.sys.entity.TypeInfo;
import com.ydy.sys.entity.UserInfo;
import com.ydy.sys.entity.VideoInfo;
import com.ydy.sys.entity.WebInfo;
import com.ydy.sys.service.channel_info.ChannelInfoService;
import com.ydy.sys.service.media_info.MediaInfoService;
import com.ydy.sys.service.nav_info.NavInfoService;
import com.ydy.sys.service.seo_info.SeoInfoService;
import com.ydy.sys.service.template_info.TemplateInfoService;
import com.ydy.sys.service.type_info.TypeInfoService;
import com.ydy.sys.service.user_info.UserInfoService;
import com.ydy.sys.service.video_info.VideoInfoService;
import com.ydy.sys.service.web_info.WebInfoService;

@Controller
@RequestMapping("portal")
public class PortalAction {

	@Autowired
	private VideoInfoService videoInfoService;
	@Autowired
	private MediaInfoService mediaInfoService;
	@Autowired
	private NavInfoService navInfoService;
	@Autowired
	private ChannelInfoService channelInfoService;
	@Autowired
	private WebInfoService webInfoService;
	@Autowired
	private SeoInfoService seoInfoService;
	@Autowired
	private UserInfoService userInfoService;
	@Autowired
	private TemplateInfoService templateInfoService;
	@Autowired
	private TypeInfoService typeInfoService;
	
	/**
	 * 跳转首页
	 * @return
	 */
	@RequestMapping("index.action")
	public String index(ModelMap map, HttpServletRequest request) {
		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);
		// 网站首页seo
		SeoInfo seoInfo = seoInfoService.selectByType("index");
		map.put("seoInfo", seoInfo);
		
		// 获取用户信息，如果存在用户信息，直接登录
		UserInfo userInfo = userInfoService.getUserInfo(request);
		map.put("userInfo", userInfo);
		
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");		
		// 获取可用导航
		/*List<NavInfo> navlist = navInfoService.listIsUse();
		map.put("navlist", navlist);
		
		for (NavInfo navInfo : navlist) {
			// 判断是否是首页
			if ("1".equals(navInfo.getIsIndex())) {
				// 判断该链接是否是系统内置的（或者是频道）
				if ("system".equals(navInfo.getType())) {
					// 系统默认首页
					if ("portal/index.action".equals(navInfo.getLink())) {
						map.put("active", navInfo.getLink());
						return "portal/pc/template/" + templatePC + "/index";
					} else {
						// 频道
						String channelId = navInfo.getChannelId();
						map.put("active", "portal/portal.action?channelId="+channelId);
						ChannelInfo channelInfo = channelInfoService.selectById(channelId);
						return "portal/pc/template/" + templatePC + "/channel/" + channelInfo.getTemplate();
					}
				} else {
					// 自定义链接
					map.put("active", navInfo.getLink());
					return "redirect:"+navInfo.getLink();
				}
			}
		}*/

		//获取视频分类
		List<TypeInfo> list = typeInfoService.list();
		map.put("typelist", list);
		return "portal/pc/template/" + templatePC + "/index";
	}
	
	/**
	 * 跳转频道页面
	 * @return
	 */
	@RequestMapping("portal.action")
	public String portal(ModelMap map,
			HttpServletRequest request,
			@RequestParam(value="channelId") String channelId) {
		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);
		
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");

		// 判断合法性
		try {
			Integer.parseInt(channelId);
		} catch (Exception e) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		ChannelInfo channelInfo = channelInfoService.selectById(channelId);
		if (channelInfo==null) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		map.put("channelInfo", channelInfo);
		
		// 获取用户信息
		UserInfo userInfo = userInfoService.getUserInfo(request);
		map.put("userInfo", userInfo);
		
		// 获取可用导航
		List<NavInfo> navlist = navInfoService.listIsUse();
		map.put("navlist", navlist);

		map.put("active", "portal.action?channelId="+channelId);
		
		return "portal/pc/template/" + templatePC + "/channel/" + channelInfo.getTemplate();
	}
	
	/**
	 * 跳转到播放页面
	 * @param videoId 视频主键
	 * @return
	 */
	@RequestMapping("play.action")
	public String play(ModelMap map,
			HttpServletRequest request,
			@RequestParam(value="videoId") String videoId) {
		// 站点信息 
		WebInfo webInfo = webInfoService.select();//这个是写死
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");//只有一个目前
		// 判断合法性
		try {
			/*Integer.parseInt(videoId);*/
		} catch (Exception e) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		// 获取该视频的信息
		VideoInfo videoInfo = videoInfoService.selectByIdWithPortal(videoId);
		System.err.println(videoInfo);
		if (videoInfo==null) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		if ("0".equals(videoInfo.getStatus())) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		map.put("videoInfo", videoInfo);
		
		// 视频播放页seo
		SeoInfo seoInfo = seoInfoService.selectByType("play");
		map.put("seoInfo", seoInfo);
		
		// 获取用户信息
		UserInfo userInfo = userInfoService.getUserInfo(request);
		map.put("userInfo", userInfo);
		
		// 获取可用导航
		List<NavInfo> navlist = navInfoService.listIsUse();
		map.put("navlist", navlist);
		
		// 根据主键，获取媒体信息
		Map<String, Object> mediaInfo = null;
		try {
			mediaInfo = mediaInfoService.selectByMediaId(videoInfo.getMediaId());
			String url = mediaInfo.get("url").toString();
			//String uri = url.replace("D:Videos", "D:\\Videos\\");
			mediaInfo.put("url", url);
			map.put("mediaInfo", mediaInfo);
		} catch (QingException e) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		
		// 电脑端获取播放页模板
		//TypeInfo typeInfo = typeInfoService.selectById(mediaInfo.get("type_id").toString());
		map.put("videoId", videoId);
		return "portal/pc/template/" + templatePC + "/play/movie";
	}
	/**
	 * 跳转到播放页面
	 * @param mediaId 视频主键
	 * @return
	 */
	@RequestMapping("play2.action")
	public String play2(ModelMap map,
					   HttpServletRequest request,
					   @RequestParam(value="mediaId") String mediaId) {
		// 站点信息
		WebInfo webInfo = webInfoService.select();//这个是写死
		map.put("webInfo", webInfo);
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");//只有一个目前
		// 判断合法性
		try {
			/*Integer.parseInt(videoId);*/
		} catch (Exception e) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		// 获取该视频的信息
		VideoInfo videoInfo = videoInfoService.selectVideoByMediaId(mediaId);
		System.err.println(videoInfo);
		if (videoInfo==null) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		if ("0".equals(videoInfo.getStatus())) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		map.put("videoInfo", videoInfo);

		// 视频播放页seo
		SeoInfo seoInfo = seoInfoService.selectByType("play");
		map.put("seoInfo", seoInfo);

		// 获取用户信息
		UserInfo userInfo = userInfoService.getUserInfo(request);
		map.put("userInfo", userInfo);

		// 获取可用导航
		List<NavInfo> navlist = navInfoService.listIsUse();
		map.put("navlist", navlist);

		// 根据主键，获取媒体信息
		Map<String, Object> mediaInfo = null;
		try {
			mediaInfo = mediaInfoService.selectByMediaId(videoInfo.getMediaId());
			String url = mediaInfo.get("url").toString();
			//String uri = url.replace("D:Videos", "D:\\Videos\\");
			mediaInfo.put("url", url);
			map.put("mediaInfo", mediaInfo);
		} catch (QingException e) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}

		// 电脑端获取播放页模板
		//TypeInfo typeInfo = typeInfoService.selectById(mediaInfo.get("type_id").toString());
		map.put("videoId", videoInfo.getVideoId());
		//page\admin\media_info\movie.jsp
		return "portal/pc/template/" + templatePC + "/play/movie";
	}
	/**
	 * 跳转到媒体详情页面
	 * @param mediaId 视频主键
	 * @return
	 */
	@RequestMapping("profile.action")
	public String profile(ModelMap map,
			HttpServletRequest request,
			@RequestParam(value="mediaId") String mediaId) {
		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);
		
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");
		
		// 根据主键，获取媒体信息
		Map<String, Object> mediaInfo = null;
		try {
			mediaInfo = mediaInfoService.selectByMediaId(mediaId);
			map.put("mediaInfo", mediaInfo);
		} catch (QingException e) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		
		// 网站首页seo
		SeoInfo seoInfo = seoInfoService.selectByType("profile");
		map.put("seoInfo", seoInfo);
		
		// 获取用户信息
		UserInfo userInfo = userInfoService.getUserInfo(request);
		map.put("userInfo", userInfo);
		
		// 获取可用导航
		List<NavInfo> navlist = navInfoService.listIsUse();
		map.put("navlist", navlist);
		
		// 电脑端获取详情页模板
		TypeInfo typeInfo = typeInfoService.selectById(mediaInfo.get("type_id").toString());
		String profileTemplate = typeInfo.getProfileTemplate();
		if (StringUtils.isEmpty(profileTemplate)) {
			// 404（该分类没有详情页面）
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		
		map.put("mediaId", mediaId);
		
		return "portal/pc/template/" + templatePC + "/profile/" + profileTemplate;
	}
	
	/**
	 * 跳转到搜索结果页面
	 * @param keyWord 视频主键
	 * @return
	 */
	@RequestMapping("search.action")
	public String search(ModelMap map,
			HttpServletRequest request,
			@RequestParam(value="keyWord") String keyWord) {
		// 站点信息
		WebInfo webInfo = webInfoService.select();//百度
		map.put("webInfo",webInfo );
		// 获取用户信息
		UserInfo userInfo = userInfoService.getUserInfo(request);
		map.put("userInfo", userInfo);
		
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");
		
		// 获取可用导航
		List<NavInfo> navlist = navInfoService.listIsUse();
		map.put("navlist", navlist);
		
		map.put("keyWord", keyWord);
		return "portal/pc/template/" + templatePC + "/search/search";
	}
	
	/**
	 * 跳转到分类检索页面
	 * @param typeId 视频主键
	 * @return
	 */
	@RequestMapping("list.action")
	public String list(ModelMap map,
			HttpServletRequest request,
			@RequestParam(value="typeId") String typeId,
			@RequestParam(required=false, value="name") String fieldName,
			@RequestParam(required=false, value="value") String fieldValue) {
		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);
		
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");
		
		// 判断合法性
		try {
			Integer.parseInt(typeId);
		} catch (Exception e) {
			// 404
			return "portal/pc/template/" + templatePC + "/error/404";
		}
		// 分类检索页seo
		SeoInfo seoInfo = seoInfoService.selectByType("list");
		map.put("seoInfo", seoInfo);
		
		// 获取用户信息
		UserInfo userInfo = userInfoService.getUserInfo(request);
		map.put("userInfo", userInfo);
		
		// 获取可用导航
		List<NavInfo> navlist = navInfoService.listIsUse();
		map.put("navlist", navlist);
		
		map.put("typeId", typeId);
		map.put("fieldName", fieldName);
		map.put("fieldValue", fieldValue);
		
		return "portal/pc/template/" + templatePC + "/list/list";
	}
	
	/**
	 * 跳转注册页面
	 * @return
	 */
	@RequestMapping("register.action")
	public String register(ModelMap map) {
		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);
		
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");
		
		// 获取可用导航
		List<NavInfo> navlist = navInfoService.listIsUse();
		map.put("navlist", navlist);
		
		return "portal/pc/template/" + templatePC + "/user/register_page";
	}
	
	/**
	 * 打开登录弹出层
	 * @return
	 */
	@RequestMapping("login.action")
	public String login() {
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");
		
		return "portal/pc/template/" + templatePC + "/user/login";
	}
	
	/**
	 * 跳转登录页面
	 * @return
	 */
	@RequestMapping("login_page.action")
	public String loginPage(ModelMap map) {
		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);
		
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");
		
		return "portal/pc/template/" + templatePC + "/user/login_page";
	}
	
	/**
	 * 跳转到找回密码页面
	 * @return
	 */
	@RequestMapping("find_pwd.action")
	public String findPwd(ModelMap map) {
		// 站点信息
		WebInfo webInfo = webInfoService.select();
		map.put("webInfo", webInfo);
		
		// 获取所选模板
		String templatePC = templateInfoService.selectNameByType("pc");
		
		return "portal/pc/template/" + templatePC + "/user/find_pwd";
	}
}
