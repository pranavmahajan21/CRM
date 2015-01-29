package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;

public class AccountAddActivity extends CRMActivity {

	MyApp myApp;

	TextView clientNameLabel_TV, sectorLabel_TV, headquarterLabel_TV,
			lobLabel_TV, sublobLabel_TV, leadPartnerLabel_TV,
			accountCategoryLabel_TV;
	TextView sector_TV, headquarter_TV, lob_TV, sublob_TV, leadPartner_TV,
			accountCategory_TV;

	EditText clientName_ET;

	RelativeLayout sector_RL, headquarter_RL, lob_RL, sublob_RL,
			leadPartner_RL, accountCategory_RL;

	Intent previousIntent, nextIntent;

	RequestQueue queue;

	int selectedSector = -1, selectedCountry = -1, selectedLob = -1,
			selectedSubLob = -1, selectedLeadPartner = -1,
			selectedAccountCategory = -1;

	Map<String, String> sectorMap;
	Map<String, String> countryMap;
	Map<String, String> lobMap;
	Map<String, String> subLobMap;
	Map<String, String> accountCategoryMap;
	Map<String, String> userMap;

	CreateDialog createDialog;
	ProgressDialog progressDialog;

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

		queue = Volley.newRequestQueue(this);
	}

	public void findThings() {
		super.findThings();

		clientNameLabel_TV = (TextView) findViewById(R.id.clientNameLabel_TV);
		sectorLabel_TV = (TextView) findViewById(R.id.sectorLabel_TV);
		headquarterLabel_TV = (TextView) findViewById(R.id.headquarterLabel_TV);
		lobLabel_TV = (TextView) findViewById(R.id.lobLabel_TV);
		sublobLabel_TV = (TextView) findViewById(R.id.sublobLabel_TV);
		leadPartnerLabel_TV = (TextView) findViewById(R.id.leadPartnerLabel_TV);
		accountCategoryLabel_TV = (TextView) findViewById(R.id.accountCategoryLabel_TV);

		sector_TV = (TextView) findViewById(R.id.sector_TV);
		headquarter_TV = (TextView) findViewById(R.id.headquarter_TV);
		lob_TV = (TextView) findViewById(R.id.lob_TV);
		sublob_TV = (TextView) findViewById(R.id.sublob_TV);
		leadPartner_TV = (TextView) findViewById(R.id.leadPartner_TV);
		accountCategory_TV = (TextView) findViewById(R.id.accountCategory_TV);

		clientName_ET = (EditText) findViewById(R.id.clientName_ET);

		sector_RL = (RelativeLayout) findViewById(R.id.sector_RL);
		headquarter_RL = (RelativeLayout) findViewById(R.id.headquarter_RL);
		lob_RL = (RelativeLayout) findViewById(R.id.lob_RL);
		sublob_RL = (RelativeLayout) findViewById(R.id.sublob_RL);
		leadPartner_RL = (RelativeLayout) findViewById(R.id.leadPartner_RL);
		accountCategory_RL = (RelativeLayout) findViewById(R.id.accountCategory_RL);

	}

	private void setTypeface() {
		clientNameLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		sectorLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		headquarterLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		sublobLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		clientName_ET.setTypeface(myApp.getTypefaceRegularSans());

	}

	Account tempAccount;

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		if (previousIntent.hasExtra("position")) {
			tempAccount = myApp.getAccountList().get(
					previousIntent.getIntExtra("position", 0));

			clientName_ET.setText(tempAccount.getName());

			Integer temp = myApp.getValueFromStringJSON(tempAccount
					.getCountry());

			if (temp != null) {
				headquarter_TV.setText(myApp.getCountryMap().get(
						Integer.toString(temp.intValue())));
				selectedCountry = myApp.getIndexFromKeyCountryMap(Integer
						.toString(temp.intValue()));
			}
			temp = myApp.getValueFromStringJSON(tempAccount.getLob());
			if (temp != null) {
				lob_TV.setText(myApp.getLobMap().get(
						Integer.toString(temp.intValue())));
				selectedLob = myApp.getIndexFromKeyLobMap(Integer.toString(temp
						.intValue()));
			}
			temp = myApp.getValueFromStringJSON(tempAccount.getSubLob());
			if (temp != null) {
				sublob_TV.setText(myApp.getSubLobMap().get(
						Integer.toString(temp.intValue())));
				selectedSubLob = myApp.getIndexFromKeySubLobMap(Integer
						.toString(temp.intValue()));
			}
			temp = myApp.getValueFromStringJSON(tempAccount.getSector());
			if (temp != null) {
				sector_TV.setText(myApp.getSectorMap().get(
						Integer.toString(temp.intValue())));
				selectedSector = myApp.getIndexFromKeySectorMap(Integer
						.toString(temp.intValue()));
			}
			temp = myApp.getValueFromStringJSON(tempAccount
					.getAccountCategory());
			if (temp != null) {
				accountCategory_TV.setText(myApp.getAccountCategoryMap().get(
						Integer.toString(temp.intValue())));
				selectedAccountCategory = myApp.getIndexFromKeyAccountMap(Integer
						.toString(temp.intValue()));
			}

			leadPartner_TV.setText(myApp
					.getStringNameFromStringJSON(tempAccount.getLeadPartner()));
			selectedLeadPartner = myApp.getIndexFromKeyUserMap(myApp
					.getStringIdFromStringJSON(tempAccount.getLeadPartner()));

			System.out
					.println("selectedSector  : " + selectedSector
							+ "\nselectedCountry  : " + selectedCountry
							+ "\nselectedLob  : " + selectedLob
							+ "\nselectedSubLob  : " + selectedSubLob
							+ "\nselectedLeadPartner  : " + selectedLeadPartner
							+ "\nselectedAccountCategory  : "
							+ selectedAccountCategory);

		}
	}

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
		initView("Add Account", "Submit");

		hideKeyboardFunctionality();

		// registerForContextMenu(corridor_RL);
		registerForContextMenu(sector_RL);
		registerForContextMenu(headquarter_RL);
		registerForContextMenu(lob_RL);
		registerForContextMenu(sublob_RL);
		registerForContextMenu(leadPartner_RL);
		registerForContextMenu(accountCategory_RL);

	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		// if (v.getId() == R.id.sector_RL) {
		// sectorMap = myApp.getSectorMap();
		//
		// List<String> list = new ArrayList<String>(sectorMap.values());
		// for (int i = 0; i < list.size(); i++) {
		// menu.add(0, v.getId(), i, list.get(i));
		// }
		//
		// }
		// if (v.getId() == R.id.headquarter_RL) {
		// countryMap = myApp.getCountryMap();
		//
		// List<String> list = new ArrayList<String>(countryMap.values());
		// for (int i = 0; i < list.size(); i++) {
		// menu.add(0, v.getId(), i, list.get(i));
		// }
		//
		// }
		if (v.getId() == R.id.lob_RL) {
			lobMap = myApp.getLobMap();

			List<String> list = new ArrayList<String>(lobMap.values());
			for (int i = 0; i < list.size(); i++) {
				menu.add(0, v.getId(), i, list.get(i));
			}

		}
		// if (v.getId() == R.id.sublob_RL) {
		// subLobMap = myApp.getSubLobMap();
		//
		// List<String> list = new ArrayList<String>(subLobMap.values());
		// for (int i = 0; i < list.size(); i++) {
		// menu.add(0, v.getId(), i, list.get(i));
		// }
		//
		// // menu.add(1, v.getId(), 0, "CIM-CM-CP-Cosmetics-Toiletries");
		// // menu.add(1, v.getId(), 0, "CIM-CM-CP-Ele Goods-Household");
		// // menu.add(1, v.getId(), 0, "CIM-CM-CP-Furniture-furnishing");
		// // menu.add(1, v.getId(), 0, "CIM-CM-CP-Others");
		// // menu.add(1, v.getId(), 0, "CIM-CM-CP-Photographic Equipment");
		// // menu.add(1, v.getId(), 0, "CIM-CM-CP-Security Sys-Svcs");
		// // menu.add(1, v.getId(), 0, "CIM-CM-CP-Textiles-Clothing");
		// // menu.add(1, v.getId(), 0, "CIM-CM-CP-Toys-Games");
		// // menu.add(1, v.getId(), 0, "CIM-CM-FD-Agricltre-Fisheries");
		// // menu.add(1, v.getId(), 0, "CIM-CM-FD-Bevrgs/Drinks-Alghic");
		// // menu.add(1, v.getId(), 0, "CIM-CM-FD-Bvrg/Drinks-Non A/C");
		// // menu.add(1, v.getId(), 0, "CIM-CM-FD-Food Production");
		// }
		if (v.getId() == R.id.leadPartner_RL) {
			// countryMap = myApp.getCountryMap();
			//
			// List<String> list = new ArrayList<String>(countryMap.values());
			// for (int i = 0; i < list.size(); i++) {
			// menu.add(0, v.getId(), i, list.get(i));
			// }

		}
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

		/** important code for mapping. DO NOT DELETE. **/
		// if (item.getGroupId() == 0) {
		// headquarter_TV.setText(item.getTitle());
		// List<String> keys = new ArrayList<String>(countryMap.keySet());
		// keys.get(item.getOrder());
		//
		// Toast.makeText(AccountAddActivity.this,
		// countryMap.get(keys.get(item.getOrder())),
		// Toast.LENGTH_SHORT).show();
		// }

		if (item.getGroupId() == 0) {
			selectedLob = item.getOrder();
			lob_TV.setText(item.getTitle());
		} else if (item.getGroupId() == 1) {
			selectedAccountCategory = item.getOrder();
			accountCategory_TV.setText(item.getTitle());
		}
		return super.onContextItemSelected(item);

	}

	public void onOpenContextMenu(View view) {
		openContextMenu(view);
	}

	public void onRightButton(View view) {
		JSONObject params = new JSONObject();

		try {
			params.put("Name",
					MyApp.encryptData(clientName_ET.getText().toString()))
					.put("sectors",
							new ArrayList<String>(sectorMap.keySet())
									.get(selectedSector))
					.put("cty",
							new ArrayList<String>(countryMap.keySet())
									.get(selectedCountry))
					.put("lobusiness",
							new ArrayList<String>(lobMap.keySet())
									.get(selectedLob))
					.put("slob",
							new ArrayList<String>(subLobMap.keySet())
									.get(selectedSubLob))
					.put("leadpartner",
							new ArrayList<String>(userMap.keySet())
									.get(selectedLeadPartner))
					.put("accountCategoryOnes",
							new ArrayList<String>(accountCategoryMap.keySet())
									.get(selectedAccountCategory));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		params = MyApp.addParamToJson(params);
		System.out.println("json" + params);

		if (previousIntent.hasExtra("is_edit_mode")
				&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
			/** Update Mode **/
			String url = MyApp.URL + MyApp.ACCOUNTS_UPDATE;

			try {
				params.put("accid", tempAccount.getAccountId());

				System.out.println("URL : " + url);

				JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
						Method.POST, url, params,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								System.out.println("length2" + response);
								progressDialog.hide();
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
				e.printStackTrace();
				progressDialog.hide();
			}
		} else {
			/** Create Mode **/
			String url = MyApp.URL + MyApp.ACCOUNTS_ADD;
			try {
				System.out.println("URL : " + url);

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
				e.printStackTrace();
			}
		}
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra("request_code", requestCode);
		super.startActivityForResult(intent, requestCode);
	}

	public void onSearchItem(View view) {
		nextIntent = new Intent(this, SearchActivity.class);

		switch (view.getId()) {
		case R.id.sector_RL:
			startActivityForResult(nextIntent, MyApp.SEARCH_SECTOR);
			break;

		case R.id.headquarter_RL:
			startActivityForResult(nextIntent, MyApp.SEARCH_HQ_COUNTRY);
			break;
		case R.id.sublob_RL:
			startActivityForResult(nextIntent, MyApp.SEARCH_SUB_LOB);
			break;
		case R.id.leadPartner_RL:
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
			if (requestCode == MyApp.SEARCH_SECTOR) {
				List<String> list = new ArrayList<String>(sectorMap.values());
				sector_TV.setText(list.get(positionItem));
				selectedSector = positionItem;
				System.out.println("selectedSector  :  " + selectedSector);
			}
			if (requestCode == MyApp.SEARCH_HQ_COUNTRY) {
				List<String> list = new ArrayList<String>(countryMap.values());
				headquarter_TV.setText(list.get(positionItem));
				selectedCountry = positionItem;
				System.out.println("selectedCountry  :  " + selectedCountry);
			}
			if (requestCode == MyApp.SEARCH_SUB_LOB) {
				// subLobMap = myApp.getSubLobMap();
				List<String> list = new ArrayList<String>(subLobMap.values());
				// System.out.println("asdsad  :  " + list.get(positionItem));
				sublob_TV.setText(list.get(positionItem));
				selectedSubLob = positionItem;
				System.out.println("selectedSubLob  :  " + selectedSubLob);
			}
			if (requestCode == MyApp.SEARCH_USER) {
				List<String> list = new ArrayList<String>(userMap.values());
				leadPartner_TV.setText(list.get(positionItem));
				selectedLeadPartner = positionItem;
				System.out.println("selectedLeadPartner  :  "
						+ selectedLeadPartner);
			}
		}

	}

}
