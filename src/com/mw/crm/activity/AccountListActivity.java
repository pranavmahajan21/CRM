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
import com.mw.crm.adapter.AccountAdapter;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;

public class AccountListActivity extends CRMActivity {

	MyApp myApp;

	ListView accountLV;

	AccountAdapter adapter;

	List<Account> accountList;
	TextView errorTV;
	EditText searchAccount_ET;

	Intent nextIntent;

//	CreateDialog createDialog;
//	ProgressDialog progressDialog;

	RequestQueue queue;

	Gson gson;

	private void initThings() {

		myApp = (MyApp) getApplicationContext();
		accountList = myApp.getAccountList();

		gson = new Gson();

//		createDialog = new CreateDialog(this);
//		progressDialog = createDialog.createProgressDialog("Fetching Accounts",
//				"This may take some time", true, null);

		queue = Volley.newRequestQueue(this);

		// progressDialog.show();
		if (accountList != null && accountList.size() > 0) {
			adapter = new AccountAdapter(this, accountList);
		}

		nextIntent = new Intent(this, AccountAddActivity.class);

	}

	public void findThings() {
		super.findThings();
		accountLV = (ListView) findViewById(R.id.account_LV);
		errorTV = (TextView) findViewById(R.id.error_TV);
		searchAccount_ET= (EditText) findViewById(R.id.searchAccount_ET);
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		if (adapter != null) {
			accountLV.setAdapter(adapter);
		} else {
			// no tickets
		}
	}

	private void myOwnOnTextChangeListeners() {
		searchAccount_ET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// On text change call filter function of Adapter
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
		setContentView(R.layout.activity_account_display);

		initThings();
		findThings();
		initView("Accounts", "Add");

		myOwnOnTextChangeListeners();
//		progressDialog.show();
		// JSONObject jsonObject;
		

		accountLV.setOnItemClickListener(new OnItemClickListener() {

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
