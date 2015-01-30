package com.mw.crm.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Appointment;

public class AppointmentDetailsActivity extends CRMActivity {

	Intent nextIntent, previousIntent;
	Appointment selectedAppointment;

	TextView purposeLabel_TV, interactionTypeLabel_TV, dateMeetingLabel_TV,
			endTimeLabel_TV, ownerLabel_TV, detailsDiscussionLabel_TV;

	TextView description_TV, nameClientOfficial_TV,
			designationClientOfficial_TV, purpose_TV, interactionType_TV,
			dateMeeting_TV, endTime_TV, owner_TV;

	private void initThings() {
		previousIntent = getIntent();
		System.out.println("position"
				+ previousIntent.getIntExtra("position", 0));
		if (previousIntent.hasExtra("appointment_dummy")) {
			selectedAppointment = new Gson().fromJson(
					previousIntent.getStringExtra("appointment_dummy"),
					Appointment.class);
		} else {
			selectedAppointment = myApp.getAppointmentList().get(
					previousIntent.getIntExtra("position", 0));
		}
	}

	public void findThings() {
		super.findThings();
		purposeLabel_TV = (TextView) findViewById(R.id.purposeLabel_TV);
		interactionTypeLabel_TV = (TextView) findViewById(R.id.interactionTypeLabel_TV);
		dateMeetingLabel_TV = (TextView) findViewById(R.id.dateMeetingLabel_TV);
		endTimeLabel_TV = (TextView) findViewById(R.id.endTimeLabel_TV);
		ownerLabel_TV = (TextView) findViewById(R.id.ownerLabel_TV);
		detailsDiscussionLabel_TV = (TextView) findViewById(R.id.detailsDiscussionLabel_TV);

		description_TV = (TextView) findViewById(R.id.description_TV);
		nameClientOfficial_TV = (TextView) findViewById(R.id.nameClientOfficial_TV);
		designationClientOfficial_TV = (TextView) findViewById(R.id.designationClientOfficial_TV);
		purpose_TV = (TextView) findViewById(R.id.purpose_TV);
		interactionType_TV = (TextView) findViewById(R.id.interactionType_TV);
		dateMeeting_TV = (TextView) findViewById(R.id.dateMeeting_TV);
		endTime_TV = (TextView) findViewById(R.id.endTime_TV);
		owner_TV = (TextView) findViewById(R.id.owner_TV);
	}

	private void setTypeface() {

		purposeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		interactionTypeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		dateMeetingLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		endTimeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		ownerLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		detailsDiscussionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		description_TV.setTypeface(myApp.getTypefaceRegularSans());
		nameClientOfficial_TV.setTypeface(myApp.getTypefaceRegularSans());
		designationClientOfficial_TV
				.setTypeface(myApp.getTypefaceRegularSans());
		purpose_TV.setTypeface(myApp.getTypefaceRegularSans());
		interactionType_TV.setTypeface(myApp.getTypefaceRegularSans());
		dateMeeting_TV.setTypeface(myApp.getTypefaceRegularSans());
		endTime_TV.setTypeface(myApp.getTypefaceRegularSans());
		owner_TV.setTypeface(myApp.getTypefaceRegularSans());
	}

	private void initWithoutMappingFields() {
		description_TV.setText(selectedAppointment.getDescription());
		nameClientOfficial_TV.setText(selectedAppointment
				.getNameOfTheClientOfficial());
		designationClientOfficial_TV.setText(selectedAppointment
				.getDesignationOfClientOfficial());
		purpose_TV.setText(selectedAppointment.getPurposeOfMeeting());

		dateMeeting_TV.setText(myApp.formatDateToString2(selectedAppointment
				.getStartTime()));
		endTime_TV.setText(myApp.formatDateToString2(selectedAppointment
				.getEndTime()));

	}

	public void initView(String string, String string2) {
		super.initView(string, string2);
		setTypeface();

		if (previousIntent.hasExtra("appointment_dummy")
				&& selectedAppointment != null) {
			initWithoutMappingFields();
			interactionType_TV.setText(selectedAppointment.getTypeOfMeeting());
			owner_TV.setText("CRM Audit Director 1");
		} else {
			initWithoutMappingFields();

			try {
				interactionType_TV.setText(myApp.getInteractionTypeMap().get(
						Integer.toString(new JSONObject(selectedAppointment
								.getTypeOfMeeting()).getInt("Value"))));
			} catch (JSONException e) {
				e.printStackTrace();
			}

			try {
				owner_TV.setText(new JSONObject(selectedAppointment
						.getOwnerId()).getString("Name"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_detail);
		initThings();
		findThings();

		if (previousIntent.hasExtra("appointment_dummy")) {
			if (previousIntent.hasExtra("appointment_created")
					&& previousIntent.getBooleanExtra("appointment_created",
							false)) {
				initView("Created Appointment", null);
			} else {
				initView("Updated Appointment", null);
			}
		} else {
			initView("Appointment", "Edit");
		}
	}

	public void onRightButton(View view) {
		nextIntent = new Intent(this, AppointmentAddActivity.class);
		nextIntent.putExtra("position",
				previousIntent.getIntExtra("position", 0));
		nextIntent.putExtra("is_edit_mode", true);
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	@Override
	public void onBack(View view) {
		setResult(RESULT_OK);
		super.onBack(view);
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data != null) {
				setResult(RESULT_OK, data);
			}
			finish();
		}
	}

}
