package com.ydy.sys.service.collection_info;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy.sys.dao.collection_info.ICollectionInfoDAO;
import com.ydy.sys.dao.video_info.IVideoInfoDAO;
import com.ydy.sys.entity.CollectionInfo;
import com.ydy.sys.entity.VideoInfo;

@Service("CollectionInfoService")
public class CollectionInfoService {
	@Autowired
	private ICollectionInfoDAO iCollectionInfoDAO;
	
	/**
	 * 收藏
	 * @param collectionInfo
	 */
	public void save(CollectionInfo collectionInfo) {
		
			// 添加收藏
			iCollectionInfoDAO.insert(collectionInfo);
	}

	/**
	 * 获取用户的视频收藏列表
	 * @param userId 用户id
	 * @return
	 */
	public List<Map<String, Object>> listCollection(String userId) {
		
		 List<Map<String, Object>> listCollection = iCollectionInfoDAO.listCollection(userId);
		 return listCollection;
	}

	/**
	 * 判断当前视频是否已被用户收藏过了
	 * @param mediaId 媒体主键
	 * @param userId 用户id
	 * @return
	 */
	public int countByMediaIdAndUserId(String mediaId, String videoId, String userId) {
		return iCollectionInfoDAO.countByMediaIdAndUserId(mediaId, videoId,userId);
	}

	/**
	 * 删除收藏的视频
	 * @param collectionInfo
	 */
	public void delete(CollectionInfo collectionInfo) {
		iCollectionInfoDAO.delete(collectionInfo);
	}

}
