package com.llc.performit;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.llc.performit.common.Constants;
import com.llc.performit.common.Global;
import com.llc.performit.common.Utils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class BubblesPopupActivity extends Activity {
	
	private LinearLayout	btnClose;
	private TextView		tvPurchase;
	private TextView		tvCoins;
	private TextView		tvBubbles;
	private ImageView		btnBubble;
	
	private int				bubbleCnt;
	private int				coins;
	private int				bubbleId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bubble_popup);
		
		btnClose = (LinearLayout) findViewById(R.id.close_linearLayout);
		tvPurchase = (TextView) findViewById(R.id.purchase_textView);
		tvCoins = (TextView) findViewById(R.id.coin_textView);
		tvBubbles = (TextView) findViewById(R.id.bubble_textView);
		btnBubble = (ImageView) findViewById(R.id.bubble_imageView);
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvPurchase.setTypeface(btnFont);
		tvCoins.setTypeface(btnFont);
		tvBubbles.setTypeface(btnFont);
		
		initListener();
		
		Intent intent = getIntent();
		
		bubbleCnt = intent.getIntExtra(Constants.KEY_BUBBLES, 0);
		coins = intent.getIntExtra(Constants.KEY_COINS, 0);
		bubbleId = intent.getIntExtra(Constants.KEY_ID, 0);
		
		tvBubbles.setText(bubbleCnt + "");
		tvCoins.setText(coins + "");
	}
	
	private void initListener() {
		btnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btnBubble.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				purchaseBubble();
			}
		});
	}
	
	private void purchaseBubble() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "purchase_bubbles";
		String params = "app_id=" + Global.mUserData.token + "&bubble_id=" + bubbleId + "&version=" + Utils.getVersion();
		
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
	            Log.d("purchaseBubble HTTP", "onSuccess: " + response);
	            
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
							Utils.showOKDialog(BubblesPopupActivity.this, "Response is null.", "OK");
							return;
						}
						
						int coins = resp.getInt(Constants.KEY_COINS);
						int bubbles = resp.getInt(Constants.KEY_BUBBLES);
						
						Global.mUserData.coins = coins;
						Global.mUserData.bubbles = bubbles;
						
						Global.mUserData.saveUserData(BubblesPopupActivity.this);
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						Utils.showOKDialog(BubblesPopupActivity.this, strResp, "OK");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(BubblesPopupActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("purchaseBubble HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(BubblesPopupActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
}
