package com.mw.crm.activity;

import java.lang.reflect.Type;
import java.util.ArrayList;
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
import android.widget.TextView;

import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mw.crm.application.MyApp;
import com.mw.crm.extra.Constant;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.DateFormatter;
import com.mw.crm.model.Account;
import com.mw.crm.model.Appointment;
import com.mw.crm.model.Contact;
import com.mw.crm.model.Opportunity;
import com.mw.crm.service.AccountService;
import com.mw.crm.service.AppointmentService;
import com.mw.crm.service.CompetitorService;
import com.mw.crm.service.ContactService;
import com.mw.crm.service.OpportunityService;
import com.mw.crm.service.ProductService;
import com.mw.crm.service.ProfitCenterService;
import com.mw.crm.service.SolutionService;
import com.mw.crm.service.UserService;

public class MenuActivity2 extends Activity {

	public static int X = 0;
	public static boolean isActivityVisible = false;

	TextView menu_item_TV1, menu_item_TV2, menu_item_TV3, menu_item_TV4;
	TextView syncMenuItem1_TV, syncMenuItem2_TV, syncMenuItem3_TV,
			syncMenuItem4_TV;

	MyApp myApp;
	Intent nextIntent, previousIntent;

	CreateDialog createDialog;
	ProgressDialog progressDialog;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	Gson gson;

	DateFormatter dateFormatter;

	private BroadcastReceiver appDataReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			X--;
			System.out.println("appDataReceiver  : " + X);
			if (X == 0) {
				progressDialog.dismiss();
			}
			setSynced();
		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();

		previousIntent = getIntent();

		createDialog = new CreateDialog(this);

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();

