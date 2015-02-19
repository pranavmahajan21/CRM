package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.MyApp;
import com.mw.crm.service.AccountService;
import com.mw.crm.service.AppointmentService;
import com.mw.crm.service.ContactService;
import com.mw.crm.service.InternalConnectService;
import com.mw.crm.service.OpportunityService;

public class MenuActivity2 extends Activity {

	public static int X = 0;
	public static boolean isActivityVisible = false;

	TextView syncDate_TV;
	LinearLayout lastSync_LL;

	MyApp myApp;
	Intent nextIntent, previousIntent;

	CreateDialog createDialog;
	ProgressDialog progressDialog;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	private BroadcastReceiver appDataReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			X--;
			System.out.println("appDataReceiver  : " + X);
			if (X == 0) {
				progressDialog.dismiss();
			}
			Map<String, String> userMap = myApp.getUserMap();
			List<String> keys = new ArrayList<String>(userMap.keySet());
			for (int i = 0; i < keys.size(); i++) {
				System.out.println("***** " + keys.get(i));
				if (myApp.getLoginUserId().equals(keys.get(i))) {
					System.out.println("hurray  " + userMap.get(keys.get(i)));
				}
			}
		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();

		previousIntent = getIntent();

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Account Setup",
				"This may take some time", true, null);

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();

	}

	public void findThings() {
		syncDate_TV = (TextView) findViewById(R.id.syncDate_TV);
		lastSync_LL = (LinearLayout) findViewById(R.id.lastSync_LL);
	}

	private void setTypeface() {
		syncDate_TV.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String title, String title2) {
		setTypeface();
		if (sharedPreferences.contains("last_sync_date")) {
			syncDate_TV.setText(sharedPreferences.getString("last_sync_date",
					"-"));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_grid2);

		initThings();
		findThings();
		initView("Menu", null);

		if (previousIntent.hasExtra("is_first_time_login")
				&& previousIntent.getBooleanExtra("is_first_time_login", true)) {
			loadAppData();
		}
	}// onCreate

	private void loadAppData() {
		progressDialog.show();

		Intent intent = new Intent(this, AccountService.class);

		X++;
		startService(intent);

		intent = new Intent(this, AppointmentService.class);
		X++;
		startService(intent);

		intent = new Intent(this, ContactService.class);
		X++;
		startService(intent);

		intent = new Intent(this, OpportunityService.class);
		X++;
		startService(intent);

		intent = new Intent(this, InternalConnectService.class);
		X++;
		startService(intent);

		// TODO: do this in broadcaster
		String temp = myApp.formatDateToString3(new Date());
		syncDate_TV.setText(temp);
		editor.putString("last_sync_date", temp);
		editor.commit();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActivityVisible = true;
		LocalBroadcastManager.getInstance(this).registerReceiver(
				appDataReceiver, new IntentFilter("app_data"));
	}

	@Override
	protected void onPause() {
		isActivityVisible = false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				appDataReceiver);
		super.onPause();
	}

	public void onSync(View view) {
		loadAppData();
	}

	public void onLogout(View view) {
		editor.clear();
		editor.commit();

		nextIntent = new Intent(this, LoginActivity.class);
		nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(nextIntent);
	}

	public void onContact(View view) {
		nextIntent = new Intent(MenuActivity2.this, ContactListActivity.class);
		nextIntent.putExtra("is_my_contact", true);
		startActivity(nextIntent);
	}

	public void onAccount(View view) {
		nextIntent = new Intent(MenuActivity2.this, AccountListActivity.class);
		startActivity(nextIntent);
	}

	public void onAppointment(View view) {
		nextIntent = new Intent(MenuActivity2.this,
				AppointmentListActivity.class);
		nextIntent.putExtra("is_my_appointment", true);
		startActivity(nextIntent);

	}

	public void onOpportunity(View view) {
		nextIntent = new Intent(MenuActivity2.this,
				OpportunityListActivity.class);
		nextIntent.putExtra("is_my_opportunity", true);
		startActivity(nextIntent);
	}
}
