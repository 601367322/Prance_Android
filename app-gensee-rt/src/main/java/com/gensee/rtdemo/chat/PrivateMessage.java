package com.gensee.rtdemo.chat;

public class PrivateMessage extends AbsChatMessage {
	protected String mReceiveName;
	protected long receiveUserId;

	public String getReceiveName() {
		return mReceiveName;
	}

	public void setReceiveName(String mReceiveName) {
		this.mReceiveName = mReceiveName;
	}

	public long getReceiveUserId() {
		return receiveUserId;
	}

	public void setReceiveUserId(long receiveUserId) {
		this.receiveUserId = receiveUserId;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		PrivateMessage other = (PrivateMessage) obj;

		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();

		return result;
	}

}
