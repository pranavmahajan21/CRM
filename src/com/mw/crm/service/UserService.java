package com.mw.crm.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
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

public class UserService extends IntentService {

	MyApp myApp;
	Gson gson = new Gson();
	Map<String, String> userMap = new LinkedHashMap<String, String>();

	public UserService() {
		super("UserService");
	}

	public UserService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Toast.makeText(this, "InternalConnectService",
		// Toast.LENGTH_SHORT).show();
		myApp = (MyApp) getApplicationContext();

		try {

			String url = Constant.URL + Constant.USER;

			JSONObject params = new JSONObject();
			params = MyApp.addParamToJson(params);

			JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
					Method.POST, url, params,
					new Response.Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							System.out.println("length2" + response);

							ProcessResponseAsynTask asynTask = new ProcessResponseAsynTask();
							asynTask.execute(response);

							// for (int i = 0; i < response.length(); i++) {
							// try {
							// createUserMap(response.getJSONObject(i));
							// } catch (JSONException e) {
							// e.printStackTrace();
							// }
							//
							// }
							// // TODO : update preferences as well
							// myApp.setUserMap(userMap, true);
							// onRequestComplete();
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out
									.println("ERROR  : " + error.getMessage());
							Toast.makeText(UserService.this,
									"Error while fetching user data.",
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

	private class ProcessResponseAsynTask extends
			AsyncTask<JSONArray, Void, Void> {

		@Override
		protected Void doInBackground(JSONArray... params) {
			JSONArray responseJsonArray = params[0];
			for (int i = 0; i < responseJsonArray.length(); i++) {
				try {
					createUserMap(responseJsonArray.getJSONObject(i));
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			// TODO : update preferences as well
			myApp.setUserMap(userMap, true);

			return null;
		}

		@Override
		protected void onPostExecute(Void aa) {
			super.onPostExecute(aa);
			onRequestComplete();
		}

	}

	private void createUserMap(JSONObject jsonObject) {
		try {

			/*
			 * System.out.println("[][]" + MyApp.getPerfectString(jsonObject
			 * .getString("systemuserid")) + "\n[][]" +
			 * MyApp.getPerfectString(jsonObject.getString("lastname")));
			 */

			userMap.put(MyApp.getPerfectString(jsonObject
					.getString("systemuserid")), MyApp
					.getPerfectString(jsonObject.getString("lastname")));
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
}
