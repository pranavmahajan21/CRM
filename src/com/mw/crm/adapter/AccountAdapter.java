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
import com.mw.crm.model.Account;

public class AccountAdapter extends BaseAdapter {

	MyApp myApp;
	Context context;

	LayoutInflater inflater;

	List<Account> accountList;
	List<Account> tempAccountList;

	public AccountAdapter(Context context, List<Account> accountList) {
		super();
		this.context = context;
		this.accountList = accountList;
		this.tempAccountList = new ArrayList<Account>();
		this.tempAccountList.addAll(accountList);
		myApp = (MyApp) context.getApplicationContext();
	}

	public void swapData(List<Account> accountList) {
		this.accountList = accountList;

		/**
		 * This list must be refreshed as it used for search functionality. Also
		 * we must re-initialize so as to clear the existing list.
		 **/
		this.tempAccountList = new ArrayList<Account>();
		this.tempAccountList.addAll(accountList);
	}

	static class ViewHolder {
		protected TextView nameClient_TV;
		protected TextView leadPartner_TV;
		protected TextView sector_TV;
		protected TextView country_TV;
		protected TextView opportunityLink_TV;
		protected TextView contactLink_TV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.element_account, parent,
					false);

			viewHolder.nameClient_TV = (TextView) convertView
					.findViewById(R.id.nameClient_TV);
			viewHolder.leadPartner_TV = (TextView) convertView
					.findViewById(R.id.leadPartner_TV);
			viewHolder.sector_TV = (TextView) convertView
					.findViewById(R.id.sector_TV);
			viewHolder.country_TV = (TextView) convertView
					.findViewById(R.id.country_TV);
			viewHolder.opportunityLink_TV = (TextView) convertView
					.findViewById(R.id.opportunityLink_TV);
			viewHolder.contactLink_TV = (TextView) convertView
					.findViewById(R.id.contactLink_TV);

			viewHolder.nameClient_TV.setTypeface(myApp.getTypefaceBoldSans());
			viewHolder.leadPartner_TV.setTypeface(myApp.getTypefaceRegularSans());
			viewHolder.sector_TV.setTypeface(myApp.getTypefaceRegularSans());
			viewHolder.country_TV.setTypeface(myApp.getTypefaceRegularSans());
			viewHolder.opportunityLink_TV.setTypeface(myApp.getTypefaceRegularSans());
			viewHolder.contactLink_TV.setTypeface(myApp.getTypefaceRegularSans());

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Account tempAccount = accountList.get(position);
		viewHolder.nameClient_TV.setText(tempAccount.getName());
		
		Integer temp = null;
		temp = myApp.getIntValueFromStringJSON(tempAccount.getSector());
		if (temp != null) {
			viewHolder.sector_TV.setText(myApp.getSectorMap().get(
					Integer.toString(temp.intValue())));
			temp = null;
		}
		
		temp = myApp
				.getIntValueFromStringJSON(tempAccount.getCountry());
		if (temp != null) {
			viewHolder.country_TV.setText(myApp.getCountryMap().get(
					Integer.toString(temp.intValue())));
		}

		try {
			viewHolder.leadPartner_TV.setText(new JSONObject(tempAccount
					.getLeadPartner()).getString("Name"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return convertView;
	}

	@Override
	public int getCount() {
		return accountList.size();
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
		accountList.clear();
		if (charText.length() == 0) {
			accountList.addAll(tempAccountList);
		} else {
			for (Account tempAccount : tempAccountList) {
				if (tempAccount.getName().toLowerCase(Locale.getDefault())
						.contains(charText)
						|| tempAccount.getCountry()
								.toLowerCase(Locale.getDefault())
								.contains(charText)) {
					accountList.add(tempAccount);
				}
			}
		}
		notifyDataSetChanged();
	}
}