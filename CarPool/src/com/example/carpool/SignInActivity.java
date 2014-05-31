package com.example.carpool;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.carpool.model.User;
import com.example.carpool.util.JSONParser;
import com.example.carpool.util.Result;
import com.example.carpool.util.URLValue;

/**
 * Sign in Screen 
 * @author ambika_b
 *
 */

public class SignInActivity  extends Activity {

	User currentUser = null;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_in);
	}

	public void signIn(View view) throws JSONException {
		//Get details from UI
		EditText editText = (EditText) findViewById(R.id.signin_uName);
		String uName = editText.getText().toString();
		editText = (EditText) findViewById(R.id.signin_uPass);
		String uPass = editText.getText().toString();

		//validate the input.
		Result result = validateInput(uName, uPass);
		String message = "";
		
		if (result == null) 
			message = "Error occured\n. Please try again after sometime.";
		else if (!result.isStatus()) 
			message = result.getMessage();
		else {
			new SignInATask().execute();
			return; // doesn't reach the showToast method call.
		}
		showToast(message);
	}

	class SignInATask extends AsyncTask<Void, Void, Void> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(SignInActivity.this);
			progressDialog.setMessage("Authenticating..");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			EditText editText = (EditText) findViewById(R.id.signin_uName);
			String uName = editText.getText().toString();
			editText = (EditText) findViewById(R.id.signin_uPass);
			String uPass = editText.getText().toString();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_email", uName));
			params.add(new BasicNameValuePair("user_pass", uPass));
			JSONObject json = JSONParser.makeHttpRequest(URLValue.SIGN_IN, "GET", params);
			if (json != null) {
				currentUser = new User();
				try {
					System.out.println("user found :D");
					currentUser.setUserId(json.getInt("id"));
					currentUser.setUserName(json.getString("Name"));
					currentUser.setMobileNo(json.getString("mobile"));
					currentUser.setEmailId(json.getString("email"));
					currentUser.setPassword(json.getString("password"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// dismiss the dialog once got all details
			progressDialog.dismiss();
			if (currentUser == null)
				showToast("Incorrect credentials.");
			else {
				Intent homeScreen = new Intent(SignInActivity.this, UserHomeActivity.class);
				homeScreen.putExtra("session", true);
				homeScreen.putExtra("user", currentUser);
				startActivity(homeScreen);
			}
		}
	}

	public void showToast(String message) {
		Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
		toast.show();
	}

	public Result validateInput(String uName, String uPass) {
		if (!isValidLength(uName)) return new Result("Enter a valid user name.", false);
		if (!isValidLength(uPass)) return new Result("Enter a valid password.", false);
		else return new Result("", true);
	}

	public boolean isValidLength(String value) {
		return value.length() > 0;
	}
}