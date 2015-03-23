package com.mw.crm.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.mw.crm.application.MyApp;
import com.mw.crm.model.Opportunity;

public class OpportunitySearchAdapter extends BaseAdapter implements Filterable {

	Context context;
	List<Opportunity> opportunityList;
	List<Opportunity> copyOpportunityList;

	MyApp myApp;

	LayoutInflater inflater;

	private TempFilter filter;
	
	public OpportunitySearchAdapter(Context context, List<Opportunity> opportunityList) {
		super();
		this.context = context;
		this.opportunityList = opportunityList;
		this.copyOpportunityList = new ArrayList<Opportunity>();
		this.copyOpportunityList.addAll(opportunityList);
		myApp = (MyApp) context.getApplicationContext();
		filter = new TempFilter();
		System.out.println("asdsa  : " + opportunityList.size());
	}

	 public void swapData(List<Opportunity> opportunityList) {
	 this.opportunityList = opportunityList;
	 this.copyOpportunityList = new ArrayList<Opportunity>();
	 this.copyOpportunityList.addAll(opportunityList);
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
		viewHolder.statusTV.setText(tempOpportunity.getDescription());
		return convertView;
	}

	@Override
	public int getCount() {
//		return opportunityList.size();
		if (opportunityList != null)
			return opportunityList.size();
		else
			return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

//	public void filter(String charText) {
//		charText = charText.toLowerCase(Locale.getDefault());
//		opportunityList.clear();
//		if (charText.length() == 0) {
//			opportunityList.addAll(copyOpportunityList);
//		} else {
//			for (Opportunity tempOpportunity : copyOpportunityList) {
//				if (tempOpportunity.getName().toLowerCase(Locale.getDefault())
//						.contains(charText)
//						|| tempOpportunity.getKpmgStatus()
//								.toLowerCase(Locale.getDefault())
//								.contains(charText)) {
//					opportunityList.add(tempOpportunity);
//				}
//			}
//		}
//		notifyDataSetChanged();
//	}

	@Override
	public Filter getFilter() {
		return filter;
	}
	
	private class TempFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence charSequence) {
			System.out.println("performFiltering");
			FilterResults oReturn = new FilterResults();
			List<Opportunity> results = new ArrayList<Opportunity>();
			
			// next 2 lines are probably not important
			if (copyOpportunityList == null)
				copyOpportunityList = opportunityList;

			if (charSequence != null) {
				if (copyOpportunityList != null && copyOpportunityList.size() > 0) {
					for (Opportunity tempOpportunity : copyOpportunityList) {
						if (tempOpportunity.getDescription().toLowerCase(Locale.getDefault()).contains(charSequence) || tempOpportunity.getKpmgStatus()
								.toLowerCase(Locale.getDefault())
								.contains(charSequence))
							results.add(tempOpportunity);
					}
				}
				oReturn.values = results;
			}
			// the return is sent to next function as argument
			return oReturn;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			System.out.println("publishResults");
			opportunityList = (ArrayList<Opportunity>) results.values;
			notifyDataSetChanged();
		}
	}
}