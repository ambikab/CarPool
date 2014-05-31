package com.example.carpool.util;

import android.content.Context;
import android.content.Intent;

import com.example.carpool.AuthenticateActivity;

/**
 * 
 * @author ambika_b
 *
 */
public class IntentUtil {

	/**
	 * Invoke the  when the session is not valid.
	 * Also invalidates any of the session information contained in the intent
	 * @param context
	 * @param message
	 * @param className
	 */
	public static void reLaunch(Context context, String message) {
		Intent intent =  new Intent(context, AuthenticateActivity.class);
		intent.putExtra("session", false);
		intent.putExtra("user", "");
		context.startActivity(intent);
		if(message.length() > 0)
			AlertsUtil.showToast(context, message);
	}
}