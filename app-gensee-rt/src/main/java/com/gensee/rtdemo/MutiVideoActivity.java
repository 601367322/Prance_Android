package com.gensee.rtdemo;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Spinner;
import android.widget.Toast;

import com.gensee.callback.IAsCallBack;
import com.gensee.callback.IAudioCallBack;
import com.gensee.callback.IChatCallBack;
import com.gensee.callback.ILodCallBack;
import com.gensee.callback.IQACallback;
import com.gensee.callback.IRoomCallBack;
import com.gensee.callback.IVideoCallBack;
import com.gensee.common.RTConstant;
import com.gensee.common.ServiceType;
import com.gensee.demo.R;
import com.gensee.entity.InitParam;
import com.gensee.net.RtComp;
import com.gensee.net.RtComp.Callback;
import com.gensee.pdu.GSDocView;
import com.gensee.pdu.GSDocView.OnDocViewEventListener;
import com.gensee.qa.QaAnswer;
import com.gensee.qa.QaQuestion;
import com.gensee.room.RtSdk;
import com.gensee.routine.LiveodItem;
import com.gensee.routine.State;
import com.gensee.routine.UserInfo;
import com.gensee.rtdemo.vote.VoteHolderFragement;
/*
 * ChatResource 表情资源库，初始化聊天表情资源，可以更加app自身需求，如果没有聊天或有聊天不需要显示表情则忽略；
 * 如果需要则添加表情库rtlib
 */
import com.gensee.rtlib.ChatResource;
import com.gensee.taskret.OnTaskRet;
import com.gensee.utils.GenseeLog;
import com.gensee.utils.StringUtil;
import com.gensee.view.GSDocViewGx;
import com.gensee.view.GSVideoView;
import com.gensee.view.LocalVideoView;
import com.gensee.view.MyTextViewEx;

//import com.gensee.rtlib.ChatResource;

public class MutiVideoActivity extends FragmentActivity implements Callback,
		IRoomCallBack, IAudioCallBack, IChatCallBack, IAsCallBack,
		IVideoCallBack, ILodCallBack, IQACallback, OnClickListener,
		OnTouchListener {

	private static final String TAG = "MutiVideoActivity";
	private SharedPreferences preferences;
	private EditText edtDomain, edtNum, edtAccount, edtPwd, edtNick,
			edtJoinPwd,edtK, edtMsg;
	RtComp action;
	/*
	 * sdk 加入成功后默认是打开扬声器的
	 */
	private boolean isSpeekerOpened = true;
	private boolean isVideoOpened = false;
	private boolean isMicOpened = false;

	private String rtParam;
	private MyTextViewEx myTextViewEx;

	private Button btnLeave;

	private GSVideoView videoViewCast, videoViewLod, videoViewAs;
	private LocalVideoView localVideoView;
//	private GSDocView docView;
	private GSDocViewGx docView;
	private Spinner spinner;
	private RtSdk rtSdk;
	public static RtSdk mRTSdk2;
	private LinearLayout lyChat;
	private Button btnPublish, btnPerson;
	private UserInfo self;
	// 站点类型ServiceType.ST_CASTLINE 直播webcast，
	// ServiceType.ST_MEETING 会议meeting，
	// ServiceType.ST_TRAINING 培训 training
	private ServiceType serviceType = ServiceType.ST_CASTLINE;

	private static final int CHAT_UPDATE = 100;

	private LinearLayout lyFloat;

	private VoteHolderFragement voteHolderFragement;

	// private boolean isLodPlaying = false;
	// private boolean isAsPlaying = false;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			GenseeLog.d(TAG, "handleMessage what = " + msg.what + "" + msg.obj);
			switch (msg.what) {
			case CHAT_UPDATE:

				break;
			case RTConstant.RoomBack.ON_JOIN_INIT:
				break;
			case RTConstant.RoomBack.ON_JOINCONFIRM:
				break;
			default:
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rtvideos);
		preferences = getPreferences(MODE_PRIVATE);
		// app 需要根据自身特性来初始化和管理该rtSdk的实例
		rtSdk = new RtSdk();
		initView();
		// 初始化聊天表情资源，可以更加app自身需求，如果没有聊天或有聊天不需要显示表情则忽略
		ChatResource.initChatResource(MutiVideoActivity.this);
	}

	private void initView() {
		voteHolderFragement = new VoteHolderFragement(rtSdk);
		lyFloat = (LinearLayout) findViewById(R.id.ly_float);
		btnPublish = (Button) findViewById(R.id.btnPublish);
		btnPerson = (Button) findViewById(R.id.btnSend);
		lyChat = (LinearLayout) findViewById(R.id.ly_chat);
		// myTextViewEx = (MyTextViewEx) findViewById(R.id.chat_message);
		edtDomain = (EditText) findViewById(R.id.editDomain);
		edtNum = (EditText) findViewById(R.id.editCastNum);
		edtAccount = (EditText) findViewById(R.id.editAccount);
		edtPwd = (EditText) findViewById(R.id.editpwd);
		edtNick = (EditText) findViewById(R.id.editNickName);
		edtJoinPwd = (EditText) findViewById(R.id.editWatchword);
		edtK = (EditText) findViewById(R.id.edtK);
		spinner = (Spinner) findViewById(R.id.spinner);
		spinner.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, new String[] {
						// webcast
						ServiceType.ST_CASTLINE.getValue(),
						// training
						ServiceType.ST_TRAINING.getValue() }));
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				serviceType = position == 0 ? ServiceType.ST_CASTLINE
						: ServiceType.ST_TRAINING;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
		spinner.setPrompt(serviceType.getValue());
		btnPerson.setOnClickListener(this);
		btnPublish.setOnClickListener(this);
		docView = (GSDocViewGx) findViewById(R.id.docView);
		// docView.setBackgroundColor(0xff000000);
