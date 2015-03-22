package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
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
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;
import com.mw.crm.model.Opportunity;
import com.mw.crm.service.OpportunityService;

public class OpportunityListActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	EditText search_ET;
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

	CreateDialog createDialog;
	ProgressDialog progressDialog;

	private BroadcastReceiver opportunityUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			initSubOpportunityList();

			if (subOpportunityList != null && subOpportunityList.size() > 0) {
				if (adapter == null) {
					adapter = new OpportunityAdapter(
							OpportunityListActivity.this, subOpportunityList);
				} else {
					adapter.swapData(subOpportunityList);
					adapter.notifyDataSetChanged();
				}
			}

			progressDialog.dismiss();
		}

	};

	private void initSubOpportunityList() {
		opportunityList = myApp.getOpportunityList();
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
	}

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		gson = new Gson();

		queue = Volley.newRequestQueue(this);

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Updating List",
				"This may take some time", true, null);

		// opportunityList = myApp.getOpportunityList();
		// if (opportunityList != null && opportunityList.size() > 0) {
		// if (previousIntent.getBooleanExtra("is_my_opportunity", true)) {
		// subOpportunityList = new ArrayList<Opportunity>(opportunityList);
		// } else if (!(previousIntent.getBooleanExtra("is_my_opportunity",
		// true)) && previousIntent.hasExtra("account_id")) {
		// subOpportunityList = new ArrayList<Opportunity>();
		// for (int i = 0; i < opportunityList.size(); i++) {
		// System.out.println("#$#$  : "
		// + opportunityList.get(i).getCustomerId());
		//
		// // TODO : try to remove the 2nd check from if(check1 &&
		// // check2 && check3)
		// if (opportunityList.get(i).getCustomerId() != null
		// && opportunityList.get(i).getCustomerId().length() > 0
		// && myApp.getStringIdFromStringJSON(
		// opportunityList.get(i).getCustomerId())
		// .equals(previousIntent
		// .getStringExtra("account_id"))) {
		// subOpportunityList.add(opportunityList.get(i));
		// }
		// }
		//
		// }
		// }
		initSubOpportunityList();

		if (subOpportunityList != null && subOpportunityList.size() > 0) {
			adapter = new OpportunityAdapter(this, subOpportunityList);
			// adapter2 = new OpportunitySearchAdapter(this, opportunityList);
		}
	}

	public void findThings() {
		super.findThings();
		opportunityLV = (ListView) findViewById(R.id.opportunity_LV);
		search_ET = (EditText) findViewById(R.id.search_ET);

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
		search_ET.addTextChangedListener(new TextWatcher() {

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
		} else if (!(previousIntent.getBooleanExtra("is_my_opportunity", true))
				&& previousIntent.hasExtra("account_id")) {
			String accountID = previousIntent.getStringExtra("account_id");
			Account tempAccount = myApp.getAccountById(accountID);
			initView(tempAccount.getName() + "-Opportunities", "Add");
		}
		myOwnOnTextChangeListeners();

		opportunityLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				nextIntent = new Intent(OpportunityListActivity.this,
						OpportunityDetailsActivity.class);
				nextIntent.putExtra("search_text", search_ET.getText()
						.toString());

				Opportunity tempOpportunity = subOpportunityList.get(position);
				search_ET.setText("");
				int index = myApp
						.getOpportunityIndexFromOpportunityId(tempOpportunity
								.getOpportunityId());

				nextIntent.putExtra("position", index);
				startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
			}

		});
	}

	public void onRightButton(View view) {
		nextIntent = new Intent(this, OpportunityAddActivity.class);
		nextIntent.putExtra("search_text", search_ET.getText().toString());
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	@Override
	public void onBack(View view) {
		search_ET.setText("");
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
		setResult(RESULT_OK, intent);
		super.onBack(view);
	}

	@Override
	public void onBackPressed() {
		search_ET.setText("");
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		search_ET.setText("");
		isActivityVisible = false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				opportunityUpdateReceiver);
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActivityVisible = true;
		LocalBroadcastManager.getInstance(this).registerReceiver(
				opportunityUpdateReceiver,
				new IntentFilter("opportunity_update_receiver"));

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data != null && data.hasExtra("refresh_list")
					&& data.getBooleanExtra("refresh_list", true)) {

				// opportunityList = myApp.getOpportunityList();
				//
				// adapter.swapData(opportunityList);
				// adapter.notifyDataSetChanged();
				initSubOpportunityList();

				adapter.swapData(subOpportunityList);
				adapter.notifyDataSetChanged();

			}
			if (data != null && data.hasExtra("search_text")) {
				search_ET.setText(data.getStringExtra("search_text"));
			}
		}
	}

	public void onRefresh(View view) {
		progressDialog.show();

		Intent intent = new Intent(this, OpportunityService.class);
		startService(intent);
	}
}
