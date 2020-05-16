package com.ydy.sys.service.reply_info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ydy.sys.dao.reply_info.IReplyInfoDAO;
import com.ydy.sys.entity.ReplyInfo;

@Service("ReplyInfoService")
public class ReplyInfoService {
	@Autowired
	private IReplyInfoDAO iReplyInfoDAO;

	/**
	 * 保存用户的回复
	 * @param replyInfo
	 */
	public void save(ReplyInfo replyInfo) {
		iReplyInfoDAO.insert(replyInfo);
	}

}
