package com.gensee.rtdemo.chat;

import java.util.ArrayList;
import java.util.List;

public class ChatManager extends AbstractChatManager {

	private List<AbsChatMessage> chatMsgList;

	public ChatManager() {
		chatMsgList = new ArrayList<AbsChatMessage>();
	}

	public void addMsg(AbsChatMessage msg) {
		mLock.writeLock().lock();
		try {
			chatMsgList.add(msg);
		} finally {
			mLock.writeLock().unlock();
		}
	}

	public List<AbsChatMessage> getMsgList() {

		List<AbsChatMessage> returnList = null;
		mLock.readLock().lock();
		try {
			returnList = new ArrayList<AbsChatMessage>(chatMsgList);
		} finally {
			mLock.readLock().unlock();
		}

		return returnList;
	}

	public void clearAll() {
		mLock.writeLock().lock();
		try {
			chatMsgList.clear();
		} finally {
			mLock.writeLock().unlock();
		}
	}
}
