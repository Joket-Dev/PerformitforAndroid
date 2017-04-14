package com.llc.performit.view;

import java.util.ArrayList;

import com.llc.performit.R;
import com.llc.performit.model.Coin;
import com.llc.performit.model.LeaderboardItem;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CoinsListAdapter extends BaseAdapter {

	private Context						mContext;
	private ArrayList<Coin>				mCoinsList;
	private LayoutInflater	 			mInflater;

	public CoinsListAdapter(Context context, ArrayList<Coin> list) {
		mContext = context;
		mCoinsList = list;
		mInflater = LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(mCoinsList == null)
			return 0;
		
		return mCoinsList.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.coin_cell, null);

			holder = new ViewHolder();
			
			holder.tvCoins = (TextView) convertView.findViewById(R.id.coins_textView);
			holder.tvPrice = (TextView) convertView.findViewById(R.id.price_textView);
			holder.tvPriceLabel = (TextView) convertView.findViewById(R.id.price_label_textView);
			holder.pictureImageView = (ImageView) convertView.findViewById(R.id.picture_imageView);
			
			Typeface btnFont = Typeface.createFromAsset(mContext.getAssets(), "marvin.ttf");
			holder.tvCoins.setTypeface(btnFont);
			holder.tvPrice.setTypeface(btnFont);
			holder.tvPriceLabel.setTypeface(btnFont);
			
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Coin item = mCoinsList.get(position);
		if(item != null) {
			holder.tvCoins.setText(item.coins + "");
			holder.tvPrice.setText(item.price);
			
			if(item.coins <= 100)
				holder.pictureImageView.setImageResource(R.drawable.coins_categ_min);
			else if(item.coins <= 200)
				holder.pictureImageView.setImageResource(R.drawable.coins_categ_med);
			else if(item.coins <= 300)
				holder.pictureImageView.setImageResource(R.drawable.coins_categ_max);
		}
			
		return convertView;
	}

	private class ViewHolder {
		public TextView		tvCoins;
		public TextView		tvPriceLabel;
		public TextView		tvPrice;
		public ImageView	pictureImageView;
	}
}
