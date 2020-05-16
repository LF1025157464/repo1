package com.ydy.sys.service.user_profile_info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy.sys.dao.user_profile_info.IUserProfileInfoDAO;
import com.ydy.sys.entity.UserProfileInfo;

@Service("UserProfileInfo")
public class UserProfileInfoService {

	@Autowired
	private IUserProfileInfoDAO iUserProfileInfoDAO;

	/**
	 * 保存用户扩展信息
	 * @param userProfileInfo
	 */
	public void save(UserProfileInfo userProfileInfo) {
		iUserProfileInfoDAO.update(userProfileInfo);
	}

}
