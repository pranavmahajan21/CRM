package com.mw.crm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;

public class AccountDetailsActivity extends CRMActivity {

	Intent nextIntent, previousIntent;
	Account selectedAccount;

	TextView leadPartnerLabel_TV, headquarterCountryLabel_TV, sectorLabel_TV,
			accountCategoryLabel_TV, relPartner1Label_TV, relPartner2Label_TV,
			relPartner3Label_TV, bdmLabel_TV;
	// lobLabel_TV,sublobLabel_TV,

	TextView leadPartner_TV, headquarterCountry_TV, sector_TV,
			accountCategory_TV, relPartner1_TV, relPartner2_TV, relPartner3_TV,
			bdm_TV;
	// lob_TV,sublob_TV,

	TextView accountName_TV;

	TextView relatedEntitiesLabel_TV;
	
	TextView contactLink_TV, opportunityLink_TV, appointmentLink_TV;

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
		// lobLabel_TV = (TextView) findViewById(R.id.lobLabel_TV);
		// sublobLabel_TV = (TextView) findViewById(R.id.sublobLabel_TV);
		sectorLabel_TV = (TextView) findViewById(R.id.sectorLabel_TV);
		accountCategoryLabel_TV = (TextView) findViewById(R.id.accountCategoryLabel_TV);
		relPartner1Label_TV = (TextView) findViewById(R.id.relPartner1Label_TV);
		relPartner2Label_TV = (TextView) findViewById(R.id.relPartner2Label_TV);
		relPartner3Label_TV = (TextView) findViewById(R.id.relPartner3Label_TV);
		bdmLabel_TV = (TextView) findViewById(R.id.bdmLabel_TV);

		leadPartner_TV = (TextView) findViewById(R.id.leadPartner_TV);
		headquarterCountry_TV = (TextView) findViewById(R.id.headquarterCountry_TV);
		// lob_TV = (TextView) findViewById(R.id.lob_TV);
		// sublob_TV = (TextView) findViewById(R.id.sublob_TV);
		sector_TV = (TextView) findViewById(R.id.sector_TV);
		accountCategory_TV = (TextView) findViewById(R.id.accountCategory_TV);
		relPartner1_TV = (TextView) findViewById(R.id.relPartner1_TV);
		relPartner2_TV = (TextView) findViewById(R.id.relPartner2_TV);
		relPartner3_TV = (TextView) findViewById(R.id.relPartner3_TV);
		bdm_TV = (TextView) findViewById(R.id.bdm_TV);

		accountName_TV = (TextView) findViewById(R.id.accountName_TV);

		relatedEntitiesLabel_TV = (TextView) findViewById(R.id.relatedEntitiesLabel_TV);
		
		contactLink_TV = (TextView) findViewById(R.id.contactLink_TV);
		opportunityLink_TV = (TextView) findViewById(R.id.opportunityLink_TV);
		appointmentLink_TV = (TextView) findViewById(R.id.appointmentLink_TV);
	}

	private void setTypeface() {
		leadPartnerLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		headquarterCountryLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// lobLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// sublobLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		sectorLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		accountCategoryLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner1Label_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner2Label_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner3Label_TV.setTypeface(myApp.getTypefaceRegularSans());
		bdmLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		leadPartner_TV.setTypeface(myApp.getTypefaceRegularSans());
		headquarterCountry_TV.setTypeface(myApp.getTypefaceRegularSans());
		// lob_TV.setTypeface(myApp.getTypefaceRegularSans());
		// sublob_TV.setTypeface(myApp.getTypefaceRegularSans());
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
			// lob_TV.setText(selectedAccount.getLob());
			// sublob_TV.setText(selectedAccount.getSubLob());
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
			// temp = myApp.getIntValueFromStringJSON(selectedAccount.getLob());
			// if (temp != null) {
			// lob_TV.setText(myApp.getLobMap().get(
			// Integer.toString(temp.intValue())));
			// temp = null;
			// }
			// temp =
			// myApp.getIntValueFromStringJSON(selectedAccount.getSubLob());
			// if (temp != null) {
			// sublob_TV.setText(myApp.getSubLobMap().get(
			// Integer.toString(temp.intValue())));
			// temp = null;
			// }
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
		// int position = rightListView.getPositionForView(view);
	}

	public void onOpportunity(View view) {
	}

	public void onAppointment(View view) {
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
