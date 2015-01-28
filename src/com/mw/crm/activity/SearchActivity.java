package com.mw.crm.activity;

import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.crm.activity.R;
import com.mw.crm.adapter.SearchAdapter;
import com.mw.crm.extra.MyApp;

public class SearchActivity extends Activity {

	MyApp myApp;
	Intent previouIntent, nextIntent;

	int requestCode = -1;

	Map<String, String> searchMap;

	SearchAdapter adapter;
	ListView search_LV;

	private void findThings() {
		search_LV = (ListView) findViewById(R.id.search_LV);
	}

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previouIntent = getIntent();
		requestCode = previouIntent.getIntExtra("request_code", 0);

		switch (requestCode) {
		case MyApp.SEARCH_SECTOR:
			searchMap = myApp.getSectorMap();
			break;
		case MyApp.SEARCH_HQ_COUNTRY:
			searchMap = myApp.getCountryMap();
			break;
		case MyApp.SEARCH_SUB_LOB:
			searchMap = myApp.getSubLobMap();
			break;

		default:
			break;
		}

		if (searchMap != null) {
			adapter = new SearchAdapter(this, searchMap);
		}
	}

	private void initView() {
		if (adapter != null) {
			search_LV.setAdapter(adapter);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		setContentView(R.layout.activity_search);

		initThings();
		findThings();
		initView();

		search_LV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.putExtra("position_item", position);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

}
