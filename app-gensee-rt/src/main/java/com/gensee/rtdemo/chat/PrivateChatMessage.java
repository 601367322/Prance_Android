package com.gensee.rtdemo.chat;



public class PrivateChatMessage extends AbsChatMessage {
	private long receiveUserId;

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivateChatMessage other = (PrivateChatMessage) obj;
		if (receiveUserId != other.receiveUserId)
			return false;
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result
				+ (int) (receiveUserId ^ (receiveUserId >>> 32));
		return result;
	}

	public long getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(long receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

}