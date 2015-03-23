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
import com.mw.crm.application.MyApp;
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
		this.tempOpportunityList = new ArrayList<Opportunity>();
		this.tempOpportunityList.addAll(opportunityList);
	}

	static class ViewHolder {
		protected TextView crmId_TV;
		protected TextView description_TV;
		protected TextView customerId_TV;
		protected TextView status_TV;
		protected TextView cost_TV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			System.out.println("OnceOnceOnceOnce  : " + position);
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.element_opportunity2,
					parent, false);

			viewHolder.crmId_TV = (TextView) convertView
					.findViewById(R.id.crmId_TV);
			viewHolder.description_TV = (TextView) convertView
					.findViewById(R.id.description_TV);
			viewHolder.customerId_TV = (TextView) convertView
					.findViewById(R.id.customerId_TV);
			viewHolder.status_TV = (TextView) convertView
					.findViewById(R.id.status_TV);
			viewHolder.cost_TV = (TextView) convertView
					.findViewById(R.id.cost_TV);

			viewHolder.crmId_TV.setTypeface(myApp.getTypefaceBoldSans());
			viewHolder.description_TV.setTypeface(myApp.getTypefaceBoldSans());
			viewHolder.customerId_TV.setTypeface(myApp.getTypefaceBoldSans());
			viewHolder.status_TV.setTypeface(myApp.getTypefaceRegularSans());
			viewHolder.cost_TV.setTypeface(myApp.getTypefaceRegularSans());

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Opportunity tempOpportunity = opportunityList.get(position);

//		viewHolder.crmId_TV.setText(myApp
//				.getStringNameFromStringJSON(tempOpportunity.getCustomerId()));
		viewHolder.description_TV.setText(tempOpportunity.getDescription());
		viewHolder.customerId_TV.setText(myApp
				.getStringNameFromStringJSON(tempOpportunity.getCustomerId()));

		Integer temp = myApp.getIntValueFromStringJSON(tempOpportunity
				.getKpmgStatus());
		if (temp != null) {
			viewHolder.status_TV.setText(myApp.getStatusMap().get(
					Integer.toString(temp.intValue())));
			temp = null;
		}
		
		temp = myApp.getIntValueFromStringJSON(tempOpportunity
				.getTotalProposalValue());
		if (temp != null) {
			viewHolder.cost_TV.setText(temp.intValue() + "");
			temp = null;
		}

		//		viewHolder.costTV.setText(tempOpportunity.getTotalAmount());
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
				if (tempOpportunity.getDescription().toLowerCase(Locale.getDefault())
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