package com.gensee.rtdemo.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gensee.demo.R;
import com.gensee.rtdemo.chat.AbsChatMessage;
import com.gensee.rtdemo.chat.PrivateChatManager;
import com.gensee.rtdemo.chat.PublicChatManager;
import com.gensee.utils.GenseeLog;
import com.gensee.view.MyTextViewEx;

public class OnChatAdapter extends AbstractAdapter<AbsChatMessage> {

	private List<AbsChatMessage> mList = new ArrayList<AbsChatMessage>();
	private Context mContext;
	private TextView mSendNameText;
	private TextView mTimetext;
	private MyTextViewEx mViewContextText;
	private long mUserId;

	public OnChatAdapter(Context context) {
		super(context);
		mList = new ArrayList<AbsChatMessage>();
		mContext = context;

	}

	public void init(long mUserID) {
		this.mUserId = mUserID;
		if (mUserID == -1000) {
			mList = new ArrayList<AbsChatMessage>();
			mList = PublicChatManager.getIns().getMsgList();
		} else {
			mList = new ArrayList<AbsChatMessage>();
			mList = PrivateChatManager.getIns().getMsgListByUserId(mUserId);
		}
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		
		return null;
	}

	@Override
	public long getItemId(int position) {
		
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		AbstractViewHolder mAbstractViewHolder = null;

		convertView = createView();
		mAbstractViewHolder = createViewHolder(convertView);
		convertView.setTag(mAbstractViewHolder);

		mAbstractViewHolder.init(position);

		return convertView;
	}

	@Override
	protected View createView() {
		
		View v;
		LayoutInflater mLayoutInflater = LayoutInflater.from(mContext);
		v = mLayoutInflater.inflate(R.layout.chat_listitem_layout, null);
		return v;
	}

	@Override
	protected AbstractViewHolder createViewHolder(View view) {
		
		ViewHolder mViewHolder = new ViewHolder(view);
		return mViewHolder;

	}

	protected class ViewHolder extends AbstractViewHolder {

		public ViewHolder(View currView) {
			mViewContextText = (MyTextViewEx) currView
					.findViewById(R.id.chatcontexttextview);
			mTimetext = (TextView) currView.findViewById(R.id.chattimetext);
			mSendNameText = (TextView) currView.findViewById(R.id.chatnametext);

		}

		@Override
		public void init(int position) {
			
			long mSendTime = mList.get(position).getTime();
			mSendNameText.setText(mList.get(position).getSendUserName());
			mTimetext.setText(String.format("%02d",
					(mSendTime / 3600 % 24 + 8) % 24)
					+ ":"
					+ String.format("%02d", mSendTime % 3600 / 60)
					+ ":"
					+ String.format("%02d", mSendTime % 3600 % 60));
			mViewContextText.setRichText(mList.get(position).getRich());
		}
	}

}