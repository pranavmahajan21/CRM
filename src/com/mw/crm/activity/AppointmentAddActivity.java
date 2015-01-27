package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Appointment;
import com.mw.crm.model.InternalConnect;

public class AppointmentAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	TextView purpose_TV, nameClient_TV, interaction_TV, designation_TV,
			owner_TV;
	EditText purpose_ET, nameClient_ET, interaction_ET, designation_ET,
			notes_ET, startTime_ET, endTime_ET;
	RelativeLayout owner_RL;

	Intent previousIntent;

	List<InternalConnect> ownerList;
	Map<String, String> ownerMap;

	RequestQueue queue;

	private BroadcastReceiver ownerReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			registerForContextMenu(owner_RL);
		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		queue = Volley.newRequestQueue(this);
	}

	public void findThings() {
		super.findThings();

		purpose_TV = (TextView) findViewById(R.id.purpose_TV);
		nameClient_TV = (TextView) findViewById(R.id.nameClient_TV);
		interaction_TV = (TextView) findViewById(R.id.interaction_TV);
		designation_TV = (TextView) findViewById(R.id.designation_TV);
		owner_TV = (TextView) findViewById(R.id.owner_TV);

		purpose_ET = (EditText) findViewById(R.id.purpose_ET);
		nameClient_ET = (EditText) findViewById(R.id.nameClient_ET);
		interaction_ET = (EditText) findViewById(R.id.interaction_ET);
		designation_ET = (EditText) findViewById(R.id.designation_ET);
		notes_ET = (EditText) findViewById(R.id.notes_ET);
		startTime_ET = (EditText) findViewById(R.id.startTime_ET);
		endTime_ET = (EditText) findViewById(R.id.endTime_ET);

		owner_RL = (RelativeLayout) findViewById(R.id.owner_RL);

	}

	private void setTypeface() {
		purpose_TV.setTypeface(myApp.getTypefaceRegularSans());
		nameClient_TV.setTypeface(myApp.getTypefaceRegularSans());
		interaction_TV.setTypeface(myApp.getTypefaceRegularSans());
		designation_TV.setTypeface(myApp.getTypefaceRegularSans());

		purpose_ET.setTypeface(myApp.getTypefaceRegularSans());
		nameClient_ET.setTypeface(myApp.getTypefaceRegularSans());
		interaction_ET.setTypeface(myApp.getTypefaceRegularSans());
		designation_ET.setTypeface(myApp.getTypefaceRegularSans());
		notes_ET.setTypeface(myApp.getTypefaceRegularSans());
		startTime_ET.setTypeface(myApp.getTypefaceRegularSans());
		endTime_ET.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		if (previousIntent.hasExtra("position")) {
			Appointment tempAppointment = myApp.getAppointmentList().get(
					previousIntent.getIntExtra("position", 0));

			System.out.println(tempAppointment);

			purpose_ET.setText(tempAppointment.getPurposeOfMeeting());

			nameClient_ET.setText(tempAppointment.getNameOfTheClientOfficial());

			try {
				interaction_ET.setText(myApp.getInteractionTypeMap().get(
						Integer.toString(new JSONObject(tempAppointment
								.getTypeOfMeeting()).getInt("Value"))));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			designation_ET.setText(tempAppointment
					.getDesignationOfClientOfficial());
			 notes_ET.setText(tempAppointment.getDescription());

			// startTime_ET.setText(tempAppointment.getStartTime().toString());
			// endTime_ET.setText(tempAppointment.getEndTime().toString());

			try {
				owner_TV.setText(new JSONObject(tempAppointment.getOwnerId())
						.getString("Name"));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_add);

		initThings();
		findThings();
		initView("Add Appointment", "Submit");

		registerForContextMenu(owner_RL);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		// menu.add(0, v.getId(), 0, "A, Pradeep");
		// menu.add(0, v.getId(), 0, "Abhishek, A");
		// menu.add(0, v.getId(), 0, "Abraham, Anil Alex");
		// menu.add(0, v.getId(), 0, "Abraham Philomena");
		// menu.add(0, v.getId(), 0, "Adhikari Sawant, Rupali");
		// menu.add(0, v.getId(), 0, "Adhikari, Pratik");
		// menu.add(0, v.getId(), 0, "Adhikary, Subhendu");
		// menu.add(0, v.getId(), 0, "Administrator, CRM");
		// menu.add(0, v.getId(), 0, "Advani, Harish");
		// menu.add(0, v.getId(), 0, "Advani, Vikram");
		// menu.add(0, v.getId(), 0, "Agarwal, Abhijit");
		// menu.add(0, v.getId(), 0, "Agarwal, Abhishek");

//		ownerList = myApp.getInternalConnectList();
//
//		for (int i = 0; i < ownerList.size(); i++) {
//			menu.add(1, v.getId(), i, ownerList.get(i).getLastName());
//		}
		
		ownerMap = myApp.getUserMap();
		List<String> list = new ArrayList<String>(ownerMap.values());
		for (int i = 0; i < list.size(); i++) {
			menu.add(0, v.getId(), i, list.get(i));
		}
//		for (int i = 0; i < ownerMap.size(); i++) {
//			menu.add(1, v.getId(), i, ownerList.get(i).getLastName());
//		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		owner_TV.setText(item.getTitle());
		return super.onContextItemSelected(item);

	}

	public void onOpenContextMenu(View view) {
		openContextMenu(view);
	}

	public void onRightButton(View view) {
		// System.out.println("\nSubject  :  "
		// + MyApp.encryptData(purpose_ET.getText().toString())
		// + "\nclientname  :  "
		// + MyApp.encryptData(nameClient_ET.getText().toString())
		// + "\ndesignation  :  "
		// + MyApp.encryptData(designation_ET.getText().toString())
		// + "\ndescribe  :  "
		// + MyApp.encryptData(notes_ET.getText().toString()));

		JSONObject params = new JSONObject();
		try {
			params.put("Subject", purpose_ET.getText().toString())
					.put("clientname", nameClient_ET.getText().toString())
					.put("designation", designation_ET.getText().toString())
					.put("describe", notes_ET.getText().toString())
					.put("inttype", "4")
					.put("ownid", "db843bfb-9a8a-e411-96e8-5cf3fc3f502a")
					.put("startdate", "2015-01-14T14:16:34Z")
					.put("enddate", "2015-01-14T14:16:34Z");
		} catch (JSONException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		params = MyApp.addParamToJson(params);

		if (previousIntent.getBooleanExtra("is_edit_mode", false)) {
			try {

				String url = MyApp.URL + MyApp.APPOINTMENTS_UPDATE;

				System.out.println("URL : " + url);

				System.out.println("json" + params);
				params.put("apponid", "8d0592e9-79a1-e411-96e8-5cf3fc3f502a");
				JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
						Method.POST, url, params,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								System.out.println("length2" + response);

							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								System.out.println("ERROR  : "
										+ error.getMessage());
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
				queue.add(jsonArrayRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {

				String url = MyApp.URL + MyApp.APPOINTMENTS_ADD;

				System.out.println("URL : " + url);

				// System.out.println("\nSubject  :  "
				// + MyApp.encryptData(purpose_ET.getText().toString())
				// + "\nclientname  :  "
				// + MyApp.encryptData(nameClient_ET.getText().toString())
				// + "\ndesignation  :  "
				// + MyApp.encryptData(designation_ET.getText().toString())
				// + "\ndescribe  :  "
				// + MyApp.encryptData(notes_ET.getText().toString()));

				// JSONObject params = new JSONObject();
				// params.put("Subject", purpose_ET.getText().toString())
				// .put("clientname", nameClient_ET.getText().toString())
				// .put("designation", designation_ET.getText().toString())
				// .put("describe", notes_ET.getText().toString())
				// .put("inttype", "4")
				// .put("ownid", "db843bfb-9a8a-e411-96e8-5cf3fc3f502a")
				// .put("startdate", "2015-01-14T14:16:34Z")
				// .put("enddate", "2015-01-14T14:16:34Z");
				//
				// params = MyApp.addParamToJson(params);

				System.out.println("json" + params);

				JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
						Method.POST, url, params,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								System.out.println("length2" + response);

							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								System.out.println("ERROR  : "
										+ error.getMessage());
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
				queue.add(jsonArrayRequest);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isActivityVisible = true;
		LocalBroadcastManager.getInstance(this).registerReceiver(ownerReceiver,
				new IntentFilter("owner_data"));

	}

	@Override
	protected void onPause() {
		isActivityVisible = false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				ownerReceiver);
		super.onPause();
	}
}
