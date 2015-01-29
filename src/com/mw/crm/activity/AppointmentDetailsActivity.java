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
			endTimeLabel_TV, ownerLabel_TV;

	TextView description_TV, nameClientOfficial_TV,
			designationClientOfficial_TV, purpose_TV, interactionType_TV,
			dateMeeting_TV, endTime_TV, owner_TV;

	private void initThings() {
		previousIntent = getIntent();
		System.out.println("position"
				+ previousIntent.getIntExtra("position", 0));
		if (previousIntent.hasExtra("contact_dummy")) {
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
		// myApp.getTypefaceBoldSans()

		// purposeLabel_TV.setTypeface();
		// interactionTypeLabel_TV.setTypeface();
		// dateMeetingLabel_TV.setTypeface();
		// endTimeLabel_TV.setTypeface();
		// ownerLabel_TV.setTypeface();
		//
		// purpose_TV.setTypeface();
		// interactionType_TV.setTypeface();
		// dateMeeting_TV.setTypeface();
		// endTime_TV.setTypeface();
		// owner_TV.setTypeface();
	}

	private void initWithoutMappingFields() {
		description_TV.setText(selectedAppointment.getDescription());
		nameClientOfficial_TV.setText(selectedAppointment
				.getNameOfTheClientOfficial());
		designationClientOfficial_TV.setText(selectedAppointment
				.getDesignationOfClientOfficial());
		purpose_TV.setText(selectedAppointment.getPurposeOfMeeting());
		
		// dateMeeting_TV.setText(selectedAppointment.getStartTime().toString());
		// endTime_TV.setText(selectedAppointment.getEndTime().toString());

	}

	public void initView(String string, String string2) {
		super.initView(string, string2);
		setTypeface();

		if (previousIntent.hasExtra("appointment_dummy")
				&& selectedAppointment != null) {
			initWithoutMappingFields();
			interactionType_TV.setText(selectedAppointment.getTypeOfMeeting());
			owner_TV.setText(selectedAppointment.getOwnerId());
		} else {
			// description_TV.setText(selectedAppointment.getDescription());
			// nameClientOfficial_TV.setText(selectedAppointment
			// .getNameOfTheClientOfficial());
			// designationClientOfficial_TV.setText(selectedAppointment
			// .getDesignationOfClientOfficial());
			// purpose_TV.setText(selectedAppointment.getPurposeOfMeeting());
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
			initView("Appointment Created", null);
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
		setResult(RESULT_OK, new Intent());
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			finish();
		}
	}

}
