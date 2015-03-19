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
import android.text.InputFilter;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.DecimalDigitsInputFilter;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;
import com.mw.crm.model.Opportunity;
import com.mw.crm.service.OpportunityService;

public class OpportunityAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	TextView clientNameLabel_TV, leadSourceLabel_TV, salesStageLabel_TV,
			probabilityLabel_TV, statusLabel_TV, expectedClosureDateLabel_TV,
			noOfSolutionLabel_TV;
	// , countryLabel_TV, lobLabel_TV, sublobLabel_TV,
	// sectorLabel_TV, oppoManagerLabel_TV
	TextView clientName_TV, leadSource_TV, salesStage_TV, probability_TV,
			status_TV, expectedClosureDate_TV, noOfSolution_TV;
	// country_TV, lob_TV, sublob_TV, sector_TV, oppoManager_TV

	TextView descriptionLabel_TV, totalProposalValueLabel_TV;
	EditText description_ET, totalProposalValue_ET;

	RelativeLayout leadSource_RL, salesStage_RL, probability_RL, status_RL,
			noOfSolution_RL;
	// , oppoManager_RL
	/** Solution */
	LinearLayout parentSolution1_LL, parentSolution2_LL, parentSolution3_LL,
			parentSolution4_LL;

	RelativeLayout expandTabSolution1_RL, expandTabSolution2_RL,
			expandTabSolution3_RL, expandTabSolution4_RL;

	ImageView arrowSol1_IV, arrowSol2_IV, arrowSol3_IV, arrowSol4_IV;

	LinearLayout childSolution1_LL, childSolution2_LL, childSolution3_LL,
			childSolution4_LL;
	/** Solution */

	Opportunity tempOpportunity;

	// Map<String, String> lobMap;
	Map<String, String> leadSourceMap;
	Map<String, String> salesStageMap;
	Map<String, String> probabilityMap;
	Map<String, String> statusMap;
	Map<String, String> userMap;

	Intent previousIntent, nextIntent;

	int selectedLeadSource = -1, selectedSalesStage = -1,
			selectedProbability = -1, selectedStatus = -1,
			selectedNoOfSolution = -1;
	int selectedClientName = -1, selectedOppoManager = -1;

	RequestQueue queue;

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	LayoutInflater inflater;

	private BroadcastReceiver opportunityUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			progressDialog.dismiss();

			/*
			 * if (!(previousIntent.hasExtra("is_edit_mode") && previousIntent
			 * .getBooleanExtra("is_edit_mode", false))) {
			 * Toast.makeText(OpportunityAddActivity.this,
			 * "Opportunity updated successfully.", Toast.LENGTH_SHORT) .show();
			 * } else { Toast.makeText(OpportunityAddActivity.this,
			 * "Opportunity created successfully.", Toast.LENGTH_SHORT) .show();
			 * }
			 * 
			 * Opportunity aa = new Opportunity(oppoManager_TV.getText()
			 * .toString(), null, null, clientName_TV.getText().toString(),
			 * description_ET .getText().toString(), status_TV.getText()
			 * .toString(), null, probability_TV.getText() .toString(),
			 * salesStage_TV.getText().toString());
			 * 
			 * nextIntent = new Intent(OpportunityAddActivity.this,
			 * OpportunityDetailsActivity.class);
			 * 
			 * nextIntent.putExtra("opportunity_dummy", new Gson().toJson(aa,
			 * Opportunity.class)); nextIntent.putExtra("country",
			 * country_TV.getText().toString()); nextIntent.putExtra("lob",
			 * lob_TV.getText().toString()); nextIntent.putExtra("sub_lob",
			 * sublob_TV.getText().toString()); nextIntent.putExtra("sector",
			 * sector_TV.getText().toString()); if
			 * (!(previousIntent.hasExtra("is_edit_mode") && previousIntent
			 * .getBooleanExtra("is_edit_mode", false))) {
			 * nextIntent.putExtra("opportunity_created", true); }
			 * startActivityForResult(nextIntent, MyApp.DETAILS_OPPORTUNITY);
			 */

		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		// lobMap = myApp.getLobMap();
		leadSourceMap = myApp.getLeadSourceMap();
		salesStageMap = myApp.getSalesStageMap();
		probabilityMap = myApp.getProbabilityMap();
		statusMap = myApp.getStatusMap();
		userMap = myApp.getUserMap();

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Saving Changes",
				"This may take some time", true, null);

		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		// view_solution = (LinearLayout)
		// inflater.inflate(R.layout.view_solution,
		// null, false);
		view_solution = getViewSolution();

		queue = Volley.newRequestQueue(this);
	}

	public void findThings() {
		super.findThings();

		descriptionLabel_TV = (TextView) findViewById(R.id.descriptionLabel_TV);
		totalProposalValueLabel_TV = (TextView) findViewById(R.id.totalProposalValueLabel_TV);

		description_ET = (EditText) findViewById(R.id.description_ET);
		totalProposalValue_ET = (EditText) findViewById(R.id.totalProposalValue_ET);

		clientNameLabel_TV = (TextView) findViewById(R.id.clientNameLabel_TV);
		leadSourceLabel_TV = (TextView) findViewById(R.id.leadSourceLabel_TV);
		salesStageLabel_TV = (TextView) findViewById(R.id.salesStageLabel_TV);
		probabilityLabel_TV = (TextView) findViewById(R.id.probabilityLabel_TV);
		statusLabel_TV = (TextView) findViewById(R.id.statusLabel_TV);
		expectedClosureDateLabel_TV = (TextView) findViewById(R.id.expectedClosureDateLabel_TV);
		noOfSolutionLabel_TV = (TextView) findViewById(R.id.noOfSolutionLabel_TV);

		clientName_TV = (TextView) findViewById(R.id.clientName_TV);
		leadSource_TV = (TextView) findViewById(R.id.leadSource_TV);
		salesStage_TV = (TextView) findViewById(R.id.salesStage_TV);
		probability_TV = (TextView) findViewById(R.id.probability_TV);
		status_TV = (TextView) findViewById(R.id.status_TV);
		expectedClosureDate_TV = (TextView) findViewById(R.id.expectedClosureDate_TV);
		noOfSolution_TV = (TextView) findViewById(R.id.noOfSolution_TV);

		leadSource_RL = (RelativeLayout) findViewById(R.id.leadSource_RL);
		salesStage_RL = (RelativeLayout) findViewById(R.id.salesStage_RL);
		probability_RL = (RelativeLayout) findViewById(R.id.probability_RL);
		status_RL = (RelativeLayout) findViewById(R.id.status_RL);
		noOfSolution_RL = (RelativeLayout) findViewById(R.id.noOfSolution_RL);

		parentSolution1_LL = (LinearLayout) findViewById(R.id.parentSolution1_LL);
		parentSolution2_LL = (LinearLayout) findViewById(R.id.parentSolution2_LL);
		parentSolution3_LL = (LinearLayout) findViewById(R.id.parentSolution3_LL);
		parentSolution4_LL = (LinearLayout) findViewById(R.id.parentSolution4_LL);

		expandTabSolution1_RL = (RelativeLayout) findViewById(R.id.expandTabSolution1_RL);
		expandTabSolution2_RL = (RelativeLayout) findViewById(R.id.expandTabSolution2_RL);
		expandTabSolution3_RL = (RelativeLayout) findViewById(R.id.expandTabSolution3_RL);
		expandTabSolution4_RL = (RelativeLayout) findViewById(R.id.expandTabSolution4_RL);

		arrowSol1_IV = (ImageView) findViewById(R.id.arrowSol1_IV);
		arrowSol2_IV = (ImageView) findViewById(R.id.arrowSol2_IV);
		arrowSol3_IV = (ImageView) findViewById(R.id.arrowSol3_IV);
		arrowSol4_IV = (ImageView) findViewById(R.id.arrowSol4_IV);

		childSolution1_LL = (LinearLayout) findViewById(R.id.childSolution1_LL);
		childSolution2_LL = (LinearLayout) findViewById(R.id.childSolution2_LL);
		childSolution3_LL = (LinearLayout) findViewById(R.id.childSolution3_LL);
		childSolution4_LL = (LinearLayout) findViewById(R.id.childSolution4_LL);
	}

	private void setTypeface() {
		descriptionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		totalProposalValueLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		description_ET.setTypeface(myApp.getTypefaceRegularSans());
		totalProposalValue_ET.setTypeface(myApp.getTypefaceRegularSans());

		clientNameLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		leadSourceLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		salesStageLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		probabilityLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		statusLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		expectedClosureDateLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		noOfSolutionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		clientName_TV.setTypeface(myApp.getTypefaceRegularSans());
		leadSource_TV.setTypeface(myApp.getTypefaceRegularSans());
		salesStage_TV.setTypeface(myApp.getTypefaceRegularSans());
		probability_TV.setTypeface(myApp.getTypefaceRegularSans());
		status_TV.setTypeface(myApp.getTypefaceRegularSans());
		expectedClosureDate_TV.setTypeface(myApp.getTypefaceRegularSans());
		noOfSolution_TV.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		/** if(edit_mode) **/
		if (previousIntent.hasExtra("position")) {
			tempOpportunity = myApp.getOpportunityList().get(
					previousIntent.getIntExtra("position", 0));
			description_ET.setText(tempOpportunity.getDescription());

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

			// oppoManager_TV.setText(myApp
			// .getStringNameFromStringJSON(tempOpportunity.getOwnerId()));
			/*
			 * selectedOppoManager = myApp.getIndexFromKeyUserMap(myApp
			 * .getStringIdFromStringJSON(tempOpportunity.getOwnerId()));
			 */

			// clientName_TV.setText(myApp
			// .getStringNameFromStringJSON(tempOpportunity
			// .getCustomerId()));

			Account tempAccount = myApp
					.getAccountById(myApp
							.getStringIdFromStringJSON(tempOpportunity
									.getCustomerId()));
			if (tempAccount != null) {
				clientName_TV.setText(tempAccount.getName());
				selectedClientName = myApp
						.getAccountIndexFromAccountId(tempAccount
								.getAccountId());

				Integer temp2 = myApp.getIntValueFromStringJSON(tempAccount
						.getCountry());
				/*
				 * if (temp2 != null) {
				 * country_TV.setText(myApp.getCountryMap().get(
				 * Integer.toString(temp2.intValue()))); temp2 = null; } temp2 =
				 * myApp.getIntValueFromStringJSON(tempAccount.getLob()); if
				 * (temp2 != null) { lob_TV.setText(myApp.getLobMap().get(
				 * Integer.toString(temp2.intValue()))); temp2 = null; } temp2 =
				 * myApp .getIntValueFromStringJSON(tempAccount.getSubLob()); if
				 * (temp2 != null) { sublob_TV.setText(myApp.getSubLobMap().get(
				 * Integer.toString(temp2.intValue()))); temp2 = null; } temp2 =
				 * myApp .getIntValueFromStringJSON(tempAccount.getSector()); if
				 * (temp2 != null) { sector_TV.setText(myApp.getSectorMap().get(
				 * Integer.toString(temp2.intValue()))); temp2 = null; }
				 */
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

	private void registerViewsForContextMenu() {
		registerForContextMenu(leadSource_RL);
		registerForContextMenu(status_RL);
		registerForContextMenu(probability_RL);
		registerForContextMenu(salesStage_RL);
		registerForContextMenu(noOfSolution_RL);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opportunity_add);

		initThings();
		findThings();
		initView("Add Opportunity", "Save");

		registerViewsForContextMenu();

		if ((previousIntent.hasExtra("is_edit_mode") && previousIntent
				.getBooleanExtra("is_edit_mode", false))) {
			initView("Modify Opportunity", "Save");
		} else {
			initView("Add Opportunity", "Save");
		}

		hideKeyboardFunctionality();

		// registerForContextMenu(client_RL);

		childSolution1_LL.addView(view_solution);

		System.out.println("!!!!!  :  "
				+ myApp.getOpportunityList().get(12).toString());
		for (int i = 0; i < myApp.getOpportunityList().get(12)
				.getSolutionList().size(); i++) {
			System.out.println("\n\n"
					+ myApp.getOpportunityList().get(12).getSolutionList()
							.get(i).toString());
		}
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

		if (v.getId() == R.id.noOfSolution_RL) {
			menu.add(3, v.getId(), 0, "1");
			menu.add(3, v.getId(), 1, "2");
			menu.add(3, v.getId(), 2, "3");
			menu.add(3, v.getId(), 3, "4");
		}

		if (v.getId() == R.id.leadSource_RL) {
			List<String> list = new ArrayList<String>(leadSourceMap.values());
			for (int i = 0; i < list.size(); i++) {
				menu.add(4, v.getId(), i, list.get(i));
			}
		}
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
		}
		// else if (selectedOppoManager < 0) {
		// alertDialogBuilder = createDialog.createAlertDialog(null,
		// "Please select an Opportunity Managaer.", false);
		// notErrorCase = false;
		// } else if (selectedProbability < 0) {
		// alertDialogBuilder = createDialog.createAlertDialog(null,
		// "Please select a probability.", false);
		// notErrorCase = false;
		// }
		// else if (selectedSalesStage < 0) {
		// alertDialogBuilder = createDialog.createAlertDialog(null,
		// "Please select a Sales Stage.", false);
		// notErrorCase = false;
		// }
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
		System.out.println("aa :  "
				+ ((EditText) childSolution2_LL.findViewById(R.id.fee_ET))
						.getText().toString());
		if (!validate()) {
			return;
		}
		JSONObject params = new JSONObject();
		try {
			params.put("Name",
					MyApp.encryptData(description_ET.getText().toString()))
					.put("CstId",
							myApp.getAccountList().get(selectedClientName)
									.getAccountId());

			if (previousIntent.hasExtra("is_edit_mode")
					&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
				params.put("kpmgstatus",
						new ArrayList<String>(statusMap.keySet())
								.get(selectedStatus));
			} else {
				params.put("KPMGStatus",
						new ArrayList<String>(statusMap.keySet())
								.get(selectedStatus));
			}
			if (selectedOppoManager > -1) {
				params.put("Oid", new ArrayList<String>(userMap.keySet())
						.get(selectedOppoManager));
			}
			if (selectedSalesStage > -1) {
				params.put("salesstagecodes", new ArrayList<String>(
						salesStageMap.keySet()).get(selectedSalesStage));
			}
			if (selectedProbability > -1) {
				params.put("probabilty",
						new ArrayList<String>(probabilityMap.keySet())
								.get(selectedProbability));
			}
			if (previousIntent.hasExtra("is_edit_mode")
					&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
				params.put("oppid", tempOpportunity.getOpportunityId());
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		params = MyApp.addParamToJson(params);

		String url;
		if (previousIntent.hasExtra("is_edit_mode")
				&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
			/** Update Mode **/
			url = MyApp.URL + MyApp.OPPORTUNITY_UPDATE;
		} else {
			url = MyApp.URL + MyApp.OPPORTUNITY_ADD;
		}

		progressDialog.show();
		// if (previousIntent.hasExtra("is_edit_mode")
		// && previousIntent.getBooleanExtra("is_edit_mode", false)) {
		// /** Update Mode **/
		// String url = MyApp.URL + MyApp.OPPORTUNITY_UPDATE;
		try {

			System.out.println("URL : " + url);

			System.out.println("json" + params);
			// params.put(
			// "kpmgstatus",
			// new ArrayList<String>(statusMap.keySet())
			// .get(selectedStatus)).put("oppid",
			// tempOpportunity.getOpportunityId());

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
							progressDialog.dismiss();

							AlertDialog alertDialog = myApp.handleError(
									createDialog, error,
									"Error while creating opportunity.");
							alertDialog.show();
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
		case R.id.leadPartner_RL:
			// startActivityForResult(nextIntent, MyApp.SEARCH_USER);
			break;
		case R.id.client_RL:
			startActivityForResult(nextIntent, MyApp.SEARCH_ACCOUNT);
			break;
		/*
		 * case R.id.oppoManager_RL: startActivityForResult(nextIntent,
		 * MyApp.SEARCH_USER); break;
		 */
		default:
			break;
		}

	}

	/*
	 * private Drawable getDrawableFromId(int id) { return
	 * getResources().getDrawable(id); }
	 */

	private void setDecimalLimitOnFields() {
		InputFilter[] inputFilters = new InputFilter[] { new DecimalDigitsInputFilter(
				10, 4) };

		((EditText) findViewById(R.id.fee_ET)).setFilters(inputFilters);
		((EditText) findViewById(R.id.pyNfr_ET)).setFilters(inputFilters);
		((EditText) findViewById(R.id.cyNfr_ET)).setFilters(inputFilters);
		((EditText) findViewById(R.id.cyNfrPlus1_ET)).setFilters(inputFilters);
		((EditText) findViewById(R.id.cyNfrPlus2_ET)).setFilters(inputFilters);
	}

	private void setChildLLTag(int visibility1, int visibility2,
			int visibility3, int visibility4) {
		if (visibility1 == View.GONE) {
			childSolution1_LL.setTag("false");
		}
		if (visibility2 == View.GONE) {
			childSolution2_LL.setTag("false");
		}
		if (visibility3 == View.GONE) {
			childSolution3_LL.setTag("false");
		}
		if (visibility4 == View.GONE) {
			childSolution4_LL.setTag("false");
		}
	}

	private void setChildLLVisibility(int visibility1, int visibility2,
			int visibility3, int visibility4) {
		childSolution1_LL.setVisibility(visibility1);
		childSolution2_LL.setVisibility(visibility2);
		childSolution3_LL.setVisibility(visibility3);
		childSolution4_LL.setVisibility(visibility4);

		setChildLLTag(visibility1, visibility2, visibility3, visibility4);
	}

	LinearLayout view_solution;

	@SuppressLint("InflateParams")
	private LinearLayout getViewSolution() {
		return (LinearLayout) inflater.inflate(R.layout.view_solution, null,
				false);
	}

	private void addSolutionViewToChild(int visibility2, int visibility3,
			int visibility4) {

		if (visibility2 == View.VISIBLE) {
			if (childSolution2_LL.getChildCount() == 0) {
				childSolution2_LL.addView(getViewSolution());
				setDecimalLimitOnFields();
			}
		} else {
			childSolution2_LL.removeAllViews();
		}

		if (visibility3 == View.VISIBLE) {
			if (childSolution3_LL.getChildCount() == 0) {
				childSolution3_LL.addView(getViewSolution());
				setDecimalLimitOnFields();
			}
		} else {
			childSolution3_LL.removeAllViews();
		}

		if (visibility4 == View.VISIBLE) {
			if (childSolution4_LL.getChildCount() == 0) {
				childSolution4_LL.addView(getViewSolution());
				setDecimalLimitOnFields();
			}
		} else {
			childSolution4_LL.removeAllViews();
		}
	}

	private void setParentLLVisibility(int visibility2, int visibility3,
			int visibility4) {
		parentSolution2_LL.setVisibility(visibility2);
		parentSolution3_LL.setVisibility(visibility3);
		parentSolution4_LL.setVisibility(visibility4);

		addSolutionViewToChild(visibility2, visibility3, visibility4);
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
		} else if (item.getGroupId() == 3) {
			int i = Integer.parseInt(item.getTitle().toString());
			switch (i) {
			case 1:
				setParentLLVisibility(View.GONE, View.GONE, View.GONE);
				break;
			case 2:
				setParentLLVisibility(View.VISIBLE, View.GONE, View.GONE);
				break;
			case 3:
				setParentLLVisibility(View.VISIBLE, View.VISIBLE, View.GONE);
				break;
			case 4:
				setParentLLVisibility(View.VISIBLE, View.VISIBLE, View.VISIBLE);
				break;

			default:
				break;
			}
			salesStage_TV.setText(item.getTitle());
			selectedSalesStage = item.getOrder();
		} else if (item.getGroupId() == 4) {
			leadSource_TV.setText(item.getTitle());
			selectedLeadSource = item.getOrder();
		}
		return super.onContextItemSelected(item);

	}

	public void onExpand(View view) {
		boolean tagVisibility;
		switch (view.getId()) {
		case R.id.expandTabSolution1_RL:
			tagVisibility = Boolean.parseBoolean((String) childSolution1_LL
					.getTag());
			tagVisibility = !tagVisibility;
			childSolution1_LL.setTag(String.valueOf(tagVisibility));

			if (tagVisibility) {
				// arrowSol1_IV
				// .setImageDrawable(getDrawableFromId(R.drawable.arrow_bottom_blue));
				setChildLLVisibility(View.VISIBLE, View.GONE, View.GONE,
						View.GONE);
			} else {
				// arrowSol1_IV
				// .setImageDrawable(getDrawableFromId(R.drawable.arrow_right));
				childSolution1_LL.setVisibility(View.GONE);
			}
			break;
		case R.id.expandTabSolution2_RL:
			tagVisibility = Boolean.parseBoolean((String) childSolution2_LL
					.getTag());
			tagVisibility = !tagVisibility;
			childSolution2_LL.setTag(String.valueOf(tagVisibility));

			if (tagVisibility) {
				setChildLLVisibility(View.GONE, View.VISIBLE, View.GONE,
						View.GONE);
			} else {
				childSolution2_LL.setVisibility(View.GONE);
			}
			break;
		case R.id.expandTabSolution3_RL:
			tagVisibility = Boolean.parseBoolean((String) childSolution3_LL
					.getTag());
			tagVisibility = !tagVisibility;
			childSolution3_LL.setTag(String.valueOf(tagVisibility));

			if (tagVisibility) {
				setChildLLVisibility(View.GONE, View.GONE, View.VISIBLE,
						View.GONE);
			} else {
				childSolution3_LL.setVisibility(View.GONE);
			}
			break;
		case R.id.expandTabSolution4_RL:
			tagVisibility = Boolean.parseBoolean((String) childSolution4_LL
					.getTag());
			tagVisibility = !tagVisibility;
			childSolution4_LL.setTag(String.valueOf(tagVisibility));

			if (tagVisibility) {
				setChildLLVisibility(View.GONE, View.GONE, View.GONE,
						View.VISIBLE);
			} else {
				childSolution4_LL.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onBack(View view) {
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
		setResult(RESULT_OK, intent);
		super.onBack(view);
	}

	@Override
	public void onBackPressed() {
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
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
					/*
					 * Integer temp =
					 * myApp.getIntValueFromStringJSON(tempAccount
					 * .getCountry()); if (temp != null) {
					 * country_TV.setText(myApp.getCountryMap().get(
					 * Integer.toString(temp.intValue()))); } else {
					 * country_TV.setText(""); } temp = myApp
					 * .getIntValueFromStringJSON(tempAccount.getLob()); if
					 * (temp != null) { lob_TV.setText(myApp.getLobMap().get(
					 * Integer.toString(temp.intValue()))); } else {
					 * lob_TV.setText(""); } temp =
					 * myApp.getIntValueFromStringJSON(tempAccount
					 * .getSubLob()); if (temp != null) {
					 * sublob_TV.setText(myApp.getSubLobMap().get(
					 * Integer.toString(temp.intValue()))); } else {
					 * sublob_TV.setText(""); } temp =
					 * myApp.getIntValueFromStringJSON(tempAccount
					 * .getSector()); if (temp != null) {
					 * sector_TV.setText(myApp.getSectorMap().get(
					 * Integer.toString(temp.intValue()))); } else {
					 * sector_TV.setText(""); }
					 */
				} else {
					Toast.makeText(this, "Account not found",
							Toast.LENGTH_SHORT).show();

				}
			}// if (requestCode == MyApp.SEARCH_ACCOUNT)
				// if (requestCode == MyApp.SEARCH_USER) {
			// List<String> list = new ArrayList<String>(userMap.values());
			// oppoManager_TV.setText(list.get(positionItem));
			//
			// selectedOppoManager = positionItem;
			// }
			if (requestCode == MyApp.DETAILS_OPPORTUNITY) {
				Intent intent = new Intent();
				intent.putExtra("refresh_list", true);
				if (previousIntent.hasExtra("search_text")) {
					intent.putExtra("search_text",
							previousIntent.getStringExtra("search_text"));
				}
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	}
}
