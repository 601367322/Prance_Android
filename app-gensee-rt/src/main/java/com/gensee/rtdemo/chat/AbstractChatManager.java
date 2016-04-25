package com.gensee.rtdemo.chat;

import java.util.concurrent.locks.ReentrantReadWriteLock;

public abstract class AbstractChatManager
{
	protected boolean chatStatus;
	protected ReentrantReadWriteLock mLock;
	protected boolean bEnable = true;   //FALSE 禁言

	public AbstractChatManager()
	{
		mLock = new ReentrantReadWriteLock();
	}

	public boolean isChatStatus() {
		return chatStatus;
	}

	public void setChatStatus(boolean chatStatus) {
		this.chatStatus = chatStatus;
	}

	public boolean isbEnable() {
		return bEnable;
	}

	public void setbEnable(boolean bEnable) {
		this.bEnable = bEnable;
	}
	
	
}
