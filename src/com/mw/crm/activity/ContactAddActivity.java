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
import com.mw.crm.model.Contact;
import com.mw.crm.model.InternalConnect;
import com.mw.crm.service.ContactService;

public class ContactAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	TextView firstNameLabel_TV, lastNameLabel_TV, dorLabel_TV,
			internalConnectLabel_TV, organizationLabel_TV, designationLabel_TV,
			emailLabel_TV, officePhoneLabel_TV, mobileLabel_TV;

	TextView dor_TV, internalConnect_TV, organization_TV;
	EditText firstName_ET, lastName_ET, designation_ET, email_ET,
			officePhone_ET, mobile_ET;
	RelativeLayout dor_RL, organization_RL, internal_RL;

	Contact tempContact;

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
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	private BroadcastReceiver contactUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			progressDialog.dismiss();

			if (!(previousIntent.hasExtra("is_edit_mode") && previousIntent
					.getBooleanExtra("is_edit_mode", false))) {
				Toast.makeText(ContactAddActivity.this,
						"Contact updated successfully.", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(ContactAddActivity.this,
						"Contact created successfully.", Toast.LENGTH_SHORT)
						.show();
			}

			Contact aa = new Contact(firstName_ET.getText().toString(),
					lastName_ET.getText().toString(), email_ET.getText()
							.toString(), designation_ET.getText().toString(),
					mobile_ET.getText().toString(), officePhone_ET.getText()
							.toString(), internalConnect_TV.getText()
							.toString(), organization_TV.getText().toString(),
					dor_TV.getText().toString(), null);

			nextIntent = new Intent(ContactAddActivity.this,
					ContactDetailsActivity.class);
			nextIntent
					.putExtra("contact_dummy", gson.toJson(aa, Contact.class));
			if (!(previousIntent.hasExtra("is_edit_mode") && previousIntent
					.getBooleanExtra("is_edit_mode", false))) {
				nextIntent.putExtra("contact_created", true);
			}
			startActivityForResult(nextIntent, MyApp.DETAILS_CONTACT);
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

	}

	private void setTypeface() {
		firstNameLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		lastNameLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		dorLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		internalConnectLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		organizationLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		designationLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		emailLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		officePhoneLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		mobileLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		dor_TV.setTypeface(myApp.getTypefaceRegularSans());
		internalConnect_TV.setTypeface(myApp.getTypefaceRegularSans());
		organization_TV.setTypeface(myApp.getTypefaceRegularSans());

		firstName_ET.setTypeface(myApp.getTypefaceRegularSans());
		lastName_ET.setTypeface(myApp.getTypefaceRegularSans());
		designation_ET.setTypeface(myApp.getTypefaceRegularSans());
		email_ET.setTypeface(myApp.getTypefaceRegularSans());
		officePhone_ET.setTypeface(myApp.getTypefaceRegularSans());
		mobile_ET.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		if (previousIntent.hasExtra("position")) {

			tempContact = myApp.getContactList().get(
					previousIntent.getIntExtra("position", 0));
			// Toast.makeText(this, "asd  :  " + tempContact.getContactId(),
			// Toast.LENGTH_SHORT).show();

			firstName_ET.setText(tempContact.getFirstName());
			lastName_ET.setText(tempContact.getLastName());

			Integer temp = myApp.getIntValueFromStringJSON(tempContact
					.getDegreeOfRelation());
			if (temp != null) {
				dor_TV.setText(myApp.getDorMap().get(
						Integer.toString(temp.intValue())));
				selectedDOR = myApp.getIndexFromKeyDORMap(Integer.toString(temp
						.intValue()));
			}

			internalConnect_TV.setText(myApp
					.getStringNameFromStringJSON(tempContact
							.getInternalConnect()));
			selectedInternalConnect = myApp
					.getIndexFromKeyUserMap(myApp
							.getStringIdFromStringJSON(tempContact
									.getInternalConnect()));

			organization_TV
					.setText(myApp.getStringNameFromStringJSON(tempContact
							.getOrganization()));
			selectedOrganisation = myApp.getAccountIndexFromAccountId(myApp
					.getStringIdFromStringJSON(tempContact.getOrganization()));

			designation_ET.setText(tempContact.getDesignation());
			email_ET.setText(tempContact.getEmail());
			officePhone_ET.setText(tempContact.getTelephone());
			mobile_ET.setText(tempContact.getMobilePhone());

			System.out.println("selectedDOR  : " + selectedDOR
					+ "\nselectedInternalConnect  : " + selectedInternalConnect
					+ "\nselectedOrganisation  : " + selectedOrganisation);
		}

	}

	@SuppressLint("ClickableViewAccessibility")
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

	private void staticNonsense() {
		firstName_ET.setText("AB");
		lastName_ET.setText("Devilliers");
		designation_ET.setText("pool");
		email_ET.setText("hullo@hullo.com");
		officePhone_ET.setText("123456");
		mobile_ET.setText("123123");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_add);

		initThings();
		findThings();
