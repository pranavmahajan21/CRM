package com.mw.crm.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Opportunity;
import com.mw.crm.model.Solution;

public class OpportunityDetailsActivity extends CRMActivity {

	Intent nextIntent, previousIntent;
	Opportunity selectedOpportunity;

	TextView crmIdLabel_TV, oppoDescriptionLabel_TV, clientNameLabel_TV,
			confidentialLabel_TV, leadSourceLabel_TV, salesStageLabel_TV,
			probabilityLabel_TV, statusLabel_TV, expectedClosureDateLabel_TV,
			totalProposalValueLabel_TV, noOfSolutionLabel_TV;

	TextView crmId_TV, oppoDescription_TV, clientName_TV, confidential_TV,
			leadSource_TV, salesStage_TV, probability_TV, status_TV,
			expectedClosureDate_TV, totalProposalValue_TV, noOfSolution_TV;

	/** Solution **/
	LinearLayout parentSolution1_LL, parentSolution2_LL, parentSolution3_LL;

	LinearLayout childSolution1_LL, childSolution2_LL, childSolution3_LL;
	/** Solution **/

	LayoutInflater inflater;
	LinearLayout view_solution;

	@SuppressLint("InflateParams")
	private LinearLayout getViewSolution() {
		return (LinearLayout) inflater.inflate(R.layout.view_solution_details,
				null, false);
	}

	private void initThings() {
		myApp.getAccountList();
		previousIntent = getIntent();

		if (previousIntent.hasExtra("opportunity_dummy")) {
			selectedOpportunity = new Gson().fromJson(
					previousIntent.getStringExtra("opportunity_dummy"),
					Opportunity.class);
		} else {
			selectedOpportunity = myApp.getOpportunityList().get(
					previousIntent.getIntExtra("position", 0));
		}
		inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view_solution = getViewSolution();
	}

	public void findThings() {
		super.findThings();
		crmIdLabel_TV = (TextView) findViewById(R.id.crmIdLabel_TV);
		oppoDescriptionLabel_TV = (TextView) findViewById(R.id.oppoDescriptionLabel_TV);
		clientNameLabel_TV = (TextView) findViewById(R.id.clientNameLabel_TV);
		confidentialLabel_TV = (TextView) findViewById(R.id.confidentialLabel_TV);
		leadSourceLabel_TV = (TextView) findViewById(R.id.leadSourceLabel_TV);
		salesStageLabel_TV = (TextView) findViewById(R.id.salesStageLabel_TV);
		probabilityLabel_TV = (TextView) findViewById(R.id.probabilityLabel_TV);
		statusLabel_TV = (TextView) findViewById(R.id.statusLabel_TV);
		expectedClosureDateLabel_TV = (TextView) findViewById(R.id.expectedClosureDateLabel_TV);
		totalProposalValueLabel_TV = (TextView) findViewById(R.id.totalProposalValueLabel_TV);
		noOfSolutionLabel_TV = (TextView) findViewById(R.id.noOfSolutionLabel_TV);

		crmId_TV = (TextView) findViewById(R.id.crmId_TV);
		oppoDescription_TV = (TextView) findViewById(R.id.oppoDescription_TV);
		clientName_TV = (TextView) findViewById(R.id.clientName_TV);
		confidential_TV = (TextView) findViewById(R.id.confidential_TV);
		leadSource_TV = (TextView) findViewById(R.id.leadSource_TV);
		salesStage_TV = (TextView) findViewById(R.id.salesStage_TV);
		probability_TV = (TextView) findViewById(R.id.probability_TV);
		status_TV = (TextView) findViewById(R.id.status_TV);
		expectedClosureDate_TV = (TextView) findViewById(R.id.expectedClosureDate_TV);
		totalProposalValue_TV = (TextView) findViewById(R.id.totalProposalValue_TV);
		noOfSolution_TV = (TextView) findViewById(R.id.noOfSolution_TV);

		parentSolution1_LL = (LinearLayout) findViewById(R.id.parentSolution1_LL);
		parentSolution2_LL = (LinearLayout) findViewById(R.id.parentSolution2_LL);
		parentSolution3_LL = (LinearLayout) findViewById(R.id.parentSolution3_LL);

		childSolution1_LL = (LinearLayout) findViewById(R.id.childSolution1_LL);
		childSolution2_LL = (LinearLayout) findViewById(R.id.childSolution2_LL);
		childSolution3_LL = (LinearLayout) findViewById(R.id.childSolution3_LL);
	}

