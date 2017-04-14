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
import com.llc.performit.model.Achievement;
import com.llc.performit.model.LeaderboardItem;
import com.llc.performit.view.AchievementsListAdapter;
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

public class AchievementsActivity extends Activity {
	
	private ImageView		btnHome;
	private ListView		mListView;
	
	private ArrayList<Achievement>	mAchievementList;
	private AchievementsListAdapter	adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_achievments);
		
		btnHome = (ImageView) findViewById(R.id.home_imageView);
		mListView = (ListView) findViewById(R.id.listView);
		
		initListener();
		
//		testLoadAchievementList();
		loadAchievementList();
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
	
	private void loadAchievementList() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "get_achievements";
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
	            Log.d("loadAchievementList HTTP", "onSuccess: " + response);
	            
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
						mAchievementList = new ArrayList<Achievement>();
						
						JSONArray list = jResponse.getJSONArray(Constants.KEY_RESPONSE);
						if(list == null) {
							Utils.showOKDialog(AchievementsActivity.this, "Achievements from server is null.", "OK");
							return;
						}
						
						for(int i = 0; i < list.length(); i++) {
							JSONObject obj = list.getJSONObject(i);
							
							Achievement item = new Achievement();
							
							item.name = obj.getString(Constants.KEY_NAME);
							item.achieved = obj.getInt(Constants.KEY_ACHIEVE);
							item.target = obj.getInt(Constants.KEY_TARGET);
							item.typeId = obj.getInt(Constants.KEY_ACHIEVEMENT_TYPE_ID);
							item.coins = obj.getInt(Constants.KEY_COINS);
							item.description = obj.getString(Constants.KEY_DESCRIPTION);
							item.entryId = obj.getInt(Constants.KEY_ACHIEVEMENT_ENTRY_ID);
							
							mAchievementList.add(item);	
						}
						
						adapter = new AchievementsListAdapter(AchievementsActivity.this, mAchievementList);
						mListView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(AchievementsActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(AchievementsActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(AchievementsActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						default:
							Utils.showOKDialog(AchievementsActivity.this, strResp, "OK");
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(AchievementsActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("loadAchievementList HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(AchievementsActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void testLoadAchievementList() {
		mAchievementList = new ArrayList<Achievement>();
		
		Achievement item = new Achievement();
		item.name = "Achievement " + 1;
		item.achieved = 20;
		item.target = 20;
		item.typeId = 1;
		item.coins = 20;
		mAchievementList.add(item);	
		
		item = new Achievement();
		item.name = "Achievement " + 2;
		item.achieved = 10;
		item.target = 20;
		item.typeId = 2;
		item.coins = 10;
		mAchievementList.add(item);	
		
		item = new Achievement();
		item.name = "Achievement " + 3;
		item.achieved = 10;
		item.target = 20;
		item.typeId = 3;
		item.coins = 10;
		mAchievementList.add(item);	
		
		item = new Achievement();
		item.name = "Achievement " + 4;
		item.achieved = 10;
		item.target = 10;
		item.typeId = 4;
		item.coins = 10;
		mAchievementList.add(item);	
		
		item = new Achievement();
		item.name = "Achievement " + 5;
		item.achieved = 30;
		item.target = 30;
		item.typeId = 5;
		item.coins = 30;
		mAchievementList.add(item);	
		
		adapter = new AchievementsListAdapter(this, mAchievementList);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
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
						Intent intent = new Intent(AchievementsActivity.this, LoginActivity.class);
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
