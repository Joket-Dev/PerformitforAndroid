package com.llc.performit.common;

public class Constants {
//	public static final String	BASE_URL = "http://10.30.65.25/SocialGame/index.php/api";
	public static final String	BASE_URL = "http://performitapp.com/api";
	public static final String	SHARE_BASE_URL = "http://188.26.20.143/socialgame";
	
	public static final String	KEY_SUCCESS = "success";
	public static final String	KEY_RESPONSE = "response";
	public static final String	KEY_VALID_LOGIN = "valid_login";
	public static final String	KEY_USERNAME = "username";
	public static final String	KEY_PASSWORD = "password";
	public static final String	KEY_EMAIL = "email";
	public static final String	KEY_NAME = "name";
	public static final String	KEY_FACEBOOK_ID = "facebook_id";
	public static final String	KEY_FACEBOOK_ACCESS_TOKEN = "facebook_acces_token";
	public static final String	KEY_COINS = "coins";
	public static final String	KEY_BUBBLES = "bubbles";
	public static final String	KEY_SOUNDS_ENABLED = "sounds_enabled";
	public static final String	KEY_DEVICE_ID = "device_id";
	public static final String	KEY_TOKEN = "token";
	public static final String	KEY_LOGIN_TYPE = "type";
	public static final String	KEY_ID = "id";
	public static final String	KEY_CODE = "code";
	public static final String	KEY_COMPLETED = "completed";
	public static final String	KEY_ENABLED = "enabled";
	public static final String	KEY_USER_ID = "user_id";
	public static final String	KEY_MY_TURN = "my_turn";
	public static final String	KEY_THEIR_TURN = "their_turn";
	public static final String	KEY_TIME = "time";
	public static final String	KEY_WORD = "word";
	public static final String	KEY_TYPE = "type";
	public static final String	KEY_AUDIO = "audio_word";
	public static final String	KEY_IMAGE = "image_word";
	public static final String	KEY_VIDEO = "video_word";
	public static final String	KEY_GAME_ROUND_ID = "game_round_id";
	public static final String	KEY_GAME_ID = "game_id";
	public static final String	KEY_CONTINUE_GAME = "continue_game";
	public static final String	KEY_OPPONENT_ID = "opponent_id";
	public static final String	KEY_PICTURE = "picture";
	public static final String	KEY_FIRSTNAME = "firstname";
	public static final String	KEY_LASTNAME = "lastname";
	public static final String	KEY_PURCHASED = "purchased";
	public static final String	KEY_PACKET_ID = "packet_id";
	public static final String	KEY_MY_ACTION = "my_action";
	public static final String	KEY_WIN_COUNT = "win_count";
	public static final String	KEY_WORD_ID = "word_id";
	public static final String	KEY_PLAYER = "player";
	public static final String	KEY_DATA = "data";
	
	public static final String	KEY_ACHIEVEMENT_TYPE_ID = "achievement_type_id";
	public static final String	KEY_ACHIEVE = "achieved";
	public static final String	KEY_TARGET = "target";
	public static final String	KEY_DESCRIPTION = "description";
	public static final String	KEY_PROGRESS = "progress";
	public static final String	KEY_ACHIEVEMENT_ENTRY_ID = "achievement_entry_id";
		
	
	public static final int		GAME_TYPE_IMAGE = 0;
	public static final int		GAME_TYPE_AUDIO = 1;
	public static final int		GAME_TYPE_VIDEO = 2;
	
	public static final String	k_SecretKey = "df02#8sd(_@sdfkjh";
	public static final int 	CONNECTION_TIMEOUT = 20000;
	
	
	//packages
	public static final int		kColors1Package = 3;
	public static final int		kColorsAllPackage = 4;
	public static final int		kBrushes1Package = 2;
	public static final int		kBrushesAllPackage = 1;
	public static final int		kBackgroundColorPackage = 6;

	public static final int		kTimer2minPackage = 5;
	public static final int		kTimer5minPackage = 7;
	public static final int		kTimerInfinitePackage = 8;
	public static final int		kTimerStopPackage = 9;
	public static final int		kHintPackage = 10;

	public static final int		k15BubblesPackage = 11;
	public static final int		k30BubblesPackage = 12;
	
	public static final String	ACTION_SEND_DATA = "send_data";
	public static final String	ACTION_FINISH_GAME = "finish_game";
	public static final String	ACTION_CONTINUE_GAME = "continue_game";
	
	public static final String	TYPE_IMAGE = "image";
	public static final String	TYPE_AUDIO = "audio";
	public static final String	TYPE_VIDEO = "video";
	
	//alerts tag
	public static final int noInternetAlert = 0;
	public static final int timeoutAlert = 1;
	public static final int loginIncompleteInfoAlert = 2;
	public static final int registerIncompleteInfoAlert = 3;
	public static final int forgotPassIncompleteInfoAlert = 4;
	public static final int loginErrorAlert = 5;
	public static final int loginSuccessAlert = 6;
	public static final int registerErrorAlert = 7;
	public static final int registerSuccessAlert = 8;
	public static final int forgotPassErrorAlert = 9;
	public static final int forgotPassSuccessAlert = 10;

	public static final int saveSettingsBeforeCloseAlert = 11;
	public static final int logoutAlert = 12;

	public static final int emailInviteIncompleteAlert = 13;
	public static final int usernameInviteIncompleteAlert = 14;

	public static final int audioRecordFailAlert = 15;
	public static final int noRecordToSendAlert = 16;
	public static final int noRecordToSaveAlert = 17;
	public static final int noRecordToShareAlert = 18;
	public static final int recordingSavingErrorAlert = 19;
	public static final int recordingSavingSuccesfullAlert = 20;
	public static final int invalidUsernamePasswordAlert = 21;
	public static final int usernameNotAvailableAlert = 22;
	public static final int invalidEmailAlert = 23;
	public static final int facebookConnectErrorAlert = 24;
	public static final int userIsMissingAlert = 25;
	public static final int noNameAlert = 26;
	public static final int saveSettingsAlert = 27;
	public static final int apiErrorAlert = 28;
	public static final int facebookRetryAlert = 29;
	public static final int noFriendPlayingAlert = 30;
	public static final int opponentDoesntExistsAlert = 31;
	public static final int wordDoesntExistsAlert = 32;
	public static final int invalidAppIDAlert = 33;
	public static final int usedUsernameAlert = 34;
	public static final int usedEmailAlert = 35;
	public static final int moreCoinsAlert = 36;
	public static final int buyCoinsAlert = 37;
	public static final int pushNotificationAlert = 38;

	public static final int errorRequestVideoAlert = 39;
	public static final int errorRequestAudioAlert = 40;
	public static final int errorRequestImageAlert = 41;
	public static final int closeGameAlert = 42;
	public static final int noActivePlayersAlert = 43;

	public static final int buyOkAlert = 44;
	public static final int drawTimeAlert = 45;

	public static final int noPurchasesAvailableAlert = 46;
	public static final int storeNotAvailableAlert = 47;
	public static final int deleteGameAlert = 48;
	public static final int buyItemAlert = 49;
	public static final int noCoinsAlert = 50;
	public static final int gameIsDeletedAlert = 51;
	public static final int accountDisabledAlert = 52;
	public static final int newVersionAlert = 53;
	public static final int noCameraAlert = 54;
	public static final int emailNotAvailableAlert = 55;
}