	private void findViewSolution(LinearLayout childLL, int solutionIndex) {
		TextView solutionManager_TV = (TextView) childLL
				.findViewById(R.id.solutionManager_TV);
		TextView solutionPartner_TV = (TextView) childLL
				.findViewById(R.id.solutionPartner_TV);
		TextView solutionName_TV = (TextView) childLL
				.findViewById(R.id.solutionName_TV);
		TextView profitCenter_TV = (TextView) childLL
				.findViewById(R.id.profitCenter_TV);
		TextView taxonomy_TV = (TextView) childLL
				.findViewById(R.id.taxonomy_TV);

		TextView fee_TV = (TextView) childLL.findViewById(R.id.fee_TV);
		TextView pyNfr_TV = (TextView) childLL.findViewById(R.id.pyNfr_TV);
		TextView cyNfr_TV = (TextView) childLL.findViewById(R.id.cyNfr_TV);
		TextView cyNfrPlus1_TV = (TextView) childLL
				.findViewById(R.id.cyNfrPlus1_TV);
		TextView cyNfrPlus2_TV = (TextView) childLL
				.findViewById(R.id.cyNfrPlus2_TV);

		solutionManager_TV.setTypeface(myApp.getTypefaceRegularSans());
		solutionPartner_TV.setTypeface(myApp.getTypefaceRegularSans());
		solutionName_TV.setTypeface(myApp.getTypefaceRegularSans());
		profitCenter_TV.setTypeface(myApp.getTypefaceRegularSans());
		taxonomy_TV.setTypeface(myApp.getTypefaceRegularSans());

		fee_TV.setTypeface(myApp.getTypefaceRegularSans());
		pyNfr_TV.setTypeface(myApp.getTypefaceRegularSans());
		cyNfr_TV.setTypeface(myApp.getTypefaceRegularSans());
		cyNfrPlus1_TV.setTypeface(myApp.getTypefaceRegularSans());
		cyNfrPlus2_TV.setTypeface(myApp.getTypefaceRegularSans());

		Solution tempSolution = selectedOpportunity.getSolutionList().get(
				solutionIndex);

		solutionManager_TV.setText(tempSolution.getSolutionManager());
		solutionPartner_TV.setText(tempSolution.getSolutionPartner());
		solutionName_TV.setText(tempSolution.getSolutionName());
		profitCenter_TV.setText(tempSolution.getProfitCenter());
		taxonomy_TV.setText(tempSolution.getTaxonomy());

		fee_TV.setText(tempSolution.getFee());
		pyNfr_TV.setText(tempSolution.getPyNfr());
		cyNfr_TV.setText(tempSolution.getCyNfr());
		cyNfrPlus1_TV.setText(tempSolution.getCyNfr1());
		cyNfrPlus2_TV.setText(tempSolution.getCyNfr2());
	}

	private void setTypeface() {
		crmIdLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		oppoDescriptionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		clientNameLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		confidentialLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		leadSourceLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		salesStageLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		probabilityLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		statusLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		expectedClosureDateLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		totalProposalValueLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		noOfSolutionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		crmId_TV.setTypeface(myApp.getTypefaceRegularSans());
		oppoDescription_TV.setTypeface(myApp.getTypefaceRegularSans());
		clientName_TV.setTypeface(myApp.getTypefaceRegularSans());
		confidential_TV.setTypeface(myApp.getTypefaceRegularSans());
		leadSource_TV.setTypeface(myApp.getTypefaceRegularSans());
		salesStage_TV.setTypeface(myApp.getTypefaceRegularSans());
		probability_TV.setTypeface(myApp.getTypefaceRegularSans());
		status_TV.setTypeface(myApp.getTypefaceRegularSans());
		expectedClosureDate_TV.setTypeface(myApp.getTypefaceRegularSans());
		totalProposalValue_TV.setTypeface(myApp.getTypefaceRegularSans());
		noOfSolution_TV.setTypeface(myApp.getTypefaceRegularSans());

		((TextView) findViewById(R.id.left_button_TV)).setTypeface(myApp
				.getTypefaceBoldSans());
	}

