package com.llc.performit;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.llc.performit.common.Constants;
import com.llc.performit.common.Global;
import com.llc.performit.common.Utils;
import com.llc.performit.model.PacketItem;
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
import android.widget.ImageView;
import android.widget.TextView;

public class AddMorePlayItemActivity extends Activity {
	
	private TextView		tvTitle;
	private TextView		tvHint;
	private TextView		tv2Min;
	private TextView		tv5Min;
	private TextView		tvInfinite;
	private TextView		tvStop;
	
	private ImageView		btnHint;
	private ImageView		btn2Min;
	private ImageView		btn5Min;
	private ImageView		btnInfinite;
	private ImageView		btnStop;
	private ImageView		btnHome;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_more_play_item);
		
		tvTitle = (TextView) findViewById(R.id.title_textView);
		tvHint = (TextView) findViewById(R.id.hint_textView);
		tv2Min = (TextView) findViewById(R.id.min_2_textView);
		tv5Min = (TextView) findViewById(R.id.min_5_textView);
		tvInfinite = (TextView) findViewById(R.id.infinite_textView);
		tvStop = (TextView) findViewById(R.id.stop_textView);
		btnHint = (ImageView) findViewById(R.id.btn_hint);
		btn2Min = (ImageView) findViewById(R.id.btn_2_min);
		btn5Min = (ImageView) findViewById(R.id.btn_5_min);
		btnInfinite = (ImageView) findViewById(R.id.btn_infinite);
		btnStop = (ImageView) findViewById(R.id.btn_stop);
		btnHome = (ImageView) findViewById(R.id.close_imageView);
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvTitle.setTypeface(btnFont);
		tvHint.setTypeface(btnFont);
		tv2Min.setTypeface(btnFont);
		tv5Min.setTypeface(btnFont);
		tvInfinite.setTypeface(btnFont);
		tvStop.setTypeface(btnFont);
		
		initListener();
		
		loadPacket();
	}
	
	private void initListener() {
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btnHint.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPurchaseConfirDlg(Constants.kHintPackage);
			}
		});
		
		btn2Min.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPurchaseConfirDlg(Constants.kTimer2minPackage);
			}
		});
		
		btn5Min.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPurchaseConfirDlg(Constants.kTimer5minPackage);
			}
		});
		
		btnInfinite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPurchaseConfirDlg(Constants.kTimerInfinitePackage);
			}
		});
		
		btnStop.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showPurchaseConfirDlg(Constants.kTimerStopPackage);
			}
		});
	}
	
	private void loadPacket() {
		boolean purchased = Utils.isPurchasedPacket(Constants.kHintPackage);
		if(purchased)
			btnHint.setImageResource(R.drawable.purchase_button_di);
		else
			btnHint.setImageResource(R.drawable.purchase_button);
		
		purchased = Utils.isPurchasedPacket(Constants.kTimer2minPackage);
		if(purchased)
			btn2Min.setImageResource(R.drawable.purchase_button_di);
		else
			btn2Min.setImageResource(R.drawable.purchase_button);
		
		purchased = Utils.isPurchasedPacket(Constants.kTimer5minPackage);
		if(purchased)
			btn5Min.setImageResource(R.drawable.purchase_button_di);
		else
			btn5Min.setImageResource(R.drawable.purchase_button);
		
		purchased = Utils.isPurchasedPacket(Constants.kTimerInfinitePackage);
		if(purchased)
			btnInfinite.setImageResource(R.drawable.purchase_button_di);
		else
			btnInfinite.setImageResource(R.drawable.purchase_button);
		
		purchased = Utils.isPurchasedPacket(Constants.kTimerStopPackage);
		if(purchased)
			btnStop.setImageResource(R.drawable.purchase_button_di);
		else
			btnStop.setImageResource(R.drawable.purchase_button);
	}
	
	private void showPurchaseConfirDlg(final int packetId) {
		PacketItem packet = null;
		
		for(int i = 0; i < Global.mInAppPacketsArray.size(); i++) {
			PacketItem item = Global.mInAppPacketsArray.get(i);
			
			if(item.packetId == packetId) {
				packet = item;
				break;
			}
		}
		
		if(packet == null) {
			return;
		}
		
		if(packet.purchased == 1)
			return;
		
		if(Global.mUserData.coins < packet.coins) {
			Utils.showOKDialog(this, "You don't have sufficient coins to purchase this item", "OK");
			return;
		}
		
		Dialog dialog = new Dialog(AddMorePlayItemActivity.this);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(AddMorePlayItemActivity.this);
		customBuilder.setTitle("title");
		customBuilder.setMessage(String.format("Do you want to buy %s for %d coins?", packet.name, packet.coins));
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
				
				registerPurchase(packetId);
			}
		});
		
		dialog = customBuilder.create();
		dialog.setCancelable(false);
		dialog.show();
	}
	
	private void registerPurchase(final int packetId) {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "purchase_packet";
		String params = "app_id=" + Global.mUserData.token + "&packet_id=" + packetId + "&version=" + Utils.getVersion();
		
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
	            Log.d("registerPurchase HTTP", "onSuccess: " + response);
	            
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
						PacketItem packet;
						
						for(int i = 0; i < Global.mInAppPacketsArray.size(); i++) {
							packet = Global.mInAppPacketsArray.get(i);
							if(packet.packetId == packetId) {
								packet.purchased = 1;
								break;
							}
						}
						
						loadPacket();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(AddMorePlayItemActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(AddMorePlayItemActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(AddMorePlayItemActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.apiErrorAlert);
							break;
						case 52:
							//invalid  packet.
							showConfirmDialog(AddMorePlayItemActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 53:
							//Packet already purchased.
							showConfirmDialog(AddMorePlayItemActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 54:
							//Error purchasing packet
							showConfirmDialog(AddMorePlayItemActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						default:
							Utils.showOKDialog(AddMorePlayItemActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(AddMorePlayItemActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("registerPurchase HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(AddMorePlayItemActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
						Intent intent = new Intent(AddMorePlayItemActivity.this, LoginActivity.class);
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
