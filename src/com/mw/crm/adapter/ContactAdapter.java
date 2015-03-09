package com.mw.crm.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.Contact;

public class ContactAdapter extends BaseAdapter {

	MyApp myApp;
	Context context;

	LayoutInflater inflater;

	List<Contact> contactList;
	List<Contact> tempContactList;

	public ContactAdapter(Context context, List<Contact> contactList) {
		super();
		this.context = context;
		this.contactList = contactList;
		this.tempContactList = new ArrayList<Contact>();
		this.tempContactList.addAll(contactList);
		myApp = (MyApp) context.getApplicationContext();
	}

	public void swapData(List<Contact> contactList) {
		this.contactList = contactList;
		this.tempContactList = new ArrayList<Contact>();
		tempContactList.addAll(contactList);
	}

	static class ViewHolder {
		protected TextView nameTV;
		protected TextView designationTV;
		protected TextView organisationTV;
		protected TextView internalConnectTV;
		protected TextView dorTV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.element_contact, parent,
					false);

			viewHolder.nameTV = (TextView) convertView
					.findViewById(R.id.name_TV);
			viewHolder.designationTV = (TextView) convertView
					.findViewById(R.id.designation_TV);
			viewHolder.organisationTV = (TextView) convertView
					.findViewById(R.id.organisation_TV);
			viewHolder.internalConnectTV = (TextView) convertView
					.findViewById(R.id.internalConnect_TV);
			viewHolder.dorTV = (TextView) convertView.findViewById(R.id.dor_TV);

			viewHolder.nameTV.setTypeface(myApp.getTypefaceBoldSans());
			viewHolder.designationTV
					.setTypeface(myApp.getTypefaceRegularSans());
			viewHolder.organisationTV.setTypeface(myApp
					.getTypefaceRegularSans());
			viewHolder.internalConnectTV.setTypeface(myApp
					.getTypefaceRegularSans());
			viewHolder.dorTV.setTypeface(myApp.getTypefaceRegularSans());

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Contact tempContact = contactList.get(position);

		String tempName = myApp.getContactName(tempContact);
		viewHolder.nameTV.setText(tempName);
		viewHolder.designationTV.setText(tempContact.getDesignation());

		try {
			viewHolder.organisationTV.setText(getNameFromJson(new JSONObject(
					tempContact.getOrganization())));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		viewHolder.internalConnectTV.setText(myApp
				.getStringNameFromStringJSON(tempContact.getInternalConnect()));

		Integer temp = myApp.getIntValueFromStringJSON(tempContact
				.getDegreeOfRelation());
		if (temp != null) {
			viewHolder.dorTV.setText(myApp.getDorMap().get(
					Integer.toString(temp.intValue())));
		}
		return convertView;
	}

	// TODO: remove this
	private String getNameFromJson(JSONObject jsonObject) {
		String temp = "-";
		try {
			temp = jsonObject.getString("Name");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return temp;
	}

	@Override
	public int getCount() {
		return contactList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	public void filter(String charText) {
		charText = charText.toLowerCase(Locale.getDefault());
		contactList.clear();
		if (charText.length() == 0) {
			contactList.addAll(tempContactList);
		} else {
			for (Contact tempContact : tempContactList) {
				if ((tempContact.getLastName() != null && tempContact
						.getLastName().toLowerCase(Locale.getDefault())
						.contains(charText))
						|| (tempContact.getFirstName() != null && tempContact
								.getFirstName()
								.toLowerCase(Locale.getDefault())
								.contains(charText))
						|| (tempContact.getOrganization() != null && tempContact
								.getOrganization()
								.toLowerCase(Locale.getDefault())
								.contains(charText))) {
					contactList.add(tempContact);
				}
			}
		}
		notifyDataSetChanged();
	}
}