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
		Toast.makeText(this, "ContactService", Toast.LENGTH_SHORT).show();
		myApp = (MyApp) getApplicationContext();

		try {

			String url = MyApp.URL + MyApp.CONTACTS_DATA;

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

							for (int i = 0; i < response.length(); i++) {
								try {
									contactList.add(getContactObject(response
											.getJSONObject(i)));
								} catch (JSONException e) {
									e.printStackTrace();
								}

							}
							myApp.setContactList(contactList);
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							System.out.println("ERROR  : " + error.getMessage());
							Toast.makeText(ContactService.this,
									"Error while fetching Contact",
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
			Intent nextIntent = new Intent("internal_connect_data");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		} else if (AppointmentAddActivity.isActivityVisible) {
			Intent nextIntent = new Intent("owner_data");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		} else if (MenuActivity.isActivityVisible) {
			Intent nextIntent = new Intent("app_data");
			LocalBroadcastManager.getInstance(this).sendBroadcast(nextIntent);
		}
	}

	private Contact getContactObject(JSONObject jsonObject) {
		try {

			Contact contact = new Contact(MyApp.getPerfectString(jsonObject
					.getString("firstname")), MyApp.decryptData(jsonObject
					.getString("lastname")), MyApp.decryptData(jsonObject
					.getString("emailaddress1")), MyApp.decryptData(jsonObject
					.getString("pcl_designation")), MyApp.decryptData(jsonObject
					.getString("mobilephone")), MyApp.decryptData(jsonObject
					.getString("telephone1")), MyApp.decryptData(jsonObject
					.getString("ownerid")), MyApp.decryptData(jsonObject
					.getString("parentcustomerid")), MyApp.decryptData(jsonObject
					.getString("customertypecode")), MyApp.decryptData(jsonObject
					.getString("contactid")));

			return contact;

		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}

	}
}
