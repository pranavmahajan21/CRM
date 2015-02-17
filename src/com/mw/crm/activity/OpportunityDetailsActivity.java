package com.mw.crm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;
import com.mw.crm.model.Opportunity;

public class OpportunityDetailsActivity extends CRMActivity {

	Intent nextIntent, previousIntent;
	Opportunity selectedOpportunity;

	TextView oppoManagerLabel_TV, probabilityLabel_TV, statusLabel_TV,
			salesStageLabel_TV, clientNameLabel_TV, countryLabel_TV,
			lobLabel_TV, sublobLabel_TV, sectorLabel_TV;

	TextView oppoName_TV, oppoManager_TV, probability_TV, status_TV,
			salesStage_TV, clientName_TV, country_TV, lob_TV, sublob_TV,
			sector_TV;

	private void initThings() {
		myApp.getAccountList();
		previousIntent = getIntent();

		if (previousIntent.hasExtra("opportunity_dummy")) {
			selectedOpportunity = new Gson().fromJson(
					previousIntent.getStringExtra("opportunity_dummy"),
					Opportunity.class);
		} else {
			selectedOpportunity = myApp.getOpportunityList().get(
					previousIntent.getIntExtra("position", 0));
		}
	}

	public void findThings() {
		super.findThings();
		oppoManagerLabel_TV = (TextView) findViewById(R.id.oppoManagerLabel_TV);
		probabilityLabel_TV = (TextView) findViewById(R.id.probabilityLabel_TV);
		statusLabel_TV = (TextView) findViewById(R.id.statusLabel_TV);
		salesStageLabel_TV = (TextView) findViewById(R.id.salesStageLabel_TV);
		clientNameLabel_TV = (TextView) findViewById(R.id.clientNameLabel_TV);
		countryLabel_TV = (TextView) findViewById(R.id.countryLabel_TV);
		lobLabel_TV = (TextView) findViewById(R.id.lobLabel_TV);
		sublobLabel_TV = (TextView) findViewById(R.id.sublobLabel_TV);
		sectorLabel_TV = (TextView) findViewById(R.id.sectorLabel_TV);

		oppoName_TV = (TextView) findViewById(R.id.oppoName_TV);
		oppoManager_TV = (TextView) findViewById(R.id.oppoManager_TV);
		probability_TV = (TextView) findViewById(R.id.probability_TV);
		status_TV = (TextView) findViewById(R.id.status_TV);
		salesStage_TV = (TextView) findViewById(R.id.salesStage_TV);
		clientName_TV = (TextView) findViewById(R.id.clientName_TV);
		country_TV = (TextView) findViewById(R.id.country_TV);
		lob_TV = (TextView) findViewById(R.id.lob_TV);
		sublob_TV = (TextView) findViewById(R.id.sublob_TV);
		sector_TV = (TextView) findViewById(R.id.sector_TV);
	}

