package com.llc.performit;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.llc.performit.common.Constants;
import com.llc.performit.common.Global;
import com.llc.performit.common.Utils;
import com.llc.performit.model.LeaderboardItem;
import com.llc.performit.view.CustomDialog;
import com.llc.performit.view.LeaderboardListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class LeaderboardActivity extends Activity {
	
	private ImageView	btnHome;
	private ListView	mListView;
	
	private ArrayList<LeaderboardItem>	mItemList;
	private LeaderboardListAdapter		adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_leaderboard);
		
		btnHome = (ImageView) findViewById(R.id.home_imageView);
		mListView = (ListView) findViewById(R.id.listView);
		
		initListener();
		
//		testLoadLeaderboard();
		loadLeaderboard();
	}
	
	private void initListener() {
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	
	private void loadLeaderboard() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "get_leaderboard";
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
	            Log.d("loadLeaderboard HTTP", "onSuccess: " + response);
	            
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
						JSONArray list = jResponse.getJSONArray(Constants.KEY_RESPONSE);
						if(list == null) {
							Utils.showOKDialog(LeaderboardActivity.this, "Leaderboard from server is null.", "OK");
							return;
						}
						
						mItemList = new ArrayList<LeaderboardItem>();
						
						for(int i = 0; i < list.length(); i++) {
							JSONObject obj = list.getJSONObject(i);
							
							LeaderboardItem item = new LeaderboardItem();
							
							item.name = obj.getString(Constants.KEY_NAME);
							item.score = obj.getInt(Constants.KEY_COINS);
							item.pictureId = obj.getString(Constants.KEY_PICTURE);
							
							mItemList.add(item);
						}
						
						adapter = new LeaderboardListAdapter(LeaderboardActivity.this, mItemList);
						mListView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(LeaderboardActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(LeaderboardActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(LeaderboardActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						default:
							Utils.showOKDialog(LeaderboardActivity.this, strResp, "OK");
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(LeaderboardActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("loadLeaderboard HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(LeaderboardActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
						Intent intent = new Intent(LeaderboardActivity.this, LoginActivity.class);
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
	
	private void testLoadLeaderboard() {
		mItemList = new ArrayList<LeaderboardItem>();
		
		for(int i = 0; i < 10; i++) {
			LeaderboardItem item = new LeaderboardItem();
			
			item.name = "User" + (i + 1);
			item.score = i * 1000;
			mItemList.add(item);
		}
		
		adapter = new LeaderboardListAdapter(this, mItemList);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}
