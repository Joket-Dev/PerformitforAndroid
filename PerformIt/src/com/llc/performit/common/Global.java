package com.llc.performit.common;

import java.util.ArrayList;

import android.content.ServiceConnection;

import com.android.vending.billing.IInAppBillingService;
import com.llc.performit.draw.drawObjectArray;
import com.llc.performit.model.GameItem;
import com.llc.performit.model.PacketItem;
import com.llc.performit.model.UserData;
import com.llc.performit.model.WordItem;

public class Global {
	
	public static UserData						mUserData;
	
	public static WordItem						mAudioWord;
	public static WordItem						mImageWord;
	public static WordItem						mVideoWord;
	
	public static GameItem						mCurGame;
	
	public static ArrayList<PacketItem>			mInAppPacketsArray;
	
	public static ArrayList<drawObjectArray>	drawObject;
	public static String						audioPath;
	public static String						videoPath;

	public static IInAppBillingService 			mService;
	public static ServiceConnection 			mServiceConn;
}