	private void setTypeface() {
		// myApp.getTypefaceBoldSans()
		oppoManagerLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		probabilityLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		statusLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		salesStageLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		clientNameLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		countryLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		lobLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		sublobLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		sectorLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		oppoName_TV.setTypeface(myApp.getTypefaceRegularSans());
		oppoManager_TV.setTypeface(myApp.getTypefaceRegularSans());
		probability_TV.setTypeface(myApp.getTypefaceRegularSans());
		status_TV.setTypeface(myApp.getTypefaceRegularSans());
		salesStage_TV.setTypeface(myApp.getTypefaceRegularSans());
		clientName_TV.setTypeface(myApp.getTypefaceRegularSans());
		country_TV.setTypeface(myApp.getTypefaceRegularSans());
		lob_TV.setTypeface(myApp.getTypefaceRegularSans());
		sublob_TV.setTypeface(myApp.getTypefaceRegularSans());
		sector_TV.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String string, String string2) {
		super.initView(string, string2);
		setTypeface();

		if (previousIntent.hasExtra("opportunity_dummy")
				&& selectedOpportunity != null) {
			oppoName_TV.setText(selectedOpportunity.getDescription());
			oppoManager_TV.setText(selectedOpportunity.getOwnerId());
			status_TV.setText(selectedOpportunity.getKpmgStatus());
			probability_TV.setText(selectedOpportunity.getProbability());
			salesStage_TV.setText(selectedOpportunity.getSalesStage());
			clientName_TV.setText(selectedOpportunity.getCustomerId());

			country_TV.setText(previousIntent.getStringExtra("country"));
			lob_TV.setText(previousIntent.getStringExtra("lob"));
			sublob_TV.setText(previousIntent.getStringExtra("sub_lob"));
			sector_TV.setText(previousIntent.getStringExtra("sector"));
		} else {
			oppoName_TV.setText(selectedOpportunity.getDescription());
			oppoManager_TV.setText(myApp
					.getStringNameFromStringJSON(selectedOpportunity
							.getOwnerId()));

			Integer temp2 = myApp.getIntValueFromStringJSON(selectedOpportunity
					.getKpmgStatus());
			if (temp2 != null) {
				status_TV.setText(myApp.getStatusMap().get(
						Integer.toString(temp2.intValue())));
				temp2 = null;
			}
			temp2 = myApp.getIntValueFromStringJSON(selectedOpportunity
					.getProbability());
			if (temp2 != null) {
				probability_TV.setText(myApp.getProbabilityMap().get(
						Integer.toString(temp2.intValue())));
				temp2 = null;
			}

			temp2 = myApp.getIntValueFromStringJSON(selectedOpportunity
					.getSalesStage());
			if (temp2 != null) {
				salesStage_TV.setText(myApp.getSalesStageMap().get(
						Integer.toString(temp2.intValue())));
				temp2 = null;
			}

			// try {
			// probability_TV.setText(myApp.getProbabilityMap().get(
			// Integer.toString(new JSONObject(selectedOpportunity
			// .getProbability()).getInt("Value"))));
			// status_TV.setText(myApp.getStatusMap().get(
			// Integer.toString(new JSONObject(selectedOpportunity
			// .getKpmgStatus()).getInt("Value"))));
			// salesStage_TV.setText(myApp.getSalesStageMap().get(
			// Integer.toString(new JSONObject(selectedOpportunity
			// .getSalesStage()).getInt("Value"))));
			// } catch (JSONException e) {
			// e.printStackTrace();
			// }

			clientName_TV.setText(myApp
					.getStringNameFromStringJSON(selectedOpportunity
							.getCustomerId()));

			Account tempAccount = myApp.getAccountById(myApp
					.getStringIdFromStringJSON(selectedOpportunity
							.getCustomerId()));
			if (tempAccount != null) {
				Integer temp = myApp.getIntValueFromStringJSON(tempAccount
						.getCountry());
				if (temp != null) {
					country_TV.setText(myApp.getCountryMap().get(
							Integer.toString(temp.intValue())));
					temp = null;
				}
				temp = myApp.getIntValueFromStringJSON(tempAccount.getLob());
				if (temp != null) {
					lob_TV.setText(myApp.getLobMap().get(
							Integer.toString(temp.intValue())));
					temp = null;
				}
				temp = myApp.getIntValueFromStringJSON(tempAccount.getSubLob());
				if (temp != null) {
					sublob_TV.setText(myApp.getSubLobMap().get(
							Integer.toString(temp.intValue())));
					temp = null;
				}
				temp = myApp.getIntValueFromStringJSON(tempAccount.getSector());
				if (temp != null) {
					sector_TV.setText(myApp.getSectorMap().get(
							Integer.toString(temp.intValue())));
					temp = null;
				}
			} else {
				Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT)
						.show();

			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opportunity_detail);

		initThings();
		findThings();

		if (previousIntent.hasExtra("opportunity_dummy")) {
			if (previousIntent.hasExtra("opportunity_created")
					&& previousIntent.getBooleanExtra("opportunity_created",
							false)) {
				initView("Created Opportunity", null);
			} else {
				initView("Updated Opportunity", null);
			}
		} else {
			initView("Opportunity", "Edit");
		}

	}

	public void onRightButton(View view) {
		nextIntent = new Intent(this, OpportunityAddActivity.class);
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
