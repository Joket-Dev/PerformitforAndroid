package com.llc.performit.model;

import java.util.ArrayList;

import com.llc.performit.draw.drawBrushPoint;
import com.llc.performit.draw.drawColorPoint;
import com.llc.performit.draw.drawObjectArray;

public class WordItem {
	public int		coins;
	public int		completed;
	public int		enabled;
	public int		gameId;
	public String	myTurn;
	public String	theirTurn;
	public int		time;
	public String	type;
	public String	word;
	public int		id;
	
	public String	mediaPath;
	
	public ArrayList<drawObjectArray>	mLines;
	public drawColorPoint				mBGColor;

}
