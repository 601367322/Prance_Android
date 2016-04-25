package com.gensee.playerdemo;


import com.gensee.view.GSImplChatView;
import com.gensee.view.GSVideoView;

import android.app.Activity;
import android.os.Bundle;

public class TestActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.test_chat);
		GSVideoView vv = (GSVideoView) findViewById(R.id.gsVideoview);
		GSImplChatView cv = (GSImplChatView) findViewById(R.id.gsChatView);
	}
}
