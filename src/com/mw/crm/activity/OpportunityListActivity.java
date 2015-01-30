package com.mw.crm.activity;

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

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.adapter.OpportunityAdapter;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Opportunity;

public class OpportunityListActivity extends CRMActivity {

	MyApp myApp;

	ListView opportunityLV;
	List<Opportunity> opportunityList;
	OpportunityAdapter adapter;

	Intent nextIntent;

	EditText searchOpportunity_ET;

	RequestQueue queue;

	Gson gson;

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		opportunityList = myApp.getOpportunityList();

		gson = new Gson();

		queue = Volley.newRequestQueue(this);

		if (opportunityList != null && opportunityList.size() > 0) {
			adapter = new OpportunityAdapter(this, opportunityList);
		}
	}

	public void findThings() {
		super.findThings();
		opportunityLV = (ListView) findViewById(R.id.opportunity_LV);
		searchOpportunity_ET = (EditText) findViewById(R.id.searchOpportunity_ET);
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		if (adapter != null) {
			opportunityLV.setAdapter(adapter);
		} else {
			// no tickets
		}
	}

	private void myOwnOnTextChangeListeners() {
		searchOpportunity_ET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// On text change call filter function of Adapter
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
		initView("Opportunities", "Add");

		myOwnOnTextChangeListeners();

		opportunityLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				nextIntent = new Intent(OpportunityListActivity.this, OpportunityDetailsActivity.class);
				nextIntent.putExtra("position", position);
				startActivity(nextIntent);
			}

		});
	}

	public void onRightButton(View view) {
		nextIntent = new Intent(this, OpportunityAddActivity.class);
		startActivity(nextIntent);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data != null && data.hasExtra("refresh_list")
					&& data.getBooleanExtra("refresh_list", true)) {
				adapter.swapData(myApp.getOpportunityList());
				adapter.notifyDataSetChanged();
			}
		}
	}

}
