package com.llc.performit;





import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.llc.performit.common.Constants;
import com.llc.performit.common.Global;
import com.llc.performit.common.Utils;
import com.llc.performit.draw.ComicEditor;
import com.llc.performit.draw.drawBrushPoint;
import com.llc.performit.draw.ComicEditor.TouchModes;
import com.llc.performit.draw.drawColorPoint;
import com.llc.performit.draw.drawObjectArray;
import com.llc.performit.view.CustomDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.RectF;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class DrawActivity extends Activity {
	private ComicEditor mainView;
	private ImageButton brushBtn;
	public static LinearLayout subToolBar;
	public static LinearLayout subColorBar;
	private String strBrushType;
	private ImageButton circleBrushBtn, triangleBrushBtn, squareBrushBtn, ovalBrushBtn, 
	                    arrowBrushBtn, crossBrushBtn, hashtagBrushBtn, heartBrushBtn, moonBrushBtn, quotationBrushBtn, 
	                    starBrushBtn, peaceBrushBtn, smileyBrushBtn;
	private ImageButton	selectedTool;
	private SeekBar strokeBar;
	private ImageButton eraseBtn;
	private int currentStrokeWidth=4;
	private ImageButton undoBtn;
	private ImageButton colorBtn;
	private ImageButton clearBtn;
	private ImageButton saveBtn;
	private ImageButton	sendBtn;
	private ImageButton right_arrowBtn, left_arrowBtn;
	private ImageButton right_scroll_arrowBtn, left_scroll_arrowBtn;
	private ImageView	homeBtn;
	private ImageButton	addMoreButton;
	private LinearLayout	layoutColor;
	private ImageButton	btnColorLeft;
	private ImageButton	btnColorRight;
	private HorizontalScrollView	colorScrollView;
	
	private SharedPreferences mPrefs = null;
	private String lastSaveName = "";
	private HorizontalScrollView HScrollView;
	private HorizontalScrollView HTScrollView;
//	public static Button color1,color3,color4, color6, color10, color11;
	
	public static DrawActivity	drawActivity;
	
	private ArrayList<Integer>		freeColor;
	private ArrayList<Integer>		simpleColor;
	private ArrayList<Integer>		allColors;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_draw);
		brushBtn=(ImageButton)this.findViewById(R.id.brush_tool);
		colorBtn=(ImageButton)this.findViewById(R.id.color_tool);
		clearBtn=(ImageButton)this.findViewById(R.id.clearBtn);
		saveBtn=(ImageButton)this.findViewById(R.id.saveBtn);
		sendBtn=(ImageButton)this.findViewById(R.id.sendBtn);
		subToolBar=(LinearLayout)this.findViewById(R.id.sub_tool_bar);
		subColorBar=(LinearLayout)this.findViewById(R.id.sub_color_bar);
		left_arrowBtn=(ImageButton)this.findViewById(R.id.left_arrow);
		right_arrowBtn=(ImageButton)this.findViewById(R.id.right_arrow);
		left_scroll_arrowBtn=(ImageButton)this.findViewById(R.id.left_scroll_arrow);
		right_scroll_arrowBtn=(ImageButton)this.findViewById(R.id.right_scroll_arrow);
		selectedTool=(ImageButton)this.findViewById(R.id.selectedtool);
		strokeBar=(SeekBar)this.findViewById(R.id.strokeBar);
		strokeBar.setProgress(4);
		undoBtn=(ImageButton)this.findViewById(R.id.undoBtn);
		homeBtn = (ImageView) findViewById(R.id.home_imageView);
		addMoreButton = (ImageButton) findViewById(R.id.add_button);
		layoutColor = (LinearLayout) findViewById(R.id.color_layout);
		btnColorLeft = (ImageButton) findViewById(R.id.color_left_btn);
		btnColorRight = (ImageButton) findViewById(R.id.color_right_btn);
		colorScrollView = (HorizontalScrollView) findViewById(R.id.color_scrollView);
		
