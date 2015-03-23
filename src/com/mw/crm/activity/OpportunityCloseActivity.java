package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.mw.crm.application.MyApp;
import com.mw.crm.extra.Constant;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.DateFormatter;
import com.mw.crm.model.Opportunity;
import com.mw.crm.service.OpportunityService;

public class OpportunityCloseActivity extends CRMActivity {

	public static boolean isActivityVisible = false;
	
	TextView statusReasonLabel_TV, actualValueLabel_TV, closeDateLabel_TV,
			competitorLabel_TV, descriptionLabel_TV;

	TextView statusReason_TV, actualValue_TV, closeDate_TV, competitor_TV;
	EditText description_ET;

	RelativeLayout statusReason_RL, competitor_RL;

	RadioGroup status_RG;

	int selectedStatusReason = -1, selectedCompetitor = -1;

	Opportunity selectedOpportunity;

	Intent nextIntent, previousIntent;

	boolean isWon;

	DateFormatter dateFormatter;

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	Map<String, String> competitorMap;
	Map<String, String> opportunityLostMap;
	Map<String, String> opportunityWonMap;

	RequestQueue queue;

	private BroadcastReceiver opportunityUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			progressDialog.dismiss();
			intent.putExtra("status", ((RadioButton) findViewById(status_RG
					.getCheckedRadioButtonId())).getText().toString());
			OpportunityCloseActivity.this.setResult(RESULT_OK, intent);
			finish();
		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();

		previousIntent = getIntent();

		selectedOpportunity = myApp.getOpportunityList().get(
				previousIntent.getIntExtra("position", 0));

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog(
				"Closing Opportunity", "This may take some time", true, null);

		competitorMap = myApp.getCompetitorMap();
		opportunityLostMap = myApp.getOpportunityLostMap();
		opportunityWonMap = myApp.getOpportunityWonMap();

