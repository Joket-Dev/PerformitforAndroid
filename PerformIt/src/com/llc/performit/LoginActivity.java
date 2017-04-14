package com.llc.performit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.ImageView;

public class LoginActivity extends Activity {
	
	private ImageView		btnLogin;
	private ImageView		btnFacebook;
	
	public static LoginActivity		loginActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		btnLogin = (ImageView) findViewById(R.id.login_imageView);
		btnFacebook = (ImageView) findViewById(R.id.facebook_imageView);
		
		loginActivity = this;
		
		initListener();
	}

	private void initListener() {
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(LoginActivity.this, LoginPopupActivity.class);
				startActivity(intent);
			}
		});
		
		btnFacebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	
}
