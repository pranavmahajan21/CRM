package com.mw.crm.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.extra.CreateDialog;

public class LoginActivity extends Activity {

	EditText email_ET, password_ET;

	Intent nextIntent;

	CreateDialog createDialog;
	ProgressDialog progressDialog;

	RequestQueue queue;

	Gson gson;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	private void initThings() {

		// myApp = (MyApp) getApplicationContext();
		// gson = new Gson();

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
		email_ET = (EditText) findViewById(R.id.email_ET);
		password_ET = (EditText) findViewById(R.id.password_ET);
	}

	public void initView() {
		email_ET.setText("asdf");
		password_ET.setText("asdf");
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
		if (email_ET.getText().toString().equalsIgnoreCase("asdf")
				&& password_ET.getText().toString().equals("asdf")) {
			progressDialog.dismiss();

			editor.putBoolean("is_user_login", true);
			editor.commit();
			// TODO put user information in preferences

			startActivity(nextIntent);
		}

	}

}
