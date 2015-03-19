package com.mw.crm.service;

import java.util.LinkedHashMap;
import java.util.Map;

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
import com.mw.crm.extra.Constant;
import com.mw.crm.extra.MyApp;

public class CompetitorService extends IntentService {

	MyApp myApp;
	Gson gson = new Gson();
	Map<String, String> competitorMap = new LinkedHashMap<String, String>();

	public CompetitorService() {
		super("CompetitorService");
	}

	public CompetitorService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		myApp = (MyApp) getApplicationContext();

		try {

			String url = MyApp.URL + Constant.COMPETITOR;

			JSONObject params = new JSONObject();
			params = MyApp.addParamToJson(params);

			JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
					Method.POST, url, params,
					new Response.Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							System.out.println("length2" + response);

							for (int i = 0; i < response.length(); i++) {
								try {
									createCompetitorMap(response.getJSONObject(i));
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
							// TODO : update preferences as well
							myApp.setCompetitorMap(competitorMap, true);
							onRequestComplete();
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println("ERROR  : " + error.getMessage());
							Toast.makeText(
									CompetitorService.this,
									"Error while fetching cometitor data.",
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
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void createCompetitorMap(JSONObject jsonObject) {
		try {

			/*System.out.println("[][]"
					+ MyApp.getPerfectString(jsonObject
							.getString("competitorid")) + "\n[][]"
					+ MyApp.getPerfectString(jsonObject.getString("name")));*/

			competitorMap.put(MyApp.getPerfectString(jsonObject
					.getString("competitorid")), MyApp
					.getPerfectString(jsonObject.getString("name")));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}