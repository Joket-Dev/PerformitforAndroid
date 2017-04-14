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
import com.llc.performit.model.GameItem;
import com.llc.performit.model.PacketItem;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameTypeActivity extends Activity {

	private TextView	tvImage;
	private TextView	tvImageCoin;
	private TextView	tvImageWord;
	private TextView	tvAudio;
	private TextView	tvAudioCoin;
	private TextView	tvAudioWord;
	private TextView	tvVideo;
	private TextView	tvVideoCoin;
	private TextView	tvVideoWord;
	private TextView	tvCoinsLabel;
	private TextView	tvCoins;
	private TextView	tvBubbles;
	private ImageView	btnChangeWord;
	private LinearLayout	imageLayout;
	private LinearLayout	audioLayout;
	private LinearLayout	videoLayout;
	
	public static GameTypeActivity	activity;
	
	private int			opponentID;
	private int			selectedGameID;
	private boolean		continueGame;
	
	private int			availableBubbleCnt;
	private int			availableBubbleCoins;
	private int			availableBubbleId;
	
	private	GameItem 	game;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_type);
		
		tvImage = (TextView) findViewById(R.id.image_textView);
		tvImageCoin = (TextView) findViewById(R.id.image_coin_textView);
		tvImageWord = (TextView) findViewById(R.id.image_word_textView);
		tvAudio = (TextView) findViewById(R.id.audio_textView);
		tvAudioCoin = (TextView) findViewById(R.id.audio_coin_textView);
		tvAudioWord = (TextView) findViewById(R.id.audio_word_textView);
		tvVideo = (TextView) findViewById(R.id.video_textView);
		tvVideoCoin = (TextView) findViewById(R.id.video_coin_textView);
		tvVideoWord = (TextView) findViewById(R.id.video_word_textView);
		tvCoinsLabel = (TextView) findViewById(R.id.coins_label_textView);
		tvCoins = (TextView) findViewById(R.id.coins_textView);
		tvBubbles = (TextView) findViewById(R.id.bubble_textView);
		btnChangeWord = (ImageView) findViewById(R.id.change_words_imageView);
		imageLayout = (LinearLayout) findViewById(R.id.image_layout);
		audioLayout = (LinearLayout) findViewById(R.id.audio_layout);
		videoLayout = (LinearLayout) findViewById(R.id.video_layout);
		
		activity = this;
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvImage.setTypeface(btnFont);
		tvImageCoin.setTypeface(btnFont);
		tvImageWord.setTypeface(btnFont);
		tvAudio.setTypeface(btnFont);
		tvAudioCoin.setTypeface(btnFont);
		tvAudioWord.setTypeface(btnFont);
		tvVideo.setTypeface(btnFont);
		tvVideoCoin.setTypeface(btnFont);
		tvVideoWord.setTypeface(btnFont);
		tvCoinsLabel.setTypeface(btnFont);
		tvCoins.setTypeface(btnFont);
		tvBubbles.setTypeface(btnFont);
		
		Intent intent = getIntent();
		
		opponentID = intent.getIntExtra(Constants.KEY_OPPONENT_ID, 0);
		selectedGameID = intent.getIntExtra(Constants.KEY_GAME_ID, -1);
		continueGame = intent.getBooleanExtra(Constants.KEY_CONTINUE_GAME, false);
		
		initListener();
		
		initWordSections();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		tvCoins.setText(Global.mUserData.coins + "");
		tvBubbles.setText(Global.mUserData.bubbles + "");
		
		getBubbles();
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		getAvailableBubbles();
	}
	
	private void initListener() {
		btnChangeWord.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(Global.mUserData.bubbles > 0) {
//					getGameWords(final int oppenetId, boolean bubbles, String token, String version)
					getGameWords(opponentID, true, Global.mUserData.token, Utils.getVersion());
				}
				else {
					Intent intent = new Intent(GameTypeActivity.this, BubblesPopupActivity.class);
					
					intent.putExtra(Constants.KEY_BUBBLES, availableBubbleCnt);
					intent.putExtra(Constants.KEY_COINS, availableBubbleCoins);
					intent.putExtra(Constants.KEY_ID, availableBubbleId);
					
					startActivity(intent);
				}
			}
		});
		
		audioLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!continueGame)
					createGame(Global.mAudioWord.id, opponentID, Global.mUserData.token, Utils.getVersion(), 1);
				else
					continueGame(selectedGameID, Global.mAudioWord.id, 1);
			}
		});
		
		videoLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!continueGame)
					createGame(Global.mVideoWord.id, opponentID, Global.mUserData.token, Utils.getVersion(), 2);
				else
					continueGame(selectedGameID, Global.mVideoWord.id, 2);
			}
		});
		
		imageLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!continueGame)
					createGame(Global.mImageWord.id, opponentID, Global.mUserData.token, Utils.getVersion(), 0);
				else
					continueGame(selectedGameID, Global.mImageWord.id, 0);
			}
		});
	}
	
	private void initWordSections() {
		if(Global.mAudioWord != null) {
			tvAudioCoin.setText(Global.mAudioWord.coins + "");
			tvAudioWord.setText(Global.mAudioWord.word);
		}
		
		if(Global.mImageWord != null) {
			tvImageCoin.setText(Global.mImageWord.coins + "");
			tvImageWord.setText(Global.mImageWord.word);
		}

		if(Global.mVideoWord != null) {
			tvVideoCoin.setText(Global.mVideoWord.coins + "");
			tvVideoWord.setText(Global.mVideoWord.word);
		}
	}
	
	private void getBubbles() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "get_bubbles";
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
	            Log.d("getBubbles HTTP", "onSuccess: " + response);
	            
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
						int bubbleCnt = jResponse.getInt(Constants.KEY_RESPONSE);
						
						Global.mUserData.bubbles = bubbleCnt;
						Global.mUserData.saveUserData(GameTypeActivity.this);
						
						if(bubbleCnt > 0)
							tvBubbles.setText(bubbleCnt + " BUBBLES");
						else
							tvBubbles.setText("BUY BUBBLES");
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						Utils.showOKDialog(GameTypeActivity.this, strResp, "OK");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(GameTypeActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("getBubbles HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(GameTypeActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void getAvailableBubbles() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "bubble_list";
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
	            Log.d("getAvailableBubbles HTTP", "onSuccess: " + response);
	            
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
						JSONArray respArray = jResponse.getJSONArray(Constants.KEY_RESPONSE);
						if(respArray == null) {
							Utils.showOKDialog(GameTypeActivity.this, "Word is null", "OK");
							return;
						}
						
						if(respArray.length() == 0) {
							Utils.showOKDialog(GameTypeActivity.this, "There is no available bubbles", "OK");
							return;
						}
						
						JSONObject resp = respArray.getJSONObject(0);
						
						int bubbleCnt = resp.getInt(Constants.KEY_BUBBLES);
						int coins = resp.getInt(Constants.KEY_COINS);
						int bubbleId = resp.getInt(Constants.KEY_ID);
						
						availableBubbleCnt = bubbleCnt;
						availableBubbleCoins = coins;
						availableBubbleId = bubbleId;
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						Utils.showOKDialog(GameTypeActivity.this, strResp, "OK");
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(GameTypeActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("getAvailableBubbles HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(GameTypeActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
							Utils.showOKDialog(GameTypeActivity.this, "Word is null", "OK");
							return;
						}
						
						int bubbleCnt = resp.getInt(Constants.KEY_BUBBLES);
						
						Global.mUserData.bubbles = bubbleCnt;
						Global.mUserData.saveUserData(GameTypeActivity.this);
						
						if(bubbleCnt > 0)
							tvBubbles.setText(bubbleCnt + " BUBBLES");
						else
							tvBubbles.setText("BUY BUBBLES");
						
						JSONObject audioObj = resp.getJSONObject(Constants.KEY_AUDIO);
						JSONObject imageObj = resp.getJSONObject(Constants.KEY_IMAGE);
						JSONObject videoObj = resp.getJSONObject(Constants.KEY_VIDEO);
						
						WordItem audioWord = Utils.parseWord(audioObj);
						WordItem imageWord = Utils.parseWord(imageObj);
						WordItem videoWord = Utils.parseWord(videoObj);
						
						Global.mAudioWord = audioWord;
						Global.mImageWord = imageWord;
						Global.mVideoWord = videoWord;
						
						initWordSections();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(GameTypeActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(GameTypeActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(GameTypeActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						case 34:
							//User doesn't exits
							showConfirmDialog(GameTypeActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						default:
							Utils.showOKDialog(GameTypeActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(GameTypeActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("getGameWords HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(GameTypeActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void createGame(int wordId, int oppenId, String token, String version, final int gameType) {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "create_game";
		String params = "player_2=" + oppenId + "&word_id=" + wordId + "&app_id=" + Global.mUserData.token + "&version=" + version;
		
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
	            Log.d("createGame HTTP", "onSuccess: " + response);
	            
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
							Utils.showOKDialog(GameTypeActivity.this, "Word is null", "OK");
							return;
						}
						
						game = new GameItem();
						
						game.gameId = resp.getInt(Constants.KEY_GAME_ID);
						game.gameRoundId = resp.getInt(Constants.KEY_GAME_ROUND_ID);
						
						if(gameType == 0) {		// Image
							getPurchasablePackets();
						}
						else if(gameType == 1) {	// Audio
							game.word = Global.mAudioWord;
							Global.mCurGame = game;
							
							Intent intent = new Intent(GameTypeActivity.this, RecordAudioActivity.class);
							startActivity(intent);
						}
						else {		// Video
							game.word = Global.mVideoWord;
							Global.mCurGame = game;
							
							Intent intent = new Intent(GameTypeActivity.this, RecordVideoActivity.class);
							startActivity(intent);
						}
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(GameTypeActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(GameTypeActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(GameTypeActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						case 24:
							//Invalid game type.
							showConfirmDialog(GameTypeActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 31:
							//User doesn't exists
							showConfirmDialog(GameTypeActivity.this, strResp, "OK", Constants.opponentDoesntExistsAlert);
							break;
						case 35:
							//Word doesn't exists
							showConfirmDialog(GameTypeActivity.this, strResp, "OK", Constants.wordDoesntExistsAlert);
							break;
						case 39:
							//There are no active players, try later
							showConfirmDialog(GameTypeActivity.this, strResp, "OK", Constants.noActivePlayersAlert);
							break;
						case 49:
							//Could not create game, try later.
							showConfirmDialog(GameTypeActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						default:
							Utils.showOKDialog(GameTypeActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(GameTypeActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("createGame HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(GameTypeActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}

	private void continueGame(int gameId, int wordId, final int gameType) {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "continue_game";
		String params = "game_id=" + gameId + "&word_id=" + wordId + "&app_id=" + Global.mUserData.token + "&version=" + Utils.getVersion();
		
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
	            Log.d("continueGame HTTP", "onSuccess: " + response);
	            
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
							Utils.showOKDialog(GameTypeActivity.this, "Word is null", "OK");
							return;
						}
						
						game = new GameItem();
						
						game.gameId = resp.getInt(Constants.KEY_GAME_ID);
						game.gameRoundId = resp.getInt(Constants.KEY_GAME_ROUND_ID);
						
						if(gameType == 0) {		// Image
							getPurchasablePackets();
						}
						else if(gameType == 1) {	// Audio
							game.word = Global.mAudioWord;
							Global.mCurGame = game;
							
							Intent intent = new Intent(GameTypeActivity.this, RecordAudioActivity.class);
							startActivity(intent);
						}
						else {		// Video
							game.word = Global.mVideoWord;
							Global.mCurGame = game;
							
							Intent intent = new Intent(GameTypeActivity.this, RecordVideoActivity.class);
							startActivity(intent);
						}
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(GameTypeActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(GameTypeActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(GameTypeActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						case 24:
							//Invalid game type.
							showConfirmDialog(GameTypeActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 31:
							//User doesn't exists
							showConfirmDialog(GameTypeActivity.this, strResp, "OK", Constants.opponentDoesntExistsAlert);
							break;
						case 35:
							//Word doesn't exists
							showConfirmDialog(GameTypeActivity.this, strResp, "OK", Constants.wordDoesntExistsAlert);
							break;
						case 21:
							//game deleted
							showConfirmDialog(GameTypeActivity.this, strResp, "OK", Constants.gameIsDeletedAlert);
							break;
						default:
							Utils.showOKDialog(GameTypeActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(GameTypeActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("continueGame HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(GameTypeActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
						
						game.word = Global.mImageWord;
						Global.mCurGame = game;
						
						Intent intent = new Intent(GameTypeActivity.this, DrawActivity.class);
						startActivity(intent);
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							Utils.showOKDialog(GameTypeActivity.this, "Something went wrong. Please try again.", "OK");
							break;
						case 62:
							//new version
							Utils.showOKDialog(GameTypeActivity.this, strResp, "Update");
							break;
						case 2:
							//Invalid app_id
							Utils.showOKDialog(GameTypeActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK");
							break;
						default:
							Utils.showOKDialog(GameTypeActivity.this, strResp, "OK");
							break;
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(GameTypeActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("getPurchasablePackets HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(GameTypeActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
						Intent intent = new Intent(GameTypeActivity.this, LoginActivity.class);
						startActivity(intent);
						finish();
						break;
					case Constants.apiErrorAlert:
						break;
					case Constants.opponentDoesntExistsAlert:
						finish();
						break;
					case Constants.wordDoesntExistsAlert:
						finish();
						break;
					case Constants.noActivePlayersAlert:
						finish();
						break;
					case Constants.gameIsDeletedAlert:
						finish();
						break;
				}
			}
		});
		
		dialog = customBuilder.create();
		dialog.setCancelable(false);
		
		dialog.show();
	}
}
