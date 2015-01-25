package com.mw.crm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.crm.activity.R;

public class AppointmentDetailsActivity extends CRMActivity {

	Intent nextIntent, previousIntent;

	TextView purposeLabel_TV, interactionTypeLabel_TV, dateMeetingLabel_TV,
			endTimeLabel_TV, ownerLabel_TV;

	TextView purpose_TV, interactionType_TV,
			dateMeeting_TV, endTime_TV, owner_TV;

	private void initThings() {
		myApp.getAccountList();
		previousIntent = getIntent();
	}

	public void findThings() {
		super.findThings();
		purposeLabel_TV = (TextView) findViewById(R.id.purposeLabel_TV);
		interactionTypeLabel_TV = (TextView) findViewById(R.id.interactionTypeLabel_TV);
		dateMeetingLabel_TV = (TextView) findViewById(R.id.dateMeetingLabel_TV);
		endTimeLabel_TV = (TextView) findViewById(R.id.endTimeLabel_TV);
		ownerLabel_TV = (TextView) findViewById(R.id.ownerLabel_TV);

		purpose_TV = (TextView) findViewById(R.id.email_TV);
		interactionType_TV = (TextView) findViewById(R.id.officePhone_TV);
		dateMeeting_TV = (TextView) findViewById(R.id.mobile_TV);
		endTime_TV = (TextView) findViewById(R.id.internalConnect_TV);
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

	public void initView(String string, String string2) {
		super.initView(string, string2);
		setTypeface();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_detail);
		initThings();
		findThings();
		initView("Appointment", "Edit");
	}

	public void onEdit(View view) {
		nextIntent = new Intent(this, AppointmentAddActivity.class);
		nextIntent.putExtra("position",
				previousIntent.getIntExtra("position", 0));
		nextIntent.putExtra("is_edit_mode", true);
		startActivity(nextIntent);
	}

}
