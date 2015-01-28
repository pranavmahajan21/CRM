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

	// public void swapData(List<ParseObject> alertList) {
	// this.eventPO = alertList;
	// }

	static class ViewHolder {
		protected TextView nameTV;
//		protected TextView descriptionTV;
//		protected TextView primaryTV;
		protected TextView statusTV;
//		protected TextView costTV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
//			convertView = inflater.inflate(R.layout.element_opportunity,
//					parent, false);
			convertView = inflater.inflate(R.layout.element_common,
					parent, false);

			viewHolder.nameTV = (TextView) convertView
					.findViewById(R.id.name_TV);
			viewHolder.statusTV = (TextView) convertView
					.findViewById(R.id.company_TV);
//			viewHolder.descriptionTV = (TextView) convertView
//					.findViewById(R.id.description_TV);
//			viewHolder.primaryTV = (TextView) convertView
//					.findViewById(R.id.primary_TV);
//			viewHolder.costTV = (TextView) convertView
//					.findViewById(R.id.cost_TV);

			viewHolder.nameTV.setTypeface(myApp.getTypefaceBoldSans());
			viewHolder.statusTV.setTypeface(myApp.getTypefaceRegularSans());
//			viewHolder.descriptionTV
//					.setTypeface(myApp.getTypefaceRegularSans());
//			viewHolder.primaryTV.setTypeface(myApp.getTypefaceRegularSans());
//			viewHolder.costTV.setTypeface(myApp.getTypefaceRegularSans());

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Opportunity tempOpportunity = opportunityList.get(position);
		opportunityList.size();
		if(viewHolder.nameTV == null)
		{
			System.out.println("111111");
		}
		if(tempOpportunity == null)
		{
			System.out.println("222222");
		}
		viewHolder.nameTV.setText(tempOpportunity.getName());
		viewHolder.statusTV.setText(tempOpportunity.getKpmgStatus());
//		viewHolder.descriptionTV.setText(tempOpportunity.getCustomerId());
//		viewHolder.costTV.setText("$"
//				+ tempOpportunity.getTotalAmount());
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