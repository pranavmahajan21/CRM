package com.mw.crm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.application.MyApp;
import com.mw.crm.model.Contact;

public class ContactDetailsActivity extends CRMActivity {

	Intent nextIntent, previousIntent;
	Contact selectedContact;

	TextView nameLabel_TV, designationLabel_TV, organizationLabel_TV,
			emailLabel_TV, officePhoneLabel_TV, mobileLabel_TV,
			internalConnectLabel_TV, dorLabel_TV;

	TextView name_TV, designation_TV, organization_TV, email_TV,
			officePhone_TV, mobile_TV, internalConnect_TV, dor_TV;

	private void initThings() {
		previousIntent = getIntent();
		if (previousIntent.hasExtra("contact_dummy")) {
			selectedContact = new Gson().fromJson(
					previousIntent.getStringExtra("contact_dummy"),
					Contact.class);
		} else {
			selectedContact = myApp.getContactList().get(
					previousIntent.getIntExtra("position", 0));
		}
	}

	public void findThings() {
		super.findThings();
		nameLabel_TV = (TextView) findViewById(R.id.nameLabel_TV);
		designationLabel_TV = (TextView) findViewById(R.id.designationLabel_TV);
		organizationLabel_TV = (TextView) findViewById(R.id.organizationLabel_TV);
		emailLabel_TV = (TextView) findViewById(R.id.emailLabel_TV);
		officePhoneLabel_TV = (TextView) findViewById(R.id.officePhoneLabel_TV);
		mobileLabel_TV = (TextView) findViewById(R.id.mobileLabel_TV);
		internalConnectLabel_TV = (TextView) findViewById(R.id.internalConnectLabel_TV);
		dorLabel_TV = (TextView) findViewById(R.id.dorLabel_TV);

		name_TV = (TextView) findViewById(R.id.name_TV);
		designation_TV = (TextView) findViewById(R.id.designation_TV);
		organization_TV = (TextView) findViewById(R.id.organization_TV);
		email_TV = (TextView) findViewById(R.id.email_TV);
		officePhone_TV = (TextView) findViewById(R.id.officePhone_TV);
		mobile_TV = (TextView) findViewById(R.id.mobile_TV);
		internalConnect_TV = (TextView) findViewById(R.id.internalConnect_TV);
		dor_TV = (TextView) findViewById(R.id.dor_TV);
	}

	private void setTypeface() {

		nameLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		designationLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		organizationLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		emailLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		officePhoneLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		mobileLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		internalConnectLabel_TV.setTypeface(myApp.getTypefaceRegularSans());
		dorLabel_TV.setTypeface(myApp.getTypefaceRegularSans());

		name_TV.setTypeface(myApp.getTypefaceRegularSans());
		designation_TV.setTypeface(myApp.getTypefaceRegularSans());
		organization_TV.setTypeface(myApp.getTypefaceRegularSans());
		email_TV.setTypeface(myApp.getTypefaceRegularSans());
		officePhone_TV.setTypeface(myApp.getTypefaceRegularSans());
		mobile_TV.setTypeface(myApp.getTypefaceRegularSans());
		internalConnect_TV.setTypeface(myApp.getTypefaceRegularSans());
		dor_TV.setTypeface(myApp.getTypefaceRegularSans());
	}

	private void initWithoutMappingFields() {
		name_TV.setText(myApp.getContactName(selectedContact));
		designation_TV.setText(selectedContact.getDesignation());
		email_TV.setText(selectedContact.getEmail());
		officePhone_TV.setText(selectedContact.getTelephone());
		mobile_TV.setText(selectedContact.getMobilePhone());
	}

	public void initView(String string, String string2) {
		super.initView(string, string2);
		setTypeface();
		if (previousIntent.hasExtra("contact_dummy") && selectedContact != null) {
			initWithoutMappingFields();

			organization_TV.setText(selectedContact.getOrganization());
			internalConnect_TV.setText(selectedContact.getInternalConnect());
			dor_TV.setText(selectedContact.getDegreeOfRelation());
		} else {
			initWithoutMappingFields();

			organization_TV.setText(myApp
					.getStringNameFromStringJSON(selectedContact
							.getOrganization()));

			internalConnect_TV.setText(myApp
					.getStringNameFromStringJSON(selectedContact
							.getInternalConnect()));

			Integer temp = myApp.getIntValueFromStringJSON(selectedContact
					.getDegreeOfRelation());

			if (temp != null) {
				dor_TV.setText(myApp.getDorMap().get(
						Integer.toString(temp.intValue())));
			}

		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_contact_detail);

		initThings();
		findThings();
		if (previousIntent.hasExtra("contact_dummy")) {
			if (previousIntent.hasExtra("contact_created")
					&& previousIntent.getBooleanExtra("contact_created", false)) {
				initView("Created Contact", null);
			} else {
				initView("Updated Contact", null);
			}
		} else {
			initView("Contact", "Edit");
		}
	}

	public void onRightButton(View view) {
		nextIntent = new Intent(this, ContactAddActivity.class);
		nextIntent.putExtra("position",
				previousIntent.getIntExtra("position", 0));
		nextIntent.putExtra("is_edit_mode", true);
		startActivityForResult(nextIntent, MyApp.NOTHING_ELSE_MATTERS);
	}

	@Override
	public void onBack(View view) {
		/**
		 * We have to setResult(RESULT_OK); here because in case of contact
		 * created successfully scenario we want to go back to the Add page &
		 * handle the result.
		 **/
		Intent intent = new Intent();
		if (previousIntent.hasExtra("search_text")) {
			intent.putExtra("search_text",
					previousIntent.getStringExtra("search_text"));
		}
		setResult(RESULT_OK, intent);
		super.onBack(view);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent();
		if (previousIntent.hasExtra("search_text")) {
			intent.putExtra("search_text",
					previousIntent.getStringExtra("search_text"));
		}
		setResult(RESULT_OK, intent);
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent dataIntent) {
		super.onActivityResult(requestCode, resultCode, dataIntent);
		if (resultCode == RESULT_OK) {
			if (dataIntent != null) {
				if (previousIntent.hasExtra("search_text")) {
					dataIntent.putExtra("search_text",
							previousIntent.getStringExtra("search_text"));
				}
				if (dataIntent.hasExtra("refresh_list")) {
					setResult(RESULT_OK, dataIntent);
				}
			}
			finish();
		}
	}

}
