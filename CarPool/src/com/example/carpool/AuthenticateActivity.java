package com.example.carpool;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TabHost;

@SuppressWarnings("deprecation")
public class AuthenticateActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_authenticate);
		
        Resources res = getResources(); // Resource object to get Drawables
        TabHost tabHost = getTabHost();  // The activity TabHost
        TabHost.TabSpec spec;  // Reusable TabSpec for each tab
        Intent intent;  // Reusable Intent for each tab

        // Create an Intent to launch an Activity for the tab (to be reused)
        intent = new Intent().setClass(this, SignInActivity.class);

        //TODO: create separate drawables for sign-in and sign-up
        // Initialize a TabSpec for each tab and add it to the TabHost
        spec = tabHost.newTabSpec("signin")
        			  .setIndicator("SignIn", res.getDrawable(R.drawable.ic_tab_search))
                      .setContent(intent);
        tabHost.addTab(spec);

        // Do the same for the other tabs
        intent = new Intent().setClass(this, SignUpActivity.class);
        spec = tabHost.newTabSpec("signup").setIndicator("SignUp",
                          res.getDrawable(R.drawable.ic_tab_post))
                      .setContent(intent);
        tabHost.addTab(spec);
        //tabHost.getTabWidget().getChildAt(0).setBackgroundColor(Color.parseColor("#5a2e7d"));
        tabHost.setCurrentTab(0);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.authenticate, menu);
		return true;
	}

}