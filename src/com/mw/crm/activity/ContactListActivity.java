package com.mw.crm.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.adapter.ContactAdapter;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Contact;

public class ContactListActivity extends CRMActivity {

	MyApp myApp;

	ListView contactLV;

	ContactAdapter adapter;

	List<Contact> contactList;
	TextView errorTV;

	Intent nextIntent;

	EditText searchContact_ET;
//	CreateDialog createDialog;
//	ProgressDialog progressDialog;

	RequestQueue queue;

	Gson gson;

	private void initThings() {

		myApp = (MyApp) getApplicationContext();
		contactList = myApp.getContactList();

		gson = new Gson();

//		createDialog = new CreateDialog(this);
//		progressDialog = createDialog.createProgressDialog("Fetching Contacts",
//				"This may take some time", true, null);

		queue = Volley.newRequestQueue(this);

		if (contactList != null && contactList.size() > 0) {
			adapter = new ContactAdapter(this, contactList);
		}

		nextIntent = new Intent(this, ContactAddActivity.class);
	}

	public void findThings() {
		super.findThings();
		contactLV = (ListView) findViewById(R.id.contact_LV);
		errorTV = (TextView) findViewById(R.id.error_TV);
		
		searchContact_ET = (EditText) findViewById(R.id.searchContact_ET);
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);

		if (adapter != null) {
			contactLV.setAdapter(adapter);
		} else {
			// no tickets
		}
	}

	private void myOwnOnTextChangeListeners() {
		searchContact_ET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// On text change call filter function of Adapter
				ContactListActivity.this.adapter.filter(cs.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void afterTextChanged(Editable arg0) {
			}
		});
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_display);

		initThings();
		findThings();
		initView("Contacts", "Add");

		myOwnOnTextChangeListeners();
//		progressDialog.show();
		// JSONObject jsonObject;
		

		contactLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				nextIntent.putExtra("position", position);
				startActivity(nextIntent);
			}

		});
	}

	public void onRightButton(View view) {
		startActivity(nextIntent);
	}

	
}
