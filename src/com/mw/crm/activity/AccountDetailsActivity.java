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
import com.mw.crm.model.Account;

public class AccountDetailsActivity extends CRMActivity {

	Intent nextIntent, previousIntent;
	Account selectedAccount;

	TextView leadPartnerLabel_TV, headquarterCountryLabel_TV, lobLabel_TV,
			sublobLabel_TV, sectorLabel_TV, accountCategoryLabel_TV;

	TextView accountName_TV, leadPartner_TV, headquarterCountry_TV, lob_TV,
			sublob_TV, sector_TV, accountCategory_TV;

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
		lobLabel_TV = (TextView) findViewById(R.id.lobLabel_TV);
		sublobLabel_TV = (TextView) findViewById(R.id.sublobLabel_TV);
		sectorLabel_TV = (TextView) findViewById(R.id.sectorLabel_TV);
		accountCategoryLabel_TV = (TextView) findViewById(R.id.accountCategoryLabel_TV);

		accountName_TV = (TextView) findViewById(R.id.accountName_TV);
		leadPartner_TV = (TextView) findViewById(R.id.leadPartner_TV);
		headquarterCountry_TV = (TextView) findViewById(R.id.headquarterCountry_TV);
		lob_TV = (TextView) findViewById(R.id.lob_TV);
		sublob_TV = (TextView) findViewById(R.id.sublob_TV);
		sector_TV = (TextView) findViewById(R.id.sector_TV);
		accountCategory_TV = (TextView) findViewById(R.id.accountCategory_TV);
	}

	private void setTypeface() {
		// myApp.getTypefaceBoldSans()

		// leadPartnerLabel_TV.setTypeface();
		// headquarterCountryLabel_TV.setTypeface();
		// lobLabel_TV.setTypeface();
		// sublobLabel_TV.setTypeface();
		// sectorLabel_TV.setTypeface();
		// accountCategoryLabel_TV.setTypeface();
		//
		// leadPartner_TV.setTypeface();
		// headquarterCountry_TV.setTypeface();
		// lob_TV.setTypeface();
		// sublob_TV.setTypeface();
		// sector_TV.setTypeface();
		// accountCategory_TV.setTypeface();
	}

	public void initView(String string, String string2) {
		super.initView(string, string2);
		setTypeface();

		if (previousIntent.hasExtra("account_dummy") && selectedAccount != null) {
			accountName_TV.setText(selectedAccount.getName());
			headquarterCountry_TV.setText(selectedAccount.getCountry());
			lob_TV.setText(selectedAccount.getLob());
			sublob_TV.setText(selectedAccount.getSubLob());
			sector_TV.setText(selectedAccount.getSector());
			accountCategory_TV.setText(selectedAccount.getAccountCategory());
			leadPartner_TV.setText(selectedAccount.getLeadPartner());
		} else {
			accountName_TV.setText(selectedAccount.getName());

			Integer temp = myApp.getIntValueFromStringJSON(selectedAccount
					.getCountry());

			if (temp != null) {
				headquarterCountry_TV.setText(myApp.getCountryMap().get(
						Integer.toString(temp.intValue())));
			}
			temp = myApp.getIntValueFromStringJSON(selectedAccount.getLob());
			if (temp != null) {
				lob_TV.setText(myApp.getLobMap().get(
						Integer.toString(temp.intValue())));
			}
			temp = myApp.getIntValueFromStringJSON(selectedAccount.getSubLob());
			if (temp != null) {
				sublob_TV.setText(myApp.getSubLobMap().get(
						Integer.toString(temp.intValue())));
			}
			temp = myApp.getIntValueFromStringJSON(selectedAccount.getSector());
			if (temp != null) {
				sector_TV.setText(myApp.getSectorMap().get(
						Integer.toString(temp.intValue())));
			}
			temp = myApp.getIntValueFromStringJSON(selectedAccount
					.getAccountCategory());
			if (temp != null) {
				accountCategory_TV.setText(myApp.getAccountCategoryMap().get(
						Integer.toString(temp.intValue())));
			}

			try {
				leadPartner_TV.setText(new JSONObject(selectedAccount
						.getLeadPartner()).getString("Name"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_detail);

		initThings();
		findThings();

		if (previousIntent.hasExtra("account_dummy")) {
			if (previousIntent.hasExtra("account_created") && previousIntent.getBooleanExtra("account_created", false)) {
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
