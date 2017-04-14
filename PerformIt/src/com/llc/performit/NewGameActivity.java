package com.llc.performit;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.llc.performit.common.Constants;
import com.llc.performit.common.Global;
import com.llc.performit.common.Utils;
import com.llc.performit.model.WordItem;
import com.llc.performit.view.CustomDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class NewGameActivity extends Activity {
	
	private RelativeLayout	createLayout;
	private TextView		tvCreate;
	private ImageView		btnEmail;
	private ImageView		btnFacebook;
	private ImageView		btnUsername;
	private ImageView		btnRandom;
	private LinearLayout	btnGameClose;
	
	private RelativeLayout	emailLayout;
	private TextView		tvEmail;
	private EditText		etEmail;
	private TextView		btnSendEmail;
	private LinearLayout	btnEmailClose;
	
	private RelativeLayout	usernameLayout;
	private TextView		tvUsername;
	private EditText		etUsername;
	private TextView		btnSendUsername;
	private LinearLayout	btnUsernameClose;
	
	private RelativeLayout	facebookLayout;
	private TextView		tvFacebook;
	private ListView		facebookListView;
	private LinearLayout	btnFacebookClose;
	
	public static NewGameActivity	activity;
	
	private int				opponentID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_game);
		
		createLayout = (RelativeLayout) findViewById(R.id.create_game_layout);
		tvCreate = (TextView) findViewById(R.id.create_game_textView);
		btnEmail = (ImageView) findViewById(R.id.mail_imageView);
		btnFacebook = (ImageView) findViewById(R.id.facebook_imageView);
		btnUsername = (ImageView) findViewById(R.id.username_imageView);
		btnRandom = (ImageView) findViewById(R.id.random_imageView);
		btnGameClose = (LinearLayout) findViewById(R.id.game_close_linearLayout);
		
		emailLayout = (RelativeLayout) findViewById(R.id.email_layout);
		tvEmail = (TextView) findViewById(R.id.create_email_textView);
		etEmail = (EditText) findViewById(R.id.email_editText);
		btnSendEmail = (TextView) findViewById(R.id.send_email_textView);
		btnEmailClose = (LinearLayout) findViewById(R.id.email_close_linearLayout);
		
		usernameLayout = (RelativeLayout) findViewById(R.id.username_layout);
		tvUsername = (TextView) findViewById(R.id.create_username_textView);
		etUsername = (EditText) findViewById(R.id.username_editText);
		btnSendUsername = (TextView) findViewById(R.id.send_username_textView);
		btnUsernameClose = (LinearLayout) findViewById(R.id.username_close_linearLayout);
		
		facebookLayout = (RelativeLayout) findViewById(R.id.facebook_layout);
		tvFacebook = (TextView) findViewById(R.id.create_facebook_textView);
		facebookListView = (ListView) findViewById(R.id.facebook_listView);
		btnFacebookClose = (LinearLayout) findViewById(R.id.facebook_close_linearLayout);
		
		activity = this;
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvCreate.setTypeface(btnFont);
		tvEmail.setTypeface(btnFont);
		btnSendEmail.setTypeface(btnFont);
		tvUsername.setTypeface(btnFont);
		btnSendUsername.setTypeface(btnFont);
		tvFacebook.setTypeface(btnFont);
		
		initListener();
	}

	private void initListener() {
		btnGameClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btnEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createLayout.setVisibility(View.GONE);
				emailLayout.setVisibility(View.VISIBLE);
			}
		});
		
		btnFacebook.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createLayout.setVisibility(View.GONE);
				facebookLayout.setVisibility(View.VISIBLE);
			}
		});
		
		btnUsername.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createLayout.setVisibility(View.GONE);
				usernameLayout.setVisibility(View.VISIBLE);
			}
		});
		
		btnRandom.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Intent intent = new Intent(NewGameActivity.this, GameTypeActivity.class);
