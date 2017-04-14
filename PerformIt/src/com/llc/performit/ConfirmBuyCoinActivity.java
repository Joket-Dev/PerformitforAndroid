package com.llc.performit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class ConfirmBuyCoinActivity extends Activity {
	
	private TextView	tvMsg;
	private TextView	tvNo;
	private TextView	tvYes;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_confirm_buy_coin);
		
		tvMsg = (TextView) findViewById(R.id.msg_textView);
		tvNo = (TextView) findViewById(R.id.no_textView);
		tvYes = (TextView) findViewById(R.id.yes_textView);
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvMsg.setTypeface(btnFont);
		tvNo.setTypeface(btnFont);
		tvYes.setTypeface(btnFont);
		
		initListener();
	}
	
	private void initListener() {
		tvNo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		tvYes.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ConfirmBuyCoinActivity.this, CoinActivity.class);
				startActivity(intent);
				
				finish();
			}
		});
	}
}
