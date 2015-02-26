package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.adapter.OpportunityAdapter;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;
import com.mw.crm.model.Opportunity;

public class OpportunityListActivity extends CRMActivity {

	MyApp myApp;

	EditText searchOpportunity_ET;
	// AutoCompleteTextView searchOpportunity_ET;
	TextView errorTV;

	ListView opportunityLV;
	List<Opportunity> opportunityList;
	List<Opportunity> subOpportunityList;
	OpportunityAdapter adapter;
	// OpportunitySearchAdapter adapter2;

	Intent previousIntent, nextIntent;

	RequestQueue queue;

	Gson gson;

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();
		opportunityList = myApp.getOpportunityList();

		gson = new Gson();

		queue = Volley.newRequestQueue(this);

		if (opportunityList != null && opportunityList.size() > 0) {
			if (previousIntent.getBooleanExtra("is_my_opportunity", true)) {
				subOpportunityList = new ArrayList<Opportunity>(opportunityList);
			} else if (!(previousIntent.getBooleanExtra("is_my_opportunity",
					true)) && previousIntent.hasExtra("account_id")) {
				subOpportunityList = new ArrayList<Opportunity>();
				for (int i = 0; i < opportunityList.size(); i++) {
					System.out.println("#$#$  : "
							+ opportunityList.get(i).getCustomerId());

					// TODO : try to remove the 2nd check from if(check1 &&
					// check2 && check3)
					if (opportunityList.get(i).getCustomerId() != null
							&& opportunityList.get(i).getCustomerId().length() > 0
							&& myApp.getStringIdFromStringJSON(
									opportunityList.get(i).getCustomerId())
									.equals(previousIntent
											.getStringExtra("account_id"))) {
						subOpportunityList.add(opportunityList.get(i));
					}
				}

			}
		}
		if (subOpportunityList != null && subOpportunityList.size() > 0) {
			adapter = new OpportunityAdapter(this, subOpportunityList);
			// adapter2 = new OpportunitySearchAdapter(this, opportunityList);
		}
	}

	public void findThings() {
		super.findThings();
		opportunityLV = (ListView) findViewById(R.id.opportunity_LV);
		searchOpportunity_ET = (EditText) findViewById(R.id.searchOpportunity_ET);

		// searchOpportunity_ET.setThreshold(1);
		errorTV = (TextView) findViewById(R.id.error_TV);
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		if (adapter != null) {
			opportunityLV.setAdapter(adapter);
			// searchOpportunity_ET.setAdapter(adapter2);
		} else {
			errorTV.setVisibility(View.VISIBLE);
			errorTV.setTypeface(myApp.getTypefaceRegularSans());
		}
	}

	private void myOwnOnTextChangeListeners() {
		searchOpportunity_ET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (OpportunityListActivity.this.adapter == null) {
					return;
				}

				OpportunityListActivity.this.adapter.filter(cs.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opportunity_list);

		initThings();
		findThings();

		if (previousIntent.getBooleanExtra("is_my_opportunity", false)) {
			initView("My Opportunities", "Add");
		} else if(!(previousIntent.getBooleanExtra("is_my_opportunity",
				true)) && previousIntent.hasExtra("account_id")){
			String accountID = previousIntent.getStringExtra("account_id");
			Account tempAccount = myApp.getAccountById(accountID);
			initView(tempAccount.getName() + "My Opportunities", "Add");
		}
		myOwnOnTextChangeListeners();

		opportunityLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Opportunity tempOpportunity = opportunityList.get(position);
				searchOpportunity_ET.setText("");
				int index = myApp
						.getOpportunityIndexFromOpportunityId(tempOpportunity
								.getOpportunityId());

				nextIntent = new Intent(OpportunityListActivity.this,
						OpportunityDetailsActivity.class);
				nextIntent.putExtra("position", index);
				// nextIntent.putExtra("position", position);
				startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
			}

		});
	}

	public void onRightButton(View view) {
		nextIntent = new Intent(this, OpportunityAddActivity.class);
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	@Override
	public void onBack(View view) {
		searchOpportunity_ET.setText("");
		super.onBack(view);
	}

	@Override
	public void onHome(View view) {
		searchOpportunity_ET.setText("");
		super.onHome(view);
	}

	@Override
	public void onBackPressed() {
		searchOpportunity_ET.setText("");
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		searchOpportunity_ET.setText("");
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data != null && data.hasExtra("refresh_list")
					&& data.getBooleanExtra("refresh_list", true)) {

				opportunityList = myApp.getOpportunityList();

				// adapter.swapData(myApp.getOpportunityList());
				adapter.swapData(opportunityList);
				adapter.notifyDataSetChanged();
			}
		}
	}

}
