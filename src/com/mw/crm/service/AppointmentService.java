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
import com.mw.crm.activity.MenuActivity2;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Appointment;

public class AppointmentService extends IntentService {

	MyApp myApp;
	Gson gson = new Gson();

	List<Appointment> appointmentList = new ArrayList<Appointment>();

	public AppointmentService() {
		super("AppointmentService");
	}

	public AppointmentService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
//		Toast.makeText(this, "AppointmentService", Toast.LENGTH_SHORT).show();
		myApp = (MyApp) getApplicationContext();

		try {

			String url = MyApp.URL + MyApp.APPOINTMENTS_DATA;

			System.out.println("URL : " + url);

			JSONObject params = new JSONObject();
			params = MyApp.addParamToJson(params);

			JsonArrayRequest jsObjRequest = new JsonArrayRequest(Method.POST,
					url, params, new Response.Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							System.out.println(">>>>Appointment Response => "
									+ response.toString());

							for (int i = 0; i < response.length(); i++) {
								try {
									appointmentList
											.add(getAppointmentObject(response
													.getJSONObject(i)));
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
							myApp.setAppointmentList(appointmentList);
							onRequestComplete();
						}

						// TODO What is the difference b/w creating this func
						// here or outside
						// private void getAppointmentObject(JSONArray
						// attributes) {
						// }
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println("ERROR  : " + error.getMessage());
							Toast.makeText(AppointmentService.this,
									"Error while fetching Appointment",
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
					}) {

				@Override
				protected VolleyError parseNetworkError(VolleyError volleyError) {
					if (volleyError.networkResponse != null
							&& volleyError.networkResponse.data != null) {
						VolleyError error = new VolleyError(new String(
								volleyError.networkResponse.data));
						volleyError = error;
					}
					return volleyError;
				}

			};

			RetryPolicy policy = new DefaultRetryPolicy(30000,
					DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
					DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
			jsObjRequest.setRetryPolicy(policy);
			Volley.newRequestQueue(this).add(jsObjRequest);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void onRequestComplete() {
		if (AppointmentAddActivity.isActivityVisible) {
		Intent nextIntent = new Intent("appointment_update_receiver");
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

	private Appointment getAppointmentObject(JSONObject jsonObject) {
		try {

			Appointment appointment = new Appointment(MyApp
					.decryptData(jsonObject.getString("activityid")), MyApp
					.getPerfectString(jsonObject.getString("subject")), MyApp
					.getPerfectString(jsonObject.getString("description")),
					MyApp.getPerfectString(jsonObject
							.getString("pcl_nameoftheclientofficial")), MyApp
							.decryptData(jsonObject
									.getString("pcl_typeofmeeting")),
					MyApp.getPerfectString(jsonObject
							.getString("pcl_designationofclientofficial")),
					MyApp.decryptData(jsonObject.getString("ownerid")), myApp
							.formatStringSpecialToDate(MyApp.getPerfectString(jsonObject
									.getString("scheduledstart"))), myApp
							.formatStringToDate(MyApp.decryptData(jsonObject
									.getString("scheduledend"))));
			return appointment;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
}
