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
import com.mw.crm.application.MyApp;
import com.mw.crm.extra.DateFormatter;
import com.mw.crm.model.Opportunity;
import com.mw.crm.model.Solution;

public class OpportunityDetailsActivity extends CRMActivity {

	Intent previousIntent, nextIntent;
	Opportunity selectedOpportunity;

	TextView crmIdLabel_TV, oppoDescriptionLabel_TV, clientNameLabel_TV,
			confidentialLabel_TV, leadSourceLabel_TV, salesStageLabel_TV,
			probabilityLabel_TV, statusLabel_TV, expectedClosureDateLabel_TV,
			totalProposalValueLabel_TV, noOfSolutionLabel_TV;

	TextView crmId_TV, oppoDescription_TV, clientName_TV, confidential_TV,
			leadSource_TV, salesStage_TV, probability_TV, status_TV,
			expectedClosureDate_TV, totalProposalValue_TV, noOfSolution_TV;

	TextView left_button_TV;
	/** Solution **/
	LinearLayout parentSolution1_LL, parentSolution2_LL, parentSolution3_LL,
			parentSolution4_LL;

	LinearLayout childSolution1_LL, childSolution2_LL, childSolution3_LL,
			childSolution4_LL;
	/** Solution **/

	int noOfSolution = 0;

	LayoutInflater inflater;
	LinearLayout view_solution;

	DateFormatter dateFormatter;

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

		dateFormatter = new DateFormatter();
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

		left_button_TV = (TextView) findViewById(R.id.left_button_TV);

		parentSolution1_LL = (LinearLayout) findViewById(R.id.parentSolution1_LL);
		parentSolution2_LL = (LinearLayout) findViewById(R.id.parentSolution2_LL);
		parentSolution3_LL = (LinearLayout) findViewById(R.id.parentSolution3_LL);
		parentSolution4_LL = (LinearLayout) findViewById(R.id.parentSolution4_LL);

