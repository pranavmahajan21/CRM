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
import com.mw.crm.application.MyApp;
import com.mw.crm.extra.Constant;

public class ProfitCenterService extends IntentService {

	MyApp myApp;
	Gson gson = new Gson();
	Map<String, String> profitCenterMap = new LinkedHashMap<String, String>();

	public ProfitCenterService() {
		super("ProfitCenterService");
	}

	public ProfitCenterService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Toast.makeText(this, "InternalConnectService",
		// Toast.LENGTH_SHORT).show();
		myApp = (MyApp) getApplicationContext();

		try {

			String url = Constant.URL + Constant.PROFIT_CENTER;

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
									createProfitCenterMap(response.getJSONObject(i));
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
							// TODO : update preferences as well
							myApp.setProfitCenterMap(profitCenterMap, true);
							onRequestComplete();
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println("ERROR  : " + error.getMessage());
							Toast.makeText(
									ProfitCenterService.this,
									"Error while fetching profit center data.",
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

	private void createProfitCenterMap(JSONObject jsonObject) {
		try {

			/*System.out.println("[][]"
					+ MyApp.getPerfectString(jsonObject
							.getString("pcl_profitcenterid")) + "\n[][]"
					+ MyApp.getPerfectString(jsonObject.getString("pcl_name")));*/

			profitCenterMap.put(MyApp.getPerfectString(jsonObject
					.getString("pcl_profitcenterid")), MyApp
					.getPerfectString(jsonObject.getString("pcl_name")));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
