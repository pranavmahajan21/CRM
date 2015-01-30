package com.mw.crm.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Contact;

public class ContactDetailsActivity extends CRMActivity {

	Intent nextIntent, previousIntent;
	Contact selectedContact;

	TextView emailLabel_TV, officePhoneLabel_TV, mobileLabel_TV,
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
		// myApp.getTypefaceBoldSans()

		// emailLabel_TV.setTypeface();
		// officePhoneLabel_TV.setTypeface();
		// mobileLabel_TV.setTypeface();
		// internalConnectLabel_TV.setTypeface();
		// dorLabel_TV.setTypeface();
		//
		// name_TV.setTypeface();
		// designation_TV.setTypeface();
		// organization_TV.setTypeface();
		// email_TV.setTypeface();
		// officePhone_TV.setTypeface();
		// mobile_TV.setTypeface();
		// internalConnect_TV.setTypeface();
		// dor_TV.setTypeface();
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
		/** We have to setResult(RESULT_OK); here because in case of contact creatd successfully scenario **/
		setResult(RESULT_OK);
		super.onBack(view);
	}

	@Override
	public void onBackPressed() {
		setResult(RESULT_OK);
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (data != null) {
				setResult(RESULT_OK, data);
			}
			finish();
		}
	}

}
