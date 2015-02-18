package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
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
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Appointment;

public class AppointmentListActivity extends CRMActivity {

	MyApp myApp;

	ListView appointmentLV;

	AppointmentAdapter adapter;

	List<Appointment> appointmentList;
	List<Appointment> subAppointmentList;
	TextView errorTV;
	EditText searchAppointment_ET;

	Intent previousIntent, nextIntent;

	RequestQueue queue;

	private void initThings() {

		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();
		appointmentList = myApp.getAppointmentList();

		queue = Volley.newRequestQueue(this);

		// if (appointmentList != null && appointmentList.size() > 0) {
		// adapter = new AppointmentAdapter(this, appointmentList);
		// }
		if (appointmentList != null && appointmentList.size() > 0) {
			if (previousIntent.hasExtra("is_my_appointment")
					&& previousIntent.getBooleanExtra("is_my_appointment",
							false)) {
				subAppointmentList = new ArrayList<Appointment>(appointmentList);
			} else if (previousIntent.hasExtra("account_id")) {
				System.out.println("acc ID is : "
						+ previousIntent.getStringExtra("account_id"));
				subAppointmentList = new ArrayList<Appointment>();
				for (int i = 0; i < appointmentList.size(); i++) {
					System.out.println("#$#$  : "
							+ appointmentList.get(i).getOwnerId());

					// TODO : try to remove the 2nd check from if(check1 &&
					// check2 && check3)
					if (appointmentList.get(i).getOwnerId() != null
							&& appointmentList.get(i).getOwnerId().length() > 0
							&& myApp.getStringIdFromStringJSON(
									appointmentList.get(i).getOwnerId())
									.equals(previousIntent
											.getStringExtra("account_id"))) {
						subAppointmentList.add(appointmentList.get(i));
					}
				}

			}
		}
		if (subAppointmentList != null && subAppointmentList.size() > 0) {
			adapter = new AppointmentAdapter(this, subAppointmentList);
		}
	}

	public void findThings() {
		super.findThings();
		appointmentLV = (ListView) findViewById(R.id.appointment_LV);
		errorTV = (TextView) findViewById(R.id.error_TV);
		searchAppointment_ET = (EditText) findViewById(R.id.searchAppointment_ET);
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
		searchAppointment_ET.addTextChangedListener(new TextWatcher() {

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
		initView("My Appointments", "Add");

		myOwnOnTextChangeListeners();

		appointmentLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Appointment tempAppointment = appointmentList.get(position);
				searchAppointment_ET.setText("");
				int index = myApp
						.getAppointmentIndexFromAppointmentId(tempAppointment
								.getId());

				nextIntent = new Intent(AppointmentListActivity.this,
						AppointmentDetailsActivity.class);
				nextIntent.putExtra("position", index);
				// nextIntent.putExtra("position", position);
				startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
			}

		});

	}

	public void onRightButton(View view) {
		nextIntent = new Intent(AppointmentListActivity.this,
				AppointmentAddActivity.class);
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	@Override
	public void onBack(View view) {
		searchAppointment_ET.setText("");
		super.onBack(view);
	}

	@Override
	public void onHome(View view) {
		searchAppointment_ET.setText("");
		super.onHome(view);
	}

	@Override
	public void onBackPressed() {
		searchAppointment_ET.setText("");
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		searchAppointment_ET.setText("");
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data != null && data.hasExtra("refresh_list")
					&& data.getBooleanExtra("refresh_list", true)) {

				appointmentList = myApp.getAppointmentList();

				// adapter.swapData(myApp.getAppointmentList());
				adapter.swapData(appointmentList);
				adapter.notifyDataSetChanged();
			}
		}
	}

}
