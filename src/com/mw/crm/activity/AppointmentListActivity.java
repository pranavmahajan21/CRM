package com.mw.crm.activity;

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
	TextView errorTV;
	EditText searchAppointment_ET;

	Intent nextIntent;

	// CreateDialog createDialog;
	// ProgressDialog progressDialog;

	RequestQueue queue;

	private void initThings() {

		myApp = (MyApp) getApplicationContext();
		appointmentList = myApp.getAppointmentList();

		// createDialog = new CreateDialog(this);
		// progressDialog = createDialog.createProgressDialog(
		// "Fetching Appointments", "This may take some time", true, null);

		queue = Volley.newRequestQueue(this);

		if (appointmentList != null && appointmentList.size() > 0) {
			adapter = new AppointmentAdapter(this, appointmentList);
		}

		nextIntent = new Intent(this, AppointmentAddActivity.class);
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
			// no tickets
		}
	}

	private void myOwnOnTextChangeListeners() {
		searchAppointment_ET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// On text change call filter function of Adapter
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
		setContentView(R.layout.activity_appointment_display);

		initThings();
		findThings();
		initView("Appointment", "Add");

		myOwnOnTextChangeListeners();
		// progressDialog.show();
		// JSONObject jsonObject;

		appointmentLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				nextIntent.putExtra("position", position);
				startActivity(nextIntent);
			}

		});

	}

	public void onRightButton(View view) {
		startActivity(nextIntent);
	}
}
