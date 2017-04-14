package com.llc.performit.common;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import com.llc.performit.model.PacketItem;
import com.llc.performit.model.UserData;
import com.llc.performit.model.WordItem;
import com.llc.performit.view.CustomDialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.FileProvider;
import android.telephony.TelephonyManager;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

public class Utils {

	public final static boolean isValidEmail(CharSequence target) {
	    if (target == null) {
	        return false;
	    } else {
	        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
	    }
	}
	
	public static void setPrefString(Context context, String key, String value) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putString(key, value);
		editor.commit();
	}
	
	public static String getPrefString(Context context, String key) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		
		String value = pref.getString(key, "");
		
		return value;
	}
	
	public static void setPrefInt(Context context, String key, int value) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putInt(key, value);
		editor.commit();
	}
	
	public static int getPreInt(Context context, String key) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		
		int value = pref.getInt(key, 0);
		
		return value;
	}
	
	public static void setPrefBool(Context context, String key, boolean value) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		
		SharedPreferences.Editor editor = pref.edit();
		
		editor.putBoolean(key, value);
		editor.commit();
	}
	
	public static boolean getPreBool(Context context, String key) {
		SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
		
		boolean value = pref.getBoolean(key, false);
		
		return value;
	}
	
	public static UserData loadUserInfo(Context context) {
		UserData userData = new UserData();
		
		if(!Utils.getPreBool(context, Constants.KEY_VALID_LOGIN))
			return null;
		
		userData.username = Utils.getPrefString(context, Constants.KEY_USERNAME);
		userData.password = Utils.getPrefString(context, Constants.KEY_PASSWORD);
		userData.email = Utils.getPrefString(context, Constants.KEY_EMAIL);
		userData.name = Utils.getPrefString(context, Constants.KEY_NAME);
		userData.facebookId = Utils.getPrefString(context, Constants.KEY_FACEBOOK_ID);
		userData.facebookAccessToken = Utils.getPrefString(context, Constants.KEY_FACEBOOK_ACCESS_TOKEN);
		userData.coins = Utils.getPreInt(context, Constants.KEY_COINS);
		userData.bubbles = Utils.getPreInt(context, Constants.KEY_BUBBLES);
		userData.soundsEnabled = Utils.getPreBool(context, Constants.KEY_SOUNDS_ENABLED);
		userData.deviceID = Utils.getPrefString(context, Constants.KEY_DEVICE_ID);
		userData.token = Utils.getPrefString(context, Constants.KEY_TOKEN);
		userData.loginType = Utils.getPrefString(context, Constants.KEY_LOGIN_TYPE);
		userData.userID = Utils.getPreInt(context, Constants.KEY_ID);
		
		return userData;
	}
	
	public static final AlertDialog.Builder createAlertDialg(Context context, String title, String message) {
		return new AlertDialog.Builder(context)
		.setTitle(title)
		.setMessage(message)
		.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) { 
				// continue with delete
			}
		});
	}
	
	public static void showOKDialog(Context context, String message, String okButton) {
		Dialog dialog = new Dialog(context);
		CustomDialog.Builder customBuilder = new CustomDialog.Builder(context);
		customBuilder.setTitle("");
		customBuilder.setMessage(message);
		customBuilder.setNegativeButton(okButton, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		
		dialog = customBuilder.create();
		dialog.setCancelable(false);
		
		dialog.show();
	}
	
	public static final String getMediaSavePath() {
		String path = Environment.getExternalStorageDirectory().getPath();
		File dir = new File(Environment.getExternalStorageDirectory(),"PerformIt" + "/");
		
		if(!dir.exists())
			dir.mkdirs();
		
		path = dir.getAbsolutePath();
		
		return path;
	}
	
	public static final String getTempFileName() {
		String name = "";
		
		Date d = new Date();
		long curTime = d.getTime();
		
		name = curTime + "";
		
		return name;
	}
	
	public static String randomAlphanumericStringWithLength(int length) {
		String letters = "abcdefghijklmnopqrstuvwxyz";
		String str = "";
		
		Random r = new Random();
		int idx = 0;
		
		for(int i = 0; i < 14 - length; i++) {
			idx = Math.abs(r.nextInt() % letters.length());
			str = str + letters.charAt(idx);
		}
		
		return str;
	}
	
	public static String randominzeString(String str) {
		String randomStr = "";
		ArrayList<String> idxList = new ArrayList<String>();
		
		Random r = new Random();
		int idx = 0;
		
		for(int i = 0; i < str.length(); i++) {
			String s = str.charAt(i) + "";
			idxList.add(s);
		}
		
		while(idxList.size() > 0) {
			idx = Math.abs(r.nextInt() % idxList.size());
			
			randomStr = randomStr + idxList.get(idx);
			idxList.remove(idx);
		}
		
		return randomStr;
	}
	
	public static String getTimeStr(long time) {
		String timeStr = "";
		int min = 0, sec = 0;
		
		time = time / 1000;
		min = (int) (time / 60);
		sec = (int) (time % 60);
		
		String minStr, secStr;
		
		if(min < 10)
			minStr = "0" + min;
		else
			minStr = "" + min;
		
		if(sec < 10)
			secStr = "0" + sec;
		else
			secStr = "" + sec;
		
		timeStr = String.format("%s:%s", minStr, secStr);
		
		return timeStr;
	}
	
	public static String sha1(String str) {
		try {
			byte[] abKey = str.getBytes("US-ASCII");
			
			MessageDigest oSHA1 = MessageDigest.getInstance("SHA-1");
			abKey = oSHA1.digest(abKey);

			String output = "";
			
			for(int i = 0; i < abKey.length; i++) {
				output = output + String.format("%02X", abKey[i]);
			}
			
//			output = new String(abKey);
			return output;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return "";
	}
	
	public static String makeSecretKey(String requestParams) {
		String[] keyArray = requestParams.split("&");
		ArrayList<String> last = new ArrayList<String>();
		
		for(int i = 0; i < keyArray.length; i++) {
			String hard = keyArray[i];
//			String newText = hard.replace("=", "*");
			
//			String[] keyVal = newText.split("*");
			String[] keyVal = hard.split("=");
			if (keyVal.length > 0) {
				String variabl = keyVal[0] + Constants.k_SecretKey;
				
				if(keyVal.length > 1)
					last.add(variabl + keyVal[1]);
				else
					last.add(variabl + "");
			}
		}
		
		String output = "";
		
		for(int i = 0; i < last.size(); i++) {
			output += last.get(i);
		}
		
		
		output = Utils.sha1(output);
		
		return output;
	}
	
	public static String getDeviceId(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService("phone");
		String imei = tm.getDeviceId();
		
		return imei;
	}
	
	public static String getVersion() {
		return "1.000000";
	}
	
	public static WordItem parseWord(JSONObject jsonWord) {
		if(jsonWord == null)
			return null;
		
		WordItem word = new WordItem();
		
		try {
			word.coins = jsonWord.getInt(Constants.KEY_COINS);
			word.completed = jsonWord.getInt(Constants.KEY_COMPLETED);
			word.enabled = jsonWord.getInt(Constants.KEY_ENABLED);
			word.gameId = jsonWord.getInt(Constants.KEY_ID);
			word.myTurn = jsonWord.getString(Constants.KEY_MY_TURN);
			word.theirTurn = jsonWord.getString(Constants.KEY_THEIR_TURN);
			word.time = jsonWord.getInt(Constants.KEY_TIME);
			word.type = jsonWord.getString(Constants.KEY_TYPE);
			word.word = jsonWord.getString(Constants.KEY_WORD);
			word.id = jsonWord.getInt(Constants.KEY_ID);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		return word;
	}
	
	@SuppressWarnings("resource")
	public static String base64Encoding(String path) {
		FileInputStream in;
		
		try {
			in = new FileInputStream(path);
			int size = in.available();
			byte[] buffer = new byte[size];
			
			in.read(buffer);
			
			String encodedStr = Base64.encodeToString(buffer, Base64.DEFAULT);
			
			// for test
			byte[] a = Base64.decode(encodedStr,  Base64.DEFAULT);
			int i;
			i = 0;
			i++;
			//
			
			return encodedStr;
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		return null;
	}
	
	public static String base64Decoding(String data, String extension) {
//		byte[] buf = Base64.decode(data, Base64.DEFAULT);
		byte[] inBuf = null;
		try {
			inBuf = data.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		byte[] buf = Base64.decode(inBuf, Base64.DEFAULT);
		
		String filePath = Utils.getMediaSavePath() + "/" + Utils.getTempFileName() + "." + extension;

		File file = new File(filePath);
		OutputStream out = null;

		try {
			out = new BufferedOutputStream(new FileOutputStream(file));
			out.write(buf);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		return filePath;
	}
	
//	public static void base64DataFromString(String string)
//	{
//	    int ixtext, lentext;
////	    unsigned char ch, inbuf[4], outbuf[4];
//	    char ch;
//	    char[] inbuf = new char[4];
//	    char[] outbuf = new char[4];
//	    short i, ixinbuf;
//	    Boolean flignore, flendtext = false;
//	    String tempcstring;
//	    
//	    ixtext = 0;
//	    
////	    tempcstring = (const unsigned char *)[string UTF8String];
//	    tempcstring = string;
//	    
//	    lentext = string.length();
//	    
//	    byte[] buf = new byte[lentext];
//	    
//	    ixinbuf = 0;
//	    
//	    while (true)
//	    {
//	        if (ixtext >= lentext)
//	        {
//	            break;
//	        }
//	        
//	        ch = tempcstring.charAt(ixtext++);
//	        
//	        flignore = false;
//	        
//	        if ((ch >= 'A') && (ch <= 'Z'))
//	        {
//	            ch = (char) (ch - 'A');
//	        }
//	        else if ((ch >= 'a') && (ch <= 'z'))
//	        {
//	            ch = (char) (ch - 'a' + 26);
//	        }
//	        else if ((ch >= '0') && (ch <= '9'))
//	        {
//	            ch = (char) (ch - '0' + 52);
//	        }
//	        else if (ch == '+')
//	        {
//	            ch = 62;
//	        }
//	        else if (ch == '=')
//	        {
//	            flendtext = true;
//	        }
//	        else if (ch == '/')
//	        {
//	            ch = 63;
//	        }
//	        else
//	        {
//	            flignore = true;
//	        }
//	        
//	        if (!flignore)
//	        {
//	            short ctcharsinbuf = 3;
//	            Boolean flbreak = false;
//	            
//	            if (flendtext)
//	            {
//	                if (ixinbuf == 0)
//	                {
//	                    break;
//	                }
//	                
//	                if ((ixinbuf == 1) || (ixinbuf == 2))
//	                {
//	                    ctcharsinbuf = 1;
//	                }
//	                else
//	                {
//	                    ctcharsinbuf = 2;
//	                }
//	                
//	                ixinbuf = 3;
//	                
//	                flbreak = true;
//	            }
//	            
//	            inbuf [ixinbuf++] = ch;
//	            
//	            if (ixinbuf == 4)
//	            {
//	                ixinbuf = 0;
//	                
//	                outbuf[0] = (char) ((inbuf[0] << 2) | ((inbuf[1] & 0x30) >> 4));
//	                outbuf[1] = (char) (((inbuf[1] & 0x0F) << 4) | ((inbuf[2] & 0x3C) >> 2));
//	                outbuf[2] = (char) (((inbuf[2] & 0x03) << 6) | (inbuf[3] & 0x3F));
//	                
//	                for (i = 0; i < ctcharsinbuf; i++)
//	                {
//	                    [theData appendBytes: &outbuf[i] length: 1];
//	                }
//	            }
//	            
//	            if (flbreak)
//	            {
//	                break;
//	            }
//	        }
//	    }
//	}
	
	public static boolean isPurchasedPacket(int packetId) {
		if(Global.mInAppPacketsArray == null)
			return false;
		
		boolean flag = false;
		
		for(int i = 0; i < Global.mInAppPacketsArray.size(); i++) {
			PacketItem item = Global.mInAppPacketsArray.get(i);
			
			if(item.packetId == packetId) {
				if(item.purchased == 1)
					flag = true;
				else
					flag = false;
				
				break;
			}
		}
		
		return flag;
	}
	
	public static int convertPxToDp(Context context, int px) {
        WindowManager wm = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        float logicalDensity = metrics.density;
        int dp = Math.round(px / logicalDensity);
        return dp;
    }
}
