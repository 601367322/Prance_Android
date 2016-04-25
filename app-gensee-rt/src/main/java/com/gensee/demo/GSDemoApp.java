package com.gensee.demo;

//import com.gensee.download.VodDownLoad;
//import com.gensee.download.VodDownLoader;
import com.gensee.common.GenseeConfig;
import com.gensee.room.RtSdk;
import com.gensee.taskret.OnTaskRet;
import com.gensee.vod.VodSite;

import android.app.Application;

public class GSDemoApp extends Application {
	
	private static GSDemoApp ins ;
//	private VodDownLoader downLoader;
	public static GSDemoApp getIns(){
		synchronized (GSDemoApp.class) {
			return ins;
		}
	}
	@Override
	public void onCreate() {
		super.onCreate();
		GenseeConfig.thirdCertificationAuth = true;
		ins = this;
		//点播初始化
		initVodSite();
		
		//Rtsdk  直播初始化
		RtSdk.loadLibrarys();
	}
	
	private void initVodSite() {
		
		VodSite.init(this.getApplicationContext(), new OnTaskRet() {

			@Override
			public void onTaskRet(boolean ret, int iret, String desc) {
				// 1、可以初始化下载模块进行下载任务
				// 2、可以初始化视频模块进行视频播放
				if (ret) {
					
//					vodDownInit();
				} else {

				}
			}
		});
	}
	
	
//	private void vodDownInit(){
//		if(downLoader == null){
//			VodDownLoader.instance(context, downloadCallback, fileDir)
//			VodDownLoad.initVodDownLoad(null,null,null,null).autoStart();
//		}
//			
//	}
	
	public void releaseVodeSite(){
		VodSite.release();
	}

}
