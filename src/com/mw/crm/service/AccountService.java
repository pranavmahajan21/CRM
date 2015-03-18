package com.mw.crm.service;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mw.crm.activity.AccountAddActivity;
import com.mw.crm.activity.MenuActivity2;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Account;

public class AccountService extends IntentService {

	MyApp myApp;
	Gson gson = new Gson();

	List<Account> accountList = new ArrayList<Account>();

	public AccountService() {
		super("AccountService");
	}

	public AccountService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Toast.makeText(this, "AccountService", Toast.LENGTH_SHORT).show();
		myApp = (MyApp) getApplicationContext();

		try {

			String url = MyApp.URL + MyApp.ACCOUNTS_DATA;

			System.out.println("URL : " + url);

			JSONObject params = new JSONObject();
			params = MyApp.addParamToJson(params);

			System.out.println(params.toString());

			JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
					Method.POST, url, params,
					new Response.Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							System.out.println(">>>>Account Response => "
									+ response.toString());

							for (int i = 0; i < response.length(); i++) {
								try {
									accountList.add(getAccountObject(response
											.getJSONObject(i)));
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							myApp.setAccountList(accountList, true);
							onRequestComplete();
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println("ERROR  : " + error.getMessage());
							Toast.makeText(AccountService.this,
									"Error while fetching Accounts",
									Toast.LENGTH_LONG).show();
							error.printStackTrace();
							onRequestComplete();
						}
					});

			RetryPolicy policy = new DefaultRetryPolicy(30000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			jsonArrayRequest.setRetryPolicy(policy);
			Volley.newRequestQueue(this).add(jsonArrayRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void onRequestComplete() {
		if (AccountAddActivity.isActivityVisible) {
			Intent nextIntent = new Intent("account_update_receiver");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		} else if (MenuActivity2.isActivityVisible) {
			Intent nextIntent = new Intent("app_data");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private Account getAccountObject(JSONObject jsonObject) {
		try {
			System.out.println("*******         START      **********");
			Account account = new Account(MyApp.getPerfectString(jsonObject
					.getString("name")), MyApp.getPerfectString(jsonObject
					.getString("accountid")), MyApp.decryptData(jsonObject
					.getString("pcl_country1")), MyApp.decryptData(jsonObject
					.getString("pcl_lob")), MyApp.decryptData(jsonObject
					.getString("pcl_sublob")), MyApp.decryptData(jsonObject
					.getString("pcl_accountcategory1")),
					MyApp.decryptData(jsonObject.getString("pcl_sectorlist")),
					MyApp.decryptData(jsonObject.getString("pcl_leadpartner")),
					MyApp.decryptData(jsonObject
							.getString("pcl_relationshippartner1")),
					MyApp.decryptData(jsonObject
							.getString("pcl_relationshippartner2")),
					MyApp.decryptData(jsonObject
							.getString("pcl_relationshippartner3")),
					MyApp.decryptData(jsonObject
							.getString("pcl_businessdevelopmentmanager")));
			System.out.println("*******         END      **********");
			// System.out.println("#$#$" + account.getName());
			return account;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
}
