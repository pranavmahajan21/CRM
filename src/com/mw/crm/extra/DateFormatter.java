package com.mw.crm.extra;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class DateFormatter {

	public String formatDateToString(Date date) {
		if (date == null) {
			return "-";
		}

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy, hh:mm");
		// formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

		String dateStr = formatter.format(date);
		System.out.println(">><<><><><" + dateStr);
		return dateStr;
	}

	public String formatDateToString2(Date date) {

		if (date == null) {
			return "-";
		}

		SimpleDateFormat formatter = new SimpleDateFormat(
				"EEE, dd MMM, hh:mmaa");
		// formatter.setTimeZone(TimeZone.getTimeZone("GMT"));

		String dateStr = formatter.format(date);
		System.out.println(">><<><><><" + dateStr);
		return dateStr;
	}

	public String formatDateToString3(Date date) {

		if (date == null) {
			return "-";
		}

		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy HH:mm");

		String dateStr = formatter.format(date);
		System.out.println(">><<><><><" + dateStr);
		return dateStr;
	}

	public String formatDateToString4(Date date) {
		// 2015-01-14T14:16:34Z
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ss'Z'");

		String dateStr = formatter.format(date);
		System.out.println(">><<><><><" + dateStr);
		return dateStr;
	}

	public Date formatStringToDate(String dateString) {
		System.out.println("1212  :  " + dateString);
		SimpleDateFormat formatter = new SimpleDateFormat(
				"yyyy-MM-dd'T'HH:mm:ssZ");

		Date date = null;
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// System.out.println(">><<><><><" + dateStr);
		return date;
	}

	public Date formatStringToDate2(String dateString) {
		System.out.println("1212  :  " + dateString);
		SimpleDateFormat formatter = new SimpleDateFormat(
				"EEE, dd MMM, hh:mmaa");

		Date date = null;
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// System.out.println(">><<><><><" + dateStr);
		return date;
	}

	public Date formatStringToDate3(String dateString) {
		System.out.println("1212  :  " + dateString);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy, hh:mm");
		// formatter.setTimeZone(TimeZone.getTimeZone("GMT+5"));

		Date date = null;
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		/** Just a temporary fix **/
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, -5);
		cal.add(Calendar.MINUTE, -30);
		Date dateMinus530 = cal.getTime();
		/** Just a temporary fix **/

		// return date;
		return dateMinus530;
	}

	public Date formatStringToDate3Copy(String dateString) {
		System.out.println("1212  :  " + dateString);
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy, hh:mm");

		Date date = null;
		try {
			date = formatter.parse(dateString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		// System.out.println(">><<><><><" + dateStr);
		return date;
	}

	@SuppressWarnings("deprecation")
	public Date formatStringSpecialToDate(String dateString) {
		/**
		 * http://stackoverflow.com/questions/20036075/json-datetime-parsing-in-
		 * android
		 **/
		if (dateString == null) {
			return null;
		}
		System.out.println("1212  : " + dateString);
		// String ackwardDate = "/Date(1376841597000)/";

		Calendar calendar = Calendar.getInstance();
		String ackwardRipOff = dateString.replace("\\/Date(", "").replace(
				")\\/", "");
		Long timeInMillis = Long.valueOf(ackwardRipOff);
		calendar.setTimeInMillis(timeInMillis);
		// calendar.setTimeZone(TimeZone.get);
		System.out.println(calendar.getTime().toGMTString());
		System.out.println(calendar.getTime());
		return calendar.getTime();
	}
}
