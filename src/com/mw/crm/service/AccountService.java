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
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mw.crm.activity.AppointmentAddActivity;
import com.mw.crm.activity.ContactAddActivity;
import com.mw.crm.activity.MenuActivity;
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
		Toast.makeText(this, "AccountService", Toast.LENGTH_SHORT).show();
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
							// TODO
							// Advanced GSON conversion required
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
							myApp.setAccountList(accountList);
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println("ERROR  : " + error.getMessage());
							Toast.makeText(AccountService.this, "Error while fetching Account", Toast.LENGTH_SHORT).show();
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
			Volley.newRequestQueue(this).add(jsonArrayRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (ContactAddActivity.isActivityVisible) {
			Intent nextIntent = new Intent("contact_data");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		} else if (AppointmentAddActivity.isActivityVisible) {
			Intent nextIntent = new Intent("owner_data");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		}else if (MenuActivity.isActivityVisible) {
			Intent nextIntent = new Intent("app_data");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		}
	}
	
	private Account getAccountObject(JSONObject jsonObject) {
		try {
			
			Account account = new Account(MyApp.decryptData(jsonObject
					.getString("name")), MyApp.decryptData(jsonObject
					.getString("accountid")), MyApp.decryptData(jsonObject
							.getString("pcl_country1")), null, MyApp.decryptData(jsonObject
					.getString("pcl_sublob")));
			
//			MyApp.decryptData(jsonObject
//					.getString("pcl_country1")), MyApp.decryptData(jsonObject
//					.getString("pcl_corridor"))
					
			System.out.println("#$#$" + account.getName());
			return account;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
}
