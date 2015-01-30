package com.mw.crm.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.crm.activity.R;
import com.mw.crm.extra.MyApp;
import com.mw.crm.model.MenuItem;

public class MenuAdapter extends BaseAdapter {

	Context context;
	List<MenuItem> menuItemList;

	MyApp myApp;

	LayoutInflater inflater;

	public MenuAdapter(Context context, List<MenuItem> menuItemList) {
		super();
		this.context = context;
		this.menuItemList = menuItemList;
		myApp = (MyApp) context.getApplicationContext();
	}

	static class ViewHolder {
		protected ImageView menuIV;
		protected TextView labelTV;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder viewHolder;
		if (convertView == null) {
			inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			viewHolder = new ViewHolder();
			convertView = inflater.inflate(R.layout.element_menu2,
					parent, false);

			viewHolder.menuIV = (ImageView) convertView
					.findViewById(R.id.menu_item_IV);
			viewHolder.labelTV = (TextView) convertView
					.findViewById(R.id.menu_item_TV);

			viewHolder.labelTV.setTypeface(myApp.getTypefaceBoldSans());
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		MenuItem tempMenuItem = menuItemList.get(position);
		viewHolder.labelTV.setText(tempMenuItem.getText());
		viewHolder.menuIV.setImageDrawable(tempMenuItem.getDrawable());

		return convertView;
	}

	@Override
	public int getCount() {
		return menuItemList.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

}