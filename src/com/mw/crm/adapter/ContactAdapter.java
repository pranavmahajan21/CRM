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
	
	List<String> organizationList;

	public ContactAdapter(Context context, List<Contact> contactList) {
		super();
		this.context = context;
		this.contactList = contactList;
		this.tempContactList = new ArrayList<Contact>();
		tempContactList.addAll(contactList);
		
		organizationList = new ArrayList<String>();
		for (int i = 0; i < this.contactList.size(); i++) {
			try {
				organizationList.add(getNameFromJson(new JSONObject(
						this.contactList.get(i).getOrganization())));
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		myApp = (MyApp) context.getApplicationContext();
	}

	public void swapData(List<Contact> contactList) {
		this.contactList = contactList;
		tempContactList.addAll(contactList);
	}

	static class ViewHolder {
		protected TextView nameTV;
		protected TextView companyTV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.element_common, parent,
					false);

			viewHolder.nameTV = (TextView) convertView
					.findViewById(R.id.name_TV);
			viewHolder.companyTV = (TextView) convertView
					.findViewById(R.id.company_TV);

			viewHolder.nameTV.setTypeface(myApp.getTypefaceBoldSans());
			viewHolder.companyTV.setTypeface(myApp.getTypefaceRegularSans());

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Contact tempContact = contactList.get(position);

		String temp = myApp.getContactName(tempContact);
		viewHolder.nameTV.setText(temp);

		try {
			viewHolder.companyTV.setText(getNameFromJson(new JSONObject(
					tempContact.getOrganization())));
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return convertView;
	}

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
				if (tempContact.getLastName().toLowerCase(Locale.getDefault())
						.contains(charText)
//						|| tempContact.getFirstName()
//								.toLowerCase(Locale.getDefault())
//								.contains(charText)
						|| tempContact.getOrganization()
								.toLowerCase(Locale.getDefault())
								.contains(charText)) {
					contactList.add(tempContact);
				}
			}
		}
		notifyDataSetChanged();
	}
}