//		color1 = (Button) findViewById(R.id.color1);
//		color3 = (Button) findViewById(R.id.color3);
//		color4 = (Button) findViewById(R.id.color4);
//		color6 = (Button) findViewById(R.id.color6);
//		color10 = (Button) findViewById(R.id.color10);
//		color11 = (Button) findViewById(R.id.color11);
		
		mainView = (ComicEditor) findViewById(R.id.editor);
		eraseBtn=(ImageButton)findViewById(R.id.eraseBtn);
		
		mainView.clrPoint=new drawColorPoint();
		mainView.clrPoint.R= 34/255;
		mainView.clrPoint.G= 30/255;
		mainView.clrPoint.B= 30/255;
		strBrushType="circle";
		selectedTool.setBackgroundResource(R.drawable.circle_brush_preview);
		mainView.mTouchMode = TouchModes.PENCIL;
		mainView.setCurrentBrushType("circle-brush.png");
		mainView.setErase("0");
		
		drawActivity = this;
		
		circleBrushBtn=(ImageButton)this.findViewById(R.id.circleBrush);
		triangleBrushBtn=(ImageButton)this.findViewById(R.id.triangleBrush);
		squareBrushBtn=(ImageButton)this.findViewById(R.id.squareBrush);
		ovalBrushBtn=(ImageButton)this.findViewById(R.id.ovalBrush);
		arrowBrushBtn=(ImageButton)this.findViewById(R.id.arrowBrush);
		crossBrushBtn=(ImageButton)this.findViewById(R.id.crossBrush);
		hashtagBrushBtn=(ImageButton)this.findViewById(R.id.hashtagBrush);
		moonBrushBtn=(ImageButton)this.findViewById(R.id.moonBrush);
		quotationBrushBtn=(ImageButton)this.findViewById(R.id.quotationBrush);
		peaceBrushBtn=(ImageButton)this.findViewById(R.id.peaceBrush);
		smileyBrushBtn=(ImageButton)this.findViewById(R.id.smileyBrush);
		heartBrushBtn=(ImageButton)this.findViewById(R.id.heartBrush);
		starBrushBtn=(ImageButton)this.findViewById(R.id.starBrush);
		HScrollView=(HorizontalScrollView)this.findViewById(R.id.tool_scroll);
		HTScrollView=(HorizontalScrollView)this.findViewById(R.id.HTScroll);
		
		addBrushPacket(-1);
		
		homeBtn.setOnClickListener(new OnClickListener() {
			
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
		
		addMoreButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(DrawActivity.this, AddMoreDrawItemActivity.class);
				startActivity(intent);
			}
		});
		
		left_arrowBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				HScrollView.setScrollX(HScrollView.getScrollX()-50);
				
			}
		});
		right_arrowBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				HScrollView.setScrollX(HScrollView.getScrollX()+50);
				
			}
		});
		left_scroll_arrowBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HTScrollView.setScrollX(HTScrollView.getScrollX()-50);
				
			}
		});
		right_scroll_arrowBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				HTScrollView.setScrollX(HTScrollView.getScrollX()+50);
				
			}
		});
		
		btnColorLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				colorScrollView.setScrollX(colorScrollView.getScrollX() - 50);
			}
		});
		
		btnColorRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				colorScrollView.setScrollX(colorScrollView.getScrollX() + 50);
			}
		});
		
	/////selected circlebrush
		starBrushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strBrushType="star";
				selectedTool.setVisibility(View.VISIBLE);
				selectedTool.setBackgroundResource(R.drawable.star_brush_preview);
				mainView.mTouchMode = TouchModes.STAR;
				mainView.setCurrentBrushType("star-brush.png");
				mainView.setErase("0");
				
			}
		});
		heartBrushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strBrushType="heart";
				selectedTool.setVisibility(View.VISIBLE);
				selectedTool.setBackgroundResource(R.drawable.heart_brush_preview);
				mainView.mTouchMode = TouchModes.HEART;
				mainView.setCurrentBrushType("heart-brush.png");
				mainView.setErase("0");
				
			}
		});
		smileyBrushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strBrushType="smiley";
				selectedTool.setVisibility(View.VISIBLE);
				selectedTool.setBackgroundResource(R.drawable.smiley_brush_preview);
				mainView.mTouchMode = TouchModes.SMILEY;
				mainView.setCurrentBrushType("smiley-brush.png");
				mainView.setErase("0");
				
			}
		});
		peaceBrushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strBrushType="peace";
				selectedTool.setVisibility(View.VISIBLE);
				selectedTool.setBackgroundResource(R.drawable.peace_brush_preview);
				mainView.mTouchMode = TouchModes.PEACE;
				mainView.setCurrentBrushType("peace-brush.png");
				mainView.setErase("0");
				
			}
		});
		quotationBrushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strBrushType="quotation";
				selectedTool.setVisibility(View.VISIBLE);
				selectedTool.setBackgroundResource(R.drawable.quotation_brush_preview);
				mainView.mTouchMode = TouchModes.QUOTATION;
				mainView.setCurrentBrushType("quotation-brush.png");
				mainView.setErase("0");
				
			}
		});
		moonBrushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strBrushType="moon";
				selectedTool.setVisibility(View.VISIBLE);
				selectedTool.setBackgroundResource(R.drawable.moon_brush_preview);
				mainView.mTouchMode = TouchModes.MOON;
				mainView.setCurrentBrushType("moon-brush.png");
				mainView.setErase("0");
				
			}
		});
		hashtagBrushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strBrushType="hashtag";
				selectedTool.setVisibility(View.VISIBLE);
				selectedTool.setBackgroundResource(R.drawable.hashtag_brush_preview);
				mainView.mTouchMode = TouchModes.HASHTAG;
				mainView.setCurrentBrushType("hashtag-brush.png");
				mainView.setErase("0");
				
			}
		});
		crossBrushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strBrushType="cross";
				selectedTool.setVisibility(View.VISIBLE);
				selectedTool.setBackgroundResource(R.drawable.cross_brush_preview);
				mainView.mTouchMode = TouchModes.CROSS;
				mainView.setCurrentBrushType("cross-brush.png");
				mainView.setErase("0");
				
			}
		});
		arrowBrushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strBrushType="arrow";
				selectedTool.setVisibility(View.VISIBLE);
				selectedTool.setBackgroundResource(R.drawable.arrow_brush_preview);
				mainView.mTouchMode = TouchModes.ARROW;
				mainView.setCurrentBrushType("arrow-brush.png");
				mainView.setErase("0");
				
			}
		});
		ovalBrushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strBrushType="oval";
				selectedTool.setVisibility(View.VISIBLE);
				selectedTool.setBackgroundResource(R.drawable.oval_brush_preview);
				mainView.mTouchMode = TouchModes.OVAL;
				mainView.setCurrentBrushType("oval-brush.png");
				mainView.setErase("0");
				
			}
		});
		squareBrushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				strBrushType="square";
				selectedTool.setVisibility(View.VISIBLE);
				selectedTool.setBackgroundResource(R.drawable.square_brush_preview);
				mainView.mTouchMode = TouchModes.SQUARE;
				mainView.setCurrentBrushType("square-brush.png");
				mainView.setErase("0");
				
			}
		});
			circleBrushBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					strBrushType="circle";
					selectedTool.setVisibility(View.VISIBLE);
					selectedTool.setBackgroundResource(R.drawable.circle_brush_preview);
					mainView.mTouchMode = TouchModes.PENCIL;
					mainView.setCurrentBrushType("circle-brush.png");
					mainView.setErase("0");
					
				}
			});
			triangleBrushBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					strBrushType="triangle";
					selectedTool.setVisibility(View.VISIBLE);
					selectedTool.setBackgroundResource(R.drawable.triangle_brush_preview);
					mainView.mTouchMode = TouchModes.TRIANGLE;
					mainView.setCurrentBrushType("triangle-brush.png");
					mainView.setErase("0");
					
					
				}
			});
		
