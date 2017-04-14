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
import com.llc.performit.view.CustomDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

public class RecordVideoActivity extends Activity implements SurfaceHolder.Callback {
	
	private TextView	tvRecordLabel;
	private TextView	tvWord;
	private TextView	tvCoinsLabel;
	private TextView	tvCoins;
	private ImageView	btnHome;
	private LinearLayout	btnRecord;
	private ImageView	btnPlayPause;
	private ImageView	recordStateImageView;
	private ProgressBar	mProgressBar;
	private ImageView	btnRotateCamera;
	private SurfaceHolder 	surfaceHolder;
    private SurfaceView 	surfaceView;
    private VideoView		videoView;
    private LinearLayout	videoViewLayout;
    private ImageView	btnSend;
    
    private Camera 			mCamera;
    public MediaRecorder	mRecorder = new MediaRecorder();
    
    private int				curCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    
    private String		videoPath;
    private boolean		isRecording;
    private boolean		recorded;
    
    private CountDownTimer	mRecordTimer;
	private CountDownTimer	mPlayTimer;
	
	private int 		recordingMaxDuration = 10000;
	private int			videoLength = 0;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record_video);
		
		tvRecordLabel = (TextView) findViewById(R.id.record_label_textView);
		tvWord = (TextView) findViewById(R.id.word_textView);
		tvCoinsLabel = (TextView) findViewById(R.id.coins_label_textView);
		tvCoins = (TextView) findViewById(R.id.coins_textView);
		btnHome = (ImageView) findViewById(R.id.home_imageView);
		btnRecord = (LinearLayout) findViewById(R.id.record_layout);
		btnPlayPause = (ImageView) findViewById(R.id.play_pause_imageView);
		recordStateImageView = (ImageView) findViewById(R.id.record_state_imageView);
		mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
		btnRotateCamera = (ImageView) findViewById(R.id.rotate_camera_mageView);
		videoViewLayout = (LinearLayout) findViewById(R.id.videoView_layout);
		btnSend = (ImageView) findViewById(R.id.send_imageView);
		
		surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
		videoView = (VideoView) findViewById(R.id.videoView);
		
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		if(Camera.getNumberOfCameras() == 1){
			btnRotateCamera.setVisibility(View.INVISIBLE);
		}
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvRecordLabel.setTypeface(btnFont);
		tvWord.setTypeface(btnFont);
		tvCoinsLabel.setTypeface(btnFont);
		tvCoins.setTypeface(btnFont);
		
		tvCoins.setText(Global.mUserData.coins + "");
		tvWord.setText(Global.mVideoWord.word);
		
		initListener();
		
		//
