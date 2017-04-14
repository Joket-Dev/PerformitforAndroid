package com.llc.performit;

import com.llc.performit.view.ColorSeekBar;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class SoundEffectsActivity extends Activity {
	
	private TextView	tvName1;
	private TextView	tvName2;
	private LinearLayout	closeLayout;
	private ColorSeekBar		seekBar1;
	private ColorSeekBar		seekBar2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sound_effects);
		
		tvName1 = (TextView) findViewById(R.id.name1_textView);
		tvName2 = (TextView) findViewById(R.id.name2_textView);
		closeLayout = (LinearLayout) findViewById(R.id.close_layout);
		seekBar1 = (ColorSeekBar) findViewById(R.id.seekBar1);
		seekBar2 = (ColorSeekBar) findViewById(R.id.seekBar2);
		
		Typeface btnFont = Typeface.createFromAsset(getAssets(), "marvin.ttf");
		tvName1.setTypeface(btnFont);
		tvName2.setTypeface(btnFont);
		
//		seekBar1.setProgress(50);
//		seekBar2.setProgress(50);
		
		initListener();
	}
	
	private void initListener() {
		closeLayout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
}
