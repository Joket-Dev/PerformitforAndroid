package com.llc.performit;

import com.llc.performit.common.Constants;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CompleteAchievementActivity extends Activity {
	
	private TextView		tvName;
	private TextView		tvAchieve;
	private ImageView		achieveImageView;
	private TextView		tvCoins;
	private LinearLayout	btnClose;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complete_achievement_layout);
		
		tvName = (TextView) findViewById(R.id.name_textView);
		tvAchieve = (TextView) findViewById(R.id.achieve_textView);
		achieveImageView = (ImageView) findViewById(R.id.achieve_imageView);
		tvCoins = (TextView) findViewById(R.id.coin_textView);
		btnClose = (LinearLayout) findViewById(R.id.close_linearLayout);
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvName.setTypeface(btnFont);
		tvAchieve.setTypeface(btnFont);
		tvCoins.setTypeface(btnFont);
		
		Intent intent = getIntent();
		
		tvName.setText(intent.getStringExtra(Constants.KEY_NAME));
		tvCoins.setText(intent.getIntExtra(Constants.KEY_COINS, 0) + "");

		initListener();
	}
	
	private void initListener() {
		btnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
