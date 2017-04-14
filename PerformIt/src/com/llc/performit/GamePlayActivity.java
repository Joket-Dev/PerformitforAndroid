package com.llc.performit;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.llc.performit.common.Constants;
import com.llc.performit.common.Global;
import com.llc.performit.common.Utils;
import com.llc.performit.draw.ComicEditor;
import com.llc.performit.model.GameItem;
import com.llc.performit.model.MyTurn;
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
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

public class GamePlayActivity extends Activity {
	
	private ImageView		btnHome;
	private TextView		tvTime;
	private ImageView		btnAddMore;
	private ImageView		btnPlay;
	private ComicEditor		drawView;
	private LinearLayout	answerScrollLayout;
	private LinearLayout	candidateTopLayout;
	private LinearLayout	candidateBottomLayout;
	private LinearLayout	winLayout;
	private TextView		tvEarn;
	private TextView		tvCoin;
	private LinearLayout	loseLayout;
	private RelativeLayout	mainLayout;
	private VideoView		videoView;
	private LinearLayout	videoLayout;
	private ImageView		btnArrowLeft;
	private ImageView		btnArrowRight;
	private LinearLayout	extraLayout;
	private ImageView		btnEndWin;
	private ImageView		btnNextWin;
	private ImageView		btnEndLose;
	private ImageView		btnNextLose;
	private HorizontalScrollView	extraScrollView;
	
	private String			word = "Word";
	private Typeface 		btnFont;
	private int				gameType = 0;
	private boolean			isPlaying = false;
	
	private ArrayList<TextView>	answerBtnList;
	private ArrayList<TextView>	topCandBtnList;
	private ArrayList<TextView>	bottomCandBtnList;
	
	private CountDownTimer	gameTimer;
	private int				gameTime = 30 * 1000;
	private int				extraTimer = -1;
	private int				timerCnt = 0;
	
	private CountDownTimer	stopTimer;
	
	private MediaPlayer		mPlayer;
	
	private boolean			cancelGame = false;
	
