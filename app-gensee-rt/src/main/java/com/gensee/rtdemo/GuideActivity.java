package com.gensee.rtdemo;

import com.gensee.demo.GSDemoApp;
import com.gensee.demo.R;
import com.gensee.voddemo.VodActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class GuideActivity extends Activity {

	private ImageView imageView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guide);
		imageView = (ImageView) findViewById(R.id.imgView);
		imageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.setVisibility(View.GONE);
			}
		});
		findViewById(R.id.btnMutiVideo).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(GuideActivity.this,
								MutiVideoActivity.class));
					}
				});
		findViewById(R.id.btnSingleVideo).setOnClickListener(
				new OnClickListener() {

					@Override
					public void onClick(View v) {
						startActivity(new Intent(getApplication(),
								SimpleActivity.class));

					}
				});

		findViewById(R.id.btnMutiView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imageView(R.drawable.muti_1);				
			}
		});
		findViewById(R.id.btnMutiView2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imageView(R.drawable.muti_2);				
			}
		});
		findViewById(R.id.btnSigleView1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imageView(R.drawable.single_1);				
			}
		});
		findViewById(R.id.btnSigleView2).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imageView(R.drawable.single_2);				
			}
		});
		findViewById(R.id.btnSigleView3).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imageView(R.drawable.single_3);				
			}
		});
		findViewById(R.id.btnVod).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(GuideActivity.this, VodActivity.class));
			}
		});
	}
	
	private void imageView(int res){
		imageView.setVisibility(View.VISIBLE);
		imageView.setImageResource(res);
	}
	
	@Override
	public void onBackPressed() {
		if (imageView.getVisibility() == View.VISIBLE) {
			imageView.setVisibility(View.GONE);
		} else {
			// 一般在程序退出的时候调用
			GSDemoApp.getIns().releaseVodeSite();
			super.onBackPressed();
		}
	}
}
