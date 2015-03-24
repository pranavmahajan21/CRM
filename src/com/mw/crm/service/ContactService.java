package com.mw.crm.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.mw.crm.activity.ContactAddActivity;
import com.mw.crm.activity.ContactListActivity;
import com.mw.crm.activity.MenuActivity2;
import com.mw.crm.application.MyApp;
import com.mw.crm.extra.Constant;
import com.mw.crm.extra.DateFormatter;
import com.mw.crm.model.Contact;

public class ContactService extends IntentService {

	MyApp myApp;
	Gson gson = new Gson();

	List<Contact> contactList = new ArrayList<Contact>();

	public ContactService() {
		super("ContactService");
	}

	public ContactService(String name) {
		super(name);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// Toast.makeText(this, "ContactService", Toast.LENGTH_SHORT).show();
		myApp = (MyApp) getApplicationContext();

		try {

			String url = Constant.URL + Constant.CONTACTS_DATA;

			System.out.println("URL : " + url);

			JSONObject params = new JSONObject();
			params = MyApp.addParamToJson(params);

			System.out.println("json" + params);

			JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
					Method.POST, url, params,
					new Response.Listener<JSONArray>() {

						@Override
						public void onResponse(JSONArray response) {
							System.out.println("Contact response"
									+ response.toString());

							ProcessResponseAsynTask asynTask = new ProcessResponseAsynTask();
							asynTask.execute(response);
							
//							for (int i = 0; i < response.length(); i++) {
//								try {
//									contactList.add(getContactObject(response
//											.getJSONObject(i)));
//								} catch (JSONException e) {
//									e.printStackTrace();
//								}
//
//							}
//							myApp.setContactList(contactList, true,
//									new DateFormatter()
//											.formatDateToString3(new Date()));
//							onRequestComplete();
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println("ERROR  : " + error.getMessage());
							Toast.makeText(ContactService.this,
									"Error while fetching Contact",
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
		} else if (ContactListActivity.isActivityVisible) {
			Intent nextIntent = new Intent("contact_update_receiver");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		} else if (ContactAddActivity.isActivityVisible) {
			Intent nextIntent = new Intent("contact_update_receiver");
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
					contactList.add(getContactObject(responseJsonArray
							.getJSONObject(i)));
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}
			myApp.setContactList(contactList, true,
					new DateFormatter()
							.formatDateToString3(new Date()));
			return null;
		}

		@Override
		protected void onPostExecute(Void aa) {
			super.onPostExecute(aa);
			onRequestComplete();
		}

	}

	private Contact getContactObject(JSONObject jsonObject) {
		try {

			Contact contact = new Contact(
					MyApp.getPerfectString(jsonObject.getString("firstname")),
					MyApp.getPerfectString(jsonObject.getString("lastname")),
					MyApp.getPerfectString(jsonObject
							.getString("emailaddress1")),
					MyApp.getPerfectString(jsonObject
							.getString("pcl_designation")),
					MyApp.getPerfectString(jsonObject.getString("mobilephone")),
					MyApp.getPerfectString(jsonObject.getString("telephone1")),
					MyApp.decryptData(jsonObject.getString("ownerid")), MyApp
							.decryptData(jsonObject
									.getString("parentcustomerid")), MyApp
							.decryptData(jsonObject
									.getString("customertypecode")),
					MyApp.getPerfectString(jsonObject.getString("contactid")));

			return contact;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
}
