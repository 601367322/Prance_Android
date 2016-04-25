package com.gensee.rtdemo.chat;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gensee.demo.R;
import com.gensee.room.RTRoom;
import com.gensee.room.RtSdk;
import com.gensee.routine.UserInfo;
import com.gensee.rtdemo.adapter.GridViewAvatarAdapter;
import com.gensee.rtdemo.adapter.GridViewAvatarAdapter.SelectAvatarInterface;
import com.gensee.user.UserManager;
import com.gensee.view.ChatEditText;
import com.gensee.view.MyTextViewEx;

public class PublicChatHolder implements OnClickListener, SelectAvatarInterface {
	protected static final String TAG = "PublicChatHolder";
	private RelativeLayout rlPublicChat;
	private ImageView ivPublicChatHor;
	private LinearLayout lySysMsg;
	private TextView tvSysMsg;
	private ListView lvPublicChat;
	private PublicChatAdapter adapter;

	protected ImageView ivAvatar;
	protected ChatEditText edtChatContent;
	protected Button btnChatSend;

	protected GridView gdAvatar;
	protected GridViewAvatarAdapter gVAdapter;

	private View rootView;
	private RtSdk rtSdk;

	public PublicChatHolder(View rootView, RtSdk rtSdk) {
		this.rtSdk = rtSdk;
		this.rootView = rootView;
		initComp();
	}

	// 显示系统消息
	// public void setSysMsg(String message) {
	// Resources res = getContext().getResources();
	// lySysMsg.setVisibility(View.VISIBLE);
	// message += "    ";
	// String sysTip = res.getString(R.string.public_chat_sys_tip);
	// String msgContent = message;
	//
	// SpannableStringBuilder span = new SpannableStringBuilder();
	// span.append(sysTip);
	// span.append(msgContent);
	//
	// int sysTipLength = sysTip.length();
	// span.setSpan(
	// new ForegroundColorSpan(res
	// .getColor(R.color.public_chat_system_tip)), 0,
	// sysTipLength, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
	//
	// int contentLength = msgContent.length();
	// span.setSpan(
	// new ForegroundColorSpan(res
	// .getColor(R.color.public_chat_other_tip)),
	// sysTipLength, sysTipLength + contentLength,
	// Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
	//
	// String html = "<img src='iv_left'/>";
	// CharSequence richText = Html.fromHtml(html, assetsImageGetter, null);
	// span.append(richText);
	//
	// int start = sysTipLength + contentLength;
	// span.setSpan(new CustomUrlSpan(), start - 1, start,
	// Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
	//
	// tvSysMsg.setText(span);
	// tvSysMsg.setMovementMethod(LinkMovementMethod.getInstance());
	// }

	public void notifyData() {
		if (null != adapter) {
		//	adapter.setMsgList(PublicChatManager.getIns().getMsgList());
		}
	}

