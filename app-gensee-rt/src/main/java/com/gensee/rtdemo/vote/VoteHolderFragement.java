package com.gensee.rtdemo.vote;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gensee.callback.IVoteCallBack;
import com.gensee.room.RtSdk;
import com.gensee.routine.UserInfo;
import com.gensee.demo.R;
import com.gensee.vote.VoteGroup;

@SuppressLint("ValidFragment")
public class VoteHolderFragement extends Fragment implements IVoteCallBack {
	private RtSdk rtSdk;
	private VoteHostFragement hostFragement;
	private VoteReceiverFragement receiverFragement;
	private List<VoteGroup> voteList;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			UserInfo myUser = rtSdk.getSelfUserInfo();
			switch (msg.what) {

			case VoteStatus.VOTE_ADD:
				if (myUser.IsHost() || myUser.IsPanelist()) {
					voteList.add((VoteGroup) msg.obj);
					if (null != hostFragement) {
						hostFragement.notifyData(voteList);
					}
				}
				break;
			case VoteStatus.VOTE_DEL:
				VoteGroup rGroup = (VoteGroup) msg.obj;
				for (VoteGroup voteGroup : voteList) {
					if (voteGroup.getM_strId().equals(rGroup.getM_strId())) {
						voteList.remove(voteGroup);
						break;
					}
				}
				if (null != hostFragement) {
					hostFragement.notifyData(voteList);
				}
				break;
			case VoteStatus.VOTE_SUBMIT:
			case VoteStatus.VOTE_PUBLISH:
			case VoteStatus.VOTE_RESULT:
			case VoteStatus.VOTE_DEADLINE:
				VoteGroup rGroup1 = (VoteGroup) msg.obj;
				boolean bTrue = false;
				int i = 0;
				for (; i < voteList.size(); i++) {
					VoteGroup voteGroup = voteList.get(i);
					if (voteGroup.getM_strId().equals(rGroup1.getM_strId())) {
						bTrue = true;
						break;
					}

				}
				if (bTrue) {
					voteList.set(i, rGroup1);
				} else {
					voteList.add(rGroup1);
				}
				if (myUser.IsHost() || myUser.IsPanelist()) {
					if (null != hostFragement) {
						hostFragement.notifyData(voteList);
					}
				} else {
					if (null != receiverFragement) {
						receiverFragement.notifyData(voteList);
					}
				}
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public VoteHolderFragement(RtSdk rtSdk) {
		this.rtSdk = rtSdk;
		voteList = new ArrayList<VoteGroup>();
		hostFragement = new VoteHostFragement(rtSdk);
		receiverFragement = new VoteReceiverFragement(rtSdk);
	}

	public void clear() {
		voteList.clear();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.vote_holder_layout, null);
		UserInfo myUser = rtSdk.getSelfUserInfo();
		if (myUser.IsHost() || myUser.IsPanelist()) {
			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.vote_holder_container,
					hostFragement);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		} else {
			FragmentTransaction fragmentTransaction = getFragmentManager()
					.beginTransaction();
			fragmentTransaction.replace(R.id.vote_holder_container,
					receiverFragement);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
		return view;
	}

	@Override
	public void onVoteJoinConfirm(boolean bRet) {

	}

	@Override
	public void onVoteAdd(VoteGroup voteGroup) {
		Message mesage = new Message();
		mesage.what = VoteStatus.VOTE_ADD;
		mesage.obj = voteGroup;
		handler.sendMessage(mesage);
	}

	@Override
	public void onVoteDel(VoteGroup voteGroup) {
		Message mesage = new Message();
		mesage.what = VoteStatus.VOTE_DEL;
		mesage.obj = voteGroup;
		handler.sendMessage(mesage);
	}

	@Override
	public void onVotePublish(VoteGroup voteGroup) {
		Message mesage = new Message();
		mesage.what = VoteStatus.VOTE_PUBLISH;
		mesage.obj = voteGroup;
		handler.sendMessage(mesage);
	}

	@Override
	public void onVotePublishResult(VoteGroup voteGroup) {
		Message mesage = new Message();
		mesage.what = VoteStatus.VOTE_RESULT;
		mesage.obj = voteGroup;
		handler.sendMessage(mesage);
	}

	@Override
	public void onVoteSubmit(VoteGroup voteGroup) {
		Message mesage = new Message();
		mesage.what = VoteStatus.VOTE_SUBMIT;
		mesage.obj = voteGroup;
		handler.sendMessage(mesage);
	}

	@Override
	public void onVoteDeadline(VoteGroup voteGroup) {
		Message mesage = new Message();
		mesage.what = VoteStatus.VOTE_DEADLINE;
		mesage.obj = voteGroup;
		handler.sendMessage(mesage);
	}

	@Override
	public void onVotePostUrl(String strURL, long optUser) {

	}

	private class VoteStatus {
		public static final int VOTE_ADD = 1000;
		public static final int VOTE_DEL = VOTE_ADD + 1;
		public static final int VOTE_PUBLISH = VOTE_ADD + 2;
		public static final int VOTE_RESULT = VOTE_ADD + 3;
		public static final int VOTE_DEADLINE = VOTE_ADD + 4;
		public static final int VOTE_SUBMIT = VOTE_ADD + 5;

	}

	@Override
	public void onCardEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCardResultPublish(int arg0, int arg1, int[] arg2, int[] arg3) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onCardSubmit(long arg0, int[] arg1) {
		// TODO Auto-generated method stub
		
	}
	
	public void onCardPublish(com.gensee.card.Card arg0) {};
}
