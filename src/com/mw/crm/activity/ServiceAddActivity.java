package com.mw.crm.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.MyApp;

public class ServiceAddActivity extends CRMActivity {

	public static boolean isActivityVisible = false;

	MyApp myApp;

	Intent previousIntent, nextIntent;

	RequestQueue queue;

	TextView queryByLabel_TV, supportTypeLabel_TV, descriptionLabel_TV;
	// nameLabel_TV, priorityLevelLabel_TV

	TextView queryBy_TV, supportType_TV;
	// , priorityLevel_TV

	EditText description_ET;
	// name_ET,

	RelativeLayout supportType_RL;
	// , priorityLevel_RL

	int selectedQueryBy = -1, selectedSupportType = -1;
	// selectedPriorityLevel = -1;

	Map<String, String> priorityLevelMap;
	Map<String, String> supportTypeMap;
	Map<String, String> userMap;

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	private void initThings() {
		myApp = (MyApp) getApplicationContext();
		previousIntent = getIntent();

		priorityLevelMap = myApp.getPriorityLevelMap();
		supportTypeMap = myApp.getSupportTypeMap();
		userMap = myApp.getUserMap();

		if (previousIntent.hasExtra("position")) {
		}

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("Saving Changes",
				"This may take some time", true, null);

		queue = Volley.newRequestQueue(this);
	}

	public void findThings() {
		super.findThings();

		queryByLabel_TV = (TextView) findViewById(R.id.queryByLabel_TV);
		supportTypeLabel_TV = (TextView) findViewById(R.id.supportTypeLabel_TV);
		// nameLabel_TV = (TextView) findViewById(R.id.nameLabel_TV);
		descriptionLabel_TV = (TextView) findViewById(R.id.descriptionLabel_TV);
		// priorityLevelLabel_TV = (TextView)
		// findViewById(R.id.priorityLevelLabel_TV);

		queryBy_TV = (TextView) findViewById(R.id.queryBy_TV);
		supportType_TV = (TextView) findViewById(R.id.supportType_TV);
		// priorityLevel_TV = (TextView) findViewById(R.id.priorityLevel_TV);

		// name_ET = (EditText) findViewById(R.id.name_ET);
		description_ET = (EditText) findViewById(R.id.description_ET);

		supportType_RL = (RelativeLayout) findViewById(R.id.supportType_RL);
		// priorityLevel_RL = (RelativeLayout)
		// findViewById(R.id.priorityLevel_RL);
	}

