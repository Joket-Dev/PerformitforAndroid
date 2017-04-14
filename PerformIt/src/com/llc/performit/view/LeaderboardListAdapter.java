package com.llc.performit.view;

import java.util.ArrayList;

import com.llc.performit.R;
import com.llc.performit.model.LeaderboardItem;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class LeaderboardListAdapter extends BaseAdapter {
	
	private Context						mContext;
	private ArrayList<LeaderboardItem>	mLeaderboardList;
	private LayoutInflater	 			mInflater;

	public LeaderboardListAdapter(Context context, ArrayList<LeaderboardItem> list) {
		mContext = context;
		mLeaderboardList = list;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mLeaderboardList == null)
			return 0;
		
		return mLeaderboardList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
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
			convertView = mInflater.inflate(R.layout.leaderboard_cell, null);

			holder = new ViewHolder();
			
			holder.tvName = (TextView) convertView.findViewById(R.id.name_textView);
			holder.tvScore = (TextView) convertView.findViewById(R.id.score_textView);
			holder.tvScoreLabel = (TextView) convertView.findViewById(R.id.score_label_textView);
			holder.pictureImageView = (ImageView) convertView.findViewById(R.id.picture_imageView);
			
			Typeface btnFont = Typeface.createFromAsset(mContext.getAssets(), "marvin.ttf");
			holder.tvName.setTypeface(btnFont);
			holder.tvScore.setTypeface(btnFont);
			holder.tvScoreLabel.setTypeface(btnFont);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		LeaderboardItem item = mLeaderboardList.get(position);
		if(item != null) {
			holder.tvName.setText(item.name);
			holder.tvScore.setText(item.score + "");
		}
			
		return convertView;
	}

	private class ViewHolder {
		public TextView		tvName;
		public TextView		tvScoreLabel;
		public TextView		tvScore;
		public ImageView	pictureImageView;
	}
}