//		docView.showAdaptViewHeight();
		docView.setOnDocViewClickedListener(new OnDocViewEventListener() {

			@Override
			public boolean onSingleClicked(GSDocView docView) {
				GenseeLog.d(TAG, "onSingleClicked ");
				return true;
			}

			@Override
			public boolean onEndHDirection(GSDocView docView, int type,int eventType) {
				// TODO Auto-generated method stub
				//eventType  MotionEvent.ACTION_MOVE or MotionEvent.ACTION_UP
				return false;
			}

			@Override
			public boolean onDoubleClicked(GSDocView docView) {
				GenseeLog.d(TAG, "onDoubleClicked ");
				Log.d(TAG, "onDoubleClicked");
				int docfillMode = docView.getShowMode();
				if (docfillMode != GSDocView.SHOW_FILL_VIEW) {
					// 文档内容填充docview
					docView.showFillView();
				} else {
					// 文档内容适合docview显示
					docView.showAdaptView();
				}
				return true;
			}
		});

		String domain = preferences.getString("domain", "");
		String number = preferences.getString("number", "");
		String account = preferences.getString("account", "");
		String accPwd = preferences.getString("accPwd", "");
		String nickName = preferences.getString("nickname", "");
		String joinPwd = preferences.getString("joinPwd", "");
		
		
		edtDomain.setText(domain);
		edtNum.setText(number);
		edtAccount.setText(account);
		edtPwd.setText(accPwd);
		edtNick.setText(nickName);
		edtJoinPwd.setText(joinPwd);


		localVideoView = (LocalVideoView) findViewById(R.id.locVideo);
		localVideoView.setOrientation(Configuration.ORIENTATION_PORTRAIT);
		videoViewCast = (GSVideoView) findViewById(R.id.videoCasting);
		videoViewLod = (GSVideoView) findViewById(R.id.videoLod);
		videoViewAs = (GSVideoView) findViewById(R.id.videoAs);
		edtMsg = (EditText) findViewById(R.id.edtMsg);

		findViewById(R.id.init).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String domain = edtDomain.getText().toString();
				String number = edtNum.getText().toString();
				String account = edtAccount.getText().toString();
				String pwd = edtPwd.getText().toString();
				String nickName = edtNick.getText().toString();
				String joinPwd = edtJoinPwd.getText().toString();
				String k = edtK.getText().toString();
				if (action != null) {
					action.setCallback(null);
				}
				action = new RtComp(MutiVideoActivity.this,
						MutiVideoActivity.this);

				InitParam p = new InitParam();
				// domain
				p.setDomain(domain);
				if(number.length() == 8){
					// 编号（8个数字字符）,如果没有编号却有直播id的情况请使用setLiveId("此处直播id或课堂id");
					p.setNumber(number);
				}else{
					// number和liveId至少要一个
					p.setLiveId("");
				}
				// 站点认证帐号，根据情况可以填""
				p.setLoginAccount(account);
				// 站点认证密码，根据情况可以填""
				p.setLoginPwd(pwd);
				// 昵称，供显示用
				p.setNickName(nickName);
				// 加入口令，没有则填""
				p.setJoinPwd(joinPwd);
				// 站点类型ServiceType.ST_CASTLINE 直播webcast，
				// ServiceType.ST_MEETING 会议meeting，
				// ServiceType.ST_TRAINING 培训 training
				p.setServiceType(serviceType);
				p.setK(k);
				action.initWithGensee(p);
			}
		});

		btnLeave = (Button) findViewById(R.id.btnExit);
		btnLeave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				leaveCast();
			}
		});

		findViewById(R.id.btnQa).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				rtSdk.qaAddQuestion(edtMsg.getText().toString(), null);

			}
		});

		findViewById(R.id.btnSpeaker).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isSpeekerOpened) {
					// 关闭扬声器
					if (!rtSdk.audioCloseSpeaker(new OnTaskRet() {

						@Override
						public void onTaskRet(boolean ret, int id, String desc) {
							if (ret) {
								isSpeekerOpened = false;
								toast("关闭扬声器成功");
							} else {
								toast("关闭扬声器失败");
							}
						}
					})) {
						toast("关闭扬声器失败");
					}
				} else {
					// 打开扬声器
					if (!rtSdk.audioOpenSpeaker(new OnTaskRet() {

						@Override
						public void onTaskRet(boolean ret, int id, String desc) {
							if (ret) {
								isSpeekerOpened = true;
								toast("打开扬声器成功");
							} else {
								toast("打开扬声器失败");
							}
						}
					})) {
						toast("打开扬声器失败");
					}
				}
			}
		});

		findViewById(R.id.btnVote).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (null != rtSdk.getSelfUserInfo()) {
					if (lyFloat.getVisibility() != View.VISIBLE) {
						if (!voteHolderFragement.isAdded()) {
							FragmentTransaction transaction = getSupportFragmentManager()
									.beginTransaction();
							transaction
									.add(R.id.container, voteHolderFragement);
							transaction.addToBackStack(null);
							transaction.commit();
						}
						lyFloat.setVisibility(View.VISIBLE);
					}
				}
			}
		});

		findViewById(R.id.btnVideo).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isVideoOpened) {
					if (!rtSdk.videoCloseCamera(new OnTaskRet() {

						@Override
						public void onTaskRet(boolean ret, int id, String desc) {
							if (ret) {
								toast("关闭本地视频成功");
								isVideoOpened = false;
							} else {
								toast("关闭本地视频失败");
							}
						}
					})) {
						toast("关闭本地视频失败");
					}
				} else {
					if (!rtSdk.videoOpenCamera(new OnTaskRet() {

						@Override
						public void onTaskRet(boolean ret, int id, String desc) {
							if (ret) {
								isVideoOpened = true;
								toast("打开本地视频成功");
							} else {
								toast("打开本地视频失败");
							}
						}
					})) {
						toast("打开本地视频失败");
					}
				}
			}
		});

		findViewById(R.id.btnAudio).setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (isMicOpened) {
					if (!rtSdk.audioCloseMic(new OnTaskRet() {

						@Override
						public void onTaskRet(boolean ret, int id, String desc) {
							if (ret) {
								isMicOpened = false;
								toast("关闭mic成功");
							} else {

								toast("关闭mic失败");
							}
						}
					})) {
						toast("关闭mic失败");

					}
				} else {
					if (!rtSdk.audioOpenMic(new OnTaskRet() {

						@Override
						public void onTaskRet(boolean ret, int id, String desc) {
							if (ret) {
								isMicOpened = true;
								toast("打开mic成功");
							} else {
								toast("打开mic失败");
							}
						}
					})) {
						toast("打开mic失败");
					}
				}
			}
		});

		findViewById(R.id.btnRoster).setOnClickListener(this);

		layout();
	}

	private void layout() {
		View v = findViewById(R.id.linVideos);
		LayoutParams p = (LayoutParams) v.getLayoutParams();
		int w = getResources().getDisplayMetrics().widthPixels;
		p.height = (int) (w / 4 * 0.75);
		v.setLayoutParams(p);
	}

	// Callback 响应
	@Override
	public void onInited(String rtParam) {
		String domain = edtDomain.getText().toString();
		String number = edtNum.getText().toString();
		String account = edtAccount.getText().toString();
		String pwd = edtPwd.getText().toString();
		String nickName = edtNick.getText().toString();
		String joinPwd = edtJoinPwd.getText().toString();

		Editor editor = preferences.edit();
		editor.putString("domain", domain);
		editor.putString("number", number);
		editor.putString("account", account);
		editor.putString("accPwd", pwd);
		editor.putString("nickname", nickName);
		editor.putString("joinPwd", joinPwd);
		// 记住本次使用的参数 免得下次输入
		editor.commit();

		//
		this.rtParam = rtParam;
		rtSdk.initWithParam("", rtParam, this);
	}

	// int ERR_DOMAIN = -100; // ip(domain)不正确
	// int ERR_TIME_OUT = -101; // 超时
	// int ERR_UNKNOWN = -102; // 未知错误
	// int ERR_SITE_UNUSED = -103; // 站点不可用
	// int ERR_UN_NET = -104; // 无网络
	// int ERR_DATA_TIMEOUT = -105; // 数据过期
	// int ERR_SERVICE = -106; // 服务不正确
	// int ERR_PARAM = -107; // 参数不正确
	// int ERR_THIRD_CERTIFICATION_AUTHORITY = -108 //第三方认证失败
	// int ERR_NUMBER_UNEXIST = 0; // 直播间不存在
	// int ERR_TOKEN = 4; // 口令错误
	// int ERR_LOGIN = 5; // 用户名或密码错误
	// int ERR_WEBCAST_UNSTART = 6; // 直播未开始
	// int ERR_ISONLY_WEB = 7; // 只支持web
	// int ERR_ROOM_UNEABLE = 8; 直播间不可用
	// int ERR_INVALID_ADDRESS = 10; // 无效地址
	// int ERR_ROOM_OVERDUE = 11; // 过期
	// int ERR_AUTHORIZATION_NOT_ENOUGH = 12;授权不够
	// int ERR_UNTIMELY = 13; // 太早
	@Override
	public void onErr(int errCode) {
		GenseeLog.d(TAG, "onErr = " + errCode);
		switch (errCode) {
		case ERR_DOMAIN:
			toast("domain不正确");
			break;
		case ERR_NUMBER_UNEXIST:
			toast("直播间不存在");
			break;
		case ERR_TOKEN:
			toast("口令错误");
			break;
		case ERR_SERVICE:
			toast("请选择正确服务类型（webcast meeting training）");
			break;
		case ERR_UN_NET:
			toast("请检查网络");
			break;
		case ERR_TIME_OUT:
			toast("连接超时，请重试");
			break;
		case ERR_PARAM:
			toast("initParam 不正确");
			break;
		case ERR_THIRD_CERTIFICATION_AUTHORITY:
			toast("第三方认证失败");
			break;
		default:
			toast("初始化错误，错误码：" + errCode + ",请查对");
			break;
		}
	}

	// 直播（课堂）加入响应
	@Override
	public void onInit(boolean result) {
		GenseeLog.d(TAG, "OnInit = " + result);
		if (result) {
			rtSdk.setGSDocViewGx(docView);
			rtSdk.setLocalVideoView(localVideoView);
			rtSdk.setVideoCallBack(this);
			rtSdk.setAudioCallback(this);
			rtSdk.setLodCallBack(this);
			rtSdk.setChatCallback(this);
			rtSdk.setVoteCallback(voteHolderFragement);
			rtSdk.setAsCallBack(this);
			rtSdk.setQACallback(this);
			rtSdk.join(new OnTaskRet() {

				@Override
				public void onTaskRet(boolean ret, int id, String desc) {
					GenseeLog.i("join ret = " + ret);
					if (!ret) {
						toast("加入失败");
					}
				}
			});
		}
	}

	@Override
	public void onRoomJoin(int result, UserInfo self) {
		GenseeLog.d(TAG, "onRoomJoin = " + result + " self " + self);
		switch (result) {
		case JR_OK:
			this.self = self;
			toast("加入成功");
			break;
		case JR_ERROR_HOST:
			toast("组织者已经加入（老师已经加入）");
			break;
		case JR_ERROR_GETPARAM:
			toast("加入参数错误");
			break;
		case JR_ERROR_LICENSE:
			toast("人数已满");
			break;
		case JR_ERROR_LOCKED:
			toast("直播间（课堂）被锁定");
			break;
		case JR_ERROR_CODEC:
			toast("音频编码不匹配");
			break;

		default:
			break;
		}

		// 失败了请释放以便于重新加入
		if (result != JR_OK) {
			release();
		}
	}

	@Override
	public void onRoomLeave(int reason) {

		GenseeLog.d(TAG, "onRoomLeave = " + reason);
		rtParam = null;
		switch (reason) {
		case LR_CLOSED:
			toast("直播（课堂）已经关闭");
			break;
		case LR_EJECTED:
			toast("被踢出直播（课堂）");
			break;
		case LR_TIMESUP:
			toast("超时，直播(课堂已过期)");
			break;
		case LR_NORMAL:
			toast("已经退出直播（课堂）");
			break;

		default:
			break;
		}
		// 已经退出房间，请进行释放以便于重新加入
		release();
	}

	private void release() {
		self = null;
		// 释放房间实例，onTask响应之后才能重新加入
		release(new OnTaskRet() {

			@Override
			public void onTaskRet(boolean ret, int id, String desc) {
				// 房间从init-join-leave-release 到此结束
				toast("释放完成，你可以重新加入");
				runOnUiThread(new Runnable() {
					public void run() {
						finish();
					}
				});
				// app应该在此时关闭界面，至于触发退出到响应执行到此的时间间隔，app可以做一个对话框来增加交互的友好性
			}
		});

	}

	// 网络断线 sdk正在重连
	@Override
	public void onRoomReconnecting() {
		toast("正在重连中");
	}

	@Override
	public void onRoomLock(boolean locked) {
	}

	@Override
	public void onRoomUserJoin(UserInfo userInfo) {
		if (userInfo != null) {
			GenseeLog.d(TAG, "onRoomUserJoin " + userInfo);

			toast(userInfo.getName() + ":已加入");
		}
	}

	// 用户信息有更新，用户的状态、名字、权限、角色更新
	@Override
	public void onRoomUserUpdate(UserInfo userInfo) {
	}

	@Override
	public void onRoomUserLeave(UserInfo userInfo) {
		if (userInfo != null) {
			toast(userInfo.getName() + ":已离开");
		}
	}

	// 必须返回有效的Context
	@Override
	public Context onGetContext() {
		return this;
	}

	@Override
	public void onAudioJoinConfirm(boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAudioMicOpened() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAudioMicClosed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAudioSpeakerOpened() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAudioSpeakerClosed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAudioLevel(int level, long userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChatJoinConfirm(boolean bRet) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChatWithPersion(UserInfo userInfo, String msg, String rich) {

	}

	@Override
	public void onChatWithPublic(UserInfo userInfo, String msg,
			final String rich) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				// myTextViewEx.insertGif(rich);
			}
		});
	}

	@Override
	public void onChatEnable(boolean enable) {

	}

	/********************** 桌面共享 ******************/
	@Override
	public void onAsJoinConfirm(boolean ok) {

	}

	@Override
	public void onAsBegin(long owner) {

		// rtSdk.setVideoView(null);
		// rtSdk.setAsVideoView(videoView);

		// isAsPlaying = true;

	}

	@Override
	public void onAsEnd() {

		// rtSdk.setAsVideoView(null);
		// rtSdk.setVideoView(videoView);

		// isAsPlaying = false;

	}

	private void leaveCast() {
		rtSdk.leave(false, null);
		// rtSdk.release(null);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		leaveCast();
	}

	@Override
	public ServiceType getServiceType() {
		return null;
	}

	@Override
	public void onAsData(byte[] data, int width, int height) {
		// 桌面共享
		videoViewAs.onReceiveFrame(data, width, height);
	}

	@Override
	public void onAudioMicAvailable(boolean isAvailable) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomPublish(State s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomRecord(State s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomData(String key, long value) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomBroadcastMsg(String msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomRollcall(int timeout) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomRollcallAck(long userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomHandup(long userId, String data) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRoomHanddown(long userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLodFailed(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLodStart(LiveodItem liveodItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLodSkip(LiveodItem liveodItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLodPause(LiveodItem liveodItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLodStop(LiveodItem liveodItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLodPlaying(LiveodItem liveodItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLodAdd(LiveodItem liveodItem) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onLodRemove(String id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoJoinConfirm(boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoCameraAvailiable(boolean ok) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoCameraOpened() {
		rtSdk.videoActive(self.getId(), true, null);
	}

	@Override
	public void onVideoCameraClosed() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoJoin(UserInfo user) {
		if (user == null) {
			return;
		}
		long userId = user.getId();
		// 插播视频
		if (UserInfo.LOD_USER_ID == userId) {
			// isLodPlaying = true;
			rtSdk.displayVideo(userId, null);
		}
	}

	@Override
	public void onVideoLeave(long userId) {
		// 插播视频
		if (UserInfo.LOD_USER_ID == userId) {
			// isLodPlaying = false;
			rtSdk.unDisplayVideo(userId, null);
		}
	}

	private long activeId = 0;

	@Override
	public void onVideoActived(UserInfo user, boolean bActived) {
		// 直播视频 bActived true 被作为直播（讲台）视频，false 取消直播（讲台）视频
		if (user == null) {
			return;
		}
		long userId = user.getId();
		if (bActived) {
			// 取消上一个直播视频，tip：如果是要显示多个“个人”的视频，是需要修改的
			if (activeId != 0) {
				rtSdk.unDisplayVideo(activeId, null);
			}
			activeId = userId;
			// 订阅userid的视频数据
			rtSdk.displayVideo(userId, null);
		} else {
			// 取消订阅userid的视频数据
			activeId = 0;
			rtSdk.unDisplayVideo(userId, null);
		}
	}

	@Override
	public void onVideoDisplay(UserInfo user) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoUndisplay(long userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onVideoDataRender(long userId, int width, int height,
			int frameFormat, float displayRatio, byte[] data) {

		// 插播视频
		if (UserInfo.LOD_USER_ID == userId) {
			videoViewLod.onReceiveFrame(data, width, height);
		} else {
			// 用户视频
			videoViewCast.onReceiveFrame(data, width, height);
		}
	}

	@Override
	public void onChatToPersion(long userId, String msg, String rich) {
	}

	// TODO Auto-generated method stub

	@Override
	public void onJoin(boolean result) {
		if (!result) {
			release(new OnTaskRet() {

				@Override
				public void onTaskRet(boolean ret, int id, String desc) {
					if (rtParam != null) {
						rtSdk.initWithParam("", rtParam, MutiVideoActivity.this);
					}

				}
			});
		}
	}

	private void release(OnTaskRet ret) {
		rtSdk.release(ret);
	}

	@Override
	public void OnUpgradeNotify(String downloadUrl) {

	}

	public void onChatMode(boolean isChatMode) {
		// isChatMode 全局聊天模式 true 允许聊天/false 禁止聊天
	}

	@Override
	public void onFreeMode(boolean isFreeMode) {
		// isFreeMod自由模式 true 自由模式/ false 非自由模式
	}

	private void toast(final String msg) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT)
						.show();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSendGroup:
		case R.id.btnSend:
			Intent mIntent = new Intent(this, ChatActivity.class);
			mRTSdk2 = rtSdk;
			startActivity(mIntent);
			break;
		// 修改用户名
		case R.id.btnRoster:
			String newName = edtMsg.getText().toString();
			// newName 的长度不要超过255个字符
			if (self != null && !StringUtil.isEmpty(newName)) {
				rtSdk.roomRename(self.getId(), newName, null);
			}
			break;
			
		case R.id.btnPublish:
			//开启直播  State.S_RUNNING  暂停直播State.S_PAUSED  停止直播 State.S_STOPPED
			rtSdk.roomPublish(State.S_RUNNING.getValue(), null);
			
			//开启录制 State.S_RUNNING 暂停录制State.S_PAUSED  停止录制 State.S_STOPPED
			rtSdk.roomRecord(State.S_RUNNING.getValue(), null);
			break;
		default:
			break;
		}

	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		return false;
	}

	@Override
	public void onBackPressed() {
		// 还未加入或还未进行加入的时候直接返回
		if (self == null) {
			release();
			super.onBackPressed();
			return;
		}
		// 调用leave 并且不要调用 super.onBackPressed(); 当release 完成的时候 再调用finish 或者
		// super.onBackPressed
		int nCount = getSupportFragmentManager().getBackStackEntryCount();
		if (nCount > 2) {
			super.onBackPressed();
			return;
		}
		if (lyFloat.getVisibility() != View.GONE) {
			super.onBackPressed();
			lyFloat.setVisibility(View.GONE);
			return;
		}
		voteHolderFragement.clear();
		leaveCast();
	}

	@Override
	public void onQaJoinConfirm(int isOk) {

	}

	@Override
	public void onQaSettingUpdate(boolean isAutoDispatchQuestion,
			boolean isAutoPublishQuestion, boolean isQAEnable) {
		GenseeLog.d(TAG, "onQaSettingUpdate isAutoDispatchQuestion = "
				+ isAutoDispatchQuestion + " isAutoPublishQuestion = "
				+ isAutoPublishQuestion + " isQAEnable = " + isQAEnable);
	}

	// QA_STATE_TEXT_REPLYING = 8 正在进行文字回复
	@Override
	public void onQaQuestion(QaQuestion qaQuestion, int state) {
		GenseeLog.d(TAG, "onQaQuestion " + state);
		// state == QA_STATE_QUESTION 问题变更
		// QA_STATE_ANSWER == state 答案变更
		if (QA_STATE_ANSWER == state || state == QA_STATE_QUESTION) {
			StringBuilder sb = new StringBuilder("问答：\n问：");
			String questId = qaQuestion.getStrQuestionId();// 问题id
			String question = qaQuestion.getStrQuestionContent();// 问题内容
			String quester = qaQuestion.getStrQuestionOwnerName();// 提问者名字
			String questerFixer = qaQuestion.getStrTaggedUserName();// 问题处理人名字
			long qTime = qaQuestion.getDwQuestionTime();// 提问时间
			sb.append(question).append('\n');
			List<QaAnswer> answers = qaQuestion.getQaAnswerList();// 答案列表
			if (answers != null) {
				for (QaAnswer answer : answers) {
					if (answer != null) {
						String answerId = answer.getStrAnswerId();// 答案id
						String answerContent = answer.getStrAnswerContent();// 答案内容
						String answerOwner = answer.getStrAnswerOwnerName();// 答案回复者名字
						long answerOwnerId = answer.getLlAnswerOwnerId();// 答案回复者id
						long anwerTime = answer.getDwAnswerTime();// 回复时间
						sb.append("答：").append(answerOwner).append('-')
								.append(answerContent).append('\n');

					}
				}
			}
			toast(sb.toString());
		}
	}

	public void closeVote() {
		if (lyFloat.getVisibility() != View.GONE) {
			lyFloat.setVisibility(View.GONE);
		}
	}


	@Override
	public void onLottery(byte arg0, String arg1) {

	}

	@Override
	public void onChatMode(int mode) {
		GenseeLog.d(TAG, "onChatMode mode = " + mode);

	}

	@Override
	public String onSettingQuery(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onSettingQuery(String arg0, int arg1) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onSettingSet(String arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSettingSet(String arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onNetworkBandwidth(int bpsSend, int bpsRec) {
		// TODO Auto-generated method stub
		
	}

	@Override
	//0-100
	public void onNetworkReport(byte level) {
		// TODO Auto-generated method stub
		
	}


}
