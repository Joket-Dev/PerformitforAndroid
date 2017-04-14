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
import com.llc.performit.draw.drawBrushPoint;
import com.llc.performit.draw.drawColorPoint;
import com.llc.performit.draw.drawObjectArray;
import com.llc.performit.model.CompleteGame;
import com.llc.performit.model.GameItem;
import com.llc.performit.model.MyTurn;
import com.llc.performit.model.PacketItem;
import com.llc.performit.model.TheirTurn;
import com.llc.performit.model.WordItem;
import com.llc.performit.view.CustomDialog;
import com.llc.performit.view.PullToRefreshListView;
import com.llc.performit.view.PullToRefreshListView.OnRefreshListener;
import com.llc.performit.view.TurnCellListAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HomeActivity extends Activity {
	
	private ImageView		btnTapjoy;
	private TextView		btnCreateGame;
	private ImageView		btnSettings;
	private ImageView		btnLeaderboard;
	private ImageView		btnAchievments;
	private LinearLayout	coinsLayout;
	private TextView		tvCoinsLbl;
	private TextView		tvCoins;
	private PullToRefreshListView		listView;
	
	private ArrayList<Object>		mTurnList;
	private TurnCellListAdapter		adapter;
	
	public static HomeActivity	homeActivity;
	
	private int				selectedGame;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		
		btnTapjoy = (ImageView) findViewById(R.id.tapjoy_imageView);
		btnCreateGame = (TextView) findViewById(R.id.create_game_imageView);
		btnSettings = (ImageView) findViewById(R.id.settings_imageView);
		btnLeaderboard = (ImageView) findViewById(R.id.leaderboard_imageView);
		btnAchievments = (ImageView) findViewById(R.id.achievements_imageView);
		coinsLayout = (LinearLayout) findViewById(R.id.coins_layout);
		tvCoinsLbl = (TextView) findViewById(R.id.coins_label_textView);
		tvCoins = (TextView) findViewById(R.id.coins_textView);
		listView = (com.llc.performit.view.PullToRefreshListView) findViewById(R.id.listView1);
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		btnCreateGame.setTypeface(btnFont);
		tvCoinsLbl.setTypeface(btnFont);
		tvCoins.setTypeface(btnFont);
		
		homeActivity = this;
		
		initListener();
		
		listView.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Do work to refresh the list here.
                getGames();
            }
        });
		
		Global.mServiceConn = new ServiceConnection() {
			@Override
			public void onServiceConnected(ComponentName arg0, IBinder service) {
				// TODO Auto-generated method stub
				Global.mService = IInAppBillingService.Stub.asInterface(service);
			}

			@Override
			public void onServiceDisconnected(ComponentName arg0) {
				// TODO Auto-generated method stub
				Global.mService = null;
			}
		};
		
		Intent serviceIntent = new Intent("com.android.vending.billing.InAppBillingService.BIND");
		serviceIntent.setPackage("com.android.vending");
		bindService(serviceIntent, Global.mServiceConn, Context.BIND_AUTO_CREATE);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		getGames();
	}
	
	@Override
	public void onDestroy() {
	    super.onDestroy();
	    if (Global.mService != null) {
	        unbindService(Global.mServiceConn);
	    }
	}

	private void initListener() {
		coinsLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeActivity.this, ConfirmBuyCoinActivity.class);
				startActivity(intent);
			}
		});
		
		btnSettings.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeActivity.this, SettingsActivity.class);
				startActivity(intent);
			}
		});
		
		btnCreateGame.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeActivity.this, NewGameActivity.class);
				startActivity(intent);
			}
		});
		
		btnAchievments.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeActivity.this, AchievementsActivity.class);
				startActivity(intent);
			}
		});
		
		btnLeaderboard.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeActivity.this, LeaderboardActivity.class);
				startActivity(intent);
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				// TODO Auto-generated method stub
				int index = arg2 - 1;
				Object item = mTurnList.get(index);
				if(item.getClass().getName().equalsIgnoreCase(MyTurn.class.getName())) {
					MyTurn turn = (MyTurn)mTurnList.get(index);
					if(turn.name == "")
						return;
					
					gameSelectedTUI(index);
				}
			}
		});

		listView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, final int arg2, long arg3) {
				// TODO Auto-generated method stub
				Dialog dialog = new Dialog(HomeActivity.this);
				CustomDialog.Builder customBuilder = new CustomDialog.Builder(HomeActivity.this);
				customBuilder.setTitle("");
				customBuilder.setMessage("Are you sure you want to delete this game?");
				customBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						
						
					}
				});
				customBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						selectedGame = arg2;
						deleteGame(selectedGame);
						
					}
				});
				
				dialog = customBuilder.create();
				dialog.setCancelable(false);
				
				dialog.show();
				
				return true;
			}
		});
	}

	private void tempLoadTurnList() {
		mTurnList = new ArrayList<Object>();
		
		for(int i = 0; i < 3; i++) {
			MyTurn item = new MyTurn();
			
			item.name = "My Turn " + i;
			item.winCount = i + 3;
			if(i == 0) {
				item.isCatgoryFirst = true;
			}
			
			item.type = "i";
			
			mTurnList.add(item);
		}
		
		for(int i = 0; i < 1; i++) {
			TheirTurn item = new TheirTurn();
			
			if(i == 0)
				item.isCatgoryFirst = true;
			
			mTurnList.add(item);
		}
		
		for(int i = 0; i < 3; i++) {
			CompleteGame item = new CompleteGame();
			
			item.name = "Game " + i;
			item.winCount = i + 3;
			if(i == 0)
				item.isCatgoryFirst = true;
			
			mTurnList.add(item);
		}
		
		adapter = new TurnCellListAdapter(this, mTurnList);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	private void deleteGame(int index) {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "delete_game";
		
		MyTurn item = (MyTurn) mTurnList.get(index);
		
		String params = "app_id=" + Global.mUserData.token + "&game_round_id=" + item.gameRoundId + "&version=" + Utils.getVersion();
		
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
	            Log.d("deleteGame HTTP", "onSuccess: " + response);
	            
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
						mTurnList.remove(selectedGame);
						adapter.notifyDataSetChanged();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(HomeActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(HomeActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(HomeActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						case 21:
							showConfirmDialog(HomeActivity.this, strResp, "OK", Constants.gameIsDeletedAlert);
							break;
						default:
							Utils.showOKDialog(HomeActivity.this, strResp, "OK");
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(HomeActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("deleteGame HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(HomeActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void getGames() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "get_games";
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
	            Log.d("getGames HTTP", "onSuccess: " + response);
	            
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
						mTurnList = new ArrayList<Object>();
						
						JSONObject resp = jResponse.getJSONObject(Constants.KEY_RESPONSE);
						
						if(!resp.isNull(Constants.KEY_MY_TURN)) {
							JSONArray jMyTurns = resp.getJSONArray(Constants.KEY_MY_TURN);
							
							if(jMyTurns.length() == 0) {
								MyTurn item = new MyTurn();
								item.name = "";
								item.isCatgoryFirst = true;
								mTurnList.add(item);
							}
							else {
								for(int i = 0; i < jMyTurns.length(); i++) {
									JSONObject obj = jMyTurns.getJSONObject(i);
									
									MyTurn item = new MyTurn();
									
									if(i == 0)
										item.isCatgoryFirst = true;
									
									if(!obj.isNull(Constants.KEY_COINS))
										item.coins = obj.getInt(Constants.KEY_COINS);
									if(!obj.isNull(Constants.KEY_GAME_ID))
										item.gameId = obj.getInt(Constants.KEY_GAME_ID);
									if(!obj.isNull(Constants.KEY_GAME_ROUND_ID))
										item.gameRoundId = obj.getInt(Constants.KEY_GAME_ROUND_ID);
									if(!obj.isNull(Constants.KEY_MY_ACTION))
										item.action = obj.getString(Constants.KEY_MY_ACTION);
									if(!obj.isNull(Constants.KEY_NAME))
										item.name = obj.getString(Constants.KEY_NAME);
									if(!obj.isNull(Constants.KEY_PICTURE))
										item.picture = obj.getString(Constants.KEY_PICTURE);
									if(!obj.isNull(Constants.KEY_TIME))
										item.time = obj.getInt(Constants.KEY_TIME);
									if(!obj.isNull(Constants.KEY_TYPE))
										item.type = obj.getString(Constants.KEY_TYPE);
									if(!obj.isNull(Constants.KEY_WIN_COUNT))
										item.winCount = obj.getInt(Constants.KEY_WIN_COUNT);
									if(!obj.isNull(Constants.KEY_WORD))
										item.word = obj.getString(Constants.KEY_WORD);
									if(!obj.isNull(Constants.KEY_WORD_ID))
										item.wordId = obj.getInt(Constants.KEY_WORD_ID);
									if(!obj.isNull(Constants.KEY_PLAYER))
										item.player = obj.getInt(Constants.KEY_PLAYER);
									
									mTurnList.add(item);
								}
							}
						}
						else {
							MyTurn item = new MyTurn();
							item.name = "";
							item.isCatgoryFirst = true;
							mTurnList.add(item);
						}
						
						if(!resp.isNull(Constants.KEY_THEIR_TURN)) {
							JSONArray jTheirTurns = resp.getJSONArray(Constants.KEY_THEIR_TURN);
							
							if(jTheirTurns.length() == 0) {
								TheirTurn item = new TheirTurn();
								item.name = "";
								item.isCatgoryFirst = true;
								mTurnList.add(item);
							}
							else {
								for(int i = 0; i < jTheirTurns.length(); i++) {
									JSONObject obj = jTheirTurns.getJSONObject(i);
									
									TheirTurn item = new TheirTurn();
									
									if(i == 0)
										item.isCatgoryFirst = true;
									
									if(!obj.isNull(Constants.KEY_NAME))
										item.name = obj.getString(Constants.KEY_NAME);
									if(!obj.isNull(Constants.KEY_PICTURE))
										item.picture = obj.getString(Constants.KEY_PICTURE);
									if(!obj.isNull(Constants.KEY_PLAYER))
										item.player = obj.getInt(Constants.KEY_PLAYER);
									if(!obj.isNull(Constants.KEY_WIN_COUNT))
										item.winCount = obj.getInt(Constants.KEY_WIN_COUNT);
									
									mTurnList.add(item);
								}
							}
						}
						else {
							TheirTurn item = new TheirTurn();
							item.name = "";
							item.isCatgoryFirst = true;
							mTurnList.add(item);
						}
						
						if(!resp.isNull(Constants.KEY_COMPLETED)) {
							JSONArray jComplete = resp.getJSONArray(Constants.KEY_COMPLETED);
							
							if(jComplete.length() == 0) {
								CompleteGame item = new CompleteGame();
								item.name = "";
								item.isCatgoryFirst = true;
								mTurnList.add(item);
							}
							else {
								for(int i = 0; i < jComplete.length(); i++) {
									JSONObject obj = jComplete.getJSONObject(i);
									
									CompleteGame item = new CompleteGame();

									if(i == 0)
										item.isCatgoryFirst = true;
									
									item.picture = obj.getString(Constants.KEY_PICTURE);
									item.gameRoundId = obj.getInt(Constants.KEY_GAME_ROUND_ID);
									item.winCount = obj.getInt(Constants.KEY_WIN_COUNT);
									item.name = obj.getString(Constants.KEY_NAME);
									
									mTurnList.add(item);
								}
							}
						}
						else {
							CompleteGame item = new CompleteGame();
							item.name = "";
							item.isCatgoryFirst = true;
							mTurnList.add(item);
						}
						
						adapter = new TurnCellListAdapter(HomeActivity.this, mTurnList);
						listView.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						
						listView.onRefreshComplete();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(HomeActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(HomeActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(HomeActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						default:
							Utils.showOKDialog(HomeActivity.this, strResp, "OK");
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(HomeActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("getGames HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(HomeActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void gameSelectedTUI(int row) {
		selectedGame = row;
		getPurchasablePackets();
	}
	
	private void proceedToGame() {
		MyTurn item = (MyTurn) mTurnList.get(selectedGame);
		
		if(item.action.equalsIgnoreCase(Constants.ACTION_SEND_DATA)) {
			GameItem game = new GameItem();
			
			game.gameId = item.gameId;
			game.gameRoundId = item.gameRoundId;
			
			WordItem word = new WordItem();
			word.word = item.word;
			word.id = item.wordId;
			word.coins = item.coins;
			word.type = item.type;
			word.time = item.time;
			word.gameId = item.gameId;
			
			game.word = word;
			
			Global.mCurGame = game;
			
			if(item.type.equalsIgnoreCase(Constants.TYPE_IMAGE)) {
				Intent intent = new Intent(HomeActivity.this, DrawActivity.class);
				startActivity(intent);
			}
			else if(item.type.equalsIgnoreCase(Constants.TYPE_AUDIO)) {
				Global.mAudioWord = word;
				Intent intent = new Intent(HomeActivity.this, RecordAudioActivity.class);
				startActivity(intent);
			}
			else if(item.type.equalsIgnoreCase(Constants.TYPE_VIDEO)) {
				Global.mVideoWord = word;
				Intent intent = new Intent(HomeActivity.this, RecordVideoActivity.class);
				startActivity(intent);
			}
		}
		else if(item.action.equalsIgnoreCase(Constants.ACTION_FINISH_GAME)) {
			getGameDetails(item);
		}
		else if(item.action.equalsIgnoreCase(Constants.ACTION_CONTINUE_GAME)) {
			getGameWords(item.player, false, Global.mUserData.token, Utils.getVersion());
		}
	}
	
	private void getGameDetails(MyTurn turn) {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "get_game_details";
		String params = "game_round_id=" + turn.gameRoundId + "&app_id=" + Global.mUserData.token + "&version=" + Utils.getVersion();
		
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
	            Log.d("getGameDetails HTTP", "onSuccess: " + response);
	            
	            if(progress.isShowing())
					progress.dismiss();
	            
	            //
//	            response = response.replace("\"{", "{");
//	            response = response.replace("}}\"", "}}");
	            //

	            JSONObject jResponse = null;
	            boolean success = false;
	            int code = 0;
	            
	            try {
					
	            	jResponse = new JSONObject(response);
					
					success = jResponse.getBoolean(Constants.KEY_SUCCESS);
					code = jResponse.getInt(Constants.KEY_CODE);
					
					if(success) {
						JSONObject resp = jResponse.getJSONObject(Constants.KEY_RESPONSE);
						
						GameItem game = new GameItem();
						game.gameId = resp.getInt(Constants.KEY_GAME_ID);
						game.gameRoundId = resp.getInt(Constants.KEY_GAME_ROUND_ID);
						game.player = resp.getInt(Constants.KEY_PLAYER);
						
						WordItem word = new WordItem();
						game.word = word;
						
						word.id = resp.getInt(Constants.KEY_WORD_ID);
						word.word = resp.getString(Constants.KEY_WORD);
						word.type = resp.getString(Constants.KEY_TYPE);
						word.coins = resp.getInt(Constants.KEY_COINS);
						word.time = resp.getInt(Constants.KEY_TIME);
						
						if(word.type.equalsIgnoreCase(Constants.TYPE_IMAGE)) {
							String str;
							str = resp.getString("data");
							
							//
							int dataStart = response.indexOf("data");
							int bgStart = response.indexOf("background");
							String lineStr = response.substring(dataStart + 7, bgStart - 2) + "}";
							//
							
							JSONObject jData=new JSONObject(str);
							
							//
							ArrayList<drawBrushPoint> line;
							ArrayList<drawObjectArray> linesList;
							
							JSONObject jBGColor = jData.getJSONObject("background");
							JSONArray jLines = jData.getJSONArray("lines");
							
							linesList = new ArrayList<drawObjectArray>();
							
							for(int i = 0; i < jLines.length(); i++) {
								line = new ArrayList<drawBrushPoint>();
								
								JSONArray jSubLine = jLines.getJSONArray(i);
								
								for(int j = 0; j < jSubLine.length(); j++) {
									drawBrushPoint point = new drawBrushPoint();
									
									JSONObject jItem = jSubLine.getJSONObject(j);
									
									JSONObject jColor = jItem.getJSONObject("brush-color");
									
									drawColorPoint color = new drawColorPoint();
									color.R = (float) jColor.getDouble("red");
									color.B = (float) jColor.getDouble("blue");
									color.G = (float) jColor.getDouble("green");
									
									
									point.RGB = color;
									
									point.brushSize = jItem.getString("brush-size");
									point.brushName = jItem.getString("brush-name");
									
									String erased = jItem.getString("erased");
									if(erased.equalsIgnoreCase("true"))
										point.erased = "1";
									else
										point.erased = "0";
									
									String pointStr = jItem.getString("point");
									pointStr = pointStr.substring(pointStr.indexOf("{") + 1, pointStr.indexOf("}"));
									String[] NSPoint = pointStr.split(",");
									
									point.pointX = NSPoint[0].trim();
									point.pointY = NSPoint[1].trim();
									
//									point.pointX = Utils.convertPxToDp(HomeActivity.this, Integer.parseInt(point.pointX)) + "";
//									point.pointY = Utils.convertPxToDp(HomeActivity.this, Integer.parseInt(point.pointY)) + "";

									line.add(point);
								}
								
								drawObjectArray pointArray = new drawObjectArray();
								pointArray.drawObjArray = line;
								linesList.add(pointArray);
							}
							
							word.mLines = linesList;
							
							word.mBGColor = new drawColorPoint();
							word.mBGColor.R = jBGColor.getInt("red");
							word.mBGColor.G = jBGColor.getInt("green");
							word.mBGColor.B = jBGColor.getInt("blue");
							
							Global.mCurGame = game;
							
							Intent intent = new Intent(HomeActivity.this, GamePlayActivity.class);
							intent.putExtra(Constants.KEY_TYPE, 0);
							startActivity(intent);
							//
						}
						else if(word.type.equalsIgnoreCase(Constants.TYPE_AUDIO)) {
							String data = resp.getString(Constants.KEY_DATA);
							data = data.replace(" ", "+");
							String path = Utils.base64Decoding(data, "3gp");
							
							word.mediaPath = path;
							
							Global.mCurGame = game;
							
							Intent intent = new Intent(HomeActivity.this, GamePlayActivity.class);
							intent.putExtra(Constants.KEY_TYPE, 1);
							startActivity(intent);
						}
						else if(word.type.equalsIgnoreCase(Constants.TYPE_VIDEO)) {
							String data = resp.getString(Constants.KEY_DATA);
							data = data.replace(" ", "+");
							String path = Utils.base64Decoding(data, "3gp");
							
							word.mediaPath = path;
							
							Global.mCurGame = game;
							
							Intent intent = new Intent(HomeActivity.this, GamePlayActivity.class);
							intent.putExtra(Constants.KEY_TYPE, 2);
							startActivity(intent);
						}
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(HomeActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(HomeActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(HomeActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						default:
							Utils.showOKDialog(HomeActivity.this, strResp, "OK");
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(HomeActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("getGameDetails HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(HomeActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void getPurchasablePackets() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "get_purchasable_packets";
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
	            Log.d("getPurchasablePackets HTTP", "onSuccess: " + response);
	            
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
						ArrayList<PacketItem> itemList = new ArrayList<PacketItem>();
						
						for(int i = 0; i < list.length(); i++) {
							JSONObject jObj = list.getJSONObject(i);
							
							PacketItem item = new PacketItem();
							item.packetId = jObj.getInt(Constants.KEY_PACKET_ID);
							item.name = jObj.getString(Constants.KEY_NAME);
							item.coins = jObj.getInt(Constants.KEY_COINS);
							item.purchased = jObj.getInt(Constants.KEY_PURCHASED);
							
							itemList.add(item);
						}
						
						Global.mInAppPacketsArray = itemList;
						
						for(int i = 0; i < Global.mInAppPacketsArray.size(); i++) {
							if(Global.mInAppPacketsArray.get(i).packetId == Constants.kColorsAllPackage &&
									Global.mInAppPacketsArray.get(i).purchased == 1) {
								for(int j = 0; j < Global.mInAppPacketsArray.size(); j++) {
									if(Global.mInAppPacketsArray.get(j).packetId == Constants.kColors1Package) {
										Global.mInAppPacketsArray.get(j).purchased = 1;
									}
								}
							}
							
							if(Global.mInAppPacketsArray.get(i).packetId == Constants.kBrushesAllPackage &&
									Global.mInAppPacketsArray.get(i).purchased == 1) {
								for(int j = 0; j < Global.mInAppPacketsArray.size(); j++) {
									if(Global.mInAppPacketsArray.get(j).packetId == Constants.kBrushes1Package) {
										Global.mInAppPacketsArray.get(j).purchased = 1;
									}
								}
							}
						}
						
						proceedToGame();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(HomeActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(HomeActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(HomeActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						default:
							Utils.showOKDialog(HomeActivity.this, strResp, "OK");
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(HomeActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("getPurchasablePackets HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(HomeActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
							Utils.showOKDialog(HomeActivity.this, "Word is null", "OK");
							return;
						}
						
						int bubbleCnt = resp.getInt(Constants.KEY_BUBBLES);
						
						Global.mUserData.bubbles = bubbleCnt;
						Global.mUserData.saveUserData(HomeActivity.this);
						
						JSONObject audioObj = resp.getJSONObject(Constants.KEY_AUDIO);
						JSONObject imageObj = resp.getJSONObject(Constants.KEY_IMAGE);
						JSONObject videoObj = resp.getJSONObject(Constants.KEY_VIDEO);
						
						WordItem audioWord = Utils.parseWord(audioObj);
						WordItem imageWord = Utils.parseWord(imageObj);
						WordItem videoWord = Utils.parseWord(videoObj);
						
						Global.mAudioWord = audioWord;
						Global.mImageWord = imageWord;
						Global.mVideoWord = videoWord;
						
						Intent intent = new Intent(HomeActivity.this, GameTypeActivity.class);
						intent.putExtra(Constants.KEY_OPPONENT_ID, oppenetId);
						
						MyTurn item = (MyTurn) mTurnList.get(selectedGame);
						intent.putExtra(Constants.KEY_GAME_ID, item.gameId);
						intent.putExtra(Constants.KEY_CONTINUE_GAME, true);
						
						startActivity(intent);
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(HomeActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(HomeActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(HomeActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						default:
							Utils.showOKDialog(HomeActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(HomeActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("getGameWords HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(HomeActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
						Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
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