		queue = Volley.newRequestQueue(this);
		dateFormatter = new DateFormatter();
	}

	public void findThings() {
		super.findThings();

		statusReasonLabel_TV = (TextView) findViewById(R.id.statusReasonLabel_TV);
		actualValueLabel_TV = (TextView) findViewById(R.id.actualValueLabel_TV);
		closeDateLabel_TV = (TextView) findViewById(R.id.closeDateLabel_TV);
		competitorLabel_TV = (TextView) findViewById(R.id.competitorLabel_TV);
		descriptionLabel_TV = (TextView) findViewById(R.id.descriptionLabel_TV);

		statusReason_TV = (TextView) findViewById(R.id.statusReason_TV);
		actualValue_TV = (TextView) findViewById(R.id.actualValue_TV);
		closeDate_TV = (TextView) findViewById(R.id.closeDate_TV);
		competitor_TV = (TextView) findViewById(R.id.competitor_TV);

		description_ET = (EditText) findViewById(R.id.description_ET);

		statusReason_RL = (RelativeLayout) findViewById(R.id.statusReason_RL);
		competitor_RL = (RelativeLayout) findViewById(R.id.competitor_RL);

		status_RG = (RadioGroup) findViewById(R.id.status_RG);
	}

	private void setTypeface() {
		statusReasonLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		actualValue_TV.setTypeface(myApp.getTypefaceRegularSans());
		closeDateLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		competitorLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		descriptionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		statusReason_TV.setTypeface(myApp.getTypefaceRegularSans());
		closeDate_TV.setTypeface(myApp.getTypefaceRegularSans());
		competitor_TV.setTypeface(myApp.getTypefaceRegularSans());

		actualValueLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		description_ET.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		actualValue_TV.setText(myApp
				.getDoubleValueFromStringJSON(selectedOpportunity
						.getTotalProposalValue())
				+ "");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opportunity_close);

		initThings();
		findThings();
		initView("Close Opportunity", "Save");

		onStatusSelected(statusReason_RL);
	}

	public void onOpenContextMenu(View view) {
		hideKeyboard(this.getCurrentFocus());
		if (view.getId() == R.id.competitor_RL
				&& Boolean
						.parseBoolean((String) ((RadioButton) findViewById(status_RG
								.getCheckedRadioButtonId())).getTag())) {
			return;
		}
		openContextMenu(view);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra("request_code", requestCode);
		super.startActivityForResult(intent, requestCode);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.statusReason_RL) {
			boolean flag = Boolean
					.parseBoolean((String) ((RadioButton) findViewById(status_RG
							.getCheckedRadioButtonId())).getTag());
			List<String> list = null;
			if (flag) {
				list = new ArrayList<String>(opportunityWonMap.values());
			} else {
				list = new ArrayList<String>(opportunityLostMap.values());
			}
			for (int i = 0; i < list.size(); i++) {
				menu.add(0, v.getId(), i, list.get(i));
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if (item.getGroupId() == 0) {
			selectedStatusReason = item.getOrder();
			statusReason_TV.setText(item.getTitle());
		}
		return super.onContextItemSelected(item);

	}

	public void onSearchItem(View view) {
		if (isWon) {
			return;
		}
		nextIntent = new Intent(this, SearchActivity.class);
		switch (view.getId()) {
		case R.id.competitor_RL:
			startActivityForResult(nextIntent, Constant.SEARCH_COMPETITOR);
			break;
		default:
			break;
		}
	}

	private boolean validate() {
		boolean notErrorCase = true;
		if (selectedStatusReason < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a Status Reason.", false);
			notErrorCase = false;
		} else if (closeDate_TV.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter some Close Date.", false);
			notErrorCase = false;
		} else if (!isWon && selectedCompetitor < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a Competitor.", false);
			notErrorCase = false;
		} else if (description_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter some description.", false);
			notErrorCase = false;
		}

		if (!notErrorCase) {
			alertDialogBuilder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
		return notErrorCase;
	}

	private void onPositiveResponse() {
		progressDialog.dismiss();

		 progressDialog = createDialog.createProgressDialog(
		 "Updating Opportunity", "This may take some time", true, null);
		 progressDialog.show();
		
		 Intent serviceIntent = new Intent(this, OpportunityService.class);
		 startService(serviceIntent);

	}

	@Override
	public void onRightButton(View view) {
		super.onRightButton(view);
		if (!validate()) {
			return;
		}

		JSONObject params = new JSONObject();
		try {
			params.put(
					"oppclosestatus",
					((RadioButton) findViewById(status_RG
							.getCheckedRadioButtonId())).getTag())
					.put("actualrevenue", actualValue_TV.getText().toString())
					.put("closuredate",
							dateFormatter.formatDateToString4(dateFormatter
									.formatStringToDate3(closeDate_TV.getText()
											.toString())))
					.put("description", description_ET.getText().toString())
					.put("oppid", selectedOpportunity.getOpportunityId());

			if (isWon) {
				params.put(
						"statusreason",
						new ArrayList<String>(opportunityWonMap.keySet())
								.get(selectedStatusReason)).put("kpmgstatus",
						Constant.OPPORTUNITY_WON);
			} else {
				params.put(
						"statusreason",
						new ArrayList<String>(opportunityLostMap.keySet())
								.get(selectedStatusReason))
						.put("kpmgstatus", Constant.OPPORTUNITY_LOST)
						.put("CompetitorId",
								new ArrayList<String>(competitorMap.keySet())
										.get(selectedCompetitor));
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		params = MyApp.addParamToJson(params);

		String url = Constant.URL + Constant.OPPORTUNITY_UPDATE;

		progressDialog.show();

		System.out.println("URL : " + url);
		System.out.println("params : " + params);

		JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(Method.POST,
				url, params, new Response.Listener<JSONObject>() {

					@Override
					public void onResponse(JSONObject response) {
						System.out.println("length2" + response);
						onPositiveResponse();
						// progressDialog.dismiss();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError error) {
						progressDialog.dismiss();

						AlertDialog alertDialog = myApp.handleError(
								createDialog, error,
								"Error while closing opportunity.");
						alertDialog.show();
					}
				});

		RetryPolicy policy = new DefaultRetryPolicy(30000,
				DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
				DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
		jsonArrayRequest.setRetryPolicy(policy);
		queue.add(jsonArrayRequest);

	}

	public void onStatusSelected(View view) {
		isWon = Boolean
				.parseBoolean((String) ((RadioButton) findViewById(status_RG
						.getCheckedRadioButtonId())).getTag());
		registerForContextMenu(statusReason_RL);
		statusReason_TV.setText("");
		selectedStatusReason = -1;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			int positionItem = 0;
			if (data != null) {
				positionItem = data.getIntExtra("position_item", 0);
			}
			if (requestCode == Constant.SEARCH_COMPETITOR) {
				List<String> list = new ArrayList<String>(
						competitorMap.values());
				competitor_TV.setText(list.get(positionItem));
				selectedCompetitor = positionItem;
			}
		}
	}

	public void onPickDate(View view) {
		super.onPickDate2(view, closeDate_TV);
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
	protected void onPause() {
		isActivityVisible = false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				opportunityUpdateReceiver);
		super.onPause();
	}
}