	private void setTypeface() {
		queryByLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		supportTypeLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// nameLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		descriptionLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		// priorityLevelLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		queryBy_TV.setTypeface(myApp.getTypefaceRegularSans());
		// name_ET.setTypeface(myApp.getTypefaceRegularSans());
		description_ET.setTypeface(myApp.getTypefaceRegularSans());
		// priorityLevel_TV.setTypeface(myApp.getTypefaceRegularSans());

		supportType_TV.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView(String title, String title2) {
		super.initView(title, title2);
		setTypeface();

		// selectedQueryBy
		int i = myApp.getIndexFromKeyUserMap(myApp.getLoginUserId());
		System.out.println("key  : "
				+ new ArrayList<String>(myApp.getUserMap().keySet()).get(i)
				+ "value  : "
				+ new ArrayList<String>(myApp.getUserMap().values()).get(i));
		queryBy_TV.setText(new ArrayList<String>(myApp.getUserMap().values())
				.get(i));
	}

	@SuppressLint("ClickableViewAccessibility")
	private void hideKeyboardFunctionality() {
		((RelativeLayout) findViewById(R.id.activity_account_add_RL))
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
		setContentView(R.layout.activity_service_add);

		initThings();
		findThings();
		initView("Add Service Connect", "Save");

		hideKeyboardFunctionality();

		registerForContextMenu(supportType_RL);
		// registerForContextMenu(priorityLevel_RL);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);

		if (v.getId() == R.id.supportType_RL) {
			List<String> list = new ArrayList<String>(supportTypeMap.values());
			for (int i = 0; i < list.size(); i++) {
				menu.add(0, v.getId(), i, list.get(i));
			}
		}
		// if (v.getId() == R.id.priorityLevel_RL) {
		// List<String> list = new ArrayList<String>(priorityLevelMap.values());
		// for (int i = 0; i < list.size(); i++) {
		// menu.add(1, v.getId(), i, list.get(i));
		// }
		// }

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {

		if (item.getGroupId() == 0) {
			selectedSupportType = item.getOrder();
			supportType_TV.setText(item.getTitle());
		}
		// else if (item.getGroupId() == 1) {
		// selectedPriorityLevel = item.getOrder();
		// priorityLevel_TV.setText(item.getTitle());
		// }
		return super.onContextItemSelected(item);
	}

	public void onOpenContextMenu(View view) {
		hideKeyboard(this.getCurrentFocus());
		openContextMenu(view);
	}

	private boolean validate() {
		boolean notErrorCase = true;
		if (selectedQueryBy < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select an owner.", false);
			notErrorCase = false;
		} else if (selectedSupportType < 0) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please select a Support Type.", false);
			notErrorCase = false;
		}
		// else if (name_ET.getText().toString().trim().length() < 1) {
		// alertDialogBuilder = createDialog.createAlertDialog(null,
		// "Please enter some Name.", false);
		// notErrorCase = false;
		// }
		else if (description_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter some description.", false);
			notErrorCase = false;
		}
		// else if (selectedPriorityLevel < 0) {
		// alertDialogBuilder = createDialog.createAlertDialog(null,
		// "Please select a Priority Level.", false);
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

		System.out.println("owner :  " + selectedQueryBy + "\n  support :  "
				+ selectedSupportType);

		System.out.println("\n\n"
				+ new ArrayList<String>(userMap.keySet()).size());

		JSONObject params = new JSONObject();
		try {
			params.put("oId", new ArrayList<String>(userMap.keySet())
					.get(selectedQueryBy));
			params.put("support",
					new ArrayList<String>(supportTypeMap.keySet())
							.get(selectedSupportType));
			// params.put("name",
			// MyApp.encryptData(name_ET.getText().toString()));
			params.put("description",
					MyApp.encryptData(description_ET.getText().toString()));
			// params.put("priority",
			// new ArrayList<String>(priorityLevelMap.keySet())
			// .get(selectedPriorityLevel));

		} catch (JSONException e1) {
			e1.printStackTrace();
		}
		params = MyApp.addParamToJson(params);

		String url = MyApp.URL + MyApp.SERVICE_ADD;
		System.out.println("json" + params);
		progressDialog.show();
		try {
			System.out.println("URL : " + url);

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
							
							AlertDialog alertDialog = myApp.handleError(createDialog,error,"Error while placing a request.");
							alertDialog.show();
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

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		intent.putExtra("request_code", requestCode);
		super.startActivityForResult(intent, requestCode);
	}

	public void onSearchItem(View view) {
		nextIntent = new Intent(this, SearchActivity.class);

		switch (view.getId()) {
		case R.id.queryBy_RL:
			startActivityForResult(nextIntent, MyApp.SEARCH_USER);
			break;

		default:
			break;
		}

	}

	private void onPositiveResponse() {
		progressDialog.dismiss();

		alertDialogBuilder = createDialog.createAlertDialog(null,
				"Comment posted successfully.", false);
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
						nextIntent = new Intent(ServiceAddActivity.this,
								MenuActivity2.class);
						nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
								| Intent.FLAG_ACTIVITY_CLEAR_TASK);
						startActivity(nextIntent);
					}
				});
		alertDialog = alertDialogBuilder.create();
		alertDialog.show();
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
				List<String> list = new ArrayList<String>(userMap.values());
				String text = list.get(positionItem);
				queryBy_TV.setText(text);
				selectedQueryBy = positionItem;
			}
		}
	}

	// @Override
	// protected void onResume() {
	// super.onResume();
	// isActivityVisible = true;
	//
	// }
	//
	// @Override
	// protected void onPause() {
	// isActivityVisible = false;
	// super.onPause();
	// }
}
