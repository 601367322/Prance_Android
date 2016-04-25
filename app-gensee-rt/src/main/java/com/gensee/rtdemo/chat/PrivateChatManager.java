package com.gensee.rtdemo.chat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gensee.user.UserManager;

public class PrivateChatManager extends AbstractChatManager {
	private static PrivateChatManager privateChatManager = null;

	// 维护谁与我聊天的列表
	private Map<Long, PrivateChatMessageDetail> privateChatMapList;

	private class PrivateChatMessageDetail {
		private List<AbsChatMessage> msgList;
		private long nSendMsgCount = -1;

		public void addMsg(AbsChatMessage msg) {
			msgList.add(msg);
		}

		public void addMsgCount() {
			if (nSendMsgCount < 0) {
				nSendMsgCount++;
			}
			nSendMsgCount++;
		}

		public PrivateChatMessageDetail() {
			msgList = new ArrayList<AbsChatMessage>();
		}

		public void clearAll() {
			if (null != msgList) {
				msgList.clear();
			}
		}

		private PrivateChatMessageDetail cloneData() {
			PrivateChatMessageDetail detail = new PrivateChatMessageDetail();
			detail.nSendMsgCount = nSendMsgCount;
			detail.msgList.addAll(msgList);
			return detail;
		}

		public List<AbsChatMessage> getMsgList() {
			return msgList;
		}

		public void setMsgList(List<AbsChatMessage> msgList) {
			this.msgList = msgList;
		}

		public long getnSendMsgCount() {
			return nSendMsgCount;
		}

		public void setnSendMsgCount(long nSendMsgCount) {
			this.nSendMsgCount = nSendMsgCount;
		}

	}

	private PrivateChatManager() {
		super();
		privateChatMapList = new HashMap<Long, PrivateChatMessageDetail>();
	}

	public static PrivateChatManager getIns() {
		synchronized (PrivateChatManager.class) {
			if (null == privateChatManager) {
				privateChatManager = new PrivateChatManager();
			}
		}
		return privateChatManager;
	}

	/*
	 * userId:与我私聊的userID
	 */
	public void addMsg(long userId, AbsChatMessage msg) {
		mLock.writeLock().lock();
		try {
			PrivateChatMessageDetail msgDetail = privateChatMapList.get(userId);
			if (null == msgDetail) {
				msgDetail = new PrivateChatMessageDetail();
				privateChatMapList.put(userId, msgDetail);
			}
			msgDetail.addMsg(msg);

			if (msg.getSendUserId() == userId)// 其他人发给我的
			{
				msgDetail.addMsgCount();
			}
		} finally {
			mLock.writeLock().unlock();
		}
	}

	// 获取与我聊天的用户列表 userID:为当前选择聊天的用户ID
	public List<Long> getPrivateUserIdList(long userId) {
		List<Long> returnList = new ArrayList<Long>();
		returnList.add(userId);

		mLock.readLock().lock();

		try {
			for (Long id : privateChatMapList.keySet()) {
				if ((id != userId)
						&& (null != UserManager.getIns().getUserByUserId(id))) {
					PrivateChatMessageDetail tmp = privateChatMapList.get(id);
					if (tmp.getnSendMsgCount() >= 0) {
						returnList.add(id);
					}
				}
			}
		} finally {
			mLock.readLock().unlock();
		}
		return returnList;
	}

	public void resetNewmsgCount(long userId) {
		mLock.readLock().lock();
		try {
			PrivateChatMessageDetail detail = privateChatMapList.get(Long
					.valueOf(userId));
			if (null != detail) {
				detail.setnSendMsgCount(0);
			}
		} finally {
			mLock.readLock().unlock();
		}
	}

	public void clearAllByUserId(long userId) {
		mLock.writeLock().lock();
		try {
			PrivateChatMessageDetail detail = privateChatMapList.get(userId);
			if (null != detail) {
				detail.clearAll();
			}
		} finally {
			mLock.writeLock().unlock();
		}
	}

	public long getNewMsgCount(long userId) {
		long nCount = 0;
		mLock.readLock().lock();
		try {
			PrivateChatMessageDetail detail = privateChatMapList.get(Long
					.valueOf(userId));
			if (null != detail) {
				nCount = detail.getnSendMsgCount();
			}
		} finally {
			mLock.readLock().unlock();
		}
		return nCount;
	}

	// 获取聊天信息列表
	public List<AbsChatMessage> getMsgListByUserId(long userId) {
		List<AbsChatMessage> returnList = null;
		mLock.readLock().lock();
		try {
			returnList = new ArrayList<AbsChatMessage>();
			PrivateChatMessageDetail detail = privateChatMapList.get(userId);
			if (null != detail) {
				returnList.addAll(detail.getMsgList());
			}
		} finally {
			mLock.readLock().unlock();
		}
		return returnList;

	}
}
