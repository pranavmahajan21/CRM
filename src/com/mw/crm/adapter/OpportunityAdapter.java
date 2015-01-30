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
import com.mw.crm.model.Opportunity;

public class OpportunityAdapter extends BaseAdapter {

	Context context;
	List<Opportunity> opportunityList;
	List<Opportunity> tempOpportunityList;

	MyApp myApp;

	LayoutInflater inflater;

	public OpportunityAdapter(Context context, List<Opportunity> opportunityList) {
		super();
		this.context = context;
		this.opportunityList = opportunityList;
		this.tempOpportunityList = new ArrayList<Opportunity>();
		this.tempOpportunityList.addAll(opportunityList);
		myApp = (MyApp) context.getApplicationContext();
	}

	 public void swapData(List<Opportunity> opportunityList) {
	 this.opportunityList = opportunityList;
	 this.tempOpportunityList.addAll(opportunityList);
	 }

	static class ViewHolder {
		protected TextView nameTV;
		protected TextView statusTV;
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
			viewHolder.statusTV = (TextView) convertView
					.findViewById(R.id.company_TV);

			viewHolder.nameTV.setTypeface(myApp.getTypefaceBoldSans());
			viewHolder.statusTV.setTypeface(myApp.getTypefaceRegularSans());

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Opportunity tempOpportunity = opportunityList.get(position);
		
		viewHolder.nameTV.setText(myApp
				.getStringNameFromStringJSON(tempOpportunity.getCustomerId()));
		viewHolder.statusTV.setText(tempOpportunity.getName());
		return convertView;
	}

	@Override
	public int getCount() {
		return opportunityList.size();
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
		opportunityList.clear();
		if (charText.length() == 0) {
			opportunityList.addAll(tempOpportunityList);
		} else {
			for (Opportunity tempOpportunity : tempOpportunityList) {
				if (tempOpportunity.getName().toLowerCase(Locale.getDefault())
						.contains(charText)
						|| tempOpportunity.getKpmgStatus()
								.toLowerCase(Locale.getDefault())
								.contains(charText)) {
					opportunityList.add(tempOpportunity);
				}
			}
		}
		notifyDataSetChanged();
	}
}