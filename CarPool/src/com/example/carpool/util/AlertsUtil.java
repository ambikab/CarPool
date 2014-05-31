package com.example.carpool.util;

import android.content.Context;
import android.widget.Toast;

/**
 * Displays the required alerts
 * @author ambika_b
 *
 */
public class AlertsUtil {

	public static void showToast(Context context, String message) {
		Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
		toast.show();
	}
}
