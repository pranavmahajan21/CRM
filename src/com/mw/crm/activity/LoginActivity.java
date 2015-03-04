package com.mw.crm.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.gson.Gson;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.MyApp;

public class LoginActivity extends Activity {
	MyApp myApp;

	TextView loginLabel_TV, forgotLabel_TV;
	EditText email_ET, password_ET;
	Button login_B;

	Intent nextIntent;

	CreateDialog createDialog;
	ProgressDialog progressDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;

	RequestQueue queue;

	Gson gson;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	private void initThings() {

		myApp = (MyApp) getApplicationContext();

		createDialog = new CreateDialog(this);
		progressDialog = createDialog.createProgressDialog("LogIn",
				"Verifying username-password.", true, null);

		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();

		queue = Volley.newRequestQueue(this);

		nextIntent = new Intent(this, MenuActivity2.class);
		nextIntent.putExtra("is_first_time_login", true);
		nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
	}

	public void findThings() {
		loginLabel_TV = (TextView) findViewById(R.id.loginLabel_TV);
		forgotLabel_TV = (TextView) findViewById(R.id.forgotLabel_TV);

		email_ET = (EditText) findViewById(R.id.email_ET);
		password_ET = (EditText) findViewById(R.id.password_ET);

		login_B = (Button) findViewById(R.id.login_B);
	}

	private void setTypeface() {
		loginLabel_TV.setTypeface(myApp.getTypefaceBoldSans());
		forgotLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		email_ET.setTypeface(myApp.getTypefaceRegularSans());
		password_ET.setTypeface(myApp.getTypefaceRegularSans());
		login_B.setTypeface(myApp.getTypefaceRegularSans());
	}

	public void initView() {
		setTypeface();
		email_ET.setText("in-fmcrmad1");
		password_ET.setText("Password01");
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);

		initThings();
		findThings();
		initView();
	}

	private boolean validate() {
		boolean notErrorCase = true;
		if (email_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter some username.", false);
			notErrorCase = false;
		} else if (password_ET.getText().toString().trim().length() < 1) {
			alertDialogBuilder = createDialog.createAlertDialog(null,
					"Please enter some password.", false);
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

	public void onLogin(View view) {
		if (!validate()) {
			return;
		}

		JSONObject params = new JSONObject();

		try {
			params.put("username",
					MyApp.encryptData(email_ET.getText().toString())).put(
					"password",
					MyApp.encryptData(password_ET.getText().toString()));
		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		String url = MyApp.URL + MyApp.LOGIN;

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
							if (response.has("id")) {
								onPositiveResponse(response);
							}
						}

					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							progressDialog.hide();
							System.out.println("ERROR  : " + error.getMessage());
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

	private void onPositiveResponse(JSONObject response) {
		progressDialog.dismiss();

		editor.putBoolean("is_user_login", true);
		editor.commit();
		try {
			myApp.setLoginUserId(response.getString("id"));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		startActivity(nextIntent);
	}
}
