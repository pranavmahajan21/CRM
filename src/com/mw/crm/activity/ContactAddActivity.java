package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.app.ProgressDialog;
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
import com.mw.crm.model.Contact;
import com.mw.crm.model.InternalConnect;

public class ContactAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	// String[] temp = new String[] { "Just Know", "High", "Medium", "Low",
	// "Comfortable" };

	MyApp myApp;

	TextView firstNameLabel_TV, lastNameLabel_TV, dorLabel_TV,
			internalConnectLabel_TV, organizationLabel_TV, designationLabel_TV,
			emailLabel_TV, officePhoneLabel_TV, mobileLabel_TV;

	TextView dor_TV, internalConnect_TV, organization_TV;
	EditText firstName_ET, lastName_ET, designation_ET, email_ET,
			officePhone_ET, mobile_ET;
	RelativeLayout dor_RL, organization_RL, internal_RL;

	boolean pickerVisibility = false;
	// NumberPicker picker;

	Intent previousIntent, nextIntent;

	int selectedDOR = -1, selectedInternalConnect = -1,
			selectedOrganisation = -1;
	List<InternalConnect> internalConnectList;
	List<Account> accountList;

	Map<String, String> dorMap;
	Map<String, String> userMap;

	Gson gson;
	RequestQueue queue;

	CreateDialog createDialog;
	ProgressDialog progressDialog;

	private BroadcastReceiver contactReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			registerForContextMenu(internal_RL);
			registerForContextMenu(organization_RL);
		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		accountList = myApp.getAccountList();
		dorMap = myApp.getDorMap();
		userMap = myApp.getUserMap();

		gson = new Gson();
		queue = Volley.newRequestQueue(this);

		createDialog = new CreateDialog(this);
		// progressDialog =
		// myApp.getCreateDialog().createProgressDialog("Saving Changes",
		// "This may take some time", true, null);
		progressDialog = createDialog.createProgressDialog("Saving Changes",
				"This may take some time", true, null);
	}

	public void findThings() {
		super.findThings();

		firstNameLabel_TV = (TextView) findViewById(R.id.firstNameLabel_TV);
		lastNameLabel_TV = (TextView) findViewById(R.id.lastNameLabel_TV);
		dorLabel_TV = (TextView) findViewById(R.id.dorLabel_TV);
		internalConnectLabel_TV = (TextView) findViewById(R.id.internalConnectLabel_TV);
		organizationLabel_TV = (TextView) findViewById(R.id.organizationLabel_TV);
		designationLabel_TV = (TextView) findViewById(R.id.designationLabel_TV);
		emailLabel_TV = (TextView) findViewById(R.id.emailLabel_TV);
		officePhoneLabel_TV = (TextView) findViewById(R.id.officePhoneLabel_TV);
		mobileLabel_TV = (TextView) findViewById(R.id.mobileLabel_TV);

		dor_TV = (TextView) findViewById(R.id.dor_TV);
		internalConnect_TV = (TextView) findViewById(R.id.internalConnect_TV);
		organization_TV = (TextView) findViewById(R.id.organization2_TV);

		firstName_ET = (EditText) findViewById(R.id.firstName_ET);
		lastName_ET = (EditText) findViewById(R.id.lastName_ET);
		designation_ET = (EditText) findViewById(R.id.designation_ET);
		email_ET = (EditText) findViewById(R.id.email_ET);
		officePhone_ET = (EditText) findViewById(R.id.officePhone_ET);
		mobile_ET = (EditText) findViewById(R.id.mobile_ET);

		dor_RL = (RelativeLayout) findViewById(R.id.dor_RL);
		internal_RL = (RelativeLayout) findViewById(R.id.internal_RL);
		organization_RL = (RelativeLayout) findViewById(R.id.organization_RL);

		// picker = (NumberPicker) findViewById(R.id.picker);
		//
		// picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		// picker.setMinValue(0);
		// picker.setMaxValue(4);
		// picker.setDisplayedValues(temp);

	}

	private void setTypeface() {
		// firstNameLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// lastNameLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// organizationLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// organization2_TV.setTypeface(myApp.getTypefaceRegularSans());
		// internalConnect_TV.setTypeface(myApp.getTypefaceRegularSans());
		// internalConnect2_TV.setTypeface(myApp.getTypefaceRegularSans());
		// dor_TV.setTypeface(myApp.getTypefaceRegularSans());
		// dor2_TV.setTypeface(myApp.getTypefaceRegularSans());
		//
		// firstName_ET.setTypeface(myApp.getTypefaceRegularSans());
		// lastName_ET.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		if (previousIntent.hasExtra("position")) {
			// getRightButtonTV().setVisibility(View.GONE);

			Contact tempContact = myApp.getContactList().get(
					previousIntent.getIntExtra("position", 0));

			System.out.println(tempContact.toString());

			firstName_ET.setText(tempContact.getFirstName());
			lastName_ET.setText(tempContact.getLastName());

			Integer temp = myApp.getValueFromStringJSON(tempContact
					.getDegreeOfRelation());
			if (temp != null) {
				dor_TV.setText(myApp.getDorMap().get(
						Integer.toString(temp.intValue())));
			}

			internalConnect_TV.setText(myApp
					.getStringFromStringJSON(tempContact.getInternalConnect()));
			organization_TV.setText(myApp.getStringFromStringJSON(tempContact
					.getOrganization()));
			designation_ET.setText(tempContact.getDesignation());
			email_ET.setText(tempContact.getEmail());
			officePhone_ET.setText(tempContact.getTelephone());
			mobile_ET.setText(tempContact.getMobilePhone());
		}

	}

	private void hideKeyboardFunctionality() {
		((RelativeLayout) findViewById(R.id.activity_contact_add_RL))
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
		setContentView(R.layout.activity_contact_add);

		initThings();
		findThings();
		initView("Add Contact", "Submit");

		hideKeyboardFunctionality();

		registerForContextMenu(dor_RL);
		// registerForContextMenu(internal_RL);
		// registerForContextMenu(organization_RL);

		// setPickerProperties();

	}

	// private void setPickerProperties() {
	// picker.setOnValueChangedListener(new OnValueChangeListener() {
	//
	// @Override
	// public void onValueChange(NumberPicker picker, int oldVal,
	// int newVal) {
	// System.out.println("value change");
	//
	// }
	// });
	//
	// picker.setOnClickListener(new OnClickListener() {
	//
	// @Override
	// public void onClick(View v) {
	// dor_TV.setText(temp[picker.getValue()]);
	// System.out.println("value click");
	//
	// }
	// });
	// }

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.dor_RL) {
			// dorMap = myApp.getDorMap();
			List<String> list = new ArrayList<String>(dorMap.values());
			for (int i = 0; i < list.size(); i++) {
				menu.add(0, v.getId(), i, list.get(i));
			}
		}
		// if (v.getId() == R.id.organization_RL) {
		// accountList = myApp.getAccountList();
		//
		// for (int i = 0; i < accountList.size(); i++) {
		// menu.add(1, v.getId(), i, accountList.get(i).getName());
		// }
		// }

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if (item.getGroupId() == 0) {
			selectedDOR = item.getOrder();
			dor_TV.setText(item.getTitle());
			// organization_TV.setText(item.getTitle());
		}
		// else if (item.getGroupId() == 1) {
		// internalConnect_TV.setText(item.getTitle());
		// }
		return super.onContextItemSelected(item);

	}

	public void onOpenContextMenu(View view) {
		openContextMenu(view);
		System.out.println(view.getId());
	}

	// public void onShowPicker(View view) {
	// pickerVisibility = !pickerVisibility;
	// if (pickerVisibility) {
	// picker.setVisibility(View.VISIBLE);
	// } else {
	// picker.setVisibility(View.GONE);
	// }
	// }

	public void onRightButton(View view) {
		progressDialog.show();

		/**
		 * ctcode - dor ,,,,oid - int conn ,,,,pcid - org
		 * **/

		
		if (previousIntent.hasExtra("is_edit_mode")
				&& previousIntent.getBooleanExtra("is_edit_mode", false)) {

		} else {
			try {

				String url = MyApp.URL + MyApp.CONTACTS_ADD;

				System.out.println("URL : " + url);

				JSONObject params = new JSONObject();

				params.put("ctCode",
						new ArrayList<String>(dorMap.keySet()).get(selectedDOR))
						.put("oId",
								new ArrayList<String>(userMap.keySet())
										.get(selectedInternalConnect))
						.put("pcId",
								accountList.get(selectedOrganisation)
										.getAccountId())

						.put("LastName",
								MyApp.encryptData(lastName_ET.getText()
										.toString()))
						.put("FirstName",
								MyApp.encryptData(firstName_ET.getText()
										.toString()))
						.put("Designation",
								MyApp.encryptData(designation_ET.getText()
										.toString()))
						.put("EMailAddress1",
								MyApp.encryptData(email_ET.getText().toString()))
						.put("Telephone1",
								MyApp.encryptData(officePhone_ET.getText()
										.toString()))
						.put("MobilePhone",
								MyApp.encryptData(mobile_ET.getText()
										.toString()));

				System.out.println("json" + params);

				params = MyApp.addParamToJson(params);

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
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isActivityVisible = true;
		LocalBroadcastManager.getInstance(this).registerReceiver(
				contactReceiver, new IntentFilter("contact_data"));

	}

	@Override
	protected void onPause() {
		isActivityVisible = false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				contactReceiver);
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
		case R.id.internal_RL:
			startActivityForResult(nextIntent, MyApp.SEARCH_USER);
			break;
		case R.id.organization_RL:
			startActivityForResult(nextIntent, MyApp.SEARCH_ACCOUNT);
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
			if (requestCode == MyApp.SEARCH_USER) {

				List<String> list = new ArrayList<String>(userMap.values());
				internalConnect_TV.setText(list.get(positionItem));

				selectedInternalConnect = positionItem;
			}
			if (requestCode == MyApp.SEARCH_ACCOUNT) {

				organization_TV
						.setText(accountList.get(positionItem).getName());
				selectedOrganisation = positionItem;
			}
		}

	}
}