package com.mw.crm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AppointmentDetailsActivity extends Activity {

	Intent nextIntent, previousIntent;
	
	
	private void initThings() {
		previousIntent = getIntent();
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initThings();
//		findThings();
//		initView("Accounts", "Add");
	}

	public void onEdit(View view)
	{
		nextIntent = new Intent(this, AppointmentAddActivity.class);
		nextIntent.putExtra("position", previousIntent.getIntExtra("position", 0));
		nextIntent.putExtra("is_edit_mode", true);
		startActivity(nextIntent);
	}
	
}
