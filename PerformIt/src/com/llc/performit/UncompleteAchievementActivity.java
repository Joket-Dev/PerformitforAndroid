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

public class UncompleteAchievementActivity extends Activity {
	
	private TextView	tvLabelProgress;
	private TextView	tvProgress;
	private TextView	tvName;
	private TextView	tvAchievement;
	private ImageView	achieveImageView;
	private TextView	tvDescription;
	private TextView	tvCoins;
	private LinearLayout	btnClose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.uncomplete_achievement_layout);
		
		tvLabelProgress = (TextView) findViewById(R.id.progress_label_textView);
		tvProgress = (TextView) findViewById(R.id.progress_textView);
		tvName = (TextView) findViewById(R.id.name_textView);
		tvAchievement = (TextView) findViewById(R.id.achieve_textView);
		achieveImageView = (ImageView) findViewById(R.id.achieve_imageView);
		tvDescription = (TextView) findViewById(R.id.description_textView);
		tvCoins = (TextView) findViewById(R.id.coin_textView);
		btnClose = (LinearLayout) findViewById(R.id.close_linearLayout);
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvLabelProgress.setTypeface(btnFont);
		tvProgress.setTypeface(btnFont);
		tvName.setTypeface(btnFont);
		tvAchievement.setTypeface(btnFont);
		tvDescription.setTypeface(btnFont);
		tvCoins.setTypeface(btnFont);
		
		Intent intent = getIntent();
		
		tvProgress.setText(intent.getStringExtra(Constants.KEY_PROGRESS));
		tvName.setText(intent.getStringExtra(Constants.KEY_NAME));
		tvCoins.setText(intent.getIntExtra(Constants.KEY_COINS, 0) + "");
		tvDescription.setText(intent.getStringExtra(Constants.KEY_DESCRIPTION));
		
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
