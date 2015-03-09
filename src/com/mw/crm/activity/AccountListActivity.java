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
import com.google.gson.Gson;
import com.mw.crm.adapter.AccountAdapter;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;

public class AccountListActivity extends CRMActivity {

	MyApp myApp;

	ListView accountLV;

	AccountAdapter adapter;

	List<Account> accountList;
	List<Account> subAccountList;
	TextView errorTV;
	EditText search_ET;

	Intent previousIntent, nextIntent;

	RequestQueue queue;

	Gson gson;

	private void initThings() {

		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();
		accountList = myApp.getAccountList();

		gson = new Gson();

		queue = Volley.newRequestQueue(this);

		if (accountList != null && accountList.size() > 0) {
			if (previousIntent.hasExtra("is_my_account")
					&& previousIntent.getBooleanExtra("is_my_account", false)) {
				subAccountList = new ArrayList<Account>(accountList);
			}
		}

		if (subAccountList != null && subAccountList.size() > 0) {
			adapter = new AccountAdapter(this, subAccountList);
		}

		nextIntent = new Intent(this, AccountDetailsActivity.class);

	}

	public void findThings() {
		super.findThings();
		accountLV = (ListView) findViewById(R.id.account_LV);
		errorTV = (TextView) findViewById(R.id.error_TV);
		search_ET = (EditText) findViewById(R.id.search_ET);
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		if (adapter != null) {
			accountLV.setAdapter(adapter);
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
				if (AccountListActivity.this.adapter == null) {
					/**
					 * This check is required in case when List<E> is null or
					 * size of List<E> is 0. In that case adapter will be
					 * uninitialized i.e. null.
					 **/
					return;
				}

				/** On text change call filter function of Adapter **/
				AccountListActivity.this.adapter.filter(cs.toString());
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
		setContentView(R.layout.activity_account_list);

		initThings();
		findThings();
		initView("Client History", "Add");

		myOwnOnTextChangeListeners();

		accountLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				nextIntent = new Intent(AccountListActivity.this,
						AccountDetailsActivity.class);
				nextIntent.putExtra("search_text", search_ET.getText()
						.toString());

				Account tempAccount = subAccountList.get(position);
				search_ET.setText("");
				int index = myApp.getAccountIndexFromAccountId(tempAccount
						.getAccountId());

				nextIntent.putExtra("position", index);
				startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
			}

		});
	}

	public void onRightButton(View view) {
		nextIntent = new Intent(this, AccountAddActivity.class);
		nextIntent.putExtra("search_text", search_ET.getText().toString());
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	public void onContact(View view) {
		int position = accountLV.getPositionForView(view);
		System.out.println("position  :  " + position);
		nextIntent = new Intent(this, ContactListActivity.class);
		nextIntent.putExtra("search_text", search_ET.getText().toString());
		nextIntent.putExtra("is_my_contact", false);
		nextIntent.putExtra("account_id", accountList.get(position)
				.getAccountId());
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
		// startActivity(nextIntent);
	}

	public void onOpportunity(View view) {
		int position = accountLV.getPositionForView(view);
		nextIntent = new Intent(this, OpportunityListActivity.class);
		nextIntent.putExtra("search_text", search_ET.getText().toString());
		nextIntent.putExtra("is_my_opportunity", false);
		nextIntent.putExtra("account_id", accountList.get(position)
				.getAccountId());
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	public void onAppointment(View view) {
		int position = accountLV.getPositionForView(view);
		nextIntent = new Intent(this, AppointmentListActivity.class);
		nextIntent.putExtra("search_text", search_ET.getText().toString());
		nextIntent.putExtra("is_my_appointment", false);
		nextIntent.putExtra("account_id", accountList.get(position)
				.getAccountId());
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	@Override
	public void onBack(View view) {
		search_ET.setText("");
		super.onBack(view);
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
				/**
				 * Step1 is required because accountList still holds reference
				 * to the old list. So updating the reference of the list to
				 * just the adapter is not sufficient. The list reference in the
				 * adapter & list reference in AccountListActivity should point
				 * to the same object, otherwise we will see discrepancies on
				 * selecting an item from list.
				 **/
				/** Step1 **/
				accountList = myApp.getAccountList();
				/** Step2 **/
				adapter.swapData(accountList);
				adapter.notifyDataSetChanged();

			}
			if (data != null && data.hasExtra("search_text")) {
				search_ET.setText(data.getStringExtra("search_text"));
			}
		}
	}
}
