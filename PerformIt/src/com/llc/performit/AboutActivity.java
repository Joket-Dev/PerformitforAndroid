package com.llc.performit;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class AboutActivity extends Activity {
	
	private ImageView		btnHome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		btnHome = (ImageView) findViewById(R.id.home_imageView);
		
		initListener();
	}

	private void initListener() {
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				SettingsActivity.settingsActivity.finish();
			}
		});
	}
}
