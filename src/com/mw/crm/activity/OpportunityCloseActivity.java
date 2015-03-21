package com.mw.crm.activity;

import com.example.crm.activity.R;
import com.mw.crm.extra.MyApp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class OpportunityCloseActivity extends CRMActivity {

	TextView statusReasonLabel_TV, actualReasonLabel_TV, closeDateLabel_TV,
			competitorLabel_TV, descriptionLabel_TV;

	TextView statusReason_TV, closeDate_TV, competitor_TV;
	EditText actualReason_ET, description_ET;

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
	}

	public void findThings() {
		super.findThings();

		statusReasonLabel_TV = (TextView) findViewById(R.id.statusReasonLabel_TV);
		actualReasonLabel_TV = (TextView) findViewById(R.id.actualReasonLabel_TV);
		closeDateLabel_TV = (TextView) findViewById(R.id.closeDateLabel_TV);
		competitorLabel_TV = (TextView) findViewById(R.id.competitorLabel_TV);
		descriptionLabel_TV = (TextView) findViewById(R.id.descriptionLabel_TV);

		statusReason_TV = (TextView) findViewById(R.id.statusReason_TV);
		closeDate_TV = (TextView) findViewById(R.id.closeDate_TV);
		competitor_TV = (TextView) findViewById(R.id.competitor_TV);

		actualReason_ET = (EditText) findViewById(R.id.actualReason_ET);
		description_ET = (EditText) findViewById(R.id.description_ET);
	}

	private void setTypeface() {
		statusReasonLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		actualReasonLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		closeDateLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		competitorLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		descriptionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		statusReason_TV.setTypeface(myApp.getTypefaceRegularSans());
		closeDate_TV.setTypeface(myApp.getTypefaceRegularSans());
		competitor_TV.setTypeface(myApp.getTypefaceRegularSans());

		actualReason_ET.setTypeface(myApp.getTypefaceRegularSans());
		description_ET.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opportunity_close);

		initThings();
		findThings();
		initView("Add Opportunity", "Save");
	}

}
