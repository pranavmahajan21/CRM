package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.gson.Gson;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Appointment;
import com.mw.crm.model.InternalConnect;
import com.mw.crm.service.AppointmentService;

public class AppointmentAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	TextView purposeLabel_TV, nameClientLabel_TV, interactionTypeLabel_TV,
			designationLabel_TV, detailsDiscussionLabel_TV,
			dateMeetingLabel_TV, endTimeLabel_TV;
	/** Removed temporarily **/
	// , ownerLabel_TV

	TextView interactionType_TV, dateMeeting_TV, endTime_TV;
	/** Removed temporarily **/
	// , owner_TV

	EditText purpose_ET, nameClient_ET, designation_ET, detailsDiscussion_ET;
	// startTime_ET, endTime_ET
	RelativeLayout interactionType_RL;
	/** Removed temporarily **/
	// , owner_RL

	Intent previousIntent, nextIntent;
	Appointment tempAppointment;

	List<InternalConnect> ownerList;

	int selectedInteraction = -1, selectedOwner = -1;

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	Map<String, String> userMap;
	Map<String, String> interactionTypeMap;

	RequestQueue queue;

	private BroadcastReceiver appointmentUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			progressDialog.dismiss();

			Toast.makeText(AppointmentAddActivity.this,
					"Appointment created successfully.", Toast.LENGTH_SHORT)
					.show();

			Appointment aa = new Appointment(null, purpose_ET.getText()
					.toString(), detailsDiscussion_ET.getText().toString(),
					nameClient_ET.getText().toString(), interactionType_TV
							.getText().toString(), designation_ET.getText()
							.toString(), null, new Date(), new Date());

			/** Removed temporarily **/
			// owner_TV.getText().toString()

			nextIntent = new Intent(AppointmentAddActivity.this,
					AppointmentDetailsActivity.class);
			nextIntent.putExtra("appointment_dummy",
					new Gson().toJson(aa, Appointment.class));
			if (!(previousIntent.hasExtra("is_edit_mode") && previousIntent
					.getBooleanExtra("is_edit_mode", false))) {
				nextIntent.putExtra("appointment_created", true);
			}
			startActivityForResult(nextIntent, MyApp.DETAILS_APPOINTMENT);

		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		interactionTypeMap = myApp.getInteractionTypeMap();
		userMap = myApp.getUserMap();

		queue = Volley.newRequestQueue(this);

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Saving Changes",
				"This may take some time", true, null);
	}

	public void findThings() {
		super.findThings();

		purposeLabel_TV = (TextView) findViewById(R.id.purposeLabel_TV);
		nameClientLabel_TV = (TextView) findViewById(R.id.nameClientLabel_TV);
		interactionTypeLabel_TV = (TextView) findViewById(R.id.interactionTypeLabel_TV);
		designationLabel_TV = (TextView) findViewById(R.id.designationLabel_TV);
		detailsDiscussionLabel_TV = (TextView) findViewById(R.id.detailsDiscussionLabel_TV);
		dateMeetingLabel_TV = (TextView) findViewById(R.id.dateMeetingLabel_TV);
		endTimeLabel_TV = (TextView) findViewById(R.id.endTimeLabel_TV);

		/** Removed temporarily **/
		// ownerLabel_TV = (TextView) findViewById(R.id.ownerLabel_TV);

		interactionType_TV = (TextView) findViewById(R.id.interactionType_TV);
		dateMeeting_TV = (TextView) findViewById(R.id.dateMeeting_TV);
		endTime_TV = (TextView) findViewById(R.id.endTime_TV);
		/** Removed temporarily **/
		// owner_TV = (TextView) findViewById(R.id.owner_TV);

		purpose_ET = (EditText) findViewById(R.id.purpose_ET);
		nameClient_ET = (EditText) findViewById(R.id.nameClient_ET);
		designation_ET = (EditText) findViewById(R.id.designation_ET);
		detailsDiscussion_ET = (EditText) findViewById(R.id.detailsDiscussion_ET);
		// startTime_ET = (EditText) findViewById(R.id.startTime_ET);
		// endTime_ET = (EditText) findViewById(R.id.endTime_ET);

		/** Removed temporarily **/
		// owner_RL = (RelativeLayout) findViewById(R.id.owner_RL);
		interactionType_RL = (RelativeLayout) findViewById(R.id.interactionType_RL);
	}

	private void setTypeface() {
		purposeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		nameClientLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		interactionTypeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		designationLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		detailsDiscussionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		dateMeetingLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		endTimeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		interactionType_TV.setTypeface(myApp.getTypefaceRegularSans());
		dateMeeting_TV.setTypeface(myApp.getTypefaceRegularSans());
		endTime_TV.setTypeface(myApp.getTypefaceRegularSans());

		purpose_ET.setTypeface(myApp.getTypefaceRegularSans());
		nameClient_ET.setTypeface(myApp.getTypefaceRegularSans());
		designation_ET.setTypeface(myApp.getTypefaceRegularSans());
		detailsDiscussion_ET.setTypeface(myApp.getTypefaceRegularSans());

		// startTime_ET.setTypeface(myApp.getTypefaceRegularSans());
		// endTime_ET.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		System.out.println(new ArrayList<String>(userMap.keySet()).get(2));
		System.out.println(new ArrayList<String>(userMap.values()).get(2));

		if (previousIntent.hasExtra("position")) {
			tempAppointment = myApp.getAppointmentList().get(
					previousIntent.getIntExtra("position", 0));

			System.out.println(tempAppointment);

			purpose_ET.setText(tempAppointment.getPurposeOfMeeting());

			nameClient_ET.setText(tempAppointment.getNameOfTheClientOfficial());

			Integer temp = myApp.getIntValueFromStringJSON(tempAppointment
					.getTypeOfMeeting());
			if (temp != null) {
				interactionType_TV.setText(myApp.getInteractionTypeMap().get(
						Integer.toString(temp.intValue())));
				selectedInteraction = myApp
						.getIndexFromKeyInteractionMap(Integer.toString(temp
								.intValue()));
			}

			designation_ET.setText(tempAppointment
					.getDesignationOfClientOfficial());
			detailsDiscussion_ET.setText(tempAppointment.getDescription());

			dateMeeting_TV.setText(myApp.formatDateToString2(tempAppointment
					.getStartTime()));
			endTime_TV.setText(myApp.formatDateToString2(tempAppointment
					.getEndTime()));

			/** Removed temporarily **/
			// owner_TV.setText(myApp.getStringNameFromStringJSON(tempAppointment
			// .getOwnerId()));
			selectedOwner = myApp.getIndexFromKeyUserMap(myApp
					.getStringIdFromStringJSON(tempAppointment.getOwnerId()));

			System.out.println("selectedInteraction  : " + selectedInteraction
					+ "\nselectedOwner  : " + selectedOwner);
		}
	}

	@SuppressLint("ClickableViewAccessibility")
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
		initView("Add Appointment", "Save");

		hideKeyboardFunctionality();

		registerForContextMenu(interactionType_RL);
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
		hideKeyboard(this.getCurrentFocus());
		openContextMenu(view);
	}

	private boolean validate() {
		boolean notErrorCase = true;
		if (purpose_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter some Purpose of Meeting.", false);
			notErrorCase = false;
		} else if (nameClient_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter the Name of Client Official.", false);
			notErrorCase = false;
		} else if (selectedInteraction < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select an interaction type.", false);
			notErrorCase = false;
		} else if (designation_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter the designation.", false);
			notErrorCase = false;
		} else if (detailsDiscussion_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter some details for the meeting.", false);
			notErrorCase = false;
		} else if (dateMeeting_TV.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a date for meeting.", false);
			notErrorCase = false;
		} else if (endTime_TV.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select an end time.", false);
			notErrorCase = false;
		}
		if (!notErrorCase) {
			alertDialogBuilder.setPositiveButton("OK",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.dismiss();
						}
					});
			alertDialog = alertDialogBuilder.create();
			alertDialog.show();
		}
		return notErrorCase;
	}

	public void onRightButton(View view) {
		if (!validate()) {
			return;
		}

		// System.out.println(myApp.formatDateToString4(new Date()));

		JSONObject params = new JSONObject();

		try {
			params.put("Subject",
					MyApp.encryptData(purpose_ET.getText().toString()))
					.put("clientname",
							MyApp.encryptData(nameClient_ET.getText()
									.toString()))
					.put("inttype",
							new ArrayList<String>(interactionTypeMap.keySet())
									.get(selectedInteraction))
					.put("designation",
							MyApp.encryptData(designation_ET.getText()
									.toString()))
					.put("describe",
							MyApp.encryptData(detailsDiscussion_ET.getText()
									.toString()))
					.put("startdate", myApp.formatDateToString4(myApp.formatStringToDate2(dateMeeting_TV.getText().toString())))
					.put("enddate", myApp.formatDateToString4(myApp.formatStringToDate2(endTime_TV.getText().toString())))
					.put("ownid", "db843bfb-9a8a-e411-96e8-5cf3fc3f502a");
			// .put("ownid",
			// new ArrayList<String>(userMap.keySet())
			// .get(selectedOwner))
			
//			.put("startdate", myApp.formatDateToString4(new Date()))
//			.put("enddate", myApp.formatDateToString4(new Date()))
//			.put("startdate", myApp.formatDateToString4(myApp.formatStringToDate2(dateMeeting_TV.getText().toString())))
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		params = MyApp.addParamToJson(params);

		progressDialog.show();

		if (previousIntent.getBooleanExtra("is_edit_mode", false)) {
			/** Update Mode **/
			String url = MyApp.URL + MyApp.APPOINTMENTS_UPDATE;
			try {
				System.out.println("URL : " + url);

				params.put("apponid", "8d0592e9-79a1-e411-96e8-5cf3fc3f502a");
				JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
						Method.POST, url, params,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								System.out.println("length2" + response);
								onPositiveResponse();
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								progressDialog.hide();
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
				progressDialog.hide();
				e.printStackTrace();
			}
		} else {
			/** Create Mode **/
			try {

				String url = MyApp.URL + MyApp.APPOINTMENTS_ADD;

				System.out.println("URL : " + url);

				System.out.println("json" + params);

				JsonObjectRequest jsonArrayRequest = new JsonObjectRequest(
						Method.POST, url, params,
						new Response.Listener<JSONObject>() {

							@Override
							public void onResponse(JSONObject response) {
								System.out.println("length2" + response);
								onPositiveResponse();
							}
						}, new Response.ErrorListener() {

							@Override
							public void onErrorResponse(VolleyError error) {
								progressDialog.hide();
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
				progressDialog.hide();
				e.printStackTrace();
			}
		}
	}

	private void onPositiveResponse() {
		progressDialog.dismiss();

		progressDialog = createDialog.createProgressDialog(
				"Updating Appointments", "This may take some time", true, null);
		progressDialog.show();

		Intent serviceIntent = new Intent(this, AppointmentService.class);
		startService(serviceIntent);

	}

	@Override
	protected void onResume() {
		super.onResume();
		isActivityVisible = true;
		LocalBroadcastManager.getInstance(this).registerReceiver(
				appointmentUpdateReceiver,
				new IntentFilter("appointment_update_receiver"));

	}

	@Override
	protected void onPause() {
		isActivityVisible = false;
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				appointmentUpdateReceiver);
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
		/** Removed temporarily **/
		// case R.id.owner_RL:
		// startActivityForResult(nextIntent, MyApp.SEARCH_USER);
		// break;

		default:
			break;
		}

	}

	private void aaaaa(View view, String x, boolean isDate) {
		System.out.println(x);
		switch (view.getId()) {
		case R.id.dateMeeting_RL:
			if (isDate) {
				dateMeeting_TV.setText(x);
			} else {
				dateMeeting_TV.setText(dateMeeting_TV.getText().toString()
						+ " " + x);
			}
			break;
		case R.id.endTime_RL:
			if (isDate) {
				endTime_TV.setText(x);
			} else {
				endTime_TV.setText(endTime_TV.getText().toString() + " " + x);
			}
			break;

		default:
			break;
		}
	}

	public void onPickDate(final View view) {
		System.out.println("1");
		final TimePickerDialog tPicker = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view2, int hourOfDay,
							int minute) {
						// System.out.println(hourOfDay + ":" + minute);
						String temp = hourOfDay + ":" + minute;
						aaaaa(view, temp, false);
					}
				}, 12, 00, true);
		tPicker.setCancelable(false);

		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dPicker = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view2, int year,
							int monthOfYear, int dayOfMonth) {
						// System.out.println(dayOfMonth + "-" + (monthOfYear +
						// 1)
						// + "-" + year);
						String temp = dayOfMonth + "-" + (monthOfYear + 1)
								+ "-" + year;
						aaaaa(view, temp, true);
						tPicker.show();

					}
				}, mYear, mMonth, mDay);
		dPicker.setCancelable(false);
		dPicker.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			int positionItem = 0;
			if (data != null) {
				positionItem = data.getIntExtra("position_item", 0);
			}
			if (requestCode == MyApp.SEARCH_USER) {
				userMap = myApp.getUserMap();
				List<String> list = new ArrayList<String>(userMap.values());
				/** Removed temporarily **/
				// owner_TV.setText(list.get(positionItem));
				selectedOwner = positionItem;
			}
			if (requestCode == MyApp.DETAILS_APPOINTMENT) {
				Intent intent = new Intent();
				intent.putExtra("refresh_list", true);
				setResult(RESULT_OK, intent);
				finish();
			}

		}

	}
}
