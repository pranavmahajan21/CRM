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
import android.view.View.OnFocusChangeListener;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import com.google.gson.Gson;
import com.mw.crm.application.MyApp;
import com.mw.crm.extra.Constant;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.DateFormatter;
import com.mw.crm.extra.DecimalDigitsInputFilter;
import com.mw.crm.model.Account;
import com.mw.crm.model.Opportunity;
import com.mw.crm.model.Solution;
import com.mw.crm.service.OpportunityService;

public class OpportunityAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	TextView clientNameLabel_TV, leadSourceLabel_TV, salesStageLabel_TV,
			probabilityLabel_TV, statusLabel_TV, expectedClosureDateLabel_TV,
			totalProposalValueLabel_TV, noOfSolutionLabel_TV;
	// , countryLabel_TV, lobLabel_TV, sublobLabel_TV,
	// sectorLabel_TV, oppoManagerLabel_TV
	TextView clientName_TV, leadSource_TV, salesStage_TV, probability_TV,
			status_TV, expectedClosureDate_TV, totalProposalValue_TV,
			noOfSolution_TV;
	// country_TV, lob_TV, sublob_TV, sector_TV, oppoManager_TV

	TextView descriptionLabel_TV;
	EditText description_ET;

	RelativeLayout leadSource_RL, salesStage_RL, probability_RL, status_RL,
			noOfSolution_RL;

	RadioGroup confidential_RG;

	// , oppoManager_RL
	/** Solution **/
	LinearLayout parentSolution1_LL, parentSolution2_LL, parentSolution3_LL,
			parentSolution4_LL;

	RelativeLayout expandTabSolution1_RL, expandTabSolution2_RL,
			expandTabSolution3_RL, expandTabSolution4_RL;

	ImageView arrowSol1_IV, arrowSol2_IV, arrowSol3_IV, arrowSol4_IV;

	LinearLayout childSolution1_LL, childSolution2_LL, childSolution3_LL,
			childSolution4_LL;
	/** Solution **/

	/** Solution View **/
	TextView solutionManager_TV, solutionPartner_TV, solutionName_TV,
			profitCenter_TV, taxonomy_TV;
	EditText fee_ET, pyNfr_ET, cyNfr_ET, cyNfrPlus1_ET, cyNfrPlus2_ET;
	/** Solution View **/

	List<String> solutionList;

	Opportunity tempOpportunity;

	// Map<String, String> lobMap;
	Map<String, String> leadSourceMap;
	Map<String, String> salesStageMap;
	Map<String, String> probabilityMap;
	Map<String, String> statusMap;
	Map<String, String> userMap;
	Map<String, String> solutionMap;
	Map<String, String> productMap;
	Map<String, String> profitCenterMap;

	Intent previousIntent, nextIntent;

	int selectedLeadSource = -1, selectedSalesStage = -1,
			selectedProbability = -1, selectedStatus = -1,
			selectedNoOfSolution = -1;
	int selectedClientName = -1, selectedOppoManager = -1;

	int selectedSolManager1 = -1, selectedSolPartner1 = -1,
			selectedSolName1 = -1, selectedProfitCenter1 = -1,
			selectedTaxonomy1 = -1;

	int selectedSolManager2 = -1, selectedSolPartner2 = -1,
			selectedSolName2 = -1, selectedProfitCenter2 = -1,
			selectedTaxonomy2 = -1;

	int selectedSolManager3 = -1, selectedSolPartner3 = -1,
			selectedSolName3 = -1, selectedProfitCenter3 = -1,
			selectedTaxonomy3 = -1;

	int selectedSolManager4 = -1, selectedSolPartner4 = -1,
			selectedSolName4 = -1, selectedProfitCenter4 = -1,
			selectedTaxonomy4 = -1;

	RequestQueue queue;
	DateFormatter dateFormatter;

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	LayoutInflater inflater;
	LinearLayout view_solution;

	private Solution getSolutionObjectFromChildLL(LinearLayout childLL) {
		return new Solution(
				((TextView) childLL.findViewById(R.id.solutionManager_TV))
						.getText().toString(),
				((TextView) childLL.findViewById(R.id.solutionPartner_TV))
						.getText().toString(),
				((TextView) childLL.findViewById(R.id.solutionName_TV))
						.getText().toString(),
				((TextView) childLL.findViewById(R.id.profitCenter_TV))
						.getText().toString(),
				((TextView) childLL.findViewById(R.id.taxonomy_TV)).getText()
						.toString(),
				((EditText) childLL.findViewById(R.id.fee_ET)).getText()
						.toString(),
				((EditText) childLL.findViewById(R.id.pyNfr_ET)).getText()
						.toString(),
				((EditText) childLL.findViewById(R.id.cyNfr_ET)).getText()
						.toString(),
				((EditText) childLL.findViewById(R.id.cyNfrPlus1_ET)).getText()
						.toString(),
				((EditText) childLL.findViewById(R.id.cyNfrPlus2_ET)).getText()
						.toString());
	}

	private BroadcastReceiver opportunityUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			progressDialog.dismiss();

			if (!(previousIntent.hasExtra("is_edit_mode") && previousIntent
					.getBooleanExtra("is_edit_mode", false))) {
				Toast.makeText(OpportunityAddActivity.this,
						"Opportunity updated successfully.", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(OpportunityAddActivity.this,
						"Opportunity created successfully.", Toast.LENGTH_SHORT)
						.show();
			}

			List<Solution> solutionList = new ArrayList<Solution>();
			solutionList.add(getSolutionObjectFromChildLL(childSolution1_LL));
			if (selectedNoOfSolution > 0) {
				System.out.println("0");
				solutionList
						.add(getSolutionObjectFromChildLL(childSolution2_LL));
			}
			if (selectedNoOfSolution > 1) {
				System.out.println("1");
				solutionList
						.add(getSolutionObjectFromChildLL(childSolution3_LL));
			}
			if (selectedNoOfSolution > 2) {
				System.out.println("2");
				solutionList
						.add(getSolutionObjectFromChildLL(childSolution4_LL));
			}

			Opportunity aa = new Opportunity(null, description_ET.getText()
					.toString(), clientName_TV.getText().toString(),
					((RadioButton) findViewById(confidential_RG
							.getCheckedRadioButtonId())).getTag().toString(),
					leadSource_TV.getText().toString(), salesStage_TV.getText()
							.toString(), probability_TV.getText().toString(),
					status_TV.getText().toString(),
					dateFormatter
							.formatStringToDate3Copy(expectedClosureDate_TV
									.getText().toString()),
					totalProposalValue_TV.getText().toString(), noOfSolution_TV
							.getText().toString(), solutionList);

			nextIntent = new Intent(OpportunityAddActivity.this,
					OpportunityDetailsActivity.class);

			nextIntent.putExtra("opportunity_dummy",
					new Gson().toJson(aa, Opportunity.class));
			if (!(previousIntent.hasExtra("is_edit_mode") && previousIntent
					.getBooleanExtra("is_edit_mode", false))) {
				nextIntent.putExtra("opportunity_created", true);
			}
			startActivityForResult(nextIntent, Constant.DETAILS_OPPORTUNITY);

		}
	};

	@SuppressLint("InflateParams")
	private LinearLayout getViewSolution() {
		return (LinearLayout) inflater.inflate(R.layout.view_solution_add,
				null, false);
	}

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		// lobMap = myApp.getLobMap();
		leadSourceMap = myApp.getLeadSourceMap();
		salesStageMap = myApp.getSalesStageMap();
		probabilityMap = myApp.getProbabilityMap();
		statusMap = myApp.getStatusMap();
		userMap = myApp.getUserMap();
		solutionMap = myApp.getSolutionMap();
		productMap = myApp.getProductMap();
		profitCenterMap = myApp.getProfitCenterMap();

		solutionList = new ArrayList<String>();
		solutionList.add("1");
		solutionList.add("2");
		solutionList.add("3");
		solutionList.add("4");

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Saving Changes",
				"This may take some time", true, null);

		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view_solution = getViewSolution();

		queue = Volley.newRequestQueue(this);
		dateFormatter = new DateFormatter();
	}

	public void findThings() {
		super.findThings();

		descriptionLabel_TV = (TextView) findViewById(R.id.descriptionLabel_TV);
		description_ET = (EditText) findViewById(R.id.description_ET);

		clientNameLabel_TV = (TextView) findViewById(R.id.clientNameLabel_TV);
		leadSourceLabel_TV = (TextView) findViewById(R.id.leadSourceLabel_TV);
		salesStageLabel_TV = (TextView) findViewById(R.id.salesStageLabel_TV);
		probabilityLabel_TV = (TextView) findViewById(R.id.probabilityLabel_TV);
		statusLabel_TV = (TextView) findViewById(R.id.statusLabel_TV);
		expectedClosureDateLabel_TV = (TextView) findViewById(R.id.expectedClosureDateLabel_TV);
		totalProposalValueLabel_TV = (TextView) findViewById(R.id.totalProposalValueLabel_TV);
		noOfSolutionLabel_TV = (TextView) findViewById(R.id.noOfSolutionLabel_TV);

		clientName_TV = (TextView) findViewById(R.id.clientName_TV);
		leadSource_TV = (TextView) findViewById(R.id.leadSource_TV);
		salesStage_TV = (TextView) findViewById(R.id.salesStage_TV);
		probability_TV = (TextView) findViewById(R.id.probability_TV);
		status_TV = (TextView) findViewById(R.id.status_TV);
		expectedClosureDate_TV = (TextView) findViewById(R.id.expectedClosureDate_TV);
		totalProposalValue_TV = (TextView) findViewById(R.id.totalProposalValue_TV);
		noOfSolution_TV = (TextView) findViewById(R.id.noOfSolution_TV);

		leadSource_RL = (RelativeLayout) findViewById(R.id.leadSource_RL);
		salesStage_RL = (RelativeLayout) findViewById(R.id.salesStage_RL);
		probability_RL = (RelativeLayout) findViewById(R.id.probability_RL);
		status_RL = (RelativeLayout) findViewById(R.id.status_RL);
		noOfSolution_RL = (RelativeLayout) findViewById(R.id.noOfSolution_RL);

		confidential_RG = (RadioGroup) findViewById(R.id.confidential_RG);

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
		totalProposalValue_TV.setTypeface(myApp.getTypefaceRegularSans());

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
		} else {
			/** Create Mode **/
			System.out.println("Create Mode ");
			selectedSalesStage = Constant.DEFAULT_SALES_STAGE_INDEX;
			selectedProbability = Constant.DEFAULT_PROBABILITY_INDEX;
			selectedStatus = Constant.DEFAULT_STATUS_INDEX;
			selectedNoOfSolution = Constant.DEFAULT_NO_OF_SOLUTION_INDEX;

			salesStage_TV.setText(salesStageMap.get(new ArrayList<String>(
					salesStageMap.keySet()).get(selectedSalesStage)));
			probability_TV.setText(probabilityMap.get(new ArrayList<String>(
					probabilityMap.keySet()).get(selectedProbability)));
			status_TV.setText(statusMap.get(new ArrayList<String>(statusMap
					.keySet()).get(selectedStatus)));
			noOfSolution_TV.setText(solutionList.get(selectedNoOfSolution));

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

		childSolution1_LL.addView(view_solution);
		setTypefaceToChildLL(childSolution1_LL);
		setDecimalLimitOnFields(childSolution1_LL);
		setOnFocusLoseListener(childSolution1_LL);

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
			for (int i = 0; i < solutionList.size(); i++) {
				menu.add(3, v.getId(), i, solutionList.get(i));
			}
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

	private boolean validateAmount(LinearLayout childSolution_LL) {
		double cyNfrPlus1 = 0;
		double cyNfrPlus2 = 0;
		double fee = Double.parseDouble(((EditText) childSolution_LL
				.findViewById(R.id.fee_ET)).getText().toString().trim());
		double pyNfr = Double.parseDouble(((EditText) childSolution_LL
				.findViewById(R.id.pyNfr_ET)).getText().toString().trim());
		double cyNfr = Double.parseDouble(((EditText) childSolution_LL
				.findViewById(R.id.cyNfr_ET)).getText().toString().trim());
		if (((EditText) childSolution_LL.findViewById(R.id.cyNfrPlus1_ET))
				.getText().toString().trim().length() > 0) {
			cyNfrPlus1 = Double.parseDouble(((EditText) childSolution_LL
					.findViewById(R.id.cyNfrPlus1_ET)).getText().toString()
					.trim());
		}
		if (((EditText) childSolution_LL.findViewById(R.id.cyNfrPlus2_ET))
				.getText().toString().trim().length() > 0) {
			cyNfrPlus2 = Double.parseDouble(((EditText) childSolution_LL
					.findViewById(R.id.cyNfrPlus2_ET)).getText().toString()
					.trim());
		}
		return (pyNfr + cyNfr + cyNfrPlus1 + cyNfrPlus2) >= fee;
	}

	private boolean validate() {
		boolean notErrorCase = true;
		if (description_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select some description.", false);
			notErrorCase = false;
		} else if (clientName_TV.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a Client Name.", false);
			notErrorCase = false;
		} else if (selectedLeadSource < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a lead source.", false);
			notErrorCase = false;
		} else if (selectedSolManager1 < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"For SOLUTION1 please select a Solution Manager.", false);
			notErrorCase = false;
		} else if (selectedSolPartner1 < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"For SOLUTION1 please select a Solution Partner.", false);
			notErrorCase = false;
		} else if (selectedSolName1 < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"For SOLUTION1 please select a Solution Name.", false);
			notErrorCase = false;
		} else if (selectedProfitCenter1 < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"For SOLUTION1 please select a Profit Center.", false);
			notErrorCase = false;
		} else if (selectedTaxonomy1 < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"For SOLUTION1 please select a Taxonomy.", false);
			notErrorCase = false;
		} else if (((EditText) childSolution1_LL.findViewById(R.id.fee_ET))
				.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"For SOLUTION1 please select some fee.", false);
			notErrorCase = false;
		} else if (((EditText) childSolution1_LL.findViewById(R.id.cyNfr_ET))
				.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"For SOLUTION1 please select some CY NFR.", false);
			notErrorCase = false;
		} else {
			if (validateAmount(childSolution1_LL)) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						Constant.ERR_SOL1_AMOUNT, false);
				notErrorCase = false;
			}
		}
		if (selectedNoOfSolution > 0 && notErrorCase) {
			if (selectedSolManager2 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION2 please select a Solution Manager.",
						false);
				notErrorCase = false;
			} else if (selectedSolPartner2 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION2 please select a Solution Partner.",
						false);
				notErrorCase = false;
			} else if (selectedSolName2 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION2 please select a Solution Name.", false);
				notErrorCase = false;
			} else if (selectedProfitCenter2 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION2 please select a Profit Center.", false);
				notErrorCase = false;
			} else if (selectedTaxonomy2 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION2 please select a Taxonomy.", false);
				notErrorCase = false;
			} else if (((EditText) childSolution2_LL.findViewById(R.id.fee_ET))
					.getText().toString().trim().length() < 1) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION2 please select some fee.", false);
				notErrorCase = false;
			} else if (((EditText) childSolution2_LL
					.findViewById(R.id.cyNfr_ET)).getText().toString().trim()
					.length() < 1) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION2 please select some CY NFR.", false);
				notErrorCase = false;
			} else {
				if (validateAmount(childSolution2_LL)) {
					alertDialogBuilder = createDialog.createAlertDialog(null,
							Constant.ERR_SOL2_AMOUNT, false);
					notErrorCase = false;
				}
			}
		}
		if (selectedNoOfSolution > 1 && notErrorCase) {
			if (selectedSolManager3 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION3 please select a Solution Manager.",
						false);
				notErrorCase = false;
			} else if (selectedSolPartner3 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION3 please select a Solution Partner.",
						false);
				notErrorCase = false;
			} else if (selectedSolName3 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION3 please select a Solution Name.", false);
				notErrorCase = false;
			} else if (selectedProfitCenter3 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION3 please select a Profit Center.", false);
				notErrorCase = false;
			} else if (selectedTaxonomy3 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION3 please select a Taxonomy.", false);
				notErrorCase = false;
			} else if (((EditText) childSolution3_LL.findViewById(R.id.fee_ET))
					.getText().toString().trim().length() < 1) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION3 please select some fee.", false);
				notErrorCase = false;
			} else if (((EditText) childSolution3_LL
					.findViewById(R.id.cyNfr_ET)).getText().toString().trim()
					.length() < 1) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION3 please select some CY NFR.", false);
				notErrorCase = false;
			} else {
				if (validateAmount(childSolution3_LL)) {
					alertDialogBuilder = createDialog.createAlertDialog(null,
							Constant.ERR_SOL3_AMOUNT, false);
					notErrorCase = false;
				}
			}
		}
		if (selectedNoOfSolution > 2 && notErrorCase) {
			if (selectedSolManager4 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION4 please select a Solution Manager.",
						false);
				notErrorCase = false;
			} else if (selectedSolPartner4 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION4 please select a Solution Partner.",
						false);
				notErrorCase = false;
			} else if (selectedSolName4 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION4 please select a Solution Name.", false);
				notErrorCase = false;
			} else if (selectedProfitCenter4 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION4 please select a Profit Center.", false);
				notErrorCase = false;
			} else if (selectedTaxonomy4 < 0) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION4 please select a Taxonomy.", false);
				notErrorCase = false;
			} else if (((EditText) childSolution4_LL.findViewById(R.id.fee_ET))
					.getText().toString().trim().length() < 1) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION4 please select some fee.", false);
				notErrorCase = false;
			} else if (((EditText) childSolution4_LL
					.findViewById(R.id.cyNfr_ET)).getText().toString().trim()
					.length() < 1) {
				alertDialogBuilder = createDialog.createAlertDialog(null,
						"For SOLUTION4 please select some CY NFR.", false);
				notErrorCase = false;
			} else {
				if (validateAmount(childSolution4_LL)) {
					alertDialogBuilder = createDialog.createAlertDialog(null,
							Constant.ERR_SOL4_AMOUNT, false);
					notErrorCase = false;
				}
			}
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
		/**
		 * This line is to ensure that money fields lose focus & Total Proposal
		 * Value gets updated.
		 **/
		description_ET.requestFocus();
		
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
					.put("confidential",
							((RadioButton) findViewById(((RadioGroup) findViewById(R.id.confidential_RG))
									.getCheckedRadioButtonId())).getTag())
					.put("leadsource",
							new ArrayList<String>(leadSourceMap.keySet())
									.get(selectedLeadSource))
					.put("salesstagecodes",
							new ArrayList<String>(salesStageMap.keySet())
									.get(selectedSalesStage))
					.put("probabilty",
							new ArrayList<String>(probabilityMap.keySet())
									.get(selectedProbability))
					.put("expclosuredate",
							dateFormatter.formatDateToString4(dateFormatter
									.formatStringToDate3(expectedClosureDate_TV
											.getText().toString())))
					.put("estvalue", totalProposalValue_TV.getText().toString())
					.put("noofsolutions",
							solutionList.get(selectedNoOfSolution))
					.put("solutionmanager",
							new ArrayList<String>(userMap.keySet())
									.get(selectedSolManager1))
					.put("solutionpartner",
							new ArrayList<String>(userMap.keySet())
									.get(selectedSolPartner1))
					.put("profitcenter",
							new ArrayList<String>(profitCenterMap.keySet())
									.get(selectedProfitCenter1))
					.put("solutionone",
							new ArrayList<String>(solutionMap.keySet())
									.get(selectedSolName1))
					.put("taxonomyone",
							new ArrayList<String>(productMap.keySet())
									.get(selectedTaxonomy1))
					.put("fees",
							((EditText) childSolution1_LL
									.findViewById(R.id.fee_ET)).getText()
									.toString())
					.put("cysolution1",
							((EditText) childSolution1_LL
									.findViewById(R.id.cyNfr_ET)).getText()
									.toString());
			if (((EditText) childSolution1_LL.findViewById(R.id.cyNfrPlus1_ET))
					.getText().toString().length() > 0) {
				params.put("nfyfy1sol1", ((EditText) childSolution1_LL
						.findViewById(R.id.cyNfrPlus1_ET)).getText().toString());
			}
			if (((EditText) childSolution1_LL.findViewById(R.id.cyNfrPlus2_ET))
					.getText().toString().length() > 0) {
				params.put("nfyfy2sol1", ((EditText) childSolution1_LL
						.findViewById(R.id.cyNfrPlus1_ET)).getText().toString());
			}
			if (selectedNoOfSolution > 0) {
				params.put(
						"solutionmanagertwo",
						new ArrayList<String>(userMap.keySet())
								.get(selectedSolManager2))
						.put("solutionpartnertwo",
								new ArrayList<String>(userMap.keySet())
										.get(selectedSolPartner2))
						.put("profitcentertwo",
								new ArrayList<String>(profitCenterMap.keySet())
										.get(selectedProfitCenter2))
						.put("solutiontwo",
								new ArrayList<String>(solutionMap.keySet())
										.get(selectedSolName2))
						.put("taxonomytwo",
								new ArrayList<String>(productMap.keySet())
										.get(selectedTaxonomy2))
						.put("feessolution2",
								((EditText) childSolution2_LL
										.findViewById(R.id.fee_ET)).getText()
										.toString())
						.put("cysolution2",
								((EditText) childSolution2_LL
										.findViewById(R.id.cyNfr_ET)).getText()
										.toString());
				if (((EditText) childSolution2_LL
						.findViewById(R.id.cyNfrPlus1_ET)).getText().toString()
						.length() > 0) {
					params.put("nfyfy1sol2", ((EditText) childSolution2_LL
							.findViewById(R.id.cyNfrPlus1_ET)).getText()
							.toString());
				}
				if (((EditText) childSolution2_LL
						.findViewById(R.id.cyNfrPlus2_ET)).getText().toString()
						.length() > 0) {
					params.put("nfyfy2sol2", ((EditText) childSolution2_LL
							.findViewById(R.id.cyNfrPlus1_ET)).getText()
							.toString());
				}
			}

			if (selectedNoOfSolution > 1) {
				params.put(
						"solutionmanagerthree",
						new ArrayList<String>(userMap.keySet())
								.get(selectedSolManager3))
						.put("solutionpartnerthree",
								new ArrayList<String>(userMap.keySet())
										.get(selectedSolPartner3))
						.put("profitcenterthree",
								new ArrayList<String>(profitCenterMap.keySet())
										.get(selectedProfitCenter3))
						.put("solutionthree",
								new ArrayList<String>(solutionMap.keySet())
										.get(selectedSolName3))
						.put("taxonomythree",
								new ArrayList<String>(productMap.keySet())
										.get(selectedTaxonomy3))
						.put("feessolution3",
								((EditText) childSolution3_LL
										.findViewById(R.id.fee_ET)).getText()
										.toString())
						.put("cysolution3",
								((EditText) childSolution3_LL
										.findViewById(R.id.cyNfr_ET)).getText()
										.toString());
				if (((EditText) childSolution3_LL
						.findViewById(R.id.cyNfrPlus1_ET)).getText().toString()
						.length() > 0) {
					params.put("nfyfy1sol3", ((EditText) childSolution3_LL
							.findViewById(R.id.cyNfrPlus1_ET)).getText()
							.toString());
				}
				if (((EditText) childSolution3_LL
						.findViewById(R.id.cyNfrPlus2_ET)).getText().toString()
						.length() > 0) {
					params.put("nfyfy2sol3", ((EditText) childSolution3_LL
							.findViewById(R.id.cyNfrPlus1_ET)).getText()
							.toString());
				}
			}

			if (selectedNoOfSolution > 2) {
				params.put(
						"solutionmanagerfour",
						new ArrayList<String>(userMap.keySet())
								.get(selectedSolManager4))
						.put("solutionpartnerfour",
								new ArrayList<String>(userMap.keySet())
										.get(selectedSolPartner4))
						.put("profitcenterfour",
								new ArrayList<String>(profitCenterMap.keySet())
										.get(selectedProfitCenter4))
						.put("solutionfour",
								new ArrayList<String>(solutionMap.keySet())
										.get(selectedSolName4))
						.put("taxonomyfour",
								new ArrayList<String>(productMap.keySet())
										.get(selectedTaxonomy4))
						.put("feessolution4",
								((EditText) childSolution4_LL
										.findViewById(R.id.fee_ET)).getText()
										.toString())
						.put("cysolution4",
								((EditText) childSolution4_LL
										.findViewById(R.id.cyNfr_ET)).getText()
										.toString());
				if (((EditText) childSolution4_LL
						.findViewById(R.id.cyNfrPlus1_ET)).getText().toString()
						.length() > 0) {
					params.put("nfyfy1sol4", ((EditText) childSolution4_LL
							.findViewById(R.id.cyNfrPlus1_ET)).getText()
							.toString());
				}
				if (((EditText) childSolution4_LL
						.findViewById(R.id.cyNfrPlus2_ET)).getText().toString()
						.length() > 0) {
					params.put("nfyfy2sol4", ((EditText) childSolution4_LL
							.findViewById(R.id.cyNfrPlus1_ET)).getText()
							.toString());
				}
			}

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
			url = Constant.URL + Constant.OPPORTUNITY_UPDATE;
		} else {
			url = Constant.URL + Constant.OPPORTUNITY_ADD;
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

	int whichSolutionTabIsVisible;

	private void putExtraUsingSwitch(String keyExtra) {
		whichSolutionTabIsVisible = checkVisibilityOfChildLL();
		switch (whichSolutionTabIsVisible) {
		case Constant.SOLUTION1_VISIBLE:
			nextIntent.putExtra(keyExtra, Constant.SOLUTION1_VISIBLE);
			break;
		case Constant.SOLUTION2_VISIBLE:
			nextIntent.putExtra(keyExtra, Constant.SOLUTION2_VISIBLE);
			break;
		case Constant.SOLUTION3_VISIBLE:
			nextIntent.putExtra(keyExtra, Constant.SOLUTION3_VISIBLE);
			break;
		case Constant.SOLUTION4_VISIBLE:
			nextIntent.putExtra(keyExtra, Constant.SOLUTION4_VISIBLE);
			break;

		default:
			break;
		}
	}

	public void onSearchItem(View view) {
		nextIntent = new Intent(this, SearchActivity.class);

		switch (view.getId()) {
		case R.id.client_RL:
			startActivityForResult(nextIntent, Constant.SEARCH_ACCOUNT);
			break;
		case R.id.solutionManager_RL:
			whichSolutionTabIsVisible = checkVisibilityOfChildLL();
			switch (whichSolutionTabIsVisible) {
			case Constant.SOLUTION1_VISIBLE:
				nextIntent.putExtra("user_value",
						Constant.USER_SOLUTION_MANAGER1);
				break;
			case Constant.SOLUTION2_VISIBLE:
				nextIntent.putExtra("user_value",
						Constant.USER_SOLUTION_MANAGER2);
				break;
			case Constant.SOLUTION3_VISIBLE:
				nextIntent.putExtra("user_value",
						Constant.USER_SOLUTION_MANAGER3);
				break;
			case Constant.SOLUTION4_VISIBLE:
				nextIntent.putExtra("user_value",
						Constant.USER_SOLUTION_MANAGER4);
				break;

			default:
				break;
			}
			startActivityForResult(nextIntent, Constant.SEARCH_USER);
			break;

		case R.id.solutionPartner_RL:
			whichSolutionTabIsVisible = checkVisibilityOfChildLL();
			switch (whichSolutionTabIsVisible) {
			case Constant.SOLUTION1_VISIBLE:
				nextIntent.putExtra("user_value",
						Constant.USER_SOLUTION_PARTNER1);
				break;
			case Constant.SOLUTION2_VISIBLE:
				nextIntent.putExtra("user_value",
						Constant.USER_SOLUTION_PARTNER2);
				break;
			case Constant.SOLUTION3_VISIBLE:
				nextIntent.putExtra("user_value",
						Constant.USER_SOLUTION_PARTNER3);
				break;
			case Constant.SOLUTION4_VISIBLE:
				nextIntent.putExtra("user_value",
						Constant.USER_SOLUTION_PARTNER4);
				break;

			default:
				break;
			}
			startActivityForResult(nextIntent, Constant.SEARCH_USER);
			break;
		case R.id.solutionName_RL:
			// whichSolutionTabIsVisible = checkVisibilityOfChildLL();
			// switch (whichSolutionTabIsVisible) {
			// case Constant.SOLUTION1_VISIBLE:
			// System.out.println("Check0");
			// nextIntent.putExtra("solution_value",
			// Constant.SOLUTION1_VISIBLE);
			// break;
			// case Constant.SOLUTION2_VISIBLE:
			// nextIntent.putExtra("solution_value",
			// Constant.SOLUTION2_VISIBLE);
			// break;
			// case Constant.SOLUTION3_VISIBLE:
			// nextIntent.putExtra("solution_value",
			// Constant.SOLUTION3_VISIBLE);
			// break;
			// case Constant.SOLUTION4_VISIBLE:
			// nextIntent.putExtra("solution_value",
			// Constant.SOLUTION4_VISIBLE);
			// break;
			//
			// default:
			// break;
			// }
			putExtraUsingSwitch("solution_value");
			startActivityForResult(nextIntent, Constant.SEARCH_SOLUTION);
			break;

		case R.id.profitCenter_RL:
			// whichSolutionTabIsVisible = checkVisibilityOfChildLL();
			// switch (whichSolutionTabIsVisible) {
			// case Constant.SOLUTION1_VISIBLE:
			// nextIntent.putExtra("profit_center_value",
			// Constant.SOLUTION1_VISIBLE);
			// break;
			// case Constant.SOLUTION2_VISIBLE:
			// nextIntent.putExtra("profit_center_value",
			// Constant.SOLUTION2_VISIBLE);
			// break;
			// case Constant.SOLUTION3_VISIBLE:
			// nextIntent.putExtra("profit_center_value",
			// Constant.SOLUTION3_VISIBLE);
			// break;
			// case Constant.SOLUTION4_VISIBLE:
			// nextIntent.putExtra("profit_center_value",
			// Constant.SOLUTION4_VISIBLE);
			// break;
			//
			// default:
			// break;
			// }
			putExtraUsingSwitch("profit_center_value");
			startActivityForResult(nextIntent, Constant.SEARCH_PROFIT_CENTER);
			break;

		case R.id.taxonomy_RL:
			// whichSolutionTabIsVisible = checkVisibilityOfChildLL();
			// switch (whichSolutionTabIsVisible) {
			// case Constant.SOLUTION1_VISIBLE:
			// nextIntent
			// .putExtra("product_value", Constant.SOLUTION1_VISIBLE);
			// break;
			// case Constant.SOLUTION2_VISIBLE:
			// nextIntent
			// .putExtra("product_value", Constant.SOLUTION2_VISIBLE);
			// break;
			// case Constant.SOLUTION3_VISIBLE:
			// nextIntent
			// .putExtra("product_value", Constant.SOLUTION3_VISIBLE);
			// break;
			// case Constant.SOLUTION4_VISIBLE:
			// nextIntent
			// .putExtra("product_value", Constant.SOLUTION4_VISIBLE);
			// break;
			//
			// default:
			// break;
			// }
			putExtraUsingSwitch("product_value");
			startActivityForResult(nextIntent, Constant.SEARCH_PRODUCT);
			break;

		default:
			System.out.println("default");
			break;
		}

	}

	private int checkVisibilityOfChildLL() {
		boolean flag = Boolean
				.parseBoolean((String) childSolution1_LL.getTag());
		System.out.println(flag);
		if (flag) {
			return Constant.SOLUTION1_VISIBLE;
		}
		flag = Boolean.parseBoolean((String) childSolution2_LL.getTag());
		System.out.println(flag);
		if (flag) {
			return Constant.SOLUTION2_VISIBLE;
		}
		flag = Boolean.parseBoolean((String) childSolution3_LL.getTag());
		System.out.println(flag);
		if (flag) {
			return Constant.SOLUTION3_VISIBLE;
		}
		flag = Boolean.parseBoolean((String) childSolution4_LL.getTag());
		System.out.println(flag);
		if (flag) {
			return Constant.SOLUTION4_VISIBLE;
		}
		return Constant.SOLUTION1_VISIBLE;
	}

	

	private double parseStringToDouble(String numberString) {
		try {
			return Double.parseDouble(numberString);
		} catch (NumberFormatException e) {
			return 0.0;
		}
	}

	private double totalAmountOfChildLL(LinearLayout childSolution_LL) {
		double fee = parseStringToDouble(((EditText) childSolution_LL
				.findViewById(R.id.fee_ET)).getText().toString().trim());
		double pyNfr = parseStringToDouble(((EditText) childSolution_LL
				.findViewById(R.id.pyNfr_ET)).getText().toString().trim());
		double cyNfr = parseStringToDouble(((EditText) childSolution_LL
				.findViewById(R.id.cyNfr_ET)).getText().toString().trim());
		double cyNfrPlus1 = parseStringToDouble(((EditText) childSolution_LL
				.findViewById(R.id.cyNfrPlus1_ET)).getText().toString().trim());
		double cyNfrPlus2 = parseStringToDouble(((EditText) childSolution_LL
				.findViewById(R.id.cyNfrPlus2_ET)).getText().toString().trim());
		return (fee + pyNfr + cyNfr + cyNfrPlus1 + cyNfrPlus2);
	}

	private void setDecimalLimitOnFields(LinearLayout childLL) {
		InputFilter[] inputFilters = new InputFilter[] { new DecimalDigitsInputFilter(
				10, 4) };

		((EditText) childLL.findViewById(R.id.fee_ET)).setFilters(inputFilters);
		// ((EditText) findViewById(R.id.pyNfr_ET)).setFilters(inputFilters);
		((EditText) childLL.findViewById(R.id.cyNfr_ET)).setFilters(inputFilters);
		((EditText) childLL.findViewById(R.id.cyNfrPlus1_ET)).setFilters(inputFilters);
		((EditText) childLL.findViewById(R.id.cyNfrPlus2_ET)).setFilters(inputFilters);
	}
	
	private void setOnFocusLoseListener(LinearLayout childLL) {
		OnFocusChangeListener focusChangeListener = new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (!hasFocus) {
					double totalProposalValue = 0;
					totalProposalValue = totalAmountOfChildLL(childSolution1_LL);
					if (selectedNoOfSolution > 0) {
						totalProposalValue = totalProposalValue
								+ totalAmountOfChildLL(childSolution2_LL);
					}
					if (selectedNoOfSolution > 1) {
						totalProposalValue = totalProposalValue
								+ totalAmountOfChildLL(childSolution3_LL);
					}
					if (selectedNoOfSolution > 2) {
						totalProposalValue = totalProposalValue
								+ totalAmountOfChildLL(childSolution4_LL);
					}
					System.out.println("hello");
					totalProposalValue_TV.setText(String
							.valueOf(totalProposalValue));
					System.out.println("hello");
				}
			}

		};

		((EditText) childLL.findViewById(R.id.fee_ET))
				.setOnFocusChangeListener(focusChangeListener);
		((EditText) childLL.findViewById(R.id.cyNfr_ET))
				.setOnFocusChangeListener(focusChangeListener);
		((EditText) childLL.findViewById(R.id.cyNfrPlus1_ET))
				.setOnFocusChangeListener(focusChangeListener);
		((EditText) childLL.findViewById(R.id.cyNfrPlus2_ET))
				.setOnFocusChangeListener(focusChangeListener);
	}

	private void setTypefaceToChildLL(LinearLayout childLL) {
		((TextView) childLL.findViewById(R.id.solutionManagerLabel_TV))
				.setTypeface(myApp.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.solutionPartnerLabel_TV))
				.setTypeface(myApp.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.solutionNameLabel_TV))
				.setTypeface(myApp.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.profitCenterLabel_TV))
				.setTypeface(myApp.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.taxonomyLabel_TV))
				.setTypeface(myApp.getTypefaceRegularSans());

		((TextView) childLL.findViewById(R.id.feeLabel_TV)).setTypeface(myApp
				.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.pyNfrLabel_TV)).setTypeface(myApp
				.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.cyNfrLabel_TV)).setTypeface(myApp
				.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.cyNfrPlus1Label_TV))
				.setTypeface(myApp.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.cyNfrPlus2Label_TV))
				.setTypeface(myApp.getTypefaceRegularSans());

		((TextView) childLL.findViewById(R.id.solutionManager_TV))
				.setTypeface(myApp.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.solutionPartner_TV))
				.setTypeface(myApp.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.solutionName_TV))
				.setTypeface(myApp.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.profitCenter_TV))
				.setTypeface(myApp.getTypefaceRegularSans());
		((TextView) childLL.findViewById(R.id.taxonomy_TV)).setTypeface(myApp
				.getTypefaceRegularSans());

		((EditText) childLL.findViewById(R.id.fee_ET)).setTypeface(myApp
				.getTypefaceRegularSans());
		((EditText) childLL.findViewById(R.id.pyNfr_ET)).setTypeface(myApp
				.getTypefaceRegularSans());
		((EditText) childLL.findViewById(R.id.cyNfr_ET)).setTypeface(myApp
				.getTypefaceRegularSans());
		((EditText) childLL.findViewById(R.id.cyNfrPlus1_ET)).setTypeface(myApp
				.getTypefaceRegularSans());
		((EditText) childLL.findViewById(R.id.cyNfrPlus2_ET)).setTypeface(myApp
				.getTypefaceRegularSans());
	}

	private void addSolutionViewToChild(int visibility2, int visibility3,
			int visibility4) {

		if (visibility2 == View.VISIBLE) {
			if (childSolution2_LL.getChildCount() == 0) {
				childSolution2_LL.addView(getViewSolution());
				setTypefaceToChildLL(childSolution2_LL);
				setDecimalLimitOnFields(childSolution2_LL);
				setOnFocusLoseListener(childSolution2_LL);
			}
		} else {
			childSolution2_LL.removeAllViews();
		}

		if (visibility3 == View.VISIBLE) {
			if (childSolution3_LL.getChildCount() == 0) {
				childSolution3_LL.addView(getViewSolution());
				setTypefaceToChildLL(childSolution3_LL);
				setDecimalLimitOnFields(childSolution3_LL);
				setOnFocusLoseListener(childSolution3_LL);
			}
		} else {
			childSolution3_LL.removeAllViews();
		}

		if (visibility4 == View.VISIBLE) {
			if (childSolution4_LL.getChildCount() == 0) {
				childSolution4_LL.addView(getViewSolution());
				setTypefaceToChildLL(childSolution4_LL);
				setDecimalLimitOnFields(childSolution4_LL);
				setOnFocusLoseListener(childSolution4_LL);
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
			noOfSolution_TV.setText(item.getTitle());
			selectedNoOfSolution = item.getOrder();
		} else if (item.getGroupId() == 4) {
			leadSource_TV.setText(item.getTitle());
			selectedLeadSource = item.getOrder();
		}
		return super.onContextItemSelected(item);

	}

	private void findViewSolution(LinearLayout childLL) {
		solutionManager_TV = (TextView) childLL
				.findViewById(R.id.solutionManager_TV);
		solutionPartner_TV = (TextView) childLL
				.findViewById(R.id.solutionPartner_TV);
		solutionName_TV = (TextView) childLL.findViewById(R.id.solutionName_TV);
		profitCenter_TV = (TextView) childLL.findViewById(R.id.profitCenter_TV);
		taxonomy_TV = (TextView) childLL.findViewById(R.id.taxonomy_TV);

		fee_ET = (EditText) childLL.findViewById(R.id.fee_ET);
		pyNfr_ET = (EditText) childLL.findViewById(R.id.pyNfr_ET);
		cyNfr_ET = (EditText) childLL.findViewById(R.id.cyNfr_ET);
		cyNfrPlus1_ET = (EditText) childLL.findViewById(R.id.cyNfrPlus1_ET);
		cyNfrPlus2_ET = (EditText) childLL.findViewById(R.id.cyNfrPlus2_ET);
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

	public void onExpand(View view) {
		boolean tagVisibility;
		switch (view.getId()) {
		case R.id.expandTabSolution1_RL:
			tagVisibility = Boolean.parseBoolean((String) childSolution1_LL
					.getTag());
			tagVisibility = !tagVisibility;
			childSolution1_LL.setTag(String.valueOf(tagVisibility));

			if (tagVisibility) {
				setChildLLVisibility(View.VISIBLE, View.GONE, View.GONE,
						View.GONE);
				findViewSolution(childSolution1_LL);
			} else {
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
				findViewSolution(childSolution2_LL);
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
				findViewSolution(childSolution3_LL);
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
				findViewSolution(childSolution4_LL);
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
			if (requestCode == Constant.SEARCH_ACCOUNT) {
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
			if (requestCode == Constant.SEARCH_USER) {
				List<String> list = new ArrayList<String>(userMap.values());
				String text = list.get(positionItem);
				switch (data.getIntExtra("user_value", -1)) {
				case Constant.USER_SOLUTION_MANAGER1:
					solutionManager_TV.setText(text);
					selectedSolManager1 = positionItem;
					break;
				case Constant.USER_SOLUTION_MANAGER2:
					solutionManager_TV.setText(text);
					selectedSolManager2 = positionItem;
					break;
				case Constant.USER_SOLUTION_MANAGER3:
					solutionManager_TV.setText(text);
					selectedSolManager3 = positionItem;
					break;
				case Constant.USER_SOLUTION_MANAGER4:
					solutionManager_TV.setText(text);
					selectedSolManager4 = positionItem;
					break;
				case Constant.USER_SOLUTION_PARTNER1:
					solutionPartner_TV.setText(text);
					selectedSolPartner1 = positionItem;
					break;
				case Constant.USER_SOLUTION_PARTNER2:
					solutionPartner_TV.setText(text);
					selectedSolPartner2 = positionItem;
					break;
				case Constant.USER_SOLUTION_PARTNER3:
					solutionPartner_TV.setText(text);
					selectedSolPartner3 = positionItem;
					break;
				case Constant.USER_SOLUTION_PARTNER4:
					solutionPartner_TV.setText(text);
					selectedSolPartner4 = positionItem;
					break;

				default:
					break;
				}

			}
			if (requestCode == Constant.SEARCH_SOLUTION) {
				System.out.println("check2");
				List<String> list = new ArrayList<String>(solutionMap.values());
				String text = list.get(positionItem);
				solutionName_TV.setText(text);
				switch (data.getIntExtra("solution_value", -1)) {
				case Constant.SOLUTION1_VISIBLE:
					System.out.println("check3");
					selectedSolName1 = positionItem;
					break;
				case Constant.SOLUTION2_VISIBLE:
					selectedSolName2 = positionItem;
					break;
				case Constant.SOLUTION3_VISIBLE:
					selectedSolName3 = positionItem;
					break;
				case Constant.SOLUTION4_VISIBLE:
					selectedSolName4 = positionItem;
					break;

				default:
					break;
				}

			}
			if (requestCode == Constant.SEARCH_PROFIT_CENTER) {
				List<String> list = new ArrayList<String>(
						profitCenterMap.values());
				String text = list.get(positionItem);
				profitCenter_TV.setText(text);
				switch (data.getIntExtra("profit_center_value", -1)) {
				case Constant.SOLUTION1_VISIBLE:
					selectedProfitCenter1 = positionItem;
					break;
				case Constant.SOLUTION2_VISIBLE:
					selectedProfitCenter2 = positionItem;
					break;
				case Constant.SOLUTION3_VISIBLE:
					selectedProfitCenter3 = positionItem;
					break;
				case Constant.SOLUTION4_VISIBLE:
					selectedProfitCenter4 = positionItem;
					break;

				default:
					break;
				}

			}
			if (requestCode == Constant.SEARCH_PRODUCT) {
				List<String> list = new ArrayList<String>(productMap.values());
				String text = list.get(positionItem);
				taxonomy_TV.setText(text);
				switch (data.getIntExtra("product_value", -1)) {
				case Constant.SOLUTION1_VISIBLE:
					selectedTaxonomy1 = positionItem;
					break;
				case Constant.SOLUTION2_VISIBLE:
					selectedTaxonomy2 = positionItem;
					break;
				case Constant.SOLUTION3_VISIBLE:
					selectedTaxonomy3 = positionItem;
					break;
				case Constant.SOLUTION4_VISIBLE:
					selectedTaxonomy4 = positionItem;
					break;

				default:
					System.out.println("DEFAULT");
					break;
				}

			}
			if (requestCode == Constant.DETAILS_OPPORTUNITY) {
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

	public void onPickDate(View view) {
		System.out.println("date picker");
		Toast.makeText(this, "date picker", Toast.LENGTH_SHORT).show();
		super.onPickDate2(view, expectedClosureDate_TV);
	}
}
