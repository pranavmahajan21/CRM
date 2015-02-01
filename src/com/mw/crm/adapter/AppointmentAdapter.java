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
import com.mw.crm.model.Appointment;

public class AppointmentAdapter extends BaseAdapter {

	MyApp myApp;
	Context context;

	LayoutInflater inflater;

	List<Appointment> appointmentList;
	List<Appointment> tempAppointmentList;

	public AppointmentAdapter(Context context, List<Appointment> appointmentList) {
		super();
		this.context = context;
		this.appointmentList = appointmentList;
		this.tempAppointmentList = new ArrayList<Appointment>();
		this.tempAppointmentList.addAll(appointmentList);
		myApp = (MyApp) context.getApplicationContext();
	}

	public void swapData(List<Appointment> appointmentList) {
		this.appointmentList = appointmentList;
		this.tempAppointmentList = new ArrayList<Appointment>();
		this.tempAppointmentList.addAll(appointmentList);
	}

	static class ViewHolder {
		protected TextView nameTV;
		protected TextView subjectTV;
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
			viewHolder.subjectTV = (TextView) convertView
					.findViewById(R.id.company_TV);

			viewHolder.nameTV.setTypeface(myApp.getTypefaceBoldSans());
			viewHolder.subjectTV.setTypeface(myApp.getTypefaceRegularSans());

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		Appointment tempAppointment = appointmentList.get(position);

		viewHolder.nameTV.setText(tempAppointment.getNameOfTheClientOfficial());
		viewHolder.subjectTV.setText(tempAppointment.getPurposeOfMeeting());

		return convertView;
	}

	@Override
	public int getCount() {
		return appointmentList.size();
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
		appointmentList.clear();
		if (charText.length() == 0) {
			appointmentList.addAll(tempAppointmentList);
		} else {
			for (Appointment tempAppointment : tempAppointmentList) {
				if (tempAppointment.getNameOfTheClientOfficial()
						.toLowerCase(Locale.getDefault()).contains(charText)
						|| tempAppointment.getPurposeOfMeeting()
								.toLowerCase(Locale.getDefault())
								.contains(charText)) {
					appointmentList.add(tempAppointment);
				}
			}
		}
		notifyDataSetChanged();
	}
}