		childSolution1_LL = (LinearLayout) findViewById(R.id.childSolution1_LL);
		childSolution2_LL = (LinearLayout) findViewById(R.id.childSolution2_LL);
		childSolution3_LL = (LinearLayout) findViewById(R.id.childSolution3_LL);
		childSolution4_LL = (LinearLayout) findViewById(R.id.childSolution4_LL);
	}

	private void findAndInitViewSolution(LinearLayout childLL, int solutionIndex) {
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

		System.out.println("lits size : "
				+ selectedOpportunity.getSolutionList().size() + "index  : "
				+ solutionIndex);

		Solution tempSolution = selectedOpportunity.getSolutionList().get(
				solutionIndex);
		System.out.println("tempSolution  : " + tempSolution.toString());
		if (previousIntent.hasExtra("opportunity_dummy")) {
			System.out.println("opportunity_dummy");
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
		} else {
			System.out.println("else");
			solutionManager_TV.setText(myApp
					.getStringNameFromStringJSON(tempSolution
							.getSolutionManager()));
			solutionPartner_TV.setText(myApp
					.getStringNameFromStringJSON(tempSolution
							.getSolutionPartner()));
			solutionName_TV
					.setText(myApp.getStringNameFromStringJSON(tempSolution
							.getSolutionName()));
			profitCenter_TV
					.setText(myApp.getStringNameFromStringJSON(tempSolution
							.getProfitCenter()));
			taxonomy_TV.setText(myApp.getStringNameFromStringJSON(tempSolution
					.getTaxonomy()));

			fee_TV.setText(myApp.getDoubleValueFromStringJSON(tempSolution
					.getFee()) + "");
			pyNfr_TV.setText(myApp.getDoubleValueFromStringJSON(tempSolution
					.getPyNfr()) + "");
			cyNfr_TV.setText(myApp.getDoubleValueFromStringJSON(tempSolution
					.getCyNfr()) + "");
			cyNfrPlus1_TV.setText(myApp
					.getDoubleValueFromStringJSON(tempSolution.getCyNfr1())
					+ "");
			cyNfrPlus2_TV.setText(myApp
					.getDoubleValueFromStringJSON(tempSolution.getCyNfr2())
					+ "");
		}
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
			/** We are coming from create/edit screen **/
			crmId_TV.setText("-");
			oppoDescription_TV.setText(selectedOpportunity.getDescription());
			clientName_TV.setText(selectedOpportunity.getCustomerId());
			confidential_TV.setText(selectedOpportunity.getIsConfidential());
			leadSource_TV.setText(selectedOpportunity.getLeadSource());
			probability_TV.setText(selectedOpportunity.getProbability());
			salesStage_TV.setText(selectedOpportunity.getSalesStage());
			status_TV.setText(selectedOpportunity.getKpmgStatus());

			expectedClosureDate_TV.setText(dateFormatter
					.formatDateToString2(selectedOpportunity
							.getExpectedClosureDate()));
			totalProposalValue_TV.setText(selectedOpportunity
					.getTotalProposalValue());
			noOfSolution_TV.setText(selectedOpportunity
					.getNoOfSolutionRequired());

			noOfSolution = Integer.parseInt(selectedOpportunity
					.getNoOfSolutionRequired());

			initAllSolutions();

		} else {
			/** We are coming from List screen **/
			crmId_TV.setText(selectedOpportunity.getCrmId());
			oppoDescription_TV.setText(selectedOpportunity.getDescription());

			clientName_TV.setText(myApp
					.getStringNameFromStringJSON(selectedOpportunity
							.getCustomerId()));

			confidential_TV.setText(selectedOpportunity.getIsConfidential());

			Integer temp2 = myApp.getIntValueFromStringJSON(selectedOpportunity
					.getSalesStage());
			if (temp2 != null) {
				leadSource_TV.setText(myApp.getLeadSourceMap().get(
						Integer.toString(temp2.intValue())));
				temp2 = null;
			}

			temp2 = myApp.getIntValueFromStringJSON(selectedOpportunity
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

			temp2 = myApp.getIntValueFromStringJSON(selectedOpportunity
					.getTotalProposalValue());
			if (temp2 != null) {
				totalProposalValue_TV
						.setText(Integer.toString(temp2.intValue()));
				temp2 = null;
			}

			expectedClosureDate_TV.setText(dateFormatter
					.formatDateToString2(selectedOpportunity
							.getExpectedClosureDate()));

			temp2 = myApp.getIntValueFromStringJSON(selectedOpportunity
					.getNoOfSolutionRequired());

			if (temp2 != null) {
				noOfSolution = temp2;
				noOfSolution_TV.setText(Integer.toString(temp2.intValue()));
				temp2 = null;
			}

			initAllSolutions();
		}
	}

	private void initAllSolutions() {
		childSolution1_LL.addView(view_solution);
		findAndInitViewSolution(childSolution1_LL, 0);

		if (noOfSolution > 1) {
			parentSolution2_LL.setVisibility(View.VISIBLE);
			view_solution = getViewSolution();
			childSolution2_LL.addView(view_solution);
			findAndInitViewSolution(childSolution2_LL, 1);
		}
		if (noOfSolution > 2) {
			parentSolution3_LL.setVisibility(View.VISIBLE);
			view_solution = getViewSolution();
			childSolution3_LL.addView(view_solution);
			findAndInitViewSolution(childSolution3_LL, 2);
		}
		if (noOfSolution > 3) {
			parentSolution4_LL.setVisibility(View.VISIBLE);
			view_solution = getViewSolution();
			childSolution4_LL.addView(view_solution);
			findAndInitViewSolution(childSolution4_LL, 3);
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_opportunity_detail);

		initThings();
		findThings();

		if (previousIntent.hasExtra("opportunity_dummy")) {
			left_button_TV.setVisibility(View.GONE);
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
		case R.id.solution1_TV:
			tagVisibility = Boolean.parseBoolean((String) childSolution1_LL
					.getTag());
			tagVisibility = !tagVisibility;
			childSolution1_LL.setTag(String.valueOf(tagVisibility));

			if (tagVisibility) {
				setChildLLVisibility(View.VISIBLE, View.GONE, View.GONE,
						View.GONE);
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
				setChildLLVisibility(View.GONE, View.VISIBLE, View.GONE,
						View.GONE);
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
				setChildLLVisibility(View.GONE, View.GONE, View.VISIBLE,
						View.GONE);
			} else {
				childSolution3_LL.setVisibility(View.GONE);
			}
			break;
		case R.id.solution4_TV:
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
		// Intent intent = new Intent();
		// if (previousIntent.hasExtra("search_text")) {
		// intent.putExtra("search_text",
		// previousIntent.getStringExtra("search_text"));
		// }
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
		setResult(RESULT_OK, intent);
		super.onBack(view);
	}

	@Override
	public void onBackPressed() {
		// Intent intent = new Intent();
		// if (previousIntent.hasExtra("search_text")) {
		// intent.putExtra("search_text",
		// previousIntent.getStringExtra("search_text"));
		// }
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
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
				if (dataIntent.hasExtra("refresh_list")) {
					finish();
				}
			}
			if (requestCode == MyApp.NOTHING_ELSE_MATTERS) {
				status_TV.setText(dataIntent.getStringExtra("status"));
				left_button_TV.setVisibility(View.GONE);
			}
		}
	}

}
