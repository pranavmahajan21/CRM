package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.Calendar;
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
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.application.MyApp;
import com.mw.crm.extra.Constant;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.DateFormatter;
import com.mw.crm.extra.SearchEngine;
import com.mw.crm.model.Account;
import com.mw.crm.model.Appointment;
import com.mw.crm.service.AppointmentService;

public class AppointmentAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	TextView purposeLabel_TV, accountLabel_TV, nameClientLabel_TV,
			designationLabel_TV, interactionTypeLabel_TV,
			detailsDiscussionLabel_TV, dateMeetingLabel_TV, endTimeLabel_TV,
			ownerLabel_TV;
	// , organizerLabel_TV

	TextView account_TV, interactionType_TV, dateMeeting_TV, endTime_TV,
			owner_TV;
	// , organizer_TV

	EditText purpose_ET, nameClient_ET, designation_ET, detailsDiscussion_ET;

	RelativeLayout account_RL, interactionType_RL, owner_RL;
	// , organizer_RL

	Intent previousIntent, nextIntent;
	Appointment tempAppointment;

	// List<InternalConnect> ownerList;
	List<Account> accountList;

	int selectedAccount = -1, selectedInteraction = -1, selectedOwner = -1;
	// ,selectedOrganizer = -1

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	Map<String, String> userMap;
	Map<String, String> interactionTypeMap;

	RequestQueue queue;
	SearchEngine searchEngine;

	DateFormatter dateFormatter;

	private BroadcastReceiver appointmentUpdateReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			progressDialog.dismiss();

			if (!(previousIntent.hasExtra("is_edit_mode") && previousIntent
					.getBooleanExtra("is_edit_mode", false))) {
				Toast.makeText(AppointmentAddActivity.this,
						"Appointment updated successfully.", Toast.LENGTH_SHORT)
						.show();
			} else {
				Toast.makeText(AppointmentAddActivity.this,
						"Appointment created successfully.", Toast.LENGTH_SHORT)
						.show();
			}

			Appointment dummyAppointment = new Appointment(null, purpose_ET
					.getText().toString(), account_TV.getText().toString(),
					nameClient_ET.getText().toString(), designation_ET
							.getText().toString(), detailsDiscussion_ET
							.getText().toString(), interactionType_TV.getText()
							.toString(), owner_TV.getText().toString(), null,
					dateFormatter.formatStringToDate3Copy(dateMeeting_TV
							.getText().toString()),
					dateFormatter.formatStringToDate3Copy(endTime_TV.getText()
							.toString()));

			nextIntent = new Intent(AppointmentAddActivity.this,
					AppointmentDetailsActivity.class);
			nextIntent.putExtra("appointment_dummy",
					new Gson().toJson(dummyAppointment, Appointment.class));
			if (!(previousIntent.hasExtra("is_edit_mode") && previousIntent
					.getBooleanExtra("is_edit_mode", false))) {
				nextIntent.putExtra("appointment_created", true);
			}
			startActivityForResult(nextIntent, Constant.DETAILS_APPOINTMENT);

		}
	};

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		accountList = myApp.getAccountList();
		interactionTypeMap = myApp.getInteractionTypeMap();
		userMap = myApp.getUserMap();

		queue = Volley.newRequestQueue(this);

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Saving Changes",
				"This may take some time", true, null);

		searchEngine = new SearchEngine(this);
		dateFormatter = new DateFormatter();
	}

	public void findThings() {
		super.findThings();

		purposeLabel_TV = (TextView) findViewById(R.id.purposeLabel_TV);
		accountLabel_TV = (TextView) findViewById(R.id.accountLabel_TV);
		nameClientLabel_TV = (TextView) findViewById(R.id.nameClientLabel_TV);
		interactionTypeLabel_TV = (TextView) findViewById(R.id.interactionTypeLabel_TV);
		designationLabel_TV = (TextView) findViewById(R.id.designationLabel_TV);
		detailsDiscussionLabel_TV = (TextView) findViewById(R.id.detailsDiscussionLabel_TV);
		dateMeetingLabel_TV = (TextView) findViewById(R.id.dateMeetingLabel_TV);
		endTimeLabel_TV = (TextView) findViewById(R.id.endTimeLabel_TV);
		/** Removed temporarily **/
		ownerLabel_TV = (TextView) findViewById(R.id.ownerLabel_TV);
		// organizerLabel_TV = (TextView) findViewById(R.id.organizerLabel_TV);

		account_TV = (TextView) findViewById(R.id.account_TV);
		interactionType_TV = (TextView) findViewById(R.id.interactionType_TV);
		dateMeeting_TV = (TextView) findViewById(R.id.dateMeeting_TV);
		endTime_TV = (TextView) findViewById(R.id.endTime_TV);
		/** Removed temporarily **/
		owner_TV = (TextView) findViewById(R.id.owner_TV);
		// organizer_TV = (TextView) findViewById(R.id.organizer_TV);

		purpose_ET = (EditText) findViewById(R.id.purpose_ET);
		nameClient_ET = (EditText) findViewById(R.id.nameClient_ET);
		designation_ET = (EditText) findViewById(R.id.designation_ET);
		detailsDiscussion_ET = (EditText) findViewById(R.id.detailsDiscussion_ET);
		// startTime_ET = (EditText) findViewById(R.id.startTime_ET);
		// endTime_ET = (EditText) findViewById(R.id.endTime_ET);

		/** Removed temporarily **/
		account_RL = (RelativeLayout) findViewById(R.id.account_RL);
		interactionType_RL = (RelativeLayout) findViewById(R.id.interactionType_RL);
		owner_RL = (RelativeLayout) findViewById(R.id.owner_RL);
		// organizer_RL = (RelativeLayout) findViewById(R.id.organizer_RL);
	}

	private void setTypeface() {
		purposeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		accountLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		nameClientLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		interactionTypeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		designationLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		detailsDiscussionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		dateMeetingLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		endTimeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		ownerLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// organizerLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		account_TV.setTypeface(myApp.getTypefaceRegularSans());
		interactionType_TV.setTypeface(myApp.getTypefaceRegularSans());
		dateMeeting_TV.setTypeface(myApp.getTypefaceRegularSans());
		endTime_TV.setTypeface(myApp.getTypefaceRegularSans());
		owner_TV.setTypeface(myApp.getTypefaceRegularSans());
		// organizer_TV.setTypeface(myApp.getTypefaceRegularSans());

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

		// System.out.println(new ArrayList<String>(userMap.keySet()).get(2));

		/** if(edit_mode) **/
		if (previousIntent.hasExtra("position")) {
			tempAppointment = myApp.getAppointmentList().get(
					previousIntent.getIntExtra("position", 0));

			purpose_ET.setText(tempAppointment.getPurposeOfMeeting());

			Account tempAccount = myApp.getAccountById(myApp
					.getStringIdFromStringJSON(tempAppointment.getAccount()));
			if (tempAccount != null) {
				selectedAccount = myApp
						.getAccountIndexFromAccountId(tempAccount
								.getAccountId());
				account_TV.setText(tempAccount.getName());
			} else {
				Toast.makeText(this, "Account not found", Toast.LENGTH_LONG)
						.show();
			}

			nameClient_ET.setText(tempAppointment.getNameOfTheClientOfficial());
			designation_ET.setText(tempAppointment
					.getDesignationOfClientOfficial());

			Integer temp = myApp.getIntValueFromStringJSON(tempAppointment
					.getTypeOfMeeting());
			if (temp != null) {
				interactionType_TV.setText(myApp.getInteractionTypeMap().get(
						Integer.toString(temp.intValue())));
				selectedInteraction = searchEngine
						.getIndexFromKeyInteractionMap(Integer.toString(temp
								.intValue()));
			}

			detailsDiscussion_ET.setText(tempAppointment.getDescription());

			dateMeeting_TV.setText(dateFormatter
					.formatDateToString(tempAppointment.getStartTime()));
			endTime_TV.setText(dateFormatter.formatDateToString(tempAppointment
					.getEndTime()));

			/** Removed temporarily **/
			owner_TV.setText(myApp.getStringNameFromStringJSON(tempAppointment
					.getOwner()));
			selectedOwner = searchEngine.getIndexFromKeyUserMap(myApp
					.getStringIdFromStringJSON(tempAppointment.getOwner()));

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

		if ((previousIntent.hasExtra("is_edit_mode") && previousIntent
				.getBooleanExtra("is_edit_mode", false))) {
			initView("Modify Appointment", "Save");
		} else {
			initView("Add Appointment", "Save");
		}

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
		} else if (selectedAccount < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select an account.", false);
			notErrorCase = false;
		} else if (nameClient_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter the Name of Client Official.", false);
			notErrorCase = false;
		} else if (designation_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter the designation.", false);
			notErrorCase = false;
		} else if (selectedInteraction < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select an interaction type.", false);
			notErrorCase = false;
		} else if (detailsDiscussion_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter some details for the meeting.", false);
			notErrorCase = false;
		} else if (dateMeeting_TV.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a date for meeting.", false);
			notErrorCase = false;
		}
		// else if (endTime_TV.getText().toString().trim().length() < 1) {
		// alertDialogBuilder = createDialog.createAlertDialog(null,
		// "Please select an end time.", false);
		// notErrorCase = false;
		// }
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

		System.out.println("check  :  "
				+ dateFormatter.formatStringToDate3(dateMeeting_TV.getText()
						.toString()));

		JSONObject params = new JSONObject();

		try {
			params.put("Subject",
					MyApp.encryptData(purpose_ET.getText().toString()))
					.put("accId",
							accountList.get(selectedAccount).getAccountId())
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
					.put("startdate",
							dateFormatter.formatDateToString4(dateFormatter
									.formatStringToDate3(dateMeeting_TV
											.getText().toString())));
			// .put("ownid", myApp.getLoginUserId());

			if (endTime_TV.getText().toString().trim().length() > 1) {
				params.put("enddate", dateFormatter
						.formatDateToString4(dateFormatter
								.formatStringToDate3(endTime_TV.getText()
										.toString())));
			}
			if (selectedOwner > -1) {
				params.put("ownid", new ArrayList<String>(userMap.keySet())
						.get(selectedOwner));
			}
			// if (selectedOrganizer > -1) {
			// params.put("organizer", new ArrayList<String>(userMap.keySet())
			// .get(selectedOrganizer));
			// }

			if (previousIntent.hasExtra("is_edit_mode")
					&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
				params.put("apponid", tempAppointment.getId());
			}

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		String url;
		if (previousIntent.hasExtra("is_edit_mode")
				&& previousIntent.getBooleanExtra("is_edit_mode", false)) {
			/** Update Mode **/
			url = Constant.URL + Constant.APPOINTMENTS_UPDATE;
		} else {
			url = Constant.URL + Constant.APPOINTMENTS_ADD;
		}

		params = MyApp.addParamToJson(params);

		progressDialog.show();

		try {
			System.out.println("URL : " + url);

			System.out.println("params : " + params);

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
							progressDialog.dismiss();

							AlertDialog alertDialog = myApp.handleError(
									createDialog, error,
									"Error while creating appointment.");
							alertDialog.show();

							// System.out.println("ERROR  : " +
							// error.getMessage());
							// error.printStackTrace();
							//
							// if (error instanceof NetworkError) {
							// System.out.println("NetworkError");
							// }
							// if (error instanceof NoConnectionError) {
							// System.out
							// .println("NoConnectionError you are now offline.");
							// }
							// if (error instanceof ServerError) {
							// System.out.println("ServerError");
							// Toast.makeText(AppointmentAddActivity.this,
							// "Permissions error.", Toast.LENGTH_LONG)
							// .show();
							// }
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
		case R.id.account_RL:
			startActivityForResult(nextIntent, Constant.SEARCH_ACCOUNT);
			break;
		case R.id.owner_RL:
			nextIntent.putExtra("user_value", 0);
			startActivityForResult(nextIntent, Constant.SEARCH_USER);
			break;
		// case R.id.organizer_RL:
		// nextIntent.putExtra("user_value", 1);
		// startActivityForResult(nextIntent, MyApp.SEARCH_USER);
		// break;
		default:
			break;
		}

	}

	private void getDateTimeString(View view, String x, boolean isDate) {
		switch (view.getId()) {
		case R.id.dateMeeting_RL:
			// System.out.println("check");
			if (isDate) {
				dateMeeting_TV.setText(x);
			} else {
				dateMeeting_TV.setText(dateMeeting_TV.getText().toString()
						+ ", " + x);
			}
			break;
		case R.id.endTime_RL:
			if (isDate) {
				endTime_TV.setText(x);
			} else {
				endTime_TV.setText(endTime_TV.getText().toString() + ", " + x);
			}
			break;

		default:
			break;
		}
	}

	int noOfTimesDateCalled = 0;
	int noOfTimesTimeCalled = 0;

	public void onPickDate(final View view) {
		final TimePickerDialog tPicker = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view2, int hourOfDay,
							int minute) {
						if (noOfTimesTimeCalled % 2 == 0) {
							// System.out.println(hourOfDay + ":" + minute);

							String timeString = "";

							if (hourOfDay < 10) {
								timeString = "0" + hourOfDay;
							} else {
								timeString = "" + hourOfDay;
							}
							if (minute < 10) {
								timeString = timeString + ":0" + minute;
							} else {
								timeString = timeString + ":" + minute;
							}

							getDateTimeString(view, timeString, false);
						}
						noOfTimesTimeCalled++;
					}
				}, 12, 00, true);
		tPicker.setCancelable(false);

		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dPicker = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {
					/**
					 * http://stackoverflow.com/questions/19836210/method-called
					 * -twice-in-datepicker
					 **/
					@Override
					public void onDateSet(DatePicker view2, int year,
							int monthOfYear, int dayOfMonth) {
						if (noOfTimesDateCalled % 2 == 0) {
							// System.out.println(dayOfMonth + "-"
							// + (monthOfYear + 1) + "-" + year);

							String dateString = "";
							if (dayOfMonth < 10) {
								dateString = "0" + dayOfMonth;
							} else {
								dateString = "" + dayOfMonth;
							}

							if (monthOfYear < 10) {
								dateString = dateString + "-0"
										+ (monthOfYear + 1);
							} else {
								dateString = dateString + "-"
										+ (monthOfYear + 1);
							}
							dateString = dateString + "-" + year;

							getDateTimeString(view, dateString, true);
							tPicker.show();
						}
						noOfTimesDateCalled++;
					}
				}, mYear, mMonth, mDay);
		dPicker.setCancelable(false);
		dPicker.show();
	}

	@Override
	public void onBack(View view) {
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
		setResult(RESULT_OK, intent);
		super.onBack(view);
	}

	@Override
	public void onBackPressed() {
		Intent intent = myApp.getIntenWithPreviousSearch(previousIntent);
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			int positionItem = 0;
			if (data != null) {
				positionItem = data.getIntExtra("position_item", 0);
			}
			if (requestCode == Constant.SEARCH_USER) {
				List<String> list = new ArrayList<String>(userMap.values());
				String text = list.get(positionItem);
				switch (data.getIntExtra("user_value", -1)) {
				case 0:
					owner_TV.setText(text);
					selectedOwner = positionItem;
					break;
				// case 1:
				// organizer_TV.setText(text);
				// selectedOrganizer = positionItem;
				// break;
				default:
					break;
				}
			}
			if (requestCode == Constant.SEARCH_ACCOUNT) {
				account_TV.setText(accountList.get(positionItem).getName());
				selectedAccount = positionItem;
			}

			if (requestCode == Constant.DETAILS_APPOINTMENT) {
//				Intent intent = new Intent();
//				intent.putExtra("refresh_list", true);
//				if (previousIntent.hasExtra("search_text")) {
//					intent.putExtra("search_text",
//							previousIntent.getStringExtra("search_text"));
//				}
				Intent intent = new MyApp().getIntenWithPreviousSearch(previousIntent);
				intent.putExtra("refresh_list", true);
				
				setResult(RESULT_OK, intent);
				finish();
			}

		}

	}
}
