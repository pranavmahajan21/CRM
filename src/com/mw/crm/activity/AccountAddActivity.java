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
import com.mw.crm.extra.SearchEngine;
import com.mw.crm.model.Account;
import com.mw.crm.service.AccountService;

public class AccountAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	TextView clientNameLabel_TV, sectorLabel_TV, headquarterLabel_TV,
			leadPartnerLabel_TV, relPartner1Label_TV, relPartner2Label_TV,
			relPartner3Label_TV, bdmLabel_TV, accountCategoryLabel_TV;

	TextView sector_TV, headquarter_TV, leadPartner_TV, relPartner1_TV,
			relPartner2_TV, relPartner3_TV, bdm_TV, accountCategory_TV;

	EditText clientName_ET;

	RelativeLayout accountCategory_RL;
	
	Intent previousIntent, nextIntent;
	Account tempAccount;

	RequestQueue queue;
	SearchEngine searchEngine;
	
	int selectedCountry = -1, selectedSector = -1, selectedLeadPartner = -1,
			selectedAccountCategory = -1, selectedRelPartner1 = -1,
			selectedRelPartner2 = -1, selectedRelPartner3 = -1,
			selectedBDM = -1;
	
	Map<String, String> sectorMap;
	Map<String, String> countryMap;
	Map<String, String> lobMap;
	Map<String, String> subLobMap;
	Map<String, String> accountCategoryMap;
	Map<String, String> userMap;

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	private BroadcastReceiver accountUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			progressDialog.dismiss();

			Account aa = new Account(clientName_ET.getText().toString(), null,
					headquarter_TV.getText().toString(), null, null,
					accountCategory_TV.getText().toString(), sector_TV
							.getText().toString(), leadPartner_TV.getText()
							.toString(), relPartner1_TV.getText().toString(),
					relPartner2_TV.getText().toString(), relPartner3_TV.getText().toString(), bdm_TV.getText()
							.toString());

			if (previousIntent.hasExtra("is_edit_mode")
					&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
				Toast.makeText(AccountAddActivity.this,
						"Account updated successfully.", Toast.LENGTH_SHORT)
						.show();
				aa.setAccountId(tempAccount.getAccountId());
			} else {
				Toast.makeText(AccountAddActivity.this,
						"Account created successfully.", Toast.LENGTH_SHORT)
						.show();
			}

			nextIntent = new Intent(AccountAddActivity.this,
					AccountDetailsActivity.class);
			nextIntent.putExtra("account_dummy",
					new Gson().toJson(aa, Account.class));

			// TODO: should we remove 2nd check. What is good practice??
			if (!(previousIntent.hasExtra("is_edit_mode") && previousIntent
					.getBooleanExtra("is_edit_mode", false))) {
				nextIntent.putExtra("account_created", true);
			}
			startActivityForResult(nextIntent, Constant.DETAILS_ACCOUNT);

		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		sectorMap = myApp.getSectorMap();
		countryMap = myApp.getCountryMap();
		lobMap = myApp.getLobMap();
		subLobMap = myApp.getSubLobMap();
		accountCategoryMap = myApp.getAccountCategoryMap();
		userMap = myApp.getUserMap();

		if (previousIntent.hasExtra("position")) {
		}

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Saving Changes",
				"This may take some time", true, null);

		searchEngine = new SearchEngine(this);
		queue = Volley.newRequestQueue(this);
	}

	public void findThings() {
		super.findThings();

		clientNameLabel_TV = (TextView) findViewById(R.id.clientNameLabel_TV);
		sectorLabel_TV = (TextView) findViewById(R.id.sectorLabel_TV);
		headquarterLabel_TV = (TextView) findViewById(R.id.headquarterLabel_TV);
		// lobLabel_TV = (TextView) findViewById(R.id.lobLabel_TV);
		// sublobLabel_TV = (TextView) findViewById(R.id.sublobLabel_TV);
		leadPartnerLabel_TV = (TextView) findViewById(R.id.leadPartnerLabel_TV);
		relPartner1Label_TV = (TextView) findViewById(R.id.relPartner1Label_TV);
		relPartner2Label_TV = (TextView) findViewById(R.id.relPartner2Label_TV);
		relPartner3Label_TV = (TextView) findViewById(R.id.relPartner3Label_TV);
		bdmLabel_TV = (TextView) findViewById(R.id.bdmLabel_TV);
		accountCategoryLabel_TV = (TextView) findViewById(R.id.accountCategoryLabel_TV);

		sector_TV = (TextView) findViewById(R.id.sector_TV);
		headquarter_TV = (TextView) findViewById(R.id.headquarter_TV);
		// lob_TV = (TextView) findViewById(R.id.lob_TV);
		// sublob_TV = (TextView) findViewById(R.id.sublob_TV);
		leadPartner_TV = (TextView) findViewById(R.id.leadPartner_TV);
		relPartner1_TV = (TextView) findViewById(R.id.relPartner1_TV);
		relPartner2_TV = (TextView) findViewById(R.id.relPartner2_TV);
		relPartner3_TV = (TextView) findViewById(R.id.relPartner3_TV);
		bdm_TV = (TextView) findViewById(R.id.bdm_TV);
		accountCategory_TV = (TextView) findViewById(R.id.accountCategory_TV);

		clientName_ET = (EditText) findViewById(R.id.clientName_ET);

//		lob_RL = (RelativeLayout) findViewById(R.id.lob_RL);
		accountCategory_RL = (RelativeLayout) findViewById(R.id.accountCategory_RL);

	}

	private void setTypeface() {
		clientNameLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		sectorLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		headquarterLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// lobLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// sublobLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		leadPartnerLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner1Label_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner2Label_TV.setTypeface(myApp.getTypefaceRegularSans());
		bdmLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		accountCategoryLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		sector_TV.setTypeface(myApp.getTypefaceRegularSans());
		headquarter_TV.setTypeface(myApp.getTypefaceRegularSans());
		// lob_TV.setTypeface(myApp.getTypefaceRegularSans());
		// sublob_TV.setTypeface(myApp.getTypefaceRegularSans());
		leadPartner_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner1_TV.setTypeface(myApp.getTypefaceRegularSans());
		relPartner2_TV.setTypeface(myApp.getTypefaceRegularSans());
		bdm_TV.setTypeface(myApp.getTypefaceRegularSans());
		accountCategory_TV.setTypeface(myApp.getTypefaceRegularSans());

		clientName_ET.setTypeface(myApp.getTypefaceRegularSans());

	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		if (previousIntent.hasExtra("position")) {
			tempAccount = myApp.getAccountList().get(
					previousIntent.getIntExtra("position", 0));

			clientName_ET.setText(tempAccount.getName());

			Integer temp = myApp.getIntValueFromStringJSON(tempAccount
					.getCountry());

			if (temp != null) {
				headquarter_TV.setText(myApp.getCountryMap().get(
						Integer.toString(temp.intValue())));
				selectedCountry = searchEngine.getIndexFromKeyCountryMap(Integer
						.toString(temp.intValue()));
			}
			// temp = myApp.getIntValueFromStringJSON(tempAccount.getLob());
			// if (temp != null) {
			// lob_TV.setText(myApp.getLobMap().get(
			// Integer.toString(temp.intValue())));
			// selectedLob = myApp.getIndexFromKeyLobMap(Integer.toString(temp
			// .intValue()));
			// }
			// temp = myApp.getIntValueFromStringJSON(tempAccount.getSubLob());
			// if (temp != null) {
			// sublob_TV.setText(myApp.getSubLobMap().get(
			// Integer.toString(temp.intValue())));
			// selectedSubLob = myApp.getIndexFromKeySubLobMap(Integer
			// .toString(temp.intValue()));
			// }
			temp = myApp.getIntValueFromStringJSON(tempAccount.getSector());
			if (temp != null) {
				sector_TV.setText(myApp.getSectorMap().get(
						Integer.toString(temp.intValue())));
				selectedSector = searchEngine.getIndexFromKeySectorMap(Integer
						.toString(temp.intValue()));
			}
			temp = myApp.getIntValueFromStringJSON(tempAccount
					.getAccountCategory());
			if (temp != null) {
				accountCategory_TV.setText(myApp.getAccountCategoryMap().get(
						Integer.toString(temp.intValue())));
				selectedAccountCategory = searchEngine
						.getIndexFromKeyAccountCategoryMap(Integer.toString(temp
								.intValue()));
			}

			leadPartner_TV.setText(myApp
					.getStringNameFromStringJSON(tempAccount.getLeadPartner()));
			selectedLeadPartner = searchEngine.getIndexFromKeyUserMap(myApp
					.getStringIdFromStringJSON(tempAccount.getLeadPartner()));

			relPartner1_TV.setText(myApp
					.getStringNameFromStringJSON(tempAccount
							.getRelationshipPartner1()));
			selectedRelPartner1 = searchEngine.getIndexFromKeyUserMap(myApp
					.getStringIdFromStringJSON(tempAccount
							.getRelationshipPartner1()));

			relPartner2_TV.setText(myApp
					.getStringNameFromStringJSON(tempAccount
							.getRelationshipPartner2()));
			selectedRelPartner2 = searchEngine.getIndexFromKeyUserMap(myApp
					.getStringIdFromStringJSON(tempAccount
							.getRelationshipPartner2()));

			relPartner3_TV.setText(myApp
					.getStringNameFromStringJSON(tempAccount
							.getRelationshipPartner3()));
			selectedRelPartner3 = searchEngine.getIndexFromKeyUserMap(myApp
					.getStringIdFromStringJSON(tempAccount
							.getRelationshipPartner3()));
			
			bdm_TV.setText(myApp
					.getStringNameFromStringJSON(tempAccount
							.getBusinessDevelopmentManager()));
			selectedBDM = searchEngine.getIndexFromKeyUserMap(myApp
					.getStringIdFromStringJSON(tempAccount
							.getBusinessDevelopmentManager()));

			System.out
					.println("selectedSector  : " + selectedSector
							+ "\nselectedCountry  : " + selectedCountry
//							+ "\nselectedLob  : " + selectedLob
//							+ "\nselectedSubLob  : " + selectedSubLob
							+ "\nselectedLeadPartner  : " + selectedLeadPartner
							+ "\nselectedRelMngr1  : " + selectedRelPartner1
							+ "\nselectedRelMngr2  : " + selectedRelPartner2
							+ "\nselectedRelMngr3  : " + selectedRelPartner3
							+ "\nselectedAccountCategory  : "
							+ selectedAccountCategory);

		}
	}

	@SuppressLint("ClickableViewAccessibility")
	private void hideKeyboardFunctionality() {
		((RelativeLayout) findViewById(R.id.activity_account_add_RL))
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
		setContentView(R.layout.activity_account_add);

		initThings();
		findThings();
		if ((previousIntent.hasExtra("is_edit_mode") && previousIntent
				.getBooleanExtra("is_edit_mode", false))) {
			initView("Modify Account", "Save");
		} else {
			initView("Add Account", "Save");
		}
		hideKeyboardFunctionality();

//		registerForContextMenu(lob_RL);
		registerForContextMenu(accountCategory_RL);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		// if (v.getId() == R.id.lob_RL) {
		// lobMap = myApp.getLobMap();
		//
		// List<String> list = new ArrayList<String>(lobMap.values());
		// for (int i = 0; i < list.size(); i++) {
		// menu.add(0, v.getId(), i, list.get(i));
		// }
		// }

		if (v.getId() == R.id.accountCategory_RL) {
			accountCategoryMap = myApp.getAccountCategoryMap();

			List<String> list = new ArrayList<String>(
					accountCategoryMap.values());
			for (int i = 0; i < list.size(); i++) {
				menu.add(1, v.getId(), i, list.get(i));
			}
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		// if (item.getGroupId() == 0) {
		// selectedLob = item.getOrder();
		// lob_TV.setText(item.getTitle());
		// } else
		if (item.getGroupId() == 1) {
			selectedAccountCategory = item.getOrder();
			accountCategory_TV.setText(item.getTitle());
		}
		return super.onContextItemSelected(item);

	}

	public void onOpenContextMenu(View view) {
		hideKeyboard(this.getCurrentFocus());
		openContextMenu(view);
	}

	private boolean validate() {
		boolean notErrorCase = true;
		if (clientName_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter some Client Name.", false);
			notErrorCase = false;
		} else if (selectedCountry < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a country.", false);
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
					MyApp.encryptData(clientName_ET.getText().toString())).put(
					"cty",
					new ArrayList<String>(countryMap.keySet())
							.get(selectedCountry));

			if (selectedSector > -1) {
				params.put("sectors", new ArrayList<String>(sectorMap.keySet())
						.get(selectedSector));
			}
			if (selectedLeadPartner > -1) {
				params.put("leadpartner",
						new ArrayList<String>(userMap.keySet())
								.get(selectedLeadPartner));
			}
			if (selectedRelPartner1 > -1) {
				params.put("relationshippartnerone", new ArrayList<String>(
						userMap.keySet()).get(selectedRelPartner1));
			}
			if (selectedRelPartner2 > -1) {
				params.put("relationshippartnertwo", new ArrayList<String>(
						userMap.keySet()).get(selectedRelPartner2));
			}
			if (selectedRelPartner3 > -1) {
				params.put("relationshippartnerthree", new ArrayList<String>(
						userMap.keySet()).get(selectedRelPartner3));
			}
			if (selectedBDM > -1) {
				params.put("bdmanager", new ArrayList<String>(userMap.keySet())
						.get(selectedBDM));
			}
			if (selectedAccountCategory > -1) {
				params.put("accountCategoryOnes", new ArrayList<String>(
						accountCategoryMap.keySet())
						.get(selectedAccountCategory));
			}
			if (previousIntent.hasExtra("is_edit_mode")
					&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
				params.put("accid", tempAccount.getAccountId());
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		String url;
		if (previousIntent.hasExtra("is_edit_mode")
				&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
			/** Update Mode **/
			url = Constant.URL + Constant.ACCOUNTS_UPDATE;
		} else {
			url = Constant.URL + Constant.ACCOUNTS_ADD;
		}

		params = MyApp.addParamToJson(params);
		System.out.println("json" + params);
		progressDialog.show();
		try {
			System.out.println("URL : " + url);

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
							
							AlertDialog alertDialog = myApp.handleError(createDialog,error,"Error while creating account.");
							alertDialog.show();
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

	// }

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		/**
		 * We need to put requestCode as extra because it is used in
		 * SearchActivity to determine the search list. It is also required as
		 * requestCode because it is used in onActivityResult() to filter
		 * results.
		 **/
		intent.putExtra("request_code", requestCode);
		super.startActivityForResult(intent, requestCode);
	}

	public void onSearchItem(View view) {
		nextIntent = new Intent(this, SearchActivity.class);

		switch (view.getId()) {
		case R.id.sector_RL:
			startActivityForResult(nextIntent, Constant.SEARCH_SECTOR);
			break;
		case R.id.headquarter_RL:
			startActivityForResult(nextIntent, Constant.SEARCH_HQ_COUNTRY);
			break;
		case R.id.leadPartner_RL:
			nextIntent.putExtra("user_value", 0);
			startActivityForResult(nextIntent, Constant.SEARCH_USER);
			break;
		case R.id.relPartner1_RL:
			nextIntent.putExtra("user_value", 1);
			startActivityForResult(nextIntent, Constant.SEARCH_USER);
			break;
		case R.id.relPartner2_RL:
			nextIntent.putExtra("user_value", 2);
			startActivityForResult(nextIntent, Constant.SEARCH_USER);
			break;
		case R.id.relPartner3_RL:
			nextIntent.putExtra("user_value", 3);
			startActivityForResult(nextIntent, Constant.SEARCH_USER);
			break;
		case R.id.bdm_RL:
			nextIntent.putExtra("user_value", 4);
			startActivityForResult(nextIntent, Constant.SEARCH_USER);
			break;

		default:
			break;
		}

	}

	private void onPositiveResponse() {
		progressDialog.dismiss();

		progressDialog = createDialog.createProgressDialog("Updating Account",
				"This may take some time", true, null);
		progressDialog.show();

		Intent serviceIntent = new Intent(this, AccountService.class);
		startService(serviceIntent);

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
			if (requestCode == Constant.SEARCH_SECTOR) {
				List<String> list = new ArrayList<String>(sectorMap.values());
				sector_TV.setText(list.get(positionItem));
				selectedSector = positionItem;
				System.out.println("selectedSector  :  " + selectedSector);
			}
			if (requestCode == Constant.SEARCH_HQ_COUNTRY) {
				List<String> list = new ArrayList<String>(countryMap.values());
				headquarter_TV.setText(list.get(positionItem));
				selectedCountry = positionItem;
				System.out.println("selectedCountry  :  " + selectedCountry);
			}
			// if (requestCode == MyApp.SEARCH_SUB_LOB) {
			// List<String> list = new ArrayList<String>(subLobMap.values());
			// sublob_TV.setText(list.get(positionItem));
			// selectedSubLob = positionItem;
			// System.out.println("selectedSubLob  :  " + selectedSubLob);
			// }
			if (requestCode == Constant.SEARCH_USER) {
				List<String> list = new ArrayList<String>(userMap.values());
				String text = list.get(positionItem);
				switch (data.getIntExtra("user_value", -1)) {
				case 0:
					leadPartner_TV.setText(text);
					selectedLeadPartner = positionItem;
					break;
				case 1:
					relPartner1_TV.setText(text);
					selectedRelPartner1 = positionItem;
					break;
				case 2:
					relPartner2_TV.setText(text);
					selectedRelPartner2 = positionItem;
					break;
				case 3:
					relPartner3_TV.setText(text);
					selectedRelPartner3 = positionItem;
					break;
				case 4:
					bdm_TV.setText(text);
					selectedBDM = positionItem;
					break;

				default:
					break;
				}
				// leadPartner_TV.setText(list.get(positionItem));
				// selectedLeadPartner = positionItem;
				// System.out.println("selectedLeadPartner  :  "
				// + selectedLeadPartner);
			}
			if (requestCode == Constant.DETAILS_ACCOUNT) {
//				Intent intent = new Intent();
//				intent.putExtra("refresh_list", true);
//				if (previousIntent.hasExtra("search_text")) {
//					intent.putExtra("search_text",
//							previousIntent.getStringExtra("search_text"));
//				}
				Intent intent = new MyApp().getIntenWithPreviousSearch(previousIntent);
				intent.putExtra("refresh_list", true);
				
				setResult(RESULT_OK, intent);
				finish();
			}
		}

	}

	@Override
	protected void onResume() {
		super.onResume();
		isActivityVisible = true;
		LocalBroadcastManager.getInstance(this).registerReceiver(
				accountUpdateReceiver,
				new IntentFilter("account_update_receiver"));

	}

	@Override
	protected void onPause() {
		isActivityVisible = false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				accountUpdateReceiver);
		super.onPause();
	}
}
