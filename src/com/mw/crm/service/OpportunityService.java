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
import com.mw.crm.activity.MenuActivity;
import com.mw.crm.activity.OpportunityAddActivity;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Opportunity;

public class OpportunityService extends IntentService {

	MyApp myApp;
	Gson gson = new Gson();

	List<Opportunity> opportunityList = new ArrayList<Opportunity>();

	public OpportunityService() {
		super("OpportunityService");
	}

	public OpportunityService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
//		Toast.makeText(this, "OpportunityService", Toast.LENGTH_SHORT).show();
		myApp = (MyApp) getApplicationContext();

		try {

			String url = MyApp.URL + MyApp.OPPORTUNITY_DATA;

			System.out.println("URL : " + url);

			JSONObject params = new JSONObject();
			params = MyApp.addParamToJson(params);

			JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
					Method.POST, url, params,
					new Response.Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							System.out.println("Oppo response"
									+ response.toString());

							for (int i = 0; i < response.length(); i++) {
								try {
									opportunityList
											.add(getOpportunityObject(response
													.getJSONObject(i)));
								} catch (JSONException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								// System.out.println("<><>"
								// + accountList.get(i).getName());
							}
							myApp.setOpportunityList(opportunityList);
							onRequestComplete();
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println("ERROR  : " + error.getMessage());
							Toast.makeText(OpportunityService.this,
									"Error while fetching oppotunitites",
									Toast.LENGTH_SHORT).show();
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
//		if (ContactAddActivity.isActivityVisible) {
//			Intent nextIntent = new Intent("internal_connect_data");
//			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
//		} else 
			if (OpportunityAddActivity.isActivityVisible) {
			Intent nextIntent = new Intent("opportunity_update_receiver");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		} else if (MenuActivity.isActivityVisible) {
			Intent nextIntent = new Intent("app_data");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
//		if (ContactAddActivity.isActivityVisible) {
//			Intent nextIntent = new Intent("internal_connect_data");
//			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
//		} else if (AppointmentAddActivity.isActivityVisible) {
//			Intent nextIntent = new Intent("owner_data");
//			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
//		} else if (MenuActivity.isActivityVisible) {
//			Intent nextIntent = new Intent("app_data");
//			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
//		}
	}

	private Opportunity getOpportunityObject(JSONObject jsonObject) {
		try {
			Opportunity opportunity = new Opportunity(
					MyApp.decryptData(jsonObject.getString("ownerid")),
					MyApp.decryptData(jsonObject
							.getString("transactioncurrencyid")),
					MyApp.decryptData(jsonObject.getString("totalamount")),
					MyApp.decryptData(jsonObject.getString("customerid")),
					MyApp.getPerfectString(jsonObject.getString("name")),
					MyApp.decryptData(jsonObject.getString("pcl_kpmgstatus")),
					MyApp.getPerfectString(jsonObject.getString("opportunityid")),
					MyApp.decryptData(jsonObject.getString("opportunityratingcode")),
					MyApp.decryptData(jsonObject.getString("salesstagecode")));
			return opportunity;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
}
