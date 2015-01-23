package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
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
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;

public class AccountAddActivity extends CRMActivity {

	MyApp myApp;

	TextView clientName_TV, corridor_TV, corridor_TV2, headquarter_TV,
			headquarter_TV2, sub_TV, sub_TV2;
	EditText clientName_ET;

	RelativeLayout corridor_RL, headquarter_RL, sub_RL;

	Intent previousIntent;

	RequestQueue queue;

	Map<String, String> countryMap;
	
	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		if (previousIntent.hasExtra("position")) {
		}

		queue = Volley.newRequestQueue(this);
	}

	public void findThings() {
		super.findThings();

		clientName_TV = (TextView) findViewById(R.id.clientName_TV);
		corridor_TV = (TextView) findViewById(R.id.corridor_TV);
		corridor_TV2 = (TextView) findViewById(R.id.corridor_TV2);
		headquarter_TV = (TextView) findViewById(R.id.headquarter_TV);
		headquarter_TV2 = (TextView) findViewById(R.id.headquarter_TV2);
		sub_TV = (TextView) findViewById(R.id.sub_TV);
		sub_TV2 = (TextView) findViewById(R.id.sub_TV2);

		clientName_ET = (EditText) findViewById(R.id.clientName_ET);

		corridor_RL = (RelativeLayout) findViewById(R.id.corridor_RL);
		headquarter_RL = (RelativeLayout) findViewById(R.id.headquarter_RL);
		sub_RL = (RelativeLayout) findViewById(R.id.sub_RL);

	}

	private void setTypeface() {
		clientName_TV.setTypeface(myApp.getTypefaceRegularSans());
		corridor_TV.setTypeface(myApp.getTypefaceRegularSans());
		headquarter_TV.setTypeface(myApp.getTypefaceRegularSans());
		sub_TV.setTypeface(myApp.getTypefaceRegularSans());

		clientName_ET.setTypeface(myApp.getTypefaceRegularSans());

	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		if (previousIntent.hasExtra("position")) {
			Account tempAccount = myApp.getAccountList().get(
					previousIntent.getIntExtra("position", 0));

			clientName_ET.setText(tempAccount.getName());

			System.out.println(tempAccount.toString());
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_add);

		initThings();
		findThings();
		initView("Add Account", "Submit");

		// registerForContextMenu(corridor_RL);
		registerForContextMenu(headquarter_RL);
		registerForContextMenu(sub_RL);
		
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		if (v.getId() == R.id.headquarter_RL) {
			countryMap = myApp.getCountryMap();
			
			List<String> list = new ArrayList<String>(countryMap.values());
			for (int i=0;i<list.size();i++) {
				menu.add(0, v.getId(), i, list.get(i));
			}
			
//			for (String countryName : countryMap.values()) {
//				menu.add(0, v.getId(), 0, countryName);
//			}
			
//			menu.add(0, v.getId(), 0, "Africa");
//			menu.add(0, v.getId(), 0, "Argentina");
		} else if (v.getId() == R.id.sub_RL) {
			menu.add(1, v.getId(), 0, "CIM-CM-CP-Cosmetics-Toiletries");
			menu.add(1, v.getId(), 0, "CIM-CM-CP-Ele Goods-Household");
			menu.add(1, v.getId(), 0, "CIM-CM-CP-Furniture-furnishing");
			menu.add(1, v.getId(), 0, "CIM-CM-CP-Others");
			menu.add(1, v.getId(), 0, "CIM-CM-CP-Photographic Equipment");
			menu.add(1, v.getId(), 0, "CIM-CM-CP-Security Sys-Svcs");
			menu.add(1, v.getId(), 0, "CIM-CM-CP-Textiles-Clothing");
			menu.add(1, v.getId(), 0, "CIM-CM-CP-Toys-Games");
			menu.add(1, v.getId(), 0, "CIM-CM-FD-Agricltre-Fisheries");
			menu.add(1, v.getId(), 0, "CIM-CM-FD-Bevrgs/Drinks-Alghic");
			menu.add(1, v.getId(), 0, "CIM-CM-FD-Bvrg/Drinks-Non A/C");
			menu.add(1, v.getId(), 0, "CIM-CM-FD-Food Production");
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getGroupId() == 0) {
			headquarter_TV2.setText(item.getTitle());
			List<String> keys = new ArrayList<String>(countryMap.keySet());
			keys.get(item.getOrder());
			
//			System.out.println(countryMap.get(keys.get(item.getOrder())));
			
			Toast.makeText(AccountAddActivity.this, countryMap.get(keys.get(item.getOrder())), Toast.LENGTH_SHORT).show();
		} else if (item.getGroupId() == 1) {
			sub_TV2.setText(item.getTitle());
		}
		return super.onContextItemSelected(item);

	}

	public void onOpenContextMenu(View view) {
		openContextMenu(view);
	}

	public void onRightButton(View view) {
		try {

			String url = MyApp.URL + MyApp.ACCOUNTS_ADD;

			System.out.println("URL : " + url);

			System.out.println("Name  : " + MyApp.encryptData("Hello World"));

			JSONObject params = new JSONObject();

			params.put("cty", "11001")
					.put("leadpartner", "401a5d5f-9a8a-e411-96e8-5cf3fc3f502a")
					.put("lobusiness", "798330000")
					.put("Name",
							MyApp.encryptData(clientName_ET.getText()
									.toString())).put("sectors", "798330029")
					.put("slob", "798330031").put("accountCategoryOnes", "1");

			params = MyApp.addParamToJson(params);
			System.out.println("json" + params);

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
}
