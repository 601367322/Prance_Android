package com.gensee.rtdemo;

import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;

import com.gensee.callback.IChatCallBack;
import com.gensee.demo.R;
import com.gensee.room.RTRoom;
import com.gensee.room.RtSdk;
import com.gensee.routine.UserInfo;
import com.gensee.rtdemo.adapter.GridViewAvatarAdapter;
import com.gensee.rtdemo.adapter.OnChatAdapter;
import com.gensee.rtdemo.adapter.GridViewAvatarAdapter.SelectAvatarInterface;
import com.gensee.rtdemo.chat.PrivateChatManager;
import com.gensee.rtdemo.chat.PrivateChatMessage;
import com.gensee.rtdemo.chat.PublicChatManager;
import com.gensee.rtdemo.chat.PublicChatMessage;
import com.gensee.taskret.OnTaskRet;
import com.gensee.view.ChatEditText;

public class ChatActivity extends Activity implements OnClickListener,
		IChatCallBack, SelectAvatarInterface, OnTaskRet {

	private Button mSendmsgButton;
	private Button mExpressionButton;
	private Spinner mSpinner;
	private RtSdk mRtSdk;
	private String[] mNameString;
	private GridView mGridView;
	private GridViewAvatarAdapter mGridViewAvatarAdapter;
	private ChatEditText mChatEditText;
	private OnChatAdapter mOnChatAdapter;
	private ListView mContextListView;
	private List<UserInfo> mlList;
	private long mUserID = -1000;
	public Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			mOnChatAdapter.init(mUserID);

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		mSendmsgButton = (Button) findViewById(R.id.chat_sendmsg);
		mExpressionButton = (Button) findViewById(R.id.chat_expression);
		mSpinner = (Spinner) findViewById(R.id.chat_spininer);
		mGridView = (GridView) findViewById(R.id.chat_grid_view);
		mChatEditText = (ChatEditText) findViewById(R.id.chat_edittext);
		mContextListView = (ListView) findViewById(R.id.chat_context_listview);
		mOnChatAdapter = new OnChatAdapter(this);

		mGridViewAvatarAdapter = new GridViewAvatarAdapter(
				mGridView.getContext(), this);
		mGridView.setAdapter(mGridViewAvatarAdapter);
		mContextListView.setAdapter(mOnChatAdapter);

		mSendmsgButton.setOnClickListener(this);
		mExpressionButton.setOnClickListener(this);
		// mSpinner.setOnClickListener(this);

		mRtSdk = MutiVideoActivity.mRTSdk2;
		initSpinner();
		mRtSdk.setChatCallback(this);
		mSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position == 0) {
					mUserID = -1000;
				} else {
					mUserID = mlList.get(position - 1).getId();
				}
				mHandler.sendEmptyMessage(0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

	}

	public void initSpinner() {
		mlList = mRtSdk.getAllUsers();

		mNameString = new String[mlList.size() + 1];
		mNameString[0] = getBaseContext().getResources().getString(
				R.string.allname);
		for (int i = 0; i < mlList.size(); i++) {
			mNameString[i + 1] = mlList.get(i).getName();
		}
		ArrayAdapter<String> mNameAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, mNameString);
		mNameAdapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		mSpinner.setAdapter(mNameAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.chat_, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.getId() == R.id.chat_sendmsg) {
			String chatText = mChatEditText.getChatText();
			String richText = mChatEditText.getRichText();
			if (mUserID == -1000) {
				mRtSdk.chatWithPublic(chatText,richText, this);
			} else {
				mRtSdk.chatWithPersion(chatText,richText, mUserID, this);
			}
			
		} else if (v.getId() == R.id.chat_expression) {

			if (mGridView.getVisibility() == View.GONE) {
				mGridView.setVisibility(View.VISIBLE);
			} else if (mGridView.getVisibility() == View.VISIBLE) {
				mGridView.setVisibility(View.GONE);
			}
			if (mGridViewAvatarAdapter == null) {
				mGridViewAvatarAdapter = new GridViewAvatarAdapter(
						mGridView.getContext(), this);
				mGridView.setAdapter(mGridViewAvatarAdapter);
			} else {
				mGridViewAvatarAdapter.notifyDataSetChanged();
			}

		}
	}

	@Override
	public void onChatJoinConfirm(boolean bRet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChatWithPersion(UserInfo userInfo, String msg, String rich) {

		PrivateChatMessage message = new PrivateChatMessage();
		message.setText(msg);
		message.setTime(Calendar.getInstance().getTimeInMillis());
		message.setSendUserId(userInfo.getId());
		message.setRich(rich);
		message.setSendUserName(userInfo.getName());
		PrivateChatManager.getIns().addMsg(userInfo.getId(), message);
		if (mUserID == userInfo.getId()) {
			mHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void onChatWithPublic(UserInfo userInfo, String msg, String rich) {

		PublicChatMessage mPublicChatMessage = new PublicChatMessage();
		mPublicChatMessage.setText(msg);
		mPublicChatMessage.setRich(rich);
		mPublicChatMessage.setSendUserName(userInfo.getName());
		mPublicChatMessage.setTime(Calendar.getInstance().getTimeInMillis());
		PublicChatManager.getIns().addMsg(mPublicChatMessage);
		if (mUserID == -1000) {
			mHandler.sendEmptyMessage(0);
		}
	}

	@Override
	public void onChatToPersion(long userId, String msg, String rich) {
		// TODO Auto-generated method stub
		PrivateChatMessage message = new PrivateChatMessage();
		message.setText(msg);
		message.setTime(Calendar.getInstance().getTimeInMillis());
		message.setSendUserId(RTRoom.getIns().getUserId());
		message.setRich(rich);
		message.setReceiveUserId(userId);
		message.setSendUserName(mRtSdk.getSelfUserInfo().getName());
		PrivateChatManager.getIns().addMsg(userId, message);
		mHandler.sendEmptyMessage(0);
	}

	@Override
	public void onChatEnable(boolean enable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectAvatar(String sAvatar, Drawable resId) {
		// TODO Auto-generated method stub
//		mChatEditText.getText().insert(mChatEditText.getSelectionStart(),
//				SpanResource.convetToSpan(sAvatar.toString(),
//						this));
		mChatEditText.insertAvatar(sAvatar,0);
	}

	@Override
	public void onTaskRet(boolean ret, int id, String desc) {
		// TODO Auto-generated method stub

		mHandler.sendEmptyMessage(0);
	}
}
