package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.mw.crm.adapter.SearchListAdapter;
import com.mw.crm.application.MyApp;
import com.mw.crm.extra.Constant;
import com.mw.crm.model.Account;
import com.mw.crm.model.Opportunity;

public class SearchActivity extends CRMActivity {

	MyApp myApp;
	Intent previouIntent, nextIntent;

	EditText search_ET;

	int requestCode = -1;

	Map<String, String> searchMap;

	List<Account> accountList;
	List<Opportunity> opportunityList;
	List<String> stringList;
	List<String> anotherStringList;

	SearchListAdapter adapterList;
	ListView search_LV;

	public void findThings() {
		search_ET = (EditText) findViewById(R.id.search_ET);
		search_LV = (ListView) findViewById(R.id.search_LV);
	}

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previouIntent = getIntent();
		requestCode = previouIntent.getIntExtra("request_code", 0);

		switch (requestCode) {
		case Constant.SEARCH_SECTOR:
			searchMap = myApp.getSectorMap();
			stringList = new ArrayList<String>(searchMap.values());
			anotherStringList = new ArrayList<String>(searchMap.values());
			break;
		case Constant.SEARCH_HQ_COUNTRY:
			searchMap = myApp.getCountryMap();
			stringList = new ArrayList<String>(searchMap.values());
			anotherStringList = new ArrayList<String>(searchMap.values());
			break;
		case Constant.SEARCH_SUB_LOB:
			searchMap = myApp.getSubLobMap();
			stringList = new ArrayList<String>(searchMap.values());
			anotherStringList = new ArrayList<String>(searchMap.values());
			break;
		case Constant.SEARCH_USER:
			searchMap = myApp.getUserMap();
			stringList = new ArrayList<String>(searchMap.values());
			anotherStringList = new ArrayList<String>(searchMap.values());
			break;
		case Constant.SEARCH_ACCOUNT:
			accountList = myApp.getAccountList();
			stringList = new ArrayList<String>();
			anotherStringList = new ArrayList<String>();
			for (int i = 0; i < accountList.size(); i++) {
				stringList.add(accountList.get(i).getName());
				anotherStringList.add(accountList.get(i).getName());
			}
			break;
		case Constant.SEARCH_SOLUTION:
			searchMap = myApp.getSolutionMap();
			stringList = new ArrayList<String>(searchMap.values());
			anotherStringList = new ArrayList<String>(searchMap.values());
			break;
		case Constant.SEARCH_PRODUCT:
			searchMap = myApp.getProductMap();
			stringList = new ArrayList<String>(searchMap.values());
			anotherStringList = new ArrayList<String>(searchMap.values());
			break;
		case Constant.SEARCH_PROFIT_CENTER:
			searchMap = myApp.getProfitCenterMap();
			stringList = new ArrayList<String>(searchMap.values());
			anotherStringList = new ArrayList<String>(searchMap.values());
			break;
		case Constant.SEARCH_COMPETITOR:
			searchMap = myApp.getCompetitorMap();
			stringList = new ArrayList<String>(searchMap.values());
			anotherStringList = new ArrayList<String>(searchMap.values());
			break;
		default:
			break;
		}// switch

		adapterList = new SearchListAdapter(this, stringList);
	}

	private void initView() {
		if (stringList != null) {
			search_LV.setAdapter(adapterList);
		}
	}

	private void myOwnOnTextChangeListeners() {
		search_ET.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence cs, int arg1, int arg2,
					int arg3) {
				// On text change call filter function of Adapter
				SearchActivity.this.adapterList.filter(cs.toString());
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_search);

		initThings();
		findThings();
		initView();

		myOwnOnTextChangeListeners();

		search_LV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int exactPosition = getExactPosition(((TextView) view
						.findViewById(R.id.item_TV)).getText().toString());
				System.out.println("exactPosition  " + exactPosition);

				Intent intent = new Intent();
				intent.putExtra("position_item", exactPosition);
				if (previouIntent.hasExtra("user_value")) {
					intent.putExtra("user_value",
							previouIntent.getIntExtra("user_value", 0));
				}
				if (previouIntent.hasExtra("solution_value")) {
					System.out.println("check1  " + previouIntent.getIntExtra("solution_value", 0));
					intent.putExtra("solution_value",
							previouIntent.getIntExtra("solution_value", 0));
				}
				if (previouIntent.hasExtra("profit_center_value")) {
					intent.putExtra("profit_center_value",
							previouIntent.getIntExtra("profit_center_value", 0));
				}
				if (previouIntent.hasExtra("product_value")) {
					intent.putExtra("product_value",
							previouIntent.getIntExtra("product_value", 0));
				}
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	// private int getExactPosition(String text) {
	// System.out.println(text + "      "+ stringList.size() );
	// for (int i = 0; i < stringList.size(); i++) {
	// if(stringList.get(i).equalsIgnoreCase(text)){
	// return i;
	// }
	// }
	// return 0;
	// }

	private int getExactPosition(String text) {
		System.out.println(text + "      " + anotherStringList.size());
		for (int i = 0; i < anotherStringList.size(); i++) {
			if (anotherStringList.get(i).equalsIgnoreCase(text)) {
				return i;
			}
		}
		return 0;
	}

}
