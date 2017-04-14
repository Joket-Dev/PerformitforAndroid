package com.llc.performit;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.llc.performit.common.Constants;
import com.llc.performit.common.Global;
import com.llc.performit.common.Utils;
import com.llc.performit.model.UserData;
import com.llc.performit.view.CustomDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LoginPopupActivity extends Activity {
	
	private RelativeLayout	loginLayout;
	private TextView		tvLoginTitle;
	private EditText		etLoginUsername;
	private EditText		etLoginPassword;
	private ImageView		btnLogin;
	private ImageView		btnLoginRegister;
	private ImageView		btnLoginForgot;
	private RelativeLayout	registerLayout;
	private TextView		tvRegisterTitle;
	private EditText		etRegisterUsername;
	private EditText		etRegisterPassword;
	private EditText		etRegisterEmail;
	private ImageView		btnRegister;
	private RelativeLayout	forgotLayout;
	private TextView		tvForgotTitle;
	private EditText		etForgotEmail;
	private ImageView		btnForgot;
	private LinearLayout	btnLoginClose;
	private LinearLayout	btnRegisterClose;
	private LinearLayout	btnForgotClose;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_popup);
		
		loginLayout = (RelativeLayout) findViewById(R.id.login_layout);
		tvLoginTitle = (TextView) findViewById(R.id.loginpopup_title_textView);
		etLoginUsername = (EditText) findViewById(R.id.login_username_editText);
		etLoginPassword = (EditText) findViewById(R.id.login_password_editText);
		btnLogin = (ImageView) findViewById(R.id.login_button);
		btnLoginRegister = (ImageView) findViewById(R.id.login_register_button);
		btnLoginForgot = (ImageView) findViewById(R.id.login_forgot_button);
		registerLayout = (RelativeLayout) findViewById(R.id.register_layout);
		tvRegisterTitle = (TextView) findViewById(R.id.registerpopup_title_textView);
		etRegisterUsername = (EditText) findViewById(R.id.register_username_editText);
		etRegisterPassword = (EditText) findViewById(R.id.register_password_editText);
		etRegisterEmail = (EditText) findViewById(R.id.register_email_editText);
		btnRegister = (ImageView) findViewById(R.id.register_button);
		forgotLayout = (RelativeLayout) findViewById(R.id.forgot_layout);
		tvForgotTitle = (TextView) findViewById(R.id.forgotpopup_title_textView);
		etForgotEmail = (EditText) findViewById(R.id.forgot_email_editText);
		btnForgot = (ImageView) findViewById(R.id.forgot_button);
		btnLoginClose = (LinearLayout) findViewById(R.id.login_close);
		btnRegisterClose = (LinearLayout) findViewById(R.id.register_close);
		btnForgotClose = (LinearLayout) findViewById(R.id.forgot_close);
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvLoginTitle.setTypeface(btnFont);
		tvRegisterTitle.setTypeface(btnFont);
		tvForgotTitle.setTypeface(btnFont);
		
		initListener();
	}
	
	private void initListener() {
		btnLogin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String username = etLoginUsername.getEditableText().toString();
				String password = etLoginPassword.getEditableText().toString();
				
				if(username == null || username.length() == 0) {
					Utils.showOKDialog(LoginPopupActivity.this, "Please input the username and try again.", "OK");
					return;
				}
				
				if(password == null || password.length() == 0) {
					Utils.showOKDialog(LoginPopupActivity.this, "Please input the password and try again.", "OK");
					
					return;
				}
				
				login();
			}
		});
		
		btnLoginClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btnRegisterClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				registerLayout.setVisibility(View.GONE);
				loginLayout.setVisibility(View.VISIBLE);
			}
		});
		
		btnForgotClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				forgotLayout.setVisibility(View.GONE);
				loginLayout.setVisibility(View.VISIBLE);
			}
		});
		
		btnLoginRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginLayout.setVisibility(View.GONE);
				registerLayout.setVisibility(View.VISIBLE);
			}
		});
		
		btnLoginForgot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				loginLayout.setVisibility(View.GONE);
				forgotLayout.setVisibility(View.VISIBLE);
			}
		});
		
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = etRegisterUsername.getEditableText().toString();
				String password = etRegisterPassword.getEditableText().toString();
				String email = etRegisterEmail.getEditableText().toString();
				
				if(username == null || username.length() == 0) {
					Utils.showOKDialog(LoginPopupActivity.this, "Please input the username and try again.", "OK");
					
					return;
				}
				
				if(password == null || password.length() == 0) {
					Utils.showOKDialog(LoginPopupActivity.this, "Please input the password and try again.", "OK");
					
					return;
				}
				
				if(email == null || email.length() == 0) {
					Utils.showOKDialog(LoginPopupActivity.this, "Please input the email and try again.", "OK");
					
					return;
				}
				
