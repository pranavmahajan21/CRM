package com.mw.crm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.crm.activity.R;

public class AccountDetailsActivity extends CRMActivity {

	Intent nextIntent, previousIntent;

	TextView leadPartnerLabel_TV, headquarterCountryLabel_TV, lobLabel_TV,
			sublobLabel_TV, sectorLabel_TV, accountCategoryLabel_TV;

	TextView leadPartner_TV, headquarterCountry_TV, lob_TV, sublob_TV,
			sector_TV, accountCategory_TV;

	private void initThings() {
//		myApp.getAccountList();
		previousIntent = getIntent();
	}

	public void findThings() {
		super.findThings();
		leadPartnerLabel_TV = (TextView) findViewById(R.id.leadPartnerLabel_TV);
		headquarterCountryLabel_TV = (TextView) findViewById(R.id.headquarterCountryLabel_TV);
		lobLabel_TV = (TextView) findViewById(R.id.lobLabel_TV);
		sublobLabel_TV = (TextView) findViewById(R.id.sublobLabel_TV);
		sectorLabel_TV = (TextView) findViewById(R.id.sectorLabel_TV);
		accountCategoryLabel_TV = (TextView) findViewById(R.id.accountCategoryLabel_TV);

		leadPartner_TV = (TextView) findViewById(R.id.leadPartner_TV);
		headquarterCountry_TV = (TextView) findViewById(R.id.headquarterCountry_TV);
		lob_TV = (TextView) findViewById(R.id.lob_TV);
		sublob_TV = (TextView) findViewById(R.id.sublob_TV);
		sector_TV = (TextView) findViewById(R.id.sector_TV);
		accountCategory_TV = (TextView) findViewById(R.id.accountCategory_TV);
	}

	private void setTypeface() {
		// myApp.getTypefaceBoldSans()

//		 leadPartnerLabel_TV.setTypeface();
//		 headquarterCountryLabel_TV.setTypeface();
//		 lobLabel_TV.setTypeface();
//		 sublobLabel_TV.setTypeface();
//		 sectorLabel_TV.setTypeface();
//		 accountCategoryLabel_TV.setTypeface();
//		 
//		 leadPartner_TV.setTypeface();
//		 headquarterCountry_TV.setTypeface();
//		 lob_TV.setTypeface();
//		 sublob_TV.setTypeface();
//		 sector_TV.setTypeface();
//		 accountCategory_TV.setTypeface();
	}

	public void initView(String string, String string2) {
		super.initView(string, string2);
		setTypeface();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_detail);

		initThings();
		findThings();
		initView("Account", "Edit");
	}

	public void onEdit(View view) {
		nextIntent = new Intent(this, AccountAddActivity.class);
		nextIntent.putExtra("position",
				previousIntent.getIntExtra("position", 0));
		nextIntent.putExtra("is_edit_mode", true);
		startActivity(nextIntent);
	}

}