////selected brush
		brushBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				subToolBar.setVisibility(View.VISIBLE);
				subColorBar.setVisibility(View.GONE);
			}
		});
		
/////selected color
		colorBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				subColorBar.setVisibility(View.VISIBLE);
				subToolBar.setVisibility(View.GONE);
				
			}
		});
//////selected erase
		eraseBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainView.mTouchMode = TouchModes.ERASER;
				mainView.setCurrentStrokeWidth(currentStrokeWidth);
				mainView.setCurrentBrushType("circle-brush.png");
				mainView.setErase("1");
				
				subColorBar.setVisibility(View.GONE);
				subToolBar.setVisibility(View.VISIBLE);
				
			}
		});
//////selected undo
		undoBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainView.popState();
				
			}
		});
//////selected clear
		clearBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mainView.resetObjects ();
				mainView.invalidate();
			}
		});
//////save PanelScreen/////
		saveBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Date d=new Date();
				String s="";
				s=String.format("%d", d.getTime());
				doSave(s,false);
				
			}
		});
////////selected send
		sendBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				mainView.drawTimerBrush();
				
//				// for test
//				Global.drawObject = mainView.objArray;
//				// end
//				
//				finish();
//				GameTypeActivity.activity.finish();
//				NewGameActivity.activity.finish();
				
				sendGameData();
				
			}
		});

