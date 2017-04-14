package com.llc.performit;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.llc.performit.common.Global;
import com.llc.performit.model.Coin;
import com.llc.performit.model.LeaderboardItem;
import com.llc.performit.view.CoinsListAdapter;
import com.llc.performit.view.LeaderboardListAdapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

public class CoinActivity extends Activity {

	private ImageView	btnHome;
	private ListView	mListView;

	private ArrayList<Coin>			mItemList;
	private CoinsListAdapter		adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coin);

		btnHome = (ImageView) findViewById(R.id.home_imageView);
		mListView = (ListView) findViewById(R.id.listView);

		initListener();

//		tempLoadCoinsList();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				loadCoinsList();
			}
		}).start();
	}

	private void initListener() {
		btnHome.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				Coin coin = mItemList.get(arg2);
				
				try {
					String sku = coin.coins + "";
					String developerPayload = "bGoa+V7g/yqDXvKRqq+JTFn4uQZbPiQJo4pf9RzJ";
					
					Bundle buyIntentBundle = Global.mService.getBuyIntent(3, getPackageName(), sku, "inapp", developerPayload );
					
					int response = buyIntentBundle.getInt("RESPONSE_CODE");
					if(response == 0) {
						PendingIntent pendingIntent = buyIntentBundle.getParcelable("BUY_INTENT");
						
						try {
							startIntentSenderForResult(pendingIntent.getIntentSender(), 1001, new Intent(), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(0));
						} catch (SendIntentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}
					
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	   if (requestCode == 1001) {
	      int responseCode = data.getIntExtra("RESPONSE_CODE", 0);
	      String purchaseData = data.getStringExtra("INAPP_PURCHASE_DATA");
	      String dataSignature = data.getStringExtra("INAPP_DATA_SIGNATURE");

	      if (resultCode == RESULT_OK) {
	         try {
	            JSONObject jo = new JSONObject(purchaseData);
	            
	            String sku = jo.getString("productId");
	            
	          }
	          catch (JSONException e) {
	             e.printStackTrace();
	          }
	      }
	   }
	}


	private void loadCoinsList() {
		mItemList = new ArrayList<Coin>();
		
		ArrayList<String> skuList = new ArrayList<String> ();
		
		skuList.add("100");
		skuList.add("200");
		skuList.add("300");
		
		Bundle querySkus = new Bundle();
		querySkus.putStringArrayList("ITEM_ID_LIST", skuList);

		try {
			Bundle skuDetails = Global.mService.getSkuDetails(3, getPackageName(), "inapp", querySkus);

			int response = skuDetails.getInt("RESPONSE_CODE");
			if (response == 0) {
				ArrayList<String> responseList
				= skuDetails.getStringArrayList("DETAILS_LIST");

				for (String thisResponse : responseList) {
					JSONObject object;
					try {
						object = new JSONObject(thisResponse);

						String sku = object.getString("productId");
						String price = object.getString("price");
						
						Coin coin = new Coin();
						
						coin.coins = Integer.parseInt(sku);
						coin.price = price;
						
						mItemList.add(coin);
						
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						adapter = new CoinsListAdapter(CoinActivity.this, mItemList);
						mListView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
					}
				});
			}

		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void tempLoadCoinsList() {
		mItemList = new ArrayList<Coin>();

		for(int i = 0; i < 3; i++) {
			Coin item = new Coin();

			item.coins = (i + 1) * 100;
			item.price = "$" + (i + 0.99);
			mItemList.add(item);
		}

		adapter = new CoinsListAdapter(this, mItemList);
		mListView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
}
