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

public class SearchListAdapter extends BaseAdapter {

	MyApp myApp;
	Context context;

	LayoutInflater inflater;

	List<String> stringList;
	List<String> tempStringList;

	public SearchListAdapter(Context context, List<String> stringList) {
		super();
		this.context = context;
		this.stringList = stringList;
		this.tempStringList = new ArrayList<String>();
		this.tempStringList.addAll(stringList);
		myApp = (MyApp) context.getApplicationContext();
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

		// Account tempAccount = stringList.get(position);
		viewHolder.itemTV.setText(stringList.get(position));

		return convertView;
	}

	@Override
	public int getCount() {
		return stringList.size();
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
		stringList.clear();
		if (charText.length() == 0) {
			stringList.addAll(tempStringList);
		} else {
			for (String tempString : tempStringList) {
				if (tempString.toLowerCase(Locale.getDefault()).contains(charText)) {
					stringList.add(tempString);
				}
			}
		}
		notifyDataSetChanged();
	}
}