	private void pbulicChatSend() {
		String chatText = edtChatContent.getChatText();
		String richText = edtChatContent.getRichText();
		rtSdk.chatWithPublic(chatText, richText, null);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_public_chat:
		case R.id.edt_public_chat_content:
			gdAvatar.setVisibility(View.GONE);
			break;
		case R.id.iv_public_chat_avatar:
			showAvatar();
			break;
		case R.id.btn_public_chat_send:
			pbulicChatSend();
			break;
		case R.id.iv_public_chat_hor:
			// ivPublicChatHor.setSelected(!ivPublicChatHor.isSelected());
			// if (ivPublicChatHor.isSelected()) {
			// ivPublicChatHor.setSelected(false);
			// RelativeLayout.LayoutParams chatParam = (LayoutParams) rootView
			// .getLayoutParams();
			// chatParam.rightMargin = -(rootView.getWidth() - ivPublicChatHor
			// .getWidth());
			// rootView.setLayoutParams(chatParam);
			// } else {
			//
			// ivPublicChatHor.setSelected(true);
			// RelativeLayout.LayoutParams chatParam = (LayoutParams) rootView
			// .getLayoutParams();
			// chatParam.rightMargin = 0;
			// rootView.setLayoutParams(chatParam);
			// }
			break;
		}

	}

	protected void showAvatar() {

		if (gdAvatar.getVisibility() == View.VISIBLE) {
			gdAvatar.setVisibility(View.GONE);
		} else {
			gdAvatar.setVisibility(View.VISIBLE);
		}
		if (null == gVAdapter) {
			gVAdapter = new GridViewAvatarAdapter(gdAvatar.getContext(), this);
			gdAvatar.setAdapter(gVAdapter);
		} else {
			gVAdapter.notifyDataSetChanged();
		}
	}

	private class CustomUrlSpan extends ClickableSpan {

		@Override
		public void updateDrawState(TextPaint ds) {
			ds.setColor(ds.linkColor);
			ds.setUnderlineText(false);
		}

		@Override
		public void onClick(View widget) {
			lySysMsg.setVisibility(View.GONE);
		}

	}

	private class PublicChatAdapter extends BaseAdapter {
		private List<AbsChatMessage> msgList = new ArrayList<AbsChatMessage>();

		// 此方法用于发送时，先将信息显示，如果发送失败，则从队列里取数据
		public void addMsg(AbsChatMessage msg) {
			if (!msgList.contains(msg)) {
				msgList.add(msg);
				notifyDataSetChanged();
				lvPublicChat.setSelection(msgList.size());
			}
		}

		public void setMsgList(List<AbsChatMessage> msgList) {
			this.msgList = msgList;
			notifyDataSetChanged();
			lvPublicChat.setSelection(msgList.size());
		}

		public void notifyData() {
			lvPublicChat.setSelection(msgList.size());
		}

		@Override
		public int getCount() {
			return msgList.size();
		}

		@Override
		public Object getItem(int position) {
			return msgList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			// if (null == convertView) {
			// LayoutInflater inflater = LayoutInflater.from(activity);
			// convertView = inflater.inflate(R.layout.item_ly_public_chat,
			// null);
			// viewHolder = new ViewHolder(convertView);
			// convertView.setTag(viewHolder);
			// } else {
			// viewHolder = (ViewHolder) convertView.getTag();
			// }
			LayoutInflater inflater = LayoutInflater.from(parent.getContext());
			convertView = inflater.inflate(R.layout.item_ly_public_chat, null);
			viewHolder = new ViewHolder(convertView);
			AbsChatMessage msg = (AbsChatMessage) getItem(position);
			viewHolder.init(msg);
			return convertView;
		}

	}

	private class ViewHolder {
		private TextView tvName;
		private TextView tvTime;
		private MyTextViewEx tvContent;
		private LinearLayout lyChat;

		public ViewHolder(View view) {
			tvName = (TextView) view
					.findViewById(R.id.item_tv_public_chat_name);
			tvTime = (TextView) view
					.findViewById(R.id.item_tv_public_chat_time);
			tvContent = (MyTextViewEx) view
					.findViewById(R.id.item_tv_public_chat_content);
			lyChat = (LinearLayout) view.findViewById(R.id.item_ly_public_chat);
		}

		public void init(AbsChatMessage msg) {
			long userId = msg.getSendUserId();
			UserInfo userInfo = UserManager.getIns().getUserByUserId(userId);
			if (null != userInfo) {
				tvName.setText(userInfo.getName());
				tvTime.setText(DateUtil.getTimeStr(
						AbsChatMessage.formatter1, msg.getTime()));
				Resources resources = tvTime.getContext().getResources();
				int colorValue = resources
						.getColor(R.color.public_chat_other_name);
				if (userInfo.IsHost()
						&& userInfo.getId() == RTRoom.getIns().getUserId()) {
					colorValue = resources
							.getColor(R.color.public_chat_self_name);
				} else if (userInfo.IsHost()) {
					colorValue = resources
							.getColor(R.color.public_chat_host_name);
				} else if (userInfo.getId() == RTRoom.getIns().getUserId()) {
					colorValue = resources
							.getColor(R.color.public_chat_self_name);
				} else {
					colorValue = resources
							.getColor(R.color.public_chat_other_name);
				}

				tvName.setTextColor(colorValue);
				tvTime.setTextColor(colorValue);
			}
			// tvContent.insertGif(msg.getMsg());
			tvContent.setRichText(msg.getRich());
		}
	}

	public String replaceString(String source, int startIndex, int endIndex,
			String replace) {
		StringBuilder sb = new StringBuilder();
		sb.append(source, 0, startIndex);
		sb.append(source, endIndex, source.length());
		return sb.toString();
	}

	private View findViewById(int resId) {
		return rootView.findViewById(resId);
	}

	private void initComp() {
		rlPublicChat = (RelativeLayout) findViewById(R.id.rl_public_chat);
		rlPublicChat.setOnClickListener(this);
		ivPublicChatHor = (ImageView) findViewById(R.id.iv_public_chat_hor);
		ivPublicChatHor.setOnClickListener(this);
		ivPublicChatHor.setSelected(true);
		lySysMsg = (LinearLayout) findViewById(R.id.ly_public_chat_sys_tip);
		tvSysMsg = (TextView) findViewById(R.id.tv_public_chat_sys_tip);
		lySysMsg.setVisibility(View.GONE);
		lvPublicChat = (ListView) findViewById(R.id.lv_public_chat);

		adapter = new PublicChatAdapter();
		lvPublicChat.setAdapter(adapter);
		ivAvatar = (ImageView) findViewById(R.id.iv_public_chat_avatar);
		ivAvatar.setOnClickListener(this);
		edtChatContent = (ChatEditText) findViewById(R.id.edt_public_chat_content);
		edtChatContent.setOnClickListener(this);

		// setTextChangeListener();
		btnChatSend = (Button) findViewById(R.id.btn_public_chat_send);
		btnChatSend.setOnClickListener(this);
		gdAvatar = (GridView) findViewById(R.id.gd_public_chat_avatar);
		gVAdapter = new GridViewAvatarAdapter(gdAvatar.getContext(), this);
		gdAvatar.setAdapter(gVAdapter);

	}

	@Override
	public void selectAvatar(String avatar, Drawable resId) {
		edtChatContent.insertAvatar(avatar, 0);
	}

}
