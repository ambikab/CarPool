package com.example.carpool.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class DisplayUIUtil {

	public static String getUIDate(int year, int month, int day, int fontSize) {
		StringBuilder uiDate = new StringBuilder();
		//StringBuilder uiDate = new StringBuilder("<p> <font size=\" " + fontSize + " \">");
		// returns a date of the format mm-dd-yy
		uiDate.append(month + 1).append("-").append(day).append("-").append(year).append(" ");
		//uiDate.append("</p>");
		return uiDate.toString();
	}
	
	public static String getUiTime(String time) {
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setDecimalSeparator(':');
		DecimalFormat df = new DecimalFormat("00.0#", symbols);
		String resultTime = df.format(Double.valueOf(time));
		return resultTime.length() == 5 ? resultTime : resultTime + "0";
	}
}