package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
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
import android.widget.Toast;

import com.example.crm.activity.R;
import com.mw.crm.adapter.SearchListAdapter;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;
import com.mw.crm.model.Opportunity;

public class SearchActivity extends Activity {

	MyApp myApp;
	Intent previouIntent, nextIntent;

	EditText search_ET;

	int requestCode = -1;

	Map<String, String> searchMap;

	List<Account> accountList;
	List<Opportunity> opportunityList;
	List<String> stringList;

	SearchListAdapter adapterList;
	ListView search_LV;

	private void findThings() {
		search_ET = (EditText) findViewById(R.id.search_ET);
		search_LV = (ListView) findViewById(R.id.search_LV);
	}

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previouIntent = getIntent();
		requestCode = previouIntent.getIntExtra("request_code", 0);

		switch (requestCode) {
		case MyApp.SEARCH_SECTOR:
			searchMap = myApp.getSectorMap();
			stringList = new ArrayList<String>(searchMap.values());
			break;
		case MyApp.SEARCH_HQ_COUNTRY:
			searchMap = myApp.getCountryMap();
			stringList = new ArrayList<String>(searchMap.values());
			break;
		case MyApp.SEARCH_SUB_LOB:
			searchMap = myApp.getSubLobMap();
			stringList = new ArrayList<String>(searchMap.values());
			break;
		case MyApp.SEARCH_USER:
			searchMap = myApp.getUserMap();
			stringList = new ArrayList<String>(searchMap.values());
			break;
		case MyApp.SEARCH_ACCOUNT:
			accountList = myApp.getAccountList();
			Toast.makeText(this, accountList.size() + "", Toast.LENGTH_SHORT)
					.show();
			stringList = new ArrayList<String>();
			for (int i = 0; i < accountList.size(); i++) {
				stringList.add(accountList.get(i).getName());
			}
			break;
		case MyApp.SEARCH_OPPORTUNITY:
			List<Opportunity> opportunityList = myApp.getOpportunityList();
			stringList = new ArrayList<String>();
			for (int i = 0; i < opportunityList.size(); i++) {
				try {
					stringList.add(new JSONObject(opportunityList.get(i)
							.getCustomerId()).getString("Name"));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
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
				Toast.makeText(SearchActivity.this, "" + position, Toast.LENGTH_SHORT).show();
				
				System.out.println(((TextView)view.findViewById(R.id.item_TV)).getText().toString());
				
				Intent intent = new Intent();
				intent.putExtra("position_item", position);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

	public void onBack(View view) {
		finish();
	}
}
