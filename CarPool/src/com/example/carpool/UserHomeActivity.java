package com.example.carpool;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import com.example.carpool.model.User;
import com.example.carpool.util.AlertsUtil;
import com.example.carpool.util.IntentUtil;

@SuppressWarnings("deprecation")
public class UserHomeActivity extends TabActivity {

	User currentUser;
	
	boolean session;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle bundle = getIntent().getExtras();
		if( bundle == null) {
			IntentUtil.reLaunch(this, "Please singin to continue");
			return;
		}
		//Extract the details from the intent. 
		currentUser = (User) bundle.getSerializable("user");
		session = bundle.getBoolean("session");
		if (!session) {
			IntentUtil.reLaunch(this, "Please singin to continue");
			return;
		}

		setContentView(R.layout.activity_authenticate);

		Resources res = getResources(); // Resource object to get Drawables
		TabHost tabHost = getTabHost();  // The activity TabHost
		TabHost.TabSpec spec;  // Reusable TabSpec for each tab
		Intent intent;  // Reusable Intent for each tab

		// Create an Intent to launch an Activity for the tab.
		intent = new Intent().setClass(this, SearchActivity.class);
		intent.putExtra("session", true);
		intent.putExtra("user", currentUser);

		// Initialize a TabSpec for each tab and add it to the TabHost
		spec = tabHost.newTabSpec("Search")
				.setIndicator("Search", res.getDrawable(R.drawable.ic_tab_search))
				.setContent(intent);
		tabHost.addTab(spec);

		// Do the same for the other tabs
		intent = new Intent().setClass(this, PostActivity.class);
		intent.putExtra("session", true);
		intent.putExtra("user", currentUser);

		spec = tabHost.newTabSpec("post").setIndicator("Post",
				res.getDrawable(R.drawable.ic_tab_post))
				.setContent(intent);
		tabHost.addTab(spec);
		//tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#5a2e7d"));
		tabHost.setCurrentTab(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.user_home, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			//NavUtils.navigateUpFromSameTask(this);
			return true;
		case R.id.action_account :
			Intent intent = new Intent(this, UserAccountActivity.class);
			intent.putExtra("session", session);
			intent.putExtra("user", currentUser);
			startActivity(intent);
			return true;
		case R.id.action_logout :
			Intent parent = new Intent(this, AuthenticateActivity.class);
			parent.putExtra("session", false);
			parent.putExtra("user", "");
			NavUtils.navigateUpTo(this, parent);
			AlertsUtil.showToast(this, "Logged out Successfully.");
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}