/////strockeBar Seek/////
		strokeBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
			
			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				currentStrokeWidth=progress;
				mainView.setCurrentStrokeWidth(currentStrokeWidth);
				
				
				
			}
		});
		
		//////////selected color////////
		
//		color1.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//
//				mainView.setCurrentColor(Color.parseColor("#E92319"));
//				mainView.clrPoint=new drawColorPoint();
//				mainView.clrPoint.R=(float)233/255;
//				mainView.clrPoint.G=(float)35/255;
//				mainView.clrPoint.B=(float)25/255;
//				
//				
//			}
//		});
//
//
//		color3.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//
//				mainView.setCurrentColor(Color.parseColor("#FFFFFF"));
//				mainView.clrPoint=new drawColorPoint();
//				mainView.clrPoint.R=(float)255/255;
//				mainView.clrPoint.G=(float)255/255;
//				mainView.clrPoint.B=(float)255/255;
//			}
//		});
//
//		color4.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//
//				mainView.setCurrentColor(Color.parseColor("#F5EE22"));
//				mainView.clrPoint=new drawColorPoint();
//				mainView.clrPoint.R=(float)245/255;
//				mainView.clrPoint.G=(float)238/255;
//				mainView.clrPoint.B=(float)34/255;
//			}
//		});
//
//
//		color6.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//
//				mainView.setCurrentColor(Color.parseColor("#221E1E"));
//				mainView.clrPoint=new drawColorPoint();
//				mainView.clrPoint.R=(float)34/255;
//				mainView.clrPoint.G=(float)30/255;
//				mainView.clrPoint.B=(float)30/255;
//			}
//		});
//
//
//		color10.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//
//				mainView.setCurrentColor(Color.parseColor("#22A8F5"));
//				mainView.clrPoint=new drawColorPoint();
//				mainView.clrPoint.R=(float)34/255;
//				mainView.clrPoint.G=(float)168/255;
//				mainView.clrPoint.B=(float)245/255;
//			}
//		});
//
//		color11.setOnClickListener(new View.OnClickListener() {
//			public void onClick(View v) {
//
//				mainView.setCurrentColor(Color.parseColor("#E9BB19"));
//				mainView.clrPoint=new drawColorPoint();
//				mainView.clrPoint.R=(float)233/255;
//				mainView.clrPoint.G=(float)187/255;
//				mainView.clrPoint.B=(float)25/255;
//			}
//		});
	}
	
	private void doSave(String fname, boolean doShare) {
		CharSequence text = getResources().getString(R.string.comic_saved_as)
				+ " ";
		try {
			String ReservedChars = "|\\?*<\":>+[]/'";
			for (char c : ReservedChars.toCharArray()) {
				fname = fname.replace(c, '_');
			}
			String value = fname;
			Bitmap b = mainView.getSaveBitmap();
			if (b == null) {
				text = getResources().getString(R.string.comic_save_fail_1);
				;
				Toast.makeText(this, text, Toast.LENGTH_LONG).show();
				return;
			}
			File folder = getFilesDir();
			if (externalStorageAvailable()) {
				try {
					folder = Environment
							.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
					if (!folder.exists() || !folder.canWrite()) {
						folder = Environment
								.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
					}
					if (!folder.exists() || !folder.canWrite()) {
						folder = Environment.getExternalStorageDirectory();
					}
				} catch (Exception e) {
					folder = Environment.getExternalStorageDirectory();
				} catch (Error e) {
					folder = Environment.getExternalStorageDirectory();
				}
				if (!folder.exists() || !folder.canWrite()) {
					folder = getFilesDir();
				}
			}
			/*
			 * String folder =
			 * Environment.getExternalStorageDirectory().toString() +
			 * "/Pictures"; try { folder =
			 * Environment.getExternalStoragePublicDirectory
			 * (Environment.DIRECTORY_PICTURES).toString(); } catch
			 * (NoSuchFieldError e) {
			 * 
			 * }
			 */
			String ext = ".jpg";
			String fullname = folder.getAbsolutePath() + File.separator + value
					+ ext;
			Map<String, String> hm = new HashMap<String, String>();
			hm.put("filename", fullname);
			// FlurryAgent.logEvent("Save image", hm);
			FileOutputStream fos;
			if (folder == getFilesDir())
				fos = openFileOutput(value + ext, Context.MODE_WORLD_WRITEABLE);
			else {
				File f2 = new File(fullname);// openFileOutput(fname,
												// Context.MODE_PRIVATE);//new
												// FileOutputStream(fullname);
				fos = new FileOutputStream(f2);
			}
			if (ext.equals(".png"))
				b.compress(CompressFormat.PNG, 95, fos);
			else
				b.compress(CompressFormat.JPEG, 95, fos);
			fos.close();
			// FlurryAgent.logEvent("Save done");
			String[] str = new String[1];
			str[0] = fullname;
			if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.FROYO) {
				MediaScannerConnection.scanFile(this, str, null, null);
			}
			text = text + value + ext + " "
					+ getResources().getString(R.string.saved_end);
			;
			lastSaveName = value;
			setDetailTitle();
			if (doShare) {
				Intent share = new Intent(Intent.ACTION_SEND);
				if (ext.equals(".png"))
					share.setType("image/png");
				else
					share.setType("image/jpeg");

				share.putExtra(Intent.EXTRA_STREAM,
						Uri.parse("file://" + fullname.replace(" ", "%20")));
				share.putExtra(Intent.EXTRA_TITLE, value);

				startActivity(Intent.createChooser(share, getResources()
						.getString(R.string.share_comic)));
			}
		} catch (Exception e) {
			Map<String, String> hm = new HashMap<String, String>();
			hm.put("text", e.toString());
			e.printStackTrace();
			text = getResources().getString(R.string.comic_save_fail_2)
					+ e.toString();
		} catch (Error e) {
			Map<String, String> hm = new HashMap<String, String>();
			hm.put("text", e.toString());
			e.printStackTrace();
			text = getResources().getString(R.string.comic_save_fail_2)
					+ e.toString();
		}
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}
	
	private boolean externalStorageAvailable() {
		boolean mExternalStorageAvailable;
		boolean mExternalStorageWriteable;
		String state = Environment.getExternalStorageState();

		if (state.equals(Environment.MEDIA_MOUNTED)) {
			// We can read and write the media
			mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (state.equals(Environment.MEDIA_MOUNTED_READ_ONLY)) {
			// We can only read the media
			mExternalStorageAvailable = true;
			mExternalStorageWriteable = false;
		} else {
			// Something else is wrong. It may be one of many other states, but
			// all we need
			// to know is we can neither read nor write
			mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		return mExternalStorageAvailable && mExternalStorageWriteable;
	}
	
	private void setDetailTitle() {
		if (lastSaveName != "")
			setTitle(getString(R.string.app_name)
					+ " - "
					+ lastSaveName
					+ " - "
					+ String.format("%.0f%%", mainView.getCanvasScale() * 100.0));
		else
			setTitle(getString(R.string.app_name)
					+ " - "
					+ String.format("%.0f%%", mainView.getCanvasScale() * 100.0));
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		layoutColor.removeAllViews();
		
		generateFreeColors();
		loadFreeColors();
		
		boolean isPurchasedAllColor = false, isPurchasedSimpleColor = false;;
		
		for(int i = 0; i < Global.mInAppPacketsArray.size(); i++) {
			if(Global.mInAppPacketsArray.get(i).packetId == Constants.kBrushesAllPackage &&
					Global.mInAppPacketsArray.get(i).purchased == 1) {
				addBrushPacket(Constants.kBrushesAllPackage);
			}

			if(Global.mInAppPacketsArray.get(i).packetId == Constants.kBrushes1Package &&
					Global.mInAppPacketsArray.get(i).purchased == 1) {
				addBrushPacket(Constants.kBrushes1Package);
			}
			
			if(Global.mInAppPacketsArray.get(i).packetId == Constants.kColorsAllPackage &&
					Global.mInAppPacketsArray.get(i).purchased == 1) {
				isPurchasedAllColor = true;
			}
			
			if(Global.mInAppPacketsArray.get(i).packetId == Constants.kColors1Package &&
					Global.mInAppPacketsArray.get(i).purchased == 1) {
				isPurchasedSimpleColor = true;
			}
		}
		
		if(isPurchasedAllColor) {
			generateAllColors();
			loadAllColors();
		}
		else {
			if(isPurchasedSimpleColor) {
				generatePackageColors();
				loadSimpleColors();
			}
		}
	}
	
	private void sendGameData() {
		String linesStr = "";
		
		if(mainView.objArray == null)
			return;
		
		for(int i = 0; i < mainView.objArray.size(); i++) {
			drawObjectArray line = mainView.objArray.get(i);
			String lineStr = "";
			
			
			for(int j = 0; j < line.drawObjArray.size(); j++) {
				drawBrushPoint point = line.drawObjArray.get(j);
				String pointStr = genStringFromBrush(point);
				
				if(j == 0)
					lineStr = pointStr;
				else
					lineStr = lineStr + "," + pointStr;
			}
			
			lineStr = "[" + lineStr + "]";
			
			if(i == 0)
				linesStr = lineStr;
			else
				linesStr = linesStr + "," + lineStr;
		}
		
		linesStr = "\"lines\":" + "[" + linesStr + "]";
		
		String bgStr = "";
		bgStr = "\"background\":{\"red\":\"1.000000\",\"green\":\"1.000000\",\"blue\":\"1.000000\"}";
		
		String data = "{" + linesStr + "," + bgStr + "}";
		
		// for test
		int dataLen = data.length();
		String temp = data.substring(dataLen - 20, dataLen);
		// end
		
		final ProgressDialog progress;
		
		progress = new ProgressDialog(this);
		progress.show();
		
		AsyncHttpClient	httpClient = new AsyncHttpClient();
		httpClient.setTimeout(Constants.CONNECTION_TIMEOUT);
		
		String function = "send_data";
		
		String params = "game_round_id=" + Global.mCurGame.gameRoundId + "&data=" + data + "&app_id=" + Global.mUserData.token + "&version=" + Utils.getVersion();
		
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
							Utils.showOKDialog(DrawActivity.this, "Word is null", "OK");
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
							showConfirmDialog(DrawActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(DrawActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(DrawActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.invalidAppIDAlert);
							break;
						case 20:
							//invalid game
							showConfirmDialog(DrawActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 37:
							//data is mising
							showConfirmDialog(DrawActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 21:
							//game deleted
							showConfirmDialog(DrawActivity.this, strResp, "OK", Constants.gameIsDeletedAlert);
							break;	
						default:
							Utils.showOKDialog(DrawActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(DrawActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("sendGameData(Audio) HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(DrawActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
						
						Intent intent = new Intent(DrawActivity.this, LoginActivity.class);
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
		
	private String genStringFromBrush(drawBrushPoint point) {
		String pointStr = "";
		String brushColor = "", brushSize = "", brushName = "", nsPoint = "", erased = "";
		
		brushColor = String.format("\"brush-color\":{\"red\":\"%f\",\"green\":\"%f\",\"blue\":\"%f\"},", point.RGB.R, point.RGB.G, point.RGB.B);
		brushSize = String.format("\"brush-size\":%s,", point.brushSize);
		brushName = String.format("\"brush-name\":\"%s\",", point.brushName);
		nsPoint = String.format("\"point\":\"NSPoint: {%s, %s}\",", point.pointX, point.pointY);
		erased = String.format("\"erased\":%s", point.erased == "0" ? "false" : "true");
		
		pointStr = "{" + brushColor + brushSize + brushName + nsPoint + erased + "}";
		
		Log.e("genStringFromBrush", pointStr);
		
		return pointStr;
	}
	
	private void addBrushPacket(int packetId) {
		if(packetId == -1) {
			circleBrushBtn.setVisibility(View.VISIBLE);
			triangleBrushBtn.setVisibility(View.GONE);
			squareBrushBtn.setVisibility(View.GONE);
			ovalBrushBtn.setVisibility(View.GONE);
			arrowBrushBtn.setVisibility(View.GONE);
			crossBrushBtn.setVisibility(View.GONE);
			hashtagBrushBtn.setVisibility(View.GONE);
			heartBrushBtn.setVisibility(View.GONE);
			moonBrushBtn.setVisibility(View.GONE);
			quotationBrushBtn.setVisibility(View.GONE);
			starBrushBtn.setVisibility(View.GONE);
			peaceBrushBtn.setVisibility(View.GONE);
			smileyBrushBtn.setVisibility(View.GONE);
		}
		
		if(packetId == Constants.kBrushesAllPackage) {
			circleBrushBtn.setVisibility(View.VISIBLE);
			triangleBrushBtn.setVisibility(View.VISIBLE);
			squareBrushBtn.setVisibility(View.VISIBLE);
			ovalBrushBtn.setVisibility(View.VISIBLE);
			arrowBrushBtn.setVisibility(View.VISIBLE);
			crossBrushBtn.setVisibility(View.VISIBLE);
			hashtagBrushBtn.setVisibility(View.VISIBLE);
			heartBrushBtn.setVisibility(View.VISIBLE);
			moonBrushBtn.setVisibility(View.VISIBLE);
			quotationBrushBtn.setVisibility(View.VISIBLE);
			starBrushBtn.setVisibility(View.VISIBLE);
			peaceBrushBtn.setVisibility(View.VISIBLE);
			smileyBrushBtn.setVisibility(View.VISIBLE);
		}
		
		if(packetId == Constants.kBrushes1Package) {
			circleBrushBtn.setVisibility(View.VISIBLE);
			triangleBrushBtn.setVisibility(View.VISIBLE);
			squareBrushBtn.setVisibility(View.VISIBLE);
			ovalBrushBtn.setVisibility(View.GONE);
			arrowBrushBtn.setVisibility(View.GONE);
			crossBrushBtn.setVisibility(View.GONE);
			hashtagBrushBtn.setVisibility(View.GONE);
			heartBrushBtn.setVisibility(View.GONE);
			moonBrushBtn.setVisibility(View.GONE);
			quotationBrushBtn.setVisibility(View.GONE);
			starBrushBtn.setVisibility(View.GONE);
			peaceBrushBtn.setVisibility(View.GONE);
			smileyBrushBtn.setVisibility(View.GONE);
		}
	}
	
	private void generateFreeColors() {
		freeColor = new ArrayList<Integer>();
		
		//black
	    int color = Color.rgb(0, 0, 0);
	    freeColor.add(Integer.valueOf(color));
	    
	    //white
	    color = Color.rgb(255, 255, 255);
	    freeColor.add(Integer.valueOf(color));
	    
	    //red
	    color = Color.rgb(241, 24, 24);
	    freeColor.add(Integer.valueOf(color));
	    
	    //brown
	    color = Color.rgb(135, 108, 80);
	    freeColor.add(Integer.valueOf(color));
	    
	    //blue
	    color = Color.rgb(22, 100, 247);
	    freeColor.add(Integer.valueOf(color));
	    
	    //blue
	    color = Color.rgb(227, 241, 22);
	    freeColor.add(Integer.valueOf(color));
	}
	
	private void loadFreeColors() {
		for(int i = 0; i < freeColor.size(); i++) {
			final Integer color = freeColor.get(i);
			
			loadColorItem(color);
		}
	}
	
	private void loadColorItem(final Integer color) {
		float[] outerRadii = {5.f,5.f,5.f,5.f,5.f,5.f,5.f,5.f};
    	float[] innerRadii = {5.f,5.f,5.f,5.f,5.f,5.f,5.f,5.f};
    	
    	LayoutInflater mInflater = LayoutInflater.from(this);
    	
		RoundRectShape shape = new RoundRectShape(outerRadii, new RectF(0.0f,0.0f,0.0f,0.0f), innerRadii) {
    		@Override
			public void draw(Canvas canvas, Paint paint) {
				paint.setColor(color);
	            
				super.draw(canvas, paint);
    		}
    	};
    	
    	ShapeDrawable d = new ShapeDrawable(shape);
    	
    	View layoutView = mInflater.inflate(R.layout.draw_item_layout, null);
    	ImageView imgView = (ImageView) layoutView.findViewById(R.id.brush_imageView);
    	imgView.setVisibility(View.GONE);
    	layoutView.setBackgroundDrawable(d);
    	
    	layoutColor.addView(layoutView);
    	
    	MarginLayoutParams p = (MarginLayoutParams) layoutView.getLayoutParams();
		p.width = 40;
		p.height = 30;
		p.rightMargin = 5;
		p.leftMargin = 5;
		
		layoutView.setLayoutParams(p);
		
		layoutView.setTag(color);
		
		layoutView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mainView.setCurrentColor(color);
				
				mainView.clrPoint = new drawColorPoint();
				mainView.clrPoint.R = (float)Color.red(color)/255;
				mainView.clrPoint.G = (float)Color.green(color)/255;
				mainView.clrPoint.B = (float)Color.blue(color)/255;
			}
		});
	}
	
	private void generatePackageColors() {
		simpleColor = new ArrayList<Integer>();
		
		//pink
	    int color = Color.rgb(222, 33, 241);
	    simpleColor.add(Integer.valueOf(color));
	    
	    
	    //green
	    color = Color.rgb(31, 241, 58);
	    simpleColor.add(Integer.valueOf(color));
	    
	    //purple
	    color = Color.rgb(111, 25, 150);
	    simpleColor.add(Integer.valueOf(color));
	    
	    //orange
	    color = Color.rgb(255, 182, 24);
	    simpleColor.add(Integer.valueOf(color));
	    
	    //grey
	    color = Color.rgb(133, 133, 133);
	    simpleColor.add(Integer.valueOf(color));
	}
	
	private void loadSimpleColors() {
		for(int i = 0; i < simpleColor.size(); i++) {
			final Integer color = simpleColor.get(i);
			
			loadColorItem(color);
		}
	}
	
	private void generateAllColors() {
		int red   = 255;
	    int green = 0;
	    int blue  = 0;
	    
	    allColors = new ArrayList<Integer>();
	    
	    for (green = 0; green <=255; green+=15) {
	    	int color = Color.rgb(red, green, blue);
	    	allColors.add(Integer.valueOf(color));
	    }
	    
	    for (red = 240; red >=0; red-=15) {
	    	int color = Color.rgb(red, green, blue);
	    	allColors.add(Integer.valueOf(color));
	    }
	    
	    red   = 0;
	    green = 255;
	    blue  = 0;
	    
	    for (blue = 15; blue <= 255; blue+=15) {
	    	int color = Color.rgb(red, green, blue);
	    	allColors.add(Integer.valueOf(color));
	    }
	    
	    red   = 0;
	    green = 255;
	    blue  = 255;
	    
	    for (green = 240; green >= 0; green-=15) {
	    	int color = Color.rgb(red, green, blue);
	    	allColors.add(Integer.valueOf(color));
	    }
	    
	    red   = 0;
	    green = 0;
	    blue  = 255;
	    
	    for (red = 15; red <= 255; red+=15) {
	    	int color = Color.rgb(red, green, blue);
	    	allColors.add(Integer.valueOf(color));
	    }
	    
	    red   = 255;
	    green = 0;
	    blue  = 255;
	    
	    for (blue = 240; blue >= 15; blue-=15) {
	    	int color = Color.rgb(red, green, blue);
	    	allColors.add(Integer.valueOf(color));
	    }
	    
	    red   = 0;
	    green = 0;
	    blue  = 0;
	    
	    for (int i = 0; i <= 255; i+=25) {
	    	red   = i;
	        green = i;
	        blue  = i;
	        if(red > 249)
	            red = 255;
	        if(green > 249)
	            green = 255;
	        if(blue > 249)
	            blue = 255;
	        
	        int color = Color.rgb(red, green, blue);
	    	allColors.add(Integer.valueOf(color));
	    }
	}
	
	private void loadAllColors() {
		for(int i = 0; i < allColors.size(); i++) {
			final Integer color = allColors.get(i);
			
			loadColorItem(color);
		}
	}
}
