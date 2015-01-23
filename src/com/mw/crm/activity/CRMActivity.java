package com.mw.crm.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.mw.crm.extra.MyApp;

public class CRMActivity extends Activity {

	TextView headerTitleTV, rightButtonTV;
	MyApp myApp ;
	
	public void findThings() {
		headerTitleTV = (TextView) findViewById(R.id.title_header_TV);
		rightButtonTV= (TextView) findViewById(R.id.right_button_TV);
		
	}
	
	private void setTypeface() {
		headerTitleTV.setTypeface(myApp.getTypefaceBoldSans());
		rightButtonTV.setTypeface(myApp.getTypefaceBoldSans());
	}

	
	public void initView(String title, String title2) {
		setTypeface();
		headerTitleTV.setText(title);
		rightButtonTV.setText(title2);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//		ActionBar actionBar = getActionBar();
//		hackJBPolicy();
//		LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//
//		View mCustomView = inflator.inflate(R.layout.header, null);
//		
//		actionBar.setCustomView(mCustomView);
//		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		initThings();
	}

	private void initThings() {
		myApp= (MyApp) getApplicationContext();
		
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
	
	
}
