package com.mw.crm.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.mw.crm.model.Appointment;
import com.mw.crm.model.Opportunity;

public class OpportunityDetailsActivity extends CRMActivity {

	Intent nextIntent, previousIntent;
	Opportunity selectedOpportunity;

	TextView ownerLabel_TV, probabilityLabel_TV, statusLabel_TV,
			salesStageLabel_TV, clientNameLabel_TV, countryLabel_TV,
			corridorLabel_TV, lobLabel_TV, sublobLabel_TV, sectorLabel_TV;

	TextView owner_TV, probability_TV, status_TV, salesStage_TV, clientName_TV,
			country_TV, corridor_TV, lob_TV, sublob_TV, sector_TV;

	private void initThings() {
		myApp.getAccountList();
		previousIntent = getIntent();
		
		selectedOpportunity = myApp.getOpportunityList().get(
				previousIntent.getIntExtra("position", 0));
	}

	public void findThings() {
		super.findThings();
		ownerLabel_TV = (TextView) findViewById(R.id.ownerLabel_TV);
		probabilityLabel_TV = (TextView) findViewById(R.id.probabilityLabel_TV);
		statusLabel_TV = (TextView) findViewById(R.id.statusLabel_TV);
		salesStageLabel_TV = (TextView) findViewById(R.id.salesStageLabel_TV);
		clientNameLabel_TV = (TextView) findViewById(R.id.clientNameLabel_TV);
		countryLabel_TV = (TextView) findViewById(R.id.countryLabel_TV);
		corridorLabel_TV = (TextView) findViewById(R.id.corridorLabel_TV);
		lobLabel_TV = (TextView) findViewById(R.id.lobLabel_TV);
		sublobLabel_TV = (TextView) findViewById(R.id.sublobLabel_TV);
		sectorLabel_TV = (TextView) findViewById(R.id.sectorLabel_TV);

		owner_TV = (TextView) findViewById(R.id.owner_TV);
		probability_TV = (TextView) findViewById(R.id.probability_TV);
		status_TV = (TextView) findViewById(R.id.status_TV);
		salesStage_TV = (TextView) findViewById(R.id.salesStage_TV);
		clientName_TV = (TextView) findViewById(R.id.clientName_TV);
		country_TV = (TextView) findViewById(R.id.country_TV);
		corridor_TV = (TextView) findViewById(R.id.corridor_TV);
		lob_TV = (TextView) findViewById(R.id.lob_TV);
		sublob_TV = (TextView) findViewById(R.id.sublob_TV);
		sector_TV = (TextView) findViewById(R.id.sector_TV);
	}

	private void setTypeface() {
		// myApp.getTypefaceBoldSans()

		// ownerLabel_TV.setTypeface();
		// probabilityLabel_TV.setTypeface();
		// statusLabel_TV.setTypeface();
		// salesStageLabel_TV.setTypeface();
		// clientNameLabel_TV.setTypeface();
		// countryLabel_TV.setTypeface();
		// corridorLabel_TV.setTypeface();
		// lobLabel_TV.setTypeface();
		// sublobLabel_TV.setTypeface();
		// sectorLabel_TV.setTypeface();
		//
		// owner_TV.setTypeface();
		// probability_TV.setTypeface();
		// status_TV.setTypeface();
		// salesStage_TV.setTypeface();
		// clientName_TV.setTypeface();
		// country_TV.setTypeface();
		// corridor_TV.setTypeface();
		// lob_TV.setTypeface();
		// sublob_TV.setTypeface();
		// sector_TV.setTypeface();
	}

	public void initView(String string, String string2) {
		super.initView(string, string2);
		setTypeface();
		
//		probability_TV.setText(myApp.getProbabilityMap().get(
//				Integer.toString(new JSONObject(selectedAppointment
//						.getTypeOfMeeting()).getInt("Value"))));
//		probability_TV.setText(selectedOpportunity.getProbability());
//		salesStage_TV.setText(selectedOpportunity.getSalesStage());
		
		try {
			probability_TV.setText(myApp.getProbabilityMap().get(
					Integer.toString(new JSONObject(selectedOpportunity
							.getProbability()).getInt("Value"))));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
//		status_TV.setText(selectedOpportunity.getKpmgStatus());
		
		try {
			status_TV.setText(myApp.getStatusMap().get(
					Integer.toString(new JSONObject(selectedOpportunity
							.getKpmgStatus()).getInt("Value"))));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		try {
			salesStage_TV.setText(myApp.getSalesStageMap().get(
					Integer.toString(new JSONObject(selectedOpportunity
							.getSalesStage()).getInt("Value"))));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		clientName_TV.setText(selectedOpportunity.getCustomerId());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opportunity_detail);
		
		initThings();
		findThings();
		initView("Opportunity", "Edit");
	}

	public void onEdit(View view) {
		nextIntent = new Intent(this, OpportunityAddActivity.class);
		nextIntent.putExtra("position",
				previousIntent.getIntExtra("position", 0));
		nextIntent.putExtra("is_edit_mode", true);
		startActivity(nextIntent);
	}

}
