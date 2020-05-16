package com.ydy.sys.service.group_info;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ydy.common.constan.ErrorMsg;
import com.ydy.common.exception.QingException;
import com.ydy.sys.dao.group_info.IGroupInfoDAO;
import com.ydy.sys.dao.user_profile_info.IUserProfileInfoDAO;
import com.ydy.sys.entity.GroupInfo;

@Service("GroupInfoService")
public class GroupInfoService {
	@Autowired
	private IGroupInfoDAO iGroupInfoDAO;
	@Autowired
	private IUserProfileInfoDAO iUserProfileInfoDAO;
	
	/**
	 * 查询用户组列表
	 */
	public List<GroupInfo> list() {
		return iGroupInfoDAO.list();
	}

	/**
	 * 保存用户组
	 * @param groupInfoList
	 * @throws QingException
	 */
	public void save(List<GroupInfo> groupInfoList) throws QingException {
		//遍历用户数组
		for (GroupInfo groupInfo : groupInfoList) {

			if (StringUtils.isEmpty(groupInfo.getId())) {//用户id不存在，表示新用户类型
				// 插入
				// 判断组名是否重复
				int count = iGroupInfoDAO.countByName(groupInfo.getName(), null);
				if (count>0) {
					throw new QingException(ErrorMsg.ERROR_110001);
				}
				//设置为非系统用户类型
				groupInfo.setType("user");
				iGroupInfoDAO.insert(groupInfo);
			} else {//用户id存在，修改用户类型
				// 更新
				// 判断组名是否重复（排除自己）
				int count = iGroupInfoDAO.countByName(groupInfo.getName(), groupInfo.getId());
				if (count>0) {
					throw new QingException(ErrorMsg.ERROR_110001);
				}
				iGroupInfoDAO.update(groupInfo);
			}
		}
	}

	/**
	 * 删除用户组
	 * @param idArr 用户组主键数组
	 * @throws QingException 
	 */
	public void delete(String[] idArr) throws QingException {
		// 判断所选用户组有没有被使用的
		for (int i=0; i<idArr.length; i++) {
			int count = iUserProfileInfoDAO.countByGroupId(idArr[i]);
			if (count>0) {
				throw new QingException(ErrorMsg.ERROR_110002);
			}
		}
		
		// 批量删除用户组
		iGroupInfoDAO.delete(idArr);
	}

	/**
	 * 查询用户权限值
	 * @param id
	 * @return
	 */
	public String selectPowerByUserId(String id) {
		return iGroupInfoDAO.selectPowerByUserId(id);
	}

	/**
	 * 查询该用户组的视频空间大小
	 * @param groupId
	 * @return
	 */
	public double selectSizeByGroupId(String groupId) {
	    return  iGroupInfoDAO.selectSizeByGroupId(groupId);
	}
}
