package com.llc.performit;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;

import com.llc.performit.common.Constants;
import com.llc.performit.common.Global;
import com.llc.performit.common.Utils;
import com.llc.performit.model.GameItem;
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
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class RecordAudioActivity extends Activity {

	private TextView	tvRecordLabel;
	private TextView	tvWord;
	private TextView	tvCoinsLabel;
	private TextView	tvCoins;
	private ImageView	btnSoundEffects;
	private LinearLayout	btnRecord;
	private ImageView	btnPlayPause;
	private ImageView	recordStateImageView;
	private ProgressBar	mProgressBar;
	private ImageView	btnHome;
	private ImageView	btnSave;
	private ImageView	btnShare;
	private ImageView	btnSend;
	private TextView	tvShareLabel;
	private ImageView	btnFacebook;
	private ImageView	btnTwitter;
	private LinearLayout	layoutShare;
	private RelativeLayout	bodyLayout;

	private MediaRecorder 	mRecorder;
	private MediaPlayer		mPlayer;

	private String		audioPath;
	private boolean		recorded;
	private boolean		isRecording;
	private int 		recordingMaxDuration = 10000;
	
	private CountDownTimer	mRecordTimer;
	private CountDownTimer	mPlayTimer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_audio);

		tvRecordLabel = (TextView) findViewById(R.id.record_label_textView);
		tvWord = (TextView) findViewById(R.id.word_textView);
		tvCoinsLabel = (TextView) findViewById(R.id.coins_label_textView);
		tvCoins = (TextView) findViewById(R.id.coins_textView);
		btnSoundEffects = (ImageView) findViewById(R.id.sound_effects_mageView);
		btnRecord = (LinearLayout) findViewById(R.id.record_layout);
		btnPlayPause = (ImageView) findViewById(R.id.play_pause_imageView);
		recordStateImageView = (ImageView) findViewById(R.id.record_state_imageView);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		btnHome = (ImageView) findViewById(R.id.home_imageView);
		btnSave = (ImageView) findViewById(R.id.save_imageView);
		btnShare = (ImageView) findViewById(R.id.share_imageView);
		btnSend = (ImageView) findViewById(R.id.send_imageView);
		tvShareLabel = (TextView) findViewById(R.id.share_label_textView);
		btnFacebook = (ImageView) findViewById(R.id.facebook_iamgeView);
		btnTwitter = (ImageView) findViewById(R.id.twitter_iamgeView);
		layoutShare = (LinearLayout) findViewById(R.id.share_item_layout);
		bodyLayout = (RelativeLayout) findViewById(R.id.body_linearLayout);

		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvRecordLabel.setTypeface(btnFont);
		tvWord.setTypeface(btnFont);
		tvCoinsLabel.setTypeface(btnFont);
		tvCoins.setTypeface(btnFont);
		
		tvCoins.setText(Global.mUserData.coins + "");
		tvWord.setText(Global.mAudioWord.word);

		initListener();
	}

	private void initListener() {
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();

				if(GameTypeActivity.activity != null) {
					GameTypeActivity.activity.finish();
					GameTypeActivity.activity = null;
				}
				
				if(NewGameActivity.activity != null) {
					NewGameActivity.activity.finish();
					NewGameActivity.activity = null;
				}
			}
		});
		
		btnSoundEffects.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				TODO Auto-generated method stub
				Intent intent = new Intent(RecordAudioActivity.this, SoundEffectsActivity.class);
				startActivity(intent);
			}
		});

		btnSave.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btnShare.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layoutShare.setVisibility(View.VISIBLE);
			}
		});
		
		bodyLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				layoutShare.setVisibility(View.GONE);
			}
		});
		
		btnSend.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendGameData();
			}
		});
		
		btnRecord.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				if(!recorded) {
					if(isRecording)
						return;
					
					if(audioPath != null)
						new File(audioPath).delete();

					audioPath = Utils.getMediaSavePath() + "/" + Utils.getTempFileName() + ".3gp";
					
					mRecorder = new MediaRecorder();

					mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
					mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
					mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

					mRecorder.setOutputFile(audioPath);
					mRecorder.setMaxDuration(recordingMaxDuration);
					try {
						mRecorder.prepare();
					} catch (IllegalStateException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					mRecorder.start(); 
					
					mProgressBar.setMax(recordingMaxDuration / 100);
					
					if(mRecordTimer != null)
						mRecordTimer = null;
					
					mRecordTimer = new CountDownTimer(recordingMaxDuration, 100) {						
						@Override
						public void onTick(long millisUntilFinished) {
							// TODO Auto-generated method stub
							mProgressBar.setProgress(mProgressBar.getProgress() + 1);
							
							int progress = mProgressBar.getProgress();
							if(progress % 4 == 0)
								recordStateImageView.setImageResource(R.drawable.record_icon_on);
							else
								recordStateImageView.setImageResource(R.drawable.record_icon_off);
						}
						
						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							stopRecording();
						}
					};
					
					mRecordTimer.start();
					
					//
					recorded = false;
					isRecording = true;
					tvRecordLabel.setText("Recording");
					btnPlayPause.setImageResource(R.drawable.stop_button);
					//
				}
				else {
					if(mPlayer != null) {
						return;
					}
					
					recorded = false;
					
					if(audioPath != null)
						new File(audioPath).delete();
					
					audioPath = null;
					tvRecordLabel.setText("Record");
					recordStateImageView.setImageResource(R.drawable.record_icon_on);
					btnPlayPause.setImageResource(R.drawable.record_button);
				}
			}
		});
		
		btnPlayPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!recorded) {
					if(audioPath == null)
						return;
					
					stopRecording();
					
					return;
				}
				
				if(isRecording) {
					btnPlayPause.setImageResource(R.drawable.stop_button);
					tvRecordLabel.setText("RE-RECORD");
					recordStateImageView.setImageResource(R.drawable.re_record_icon);
					
					stopRecording();
				}
				else {
					if(mPlayer == null) {
						btnPlayPause.setImageResource(R.drawable.stop_button);
						
						if(mPlayer != null) {
							mPlayer.stop();
							mPlayer.release();
							mPlayer = null;
						}
						
						mPlayer = new MediaPlayer();
						
						try {
							mPlayer.setDataSource(audioPath);
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
						
						mPlayer.setOnCompletionListener(new OnCompletionListener() {
							
							@Override
							public void onCompletion(MediaPlayer mp) {
								// TODO Auto-generated method stub
								btnPlayPause.setImageResource(R.drawable.play_button);
								
								if(mPlayTimer != null) {
									mPlayTimer.cancel();
									mPlayTimer = null;
									mPlayer = null;
								}
							}
						});
						
						mProgressBar.setMax(mPlayer.getDuration() / 100);
						
						mPlayTimer = new CountDownTimer(mPlayer.getDuration(), 100) {
							
							@Override
							public void onTick(long millisUntilFinished) {
								// TODO Auto-generated method stub
								mProgressBar.setProgress(mProgressBar.getProgress() + 1);
							}
							
							@Override
							public void onFinish() {
								// TODO Auto-generated method stub
								btnPlayPause.setImageResource(R.drawable.play_button);
								mProgressBar.setProgress(0);
								
								mPlayer.stop();
								mPlayer.release();
								mPlayer = null;
							}
						};
						
						mPlayTimer.start();
					}
					else {
						if(mPlayer.isPlaying()) {
							mPlayer.stop();
							mPlayer.release();
							mPlayer = null;
						}
						
						if(mPlayTimer != null) {
							mPlayTimer.cancel();
							mPlayTimer = null;
						}
						
						mProgressBar.setProgress(0);
						btnPlayPause.setImageResource(R.drawable.play_button);
					}
				}
			}
		});
	}
	
	private void stopRecording() {
		if(mRecorder != null) {
			mRecorder.stop();
			mRecorder.reset();
			mRecorder.release();
			mRecorder = null;
		}
		
		isRecording = false;
		recorded = true;
		
		btnPlayPause.setImageResource(R.drawable.play_button);
		tvRecordLabel.setText("RE-RECORD");
		recordStateImageView.setImageResource(R.drawable.re_record_icon);
		
		if(mRecordTimer != null) {
			mRecordTimer.cancel();
			mRecordTimer = null;
		}
		
		mProgressBar.setProgress(0);
	}
	
	private void sendGameData() {
		if(!recorded || audioPath == null) {
			Utils.showOKDialog(this, "Please record a audio file before sending it to your opponent.", "OK");
			return;
		}
		
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "send_data";
		
		String audioString = Utils.base64Encoding(audioPath);
		if(audioString == null) {
			Utils.showOKDialog(this, "Failed to encoding audio file.", "OK");
			return;
		}
		
		String params = "game_round_id=" + Global.mCurGame.gameRoundId + "&data=" + audioString + "&app_id=" + Global.mUserData.token + "&version=" + Utils.getVersion();
		
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
	            Log.d("sendGameData(Audio) HTTP", "onSuccess: " + response);
	            
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
							Utils.showOKDialog(RecordAudioActivity.this, "Word is null", "OK");
							return;
						}
						
						finish();
						
						if(GameTypeActivity.activity != null) {
							GameTypeActivity.activity.finish();
							GameTypeActivity.activity = null;
						}
						
						if(NewGameActivity.activity != null) {
							NewGameActivity.activity.finish();
							NewGameActivity.activity = null;
						}
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(RecordAudioActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(RecordAudioActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(RecordAudioActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						case 20:
							//invalid game
							showConfirmDialog(RecordAudioActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 37:
							//data is mising
							showConfirmDialog(RecordAudioActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 21:
							//game deleted
							showConfirmDialog(RecordAudioActivity.this, strResp, "OK", Constants.gameIsDeletedAlert);
							break;	
						default:
							Utils.showOKDialog(RecordAudioActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(RecordAudioActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("sendGameData(Audio) HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(RecordAudioActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
						finish();

						if(GameTypeActivity.activity != null) {
							GameTypeActivity.activity.finish();
							GameTypeActivity.activity = null;
						}
						
						if(NewGameActivity.activity != null) {
							NewGameActivity.activity.finish();
							NewGameActivity.activity = null;
						}
						
						Intent intent = new Intent(RecordAudioActivity.this, LoginActivity.class);
						startActivity(intent);
						break;
					case Constants.apiErrorAlert:
						break;
					case Constants.gameIsDeletedAlert:
						finish();

						if(GameTypeActivity.activity != null) {
							GameTypeActivity.activity.finish();
							GameTypeActivity.activity = null;
						}
						
						if(NewGameActivity.activity != null) {
							NewGameActivity.activity.finish();
							NewGameActivity.activity = null;
						}
						break;
				}
			}
		});
		
		dialog = customBuilder.create();
		dialog.setCancelable(false);
		
		dialog.show();
	}
}
