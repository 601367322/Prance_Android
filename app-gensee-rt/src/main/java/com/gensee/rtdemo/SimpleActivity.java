package com.gensee.rtdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

import com.gensee.common.ServiceType;
import com.gensee.demo.R;
import com.gensee.entity.InitParam;
import com.gensee.net.RtComp;
import com.gensee.net.RtComp.Callback;
import com.gensee.room.RtSimpleImpl;
import com.gensee.routine.IRTEvent.IRoomEvent.LeaveReason;
import com.gensee.routine.State;
import com.gensee.routine.UserInfo;
import com.gensee.routine.IRTEvent.IRoomEvent.JoinResult;
import com.gensee.utils.GenseeLog;
import com.gensee.utils.RTLog;
import com.gensee.view.GSVideoView;
import com.gensee.view.GSVideoView.RenderMode;

public class SimpleActivity extends Activity implements Callback{
	private static final String TAG = "SimpleActivity";
	private SharedPreferences preferences;
	private EditText edtDomain, edtNum, edtAccount, edtPwd, edtNick,
			edtJoinPwd,edtK;


	private Button btnLeave;

	private GSVideoView videoView;
	
	private Spinner spinner;

	private RtSimpleImpl simpleImpl;
	
	private Button btnInit;
	
	// 站点类型ServiceType.ST_CASTLINE 直播webcast，
		// ServiceType.ST_MEETING 会议meeting，
		// ServiceType.ST_TRAINING 培训 training
	private ServiceType serviceType = ServiceType.ST_CASTLINE;
	private UserInfo self;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple);
		preferences = getPreferences(MODE_PRIVATE);
		/**
		 * 初始化一个RTSDK简单实现，重新定义一个类继承RtSimpleImpl也是可以的，
		 * 并实现其3个抽象函数，其中onGetContext必须要返回app context，音视频最佳选择。
		 * 注：这里是在onCreate中创建的simpleImpl，如果有屏幕变化引起simpleImpl 重新被创建需要自行处理；回调线程为非UI线程，更新ui请在UI线程中处理。
		 */
		simpleImpl = new RtSimpleImpl() {
			
			
			
			@Override
			public Context onGetContext() {
				return getBaseContext();
			}
			
			@Override
			protected void onVideoStart() {
				GenseeLog.d(TAG, "onVideoStart");
				
			}
			
			@Override
			protected void onVideoEnd() {
				GenseeLog.d(TAG, "onVideoEnd");
				
			}
			
			/**
			 * result 0 表示加入房间（直播间、会议室、课堂）成功  其他代表加入失败  失败后最好以对话框通知用户本次操作失败了
			 */
			@Override
			public void onRoomJoin(final int result, UserInfo self) {
				super.onRoomJoin(result, self);
				SimpleActivity.this.self = self;
				GenseeLog.d(TAG, "onRoomJoin");
				runOnUiThread(new Runnable() {
					public void run() {
						
						String resultDesc = null;
						switch (result) {
						//加入成功  除了成功其他均需要正常提示给用户
						case JoinResult.JR_OK:
							btnInit.setText("已加入");
							AlertUtil.toast(SimpleActivity.this, "您已加入成功");
							break;
							//加入错误
						case JoinResult.JR_ERROR:
							resultDesc = "加入失败，重试或联系管理员";
							break;
							//课堂被锁定
						case JoinResult.JR_ERROR_LOCKED:
							resultDesc = "直播间已被锁定";
							
							break;
							//老师（组织者已经加入）
						case JoinResult.JR_ERROR_HOST:
							resultDesc = "老师已经加入，请以其他身份加入";
							break;
							//加入人数已满
						case JoinResult.JR_ERROR_LICENSE:
							resultDesc = "人数已满，联系管理员";
							
							break;
							//音视频编码不匹配
						case JoinResult.JR_ERROR_CODEC:
							resultDesc = "编码不匹配";
							break;
							//超时
						case JoinResult.JR_ERROR_TIMESUP:
							resultDesc = "已经超过直播结束时间";
							break;
							
						default:
							resultDesc = "其他结果码：" + result + "联系管理员";
							break;
						}
						if(resultDesc != null){
							AlertUtil.showDialog(SimpleActivity.this, resultDesc, new DialogInterface.OnClickListener() {
								
								@Override
								public void onClick(DialogInterface dialog, int which) {
									leave(false);
								}
							}, null);
						}
					}
				});
				
			}
			
			/**
			 * 直播状态 s.getValue()   0 默认直播未开始 1、直播中， 2、直播停止，3、直播暂停
			 */
			@Override
			public void onRoomPublish(State s) {
				super.onRoomPublish(s);
				
				//TODO 此逻辑是控制视频要在直播开始后才准许看的逻辑
				/*	byte castState = s.getValue();
				    RtSdk rtSdk = getRtSdk();
				    
					switch (castState) {
					case 1:
						setVideoView(videoView);
				        rtSdk.audioOpenSpeaker(null);
						break;
					case 0:
					case 2:
					case 3:
						setVideoView(null);
				        rtSdk.audioCloseSpeaker(null);
					default:
						break;
					}*/
				
			}

			@Override
			public void onJoin(boolean result) {
				// TODO Auto-generated method stub
				
			}
			
			//退出完成 关闭界面
			@Override
			protected void onRelease(final int reason) {
				//reason 退出原因
				runOnUiThread(new Runnable() {
					
					@SuppressLint("NewApi")
					@Override
					public void run() {
						String msg = "已退出";
						switch (reason) {
						//用户自行退出  正常退出
						case LeaveReason.LR_NORMAL:
							msg = "您已经成功退出";
							break;
					    //LR_EJECTED = LR_NORMAL + 1; //被踢出
						case LeaveReason.LR_EJECTED:
							msg = "您已被踢出"; 
							break;
						//LR_TIMESUP = LR_NORMAL + 2; //时间到
						case LeaveReason.LR_TIMESUP:
							msg = "时间已过"; 
							break;
						//LR_CLOSED = LR_NORMAL + 3; //直播（课堂）已经结束（被组织者结束）
						case LeaveReason.LR_CLOSED:
							msg = "直播间已经被关闭"; 
							break;

						default:
							break;
						}
						if (isDestroyed()) {
							return;
						}
						//这里可以弹出对话框，确定后在关闭界面
						AlertUtil.showDialog(SimpleActivity.this, msg, new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								//确认关闭界面
								finish();
							}
						}, null);
						
						
					}
				});
			}
		};
		initView();
	}

	private void initView() {
		edtDomain = (EditText) findViewById(R.id.editDomain);
		edtNum = (EditText) findViewById(R.id.editCastNum);
		edtAccount = (EditText) findViewById(R.id.editAccount);
		edtPwd = (EditText) findViewById(R.id.editpwd);
		edtNick = (EditText) findViewById(R.id.editNickName);
		edtJoinPwd = (EditText) findViewById(R.id.editWatchword);
		edtK = (EditText) findViewById(R.id.edtK);
		btnInit = (Button) findViewById(R.id.init);


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
		
		
		
		videoView = (GSVideoView) findViewById(R.id.surface_casting_cus);


		findViewById(R.id.init).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				btnInit.setEnabled(false);
				btnInit.setText("正在加入。。。");
				String domain = edtDomain.getText().toString();
				String number = edtNum.getText().toString();
				String account = edtAccount.getText().toString();
				String pwd = edtPwd.getText().toString();
				String nickName = edtNick.getText().toString();
				String joinPwd = edtJoinPwd.getText().toString();
				String k = edtK.getText().toString();
				InitParam p = new InitParam();
				//domain
				p.setDomain(domain);
				//编号（直播间号）,如果没有编号却有直播id的情况请使用setLiveId("此处直播id或课堂id");
				p.setNumber(number);
				//直播id，没有编号而有id的情况下使用
//				p.setLiveId("a2732e3dbaf743c8aafb3205af085bdd");
				//站点认证帐号，根据情况可以填""
				p.setLoginAccount(account);
				//站点认证密码，根据情况可以填""
				p.setLoginPwd(pwd);
				//昵称，供显示用
				p.setNickName(nickName);
				//加入口令，没有则填""
				p.setJoinPwd(joinPwd);
				//站点类型ServiceType.ST_CASTLINE 直播webcast，
				//ServiceType.ST_MEETING 会议meeting，
				//ServiceType.ST_TRAINING 培训
				p.setServiceType(serviceType);
				p.setK(k);
				
				RtComp comp = new RtComp(getApplicationContext(),
						SimpleActivity.this);
//				comp.setbAttendeeOnly(true);
				comp.initWithGensee(p);
				
				
			}
		});

		btnLeave = (Button) findViewById(R.id.btnExit);
		btnLeave.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				leaveCast();
			}
		});

		/**
		 * 设置视频View
		 */
		simpleImpl.setVideoView(videoView);
		findViewById(R.id.btnVideo).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				RenderMode  mode = videoView.getRenderMode();
				switch (mode) {
				//适应窗口
				case RM_ADPT_XY:
					videoView.setRenderMode(RenderMode.RM_FILL_XY);
					break;
					//完全充满
				case RM_FILL_XY:
					videoView.setRenderMode(RenderMode.RM_CENTER);
					break;
					//中间显示  原始大小比窗口小的时候
				case RM_CENTER:
					videoView.setRenderMode(RenderMode.RM_ADPT_XY);
					break;

				default:
					break;
				}
			}
		});
		
		// 这里可以获得Rtsdk的实例，因此可以注册其他回调进行其他功能"问答、聊天、投票"的开发
		/*
		 * RtSdk rtSdk = simpleImpl.getRtSdk(); 
		 * rtSdk.setQACallback(qaCallback); //问答功能
		 * rtSdk.setVoteCallback(voteCallBack);//投票
		 * rtSdk.setChatCallback(chatCallBack);//聊天
		 */
		
