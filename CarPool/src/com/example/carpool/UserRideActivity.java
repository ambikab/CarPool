package com.example.carpool;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.carpool.model.Ride;
import com.example.carpool.model.User;
import com.example.carpool.util.AlertsUtil;
import com.example.carpool.util.IntentUtil;
import com.example.carpool.util.JSONParser;
import com.example.carpool.util.URLValue;

/**
 * 
 * @author ambika_b
 *
 */
public class UserRideActivity extends Activity {

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
		//Inflate the view
		final View infoView = getLayoutInflater().inflate(R.layout.activity_user_ride, null);
		//Populate the list view.
		ArrayList<Ride> userRides = new ArrayList<Ride>();
		try {
			userRides = new FetchUserRideTask().execute().get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		} 
		fetchResults(infoView, userRides);
		setContentView(infoView);
	}

	class FetchUserRideTask extends AsyncTask<Void, Void, ArrayList<Ride>> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(UserRideActivity.this);
			progressDialog.setMessage("Loading..");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		protected ArrayList<Ride> doInBackground(Void... args) {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("user_id", user.getUserId() + "" ));
			JSONObject json = JSONParser.makeHttpRequest(URLValue.USER_RIDES, "GET", params);
			ArrayList<Ride> result = new ArrayList<Ride>();
			if (json != null) {
				try {
					JSONArray entries = json.getJSONArray("rides");
					for (int i = 0; i < entries.length(); i++) {
						JSONObject object = entries.getJSONObject(i);
						Ride newRide = new Ride();
						newRide.setComments(object.getString("comments"));
						newRide.setFrom(object.getString("cityIdFrom"));
						newRide.setTo(object.getString("cityIdTo"));
						newRide.setFromDate(object.getString("date"));
						newRide.setFromTime(object.getString("time"));
						newRide.setRideId(object.getInt("id"));
						result.add(newRide);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return result;
		}

		@Override
		protected void onPostExecute(ArrayList<Ride> result) {
			//Dismiss the progress bar alert.
			progressDialog.dismiss();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_ride, menu);
		return true;
	}

	public void fetchResults( View view, ArrayList<Ride> rides) {
		//Step01: get the list view object from the UI.
		final ListView listView = (ListView) view.findViewById(R.id.results);

		final RideAdapter adapter =  new RideAdapter(this, R.layout.layout_ride_results, rides, true);
		listView.setAdapter(adapter);

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				displayDialog(UserRideActivity.this, (((Ride) adapter.getItem(position))));
			}
		});  
	}


	/**
	 * Creates a dialog with the ride details populated in it.
	 * @param context
	 * @param ride
	 */
	public void displayDialog(Context context, final Ride ride) {
		final View dialogView = getLayoutInflater().inflate(R.layout.dialog_ride_edit, null);

		final TextView rFrom = (TextView) dialogView.findViewById(R.id.dialog_rFrom);
		rFrom.setText(ride.getFrom());

		final TextView rTo = (TextView) dialogView.findViewById(R.id.dialog_rTo);
		rTo.setText(ride.getTo());

		final TextView rEmail = (TextView) dialogView.findViewById(R.id.dialog_rEmail);
		rEmail.setText(user.getEmailId());

		final TextView rTime = (TextView) dialogView.findViewById(R.id.dialog_rTime);
		rTime.setText(ride.getFromTime());

		final TextView rComments = (TextView) dialogView.findViewById(R.id.dialog_comments);
		rComments.setText(ride.getComments());

		final TextView rDate = (TextView) dialogView.findViewById(R.id.dialog_rDate);
		rDate.setText(ride.getFromDate());

		//set the data for the items in the dialog
		final Dialog dialog = new Dialog(UserRideActivity.this);
		dialog.setContentView(dialogView);
		dialog.setTitle(ride.getFromDate() + " ( " + ride.getFromTime() + " )"); 

		final Button dialogButton = (Button) dialogView.findViewById(R.id.update);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Ride newRide = new Ride();
					newRide.setRideId(ride.getRideId());
					newRide.setComments(rComments.getText().toString());
					newRide.setFromDate(rDate.getText().toString());
					newRide.setFromTime(rTime.getText().toString());
					if(new UpdateRideTask().execute(newRide).get())
						AlertsUtil.showToast(UserRideActivity.this, "Update successful.");
					else
						AlertsUtil.showToast(UserRideActivity.this, "Error occured. Please retry after sometime.");
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				dialog.dismiss();
				Intent restart = new Intent(UserRideActivity.this, UserAccountActivity.class);
				restart.putExtra("session", session);
				restart.putExtra("user", user);
				restart.putExtra("rides", true);
				startActivity(restart);
			}
		});

		final Button cancelButton = (Button) dialogView.findViewById(R.id.cancel);
		cancelButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	class UpdateRideTask extends AsyncTask<Ride, Void, Boolean> {
		
		private ProgressDialog progressDialog = null;
		
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(UserRideActivity.this);
			progressDialog.setMessage("Loading..");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}
		
		protected Boolean doInBackground(Ride... rides) {
			ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ride_id", rides[0].getRideId() + ""));
			params.add(new BasicNameValuePair("ride_comments", rides[0].getComments()));
			params.add(new BasicNameValuePair("ride_date", rides[0].getFromDate()));
			params.add(new BasicNameValuePair("ride_time", rides[0].getFromTime()));
			JSONObject json = JSONParser.makeHttpRequest(URLValue.UPDATE_RIDE, "GET", params);
			boolean status = false;
			if (json != null) 
				try {
					status = json.getBoolean("status");
				} catch (JSONException e) {
					e.printStackTrace();
				}
			return status;
		}
		
		protected void onPostExecute(Boolean result) {
			progressDialog.dismiss();
		}
	}
}