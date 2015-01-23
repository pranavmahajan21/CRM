package com.mw.crm.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.crm.activity.R;

public class MainActivity extends Activity {

	Intent nextIntent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void onOpportunity(View view)
	{
		nextIntent = new Intent(this, OpportunityListActivity.class);
		startActivity(nextIntent);
	}
	
	public void onOppoAdd(View view)
	{
		nextIntent = new Intent(this, OpportunityAddActivity.class);
		startActivity(nextIntent);
	}
	
	public void onContactList(View view)
	{
		nextIntent = new Intent(this, ContactListActivity.class);
		startActivity(nextIntent);
	}
	
	public void onContactAdd(View view)
	{
		nextIntent = new Intent(this, ContactAddActivity.class);
		startActivity(nextIntent);
	}
	
	public void onMenu(View view)
	{
		nextIntent = new Intent(this, MenuActivity.class);
		startActivity(nextIntent);
	}

	public void onAppointment(View view)
	{
		nextIntent = new Intent(this, AppointmentListActivity.class);
		startActivity(nextIntent);
	}
	
	public void onAppointmentAdd(View view)
	{
		nextIntent = new Intent(this, AppointmentAddActivity.class);
		startActivity(nextIntent);
	}
	
	public void onAccountList(View view)
	{
		nextIntent = new Intent(this, AccountListActivity.class);
		startActivity(nextIntent);
	}
	
	public void onAccountAdd(View view)
	{
		nextIntent = new Intent(this, AccountAddActivity.class);
		startActivity(nextIntent);
	}
	
}
