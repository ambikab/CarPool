package com.example.carpool;

import android.annotation.TargetApi;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.example.carpool.model.User;
import com.example.carpool.util.IntentUtil;

@SuppressWarnings("deprecation")
public class UserAccountActivity extends TabActivity {

	User currentUser;

	boolean session;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		//Retrieve the session information.
		Bundle bundle = getIntent().getExtras();
		if( bundle == null) {
			IntentUtil.reLaunch(this, "Please singin to continue");
			return;
		}
		currentUser = (User) bundle.getSerializable("user");
		session = bundle.getBoolean("session");
		if (!session || currentUser == null) {
			IntentUtil.reLaunch(this, "Please signin to continue");
			return;
		}
		boolean rides = (boolean) bundle.getBoolean("rides");
		setContentView(R.layout.activity_user_account);
		//Set the tab activities
		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost();  // The activity TabHost
		TabHost.TabSpec spec;  // Reusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab.
		intent = new Intent().setClass(this, UserInfoActivity.class);
		intent.putExtra("session", true);
		intent.putExtra("user", currentUser);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("Account")
				.setIndicator("Account", res.getDrawable(R.drawable.ic_tab_search))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, UserRideActivity.class);
		intent.putExtra("session", true);
		intent.putExtra("user", currentUser);

		spec = tabHost.newTabSpec("ridehistory").setIndicator("Ride History",
				res.getDrawable(R.drawable.ic_tab_post))
				.setContent(intent);
		tabHost.addTab(spec);
		if (!rides)
			tabHost.setCurrentTab(0);
		else tabHost.setCurrentTab(1);
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_account, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent parent = new Intent(this, UserHomeActivity.class);
			parent.putExtra("session", session);
			parent.putExtra("user", currentUser);
			NavUtils.navigateUpTo(this, parent);
			return true;
		} 
		return super.onOptionsItemSelected(item);
	}
}