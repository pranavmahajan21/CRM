package com.mw.crm.activity;

import java.util.Date;
import java.util.List;

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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.mw.crm.adapter.MenuAdapter;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.MenuItem;
import com.mw.crm.service.AccountService;
import com.mw.crm.service.AppointmentService;
import com.mw.crm.service.ContactService;
import com.mw.crm.service.InternalConnectService;
import com.mw.crm.service.OpportunityService;

public class MenuActivity extends Activity {

	public static int X = 0;
	public static boolean isActivityVisible = false;

	TextView syncDate_TV;
	LinearLayout lastSync_LL;

	MyApp myApp;
	Intent nextIntent, previousIntent;

	GridView menuGV;

	MenuAdapter adapter;

	CreateDialog createDialog;
	ProgressDialog progressDialog;

	List<MenuItem> menuItemList;

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
		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		menuItemList = myApp.getMenuItemList();

		previousIntent = getIntent();

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Account Setup",
				"This may take some time", true, null);

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();

		if (menuItemList != null && menuItemList.size() > 0) {
			adapter = new MenuAdapter(this, menuItemList);
		}
		// nextIntent = new Intent(this, TicketDetailActivity.class);
	}

	public void findThings() {
		menuGV = (GridView) findViewById(R.id.menu_GV);
		syncDate_TV = (TextView) findViewById(R.id.syncDate_TV);
		lastSync_LL = (LinearLayout) findViewById(R.id.lastSync_LL);
	}

	public void initView(String title, String title2) {
		// super.initView(title, title2);
		// ((ImageButton)
		// findViewById(R.id.back_B)).setVisibility(View.INVISIBLE);

		if (sharedPreferences.contains("last_sync_date")) {
			syncDate_TV.setText(sharedPreferences.getString("last_sync_date",
					"-"));
		}

		if (adapter != null) {
			menuGV.setAdapter(adapter);
		} else {
			// no tickets
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_menu_grid);

		initThings();
		findThings();
		initView("Menu", null);

		menuGV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 0:
					nextIntent = new Intent(MenuActivity.this,
							ContactListActivity.class);
					startActivity(nextIntent);
					break;

				case 1:
					nextIntent = new Intent(MenuActivity.this,
							AccountListActivity.class);
					startActivity(nextIntent);
					break;

				case 2:
					nextIntent = new Intent(MenuActivity.this,
							AppointmentListActivity.class);
					startActivity(nextIntent);
					break;

				case 3:
					nextIntent = new Intent(MenuActivity.this,
							OpportunityListActivity.class);
					startActivity(nextIntent);
					break;

				default:
					break;
				}

			}

		});
		if (previousIntent.hasExtra("is_first_time_login")
				&& previousIntent.getBooleanExtra("is_first_time_login", true)) {
			loadAppData();
		}
	}// onCreate

	private void loadAppData() {
		progressDialog.show();

		Intent intent = new Intent(this, AccountService.class);

//		X++;
//		startService(intent);
//
//		intent = new Intent(this, AppointmentService.class);
//		X++;
//		startService(intent);
//
		intent = new Intent(this, ContactService.class);
		X++;
		startService(intent);

//		intent = new Intent(this, OpportunityService.class);
//		X++;
//		startService(intent);
//
//		intent = new Intent(this, InternalConnectService.class);
//		X++;
//		startService(intent);

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
}
