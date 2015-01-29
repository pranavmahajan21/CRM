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
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Appointment;
import com.mw.crm.model.InternalConnect;

public class AppointmentAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	TextView purposeLabel_TV, nameClientLabel_TV, interactionTypeLabel_TV,
			designationLabel_TV, ownerLabel_TV;

	TextView interactionType_TV, owner_TV;

	EditText purpose_ET, nameClient_ET, designation_ET, notes_ET, startTime_ET,
			endTime_ET;

	RelativeLayout interactionType_RL, owner_RL;

	Intent previousIntent, nextIntent;

	List<InternalConnect> ownerList;

	int selectedInteraction = -1, selectedOwner = -1;

	Map<String, String> userMap;
	Map<String, String> interactionTypeMap;

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

		interactionTypeMap = myApp.getInteractionTypeMap();

		queue = Volley.newRequestQueue(this);
	}

	public void findThings() {
		super.findThings();

		purposeLabel_TV = (TextView) findViewById(R.id.purposeLabel_TV);
		nameClientLabel_TV = (TextView) findViewById(R.id.nameClientLabel_TV);
		interactionTypeLabel_TV = (TextView) findViewById(R.id.interactionTypeLabel_TV);
		designationLabel_TV = (TextView) findViewById(R.id.designationLabel_TV);
		ownerLabel_TV = (TextView) findViewById(R.id.ownerLabel_TV);

		interactionType_TV = (TextView) findViewById(R.id.interactionType_TV);
		owner_TV = (TextView) findViewById(R.id.owner_TV);

		purpose_ET = (EditText) findViewById(R.id.purpose_ET);
		nameClient_ET = (EditText) findViewById(R.id.nameClient_ET);
		designation_ET = (EditText) findViewById(R.id.designation_ET);
		notes_ET = (EditText) findViewById(R.id.notes_ET);
		startTime_ET = (EditText) findViewById(R.id.startTime_ET);
		endTime_ET = (EditText) findViewById(R.id.endTime_ET);

		owner_RL = (RelativeLayout) findViewById(R.id.owner_RL);
		interactionType_RL = (RelativeLayout) findViewById(R.id.interactionType_RL);
	}

	private void setTypeface() {
		purposeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		nameClientLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		interactionTypeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		designationLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		purpose_ET.setTypeface(myApp.getTypefaceRegularSans());
		nameClient_ET.setTypeface(myApp.getTypefaceRegularSans());
		designation_ET.setTypeface(myApp.getTypefaceRegularSans());
		notes_ET.setTypeface(myApp.getTypefaceRegularSans());
		startTime_ET.setTypeface(myApp.getTypefaceRegularSans());
		endTime_ET.setTypeface(myApp.getTypefaceRegularSans());
	}
	Appointment tempAppointment;
	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		if (previousIntent.hasExtra("position")) {
			tempAppointment = myApp.getAppointmentList().get(
					previousIntent.getIntExtra("position", 0));

			System.out.println(tempAppointment);

			purpose_ET.setText(tempAppointment.getPurposeOfMeeting());

			nameClient_ET.setText(tempAppointment.getNameOfTheClientOfficial());

			Integer temp = myApp.getValueFromStringJSON(tempAppointment
					.getTypeOfMeeting());
			if (temp != null) {
				interactionType_TV.setText(myApp.getInteractionTypeMap().get(
						Integer.toString(temp.intValue())));
				selectedInteraction = myApp.getIndexFromKeyInteractionMap(Integer.toString(temp
						.intValue()));
			}
			
			designation_ET.setText(tempAppointment
					.getDesignationOfClientOfficial());
			notes_ET.setText(tempAppointment.getDescription());

			// startTime_ET.setText(tempAppointment.getStartTime().toString());
			// endTime_ET.setText(tempAppointment.getEndTime().toString());

			
			owner_TV.setText(myApp
					.getStringNameFromStringJSON(tempAppointment
							.getOwnerId()));
			selectedOwner = myApp
					.getIndexFromKeyUserMap(myApp
							.getStringIdFromStringJSON(tempAppointment
									.getOwnerId()));
//			try {
//				ownerLabel_TV.setText(new JSONObject(tempAppointment
//						.getOwnerId()).getString("Name"));
//			} catch (JSONException e) {
//				e.printStackTrace();
//			}
		}
	}

	private void hideKeyboardFunctionality() {
		((RelativeLayout) findViewById(R.id.activity_appointment_add_RL))
				.setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(getCurrentFocus()
								.getWindowToken(), 0);
						return false;
					}
				});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appointment_add);

		initThings();
		findThings();
		initView("Add Appointment", "Submit");

		hideKeyboardFunctionality();

		registerForContextMenu(interactionType_RL);
		// registerForContextMenu(owner_RL);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.interactionType_RL) {
			List<String> list = new ArrayList<String>(
					interactionTypeMap.values());
			for (int i = 0; i < list.size(); i++) {
				menu.add(0, v.getId(), i, list.get(i));
			}
		}
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		if (item.getGroupId() == 0) {
			selectedInteraction = item.getOrder();
			interactionType_TV.setText(item.getTitle());
		}
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

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra("request_code", requestCode);
		super.startActivityForResult(intent, requestCode);
	}

	public void onSearchItem(View view) {
		nextIntent = new Intent(this, SearchActivity.class);

		switch (view.getId()) {
		case R.id.owner_RL:
			startActivityForResult(nextIntent, MyApp.SEARCH_USER);
			break;

		default:
			break;
		}

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			int positionItem = data.getIntExtra("position_item", 0);
			if (requestCode == MyApp.SEARCH_USER) {
				userMap = myApp.getUserMap();
				List<String> list = new ArrayList<String>(userMap.values());
				owner_TV.setText(list.get(positionItem));
				selectedOwner = positionItem;
			}
		}

	}
}