	public void initView(String string, String string2) {
		super.initView(string, string2);
		setTypeface();

		if (previousIntent.hasExtra("opportunity_dummy")
				&& selectedOpportunity != null) {
			crmId_TV.setText("-");
			oppoDescription_TV.setText(selectedOpportunity.getDescription());
			clientName_TV.setText(selectedOpportunity.getCustomerId());
			confidential_TV.setText(selectedOpportunity.getIsConfidential());
			leadSource_TV.setText(selectedOpportunity.getLeadSource());
			probability_TV.setText(selectedOpportunity.getProbability());
			salesStage_TV.setText(selectedOpportunity.getSalesStage());
			status_TV.setText(selectedOpportunity.getKpmgStatus());

			expectedClosureDate_TV.setText(myApp.formatDateToString2(selectedOpportunity
					.getExpectedClosureDate()));
			totalProposalValue_TV.setText(selectedOpportunity.getTotalProposalValue());
			noOfSolution_TV.setText(selectedOpportunity.getNoOfSolutionRequired());

		} else {
			crmId_TV.setText(selectedOpportunity.getCrmId());
			oppoDescription_TV.setText(selectedOpportunity
					.getDescription());

			clientName_TV.setText(myApp
					.getStringNameFromStringJSON(selectedOpportunity
							.getCustomerId()));

			confidential_TV.setText(selectedOpportunity.getIsConfidential());

			leadSource_TV.setText(selectedOpportunity.getLeadSource());

			// oppoManager_TV.setText(myApp
			// .getStringNameFromStringJSON(selectedOpportunity
			// .getOwnerId()));

			Integer temp2 = myApp.getIntValueFromStringJSON(selectedOpportunity
					.getSalesStage());
			if (temp2 != null) {
				salesStage_TV.setText(myApp.getSalesStageMap().get(
						Integer.toString(temp2.intValue())));
				temp2 = null;
			}
			temp2 = myApp.getIntValueFromStringJSON(selectedOpportunity
					.getProbability());
			if (temp2 != null) {
				probability_TV.setText(myApp.getProbabilityMap().get(
						Integer.toString(temp2.intValue())));
				temp2 = null;
			}

			temp2 = myApp.getIntValueFromStringJSON(selectedOpportunity
					.getKpmgStatus());
			if (temp2 != null) {
				status_TV.setText(myApp.getStatusMap().get(
						Integer.toString(temp2.intValue())));
				temp2 = null;
			}

			expectedClosureDate_TV.setText(myApp
					.formatDateToString2(selectedOpportunity
							.getExpectedClosureDate()));
			totalProposalValue_TV.setText(selectedOpportunity
					.getTotalProposalValue());
			noOfSolution_TV.setText(selectedOpportunity
					.getNoOfSolutionRequired());

			int noOfSolution = 0;
			try {
				noOfSolution = Integer.parseInt(selectedOpportunity
						.getNoOfSolutionRequired());
			} catch (NumberFormatException exception) {
			}

			childSolution1_LL.addView(view_solution);
			findViewSolution(childSolution1_LL, 0);

			if (noOfSolution > 0) {
				parentSolution2_LL.setVisibility(View.VISIBLE);
				view_solution = getViewSolution();
				childSolution2_LL.addView(view_solution);
				findViewSolution(childSolution1_LL, 1);
			}
			if (noOfSolution > 1) {
				parentSolution3_LL.setVisibility(View.VISIBLE);
				view_solution = getViewSolution();
				childSolution3_LL.addView(view_solution);
				findViewSolution(childSolution1_LL, 2);
			}

			/*
			 * Account tempAccount = myApp.getAccountById(myApp
			 * .getStringIdFromStringJSON(selectedOpportunity
			 * .getCustomerId())); if (tempAccount != null) { Integer temp =
			 * myApp.getIntValueFromStringJSON(tempAccount .getCountry()); if
			 * (temp != null) {
			 * confidential_TV.setText(myApp.getCountryMap().get(
			 * Integer.toString(temp.intValue()))); temp = null; } temp =
			 * myApp.getIntValueFromStringJSON(tempAccount.getLob()); if (temp
			 * != null) { leadSource_TV.setText(myApp.getLobMap().get(
			 * Integer.toString(temp.intValue()))); temp = null; } temp =
			 * myApp.getIntValueFromStringJSON(tempAccount.getSubLob()); if
			 * (temp != null) {
			 * expectedClosureDate_TV.setText(myApp.getSubLobMap().get(
			 * Integer.toString(temp.intValue()))); temp = null; } temp =
			 * myApp.getIntValueFromStringJSON(tempAccount.getSector()); if
			 * (temp != null) {
			 * totalProposalValue_TV.setText(myApp.getSectorMap().get(
			 * Integer.toString(temp.intValue()))); temp = null; } } else {
			 * Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT)
			 * .show();
			 * 
			 * }
			 */
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opportunity_detail);

		initThings();
		findThings();

		if (previousIntent.hasExtra("opportunity_dummy")) {
			if (previousIntent.hasExtra("opportunity_created")
					&& previousIntent.getBooleanExtra("opportunity_created",
							false)) {
				initView("Created Opportunity", null);
			} else {
				initView("Updated Opportunity", null);
			}
		} else {
			initView("Opportunity", "Edit");
		}

		onExpand(findViewById(R.id.solution1_TV));
	}

