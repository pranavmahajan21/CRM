package com.mw.crm.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
		protected TextView nameTV;
		protected TextView countryTV;
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
			viewHolder.countryTV = (TextView) convertView
					.findViewById(R.id.company_TV);

			viewHolder.nameTV.setTypeface(myApp.getTypefaceBoldSans());
			viewHolder.countryTV.setTypeface(myApp.getTypefaceRegularSans());

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Account tempAccount = accountList.get(position);
		viewHolder.nameTV.setText(tempAccount.getName());
		Integer temp = myApp
				.getIntValueFromStringJSON(tempAccount.getCountry());

		if (temp != null) {
			// headquarterCountry_TV.setText(myApp.getCountryMap().get(
			// Integer.toString(temp.intValue())));
			viewHolder.countryTV.setText(myApp.getCountryMap().get(
					Integer.toString(temp.intValue())));
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