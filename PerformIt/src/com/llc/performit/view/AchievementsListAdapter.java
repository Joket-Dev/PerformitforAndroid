package com.llc.performit.view;

import java.io.Console;
import java.util.ArrayList;

import com.llc.performit.CompleteAchievementActivity;
import com.llc.performit.R;
import com.llc.performit.UncompleteAchievementActivity;
import com.llc.performit.common.Constants;
import com.llc.performit.model.Achievement;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AchievementsListAdapter extends BaseAdapter {
	
	private Context						mContext;
	private ArrayList<Achievement>		mAchievementList;
	private LayoutInflater	 			mInflater;

	public AchievementsListAdapter(Context context, ArrayList<Achievement> list) {
		mContext = context;
		mAchievementList = list;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mAchievementList == null)
			return 0;
		
		int cnt = 0;
		if(mAchievementList.size() % 2 == 0)
			cnt = mAchievementList.size() / 2;
		else
			cnt = mAchievementList.size() / 2 + 1;
		
		return cnt;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mAchievementList.get(arg0);
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
			convertView = mInflater.inflate(R.layout.achievement_cell, null);

			holder = new ViewHolder();
			
			holder.l_Layout = (RelativeLayout) convertView.findViewById(R.id.left_layout);
			holder.l_tvAchieved = (TextView) convertView.findViewById(R.id.l_achieved_textView);
			holder.l_achievedImageView = (ImageView) convertView.findViewById(R.id.l_achieved_imageView);
			holder.l_tvName = (TextView) convertView.findViewById(R.id.l_name_textView);
			holder.l_typeImageView = (ImageView) convertView.findViewById(R.id.l_type_imageView1);
			
			holder.r_Layout = (RelativeLayout) convertView.findViewById(R.id.right_layout);
			holder.r_tvAchieved = (TextView) convertView.findViewById(R.id.r_achieved_textView);
			holder.r_achievedImageView = (ImageView) convertView.findViewById(R.id.r_achieved_imageView);
			holder.r_tvName = (TextView) convertView.findViewById(R.id.r_name_textView);
			holder.r_typeImageView = (ImageView) convertView.findViewById(R.id.r_type_imageView1);
			
			Typeface btnFont = Typeface.createFromAsset(mContext.getAssets(), "marvin.ttf");
			holder.l_tvAchieved.setTypeface(btnFont);
			holder.l_tvName.setTypeface(btnFont);
			holder.r_tvAchieved.setTypeface(btnFont);
			holder.r_tvName.setTypeface(btnFont);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final Achievement lItem;
//		0 1		0	position * 2, postion * 2 + 1
//		2 3 	1
//		4 5		2
		
		lItem = mAchievementList.get(position * 2);
		int typeResId = 0;
		
		if(lItem != null) {
			if(lItem.target == lItem.achieved) {
				holder.l_achievedImageView.setVisibility(View.VISIBLE);
				typeResId = getAchieveTypeImage(lItem.typeId, true);
			}
			else {
				holder.l_achievedImageView.setVisibility(View.GONE);
				typeResId = getAchieveTypeImage(lItem.typeId, false);
			}
			
			holder.l_typeImageView.setImageResource(typeResId);
			
			holder.l_tvAchieved.setText(lItem.achieved + "/" + lItem.target);
			holder.l_tvName.setText(lItem.name);
			
			holder.l_Layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					showAchievement(lItem);
				}
			});
		}
		
		final Achievement rItem;
		
		if(position * 2 + 1 < mAchievementList.size()) {
			holder.r_Layout.setVisibility(View.VISIBLE);
			
			rItem = mAchievementList.get(position * 2 + 1);
			
			if(rItem != null) {
				if(rItem.target == rItem.achieved) {
					holder.r_achievedImageView.setVisibility(View.VISIBLE);
					typeResId = getAchieveTypeImage(rItem.typeId, true);
				}
				else {
					holder.r_achievedImageView.setVisibility(View.GONE);
					typeResId = getAchieveTypeImage(rItem.typeId, false);
				}
				
				holder.r_typeImageView.setImageResource(typeResId);
				
				holder.r_tvAchieved.setText(rItem.achieved + "/" + rItem.target);
				holder.r_tvName.setText(rItem.name);
				
				holder.r_Layout.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						showAchievement(rItem);
					}
				});
			}
		}
		else
			holder.r_Layout.setVisibility(View.INVISIBLE);
		
		return convertView;
	}
	
	private int getAchieveTypeImage(int type, boolean isAchieved) {
		int resId = 0;
		
		switch (type) {
		case 1:
			if(isAchieved)
				resId = R.drawable.achievement_1;
			else
				resId = R.drawable.achievement_1_uncompleted;
			break;
		case 2:
			if(isAchieved)
				resId = R.drawable.achievement_2;
			else
				resId = R.drawable.achievement_2_uncompleted;
			break;
		case 3:
			if(isAchieved)
				resId = R.drawable.achievement_3;
			else
				resId = R.drawable.achievement_3_uncompleted;
			break;
		case 4:
			if(isAchieved)
				resId = R.drawable.achievement_4;
			else
				resId = R.drawable.achievement_4_uncompleted;
			break;
		case 5:
			if(isAchieved)
				resId = R.drawable.achievement_5;
			else
				resId = R.drawable.achievement_5_uncompleted;
			break;
		case 6:
			if(isAchieved)
				resId = R.drawable.achievement_6;
			else
				resId = R.drawable.achievement_6_uncompleted;
			break;
		case 7:
			if(isAchieved)
				resId = R.drawable.achievement_7;
			else
				resId = R.drawable.achievement_7_uncompleted;
			break;
		case 8:
			if(isAchieved)
				resId = R.drawable.achievement_8;
			else
				resId = R.drawable.achievement_8_uncompleted;
			break;
		case 9:
			if(isAchieved)
				resId = R.drawable.achievement_9;
			else
				resId = R.drawable.achievement_9_uncompleted;
			break;
		case 10:
			if(isAchieved)
				resId = R.drawable.achievement_10;
			else
				resId = R.drawable.achievement_10_uncompleted;
			break;
		case 11:
			if(isAchieved)
				resId = R.drawable.achievement_11;
			else
				resId = R.drawable.achievement_11_uncompleted;
			break;
		case 12:
			if(isAchieved)
				resId = R.drawable.achievement_12;
			else
				resId = R.drawable.achievement_12_uncompleted;
			break;
		case 13:
			if(isAchieved)
				resId = R.drawable.achievement_13;
			else
				resId = R.drawable.achievement_13_uncompleted;
			break;
		case 14:
			if(isAchieved)
				resId = R.drawable.achievement_14;
			else
				resId = R.drawable.achievement_14_uncompleted;
			break;
		case 15:
			if(isAchieved)
				resId = R.drawable.achievement_15;
			else
				resId = R.drawable.achievement_15_uncompleted;
			break;
		case 16:
			if(isAchieved)
				resId = R.drawable.achievement_16;
			else
				resId = R.drawable.achievement_16_uncompleted;
			break;
		case 17:
			if(isAchieved)
				resId = R.drawable.achievement_17;
			else
				resId = R.drawable.achievement_17_uncompleted;
			break;
		case 18:
			if(isAchieved)
				resId = R.drawable.achievement_18;
			else
				resId = R.drawable.achievement_18_uncompleted;
			break;
		default:
			break;
		}
		
		return resId;
	}
	
	private void showAchievement(Achievement item) {
		Intent intent;
		
		if(item.achieved == item.target) {
			intent = new Intent(mContext, CompleteAchievementActivity.class);
			
			intent.putExtra(Constants.KEY_NAME, item.name);
			intent.putExtra(Constants.KEY_ACHIEVEMENT_TYPE_ID, item.typeId);
			intent.putExtra(Constants.KEY_COINS, item.coins);
			intent.putExtra(Constants.KEY_DESCRIPTION, item.description);
			
			mContext.startActivity(intent);
		}
		else {
			intent = new Intent(mContext, UncompleteAchievementActivity.class);
			
			intent.putExtra(Constants.KEY_NAME, item.name);
			intent.putExtra(Constants.KEY_ACHIEVEMENT_TYPE_ID, item.typeId);
			intent.putExtra(Constants.KEY_COINS, item.coins);
			intent.putExtra(Constants.KEY_PROGRESS, item.achieved + "/" + item.target);
			intent.putExtra(Constants.KEY_DESCRIPTION, item.description);
			
			mContext.startActivity(intent);
		}
	}

	private class ViewHolder {
		public RelativeLayout	l_Layout;
		public TextView			l_tvAchieved;
		public ImageView		l_achievedImageView;
		public TextView			l_tvName;
		public ImageView		l_typeImageView;
		
		public RelativeLayout	r_Layout;
		public TextView			r_tvAchieved;
		public ImageView		r_achievedImageView;
		public TextView			r_tvName;
		public ImageView		r_typeImageView;
	}
}
