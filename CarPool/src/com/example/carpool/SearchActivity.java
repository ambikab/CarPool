package com.example.carpool;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import com.example.carpool.model.City;
import com.example.carpool.model.Ride;
import com.example.carpool.model.User;
import com.example.carpool.util.DisplayUIUtil;
import com.example.carpool.util.IntentUtil;
import com.example.carpool.util.JSONParser;
import com.example.carpool.util.URLValue;

/**
 * Lets the user to search and view his / her results.
 * @author ambika_b
 *
 */

@SuppressWarnings("deprecation")
public class SearchActivity extends Activity {

	private boolean session;

	private User user;

	private int year;

	private int month;

	private int day;

	static final int DATE_DIALOG_ID = 0;

	private ArrayList<City> cities = null;

	private HashMap<String, Integer> citylookUp = new HashMap<String, Integer>();

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			Button showPicker = (Button) findViewById(R.id.search_date);
			showPicker.setText(Html.fromHtml(DisplayUIUtil.getUIDate(year, month, day, 50)));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if( bundle == null) {
			IntentUtil.reLaunch(this, "Please sign-in to continue");
			return;
		}
		//Extract the details from the intent. 
		user = (User) bundle.getSerializable("user");
		session = bundle.getBoolean("session");
		if (!session) {
			IntentUtil.reLaunch(this, "Please signin to continue");
			return;
		}

		try {
			cities = new CityFetchTask().execute().get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		//CityAdapter cityAdapter = new CityAdapter(this, R.layout.layout_city_list, cities, false);
		ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(this, android.R.layout.simple_dropdown_item_1line, cities);
		final View searchView = getLayoutInflater().inflate(R.layout.activity_search, null);
		final MultiAutoCompleteTextView searchFrom = (MultiAutoCompleteTextView) searchView.findViewById(R.id.search_from);
		final MultiAutoCompleteTextView searchTo = (MultiAutoCompleteTextView) searchView.findViewById(R.id.search_to);
		searchTo.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		searchTo.setAdapter(cityAdapter);
		searchFrom.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
		searchFrom.setAdapter(cityAdapter);

		setContentView(searchView);
		setCurrentDateOnView();
	}

	class CityFetchTask extends AsyncTask<Void, Void, ArrayList<City>> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(SearchActivity.this);
			progressDialog.setMessage("Loading..");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected ArrayList<City> doInBackground(Void... params) {
			JSONObject json = JSONParser.makeHttpRequest(URLValue.ALL_CITIES, "GET", new ArrayList<NameValuePair>());
			ArrayList<City> cities = new ArrayList<City>();
			if (json != null) {
				try {
					JSONArray entries = json.getJSONArray("cities");
					for (int i = 0; i < entries.length(); i++) {
						JSONObject object = entries.getJSONObject(i);
						cities.add( new City(object.getInt("id"), object.getString("cityName")));
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return cities;
		}

		@Override
		protected void onPostExecute(ArrayList<City> result) {
			cities = result;
			//Dismiss the progress bar alert.
			progressDialog.dismiss();
		}
	}

	/**
	 * pops up a date picker on touch of the text view.
	 */
	public void showPicker(View view) {
		showDialog(DATE_DIALOG_ID);
	}

	/**
	 * Displays the current date on the view.
	 */
	public void setCurrentDateOnView() {
		Button showPicker = (Button) findViewById(R.id.search_date);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into text view
		showPicker.setText(Html.fromHtml(DisplayUIUtil.getUIDate(year, month, day, 45)));
	}

	/**
	 * on click of the button 
	 * fetches the result and 
	 * populates the list adapter.
	 * @param view
	 * @throws ExecutionException 
	 * @throws InterruptedException 
	 */
	public void fetchResults( View view) throws InterruptedException, ExecutionException {
		//Step01: get the list view object from the UI.
		ListView listView = (ListView) findViewById(R.id.results);
		//Start a request to fetch the results from an async task.
		ArrayList<Ride> rides = new FetchResultTask().execute().get();

		final RideAdapter adapter =  new RideAdapter(this, R.layout.layout_ride_results, rides, false);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
				View dialogView = null;
				try {
					dialogView = new FetchRideTask().execute(((Ride) adapter.getItem(position)).getRideId()).get();
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
				displayDialog(SearchActivity.this, dialogView);
			}
		});  
	}