		gson = new Gson();
		dateFormatter = new DateFormatter();
	}

	public void findThings() {
		menu_item_TV1 = (TextView) findViewById(R.id.titleMenuItem1_TV);
		menu_item_TV2 = (TextView) findViewById(R.id.titleMenuItem2_TV);
		menu_item_TV3 = (TextView) findViewById(R.id.titleMenuItem3_TV);
		menu_item_TV4 = (TextView) findViewById(R.id.titleMenuItem4_TV);

		syncMenuItem1_TV = (TextView) findViewById(R.id.syncMenuItem1_TV);
		syncMenuItem2_TV = (TextView) findViewById(R.id.syncMenuItem2_TV);
		syncMenuItem3_TV = (TextView) findViewById(R.id.syncMenuItem3_TV);
		syncMenuItem4_TV = (TextView) findViewById(R.id.syncMenuItem4_TV);
	}

	private void setTypeface() {
		menu_item_TV1.setTypeface(myApp.getTypefaceBoldSans());
		menu_item_TV2.setTypeface(myApp.getTypefaceBoldSans());
		menu_item_TV3.setTypeface(myApp.getTypefaceBoldSans());
		menu_item_TV4.setTypeface(myApp.getTypefaceBoldSans());

		syncMenuItem1_TV.setTypeface(myApp.getTypefaceRegularSans());
		syncMenuItem2_TV.setTypeface(myApp.getTypefaceRegularSans());
		syncMenuItem3_TV.setTypeface(myApp.getTypefaceRegularSans());
		syncMenuItem4_TV.setTypeface(myApp.getTypefaceRegularSans());
	}

	private void setSynced() {
		if (sharedPreferences.contains(Constant.PREF_LAST_SYNC_DATE_ACCOUNT)) {
			syncMenuItem1_TV.setText("Synced:"
					+ sharedPreferences.getString(
							Constant.PREF_LAST_SYNC_DATE_ACCOUNT, "-"));
		}
		if (sharedPreferences.contains(Constant.PREF_LAST_SYNC_DATE_CONTACT)) {
			syncMenuItem2_TV.setText("Synced:"
					+ sharedPreferences.getString(
							Constant.PREF_LAST_SYNC_DATE_CONTACT, "-"));
		}
		if (sharedPreferences
				.contains(Constant.PREF_LAST_SYNC_DATE_APPOINTMENT)) {
			syncMenuItem3_TV.setText("Synced:"
					+ sharedPreferences.getString(
							Constant.PREF_LAST_SYNC_DATE_APPOINTMENT, "-"));
		}
		if (sharedPreferences
				.contains(Constant.PREF_LAST_SYNC_DATE_OPPORTUNITY)) {
			syncMenuItem4_TV.setText("Synced:"
					+ sharedPreferences.getString(
							Constant.PREF_LAST_SYNC_DATE_OPPORTUNITY, "-"));
		}
	}

	public void initView(String title, String title2) {
		setTypeface();
		setSynced();
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
		} else {
			fetchPreferences();
		}
	}// onCreate

	private void loadAppData() {
		progressDialog = createDialog.createProgressDialog("Account Setup",
				"This may take some time", true, null);
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

		intent = new Intent(this, CompetitorService.class);
		X++;
		startService(intent);

		intent = new Intent(this, ProductService.class);
		X++;
		startService(intent);

		intent = new Intent(this, ProfitCenterService.class);
		X++;
		startService(intent);

		intent = new Intent(this, SolutionService.class);
		X++;
		startService(intent);

		intent = new Intent(this, UserService.class);
		X++;
		startService(intent);

	}

	@SuppressWarnings("unchecked")
	private void fetchPreferences() {
		progressDialog = createDialog.createProgressDialog(
				"Fetching Preferences", "This may take some time", true, null);
		progressDialog.show();

		if (sharedPreferences.contains(Constant.PREF_LOGIN_USER_ID)) {
			myApp.setLoginUserId(sharedPreferences.getString(
					Constant.PREF_LOGIN_USER_ID, null));
		}

		if (sharedPreferences.contains(Constant.PREF_ACCOUNT_LIST)) {
			String value = sharedPreferences.getString(
					Constant.PREF_ACCOUNT_LIST, null);
			if (value != null) {
				Type listType = (Type) new TypeToken<ArrayList<Account>>() {
				}.getType();
				myApp.setAccountList(
						(List<Account>) gson.fromJson(value, listType), false,
						null);
				System.out.println("Account size  : "
						+ myApp.getAccountList().size());
			}
		}

		if (sharedPreferences.contains(Constant.PREF_APPOINTMENT_LIST)) {
			String value = sharedPreferences.getString(
					Constant.PREF_APPOINTMENT_LIST, null);
			if (value != null) {
				Type listType = (Type) new TypeToken<ArrayList<Appointment>>() {
				}.getType();
				myApp.setAppointmentList(
						(List<Appointment>) gson.fromJson(value, listType),
						false, null);
				System.out.println("Appointment size  : "
						+ myApp.getAppointmentList().size());
			}
		}

		if (sharedPreferences.contains(Constant.PREF_CONTACT_LIST)) {
			String value = sharedPreferences.getString(
					Constant.PREF_CONTACT_LIST, null);
			if (value != null) {
				Type listType = (Type) new TypeToken<ArrayList<Contact>>() {
				}.getType();
				myApp.setContactList(
						(List<Contact>) gson.fromJson(value, listType), false,
						null);
				System.out.println("Contact size  : "
						+ myApp.getContactList().size());
			}
		}

		if (sharedPreferences.contains(Constant.PREF_OPPORTUNITY_LIST)) {
			String value = sharedPreferences.getString(
					Constant.PREF_OPPORTUNITY_LIST, null);
			if (value != null) {
				Type listType = (Type) new TypeToken<ArrayList<Opportunity>>() {
				}.getType();
				myApp.setOpportunityList(
						(List<Opportunity>) gson.fromJson(value, listType),
						false, null);
				System.out.println("Opportunity size  : "
						+ myApp.getOpportunityList().size());
			}
		}

		if (sharedPreferences.contains(Constant.PREF_COMPETITOR_MAP)) {
			String value = sharedPreferences.getString(
					Constant.PREF_COMPETITOR_MAP, null);
			if (value != null) {
				Type mapType = (Type) new TypeToken<Map<String, String>>() {
				}.getType();
				myApp.setCompetitorMap(
						(Map<String, String>) gson.fromJson(value, mapType),
						false);
				System.out.println("Competitor map size  : "
						+ myApp.getCompetitorMap().size());
			}
		}
		if (sharedPreferences.contains(Constant.PREF_PRODUCT_MAP)) {
			String value = sharedPreferences.getString(
					Constant.PREF_PRODUCT_MAP, null);
			if (value != null) {
				Type mapType = (Type) new TypeToken<Map<String, String>>() {
				}.getType();
				myApp.setProductMap(
						(Map<String, String>) gson.fromJson(value, mapType),
						false);
				System.out.println("Product map size  : "
						+ myApp.getProductMap().size());
			}
		}

		if (sharedPreferences.contains(Constant.PREF_PROFIT_CENTER_MAP)) {
			String value = sharedPreferences.getString(
					Constant.PREF_PROFIT_CENTER_MAP, null);
			if (value != null) {
				Type mapType = (Type) new TypeToken<Map<String, String>>() {
				}.getType();
				myApp.setProfitCenterMap(
						(Map<String, String>) gson.fromJson(value, mapType),
						false);
				System.out.println("Profit Center map size  : "
						+ myApp.getProfitCenterMap().size());
			}
		}

		if (sharedPreferences.contains(Constant.PREF_SOLUTION_MAP)) {
			String value = sharedPreferences.getString(
					Constant.PREF_SOLUTION_MAP, null);
			if (value != null) {
				Type mapType = (Type) new TypeToken<Map<String, String>>() {
				}.getType();
				myApp.setSolutionMap(
						(Map<String, String>) gson.fromJson(value, mapType),
						false);
				System.out.println("Solution map size  : "
						+ myApp.getSolutionMap().size());
			}
		}
		if (sharedPreferences.contains(Constant.PREF_USER_MAP)) {
			String value = sharedPreferences.getString(Constant.PREF_USER_MAP,
					null);
			if (value != null) {
				Type mapType = (Type) new TypeToken<Map<String, String>>() {
				}.getType();
				myApp.setUserMap(
						(Map<String, String>) gson.fromJson(value, mapType),
						false);
				System.out.println("User map size  : "
						+ myApp.getUserMap().size());
			}
		}

		progressDialog.dismiss();
		loadUnloadedData();
	}

	private void loadUnloadedData() {
		progressDialog = createDialog.createProgressDialog(
				"Load Unloaded Data", "This may take some time", true, null);
		progressDialog.show();

		Intent intent;
		if (myApp.getOpportunityList() == null) {
			X++;
			intent = new Intent(this, OpportunityService.class);
			startService(intent);
		}
		if (myApp.getContactList() == null) {
			X++;
			intent = new Intent(this, ContactService.class);
			startService(intent);
		}
		if (myApp.getAppointmentList() == null) {
			X++;
			intent = new Intent(this, AppointmentService.class);
			startService(intent);
		}
		if (myApp.getAccountList() == null) {
			X++;
			intent = new Intent(this, AccountService.class);
			startService(intent);
		}
		if (myApp.getUserMap() == null) {
			X++;
			intent = new Intent(this, UserService.class);
			startService(intent);
		}
		if (myApp.getProductMap() == null) {
			X++;
			intent = new Intent(this, ProductService.class);
			startService(intent);
		}
		if (myApp.getProfitCenterMap() == null) {
			X++;
			intent = new Intent(this, ProfitCenterService.class);
			startService(intent);
		}
		if (myApp.getSolutionMap() == null) {
			X++;
			intent = new Intent(this, SolutionService.class);
			startService(intent);
		}
		if (myApp.getCompetitorMap() == null) {
			X++;
			intent = new Intent(this, CompetitorService.class);
			startService(intent);
		}

		if (X == 0) {
			progressDialog.dismiss();
		}
	}

	public void onSync(View view) {
		loadAppData();
	}

	public void onLogout(View view) {
		// editor.clear();
		editor.remove(Constant.PREF_IS_USER_LOGIN);
		editor.remove(Constant.PREF_LOGIN_USER_ID);
		
		editor.remove(Constant.PREF_ACCOUNT_LIST);
		editor.remove(Constant.PREF_APPOINTMENT_LIST);
		editor.remove(Constant.PREF_CONTACT_LIST);
		editor.remove(Constant.PREF_OPPORTUNITY_LIST);
		
		editor.remove(Constant.PREF_LAST_SYNC_DATE_ACCOUNT);
		editor.remove(Constant.PREF_LAST_SYNC_DATE_APPOINTMENT);
		editor.remove(Constant.PREF_LAST_SYNC_DATE_CONTACT);
		editor.remove(Constant.PREF_LAST_SYNC_DATE_OPPORTUNITY);
		
		editor.remove(Constant.PREF_USER_MAP);
		editor.remove(Constant.PREF_PRODUCT_MAP);
		editor.remove(Constant.PREF_PROFIT_CENTER_MAP);
		editor.remove(Constant.PREF_SOLUTION_MAP);
		editor.remove(Constant.PREF_COMPETITOR_MAP);
		
		editor.commit();

		nextIntent = new Intent(this, LoginActivity.class);
		nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(nextIntent);
	}

	public void onContact(View view) {
		nextIntent = new Intent(this, ContactListActivity.class);
		nextIntent.putExtra("is_my_contact", true);
		startActivity(nextIntent);
	}

	public void onAccount(View view) {
		nextIntent = new Intent(this, AccountListActivity.class);
		nextIntent.putExtra("is_my_account", true);
		startActivity(nextIntent);
	}

	public void onAppointment(View view) {
		nextIntent = new Intent(this, AppointmentListActivity.class);
		nextIntent.putExtra("is_my_appointment", true);
		startActivity(nextIntent);

	}

	public void onOpportunity(View view) {
		nextIntent = new Intent(this, OpportunityListActivity.class);
		// nextIntent = new Intent(this,
		// OpportunityAddActivity.class);
		nextIntent.putExtra("is_my_opportunity", true);
		startActivity(nextIntent);
	}

	public void onService(View view) {
		nextIntent = new Intent(this, ServiceAddActivity.class);
		startActivity(nextIntent);
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		setSynced();
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

}
