package com.example.carpool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.carpool.model.User;
import com.example.carpool.util.AlertsUtil;
import com.example.carpool.util.IntentUtil;
import com.example.carpool.util.JSONParser;
import com.example.carpool.util.URLValue;

public class UserInfoActivity extends Activity {

	private User user;

	private boolean session;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Retrieve the session information.
		Bundle bundle = getIntent().getExtras();
		if( bundle == null) {
			IntentUtil.reLaunch(this, "Please singin to continue");
			return;
		}
		user = (User) bundle.getSerializable("user");
		session = bundle.getBoolean("session");
		if (!session || user == null) {
			IntentUtil.reLaunch(this, "Please singin to continue");
			return;
		}
		//fetches the user info.
		Long[] params = {user.getUserId()};
		try {
			user = new UserInfoTask().execute(params).get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		//Inflate the view
		final View infoView = getLayoutInflater().inflate(R.layout.activity_user_info, null);
		//Fetch the UI elements.
		final EditText uName = (EditText) infoView.findViewById(R.id.uaccount_uName);
		final EditText uMobile = (EditText) infoView.findViewById(R.id.uaccount_uMobile);
		final EditText uPass = (EditText) infoView.findViewById(R.id.uaccount_uPass);
		final EditText uEmail = (EditText) infoView.findViewById(R.id.uaccount_uEmail);

		//populate the values for the UI elements.
		uName.setText(user.getUserName());
		uMobile.setText(user.getMobileNo());
		uEmail.setText(user.getEmailId());
		uPass.setText(user.getPassword());
		setContentView(infoView);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_info, menu);
		return true;
	}

	public void update(View view) {
		new UpdateUserTask().execute();
	}

	class UserInfoTask extends AsyncTask<Long, Void, User> {
		private ProgressDialog progressDialog;

		boolean success = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(UserInfoActivity.this);
			progressDialog.setMessage("Loading..");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected User doInBackground(Long... arg0) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", user.getUserId() + ""));
			JSONObject json = JSONParser.makeHttpRequest(URLValue.USER_INFO, "GET", params);
			User currentUser = null;
			if (json != null) {
				try {
					currentUser = new User();
					currentUser.setUserId(json.getInt("id"));
					currentUser.setUserName(json.getString("Name"));
					currentUser.setMobileNo(json.getString("mobile"));
					currentUser.setEmailId(json.getString("email"));
					currentUser.setPassword(json.getString("password"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return currentUser;
		}

		@Override
		protected void onPostExecute(User result) {
			user = result;
			//Dismiss the progress bar alert.
			progressDialog.dismiss();
		}
	}

	/**
	 * Async task to update the changes to the db 
	 * made by the user.
	 * @author ambika_b
	 *
	 */
	class UpdateUserTask extends AsyncTask<Void, Void, Void> {
		private ProgressDialog progressDialog;

		boolean success = false;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(UserInfoActivity.this);
			progressDialog.setMessage("Loading..");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			//Inflate the view
			//Fetch the UI elements.
			EditText uName = (EditText) findViewById(R.id.uaccount_uName);
			EditText uMobile = (EditText) findViewById(R.id.uaccount_uMobile);
			EditText uPass = (EditText) findViewById(R.id.uaccount_uPass);
			EditText uEmail = (EditText) findViewById(R.id.uaccount_uEmail);

			//populate the values for the UI elements.
			String name = uName.getText().toString();
			String mobile = uMobile.getText().toString();
			String email = uEmail.getText().toString();
			String pass = uPass.getText().toString();

			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_email", email));
			params.add(new BasicNameValuePair("user_pass", pass));
			params.add(new BasicNameValuePair("user_mobile", mobile));
			params.add(new BasicNameValuePair("user_name", name));
			params.add(new BasicNameValuePair("user_id", user.getUserId() + ""));

			JSONObject json = JSONParser.makeHttpRequest(URLValue.UPDATE_USER, "GET", params);
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
				message = "Info updated.";
			else
				message = "Error. Please re-try after sometime.";
			AlertsUtil.showToast(UserInfoActivity.this, message);
		}
	}
}
