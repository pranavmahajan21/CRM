package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
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
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;
import com.mw.crm.model.Opportunity;

public class OpportunityAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	String[] temp = new String[] { "Active", "On Hold", "Delete", "Scrapped" };

	MyApp myApp;

	TextView clientNameLabel_TV, descriptionLabel_TV, statusLabel_TV, oppoManagerLabel_TV,
			probabilityLabel_TV, salesStageLabel_TV, countryLabel_TV,
			lobLabel_TV, sublobLabel_TV, sectorLabel_TV;

	TextView clientName_TV, status_TV, oppoManager_TV, probability_TV, salesStage_TV,
			country_TV, lob_TV, sublob_TV, sector_TV;

	EditText description_ET;

	RelativeLayout status_RL, oppoManager_RL, probability_RL, salesStage_RL;

	boolean pickerVisibility = false;
	// NumberPicker picker;

	Map<String, String> lobMap;
	Map<String, String> probabilityMap;
	Map<String, String> salesStageMap;
	Map<String, String> statusMap;
	Map<String, String> userMap;

	Intent previousIntent, nextIntent;

	int selectedClientName = -1, selectedStatus = -1, selectedOppoManager = -1,
			selectedProbability = -1, selectedSalesStage = -1;

	RequestQueue queue;

	private BroadcastReceiver opportunityReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			// registerForContextMenu(client_RL);
		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		lobMap = myApp.getLobMap();
		probabilityMap = myApp.getProbabilityMap();
		salesStageMap = myApp.getSalesStageMap();
		statusMap = myApp.getStatusMap();
		userMap = myApp.getUserMap();

		queue = Volley.newRequestQueue(this);
	}

	public void findThings() {
		super.findThings();

		clientNameLabel_TV = (TextView) findViewById(R.id.clientNameLabel_TV);
		descriptionLabel_TV = (TextView) findViewById(R.id.description_TV);
		statusLabel_TV = (TextView) findViewById(R.id.statusLabel_TV);
		oppoManagerLabel_TV= (TextView) findViewById(R.id.oppoManagerLabel_TV);
		probabilityLabel_TV = (TextView) findViewById(R.id.probabilityLabel_TV);
		salesStageLabel_TV = (TextView) findViewById(R.id.salesStageLabel_TV);
		countryLabel_TV = (TextView) findViewById(R.id.countryLabel_TV);
		lobLabel_TV = (TextView) findViewById(R.id.lobLabel_TV);
		sublobLabel_TV = (TextView) findViewById(R.id.sublobLabel_TV);
		sectorLabel_TV = (TextView) findViewById(R.id.sectorLabel_TV);

		clientName_TV = (TextView) findViewById(R.id.clientName_TV);
		status_TV = (TextView) findViewById(R.id.status_TV);
		oppoManager_TV= (TextView) findViewById(R.id.oppoManager_TV);
		probability_TV = (TextView) findViewById(R.id.probability_TV);
		salesStage_TV = (TextView) findViewById(R.id.salesStage_TV);
		country_TV = (TextView) findViewById(R.id.country_TV);
		lob_TV = (TextView) findViewById(R.id.lob_TV);
		sublob_TV = (TextView) findViewById(R.id.sublob_TV);
		sector_TV = (TextView) findViewById(R.id.sector_TV);

		description_ET = (EditText) findViewById(R.id.description_ET);

		status_RL = (RelativeLayout) findViewById(R.id.status_RL);
		oppoManager_RL = (RelativeLayout) findViewById(R.id.oppoManager_RL);
		probability_RL = (RelativeLayout) findViewById(R.id.probability_RL);
		salesStage_RL = (RelativeLayout) findViewById(R.id.salesStage_RL);

	}

	private void setTypeface() {
		// clientName_TV.setTypeface(myApp.getTypefaceRegularSans());
		// descriptionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// statusLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// requiredSolutions_TV.setTypeface(myApp.getTypefaceRegularSans());
		// currency_TV.setTypeface(myApp.getTypefaceRegularSans());

	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		if (previousIntent.hasExtra("position")) {
			Opportunity tempOpportunity = myApp.getOpportunityList().get(
					previousIntent.getIntExtra("position", 0));

			System.out.println(tempOpportunity.toString());

			description_ET.setText(tempOpportunity.getCustomerId());
		}

	}

	private void hideKeyboardFunctionality() {
		((RelativeLayout) findViewById(R.id.activity_opportunity_add_RL))
				.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(getCurrentFocus()
								.getWindowToken(), 0);
						return false;
					}
				});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opportunity_add);

		initThings();
		findThings();
		initView("Add Opportunity", "Submit");

		hideKeyboardFunctionality();

		// registerForContextMenu(client_RL);
		registerForContextMenu(status_RL);
		registerForContextMenu(probability_RL);
		registerForContextMenu(salesStage_RL);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.status_RL) {
			List<String> list = new ArrayList<String>(statusMap.values());
			for (int i = 0; i < list.size(); i++) {
				menu.add(0, v.getId(), i, list.get(i));
			}

		}

		if (v.getId() == R.id.probability_RL) {
			List<String> list = new ArrayList<String>(probabilityMap.values());
			for (int i = 0; i < list.size(); i++) {
				menu.add(1, v.getId(), i, list.get(i));
			}
		}

		if (v.getId() == R.id.salesStage_RL) {
			List<String> list = new ArrayList<String>(salesStageMap.values());
			for (int i = 0; i < list.size(); i++) {
				menu.add(2, v.getId(), i, list.get(i));
			}

		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getGroupId() == 0) {
			status_TV.setText(item.getTitle());
			selectedStatus = item.getOrder();
		} else if (item.getGroupId() == 1) {
			probability_TV.setText(item.getTitle());
			selectedProbability = item.getOrder();
		} else if (item.getGroupId() == 2) {
			salesStage_TV.setText(item.getTitle());
			selectedSalesStage = item.getOrder();
		}
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
			params.put("Name",
					MyApp.encryptData(clientName_TV.getText().toString()))
					.put("CstId", "da7bb1a7-8095-e411-96e8-5cf3fc3f502a")
					.put("KPMGStatus",
							new ArrayList<String>(statusMap.keySet())
									.get(selectedStatus))
					.put("Oid", "401a5d5f-9a8a-e411-96e8-5cf3fc3f502a")
					.put("salesstagecodes",
							new ArrayList<String>(salesStageMap.keySet())
									.get(selectedSalesStage))
					.put("probabilty",
							new ArrayList<String>(probabilityMap.keySet())
									.get(selectedProbability));

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

	@Override
	protected void onResume() {
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

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra("request_code", requestCode);
		super.startActivityForResult(intent, requestCode);
	}

	public void onSearchItem(View view) {
		nextIntent = new Intent(this, SearchActivity.class);

		switch (view.getId()) {
		case R.id.client_RL:
			startActivityForResult(nextIntent, MyApp.SEARCH_ACCOUNT);
			break;
		case R.id.oppoManager_RL:
			startActivityForResult(nextIntent, MyApp.SEARCH_USER);
			break;
		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			int positionItem = data.getIntExtra("position_item", 0);
			selectedClientName = positionItem;
			if (requestCode == MyApp.SEARCH_ACCOUNT) {

				Account tempAccount = null;
				tempAccount = myApp.getAccountList().get(positionItem);
				if (tempAccount != null) {
					clientName_TV.setText(tempAccount.getName());
					Integer temp = myApp.getValueFromStringJSON(tempAccount
							.getCountry());
					if (temp != null) {
						country_TV.setText(myApp.getCountryMap().get(
								Integer.toString(temp.intValue())));
					} else {
						country_TV.setText("");
					}
					temp = myApp.getValueFromStringJSON(tempAccount.getLob());
					if (temp != null) {
						lob_TV.setText(myApp.getLobMap().get(
								Integer.toString(temp.intValue())));
					} else {
						lob_TV.setText("");
					}
					temp = myApp
							.getValueFromStringJSON(tempAccount.getSubLob());
					if (temp != null) {
						sublob_TV.setText(myApp.getSubLobMap().get(
								Integer.toString(temp.intValue())));
					} else {
						sublob_TV.setText("");
					}
					temp = myApp
							.getValueFromStringJSON(tempAccount.getSector());
					if (temp != null) {
						sector_TV.setText(myApp.getSectorMap().get(
								Integer.toString(temp.intValue())));
					} else {
						sector_TV.setText("");
					}
				} else {
					Toast.makeText(this, "Account not found",
							Toast.LENGTH_SHORT).show();

				}
			}//if (requestCode == MyApp.SEARCH_ACCOUNT)
			if (requestCode == MyApp.SEARCH_USER) {
				List<String> list = new ArrayList<String>(userMap.values());
				oppoManager_TV.setText(list.get(positionItem));
				
				selectedOppoManager = positionItem;
			}
		}
	}
}
