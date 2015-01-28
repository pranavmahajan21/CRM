package com.mw.crm.activity;

import java.util.List;

import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
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
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;
import com.mw.crm.model.Contact;
import com.mw.crm.model.InternalConnect;

public class ContactAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	String[] temp = new String[] { "Just Know", "High", "Medium", "Low",
			"Comfortable" };

	MyApp myApp;

	TextView firstNameLabel_TV, lastNameLabel_TV, dorLabel_TV,
			internalConnectLabel_TV, organizationLabel_TV, designationLabel_TV,
			emailLabel_TV, officePhoneLabel_TV, mobileLabel_TV;

	TextView dor_TV, internalConnect_TV, organization_TV;
	EditText firstName_ET, lastName_ET, designation_ET, email_ET,
			officePhone_ET, mobile_ET;
	RelativeLayout organizationRL, internalRL;

	boolean pickerVisibility = false;
	NumberPicker picker;

	Intent previousIntent;

	int selectedInternalConnect = -1;
	List<InternalConnect> internalConnectList;
	List<Account> accountList;

	Gson gson;
	RequestQueue queue;

	private BroadcastReceiver contactReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			registerForContextMenu(internalRL);
			registerForContextMenu(organizationRL);
		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		gson = new Gson();
		queue = Volley.newRequestQueue(this);
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

		internalRL = (RelativeLayout) findViewById(R.id.internal_RL);
		organizationRL = (RelativeLayout) findViewById(R.id.organization_RL);

		picker = (NumberPicker) findViewById(R.id.picker);

		picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
		picker.setMinValue(0);
		picker.setMaxValue(4);
		picker.setDisplayedValues(temp);

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
			getRightButtonTV().setVisibility(View.GONE);

			Contact tempContact = myApp.getContactList().get(
					previousIntent.getIntExtra("position", 0));

			System.out.println(tempContact.toString());

			firstName_ET.setText(tempContact.getLastName());
			lastName_ET.setText(tempContact.getLastName());
		} else {
			registerForContextMenu(internalRL);
			registerForContextMenu(organizationRL);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_add);

		initThings();
		findThings();
		initView("Add Contact", "Submit");

		setPickerProperties();

	}

	private void setPickerProperties() {
		picker.setOnValueChangedListener(new OnValueChangeListener() {

			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				System.out.println("value change");

				// TODO Auto-generated method stub
			}
		});

		picker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dor_TV.setText(temp[picker.getValue()]);
				System.out.println("value click");

			}
		});
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.organization_RL) {
			accountList = myApp.getAccountList();

			for (int i = 0; i < accountList.size(); i++) {
				menu.add(1, v.getId(), i, accountList.get(i).getName());
			}
			// menu.add(0, v.getId(), 0,
			// "South Asia Clean Energy Fund Partners, L.p");
			// menu.add(0, v.getId(), 0, "(n)Code Solutions");
			// menu.add(0, v.getId(), 0, "1 MG");
			// menu.add(0, v.getId(), 0, "1 MG Road");
			// menu.add(0, v.getId(), 0,
			// "10C India Internet India Private Limited");
			// menu.add(0, v.getId(), 0, "10C India Internet Pve. Ltd.");
			// menu.add(0, v.getId(), 0, "11210");
			// menu.add(0, v.getId(), 0,
			// "120 Media Collective Private Limited");
			// menu.add(0, v.getId(), 0,
			// "1FB Support Services Private Limited");
			// menu.add(0, v.getId(), 0, "2 Degrees");
			// menu.add(0, v.getId(), 0, "20 Cube Group");
		} else if (v.getId() == R.id.internal_RL) {
			/** For Internal Connect **/
			internalConnectList = myApp.getInternalConnectList();

			for (int i = 0; i < internalConnectList.size(); i++) {
				menu.add(1, v.getId(), i, internalConnectList.get(i)
						.getLastName());
			}

		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if (item.getGroupId() == 0) {
			organization_TV.setText(item.getTitle());
		} else if (item.getGroupId() == 1) {
			internalConnect_TV.setText(item.getTitle());
			selectedInternalConnect = item.getOrder();
		}
		return super.onContextItemSelected(item);

	}

	public void onOpenContextMenu(View view) {
		openContextMenu(view);
		System.out.println(view.getId());
	}

	public void onShowPicker(View view) {
		pickerVisibility = !pickerVisibility;
		if (pickerVisibility) {
			picker.setVisibility(View.VISIBLE);
		} else {
			picker.setVisibility(View.GONE);
		}
	}

	public void onRightButton(View view) {
		try {

			String url = MyApp.URL + MyApp.CONTACTS_ADD;

			System.out.println("URL : " + url);

			System.out.println("LastName  : "
					+ MyApp.encryptData(lastName_ET.getText().toString())
					+ "\nFirstName  : "
					+ MyApp.encryptData(firstName_ET.getText().toString())
					+ "\nDesignation  : " + MyApp.encryptData("Chairman")
					+ "\nEMailAddress1  : "
					+ MyApp.encryptData("sharath@motifworks.com")
					+ "\nTelephone1  : " + MyApp.encryptData("01127675465")
					+ "\nMobilePhone  : " + MyApp.encryptData("09538656366"));

			JSONObject params = new JSONObject();
			// .put("username", MyApp.encryptData("in-fmcrmad1"))
			// .put("password", MyApp.encryptData("Password01"))
			params
			// .put("oId",
			// internalConnectList.get(selectedInternalConnect)
			// .getSystemUserId())
			.put("oId", "7f08bfb3-858a-e411-96e8-5cf3fc3f502a")
					.put("pcId", "8ca70259-8095-e411-96e8-5cf3fc3f502a")
					.put("ctCode", "1")
					.put("LastName",
							MyApp.encryptData(lastName_ET.getText().toString()))
					.put("FirstName",
							MyApp.encryptData(firstName_ET.getText().toString()))
					.put("Designation", "Chairman").put("EMailAddress1", "")
					.put("Telephone1", "").put("MobilePhone", "");

			System.out.println("json" + params);

			params = MyApp.addParamToJson(params);

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
							System.out.println("ERROR  : " + error.getMessage());
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

}