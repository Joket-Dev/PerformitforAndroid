package com.llc.performit;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.vending.billing.IInAppBillingService;
import com.llc.performit.common.Constants;
import com.llc.performit.common.Global;
import com.llc.performit.common.Utils;
import com.llc.performit.model.Achievement;
import com.llc.performit.model.PacketItem;
import com.llc.performit.model.UserData;
import com.llc.performit.view.AchievementsListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		Global.mUserData = new UserData();
		Global.mUserData.loadUserData(this);
		
		initInAppPacketsArray();

		getAvailableCoins();
	}

	private void getAvailableCoins() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "coin_list";
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
	            Log.d("getAvailableCoins HTTP", "onSuccess: " + response);
	            
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
							Utils.showOKDialog(SplashActivity.this, "Achievements from server is null.", "OK");
							return;
						}
						
						for(int i = 0; i < list.length(); i++) {
							JSONObject obj = list.getJSONObject(i);
							
						}
						
						hideSplash();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							Utils.showOKDialog(SplashActivity.this, "Something went wrong. Please try again.", "OK");
							break;
						case 62:
							//new version
							Utils.showOKDialog(SplashActivity.this, strResp, "Update");
							break;
						case 2:
							//Invalid app_id
							Utils.showOKDialog(SplashActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK");
							break;
						default:
							Utils.showOKDialog(SplashActivity.this, strResp, "OK");
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(SplashActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("getAvailableCoins HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(SplashActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void initInAppPacketsArray() {
		Global.mInAppPacketsArray = new ArrayList<PacketItem>();
		
		PacketItem item = new PacketItem();
		item.packet = "allcolors";
		item.packetId = Constants.kColorsAllPackage;
		Global.mInAppPacketsArray.add(item);
		
		item = new PacketItem();
		item.packet = "limitedcolors";
		item.packetId = Constants.kColors1Package;
		Global.mInAppPacketsArray.add(item);
		
		item = new PacketItem();
		item.packet = "allbrushes";
		item.packetId = Constants.kBrushesAllPackage;
		Global.mInAppPacketsArray.add(item);
		
		item = new PacketItem();
		item.packet = "limitedbrushes";
		item.packetId = Constants.kBrushes1Package;
		Global.mInAppPacketsArray.add(item);
		
		item = new PacketItem();
		item.packet = "backgroundcolor";
		item.packetId = Constants.kBackgroundColorPackage;
		Global.mInAppPacketsArray.add(item);
		
		item = new PacketItem();
		item.packet = "15bubbles";
		item.packetId = Constants.k15BubblesPackage;
		Global.mInAppPacketsArray.add(item);
		
		item = new PacketItem();
		item.packet = "30bubbles";
		item.packetId = Constants.k30BubblesPackage;
		Global.mInAppPacketsArray.add(item);
		
		item = new PacketItem();
		item.packet = "timer2minutes";
		item.packetId = Constants.kTimer2minPackage;
		Global.mInAppPacketsArray.add(item);
		
		item = new PacketItem();
		item.packet = "timer5minutes";
		item.packetId = Constants.kTimer5minPackage;
		Global.mInAppPacketsArray.add(item);
		
		item = new PacketItem();
		item.packet = "timerinfinite";
		item.packetId = Constants.kTimerInfinitePackage;
		Global.mInAppPacketsArray.add(item);
		
		item = new PacketItem();
		item.packet = "timerstop";
		item.packetId = Constants.kTimerStopPackage;
		Global.mInAppPacketsArray.add(item);
		
		item = new PacketItem();
		item.packet = "hint";
		item.packetId = Constants.kHintPackage;
		Global.mInAppPacketsArray.add(item);
	}
	
	private void hideSplash() {
		if(!Utils.getPreBool(this, Constants.KEY_VALID_LOGIN)) {
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			
			finish();
		}
		else {
			Intent intent = new Intent(this, HomeActivity.class);
			startActivity(intent);
			
			finish();
		}
	}
	
}
