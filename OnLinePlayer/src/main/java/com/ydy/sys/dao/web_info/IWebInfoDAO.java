package com.ydy.sys.dao.web_info;

import com.ydy.sys.entity.WebInfo;

public interface IWebInfoDAO {

	/**
	 * 查询网站信息
	 * @return
	 */
	WebInfo select();

	/**
	 * 保存站点信息配置
	 * @param webInfo
	 */
	int update(WebInfo webInfo);

}