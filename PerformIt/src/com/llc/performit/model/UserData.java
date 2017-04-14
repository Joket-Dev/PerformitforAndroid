package com.llc.performit.model;

import android.content.Context;

import com.llc.performit.common.Constants;
import com.llc.performit.common.Global;
import com.llc.performit.common.Utils;

public class UserData {
	public String	username;
	public String	password;
	public String	email;
	public String 	name;
	public String	facebookId;
	public String	facebookAccessToken;
	public String	deviceID;
	public int		bubbles;
	public int		coins;
	public boolean	soundsEnabled;
	public boolean	validLogin;
	public String	token;
	public String	loginType;
	public int		userID;
	public String	firstname;
	public String	lastname;
	
	public void saveUserData(Context context) {
		Utils.setPrefString(context, Constants.KEY_USERNAME, username);
		Utils.setPrefString(context, Constants.KEY_PASSWORD, password);
		Utils.setPrefString(context, Constants.KEY_EMAIL, email);
		Utils.setPrefString(context, Constants.KEY_NAME, name);
		Utils.setPrefString(context, Constants.KEY_FACEBOOK_ID, facebookId);
		Utils.setPrefString(context, Constants.KEY_FACEBOOK_ACCESS_TOKEN, facebookAccessToken);
		Utils.setPrefString(context, Constants.KEY_DEVICE_ID, deviceID);
		Utils.setPrefInt(context, Constants.KEY_BUBBLES, bubbles);
		Utils.setPrefInt(context, Constants.KEY_COINS, coins);
		Utils.setPrefBool(context, Constants.KEY_SOUNDS_ENABLED, soundsEnabled);
		Utils.setPrefBool(context, Constants.KEY_VALID_LOGIN, validLogin);
		Utils.setPrefString(context, Constants.KEY_TOKEN, token);
		Utils.setPrefString(context, Constants.KEY_LOGIN_TYPE, loginType);
		Utils.setPrefInt(context, Constants.KEY_ID, userID);
		Utils.setPrefString(context, Constants.KEY_FIRSTNAME, firstname);
		Utils.setPrefString(context, Constants.KEY_LASTNAME, lastname);
	}
	
	public void loadUserData(Context context) {
		username = Utils.getPrefString(context, Constants.KEY_USERNAME);
		password = Utils.getPrefString(context, Constants.KEY_PASSWORD);
		email = Utils.getPrefString(context, Constants.KEY_EMAIL);
		name = Utils.getPrefString(context, Constants.KEY_NAME);
		facebookId = Utils.getPrefString(context, Constants.KEY_FACEBOOK_ID);
		facebookAccessToken = Utils.getPrefString(context, Constants.KEY_FACEBOOK_ACCESS_TOKEN);
		deviceID = Utils.getPrefString(context, Constants.KEY_DEVICE_ID);
		bubbles = Utils.getPreInt(context, Constants.KEY_BUBBLES);
		coins = Utils.getPreInt(context, Constants.KEY_COINS);
		soundsEnabled = Utils.getPreBool(context, Constants.KEY_SOUNDS_ENABLED);
		validLogin = Utils.getPreBool(context, Constants.KEY_VALID_LOGIN);
		token = Utils.getPrefString(context, Constants.KEY_TOKEN);
		loginType = Utils.getPrefString(context, Constants.KEY_LOGIN_TYPE);
		userID = Utils.getPreInt(context, Constants.KEY_ID);
		firstname = Utils.getPrefString(context, Constants.KEY_FIRSTNAME);
		lastname = Utils.getPrefString(context, Constants.KEY_LASTNAME);
	}
	
}
