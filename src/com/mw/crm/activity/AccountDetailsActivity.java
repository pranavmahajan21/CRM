package com.mw.crm.activity;

import com.example.crm.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class AccountDetailsActivity extends Activity {

	Intent nextIntent, previousIntent;
	
	
	private void initThings() {
		previousIntent = getIntent();
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initThings();
		setContentView(R.layout.activity_appointment_detail);
//		findThings();
//		initView("Accounts", "Add");
	}

	public void onEdit(View view)
	{
		nextIntent = new Intent(this, AccountAddActivity.class);
		nextIntent.putExtra("position", previousIntent.getIntExtra("position", 0));
		nextIntent.putExtra("is_edit_mode", true);
		startActivity(nextIntent);
	}
	
}
