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
	List<Account> subAccountList;
	TextView errorTV;
	EditText searchAccount_ET;

	Intent nextIntent;

	RequestQueue queue;

	Gson gson;

	private void initThings() {

		myApp = (MyApp) getApplicationContext();
		accountList = myApp.getAccountList();

		gson = new Gson();

		queue = Volley.newRequestQueue(this);

		if (accountList != null && accountList.size() > 0) {
			// subAccountList = new ArrayList<Account>();
			// for (int i = 0; i < accountList.size(); i++) {
			// sub
			// }
			adapter = new AccountAdapter(this, accountList);
		}

		nextIntent = new Intent(this, AccountDetailsActivity.class);

	}

	public void findThings() {
		super.findThings();
		accountLV = (ListView) findViewById(R.id.account_LV);
		errorTV = (TextView) findViewById(R.id.error_TV);
		searchAccount_ET = (EditText) findViewById(R.id.searchAccount_ET);
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
		searchAccount_ET.addTextChangedListener(new TextWatcher() {

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

		// ViewTreeObserver viewTreeObserver =
		// rightButtonTV.getViewTreeObserver();
		// if (viewTreeObserver.isAlive()) {
		// viewTreeObserver
		// .addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		// @Override
		// public void onGlobalLayout() {
		// rightButtonTV.getViewTreeObserver()
		// .removeOnGlobalLayoutListener(this);
		// viewWidth = rightButtonTV.getWidth();
		// viewHeight = rightButtonTV.getHeight();
		// }
		// });
		// }
		// System.out.println("width  : " + viewWidth + "height  : " +
		// viewHeight);
		//
		// int width = rightButtonTV.getLayoutParams().width;
		// int height = rightButtonTV.getLayoutParams().height;
		// System.out.println("width  : " + width + "height  : " + height);

		myOwnOnTextChangeListeners();

		accountLV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Account tempAccount = accountList.get(position);
				searchAccount_ET.setText("");
				int index = myApp.getAccountIndexFromAccountId(tempAccount
						.getAccountId());

				nextIntent = new Intent(AccountListActivity.this,
						AccountDetailsActivity.class);
				nextIntent.putExtra("position", index);
				// nextIntent.putExtra("position", position);
				startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
			}

		});
	}

	public void onRightButton(View view) {
		nextIntent = new Intent(this, AccountAddActivity.class);
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	public void onContact(View view) {
		int position = accountLV.getPositionForView(view);
		System.out.println("position  :  " + position);
		nextIntent = new Intent(this, ContactListActivity.class);
		nextIntent.putExtra("is_my_contact", false);
		nextIntent.putExtra("account_id", accountList.get(position)
				.getAccountId());
		startActivity(nextIntent);
	}

	public void onOpportunity(View view) {
		int position = accountLV.getPositionForView(view);
		nextIntent = new Intent(this, OpportunityListActivity.class);
		nextIntent.putExtra("is_my_opportunity", false);
		nextIntent.putExtra("account_id", accountList.get(position)
				.getAccountId());
		startActivity(nextIntent);
	}

	public void onAppointment(View view) {
		int position = accountLV.getPositionForView(view);
		nextIntent = new Intent(this, AppointmentListActivity.class);
		nextIntent.putExtra("is_my_appointment", false);
		nextIntent.putExtra("account_id", accountList.get(position)
				.getAccountId());
		startActivity(nextIntent);
	}

	@Override
	public void onBack(View view) {
		searchAccount_ET.setText("");
		super.onBack(view);
	}

	@Override
	public void onHome(View view) {
		searchAccount_ET.setText("");
		super.onHome(view);
	}

	@Override
	public void onBackPressed() {
		searchAccount_ET.setText("");
		super.onBackPressed();
	}

	@Override
	protected void onPause() {
		searchAccount_ET.setText("");
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
		}
	}
}
