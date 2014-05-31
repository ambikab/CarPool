package com.example.carpool.util;

//From:  http://www.androidhive.info/2012/05/how-to-connect-android-with-php-mysql/
//Author:  Ravi Tamada

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import android.util.Log;

public abstract class JSONParser {

	// function get json from url by making HTTP POST or GET method
	public static JSONObject makeHttpRequest(String url, String method,
			List<NameValuePair> params) {

		InputStream is = null;
		// Making HTTP request
		try {

			// check for request method
			if (method == "POST") {
				// request method is POST
				// defaultHttpClient
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url);
				httpPost.setEntity(new UrlEncodedFormEntity(params));

				HttpResponse httpResponse = httpClient.execute(httpPost);
				is = httpResponse.getEntity().getContent();

			} else if (method == "GET") {
				// request method is GET
				DefaultHttpClient httpClient = new DefaultHttpClient();
				String paramString = URLEncodedUtils.format(params, "utf-8");
				url += "?" + paramString;
				HttpGet httpGet = new HttpGet(url);

				HttpResponse httpResponse = httpClient.execute(httpGet);
				is = httpResponse.getEntity().getContent();
			}           

		} catch (UnsupportedEncodingException e) {
			return null;
		} catch (ClientProtocolException e) {
			return null;
		} catch (IOException e) {
			return null;
		}

		String json = "";
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "iso-8859-1"));
			String line = null;
			while ((line = reader.readLine()) != null) {
				json = json + line + "\n";
			}
			is.close();
		} catch (Exception e) {
			Log.e("JSONParser", "Error converting result " + e.toString());
			return null;
		}

		try {
			return new JSONObject(json);
		} catch (JSONException e) {
			Log.e("JSONParser", "Error parsing data " + e.toString());
		}

		return null;
	}
}