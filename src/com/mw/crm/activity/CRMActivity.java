package com.mw.crm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.mw.crm.extra.MyApp;

public class CRMActivity extends Activity {

	TextView headerTitleTV, rightButtonTV;
	MyApp myApp;

	public void findThings() {
		headerTitleTV = (TextView) findViewById(R.id.title_header_TV);
		rightButtonTV = (TextView) findViewById(R.id.right_button_TV);

	}

	private void setTypeface() {
		headerTitleTV.setTypeface(myApp.getTypefaceBoldSans());
		rightButtonTV.setTypeface(myApp.getTypefaceBoldSans());
	}

	public void initView(String title, String title2) {
		setTypeface();
		headerTitleTV.setText(title);
		if (title2 != null) {
			rightButtonTV.setText(title2);
		} else {
			rightButtonTV.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		initThings();
	}

	private void initThings() {
		myApp = (MyApp) getApplicationContext();

	}

	public void onBack(View view) {
		finish();
	}

	public void onRightButton(View view) {

	}

	public TextView getRightButtonTV() {
		return rightButtonTV;
	}

	public void setRightButtonTV(TextView rightButtonTV) {
		this.rightButtonTV = rightButtonTV;
	}

	public void onHome(View view) {
		Intent intent = new Intent(this, MenuActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
				| Intent.FLAG_ACTIVITY_CLEAR_TASK);
		startActivity(intent);
	}
}
