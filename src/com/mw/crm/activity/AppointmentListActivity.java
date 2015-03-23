package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.mw.crm.adapter.AppointmentAdapter;
import com.mw.crm.application.MyApp;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.model.Account;
import com.mw.crm.model.Appointment;
import com.mw.crm.service.AppointmentService;

public class AppointmentListActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	ListView appointmentLV;

	AppointmentAdapter adapter;

	List<Appointment> appointmentList;
	List<Appointment> subAppointmentList;
	TextView errorTV;
	EditText search_ET;

	Intent previousIntent, nextIntent;

	RequestQueue queue;

	CreateDialog createDialog;
	ProgressDialog progressDialog;

	private BroadcastReceiver appointmentUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			initSubAppointmentList();

			if (subAppointmentList != null && subAppointmentList.size() > 0) {
				if (adapter == null) {
					adapter = new AppointmentAdapter(
							AppointmentListActivity.this, subAppointmentList);
				} else {
					adapter.swapData(subAppointmentList);
					adapter.notifyDataSetChanged();
				}
			}

			progressDialog.dismiss();

		}
	};

	private void initSubAppointmentList() {
		appointmentList = myApp.getAppointmentList();
		if (appointmentList != null && appointmentList.size() > 0) {
			if (previousIntent.hasExtra("is_my_appointment")
					&& previousIntent.getBooleanExtra("is_my_appointment",
							false)) {
				/** We are coming from MenuActivity **/
				subAppointmentList = new ArrayList<Appointment>(appointmentList);
			} else if (previousIntent.hasExtra("account_id")) {
				/** We are coming from AccountListActivity **/
				System.out.println("acc ID is : "
						+ previousIntent.getStringExtra("account_id"));
				subAppointmentList = new ArrayList<Appointment>();
				for (int i = 0; i < appointmentList.size(); i++) {
					System.out.println("#$#$  : "
							+ appointmentList.get(i).getOwner());

					// TODO : try to remove the 2nd check from if(check1 &&
					// check2 && check3)
					if (appointmentList.get(i).getAccount() != null
							&& appointmentList.get(i).getAccount().length() > 0
							&& myApp.getStringIdFromStringJSON(
									appointmentList.get(i).getAccount())
									.equals(previousIntent
											.getStringExtra("account_id"))) {
						subAppointmentList.add(appointmentList.get(i));
					}
				}

			}
		}
	}

	private void initThings() {

		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		queue = Volley.newRequestQueue(this);

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Updating List",
				"This may take some time", true, null);

		// if (appointmentList != null && appointmentList.size() > 0) {
		// if (previousIntent.hasExtra("is_my_appointment")
		// && previousIntent.getBooleanExtra("is_my_appointment",
		// false)) {
		// subAppointmentList = new ArrayList<Appointment>(appointmentList);
		// } else if (previousIntent.hasExtra("account_id")) {
		// System.out.println("acc ID is : "
		// + previousIntent.getStringExtra("account_id"));
		// subAppointmentList = new ArrayList<Appointment>();
		// for (int i = 0; i < appointmentList.size(); i++) {
		// System.out.println("#$#$  : "
		// + appointmentList.get(i).getOwner());
		//
		// // TODO : try to remove the 2nd check from if(check1 &&
		// // check2 && check3)
		// if (appointmentList.get(i).getAccount() != null
		// && appointmentList.get(i).getAccount().length() > 0
		// && myApp.getStringIdFromStringJSON(
		// appointmentList.get(i).getAccount())
		// .equals(previousIntent
		// .getStringExtra("account_id"))) {
		// subAppointmentList.add(appointmentList.get(i));
		// }
		// }
		//
		// }
		// }
		initSubAppointmentList();
		if (subAppointmentList != null && subAppointmentList.size() > 0) {
			adapter = new AppointmentAdapter(this, subAppointmentList);
		}
	}

	public void findThings() {
		super.findThings();
		appointmentLV = (ListView) findViewById(R.id.appointment_LV);
		errorTV = (TextView) findViewById(R.id.error_TV);
		search_ET = (EditText) findViewById(R.id.search_ET);
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		if (adapter != null) {
			appointmentLV.setAdapter(adapter);
		} else {
			errorTV.setVisibility(View.VISIBLE);
			errorTV.setTypeface(myApp.getTypefaceRegularSans());
		}
	}

	private void myOwnOnTextChangeListeners() {
		search_ET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (AppointmentListActivity.this.adapter == null) {
					return;
				}

				AppointmentListActivity.this.adapter.filter(cs.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_list);

		initThings();
		findThings();

		if (previousIntent.getBooleanExtra("is_my_appointment", false)) {
			initView("My Appointments", "Add");
		} else if (!(previousIntent.getBooleanExtra("is_my_appointment", true))
				&& previousIntent.hasExtra("account_id")) {
			String accountID = previousIntent.getStringExtra("account_id");
			Account tempAccount = myApp.getAccountById(accountID);
			initView(tempAccount.getName() + "-Appointments", "Add");
		}

		myOwnOnTextChangeListeners();

		appointmentLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				nextIntent = new Intent(AppointmentListActivity.this,
						AppointmentDetailsActivity.class);
				nextIntent.putExtra("search_text", search_ET.getText()
						.toString());

				Appointment tempAppointment = subAppointmentList.get(position);
				// search_ET.setText("");
				int index = myApp
						.getAppointmentIndexFromAppointmentId(tempAppointment
								.getId());

				nextIntent.putExtra("position", index);
				startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
			}

		});

	}

	public void onRightButton(View view) {
		nextIntent = new Intent(AppointmentListActivity.this,
				AppointmentAddActivity.class);
		nextIntent.putExtra("search_text", search_ET.getText().toString());
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	@Override
	public void onBack(View view) {
		search_ET.setText("");
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
		setResult(RESULT_OK, intent);
		super.onBack(view);
	}

	@Override
	public void onBackPressed() {
		search_ET.setText("");
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		search_ET.setText("");
		isActivityVisible = false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				appointmentUpdateReceiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActivityVisible = true;
		LocalBroadcastManager.getInstance(this).registerReceiver(
				appointmentUpdateReceiver,
				new IntentFilter("appointment_update_receiver"));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data != null && data.hasExtra("refresh_list")
					&& data.getBooleanExtra("refresh_list", true)) {

				// appointmentList = myApp.getAppointmentList();
				//
				// adapter.swapData(appointmentList);
				// adapter.notifyDataSetChanged();

				initSubAppointmentList();

				adapter.swapData(subAppointmentList);
				adapter.notifyDataSetChanged();

			}
			if (data != null && data.hasExtra("search_text")) {
				search_ET.setText(data.getStringExtra("search_text"));
			}
		}
	}

	public void onRefresh(View view) {
		progressDialog.show();

		Intent intent = new Intent(this, AppointmentService.class);
		startService(intent);
	}

}