	class FetchRideTask extends AsyncTask<Integer, Void, View> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(SearchActivity.this);
			progressDialog.setMessage("Loading..");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected View doInBackground(Integer... arg0) {
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("ride_id", arg0[0] + ""));
			JSONObject json = JSONParser.makeHttpRequest(URLValue.FETCH_RIDE, "GET", params);
			if (json != null) {
				try {
					
					final View dialogView = getLayoutInflater().inflate(R.layout.dialog_ride_details, null);
					final TextView rDate = (TextView) dialogView.findViewById(R.id.dialog_rDate);
					rDate.setText(json.getString("ride_date"));
					final TextView rTime = (TextView) dialogView.findViewById(R.id.dialog_rTime);
					rTime.setText(DisplayUIUtil.getUiTime(json.getString("ride_time")));
					final TextView rFrom = (TextView) dialogView.findViewById(R.id.dialog_rFrom);
					rFrom.setText(json.getString("city_from"));
					final TextView rTo = (TextView) dialogView.findViewById(R.id.dialog_rTo);
					rTo.setText(json.getString("city_to"));
					final TextView rEmail = (TextView) dialogView.findViewById(R.id.dialog_rEmail);
					rEmail.setText(json.getString("user_email"));
					final TextView rMobile = (TextView) dialogView.findViewById(R.id.dialog_rMobile);
					rMobile.setText(json.getString("user_mobile"));
					final TextView rComments = (TextView) dialogView.findViewById(R.id.dialog_comments);
					rComments.setText(json.getString("ride_comments") + "  -  " + json.getString("user_name"));
					return dialogView;
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return null;
			}
			return null;
		}

		@Override
		protected void onPostExecute(View view) {
			// dismiss the dialog once got all details
			progressDialog.dismiss();

		}

	}

	class FetchResultTask extends AsyncTask<Void, Void, ArrayList<Ride>> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(SearchActivity.this);
			progressDialog.setMessage("Fetching..");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected ArrayList<Ride> doInBackground(Void... arg0) {
			MultiAutoCompleteTextView from = (MultiAutoCompleteTextView) findViewById(R.id.search_from);
			String fromCities = from.getText().toString();
			from = (MultiAutoCompleteTextView) findViewById(R.id.search_to);
			String toCities = from.getText().toString();
			Button showPicker = (Button) findViewById(R.id.search_date);
			String date = showPicker.getText().toString();
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("city_from", fromCities));
			params.add(new BasicNameValuePair("city_to", toCities));
			params.add(new BasicNameValuePair("ride_date", date));
			System.out.println("Date : " + date + "From " + fromCities + "To :" + toCities);
			//TODO:change to post.
			ArrayList<Ride> rides = new ArrayList<Ride>();
			JSONObject json = JSONParser.makeHttpRequest(URLValue.SEARCH_RIDES, "GET", params);
			if (json != null) {
				try {
					JSONArray entries = json.getJSONArray("rides");
					for (int i = 0; i < entries.length(); i++) {
						JSONObject object = entries.getJSONObject(i);
						Ride ride = new Ride();
						ride.setComments(object.getString("comments"));
						ride.setFrom(object.getString("cityIdFrom"));
						ride.setTo(object.getString("cityIdTo"));
						ride.setFromDate(object.getString("date"));
						ride.setFromTime(object.getString("time"));
						ride.setRideId(object.getInt("id"));
						rides.add(ride);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			return rides;
		}

		@Override
		protected void onPostExecute(ArrayList<Ride> result) {
			// dismiss the dialog once got all details
			progressDialog.dismiss();

		}

	}

	/**
	 * Creates a dialog with the ride details populated in it.
	 * @param context
	 * @param ride
	 */
	public void displayDialog(Context context, View dialogView) {
		//set the data for the items in the dialog
		final Dialog dialog = new Dialog(SearchActivity.this);
		dialog.setContentView(dialogView);
		final TextView rDate = (TextView) dialogView.findViewById(R.id.dialog_rDate);
		final TextView rTime = (TextView) dialogView.findViewById(R.id.dialog_rTime);
		dialog.setTitle( rDate.getText().toString() + " ( " + rTime.getText().toString() + " )");
		//dialog.setTitle(ride.getFromDate() + " ( " + ride.getFromTime() + " )");
		dialog.show();
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
			return new DatePickerDialog(this, datePickerListener, year, month,day);
		}
		return null;
	}

}