//		int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
//		int topHeight, shareHeight;
//		LinearLayout topLayout = (LinearLayout) findViewById(R.id.linearLayout1);
//		RelativeLayout shareLayout = (RelativeLayout) findViewById(R.id.share_layout);
//		
//		android.view.ViewGroup.LayoutParams tp = topLayout.getLayoutParams(); 
//		android.view.ViewGroup.LayoutParams sp = shareLayout.getLayoutParams();
//		
//		int videoLayoutHeight = screenHeight - tp.height - sp.height;
//		
//		android.view.ViewGroup.LayoutParams vp = videoViewLayout.getLayoutParams();
//		vp.height = videoLayoutHeight;
//		videoViewLayout.setLayoutParams(vp);
		
		videoViewLayout.setVisibility(View.GONE);
		//
	}
	
	private void initListener() {
		btnHome.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
				GameTypeActivity.activity.finish();
				NewGameActivity.activity.finish();
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
					surfaceView.setBackgroundColor(RecordVideoActivity.this.getResources().getColor(R.color.transparent));
					
					if(isRecording)
						return;
					
					if(videoPath != null)
						new File(videoPath).delete();
					
					try {
						startRecording();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
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
							
							videoLength = (int) (recordingMaxDuration - millisUntilFinished);
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
					if(videoView.isPlaying())
						return;
					
					recorded = false;

					if(videoPath != null)
						new File(videoPath).delete();
					
					videoPath = null;
					
					tvRecordLabel.setText("Record");
					recordStateImageView.setImageResource(R.drawable.record_icon_on);
					btnPlayPause.setImageResource(R.drawable.record_video_button);
					
					if(mCamera == null) {
						surfaceHolder = surfaceView.getHolder();
						surfaceHolder.addCallback(RecordVideoActivity.this);
						surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
			    	}
					
					videoViewLayout.setVisibility(View.GONE);
					surfaceView.setVisibility(View.VISIBLE);
				}
			}
		});
		
		btnRotateCamera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(Camera.getNumberOfCameras() == 1)
					return;
				
				if(isRecording)
					return;
				
				if(curCameraId == Camera.CameraInfo.CAMERA_FACING_BACK)
					curCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
				else
					curCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
				
				mCamera.stopPreview();
				mCamera.setPreviewCallback(null);
		        mCamera.release();
		        mCamera = null;
		        surfaceHolder.removeCallback(RecordVideoActivity.this);
		        surfaceHolder = null;
				
				if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT))
					mCamera = Camera.open(curCameraId);
				else
					mCamera = Camera.open();
				
				mCamera.setDisplayOrientation(90);
				
				surfaceHolder = surfaceView.getHolder();
				surfaceHolder.addCallback(RecordVideoActivity.this);
				surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
				
				try {
					mCamera.setPreviewDisplay(surfaceHolder);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}               

				mCamera.startPreview();
			}
		});
		
		btnPlayPause.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!recorded) {
					if(videoPath == null)
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
					if(!videoView.isPlaying()) {
						//
						if(mCamera != null) {
//							mCamera.stopPreview();
//							mCamera.setPreviewCallback(null);
//					        mCamera.release();
//					        mCamera = null;
//					        surfaceHolder.removeCallback(RecordVideoActivity.this);
//					        surfaceHolder = null;
						}
						//
						
						videoView.setVideoURI(Uri.parse(videoPath));
						
						videoView.setOnCompletionListener(new OnCompletionListener() {
							
							@Override
							public void onCompletion(MediaPlayer mp) {
								// TODO Auto-generated method stub
								btnPlayPause.setImageResource(R.drawable.play_button);
								
								if(mPlayTimer != null) {
									mPlayTimer.cancel();
									mPlayTimer = null;
								}
								
								mProgressBar.setProgress(0);
							}
						});
						
						mPlayTimer = new CountDownTimer(videoLength, 100) {
							
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
								
								videoView.stopPlayback();
							}
						};
						
						mPlayTimer.start();
						
						videoView.start();
						
						mProgressBar.setMax(videoLength / 100);
						btnPlayPause.setImageResource(R.drawable.stop_button);
						
						videoViewLayout.setVisibility(View.VISIBLE);
//						surfaceView.setVisibility(View.INVISIBLE);
					}
					else {
						videoView.stopPlayback();
						
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
	
	protected void startRecording() throws IOException 
    {
    	mRecorder = new MediaRecorder();  // Works well
    	
    	mCamera.unlock();
        mRecorder.setCamera(mCamera);

        mRecorder.setPreviewDisplay(surfaceHolder.getSurface());
        mRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
        mRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
//        mRecorder.setVideoFrameRate(30);
//        mRecorder.setVideoEncodingBitRate(3000000);
        mRecorder.setVideoSize(640,480); 
        mRecorder.setMaxDuration(recordingMaxDuration);
        
        videoPath = Utils.getMediaSavePath() + "/" + Utils.getTempFileName() + ".mp4";
        mRecorder.setOutputFile(videoPath);
        
        //
		Global.videoPath = videoPath;
		//
        
        mRecorder.prepare();
        mRecorder.start();
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

	@Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		mCamera.setDisplayOrientation(90);
		try {
			mCamera.setPreviewDisplay(surfaceHolder);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}      
		
		mCamera.startPreview();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    	if(getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT))
			mCamera = Camera.open(curCameraId);
		else
			mCamera = Camera.open();
		
		if(mCamera == null) {
			Toast.makeText(getApplicationContext(), "Camera not available!", Toast.LENGTH_LONG).show();
			finish();
		}
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    	if(mCamera != null) {
    		mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
    	}
    }
    
    private void sendGameData() {
		if(!recorded || videoPath == null) {
			Utils.showOKDialog(this, "Please record a video file before sending it to your opponent.", "OK");
			return;
		}
		
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "send_data";
		
		String videoString = Utils.base64Encoding(videoPath);
		if(videoString == null) {
			Utils.showOKDialog(this, "Failed to encoding audio file.", "OK");
			return;
		}
		
		String params = "game_round_id=" + Global.mCurGame.gameRoundId + "&data=" + videoString + "&app_id=" + Global.mUserData.token + "&version=" + Utils.getVersion();
		
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
	            Log.d("sendGameData(Video) HTTP", "onSuccess: " + response);
	            
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
							Utils.showOKDialog(RecordVideoActivity.this, "Word is null", "OK");
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
							showConfirmDialog(RecordVideoActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(RecordVideoActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(RecordVideoActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						case 20:
							//invalid game
							showConfirmDialog(RecordVideoActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 37:
							//data is mising
							showConfirmDialog(RecordVideoActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 21:
							//game deleted
							showConfirmDialog(RecordVideoActivity.this, strResp, "OK", Constants.gameIsDeletedAlert);
							break;	
						default:
							Utils.showOKDialog(RecordVideoActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(RecordVideoActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("sendGameData(Video) HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(RecordVideoActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
						
						Intent intent = new Intent(RecordVideoActivity.this, LoginActivity.class);
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
