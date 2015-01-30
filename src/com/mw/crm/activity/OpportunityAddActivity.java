package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import com.google.gson.Gson;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;
import com.mw.crm.model.Opportunity;
import com.mw.crm.service.OpportunityService;

public class OpportunityAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	TextView clientNameLabel_TV, descriptionLabel_TV, statusLabel_TV,
			oppoManagerLabel_TV, probabilityLabel_TV, salesStageLabel_TV,
			countryLabel_TV, lobLabel_TV, sublobLabel_TV, sectorLabel_TV;

	TextView clientName_TV, status_TV, oppoManager_TV, probability_TV,
			salesStage_TV, country_TV, lob_TV, sublob_TV, sector_TV;

	EditText description_ET;

	RelativeLayout status_RL, oppoManager_RL, probability_RL, salesStage_RL;

	Opportunity tempOpportunity;

	Map<String, String> lobMap;
	Map<String, String> probabilityMap;
	Map<String, String> salesStageMap;
	Map<String, String> statusMap;
	Map<String, String> userMap;

	Intent previousIntent, nextIntent;

	int selectedClientName = -1, selectedStatus = -1, selectedOppoManager = -1,
			selectedProbability = -1, selectedSalesStage = -1;

	RequestQueue queue;

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	private BroadcastReceiver opportunityUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			progressDialog.dismiss();

			Toast.makeText(OpportunityAddActivity.this,
					"Opportunity created successfully.", Toast.LENGTH_SHORT)
					.show();

			Opportunity aa = new Opportunity(oppoManager_TV.getText()
					.toString(), null, null,
					clientName_TV.getText().toString(), description_ET
							.getText().toString(), status_TV.getText()
							.toString(), null, probability_TV.getText()
							.toString(), salesStage_TV.getText().toString());

			nextIntent = new Intent(OpportunityAddActivity.this,
					OpportunityDetailsActivity.class);

			nextIntent.putExtra("opportunity_dummy",
					new Gson().toJson(aa, Opportunity.class));
			nextIntent.putExtra("country", country_TV.getText().toString());
			nextIntent.putExtra("lob", lob_TV.getText().toString());
			nextIntent.putExtra("sub_lob", sublob_TV.getText().toString());
			nextIntent.putExtra("sector", sector_TV.getText().toString());
			if (!(previousIntent.hasExtra("is_edit_mode") && previousIntent
					.getBooleanExtra("is_edit_mode", false))) {
				nextIntent.putExtra("opportunity_created", true);
			}
			startActivityForResult(nextIntent, MyApp.DETAILS_OPPORTUNITY);

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

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Saving Changes",
				"This may take some time", true, null);

		queue = Volley.newRequestQueue(this);
	}

	public void findThings() {
		super.findThings();

		clientNameLabel_TV = (TextView) findViewById(R.id.clientNameLabel_TV);
		descriptionLabel_TV = (TextView) findViewById(R.id.descriptionLabel_TV);
		statusLabel_TV = (TextView) findViewById(R.id.statusLabel_TV);
		oppoManagerLabel_TV = (TextView) findViewById(R.id.oppoManagerLabel_TV);
		probabilityLabel_TV = (TextView) findViewById(R.id.probabilityLabel_TV);
		salesStageLabel_TV = (TextView) findViewById(R.id.salesStageLabel_TV);
		countryLabel_TV = (TextView) findViewById(R.id.countryLabel_TV);
		lobLabel_TV = (TextView) findViewById(R.id.lobLabel_TV);
		sublobLabel_TV = (TextView) findViewById(R.id.sublobLabel_TV);
		sectorLabel_TV = (TextView) findViewById(R.id.sectorLabel_TV);

		clientName_TV = (TextView) findViewById(R.id.clientName_TV);
		status_TV = (TextView) findViewById(R.id.status_TV);
		oppoManager_TV = (TextView) findViewById(R.id.oppoManager_TV);
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
		clientName_TV.setTypeface(myApp.getTypefaceRegularSans());
		descriptionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		statusLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		oppoManagerLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		probabilityLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		salesStageLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		countryLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		lobLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		sublobLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		sectorLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		clientName_TV.setTypeface(myApp.getTypefaceRegularSans());
		status_TV.setTypeface(myApp.getTypefaceRegularSans());
		oppoManager_TV.setTypeface(myApp.getTypefaceRegularSans());
		probability_TV.setTypeface(myApp.getTypefaceRegularSans());
		salesStage_TV.setTypeface(myApp.getTypefaceRegularSans());
		country_TV.setTypeface(myApp.getTypefaceRegularSans());
		lob_TV.setTypeface(myApp.getTypefaceRegularSans());
		sublob_TV.setTypeface(myApp.getTypefaceRegularSans());
		sector_TV.setTypeface(myApp.getTypefaceRegularSans());

		description_ET.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		if (previousIntent.hasExtra("position")) {
			tempOpportunity = myApp.getOpportunityList().get(
					previousIntent.getIntExtra("position", 0));
			descriptionLabel_TV.setText(tempOpportunity.getName());
			System.out.println(tempOpportunity.toString());

			Toast.makeText(this, tempOpportunity.getOpportunityId(),
					Toast.LENGTH_SHORT).show();

			// description_ET.setText(tempOpportunity.getCustomerId());

			Integer temp = myApp.getIntValueFromStringJSON(tempOpportunity
					.getKpmgStatus());
			if (temp != null) {
				status_TV.setText(myApp.getStatusMap().get(
						Integer.toString(temp.intValue())));
				selectedStatus = myApp.getIndexFromKeyStatusMap(Integer
						.toString(temp.intValue()));
				temp = null;
			}

			temp = myApp.getIntValueFromStringJSON(tempOpportunity
					.getProbability());
			if (temp != null) {
				probability_TV.setText(myApp.getProbabilityMap().get(
						Integer.toString(temp.intValue())));
				selectedProbability = myApp
						.getIndexFromKeyProbabilityMap(Integer.toString(temp
								.intValue()));
				temp = null;
			}

			temp = myApp.getIntValueFromStringJSON(tempOpportunity
					.getProbability());
			if (temp != null) {
				salesStage_TV.setText(myApp.getSalesStageMap().get(
						Integer.toString(temp.intValue())));
				selectedSalesStage = myApp.getIndexFromKeySalesMap(Integer
						.toString(temp.intValue()));
				temp = null;
			}

			oppoManager_TV.setText(myApp
					.getStringNameFromStringJSON(tempOpportunity.getOwnerId()));
			selectedOppoManager = myApp.getIndexFromKeyUserMap(myApp
					.getStringIdFromStringJSON(tempOpportunity.getOwnerId()));

			clientName_TV.setText(myApp
					.getStringNameFromStringJSON(tempOpportunity
							.getCustomerId()));
			Account tempAccount = myApp
					.getAccountById(myApp
							.getStringIdFromStringJSON(tempOpportunity
									.getCustomerId()));
			if (tempAccount != null) {
				selectedClientName = myApp.getIndexFromAccountList(tempAccount
						.getAccountId());
				Integer temp2 = myApp.getIntValueFromStringJSON(tempAccount
						.getCountry());
				if (temp2 != null) {
					country_TV.setText(myApp.getCountryMap().get(
							Integer.toString(temp2.intValue())));
					temp2 = null;
				}
				temp2 = myApp.getIntValueFromStringJSON(tempAccount.getLob());
				if (temp2 != null) {
					lob_TV.setText(myApp.getLobMap().get(
							Integer.toString(temp2.intValue())));
					temp2 = null;
				}
				temp2 = myApp
						.getIntValueFromStringJSON(tempAccount.getSubLob());
				if (temp2 != null) {
					sublob_TV.setText(myApp.getSubLobMap().get(
							Integer.toString(temp2.intValue())));
					temp2 = null;
				}
				temp2 = myApp
						.getIntValueFromStringJSON(tempAccount.getSector());
				if (temp2 != null) {
					sector_TV.setText(myApp.getSectorMap().get(
							Integer.toString(temp2.intValue())));
					temp2 = null;
				}
			} else {
				Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT)
						.show();
			}

			System.out.println("selectedClientName  : " + selectedClientName
					+ "\nselectedStatus  : " + selectedStatus
					+ "\nselectedOppoManager  : " + selectedOppoManager
					+ "\nselectedProbability  : " + selectedProbability
					+ "\nselectedSalesStage  : " + selectedSalesStage);
		}

	}

	@SuppressLint("ClickableViewAccessibility")
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
		initView("Add Opportunity", "Save");

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
		hideKeyboard(this.getCurrentFocus());
		openContextMenu(view);
	}

	private boolean validate() {
		boolean notErrorCase = true;
		if (clientName_TV.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a Client Name.", false);
			notErrorCase = false;
		} else if (description_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select some description.", false);
			notErrorCase = false;
		} else if (selectedStatus < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a status.", false);
			notErrorCase = false;
		} else if (selectedOppoManager < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select an Opportunity Managaer.", false);
			notErrorCase = false;
		} else if (selectedProbability < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a probability.", false);
			notErrorCase = false;
		} else if (selectedSalesStage < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a Sales Stage.", false);
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

	public void onRightButton(View view) {
		if (!validate()) {
			return;
		}
		JSONObject params = new JSONObject();
		try {
			params.put("Name",
					MyApp.encryptData(description_ET.getText().toString()))
					.put("CstId",
							myApp.getAccountList().get(selectedClientName)
									.getAccountId())
					.put("Oid",
							new ArrayList<String>(userMap.keySet())
									.get(selectedOppoManager))
					.put("salesstagecodes",
							new ArrayList<String>(salesStageMap.keySet())
									.get(selectedSalesStage))
					.put("probabilty",
							new ArrayList<String>(probabilityMap.keySet())
									.get(selectedProbability));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		params = MyApp.addParamToJson(params);

		progressDialog.show();
		if (previousIntent.hasExtra("is_edit_mode")
				&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
			/** Update Mode **/
			String url = MyApp.URL + MyApp.OPPORTUNITY_UPDATE;
			try {

				System.out.println("URL : " + url);

				System.out.println("json" + params);
				params.put(
						"kpmgstatus",
						new ArrayList<String>(statusMap.keySet())
								.get(selectedStatus)).put("oppid",
						tempOpportunity.getOpportunityId());

				JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
						Method.POST, url, params,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								System.out.println("length2" + response);
								onPositiveResponse();
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								progressDialog.hide();
								System.out.println("ERROR  : "
										+ error.getMessage());
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
				progressDialog.hide();
				e.printStackTrace();
			}
		} else {
			/** Create Mode **/
			String url = MyApp.URL + MyApp.OPPORTUNITY_ADD;
			try {
				System.out.println("URL : " + url);

				System.out.println("json" + params);

				params.put("KPMGStatus",
						new ArrayList<String>(statusMap.keySet())
								.get(selectedStatus));

				JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
						Method.POST, url, params,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								System.out.println("length2" + response);
								onPositiveResponse();
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								progressDialog.hide();
								System.out.println("ERROR  : "
										+ error.getMessage());
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
				progressDialog.hide();
				e.printStackTrace();
			}
		}
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
			int positionItem = 0;
			if (data != null) {
				positionItem = data.getIntExtra("position_item", 0);
			}
			if (requestCode == MyApp.SEARCH_ACCOUNT) {
				selectedClientName = positionItem;

				Account tempAccount = null;
				tempAccount = myApp.getAccountList().get(positionItem);
				if (tempAccount != null) {
					clientName_TV.setText(tempAccount.getName());
					Integer temp = myApp.getIntValueFromStringJSON(tempAccount
							.getCountry());
					if (temp != null) {
						country_TV.setText(myApp.getCountryMap().get(
								Integer.toString(temp.intValue())));
					} else {
						country_TV.setText("");
					}
					temp = myApp
							.getIntValueFromStringJSON(tempAccount.getLob());
					if (temp != null) {
						lob_TV.setText(myApp.getLobMap().get(
								Integer.toString(temp.intValue())));
					} else {
						lob_TV.setText("");
					}
					temp = myApp.getIntValueFromStringJSON(tempAccount
							.getSubLob());
					if (temp != null) {
						sublob_TV.setText(myApp.getSubLobMap().get(
								Integer.toString(temp.intValue())));
					} else {
						sublob_TV.setText("");
					}
					temp = myApp.getIntValueFromStringJSON(tempAccount
							.getSector());
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
			}// if (requestCode == MyApp.SEARCH_ACCOUNT)
			if (requestCode == MyApp.SEARCH_USER) {
				List<String> list = new ArrayList<String>(userMap.values());
				oppoManager_TV.setText(list.get(positionItem));

				selectedOppoManager = positionItem;
			}
			if (requestCode == MyApp.DETAILS_OPPORTUNITY) {
				Intent intent = new Intent();
				intent.putExtra("refresh_list", true);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	}
}
