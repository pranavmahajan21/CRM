package com.mw.crm.activity;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Opportunity;

public class OpportunityAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	String[] temp = new String[] { "Active", "On Hold", "Delete", "Scrapped" };

	MyApp myApp;

	TextView clientNameLabel_TV, descriptionLabel_TV, statusLabel_TV,
			requiredSolutions_TV, currency_TV, probabilityLabel_TV, salesStageLabel_TV;
	
	TextView clientName_TV, probability_TV, salesStage_TV;
	
	EditText description_ET, requiredSolutions_ET, currency_ET;
	
	RelativeLayout clientRL, probability_RL, salesStage_RL;

	boolean pickerVisibility = false;
	NumberPicker picker;

	Intent previousIntent;

	RequestQueue queue;

	private BroadcastReceiver opportunityReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			registerForContextMenu(clientRL);
		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		queue = Volley.newRequestQueue(this);
	}

	public void findThings() {
		super.findThings();

		descriptionLabel_TV = (TextView) findViewById(R.id.description_TV);
		clientNameLabel_TV = (TextView) findViewById(R.id.clientNameLabel_TV);
		statusLabel_TV = (TextView) findViewById(R.id.statusLabel_TV);
		probabilityLabel_TV = (TextView) findViewById(R.id.probabilityLabel_TV);
		salesStageLabel_TV = (TextView) findViewById(R.id.salesStageLabel_TV);
		
		clientName_TV = (TextView) findViewById(R.id.clientName_TV);
		requiredSolutions_TV = (TextView) findViewById(R.id.requiredSolutions_TV);
		currency_TV = (TextView) findViewById(R.id.currency_TV);
		probability_TV = (TextView) findViewById(R.id.probability_TV);
		salesStage_TV = (TextView) findViewById(R.id.salesStage_TV);
		
		description_ET = (EditText) findViewById(R.id.description_ET);
		requiredSolutions_ET = (EditText) findViewById(R.id.requiredSolutions_ET);
		currency_ET = (EditText) findViewById(R.id.currency_ET);

		clientRL = (RelativeLayout) findViewById(R.id.client_RL);
		probability_RL = (RelativeLayout) findViewById(R.id.probability_RL);
		salesStage_RL = (RelativeLayout) findViewById(R.id.salesStage_RL);
		
		picker = (NumberPicker) findViewById(R.id.status_NP);

		picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		picker.setMinValue(0);
		picker.setMaxValue(3);
		// picker.setDisplayedValues(new String[] { "A", "B", "C", "D" });
		picker.setDisplayedValues(temp);
	}

	private void setTypeface() {
		clientName_TV.setTypeface(myApp.getTypefaceRegularSans());
		descriptionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		statusLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		requiredSolutions_TV.setTypeface(myApp.getTypefaceRegularSans());
		currency_TV.setTypeface(myApp.getTypefaceRegularSans());

	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		if (previousIntent.hasExtra("position")) {
			Opportunity tempOpportunity = myApp.getOpportunityList().get(
					previousIntent.getIntExtra("position", 0));

			System.out.println(tempOpportunity.toString());

			description_ET.setText(tempOpportunity.getCustomerId());
			// requiredSolutions_ET.setText(tempOpportunity.getNameFirst());
			// currency_ET.setText(tempOpportunity.getNameFirst());
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opportunity_add);

		initThings();
		findThings();
		initView("Add Opportunity", "Submit");

		registerForContextMenu(clientRL);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		menu.add(0, v.getId(), 0, "South Asia Clean Energy Fund Partners, L.p");
		menu.add(0, v.getId(), 0, "(n)Code Solutions");
		menu.add(0, v.getId(), 0, "1 MG");
		menu.add(0, v.getId(), 0, "10 MG Road");
		menu.add(0, v.getId(), 0, "10C India Internet India Private Limited");
		menu.add(0, v.getId(), 0, "10C India Internet Pve. Ltd.");
		menu.add(0, v.getId(), 0, "11210");
		menu.add(0, v.getId(), 0, "20 Media Collective Private Limited");
		menu.add(0, v.getId(), 0, "1FB Support Services Private Limited");
		menu.add(0, v.getId(), 0, "2 Degrees");
		menu.add(0, v.getId(), 0, "20 Cube Group");
		menu.add(0, v.getId(), 0, "20 Cube Group");
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		clientNameLabel_TV.setText(item.getTitle());
		return super.onContextItemSelected(item);

	}

	public void onOpenContextMenu(View view) {
		openContextMenu(view);
	}

	public void onRightButton(View view) {
		try {

			String url = MyApp.URL + MyApp.OPPORTUNITY_ADD;

			System.out.println("URL : " + url);

			JSONObject params = new JSONObject();
			params.put("Name", "Hello World")
					.put("CstId", "da7bb1a7-8095-e411-96e8-5cf3fc3f502a")
					.put("KPMGStatus", "798330000")
					.put("Oid", "401a5d5f-9a8a-e411-96e8-5cf3fc3f502a")
					.put("salesstagecodes", "1").put("probabilty", "1");

			params = MyApp.addParamToJson(params);

			System.out.println("json" + params);

			JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
					Method.POST, url, params,
					new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							System.out.println("length2" + response);

						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println("ERROR  : " + error.getMessage());
							error.printStackTrace();

							if (error instanceof NetworkError) {
								System.out.println("NetworkError");
							}
							if (error instanceof NoConnectionError) {
								System.out
										.println("NoConnectionError you are now offline.");
							}
							if (error instanceof ServerError) {
								System.out.println("ServerError");
							}
						}
					});

			RetryPolicy policy = new DefaultRetryPolicy(30000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			jsonArrayRequest.setRetryPolicy(policy);
			queue.add(jsonArrayRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onShowPicker(View view) {
		pickerVisibility = !pickerVisibility;
		if (pickerVisibility) {
			picker.setVisibility(View.VISIBLE);
		} else {
			picker.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isActivityVisible = true;
		LocalBroadcastManager.getInstance(this).registerReceiver(
				opportunityReceiver, new IntentFilter("owner_data"));

	}

	@Override
	protected void onPause() {
		isActivityVisible = false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				opportunityReceiver);
		super.onPause();
	}

}
