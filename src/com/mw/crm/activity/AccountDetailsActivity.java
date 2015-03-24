package com.mw.crm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.application.MyApp;
import com.mw.crm.model.Account;

public class AccountDetailsActivity extends CRMActivity {

	Intent nextIntent, previousIntent;
	Account selectedAccount;

	TextView leadPartnerLabel_TV, headquarterCountryLabel_TV, sectorLabel_TV,
			accountCategoryLabel_TV, relPartner1Label_TV, relPartner2Label_TV,
			relPartner3Label_TV, bdmLabel_TV;

	TextView leadPartner_TV, headquarterCountry_TV, sector_TV,
			accountCategory_TV, relPartner1_TV, relPartner2_TV, relPartner3_TV,
			bdm_TV;

	TextView accountName_TV;

	TextView relatedEntitiesLabel_TV;

	LinearLayout opportunityLink_LL, contactLink_LL, appointmentLink_LL;
	TextView opportunityLink_TV, contactLink_TV, appointmentLink_TV;

	private void initThings() {
		previousIntent = getIntent();
		if (previousIntent.hasExtra("account_dummy")) {
			selectedAccount = new Gson().fromJson(
					previousIntent.getStringExtra("account_dummy"),
					Account.class);
		} else {
			selectedAccount = myApp.getAccountList().get(
					previousIntent.getIntExtra("position", 0));
		}
	}

	public void findThings() {
		super.findThings();
		leadPartnerLabel_TV = (TextView) findViewById(R.id.leadPartnerLabel_TV);
		headquarterCountryLabel_TV = (TextView) findViewById(R.id.headquarterCountryLabel_TV);
		sectorLabel_TV = (TextView) findViewById(R.id.sectorLabel_TV);
		accountCategoryLabel_TV = (TextView) findViewById(R.id.accountCategoryLabel_TV);
		relPartner1Label_TV = (TextView) findViewById(R.id.relPartner1Label_TV);
		relPartner2Label_TV = (TextView) findViewById(R.id.relPartner2Label_TV);
		relPartner3Label_TV = (TextView) findViewById(R.id.relPartner3Label_TV);
		bdmLabel_TV = (TextView) findViewById(R.id.bdmLabel_TV);

		leadPartner_TV = (TextView) findViewById(R.id.leadPartner_TV);
		headquarterCountry_TV = (TextView) findViewById(R.id.headquarterCountry_TV);
		sector_TV = (TextView) findViewById(R.id.sector_TV);
		accountCategory_TV = (TextView) findViewById(R.id.accountCategory_TV);
		relPartner1_TV = (TextView) findViewById(R.id.relPartner1_TV);
		relPartner2_TV = (TextView) findViewById(R.id.relPartner2_TV);
		relPartner3_TV = (TextView) findViewById(R.id.relPartner3_TV);
		bdm_TV = (TextView) findViewById(R.id.bdm_TV);

		accountName_TV = (TextView) findViewById(R.id.accountName_TV);

		relatedEntitiesLabel_TV = (TextView) findViewById(R.id.relatedEntitiesLabel_TV);

		opportunityLink_LL = (LinearLayout) findViewById(R.id.opportunityLink_LL);
		contactLink_LL = (LinearLayout) findViewById(R.id.contactLink_LL);
		appointmentLink_LL = (LinearLayout) findViewById(R.id.appointmentLink_LL);
		
		opportunityLink_TV = (TextView) findViewById(R.id.opportunityLink_TV);
		contactLink_TV = (TextView) findViewById(R.id.contactLink_TV);
		appointmentLink_TV = (TextView) findViewById(R.id.appointmentLink_TV);
	}

