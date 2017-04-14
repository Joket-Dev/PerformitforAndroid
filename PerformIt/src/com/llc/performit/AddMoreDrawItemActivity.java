package com.llc.performit;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.StringEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import com.llc.performit.common.Constants;
import com.llc.performit.common.Global;
import com.llc.performit.common.Utils;
import com.llc.performit.model.GameItem;
import com.llc.performit.model.PacketItem;
import com.llc.performit.view.CustomDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import android.R.integer;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AddMoreDrawItemActivity extends Activity {
	
	private ImageView			btnClose;
	private TextView			tvTitle;
	private TextView			tvAllPatternLabel;
	private TextView			tvSimplePatternLabel;
	private TextView			tvSimpleColorLabel;
	private TextView			tvAllColorLabel;
	private TextView			tvSpecialBrushLabel;
	
	private ImageView			btnAllPatternPurchase;
	private ImageView			btnSimplePatternPurchase;
	private ImageView			btnSimpleColorPurchase;
	private ImageView			btnAllColorPurchase;
	private ImageView			btnSpecialBrushPurchase;
	
	private LinearLayout		layoutAllPattern;
	private LinearLayout		layoutSimplePattern;
	private LinearLayout		layoutSimpleColor;
	private LinearLayout		layoutAllColor;
	private LinearLayout		layoutSpecialBrush;
	
	private int[]				allBrush = {R.drawable.circle_brush_preview, R.drawable.square_brush_preview, R.drawable.triangle_brush_preview,
			R.drawable.star_brush_preview, R.drawable.oval_brush_preview, R.drawable.hashtag_brush_preview, R.drawable.smiley_brush_preview,
			R.drawable.peace_brush_preview, R.drawable.cross_brush_preview, R.drawable.moon_brush_preview, R.drawable.heart_brush_preview,
			R.drawable.arrow_brush_preview, R.drawable.quotation_brush_preview};
	private int[]				simpleBrush = {R.drawable.square_brush_preview, R.drawable.triangle_brush_preview,
			R.drawable.star_brush_preview};
	
	private ArrayList<Integer>		allColors;
	private ArrayList<Integer>		simpleColors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_more_draw_item);
		
		btnClose = (ImageView) findViewById(R.id.close_imageView);
		tvTitle = (TextView) findViewById(R.id.title_textView);
		tvAllPatternLabel = (TextView) findViewById(R.id.all_pattern_label_textView);
		tvSimplePatternLabel = (TextView) findViewById(R.id.simple_pattern_label_textView);
		tvSimpleColorLabel = (TextView) findViewById(R.id.simple_color_label_textView);
		tvAllColorLabel = (TextView) findViewById(R.id.all_color_label_textView);
		tvSpecialBrushLabel = (TextView) findViewById(R.id.special_brush_label_textView);
		
		btnAllPatternPurchase = (ImageView) findViewById(R.id.all_pattern_purchase_imageView);
		btnSimplePatternPurchase = (ImageView) findViewById(R.id.simple_pattern_purchase_imageView);
		btnSimpleColorPurchase = (ImageView) findViewById(R.id.simple_color_purchase_imageView);
		btnAllColorPurchase = (ImageView) findViewById(R.id.all_color_purchase_imageView);
		btnSpecialBrushPurchase = (ImageView) findViewById(R.id.special_brush_purchase_imageView);
		
		layoutAllPattern = (LinearLayout) findViewById(R.id.all_pattern_layout);
		layoutSimplePattern = (LinearLayout) findViewById(R.id.simple_pattern_layout);
		layoutSimpleColor = (LinearLayout) findViewById(R.id.simple_color_layout);
		layoutAllColor = (LinearLayout) findViewById(R.id.all_color_layout);
		layoutSpecialBrush = (LinearLayout) findViewById(R.id.special_brush_layout);
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvTitle.setTypeface(btnFont);
		tvAllPatternLabel.setTypeface(btnFont);
		tvSimplePatternLabel.setTypeface(btnFont);
		tvSimpleColorLabel.setTypeface(btnFont);
		tvAllColorLabel.setTypeface(btnFont);
		tvSpecialBrushLabel.setTypeface(btnFont);
		
		initListener();
		
		loadDrawItems();
		loadColors();
	}
	
	private void initListener() {
		btnClose.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
		btnSimplePatternPurchase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int packetId = Constants.kBrushes1Package;
				
				showPurchaseConfirDlg(packetId);
			}
		});
		
		btnAllPatternPurchase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int packetId = Constants.kBrushesAllPackage;
				
				showPurchaseConfirDlg(packetId);
			}
		});
		
		btnSimpleColorPurchase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				int packetId = Constants.kColors1Package;
				
				showPurchaseConfirDlg(packetId);
			}
		});
		
		btnAllColorPurchase.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				int packetId = Constants.kColorsAllPackage;
				
				showPurchaseConfirDlg(packetId);
			}
		});
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
		
		Dialog dialog = new Dialog(AddMoreDrawItemActivity.this);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(AddMoreDrawItemActivity.this);
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
	
	private void loadDrawItems() {
		LayoutInflater mInflater;
		
		mInflater = LayoutInflater.from(this);
		
		for(int i = 0; i < allBrush.length; i++) {
			View layoutView = mInflater.inflate(R.layout.draw_item_layout, null);
			
			ImageView imgView = (ImageView) layoutView.findViewById(R.id.brush_imageView);
			imgView.setImageResource(allBrush[i]);
			
			layoutAllPattern.addView(layoutView);
			
			MarginLayoutParams p = (MarginLayoutParams) layoutView.getLayoutParams();
			p.width = 30;
			p.height = 30;
			p.rightMargin = 15;
			p.leftMargin = 15;
			
			layoutView.setLayoutParams(p);
		}
		
		boolean purchased = Utils.isPurchasedPacket(Constants.kBrushesAllPackage);
		if(purchased)
			btnAllPatternPurchase.setImageResource(R.drawable.purchase_button_di);
		else
			btnAllPatternPurchase.setImageResource(R.drawable.purchase_button);
		
		for(int i = 0; i < simpleBrush.length; i++) {
			View layoutView = mInflater.inflate(R.layout.draw_item_layout, null);
			
			ImageView imgView = (ImageView) layoutView.findViewById(R.id.brush_imageView);
			imgView.setImageResource(simpleBrush[i]);
			
			layoutSimplePattern.addView(layoutView);
			
			MarginLayoutParams p = (MarginLayoutParams) layoutView.getLayoutParams();
			p.width = 30;
			p.height = 30;
			p.rightMargin = 15;
			p.leftMargin = 15;
			
			layoutView.setLayoutParams(p);
		}
		
		if(purchased)
			btnSimplePatternPurchase.setImageResource(R.drawable.purchase_button_di);
		else {
			purchased = Utils.isPurchasedPacket(Constants.kBrushes1Package);
			
			if(purchased)
				btnSimplePatternPurchase.setImageResource(R.drawable.purchase_button_di);
			else
				btnSimplePatternPurchase.setImageResource(R.drawable.purchase_button);
		}
	}
	
	private void loadColors() {
		generateAllColors();
		
		float[] outerRadii = {5.f,5.f,5.f,5.f,5.f,5.f,5.f,5.f};
    	float[] innerRadii = {5.f,5.f,5.f,5.f,5.f,5.f,5.f,5.f};
    	
    	LayoutInflater mInflater = LayoutInflater.from(this);
		
		for(int i = 0; i < allColors.size(); i++) {
			final Integer color = allColors.get(i);
			
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
        	
        	layoutAllColor.addView(layoutView);
        	
        	MarginLayoutParams p = (MarginLayoutParams) layoutView.getLayoutParams();
			p.width = 30;
			p.height = 30;
			p.rightMargin = 15;
			p.leftMargin = 15;
			
			layoutView.setLayoutParams(p);
		}
		
		boolean purchased = Utils.isPurchasedPacket(Constants.kColorsAllPackage);
		if(purchased)
			btnAllColorPurchase.setImageResource(R.drawable.purchase_button_di);
		else
			btnAllColorPurchase.setImageResource(R.drawable.purchase_button);
		
		generatePackageColors();
		
		for(int i = 0; i < simpleColors.size(); i++) {
			final Integer color = simpleColors.get(i);
			
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
        	
        	layoutSimpleColor.addView(layoutView);
        	
        	MarginLayoutParams p = (MarginLayoutParams) layoutView.getLayoutParams();
			p.width = 30;
			p.height = 30;
			p.rightMargin = 15;
			p.leftMargin = 15;
			
			layoutView.setLayoutParams(p);
		}
		
		if(purchased)
			btnSimpleColorPurchase.setImageResource(R.drawable.purchase_button_di);
		else {
			purchased = Utils.isPurchasedPacket(Constants.kColors1Package);
			
			if(purchased)
				btnSimpleColorPurchase.setImageResource(R.drawable.purchase_button_di);
			else
				btnSimpleColorPurchase.setImageResource(R.drawable.purchase_button);
		}
	}
	
	private void generatePackageColors() {
		simpleColors = new ArrayList<Integer>();
		
		//pink
	    int color = Color.rgb(222, 33, 241);
	    simpleColors.add(Integer.valueOf(color));
	    
	    
	    //green
	    color = Color.rgb(31, 241, 58);
	    simpleColors.add(Integer.valueOf(color));
	    
	    //purple
	    color = Color.rgb(111, 25, 150);
	    simpleColors.add(Integer.valueOf(color));
	    
	    //orange
	    color = Color.rgb(255, 182, 24);
	    simpleColors.add(Integer.valueOf(color));
	    
	    //grey
	    color = Color.rgb(133, 133, 133);
	    simpleColors.add(Integer.valueOf(color));
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
						JSONObject	resp = jResponse.getJSONObject(Constants.KEY_RESPONSE);
						int coin = resp.getInt(Constants.KEY_COINS);
						
						Global.mUserData.coins = coin;
						Global.mUserData.saveUserData(AddMoreDrawItemActivity.this);
						
						for(int i = 0; i < Global.mInAppPacketsArray.size(); i++) {
							PacketItem item = Global.mInAppPacketsArray.get(i);
							
							if(item.packetId == packetId) {
								item.purchased = 1;
								break;
							}
						}
						
						if(packetId == Constants.kColorsAllPackage)
							btnAllColorPurchase.setImageResource(R.drawable.purchase_button_di);
						else if(packetId == Constants.kColors1Package)
							btnSimpleColorPurchase.setImageResource(R.drawable.purchase_button_di);
						else if(packetId == Constants.kBrushesAllPackage)
							btnAllPatternPurchase.setImageResource(R.drawable.purchase_button_di);
						else if(packetId == Constants.kBrushes1Package)
							btnSimplePatternPurchase.setImageResource(R.drawable.purchase_button_di);
						
						Dialog dialog = new Dialog(AddMoreDrawItemActivity.this);
						CustomDialog.Builder customBuilder = new CustomDialog.Builder(AddMoreDrawItemActivity.this);
						customBuilder.setTitle("");
						customBuilder.setMessage("Packet successfully purchased.");
						customBuilder.setPositiveButton("OK" ,
								new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
						
						dialog = customBuilder.create();
						dialog.setCancelable(false);
						dialog.show();
					}
					else {
						String strResp = jResponse.getString(Constants.KEY_RESPONSE);
						
						switch (code) {
						case 1:
							//Invalid validation_hash
							showConfirmDialog(AddMoreDrawItemActivity.this, "Something went wrong. Please try again.", "OK", Constants.apiErrorAlert);
							break;
						case 62:
							//new version
							showConfirmDialog(AddMoreDrawItemActivity.this, strResp, "Update", Constants.newVersionAlert);
							break;
						case 2:
							//Invalid app_id
							showConfirmDialog(AddMoreDrawItemActivity.this, "Another session with the same user name already exists. Click OK to login and disconnect the other session.", "OK", Constants.apiErrorAlert);
							break;
						case 52:
							//invalid  packet.
							showConfirmDialog(AddMoreDrawItemActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 53:
							//Packet already purchased.
							showConfirmDialog(AddMoreDrawItemActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						case 54:
							//Error purchasing packet
							showConfirmDialog(AddMoreDrawItemActivity.this, strResp, "OK", Constants.apiErrorAlert);
							break;
						default:
							Utils.showOKDialog(AddMoreDrawItemActivity.this, strResp, "OK");
							break;
						}
					}
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Utils.showOKDialog(AddMoreDrawItemActivity.this, e.getMessage(), "OK");
				}
			}
			
			@Override
			public void onFailure(java.lang.Throwable error, java.lang.String content) {
				Log.d("registerPurchase HTTP", "onFailure: " + content);
				
				if(progress.isShowing())
					progress.dismiss();
				
				Utils.showOKDialog(AddMoreDrawItemActivity.this, "A connection timeout occured. Plase try later.", "OK");
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
						Intent intent = new Intent(AddMoreDrawItemActivity.this, LoginActivity.class);
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