//				Intent intent = new Intent(LoginPopupActivity.this, HomeActivity.class);
//				startActivity(intent);
//				
//				LoginActivity.loginActivity.finish();
//				finish();
				
				register();
			}
		});
		
		btnForgot.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = etForgotEmail.getEditableText().toString();
				
				if(email == null || email.length() == 0) {
					Utils.showOKDialog(LoginPopupActivity.this, "Please input the email and try again.", "OK");
					
					return;
				}
				
				forgotPassword();
			}
		});
	}
	
	private void login() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String deviceId = Utils.getDeviceId(this);
		String version = Utils.getVersion();
		
		String function = "login_custom";
		String params = "device_id=" + deviceId + "&password=" + etLoginPassword.getText().toString() + "&username=" + etLoginUsername.getText().toString() + "&version=" + version;
		
		String secretParam = Utils.makeSecretKey(params);
		
		String requestString = function + "&" + params;
		requestString += "&validation_hash=" + secretParam;
		
		StringEntity entity = null;
		try {
			entity = new StringEntity(requestString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		httpClient.post(this, Constants.BASE_URL + "/" + function, entity, "application/x-www-form-urlencoded", new AsyncHttpResponseHandler() {
			@Override
	        public void onSuccess(String response) {
	            Log.d("Log In HTTP", "onSuccess: " + response);
	            
	            if(progress.isShowing())
					progress.dismiss();
	            
	            JSONObject jResponse = null;
	            boolean success = false;
	            int code = 0;
	            JSONObject resp = null;
	            
	            try {
					jResponse = new JSONObject(response);
					
					success = jResponse.getBoolean(Constants.KEY_SUCCESS);
					code = jResponse.getInt(Constants.KEY_CODE);
					
					if(success) {
						Global.mUserData = new UserData();
						resp = jResponse.getJSONObject(Constants.KEY_RESPONSE);
						
						if(resp != null) {
							Global.mUserData.token = resp.getString(Constants.KEY_TOKEN);
							Global.mUserData.username = resp.getString(Constants.KEY_USERNAME);
							Global.mUserData.userID = resp.getInt(Constants.KEY_ID);
							Global.mUserData.loginType = resp.getString(Constants.KEY_LOGIN_TYPE);
							Global.mUserData.coins = resp.getInt(Constants.KEY_COINS);
							Global.mUserData.validLogin = true;
							Global.mUserData.bubbles = resp.getInt(Constants.KEY_BUBBLES);
							
							if(!resp.isNull(Constants.KEY_FIRSTNAME) && !resp.isNull(Constants.KEY_LASTNAME)) {
								Global.mUserData.firstname = resp.getString(Constants.KEY_FIRSTNAME);
								Global.mUserData.lastname = resp.getString(Constants.KEY_LASTNAME);
								
								if(Global.mUserData.firstname.isEmpty() || Global.mUserData.lastname.isEmpty())
									Global.mUserData.name = Global.mUserData.username;
								else
									Global.mUserData.name = Global.mUserData.firstname + " " + Global.mUserData.lastname;
							}
							else
								Global.mUserData.name = Global.mUserData.username;
							
							Global.mUserData.saveUserData(LoginPopupActivity.this);
							
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Intent intent = new Intent(LoginPopupActivity.this, HomeActivity.class);
									startActivity(intent);
									
									LoginActivity.loginActivity.finish();
									finish();
								}
							});
						}
						else {
							Utils.showOKDialog(LoginPopupActivity.this, "Response field is null.", "OK");
							return;
						}
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						switch (code) {
						case 1:
							//Invalid validation_hash
							Utils.showOKDialog(LoginPopupActivity.this, "Something went wrong. Please try again.", "OK");
							break;
						case 62:
							//new version
							Utils.showOKDialog(LoginPopupActivity.this, strResp, "Update");
							break;
						case 12:
							//invalid username/password
							Utils.showOKDialog(LoginPopupActivity.this, strResp, "OK");
							break;
						case 14:
							//accound disabled
							Utils.showOKDialog(LoginPopupActivity.this, strResp, "OK");
							break;
						case 5:
							//Invalid device_id
							Utils.showOKDialog(LoginPopupActivity.this, strResp, "OK");
							break;

						default:
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(LoginPopupActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("Log In HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(LoginPopupActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void register() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String deviceId = Utils.getDeviceId(this);
		String version = Utils.getVersion();
		
		String function = "register";
		String params = "device_id=" + deviceId + "&email=" + etRegisterEmail.getText().toString() + "&password=" + etLoginPassword.getText().toString() + "&username=" + etLoginUsername.getText().toString() + "&version=" + version;
		
		String secretParam = Utils.makeSecretKey(params);
		
		String requestString = function + "&" + params;
		requestString += "&validation_hash=" + secretParam;
		
		StringEntity entity = null;
		try {
			entity = new StringEntity(requestString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		httpClient.post(this, Constants.BASE_URL + "/" + function, entity, "application/x-www-form-urlencoded", new AsyncHttpResponseHandler() {
			@Override
	        public void onSuccess(String response) {
	            Log.d("Register HTTP", "onSuccess: " + response);
	            
	            if(progress.isShowing())
					progress.dismiss();
	            
	            JSONObject jResponse = null;
	            boolean success = false;
	            int code = 0;
	            JSONObject resp = null;
	            
	            try {
					jResponse = new JSONObject(response);
					
					success = jResponse.getBoolean(Constants.KEY_SUCCESS);
					code = jResponse.getInt(Constants.KEY_CODE);
					
					if(success) {
						Global.mUserData = new UserData();
						resp = jResponse.getJSONObject(Constants.KEY_RESPONSE);
						
						if(resp != null) {
							Global.mUserData.token = resp.getString(Constants.KEY_TOKEN);
							Global.mUserData.username = resp.getString(Constants.KEY_USERNAME);
							Global.mUserData.userID = resp.getInt(Constants.KEY_ID);
							Global.mUserData.loginType = resp.getString(Constants.KEY_LOGIN_TYPE);
							Global.mUserData.coins = resp.getInt(Constants.KEY_COINS);
							Global.mUserData.validLogin = true;
							
							runOnUiThread(new Runnable() {
								
								@Override
								public void run() {
									// TODO Auto-generated method stub
									Intent intent = new Intent(LoginPopupActivity.this, HomeActivity.class);
									startActivity(intent);
									
									LoginActivity.loginActivity.finish();
									finish();
								}
							});
						}
						else {
							Utils.showOKDialog(LoginPopupActivity.this, "Response field is null.", "OK");
							return;
						}
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						switch (code) {
						case 1:
							//Invalid validation_hash
							Utils.showOKDialog(LoginPopupActivity.this, strResp, "OK");
							break;
						case 9:
							//Username not available
							Utils.showOKDialog(LoginPopupActivity.this, strResp, "OK");
							break;
						case 32:
							//Email not available.
							Utils.showOKDialog(LoginPopupActivity.this, strResp, "OK");
							break;

						default:
							Utils.showOKDialog(LoginPopupActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(LoginPopupActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("Register HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(LoginPopupActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void forgotPassword() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String deviceId = Utils.getDeviceId(this);
		String version = Utils.getVersion();
		
		String function = "recover_password";
		String params = "device_id=" + deviceId + "&email=" + etRegisterEmail.getText().toString() + "&version=" + version;
		
		String secretParam = Utils.makeSecretKey(params);
		
		String requestString = function + "&" + params;
		requestString += "&validation_hash=" + secretParam;
		
		StringEntity entity = null;
		try {
			entity = new StringEntity(requestString);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		httpClient.post(this, Constants.BASE_URL + "/" + function, entity, "application/x-www-form-urlencoded", new AsyncHttpResponseHandler() {
			@Override
	        public void onSuccess(String response) {
	            Log.d("forgotPassword HTTP", "onSuccess: " + response);
	            
	            if(progress.isShowing())
					progress.dismiss();
	            
	            JSONObject jResponse = null;
	            boolean success = false;
	            int code = 0;
	            
	            try {
					jResponse = new JSONObject(response);
					
					success = jResponse.getBoolean(Constants.KEY_SUCCESS);
					code = jResponse.getInt(Constants.KEY_CODE);
					
					if(success) {
						Utils.showOKDialog(LoginPopupActivity.this, "A new password was sent to your email address.", "OK");
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						switch (code) {
						case 1:
							//Invalid validation_hash
							Utils.showOKDialog(LoginPopupActivity.this, "Something went wrong. Please try again.", "OK");
							break;
						case 62:
							//new version
							Utils.showOKDialog(LoginPopupActivity.this, strResp, "Update");
							break;
						case 11:
							//Invalid email
							Utils.showOKDialog(LoginPopupActivity.this, strResp, "OK");
							break;

						default:
							Utils.showOKDialog(LoginPopupActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(LoginPopupActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("forgotPassword HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(LoginPopupActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	public void showConfirmDialog(Context context, String message, String okButton, final int dlgId) {
		Dialog dialog = new Dialog(context);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(context);
		customBuilder.setTitle("");
		customBuilder.setMessage(message);
		customBuilder.setNegativeButton(okButton, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				
				switch(dlgId) {
					case Constants.newVersionAlert:
						break;
				}
			}
		});
		
		dialog = customBuilder.create();
		dialog.setCancelable(false);
		
		dialog.show();
	}
}