	private void setChildLLTag(int visibility1, int visibility2, int visibility3) {
		if (visibility1 == View.GONE) {
			childSolution1_LL.setTag("false");
		}
		if (visibility2 == View.GONE) {
			childSolution2_LL.setTag("false");
		}
		if (visibility3 == View.GONE) {
			childSolution3_LL.setTag("false");
		}
	}

	private void setChildLLVisibility(int visibility1, int visibility2,
			int visibility3) {
		childSolution1_LL.setVisibility(visibility1);
		childSolution2_LL.setVisibility(visibility2);
		childSolution3_LL.setVisibility(visibility3);

		setChildLLTag(visibility1, visibility2, visibility3);
	}

	public void onExpand(View view) {
		boolean tagVisibility;
		switch (view.getId()) {
		case R.id.solution1_TV:
			tagVisibility = Boolean.parseBoolean((String) childSolution1_LL
					.getTag());
			tagVisibility = !tagVisibility;
			childSolution1_LL.setTag(String.valueOf(tagVisibility));

			if (tagVisibility) {
				setChildLLVisibility(View.VISIBLE, View.GONE, View.GONE);
			} else {
				childSolution1_LL.setVisibility(View.GONE);
			}
			break;
		case R.id.solution2_TV:
			tagVisibility = Boolean.parseBoolean((String) childSolution2_LL
					.getTag());
			tagVisibility = !tagVisibility;
			childSolution2_LL.setTag(String.valueOf(tagVisibility));

			if (tagVisibility) {
				setChildLLVisibility(View.GONE, View.VISIBLE, View.GONE);
			} else {
				childSolution2_LL.setVisibility(View.GONE);
			}
			break;
		case R.id.solution3_TV:
			tagVisibility = Boolean.parseBoolean((String) childSolution3_LL
					.getTag());
			tagVisibility = !tagVisibility;
			childSolution3_LL.setTag(String.valueOf(tagVisibility));

			if (tagVisibility) {
				setChildLLVisibility(View.GONE, View.GONE, View.VISIBLE);
			} else {
				childSolution3_LL.setVisibility(View.GONE);
			}
			break;
		default:
			break;
		}
	}

	public void onRightButton(View view) {
		nextIntent = new Intent(this, OpportunityAddActivity.class);
		nextIntent.putExtra("position",
				previousIntent.getIntExtra("position", 0));
		nextIntent.putExtra("is_edit_mode", true);
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	public void onLeftButton(View view) {
		nextIntent = new Intent(this, OpportunityCloseActivity.class);
		nextIntent.putExtra("position",
				previousIntent.getIntExtra("position", 0));
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	@Override
	public void onBack(View view) {
		Intent intent = new Intent();
		if (previousIntent.hasExtra("search_text")) {
			intent.putExtra("search_text",
					previousIntent.getStringExtra("search_text"));
		}
		setResult(RESULT_OK, intent);
		super.onBack(view);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		if (previousIntent.hasExtra("search_text")) {
			intent.putExtra("search_text",
					previousIntent.getStringExtra("search_text"));
		}
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent dataIntent) {
		super.onActivityResult(requestCode, resultCode, dataIntent);
		if (resultCode == RESULT_OK) {
			if (dataIntent != null) {
				if (previousIntent.hasExtra("search_text")) {
					dataIntent.putExtra("search_text",
							previousIntent.getStringExtra("search_text"));
				}
				setResult(RESULT_OK, dataIntent);
			}
			finish();
		}
	}

}
