package com.llc.performit;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends Activity {
	
	private EditText	etUsername;
	private ImageView	btnChange;
	private TextView	tvCoinsLabel;
	private TextView	tvCoinsCnt;
	private ImageView	btnAbout;
	private ImageView	btnLogout;
	private LinearLayout	btnClose;
	
	public static SettingsActivity	settingsActivity;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		
		etUsername = (EditText) findViewById(R.id.name_editText);
		btnChange = (ImageView) findViewById(R.id.change_imageView);
		tvCoinsLabel = (TextView) findViewById(R.id.coin_label_textView);
		tvCoinsCnt = (TextView) findViewById(R.id.coin_count_textView);
		btnAbout = (ImageView) findViewById(R.id.about_imageView);
		btnLogout = (ImageView) findViewById(R.id.logout_imageView);
		btnClose = (LinearLayout) findViewById(R.id.close_linearLayout);
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvCoinsLabel.setTypeface(btnFont);
		tvCoinsCnt.setTypeface(btnFont);
		
		settingsActivity = this;
		
		etUsername.setText(Global.mUserData.name);
		tvCoinsCnt.setText(Global.mUserData.coins + "");
		
		initListener();
	}
	
	private void initListener() {
		btnAbout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingsActivity.this, AboutActivity.class);
				startActivity(intent);
			}
		});
		
		btnLogout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog dialog = new Dialog(SettingsActivity.this);
				CustomDialog.Builder customBuilder = new CustomDialog.Builder(SettingsActivity.this);
				customBuilder.setTitle("title");
				customBuilder.setMessage("Are you sure you want to logout?");
				customBuilder.setNegativeButton("No" ,
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				customBuilder.setPositiveButton("Yes" ,
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
						logout();
					}
				});
				
				dialog = customBuilder.create();
				dialog.setCancelable(false);
				dialog.show();
			}
		});
		
		btnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btnChange.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String name = etUsername.getText().toString();
				if(name.equalsIgnoreCase(Global.mUserData.name))
					return;
				
				saveSettings();
			}
		});
	}
	
	private void logout() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "logout";
		String params = "app_id=" + Global.mUserData.token + "&version=" + Utils.getVersion();
		
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
	            Log.d("logout HTTP", "onSuccess: " + response);
	            
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
						Global.mUserData.token = "";
						Global.mUserData.username = "";
						Global.mUserData.userID = 0;
						Global.mUserData.loginType = "";
						Global.mUserData.coins = 0;
						Global.mUserData.validLogin = false;
						Global.mUserData.bubbles = 0;
						
						Global.mUserData.saveUserData(SettingsActivity.this);
						
						Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
						startActivity(intent);
						
						finish();
						HomeActivity.homeActivity.finish();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							Utils.showOKDialog(SettingsActivity.this, "Something went wrong. Please try again.", "OK");
							break;
						case 62:
							//new version
							Utils.showOKDialog(SettingsActivity.this, strResp, "Update");
							break;
						case 2:
							//Invalid app_id
							Utils.showOKDialog(SettingsActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK");
							break;
						default:
							Utils.showOKDialog(SettingsActivity.this, strResp, "OK");
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(SettingsActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("logout HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(SettingsActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void saveSettings() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "save_profile";
		
		final String name = etUsername.getText().toString();
		String[] fullname = name.split(" ");
		final String firstname = fullname[0];
		final String lastname;
		
		if(fullname.length > 1)
			lastname = fullname[1];
		else
			lastname = "";
		
		String params = "firstname=" + firstname + "&lastname=" + lastname + "&app_id=" + Global.mUserData.token + "&version=" + Utils.getVersion();
		
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
	            Log.d("logout HTTP", "onSuccess: " + response);
	            
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
						Global.mUserData.name = name;
						Global.mUserData.firstname = firstname;
						Global.mUserData.lastname = lastname;
						
						Global.mUserData.saveUserData(SettingsActivity.this);
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							Utils.showOKDialog(SettingsActivity.this, "Something went wrong. Please try again.", "OK");
							break;
						case 62:
							//new version
							Utils.showOKDialog(SettingsActivity.this, strResp, "Update");
							showConfirmDialog(SettingsActivity.this, strResp, "OK", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(SettingsActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						default:
							Utils.showOKDialog(SettingsActivity.this, strResp, "OK");
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(SettingsActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("logout HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(SettingsActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
					case Constants.invalidAppIDAlert:
						finish();

						Intent intent = new Intent(SettingsActivity.this, LoginActivity.class);
						startActivity(intent);
						break;
					case Constants.apiErrorAlert:
						break;
				}
			}
		});
		
		dialog = customBuilder.create();
		dialog.setCancelable(false);
		
		dialog.show();
	}
}