//				startActivity(intent);
				
				opponentID = -1;
				getGameWords(opponentID, false, Global.mUserData.token, Utils.getVersion());
			}
		});
		
		btnEmailClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createLayout.setVisibility(View.VISIBLE);
				emailLayout.setVisibility(View.GONE);
			}
		});
		
		btnFacebookClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createLayout.setVisibility(View.VISIBLE);
				facebookLayout.setVisibility(View.GONE);
			}
		});
		
		btnUsernameClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				createLayout.setVisibility(View.VISIBLE);
				usernameLayout.setVisibility(View.GONE);
			}
		});
		
		btnSendEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String email = etEmail.getEditableText().toString();
				if(email == null || email.length() == 0) {
					Utils.showOKDialog(NewGameActivity.this, "Please input the email and try again.", "OK");
					return;
				}
				
				if(email.equalsIgnoreCase(Global.mUserData.email)) {
					Utils.showOKDialog(NewGameActivity.this, "This is your email. Please use another one.", "OK");
					return;
				}
				
				checkUser("email");
			}
		});
		
		btnSendUsername.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String username = etUsername.getEditableText().toString();
				if(username == null || username.length() == 0) {
					Utils.showOKDialog(NewGameActivity.this, "Please input the username and try again.", "OK");
					return;
				}
				
				if(Global.mUserData.loginType.equalsIgnoreCase("CU") && username.equalsIgnoreCase(Global.mUserData.username)) {
					Utils.showOKDialog(NewGameActivity.this, "This is your username. Please use another one.", "OK");
					return;
				}
				
				checkUser("username");
			}
		});
	}
	
	private void checkUser(String type) {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String deviceId = Utils.getDeviceId(this);
		String version = Utils.getVersion();
		
		String function = "check_user";
		String params = "field=" + type + "&data=" + etUsername.getText().toString() + "&app_id=" + Global.mUserData.token + "&version=" + version;
		
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
	            Log.d("checkUser HTTP", "onSuccess: " + response);
	            
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
						JSONObject resp = jResponse.getJSONObject(Constants.KEY_RESPONSE);
						if(resp == null) {
							Utils.showOKDialog(NewGameActivity.this, "Not valid json object", "OK");
							return;
						}
						
						opponentID = resp.getInt(Constants.KEY_USER_ID);
						
						getGameWords(opponentID, false, Global.mUserData.token, Utils.getVersion());
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(NewGameActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(NewGameActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(NewGameActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						case 31:
							//User doesn't exits
							showConfirmDialog(NewGameActivity.this, strResp, "OK", Constants.userIsMissingAlert);
							break;

						default:
							Utils.showOKDialog(NewGameActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(NewGameActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("checkUser HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(NewGameActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void getGameWords(final int oppenetId, boolean bubbles, String token, String version) {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "get_game_words";
		String params = "player_id=" + oppenetId + "&app_id=" + token + "&check_bubbles=" + (bubbles ? 1 : 0) + "&version=" + version;
		
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
	            Log.d("getGameWords HTTP", "onSuccess: " + response);
	            
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
						JSONObject resp = jResponse.getJSONObject(Constants.KEY_RESPONSE);
						if(resp == null) {
							Utils.showOKDialog(NewGameActivity.this, "Word is null", "OK");
							return;
						}
						
						JSONObject audioObj = resp.getJSONObject(Constants.KEY_AUDIO);
						JSONObject imageObj = resp.getJSONObject(Constants.KEY_IMAGE);
						JSONObject videoObj = resp.getJSONObject(Constants.KEY_VIDEO);
						
						WordItem audioWord = Utils.parseWord(audioObj);
						WordItem imageWord = Utils.parseWord(imageObj);
						WordItem videoWord = Utils.parseWord(videoObj);
						
						Global.mAudioWord = audioWord;
						Global.mImageWord = imageWord;
						Global.mVideoWord = videoWord;
						
						Intent intent = new Intent(NewGameActivity.this, GameTypeActivity.class);
						intent.putExtra(Constants.KEY_OPPONENT_ID, oppenetId);
						intent.putExtra(Constants.KEY_GAME_ID, -1);
						intent.putExtra(Constants.KEY_CONTINUE_GAME, false);
						
						startActivity(intent);
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(NewGameActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(NewGameActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(NewGameActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						case 34:
							//Value must be numeric.
							showConfirmDialog(NewGameActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;

						default:
							Utils.showOKDialog(NewGameActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(NewGameActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("getGameWords HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(NewGameActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
						Intent intent = new Intent(NewGameActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
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
