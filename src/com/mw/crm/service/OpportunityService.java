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
import com.mw.crm.activity.MenuActivity2;
import com.mw.crm.activity.OpportunityAddActivity;
import com.mw.crm.activity.OpportunityListActivity;
import com.mw.crm.application.MyApp;
import com.mw.crm.extra.Constant;
import com.mw.crm.extra.DateFormatter;
import com.mw.crm.model.Opportunity;
import com.mw.crm.model.Solution;

public class OpportunityService extends IntentService {

	MyApp myApp;
	Gson gson = new Gson();
	DateFormatter dateFormatter = new DateFormatter();
	
	List<Opportunity> opportunityList = new ArrayList<Opportunity>();

	public OpportunityService() {
		super("OpportunityService");
	}

	public OpportunityService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Toast.makeText(this, "OpportunityService",
		// Toast.LENGTH_SHORT).show();
		myApp = (MyApp) getApplicationContext();

		try {

			String url = Constant.URL + Constant.OPPORTUNITY_DATA;

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
							myApp.setOpportunityList(opportunityList, true);
							Toast.makeText(OpportunityService.this, "Done",
									Toast.LENGTH_SHORT).show();
							onRequestComplete();
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println("ERROR  : " + error.getMessage());
							Toast.makeText(OpportunityService.this,
									"Error while fetching oppotunitites",
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
		if (MenuActivity2.isActivityVisible) {
			Intent nextIntent = new Intent("app_data");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		} else if (OpportunityListActivity.isActivityVisible) {
			Intent nextIntent = new Intent("opportunity_update_receiver");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		} else if (OpportunityAddActivity.isActivityVisible) {
			Intent nextIntent = new Intent("opportunity_update_receiver");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	List<Solution> solutionList;

	private Opportunity getOpportunityObject(JSONObject jsonObject) {
		solutionList = new ArrayList<Solution>();
		try {
			solutionList.add(new Solution(MyApp.decryptData(jsonObject
					.getString("ownerid")), MyApp.decryptData(jsonObject
					.getString("pcl_partnersolution1")),
					MyApp.decryptData(jsonObject
							.getString("pcl_solutionsolution1")), MyApp
							.decryptData(jsonObject
									.getString("pcl_profitcentersolution1")),
					MyApp.decryptData(jsonObject
							.getString("pcl_taxonomysolution1")), MyApp
							.decryptData(jsonObject
									.getString("pcl_feessolution1")),
					MyApp.decryptData(jsonObject
							.getString("pcl_nfrpreviousyearsolution1")), MyApp
							.decryptData(jsonObject
									.getString("pcl_nfrcurrentyearsolution1")),
					MyApp.decryptData(jsonObject
							.getString("pcl_nfrfy1solution1")), null));
			solutionList.add(new Solution(MyApp.decryptData(jsonObject
					.getString("pcl_managersolution2")), MyApp
					.decryptData(jsonObject.getString("pcl_partnersolution2")),
					MyApp.decryptData(jsonObject
							.getString("pcl_solutionsolution2")), MyApp
							.decryptData(jsonObject
									.getString("pcl_profitcentersolution2")),
					MyApp.decryptData(jsonObject
							.getString("pcl_taxonomysolution2")), MyApp
							.decryptData(jsonObject
									.getString("pcl_feessolution2")),
					MyApp.decryptData(jsonObject
							.getString("pcl_nfrpreviousyearsolution2")), MyApp
							.decryptData(jsonObject
									.getString("pcl_nfrcurrentyearsolution2")),
					MyApp.decryptData(jsonObject
							.getString("pcl_nfrfy1solution2")), MyApp
							.decryptData(jsonObject
									.getString("pcl_nfrfy2solution2"))));
			solutionList.add(new Solution(MyApp.decryptData(jsonObject
					.getString("pcl_managersolution3")), MyApp
					.decryptData(jsonObject.getString("pcl_partnersolution3")),
					MyApp.decryptData(jsonObject
							.getString("pcl_solutionsolution3")), MyApp
							.decryptData(jsonObject
									.getString("pcl_profitcentersolution3")),
					MyApp.decryptData(jsonObject
							.getString("pcl_taxonomysolution3")), MyApp
							.decryptData(jsonObject
									.getString("pcl_feessolution3")),
					MyApp.decryptData(jsonObject
							.getString("pcl_nfrpreviousyearsolution3")), MyApp
							.decryptData(jsonObject
									.getString("pcl_nfrcurrentyearsolution3")),
					MyApp.decryptData(jsonObject
							.getString("pcl_nfrfy1solution3")), MyApp
							.decryptData(jsonObject
									.getString("pcl_nfrfy2solution3"))));
			solutionList.add(new Solution(MyApp.decryptData(jsonObject
					.getString("pcl_managersolution4")), MyApp
					.decryptData(jsonObject.getString("pcl_partnersolution4")),
					MyApp.decryptData(jsonObject
							.getString("pcl_solutionsolution4")), MyApp
							.decryptData(jsonObject
									.getString("pcl_profitcentersolution4")),
					MyApp.decryptData(jsonObject
							.getString("pcl_taxonomysolution4")), MyApp
							.decryptData(jsonObject
									.getString("pcl_feessolution4")),
					MyApp.decryptData(jsonObject
							.getString("pcl_nfrpreviousyearsolution4")), MyApp
							.decryptData(jsonObject
									.getString("pcl_nfrcurrentyearsolution4")),
					MyApp.decryptData(jsonObject
							.getString("pcl_nfrfy1solution4")), MyApp
							.decryptData(jsonObject
									.getString("pcl_nfrfy2solution4"))));

			Opportunity opportunity = new Opportunity(
					MyApp.getPerfectString(jsonObject
							.getString("opportunityid")),
					MyApp.getPerfectString(jsonObject.getString("name")),
					MyApp.decryptData(jsonObject.getString("customerid")),
					MyApp.decryptData(jsonObject.getString("pcl_confendential")),
					MyApp.decryptData(jsonObject.getString("pcl_leadsourcenew")),
					MyApp.decryptData(jsonObject.getString("salesstagecode")),
					MyApp.decryptData(jsonObject
							.getString("opportunityratingcode")),
					MyApp.decryptData(jsonObject.getString("pcl_kpmgstatus")),
					dateFormatter.formatStringSpecialToDate(MyApp
							.getPerfectString(jsonObject
									.getString("estimatedclosedate"))),
					MyApp.decryptData(jsonObject.getString("estimatedvalue")),
					MyApp.decryptData(jsonObject
							.getString("pcl_noofsolutionsrequired")),
					solutionList);
			return opportunity;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
}
