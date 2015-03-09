package com.mw.crm.activity;

import java.util.ArrayList;
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
import com.mw.crm.adapter.ContactAdapter;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;
import com.mw.crm.model.Contact;

public class ContactListActivity extends CRMActivity {

	MyApp myApp;

	ListView contactLV;

	ContactAdapter adapter;

	List<Contact> contactList;
	List<Contact> subContactList;
	TextView errorTV;

	Intent previousIntent, nextIntent;

	EditText search_ET;

	RequestQueue queue;

	private void initThings() {

		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();
		contactList = myApp.getContactList();

		queue = Volley.newRequestQueue(this);

		if (contactList != null && contactList.size() > 0) {
			if (previousIntent.hasExtra("is_my_contact")
					&& previousIntent.getBooleanExtra("is_my_contact", false)) {
				subContactList = new ArrayList<Contact>(contactList);
			} else if (previousIntent.hasExtra("account_id")) {
				subContactList = new ArrayList<Contact>();
				for (int i = 0; i < contactList.size(); i++) {
					System.out.println("#$#$  : "
							+ contactList.get(i).getOrganization());

					// TODO : try to remove the 2nd check from if(check1 &&
					// check2 && check3)
					if (contactList.get(i).getOrganization() != null
							&& contactList.get(i).getOrganization().length() > 0
							&& myApp.getStringIdFromStringJSON(
									contactList.get(i).getOrganization())
									.equals(previousIntent
											.getStringExtra("account_id"))) {
						subContactList.add(contactList.get(i));
					}
				}

			}
		}

		if (subContactList != null && subContactList.size() > 0) {
			adapter = new ContactAdapter(this, subContactList);
		}

	}

	public void findThings() {
		super.findThings();
		contactLV = (ListView) findViewById(R.id.contact_LV);
		errorTV = (TextView) findViewById(R.id.error_TV);

		search_ET = (EditText) findViewById(R.id.search_ET);
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		if (adapter != null) {
			contactLV.setAdapter(adapter);
		} else {
			errorTV.setVisibility(View.VISIBLE);
			errorTV.setTypeface(myApp.getTypefaceRegularSans());
		}
	}

	private void myOwnOnTextChangeListeners() {
		search_ET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				if (ContactListActivity.this.adapter == null) {
					return;
				}

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
		setContentView(R.layout.activity_contact_list);

		initThings();
		findThings();

		if (previousIntent.getBooleanExtra("is_my_contact", false)) {
			initView("Client Contacts", "Add");
		} else if (!(previousIntent.getBooleanExtra("is_my_contact", true))
				&& previousIntent.hasExtra("account_id")) {
			String accountID = previousIntent.getStringExtra("account_id");
			Account tempAccount = myApp.getAccountById(accountID);
			initView(tempAccount.getName() + "-Client Contacts", "Add");
		}

		myOwnOnTextChangeListeners();

		contactLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				nextIntent = new Intent(ContactListActivity.this,
						ContactDetailsActivity.class);
				nextIntent.putExtra("search_text", search_ET.getText()
						.toString());

				Contact tempContact = subContactList.get(position);
				search_ET.setText("");
				int index = myApp.getContactIndexFromContactId(tempContact
						.getContactId());

				nextIntent.putExtra("position", index);
				startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
			}

		});
	}

	public void onRightButton(View view) {
		nextIntent = new Intent(this, ContactAddActivity.class);
		nextIntent.putExtra("search_text", search_ET.getText().toString());
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	@Override
	public void onBack(View view) {
		search_ET.setText("");
		super.onBack(view);
	}

	@Override
	public void onHome(View view) {
		search_ET.setText("");
		super.onHome(view);
	}

	@Override
	public void onBackPressed() {
		search_ET.setText("");
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		search_ET.setText("");
		super.onPause();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data != null && data.hasExtra("refresh_list")
					&& data.getBooleanExtra("refresh_list", true)) {

				contactList = myApp.getContactList();

				adapter.swapData(contactList);
				adapter.notifyDataSetChanged();
			}
			if (data != null && data.hasExtra("search_text")) {
				search_ET.setText(data.getStringExtra("search_text"));
			}
		}
	}

}