//		initView("Add Contact", "Save");
		
		if ((previousIntent.hasExtra("is_edit_mode") && previousIntent
				.getBooleanExtra("is_edit_mode", false))) {
			initView("Modify Contact", "Save");
		} else {
			initView("Add Contact", "Save");
		}

		hideKeyboardFunctionality();

		registerForContextMenu(dor_RL);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.dor_RL) {
			List<String> list = new ArrayList<String>(dorMap.values());
			for (int i = 0; i < list.size(); i++) {
				menu.add(0, v.getId(), i, list.get(i));
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if (item.getGroupId() == 0) {
			selectedDOR = item.getOrder();
			dor_TV.setText(item.getTitle());
		}
		return super.onContextItemSelected(item);

	}

	/** Maybe we can put this method in CRMActivity **/
	public void onOpenContextMenu(View view) {
		hideKeyboard(this.getCurrentFocus());
		openContextMenu(view);
	}

	private boolean validate() {
		boolean notErrorCase = true;
		// if (firstName_ET.getText().toString().trim().length() < 1) {
		// alertDialogBuilder = createDialog.createAlertDialog(null,
		// "Please enter some First Name.", false);
		// notErrorCase = false;
		// } else
		if (lastName_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter some Last Name.", false);
			notErrorCase = false;
		} else if (selectedDOR < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a DOR.", false);
			notErrorCase = false;
		} else if (selectedInternalConnect < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a Internal Connect.", false);
			notErrorCase = false;
		} else if (selectedOrganisation < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select an organization.", false);
			notErrorCase = false;
		}
		// else if (designation_ET.getText().toString().trim().length() < 1) {
		// alertDialogBuilder = createDialog.createAlertDialog(null,
		// "Please enter the designation.", false);
		// notErrorCase = false;
		// } else if (email_ET.getText().toString().trim().length() < 1) {
		// alertDialogBuilder = createDialog.createAlertDialog(null,
		// "Please enter the email.", false);
		// notErrorCase = false;
		// } else if (officePhone_ET.getText().toString().trim().length() < 1) {
		// alertDialogBuilder = createDialog.createAlertDialog(null,
		// "Please enter Office Phone.", false);
		// notErrorCase = false;
		// } else if (lastName_ET.getText().toString().trim().length() < 1) {
		// alertDialogBuilder = createDialog.createAlertDialog(null,
		// "Please enter Mobile.", false);
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
		if (!validate()) {
			return;
		}
		/**
		 * ctcode - dor ,,,,oid - int conn ,,,,pcid - org
		 * **/
		JSONObject params = new JSONObject();

		try {
			params.put("ctCode",
					new ArrayList<String>(dorMap.keySet()).get(selectedDOR))
					.put("oId",
							new ArrayList<String>(userMap.keySet())
									.get(selectedInternalConnect))
					.put("pcId",
							accountList.get(selectedOrganisation)
									.getAccountId())
					.put("LastName",
							MyApp.encryptData(lastName_ET.getText().toString()));

			if (firstName_ET.getText().toString().trim().length() > 1) {
				params.put("FirstName",
						MyApp.encryptData(firstName_ET.getText().toString()));
			}
			if (designation_ET.getText().toString().trim().length() > 1) {
				params.put("Designation",
						MyApp.encryptData(designation_ET.getText().toString()));
			}
			if (officePhone_ET.getText().toString().trim().length() > 1) {
				params.put("Telephone1",
						MyApp.encryptData(officePhone_ET.getText().toString()));
			}
			if (mobile_ET.getText().toString().trim().length() > 1) {
				params.put("MobilePhone",
						MyApp.encryptData(mobile_ET.getText().toString()));
			}
			if (email_ET.getText().toString().trim().length() > 1) {
				if (previousIntent.hasExtra("is_edit_mode")
						&& previousIntent
								.getBooleanExtra("is_edit_mode", false)) {
					params.put("EmailAddress1",
							MyApp.encryptData(email_ET.getText().toString()));
				} else {
					params.put("EMailAddress1",
							MyApp.encryptData(email_ET.getText().toString()));
				}
			}
			if (previousIntent.hasExtra("is_edit_mode")
					&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
				params.put("conid", tempContact.getContactId());
			}
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		String url;
		if (previousIntent.hasExtra("is_edit_mode")
				&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
			/** Update Mode **/
			url = MyApp.URL + MyApp.CONTACTS_UPDATE;
		} else {
			url = MyApp.URL + MyApp.CONTACTS_ADD;
		}
		System.out.println("json" + params);

		params = MyApp.addParamToJson(params);
		progressDialog.show();
			try {

				System.out.println("URL : " + url);
				System.out.println("params : " + params);

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
				e.printStackTrace();
				progressDialog.hide();
			}
	}

	private void onPositiveResponse() {
		progressDialog.dismiss();

		progressDialog = createDialog.createProgressDialog("Updating Contacts",
				"This may take some time", true, null);
		progressDialog.show();

		Intent serviceIntent = new Intent(this, ContactService.class);
		startService(serviceIntent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		isActivityVisible = true;
		LocalBroadcastManager.getInstance(this).registerReceiver(
				contactUpdateReceiver,
				new IntentFilter("contact_update_receiver"));

	}

	@Override
	protected void onPause() {
		isActivityVisible = false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				contactUpdateReceiver);
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
			int positionItem = 0;
			if (data != null) {
				positionItem = data.getIntExtra("position_item", 0);
			}
			if (requestCode == MyApp.SEARCH_USER) {
				List<String> list = new ArrayList<String>(userMap.values());
				internalConnect_TV.setText(list.get(positionItem));

				selectedInternalConnect = positionItem;

				System.out.println(new ArrayList<String>(userMap.keySet())
						.get(selectedInternalConnect));

				System.out.println(userMap.get(new ArrayList<String>(userMap
						.keySet()).get(selectedInternalConnect)));
			}
			if (requestCode == MyApp.SEARCH_ACCOUNT) {
				organization_TV
						.setText(accountList.get(positionItem).getName());
				selectedOrganisation = positionItem;
			}
			if (requestCode == MyApp.DETAILS_CONTACT) {
				Intent intent = new Intent();
				intent.putExtra("refresh_list", true);
				setResult(RESULT_OK, intent);
				finish();
			}
		}
	}

}