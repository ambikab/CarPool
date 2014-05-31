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
 * Sign Up Screen and related tasks.
 * @author ambika_b
 *
 */
public class SignUpActivity  extends Activity {

	/**
	 * inflates the view
	 */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sign_up);
	}

	public void signUp(View view) {
		//Step 01: collect the strings from the fields.
		/*EditText editText = (EditText) findViewById(R.id.signup_uName);
		String uName = editText.getText().toString();
		editText = (EditText) findViewById(R.id.signup_uPass);
		String uPass = editText.getText().toString();
		editText = (EditText) findViewById(R.id.signup_uMobile);
		String uMobile = editText.getText().toString();
		editText = (EditText) findViewById(R.id.signup_uEmail);
		String uEmail = editText.getText().toString();*/

		//Step 02: validate input before adding them to the database.
		//Result result = validateInputs(uName, uPass, uMobile);
		new SignUpTask().execute();
	}

	class SignUpTask extends AsyncTask<Void, Void, Void> {

		private ProgressDialog progressDialog;

		boolean success = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(SignUpActivity.this);
			progressDialog.setMessage("In progress..");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			EditText editText = (EditText) findViewById(R.id.signup_uName);
			String uName = editText.getText().toString();
			editText = (EditText) findViewById(R.id.signup_uPass);
			String uPass = editText.getText().toString();
			editText = (EditText) findViewById(R.id.signup_uMobile);
			String uMobile = editText.getText().toString();
			editText = (EditText) findViewById(R.id.signup_uEmail);
			String uEmail = editText.getText().toString();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_email", uEmail));
			params.add(new BasicNameValuePair("user_pass", uPass));
			params.add(new BasicNameValuePair("user_mobile", uMobile));
			params.add(new BasicNameValuePair("user_name", uName));

			JSONObject json = JSONParser.makeHttpRequest(URLValue.ADD_USER, "GET", params);
			if (json != null) {
				try {
					success = json.getBoolean("status");
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
			String message;
			if (success) 	
				message = "Registered successfully!!\n You can sign-in now.";
			else
				message = "Error. Please re-try after sometime.";
			Toast toast = Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT);
			toast.show();
		}

	}

	public Result validateInputs(String uName, String uPass, String uMobile) {
		Result result = null;
		return result;
	}
}