	private void setTypeface() {
		leadPartnerLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		headquarterCountryLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		sectorLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		accountCategoryLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner1Label_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner2Label_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner3Label_TV.setTypeface(myApp.getTypefaceRegularSans());
		bdmLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		leadPartner_TV.setTypeface(myApp.getTypefaceRegularSans());
		headquarterCountry_TV.setTypeface(myApp.getTypefaceRegularSans());
		sector_TV.setTypeface(myApp.getTypefaceRegularSans());
		accountCategory_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner1_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner2_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner3_TV.setTypeface(myApp.getTypefaceRegularSans());
		bdm_TV.setTypeface(myApp.getTypefaceRegularSans());

		accountName_TV.setTypeface(myApp.getTypefaceRegularSans());

		relatedEntitiesLabel_TV.setTypeface(myApp.getTypefaceBoldSans());

		contactLink_TV.setTypeface(myApp.getTypefaceRegularSans());
		opportunityLink_TV.setTypeface(myApp.getTypefaceRegularSans());
		appointmentLink_TV.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String string, String string2) {
		super.initView(string, string2);
		setTypeface();

		System.out.println("acc id  : " + selectedAccount.getAccountId());

		if (previousIntent.hasExtra("account_dummy") && selectedAccount != null) {
			accountName_TV.setText(selectedAccount.getName());
			headquarterCountry_TV.setText(selectedAccount.getCountry());
			sector_TV.setText(selectedAccount.getSector());
			accountCategory_TV.setText(selectedAccount.getAccountCategory());
			leadPartner_TV.setText(selectedAccount.getLeadPartner());
			relPartner1_TV.setText(selectedAccount.getRelationshipPartner1());
			relPartner2_TV.setText(selectedAccount.getRelationshipPartner2());
			relPartner3_TV.setText(selectedAccount.getRelationshipPartner3());
			bdm_TV.setText(selectedAccount.getBusinessDevelopmentManager());
		} else {
			accountName_TV.setText(selectedAccount.getName());

			Integer temp = myApp.getIntValueFromStringJSON(selectedAccount
					.getCountry());

			if (temp != null) {
				headquarterCountry_TV.setText(myApp.getCountryMap().get(
						Integer.toString(temp.intValue())));
				temp = null;
			}
			temp = myApp.getIntValueFromStringJSON(selectedAccount.getSector());
			if (temp != null) {
				sector_TV.setText(myApp.getSectorMap().get(
						Integer.toString(temp.intValue())));
				temp = null;
			}
			temp = myApp.getIntValueFromStringJSON(selectedAccount
					.getAccountCategory());
			if (temp != null) {
				accountCategory_TV.setText(myApp.getAccountCategoryMap().get(
						Integer.toString(temp.intValue())));
				temp = null;
			}

			leadPartner_TV.setText(myApp
					.getStringNameFromStringJSON(selectedAccount
							.getLeadPartner()));
			relPartner1_TV.setText(myApp
					.getStringNameFromStringJSON(selectedAccount
							.getRelationshipPartner1()));
			relPartner2_TV.setText(myApp
					.getStringNameFromStringJSON(selectedAccount
							.getRelationshipPartner2()));
			relPartner3_TV.setText(myApp
					.getStringNameFromStringJSON(selectedAccount
							.getRelationshipPartner2()));
			bdm_TV.setText(myApp.getStringNameFromStringJSON(selectedAccount
					.getBusinessDevelopmentManager()));
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_detail);
		initThings();
		findThings();

		if (previousIntent.hasExtra("account_dummy")) {
			if (previousIntent.hasExtra("account_created")
					&& previousIntent.getBooleanExtra("account_created", false)) {
				relatedEntitiesLabel_TV.setVisibility(View.GONE);
				
				opportunityLink_LL.setVisibility(View.GONE);
				contactLink_LL.setVisibility(View.GONE);
				appointmentLink_LL.setVisibility(View.GONE);

				initView("Created Account", null);
			} else {
				initView("Updated Account", null);
			}
		} else {
			initView("Account", "Edit");
		}
	}

	public void onRightButton(View view) {
		nextIntent = new Intent(this, AccountAddActivity.class);
		nextIntent.putExtra("position",
				previousIntent.getIntExtra("position", 0));
		nextIntent.putExtra("is_edit_mode", true);
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	public void onContact(View view) {
		nextIntent = new Intent(this, ContactListActivity.class);
		nextIntent.putExtra("is_my_contact", false);
		nextIntent.putExtra("account_id", selectedAccount.getAccountId());
		startActivity(nextIntent);
	}

	public void onOpportunity(View view) {
		nextIntent = new Intent(this, OpportunityListActivity.class);
		nextIntent.putExtra("is_my_opportunity", false);
		nextIntent.putExtra("account_id", selectedAccount.getAccountId());
		startActivity(nextIntent);
	}

	public void onAppointment(View view) {
		nextIntent = new Intent(this, AppointmentListActivity.class);
		nextIntent.putExtra("is_my_appointment", false);
		nextIntent.putExtra("account_id", selectedAccount.getAccountId());
		startActivity(nextIntent);
	}

	@Override
	public void onBack(View view) {
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
		setResult(RESULT_OK, intent);
		super.onBack(view);
	}

	@Override
	public void onBackPressed() {
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent dataIntent) {
		super.onActivityResult(requestCode, resultCode, dataIntent);
		if (resultCode == RESULT_OK) {
			if (dataIntent != null) {
				if (previousIntent.hasExtra("search_text")) {
					dataIntent.putExtra("search_text",
							previousIntent.getStringExtra("search_text"));
				}
				setResult(RESULT_OK, dataIntent);
			}
			if (dataIntent.hasExtra("refresh_list")) {
				finish();
			}
		}
	}
}
