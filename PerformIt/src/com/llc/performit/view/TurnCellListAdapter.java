package com.llc.performit.view;

import java.util.ArrayList;

import com.llc.performit.GamePlayActivity;
import com.llc.performit.R;
import com.llc.performit.common.Constants;
import com.llc.performit.model.CompleteGame;
import com.llc.performit.model.MyTurn;
import com.llc.performit.model.TheirTurn;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView; 

public class TurnCellListAdapter extends BaseAdapter {

	private Context					mContext;
	private ArrayList<Object>		mTurnList;
	private LayoutInflater	 		mInflater;
	
	public TurnCellListAdapter(Context context, ArrayList<Object> list) {
		mContext = context;
		mTurnList = list;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mTurnList == null)
			return 0;
		
		return mTurnList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mTurnList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.myturn_cell, null);

			holder = new ViewHolder();
			
			holder.tvCategory = (TextView) convertView.findViewById(R.id.category_textView);
			holder.tvOppentname = (TextView) convertView.findViewById(R.id.name_textView);
			holder.tvRound = (TextView) convertView.findViewById(R.id.round_textView);
			holder.arrowImageView = (ImageView) convertView.findViewById(R.id.arrow_imageView);
			holder.infoLayout = (RelativeLayout) convertView.findViewById(R.id.info_layout);
			holder.pictureImageView = (ImageView) convertView.findViewById(R.id.picture_imageView);
			
			Typeface btnFont = Typeface.createFromAsset(mContext.getAssets(), "marvin.ttf");
			holder.tvCategory.setTypeface(btnFont);
//			holder.tvOppentname.setTypeface(btnFont);
			holder.tvRound.setTypeface(btnFont);

			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Object item = mTurnList.get(position);
		if(item.getClass().getName().equalsIgnoreCase(MyTurn.class.getName())) {
			final MyTurn myTurn = (MyTurn)mTurnList.get(position);
			
			if(myTurn.name == "" || myTurn.gameRoundId == 0) {
				holder.tvOppentname.setText("No games in this category");
				holder.pictureImageView.setVisibility(View.GONE);
				holder.tvRound.setVisibility(View.GONE);
				holder.arrowImageView.setVisibility(View.GONE);
				
				holder.tvOppentname.setGravity(Gravity.CENTER);
				
				RelativeLayout.LayoutParams p = (LayoutParams) holder.tvOppentname.getLayoutParams();
				p.width = LayoutParams.FILL_PARENT;
				holder.tvOppentname.setLayoutParams(p);
			}
			else {
				holder.tvOppentname.setText(myTurn.name);
				holder.tvRound.setText(myTurn.winCount + "");
				
//				holder.infoLayout.setOnClickListener(new OnClickListener() {
//					
//					@Override
//					public void onClick(View v) {
//						// TODO Auto-generated method stub
//						Intent intent = new Intent(mContext, GamePlayActivity.class);
//						intent.putExtra(Constants.KEY_TYPE, myTurn.type);
//						mContext.startActivity(intent);
//					}
//				});
			}
			
			if(myTurn.isCatgoryFirst) {
				holder.tvCategory.setVisibility(View.VISIBLE);
				holder.tvCategory.setText("YOUR TURN");
			}
			else
				holder.tvCategory.setVisibility(View.GONE);
		}
		else if(item.getClass().getName().equalsIgnoreCase(TheirTurn.class.getName())) {
			TheirTurn theirTurn = (TheirTurn) mTurnList.get(position);
			
			if(theirTurn.name == "") {
				holder.tvOppentname.setText("No games in this category");
				holder.pictureImageView.setVisibility(View.GONE);
				holder.tvRound.setVisibility(View.GONE);
				holder.arrowImageView.setVisibility(View.GONE);
				holder.tvOppentname.setGravity(Gravity.CENTER);
				
				RelativeLayout.LayoutParams p = (LayoutParams) holder.tvOppentname.getLayoutParams();
				p.width = LayoutParams.FILL_PARENT;
				holder.tvOppentname.setLayoutParams(p);
			}
			else {
				holder.tvOppentname.setText(theirTurn.name);
				holder.tvRound.setText(theirTurn.winCount + "");
			}
			
			if(theirTurn.isCatgoryFirst) {
				holder.tvCategory.setVisibility(View.VISIBLE);
				holder.tvCategory.setText("THEIR TURN");
			}
			else
				holder.tvCategory.setVisibility(View.GONE);
				
			holder.arrowImageView.setVisibility(View.INVISIBLE);
		}
		else if(item.getClass().getName().equalsIgnoreCase(CompleteGame.class.getName())) {
			CompleteGame game = (CompleteGame) mTurnList.get(position);
			
			if(game.name == "") {
				holder.tvOppentname.setText("No games in this category");
				holder.pictureImageView.setVisibility(View.GONE);
				holder.tvRound.setVisibility(View.GONE);
				holder.arrowImageView.setVisibility(View.GONE);
				holder.tvOppentname.setGravity(Gravity.CENTER);
				
				RelativeLayout.LayoutParams p = (LayoutParams) holder.tvOppentname.getLayoutParams();
				p.width = LayoutParams.FILL_PARENT;
				holder.tvOppentname.setLayoutParams(p);
			}
			else {
				holder.tvOppentname.setText(game.name);
				holder.tvRound.setText(game.winCount + "");
			}
			
			if(game.isCatgoryFirst) {
				holder.tvCategory.setVisibility(View.VISIBLE);
				holder.tvCategory.setText("COMPLETED GAMES");
			}
			else {
				holder.tvCategory.setVisibility(View.GONE);
			}
				
			holder.arrowImageView.setVisibility(View.INVISIBLE);
		}
		
		return convertView;
	}

	class ViewHolder {
		public TextView		tvCategory;
		public TextView		tvOppentname;
		public TextView		tvRound;
		public ImageView	arrowImageView;
		public RelativeLayout	infoLayout;
		public ImageView	pictureImageView;
	}
}
