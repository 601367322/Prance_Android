package com.gensee.rtdemo.chat;

public class SysMessage extends AbsChatMessage {

	public long getSendUserId() {
		return sendUserId;
	}

	public void setSendUserId(long sendUserId) {
		this.sendUserId = sendUserId;
	}
}