//		simpleImpl.setVideoView(videoView);
		/*
		 * 设置文档View
		 */
        //simpleImpl.setDocView(docView);
		simpleImpl.getRtSdk().setQACallback(null);
	}

	@Override
	public void onInited(String rtParam) {
		RTLog.d(TAG, "rtParam = " + rtParam);
		
		String domain = edtDomain.getText().toString();
		String number = edtNum.getText().toString();
		String account = edtAccount.getText().toString();
		String pwd = edtPwd.getText().toString();
		String nickName = edtNick.getText().toString();
		String joinPwd = edtJoinPwd.getText().toString();
		
		Editor editor =  preferences.edit();
		editor.putString("domain", domain);
		editor.putString("number",number);
		editor.putString("account", account);
		editor.putString("accPwd", pwd);
		editor.putString("nickname", nickName);
		editor.putString("joinPwd",joinPwd);
		//记住本次使用的参数 免得下次输入
		editor.commit();
		
		simpleImpl.joinWithParam("", rtParam);
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
	public void onErr(final int errCode) {
		RTLog.d(TAG, "onErr = " + errCode);
		
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				btnInit.setText("加入");
				btnInit.setEnabled(true);
				AlertUtil.showDialog(SimpleActivity.this, getErrMsg(errCode),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						}, null);
			}
		});
	}
	
	private String getErrMsg(int errCode) {
		String errMsg;
		switch (errCode) {
		case ERR_THIRD_CERTIFICATION_AUTHORITY:
			errMsg = "第三方认证失败";
			break;
		default:
			errMsg = "初始化错误:错误码" + errCode + "，请参考文档中的错误码说明";
			break;
		}
		return errMsg;
	}

	
	/**
	 * 退出的时候请调用
	 */
	private void leaveCast(){
		//TODO 显示进度框
		simpleImpl.leave(false);
	}

	
	@Override
	public void onBackPressed() {
		if (self == null) {
			super.onBackPressed();
			return;
		}
		leaveCast();
	}
}