	private String 			letters;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_play);
		
		btnHome = (ImageView) findViewById(R.id.home_imageView);
		tvTime = (TextView) findViewById(R.id.time_textView);
		btnAddMore = (ImageView) findViewById(R.id.add_more_imageView);
		btnPlay = (ImageView) findViewById(R.id.play_imageView);
		drawView = (ComicEditor) findViewById(R.id.editor);
		answerScrollLayout = (LinearLayout) findViewById(R.id.answer_scroll_layout);
		candidateTopLayout = (LinearLayout) findViewById(R.id.candidate_top_linearLayout);
		candidateBottomLayout = (LinearLayout) findViewById(R.id.candidate_bottom_linearLayout);
		winLayout = (LinearLayout) findViewById(R.id.win_layout);
		tvEarn = (TextView) findViewById(R.id.earn_textView);
		tvCoin = (TextView) findViewById(R.id.coin_textView);
		loseLayout = (LinearLayout) findViewById(R.id.lose_layout);
		mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
		videoView = (VideoView) findViewById(R.id.videoView);
		videoLayout = (LinearLayout) findViewById(R.id.videoView_layout);
		btnArrowLeft = (ImageView) findViewById(R.id.left_scroll_arrow);
		btnArrowRight = (ImageView) findViewById(R.id.right_scroll_arrow);
		extraLayout = (LinearLayout) findViewById(R.id.extra_layout);
		btnEndWin = (ImageView) findViewById(R.id.end_win_imageView);
		btnNextWin = (ImageView) findViewById(R.id.next_win_imageView);
		btnEndLose = (ImageView) findViewById(R.id.end_lose_imageView);
		btnNextLose = (ImageView) findViewById(R.id.next_lose_imageView);
		extraScrollView = (HorizontalScrollView) findViewById(R.id.HTScroll);
		
		btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvTime.setTypeface(btnFont);
		tvEarn.setTypeface(btnFont);
		tvCoin.setTypeface(btnFont);
		
		Intent intent = getIntent();
		gameType = intent.getIntExtra(Constants.KEY_TYPE, 0);
		word = Global.mCurGame.word.word;
		gameTime = Global.mCurGame.word.time * 1000;
		tvTime.setText("" + Utils.getTimeStr(gameTime));
		
		if(gameType == Constants.GAME_TYPE_AUDIO) {
			mainLayout.setBackgroundResource(R.drawable.game_play_audio_bg);
			drawView.setVisibility(View.GONE);
			videoLayout.setVisibility(View.GONE);
		}
		else if(gameType == Constants.GAME_TYPE_VIDEO) {
			drawView.setVisibility(View.INVISIBLE);
			videoLayout.setVisibility(View.VISIBLE);
			videoView.setVisibility(View.GONE);
		}
		else if(gameType == Constants.GAME_TYPE_IMAGE) {
			drawView.setVisibility(View.VISIBLE);
			videoLayout.setVisibility(View.GONE);
			drawView.setClickable(false);
		}
		
		initListener();
		
		initAnswerLetterView();
		initGameTimer();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		loadExtraPacket();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		
		Dialog dialog = new Dialog(GamePlayActivity.this);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(GamePlayActivity.this);
		customBuilder.setTitle("");
		customBuilder.setMessage("Do you want to return to home screen? You will lose the game?");
		customBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		customBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				
				cancelGame = true;
				gameTimer.cancel();
				stopPlaying();
				
				finishGame("");
			}
		});
		
		dialog = customBuilder.create();
		dialog.setCancelable(false);
		
		dialog.show();
	}

	private void initListener() {
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Dialog dialog = new Dialog(GamePlayActivity.this);
				CustomDialog.Builder customBuilder = new CustomDialog.Builder(GamePlayActivity.this);
				customBuilder.setTitle("");
				customBuilder.setMessage("Do you want to return to home screen? You will lose the game?");
				customBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				customBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						
						cancelGame = true;
						gameTimer.cancel();
						stopPlaying();
						
						finishGame("");
					}
				});
				
				dialog = customBuilder.create();
				dialog.setCancelable(false);
				
				dialog.show();
			}
		});
		
		btnPlay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				btnPlay.setVisibility(View.GONE);
				
				initCanidateLetterView();
				
				// Image type
				if(gameType == Constants.GAME_TYPE_IMAGE) {
					if(Global.mCurGame.word.mLines != null) {
						drawView.objArray = Global.mCurGame.word.mLines;
						drawView.drawTimerBrush();
					}
				}
				else if(gameType == Constants.GAME_TYPE_AUDIO) {
					if(Global.mCurGame.word.mediaPath != null) {
						mPlayer = new MediaPlayer();
						
						try {
							mPlayer.setDataSource(Global.mCurGame.word.mediaPath);
							mPlayer.prepare();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalStateException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						mPlayer.start();
					}
				}
				else if(gameType == Constants.GAME_TYPE_VIDEO) {
					if(Global.mCurGame.word.mediaPath != null) {
						videoView.setVisibility(View.VISIBLE);
						videoView.setVideoURI(Uri.parse(Global.mCurGame.word.mediaPath));
						videoView.start();
					}
				}
				
				isPlaying = true;
				gameTimer.start();
			}
		});
		
		btnAddMore.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(GamePlayActivity.this, AddMorePlayItemActivity.class);
				startActivity(intent);
			}
		});
		
		btnEndWin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteGame();
			}
		});
		
		btnNextWin.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getGameWords(Global.mCurGame.player, false, Global.mUserData.token, Utils.getVersion());
			}
		});
		
		btnEndLose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				deleteGame();
			}
		});
		
		btnNextLose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getGameWords(Global.mCurGame.player, false, Global.mUserData.token, Utils.getVersion());
			}
		});
		
		btnArrowLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				extraScrollView.setScrollX(extraScrollView.getScrollX() - 50);
			}
		});
		
		btnArrowRight.setOnClickListener(new OnClickListener() {
	
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				extraScrollView.setScrollX(extraScrollView.getScrollX() + 50);
			}
		});
	}
	
	private void addExtraPacket(ImageView packetImg) {
		packetImg.setPadding(10, 0, 10, 0);
		extraLayout.addView(packetImg);
	}
	
	private void loadExtraPacket() {
		extraLayout.removeAllViews();
		
		for(int i = 0; i < Global.mInAppPacketsArray.size(); i++) {
			final PacketItem packet = Global.mInAppPacketsArray.get(i);
			
			if(packet.packetId == Constants.kHintPackage) {
				if(packet.purchased == 1) {
					ImageView packetImg = new ImageView(this);
					packetImg.setImageResource(R.drawable.give_hint);
					packet.btn = packetImg;
					
					packetImg.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(!isPlaying)
								return;
							
							extraTimer = Constants.kHintPackage;
							removeLetter();
						}
					});
					
					addExtraPacket(packetImg);
				}
			}
			
			if(packet.packetId == Constants.kTimer2minPackage) {
				if(packet.purchased == 1) {
					final ImageView packetImg = new ImageView(this);
					packetImg.setImageResource(R.drawable.minutes_2_timer);
					packet.btn = packetImg;
					
					packetImg.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(!isPlaying)
								return;
							
							if(extraTimer == Constants.kTimer5minPackage || extraTimer == Constants.kTimerInfinitePackage
									|| extraTimer == Constants.kTimerStopPackage)
								return;
							
							extraTimer = Constants.kTimer2minPackage;
							gameTime = gameTime - timerCnt + 2 * 60 * 1000;
							gameTimer.cancel();
							gameTimer = null;
							packet.used = true;
							
							initGameTimer();
							gameTimer.start();
							
							packetImg.setImageResource(R.drawable.minutes_2_timer_dis);
						}
					});
					
					addExtraPacket(packetImg);
				}
			}
			
			if(packet.packetId == Constants.kTimer5minPackage) {
				if(packet.purchased == 1) {
					final ImageView packetImg = new ImageView(this);
					packetImg.setImageResource(R.drawable.minutes_5_timer);
					packet.btn = packetImg;
					
					packetImg.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(!isPlaying)
								return;
							
							if(extraTimer == Constants.kTimer2minPackage || extraTimer == Constants.kTimerInfinitePackage
									|| extraTimer == Constants.kTimerStopPackage)
								return;
							
							extraTimer = Constants.kTimer5minPackage;
							gameTime = gameTime - timerCnt + 5 * 60 * 1000;
							gameTimer.cancel();
							gameTimer = null;
							packet.used = true;
							
							initGameTimer();
							gameTimer.start();
							
							packetImg.setImageResource(R.drawable.minutes_5_timer_dis);
						}
					});

					addExtraPacket(packetImg);
				}
			}
			
			if(packet.packetId == Constants.kTimerInfinitePackage) {
				if(packet.purchased == 1) {
					final ImageView packetImg = new ImageView(this);
					packetImg.setImageResource(R.drawable.infinite_timer);
					packet.btn = packetImg;
					
					packetImg.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(!isPlaying)
								return;
							
							if(extraTimer == Constants.kTimerInfinitePackage)
								return;
							
							extraTimer = Constants.kTimerInfinitePackage;
							packet.used = true;
							
							gameTimer.cancel();
							gameTimer = null;
							
							tvTime.setText("∞");
							tvTime.setTextSize(40);
							
							packetImg.setImageResource(R.drawable.infinite_timer_dis);
							
							for(int j = 0; j < Global.mInAppPacketsArray.size(); j++) {
								PacketItem temp = Global.mInAppPacketsArray.get(j);
								
								if(temp.packetId == Constants.kTimer2minPackage) {
									if(temp.btn != null)
										temp.btn.setImageResource(R.drawable.minutes_2_timer_dis);
									
									temp.used = true;
								}
								
								if(temp.packetId == Constants.kTimer5minPackage) {
									if(temp.btn != null)
										temp.btn.setImageResource(R.drawable.minutes_5_timer_dis);
									
									temp.used = true;
								}
								
								if(temp.packetId == Constants.kTimerStopPackage) {
									if(temp.btn != null)
										temp.btn.setImageResource(R.drawable.stop_timer_dis);
									
									temp.used = true;
								}
							}
						}
					});
					
					addExtraPacket(packetImg);
				}
			}
			
			if(packet.packetId == Constants.kTimerStopPackage) {
				if(packet.purchased == 1) {
					final ImageView packetImg = new ImageView(this);
					packetImg.setImageResource(R.drawable.stop_timer);
					packet.btn = packetImg;
					
					packetImg.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if(extraTimer == Constants.kTimerStopPackage)
								return;
							
							extraTimer = Constants.kTimerStopPackage;
							packet.used = true;
							
							gameTimer.cancel();
							gameTimer = null;
							
							packetImg.setImageResource(R.drawable.stop_timer_dis);
							
							gameTime = gameTime - timerCnt;
							
							initStopTimer();
							stopTimer.start();
						}
					});
					
					addExtraPacket(packetImg);
				}
			}
		}
	}
	
	private void initStopTimer() {
		stopTimer = new CountDownTimer(20 * 1000, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				initGameTimer();
				gameTimer.start();
			}
		};
	}
	
	private void stopPlaying() {
		if(gameType == Constants.GAME_TYPE_IMAGE) {
			
		}
		else if(gameType == Constants.GAME_TYPE_AUDIO) {
			if(mPlayer != null) {
				mPlayer.stop();
				mPlayer.release();
				mPlayer = null;
			}
		}
		else if(gameType == Constants.GAME_TYPE_VIDEO) {
			if(Global.mCurGame.word.mediaPath != null) {
				videoView.stopPlayback();
			}
		}
	}
	
	private void initAnswerLetterView() {
		if(word ==  null)
			return;
		
		answerBtnList = new ArrayList<TextView>();
		
		for(int i = 0; i < word.length(); i++) {
			final TextView btnAnswerLetter = new TextView(this);
			
			btnAnswerLetter.setGravity(Gravity.CENTER);
			btnAnswerLetter.setTextColor(getResources().getColor(R.color.black));
			btnAnswerLetter.setBackgroundResource(R.drawable.letter_bg);
			
			answerScrollLayout.addView(btnAnswerLetter);
			
			LayoutParams p = new LayoutParams(35, 35);
			p.rightMargin = 5;
			btnAnswerLetter.setLayoutParams(p);
			
			btnAnswerLetter.setTypeface(btnFont);
			
			answerBtnList.add(btnAnswerLetter);
			
			btnAnswerLetter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String letter = btnAnswerLetter.getText().toString();
					TextView btn = null;
					boolean empty = false;
					
					if(topCandBtnList == null || bottomCandBtnList == null)
						return;
					
					for(int i = 0; i < answerBtnList.size(); i++) {
						btn = answerBtnList.get(i);
						btn.setBackgroundResource(R.drawable.letter_bg);
					}
					
					for(int i = 0; i < 7; i++) {
						btn = topCandBtnList.get(i);
						CharSequence tmpLetter = btn.getText();
						if(tmpLetter == "") {
							empty = true;
							break;
						}
					}
					
					if(empty) {
						btn.setText(letter);
						btnAnswerLetter.setText("");
						
						return;
					}
					
					for(int i = 0; i < 7; i++) {
						btn = bottomCandBtnList.get(i);
						CharSequence tmpLetter = btn.getText();
						if(tmpLetter == "") {
							empty = true;
							break;
						}
					}
					
					if(!empty)
						return;
					
					btn.setText(letter);
					btnAnswerLetter.setText("");
				}
			});
		}
	}
	
	private void initCanidateLetterView() {
		letters = word + Utils.randomAlphanumericStringWithLength(word.length());
		letters = Utils.randominzeString(letters);
		
		topCandBtnList = new ArrayList<TextView>();
		bottomCandBtnList = new ArrayList<TextView>();
		
		for(int i = 0; i < letters.length(); i++) {
			final TextView btnCandiLetter = new TextView(this);
			
			btnCandiLetter.setGravity(Gravity.CENTER);
			btnCandiLetter.setTextColor(getResources().getColor(R.color.black));
			btnCandiLetter.setBackgroundResource(R.drawable.letter_bg);
			
			if(i < 7) {
				candidateTopLayout.addView(btnCandiLetter);
				topCandBtnList.add(btnCandiLetter);
			}
			else {
				candidateBottomLayout.addView(btnCandiLetter);
				bottomCandBtnList.add(btnCandiLetter);
			}
			
			LayoutParams p = new LayoutParams(35, 35);
			p.rightMargin = 5;
			btnCandiLetter.setLayoutParams(p);
			
			char title = letters.charAt(i);
			btnCandiLetter.setText(title + "");
			btnCandiLetter.setTypeface(btnFont);
//			btnCandiLetter.setTextSize(16);
			
			btnCandiLetter.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String letter = btnCandiLetter.getText().toString();
					TextView btn = null;
					boolean empty = false;
					
					for(int i = 0; i < word.length(); i++) {
						btn = answerBtnList.get(i);
						CharSequence tmpLetter = btn.getText();
						if(tmpLetter == "") {
							empty = true;
							break;
						}
					}
					
					if(!empty) {
						return;
					}
					
					btn.setText(letter);
					btnCandiLetter.setText("");
					
					// check validation
					empty = false;
					for(int i = 0; i < word.length(); i++) {
						btn = answerBtnList.get(i);
						CharSequence tmpLetter = btn.getText();
						if(tmpLetter == "") {
							empty = true;
							break;
						}
					}
					
					if(!empty) {
						if(checkValidation()) {
							for(int i = 0; i < answerBtnList.size(); i++) {
								btn = answerBtnList.get(i);
								btn.setBackgroundResource(R.drawable.letter_bg_correct);
							}
							
							endGame(true);
						}
						else {
							for(int i = 0; i < answerBtnList.size(); i++) {
								btn = answerBtnList.get(i);
								btn.setBackgroundResource(R.drawable.letter_bg_wrong);
							}
						}
					}
				}
			});
		}
	}
	
	private void removeLetter() {
		ArrayList<String> tempLetter = new ArrayList<String>();
		for(int i = 0; i < 14; i++) {
			tempLetter.add("0");
		}
		
		ArrayList<TextView> letterList = new ArrayList<TextView>();
		letterList.addAll(0, topCandBtnList);
		letterList.addAll(7, bottomCandBtnList);
		
		for(int i = 0; i < word.length(); i++) {
			String tmp = word.substring(i, i + 1);
			
			for(int j = 0; j < letterList.size(); j++) {
				TextView tv = letterList.get(j);
				String text = (String) tv.getText();
				
				String temp = tempLetter.get(j);
				
				if(temp == "0") {
					if(text.equalsIgnoreCase(tmp)) {
						tempLetter.set(j, "1");
						break;
					}
				}
			}
		}
		
		for(int i = 0; i < tempLetter.size(); i++) {
			String temp = tempLetter.get(i);
			if(temp == "0") {
				TextView tv = letterList.get(i);
				tv.setVisibility(View.INVISIBLE);
			}
		}
	}
	
	private void initGameTimer() {
		gameTimer = new CountDownTimer(gameTime, 1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				// TODO Auto-generated method stub
				tvTime.setText("" + Utils.getTimeStr(millisUntilFinished));
				
				timerCnt = timerCnt + 1000;
			}
			
			@Override
			public void onFinish() {
				// TODO Auto-generated method stub
				tvTime.setText("00:00");
				stopPlaying();
				
				endGame(false);
			}
		};
	}
	
	private boolean checkValidation() {
		boolean ret = true;
		String valStr = "";
		
		for(int i = 0; i < answerBtnList.size(); i++) {
			TextView btn = answerBtnList.get(i);
			
			valStr = valStr + btn.getText();
		}
		
		if(valStr.equalsIgnoreCase(word))
			ret = true;
		else
			ret = false;
		
		return ret;
	}
	
	private void buttonDisable() {
		if(answerBtnList != null) {
			for(int i = 0; i < answerBtnList.size(); i++) {
				TextView	btn = answerBtnList.get(i);
				btn.setClickable(false);
			}
		}
		
		if(bottomCandBtnList != null) {
			for(int i = 0; i < bottomCandBtnList.size(); i++) {
				TextView	btn = bottomCandBtnList.get(i);
				btn.setClickable(false);
			}
		}
		
		if(topCandBtnList != null) {
			for(int i = 0; i < topCandBtnList.size(); i++) {
				TextView	btn = topCandBtnList.get(i);
				btn.setClickable(false);
			}
		}
	}
	
	private void endGame(boolean win) {
		if(win) {
			finishGame(Global.mCurGame.word.word);
		}
		else {
			finishGame("");
			showLosePopup();
		}
	}
	
	private void finishGame(final String word) {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "finish_game";
		String packetId = "";
		
		if(extraTimer != -1)
			packetId = extraTimer + "";
		
		String params = "game_round_id=" + Global.mCurGame.gameRoundId + "&word=" + word + "&time=" + timerCnt / 1000 + "&app_id=" + Global.mUserData.token + "&packet_id=" + packetId + "&version=" + Utils.getVersion();
		
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
	            Log.d("finishGame HTTP", "onSuccess: " + response);
	            
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
						if(cancelGame) {
							finish();
							return;
						}
						
						JSONObject resp = jResponse.getJSONObject(Constants.KEY_RESPONSE);
						int coins = resp.getInt(Constants.KEY_COINS);
						
						Global.mUserData.coins = coins;
						Global.mUserData.saveUserData(GamePlayActivity.this);
						
						if(word != "")
							showWinPopup();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							Utils.showOKDialog(GamePlayActivity.this, "Something went wrong. Please try again.", "OK");
							break;
						case 62:
							//new version
							Utils.showOKDialog(GamePlayActivity.this, strResp, "Update");
							break;
						default:
							Utils.showOKDialog(GamePlayActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(GamePlayActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("finishGame HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(GamePlayActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void deleteGame() {
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "delete_game";
		
		String params = "game_round_id=" + Global.mCurGame.gameRoundId + "&app_id=" + Global.mUserData.token + "&version=" + Utils.getVersion();
		
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
						finish();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(GamePlayActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(GamePlayActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							showConfirmDialog(GamePlayActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						case 21:
							showConfirmDialog(GamePlayActivity.this, strResp, "OK", Constants.gameIsDeletedAlert);
							break;
						default:
							Utils.showOKDialog(GamePlayActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(GamePlayActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("deleteGame HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(GamePlayActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
							Utils.showOKDialog(GamePlayActivity.this, "Word is null", "OK");
							return;
						}
						
						int bubbleCnt = resp.getInt(Constants.KEY_BUBBLES);
						
						Global.mUserData.bubbles = bubbleCnt;
						Global.mUserData.saveUserData(GamePlayActivity.this);
						
						JSONObject audioObj = resp.getJSONObject(Constants.KEY_AUDIO);
						JSONObject imageObj = resp.getJSONObject(Constants.KEY_IMAGE);
						JSONObject videoObj = resp.getJSONObject(Constants.KEY_VIDEO);
						
						WordItem audioWord = Utils.parseWord(audioObj);
						WordItem imageWord = Utils.parseWord(imageObj);
						WordItem videoWord = Utils.parseWord(videoObj);
						
						Global.mAudioWord = audioWord;
						Global.mImageWord = imageWord;
						Global.mVideoWord = videoWord;
						
						Intent intent = new Intent(GamePlayActivity.this, GameTypeActivity.class);
						intent.putExtra(Constants.KEY_OPPONENT_ID, oppenetId);
						intent.putExtra(Constants.KEY_GAME_ID, Global.mCurGame.gameId);
						intent.putExtra(Constants.KEY_CONTINUE_GAME, true);
						
						startActivity(intent);
						
						finish();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							Utils.showOKDialog(GamePlayActivity.this, "Something went wrong. Please try again.", "OK");
							break;
						case 62:
							//new version
							Utils.showOKDialog(GamePlayActivity.this, strResp, "Update");
							break;
						case 2:
							//Invalid app_id
							Utils.showOKDialog(GamePlayActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK");
							break;
						case 31:
							//User doesn't exits
							Utils.showOKDialog(GamePlayActivity.this, strResp, "OK");
							break;

						default:
							Utils.showOKDialog(GamePlayActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(GamePlayActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("getGameWords HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(GamePlayActivity.this, "A connection timeout occured. Plase try later.", "OK");
			}
		});
	}
	
	private void showWinPopup() {
		gameTimer.cancel();
		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							winLayout.setVisibility(View.VISIBLE);
							buttonDisable();
						}
					});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	private void showLosePopup() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					Thread.sleep(1000);
					
					runOnUiThread(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							loseLayout.setVisibility(View.VISIBLE);
							buttonDisable();
						}
					});
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
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
					case Constants.timeoutAlert:
						finish();
						break;
					case Constants.apiErrorAlert:
						finish();
						break;
					case Constants.closeGameAlert:
						gameTimer.cancel();
						stopPlaying();

						finishGame("");
						break;
					case Constants.newVersionAlert:
						break;
					case Constants.invalidAppIDAlert:
						Global.mUserData.validLogin = false;
						Global.mUserData.saveUserData(GamePlayActivity.this);
						
						Intent intent = new Intent(GamePlayActivity.this, LoginActivity.class);
						startActivity(intent);
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
