package com.ydy.sys.service.seo_info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy.sys.dao.seo_info.ISeoInfoDAO;
import com.ydy.sys.entity.SeoInfo;

@Service("SeoInfoService")
public class SeoInfoService {
	@Autowired
	private ISeoInfoDAO iSeoInfoDAO;

	/**
	 * 根据类型，查询seo信息
	 * @param type
	 * @return
	 */
	public SeoInfo selectByType(String type) {
		return iSeoInfoDAO.selectByType(type);
	}

	/**
	 * 保存seo信息配置
	 * @param seoInfo
	 */
	public void save(SeoInfo seoInfo) {
		iSeoInfoDAO.update(seoInfo);
	}

}
