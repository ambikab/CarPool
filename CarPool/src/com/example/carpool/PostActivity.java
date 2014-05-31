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
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.carpool.model.City;
import com.example.carpool.model.User;
import com.example.carpool.util.AlertsUtil;
import com.example.carpool.util.DisplayUIUtil;
import com.example.carpool.util.IntentUtil;
import com.example.carpool.util.JSONParser;
import com.example.carpool.util.URLValue;

public class PostActivity extends Activity implements OnItemSelectedListener {

	private boolean session;

	private User user;

	private int year;

	private int month;

	private int day;
	
	private double time = 0.00;

	static final int DATE_DIALOG_ID = 0;

	private ArrayList<City> cities = null;

	private HashMap<String, Integer> citylookUp = new HashMap<String, Integer>();

	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int selectedYear,
				int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
			Button showPicker = (Button) findViewById(R.id.post_date);
			showPicker.setText(Html.fromHtml(DisplayUIUtil.getUIDate(year, month, day, 50)));
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if( bundle == null) {
			IntentUtil.reLaunch(this, "Please singin to continue");
			return;
		}
		user = (User) bundle.getSerializable("user");
		session = bundle.getBoolean("session");
		if (!session) {
			IntentUtil.reLaunch(this, "Please signin to continue");
			return;
		}

		try {
			cities = new CityFetchTask().execute().get();
			for (City city : cities)
				citylookUp.put(city.getCityName(), city.getCityId());
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		ArrayAdapter<City> cityAdapter = new ArrayAdapter<City>(this, android.R.layout.simple_dropdown_item_1line, cities);
		final View postView = getLayoutInflater().inflate(R.layout.activity_post, null);
		final AutoCompleteTextView searchFrom = (AutoCompleteTextView) postView.findViewById(R.id.post_from);
		final AutoCompleteTextView searchTo = (AutoCompleteTextView) postView.findViewById(R.id.post_to);
		Spinner spinner = (Spinner) postView.findViewById(R.id.post_time);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.time_array, android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		searchTo.setAdapter(cityAdapter);
		searchFrom.setAdapter(cityAdapter);
		setContentView(postView);
		setCurrentDateOnView();
	}

	class CityFetchTask extends AsyncTask<Void, Void, ArrayList<City>> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(PostActivity.this);
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
		Button showPicker = (Button) findViewById(R.id.post_date);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);

		// set current date into text view
		showPicker.setText(Html.fromHtml(DisplayUIUtil.getUIDate(year, month, day, 45)));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post, menu);
		return true;
	}

	/**
	 * Saves the ride posted by the current user.
	 * @param view
	 */
	public void saveRide(View view) {
		new AddRideTask().execute();
		AlertsUtil.showToast(this, "Successfully posted!");
	}

	public void onItemSelected(AdapterView<?> parent, View view, 
            int pos, long id) {
        time = Double.valueOf(parent.getItemAtPosition(pos) + "");
    }

    public void onNothingSelected(AdapterView<?> parent) { time = 0.00; }
    
	class AddRideTask extends AsyncTask<Void, Void, Void> {

		private ProgressDialog progressDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(PostActivity.this);
			progressDialog.setMessage("Fetching..");
			progressDialog.setIndeterminate(true);
			progressDialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) {
			AutoCompleteTextView from = (AutoCompleteTextView) findViewById(R.id.post_from);
			String fromCities = from.getText().toString();
			from = (AutoCompleteTextView) findViewById(R.id.post_to);
			String toCities = from.getText().toString();
			Button showPicker = (Button) findViewById(R.id.post_date);
			String date = showPicker.getText().toString();
			EditText comments = (EditText) findViewById(R.id.post_comments);
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("city_from", citylookUp.get(fromCities) + ""));
			params.add(new BasicNameValuePair("city_to", citylookUp.get(toCities) + ""));
			params.add(new BasicNameValuePair("ride_date", date));
			params.add(new BasicNameValuePair("ride_time", time + ""));
			params.add(new BasicNameValuePair("ride_comments", comments.getText().toString()));
			params.add(new BasicNameValuePair("user_id", user.getUserId() + ""));
			System.out.println("Date : " + date + "From " + fromCities + "To :" + toCities);
			JSONObject json = JSONParser.makeHttpRequest(URLValue.ADD_RIDE, "GET", params);
			if (json != null) {
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// dismiss the dialog once got all details
			progressDialog.dismiss();
		}
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