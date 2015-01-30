package com.mw.crm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.RequestQueue;
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

		nextIntent = new Intent(this, MenuActivity.class);
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

	public void onLogin(View view) {
		if (email_ET.getText().toString().equalsIgnoreCase("in-fmcrmad1")
				&& password_ET.getText().toString().equals("Password01")) {
			progressDialog.dismiss();

			editor.putBoolean("is_user_login", true);
			editor.commit();
			// TODO put user information in preferences

			startActivity(nextIntent);
		}

	}

}
