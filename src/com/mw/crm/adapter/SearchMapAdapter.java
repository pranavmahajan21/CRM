package com.mw.crm.adapter;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.mw.crm.application.MyApp;

public class SearchMapAdapter extends BaseAdapter {

	MyApp myApp;
	Context context;

	LayoutInflater inflater;

	Map<String, String> searchMap;
	Map<String, String> tempSearchMap;

	List<String> keys;
	List<String> values;

	public SearchMapAdapter(Context context, Map<String, String> searchMap) {
		super();
		this.context = context;
		this.searchMap = searchMap;
		this.tempSearchMap = new LinkedHashMap<String, String>();
		this.tempSearchMap.putAll(searchMap);
		myApp = (MyApp) context.getApplicationContext();
		keys = new ArrayList<String>(searchMap.keySet());
		values = new ArrayList<String>(searchMap.values());
	}

	static class ViewHolder {
		protected TextView itemTV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.element_search_item,
					parent, false);

			viewHolder.itemTV = (TextView) convertView
					.findViewById(R.id.item_TV);

			viewHolder.itemTV.setTypeface(myApp.getTypefaceBoldSans());

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		System.out.println("1212  " + searchMap.get(keys.get(position)));
		viewHolder.itemTV.setText(searchMap.get(keys.get(position)));

		return convertView;
	}

	@Override
	public int getCount() {
		return searchMap.size();
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
		searchMap.clear();
		if (charText.length() == 0) {
			searchMap.putAll(tempSearchMap);
		} else {
			
			for (int i = 0; i < tempSearchMap.size(); i++) {
				
			}
			
			
//			for (Account tempAccount : tempSearchMap) {
//				if (tempAccount.getName().toLowerCase(Locale.getDefault())
//						.contains(charText)
//						|| tempAccount.getCountry()
//								.toLowerCase(Locale.getDefault())
//								.contains(charText)) {
//					accountList.add(tempAccount);
//				}
//			}
		}
		notifyDataSetChanged();
	}
}