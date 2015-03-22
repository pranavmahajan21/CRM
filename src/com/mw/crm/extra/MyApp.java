package com.mw.crm.extra;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mw.crm.model.Account;
import com.mw.crm.model.Appointment;
import com.mw.crm.model.Contact;
import com.mw.crm.model.MenuItem;
import com.mw.crm.model.Opportunity;
import com.mw.crm.service.AccountService;
import com.mw.crm.service.AppointmentService;
import com.mw.crm.service.ContactService;
import com.mw.crm.service.UserService;
import com.mw.crm.service.OpportunityService;

@SuppressLint("SimpleDateFormat")
public class MyApp extends Application {

	// public static String URL = "https://www.kpmgapps.kpmg.in/crmproxy/api";
	public static String URL = "https://www.kpmgapps.kpmg.in/CRMProxyTest/api";

	/** Used to get List<T> **/
	public static String APPOINTMENTS_DATA = "/PostAppointments";
	public static String ACCOUNTS_DATA = "/PostAccounts";
	public static String CONTACTS_DATA = "/PostContacts";
	public static String OPPORTUNITY_DATA = "/PostOpportunities";

	/** Used to create <T> **/
	public static String APPOINTMENTS_ADD = "/appointments";
	public static String ACCOUNTS_ADD = "/accounts";
	public static String CONTACTS_ADD = "/contacts";
	public static String OPPORTUNITY_ADD = "/opportunities";
	public static String SERVICE_ADD = "/serviceconnect";

	/** Used to update <T> **/
	public static String APPOINTMENTS_UPDATE = "/PostAppointmentUpdate";
	public static String ACCOUNTS_UPDATE = "/PostAccountUpdate";
	public static String CONTACTS_UPDATE = "/PostContactUpdate";
	public static String OPPORTUNITY_UPDATE = "/PostOpportunityUpdate";

	public static String LOGIN = "/PostCurrentUser";

	/*
	 * Used for Internal Connect in CONTACT screen & owner in APPOINTMENT screen
	 * & LeadPartner in ACCOUNT screen
	 */

	String loginUserId;

	final public static int SEARCH_SECTOR = 0;
	final public static int SEARCH_HQ_COUNTRY = 1;
	final public static int SEARCH_SUB_LOB = 10;
	final public static int SEARCH_USER = 11;
	final public static int SEARCH_ACCOUNT = 100;

	final public static int DETAILS_ACCOUNT = 1100;
	final public static int DETAILS_APPOINTMENT = 1100;
	final public static int DETAILS_CONTACT = 1100;
	final public static int DETAILS_OPPORTUNITY = 1100;

	final public static int NOTHING_ELSE_MATTERS = 55;

	List<MenuItem> menuItemList = new ArrayList<MenuItem>();

	List<Opportunity> opportunityList;// = new ArrayList<Opportunity>();
	List<Opportunity> opportunityMyList;
	List<Contact> contactList;// = new ArrayList<Contact>();
	List<Appointment> appointmentList;// = new ArrayList<Appointment>();
	List<Account> accountList;// = new ArrayList<Account>();

	/** Coming from excels **/
	Map<String, String> accountCategoryMap;
	Map<String, String> countryMap;
	Map<String, String> dorMap;
	Map<String, String> interactionTypeMap;
	Map<String, String> lobMap;
	Map<String, String> leadSourceMap;
	Map<String, String> opportunityLostMap;
	Map<String, String> opportunityWonMap;
	Map<String, String> priorityLevelMap;
	Map<String, String> probabilityMap;
	Map<String, String> salesStageMap;
	Map<String, String> sectorMap;
	Map<String, String> statusMap;
	Map<String, String> subLobMap;
	Map<String, String> supportTypeMap;

	/** Coming from Internal Connect **/
	/** <db843bfb-9a8a-e411-96e8-5cf3fc3f502a, CRM Audit Director 1 > **/

	Map<String, String> userMap;
	Map<String, String> productMap;
	Map<String, String> profitCenterMap;
	Map<String, String> solutionMap;
	Map<String, String> competitorMap;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	Typeface typefaceRegularSans;
	Typeface typefaceBoldSans;

	Intent previousIntent;

	// CreateDialog createDialog;
	AlertDialog.Builder alertDialogBuilder;
	AlertDialog alertDialog;
	// Volley stuff
	public static final String TAG = MyApp.class.getSimpleName();

	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;

	Gson gson;
	private static MyApp mInstance;

	// VOlley stuff

	private void initThings() {
		mInstance = this;
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		editor = sharedPreferences.edit();

		typefaceRegularSans = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Regular.ttf");
		typefaceBoldSans = Typeface.createFromAsset(getAssets(),
				"fonts/SourceSansPro-Semibold.ttf");

		// previousIntent = getPackageManager().getLaunchIntentForPackage(
		// "com.example.crm.activity");
		//
		// System.out.println("loop  :  "
		// + previousIntent.hasExtra("should_start_app"));

		accountCategoryMap = new LinkedHashMap<String, String>();
		countryMap = new LinkedHashMap<String, String>();
		dorMap = new LinkedHashMap<String, String>();
		interactionTypeMap = new LinkedHashMap<String, String>();
		leadSourceMap = new LinkedHashMap<String, String>();
		lobMap = new LinkedHashMap<String, String>();
		opportunityLostMap = new LinkedHashMap<String, String>();
		opportunityWonMap = new LinkedHashMap<String, String>();
		priorityLevelMap = new LinkedHashMap<String, String>();
		probabilityMap = new LinkedHashMap<String, String>();
		salesStageMap = new LinkedHashMap<String, String>();
		sectorMap = new LinkedHashMap<String, String>();
		statusMap = new LinkedHashMap<String, String>();
		subLobMap = new LinkedHashMap<String, String>();
		supportTypeMap = new LinkedHashMap<String, String>();

		competitorMap = new LinkedHashMap<String, String>();
		productMap = new LinkedHashMap<String, String>();
		profitCenterMap = new LinkedHashMap<String, String>();
		solutionMap = new LinkedHashMap<String, String>();
		userMap = new LinkedHashMap<String, String>();

		// createDialog = new CreateDialog(this);

		gson = new Gson();
	}

	@SuppressWarnings("unchecked")
	private void fetchPreferences() {
		if (sharedPreferences.contains("login_user_id")) {
			setLoginUserId(sharedPreferences.getString("login_user_id", null));
		}

		if (sharedPreferences.contains("account_list")) {
			String value = sharedPreferences.getString("account_list", null);
			if (value != null) {
				Type listType = (Type) new TypeToken<ArrayList<Account>>() {
				}.getType();
				setAccountList((List<Account>) gson.fromJson(value, listType),
						false);
				System.out
						.println("Account size  : " + getAccountList().size());
			}
		}

		if (sharedPreferences.contains("appointment_list")) {
			String value = sharedPreferences
					.getString("appointment_list", null);
			if (value != null) {
				Type listType = (Type) new TypeToken<ArrayList<Appointment>>() {
				}.getType();
				setAppointmentList(
						(List<Appointment>) gson.fromJson(value, listType),
						false);
				System.out.println("Appointment size  : "
						+ getAppointmentList().size());
			}
		}

		if (sharedPreferences.contains("contact_list")) {
			String value = sharedPreferences.getString("contact_list", null);
			if (value != null) {
				Type listType = (Type) new TypeToken<ArrayList<Contact>>() {
				}.getType();
				setContactList((List<Contact>) gson.fromJson(value, listType),
						false);
				System.out
						.println("Contact size  : " + getContactList().size());
			}
		}

		if (sharedPreferences.contains("opportunity_list")) {
			String value = sharedPreferences
					.getString("opportunity_list", null);
			if (value != null) {
				Type listType = (Type) new TypeToken<ArrayList<Opportunity>>() {
				}.getType();
				setOpportunityList(
						(List<Opportunity>) gson.fromJson(value, listType),
						false);
				System.out.println("Opportunity size  : "
						+ getOpportunityList().size());
			}
		}

		if (sharedPreferences.contains("competitor_map")) {
			String value = sharedPreferences.getString("competitor_map", null);
			if (value != null) {
				Type mapType = (Type) new TypeToken<Map<String, String>>() {
				}.getType();
				setCompetitorMap(
						(Map<String, String>) gson.fromJson(value, mapType),
						false);
				System.out.println("Competitor map size  : "
						+ getCompetitorMap().size());
			}
		}
		if (sharedPreferences.contains("product_map")) {
			String value = sharedPreferences.getString("product_map", null);
			if (value != null) {
				Type mapType = (Type) new TypeToken<Map<String, String>>() {
				}.getType();
				setProductMap(
						(Map<String, String>) gson.fromJson(value, mapType),
						false);
				System.out.println("Product map size  : "
						+ getProductMap().size());
			}
		}

		if (sharedPreferences.contains("profit_center_map")) {
			String value = sharedPreferences.getString("profit_center_map",
					null);
			if (value != null) {
				Type mapType = (Type) new TypeToken<Map<String, String>>() {
				}.getType();
				setProfitCenterMap(
						(Map<String, String>) gson.fromJson(value, mapType),
						false);
				System.out.println("Profit Center map size  : "
						+ getProfitCenterMap().size());
			}
		}

		if (sharedPreferences.contains("solution_map")) {
			String value = sharedPreferences.getString("solution_map", null);
			if (value != null) {
				Type mapType = (Type) new TypeToken<Map<String, String>>() {
				}.getType();
				setSolutionMap(
						(Map<String, String>) gson.fromJson(value, mapType),
						false);
				System.out.println("Solution map size  : "
						+ getSolutionMap().size());
			}
		}
		if (sharedPreferences.contains("user_map")) {
			String value = sharedPreferences.getString("user_map", null);
			if (value != null) {
				Type mapType = (Type) new TypeToken<Map<String, String>>() {
				}.getType();
				setUserMap((Map<String, String>) gson.fromJson(value, mapType),
						false);
				System.out.println("User map size  : " + getUserMap().size());
			}
		}

		loadUnloadedData();
	}

	private void loadUnloadedData() {
		boolean isUnloadedDataThere = false;

		Intent intent;
		if (opportunityList == null) {
			isUnloadedDataThere = true;
			intent = new Intent(this, OpportunityService.class);
			startService(intent);
		}
		if (contactList == null) {
			isUnloadedDataThere = true;
			intent = new Intent(this, ContactService.class);
			startService(intent);
		}
		if (appointmentList == null) {
			isUnloadedDataThere = true;
			intent = new Intent(this, AppointmentService.class);
			startService(intent);
		}
		if (accountList == null) {
			isUnloadedDataThere = true;
			intent = new Intent(this, AccountService.class);
			startService(intent);
		}
		if (userMap == null) {
			isUnloadedDataThere = true;
			intent = new Intent(this, UserService.class);
			startService(intent);
		}
		if (isUnloadedDataThere) {
			Toast.makeText(this, "Fetching some unloaded data.",
					Toast.LENGTH_SHORT).show();
		}
	}

	private void loadMenuItems() {
		menuItemList.add(new MenuItem("CONTACTS", getResources().getDrawable(
				R.drawable.contact)));
		menuItemList.add(new MenuItem("ACCOUNTS", getResources().getDrawable(
				R.drawable.account)));
		menuItemList.add(new MenuItem("APPOINTMENTS", getResources()
				.getDrawable(R.drawable.appointment)));
		menuItemList.add(new MenuItem("OPPORTUNITIES", getResources()
				.getDrawable(R.drawable.opportunity)));
	}

	@Override
	public void onCreate() {
		super.onCreate();

		// ProgressDialog.show(this, "Status", "Downloading The master");

		Toast.makeText(this, "dskhf", Toast.LENGTH_SHORT).show();
		System.out.println("hello everyboooty");

		initThings();
		loadMenuItems();

		readDataFromExcel();

		// Intent intent2 = new Intent(this, OpportunityService.class);
		// startService(intent2);

		if (sharedPreferences.contains("is_user_login")
				&& sharedPreferences.getBoolean("is_user_login", false)) {
			System.out.println("hello   1");
			fetchPreferences();

			// Intent intent = new Intent(this, MenuActivity2.class);
			// intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
			// | Intent.FLAG_ACTIVITY_CLEAR_TASK);
			// startActivity(intent);

		} else {
			System.out.println("hello   2");
		}

	}

	public static synchronized MyApp getInstance() {
		return mInstance;
	}

	public RequestQueue getRequestQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public ImageLoader getImageLoader() {
		getRequestQueue();
		if (mImageLoader == null) {
			mImageLoader = new ImageLoader(this.mRequestQueue,
					new LruBitmapCache());
		}
		return this.mImageLoader;
	}

	public <T> void addToRequestQueue(Request<T> req, String tag) {
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue().add(req);
	}

	public <T> void addToRequestQueue(Request<T> req) {
		req.setTag(TAG);
		getRequestQueue().add(req);
	}

	public void cancelPendingRequests(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

	public static JSONObject addParamToJson(JSONObject params) {
		try {
			params.put("username", "b6kU3JMlqnK7wTXzxyspqg==")
					.put("password", "PvLOkPxK4IWICGiEAQSPYA==")
					.put("empId", "s/tmN9+BSG4=")
					.put("imieNo",
							"vXWtixqzyP25f6eRyQekjnIMLL1pRUTBD6kXPvQPs4A0niSxztttvg==")
					.put("osVersion", "dvM4+9Kt1SY=")
					.put("appId", "3VciTvXPU/o=")
					.put("appVersion", "Xtn2GgZpLoo=");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return params;
	}

	public List<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(List<Contact> contactList,
			boolean updatePreferences) {
		this.contactList = contactList;
		if (updatePreferences) {
			String json = gson.toJson(contactList);
			editor.putString("contact_list", json);
			editor.commit();
		}
	}

	public List<Appointment> getAppointmentList() {
		return appointmentList;
	}

	public void setAppointmentList(List<Appointment> appointmentList,
			boolean updatePreferences) {
		this.appointmentList = appointmentList;
		if (updatePreferences) {
			String json = gson.toJson(appointmentList);
			editor.putString("appointment_list", json);
			editor.commit();
		}
	}

	public List<Account> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Account> accountList,
			boolean updatePreferences) {
		this.accountList = accountList;
		if (updatePreferences) {
			String json = gson.toJson(accountList);
			editor.putString("account_list", json);
			editor.commit();
		}
	}

	public List<Opportunity> getOpportunityList() {
		return opportunityList;
	}

	public void setOpportunityList(List<Opportunity> opportunityList,
			boolean updatePreferences) {
		this.opportunityList = opportunityList;
		if (updatePreferences) {
			String json = gson.toJson(opportunityList);
			editor.putString("opportunity_list", json);
			editor.commit();
		}
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	// TODO : replicate boolean everywhere
	public void setUserMap(Map<String, String> userMap,
			boolean updatePreferences) {
		this.userMap = userMap;

		if (updatePreferences) {
			String json = gson.toJson(userMap);
			editor.putString("user_map", json);
			editor.commit();
		}
	}

	public Map<String, String> getProductMap() {
		return productMap;
	}

	public void setProductMap(Map<String, String> productMap,
			boolean updatePreferences) {
		this.productMap = productMap;

		if (updatePreferences) {
			String json = gson.toJson(productMap);
			editor.putString("product_map", json);
			editor.commit();
		}
	}

	public Map<String, String> getProfitCenterMap() {
		return profitCenterMap;
	}

	public void setProfitCenterMap(Map<String, String> profitCenterMap,
			boolean updatePreferences) {
		this.profitCenterMap = profitCenterMap;

		if (updatePreferences) {
			String json = gson.toJson(profitCenterMap);
			editor.putString("profit_center_map", json);
			editor.commit();
		}
	}

	public Map<String, String> getSolutionMap() {
		return solutionMap;
	}

	public void setSolutionMap(Map<String, String> solutionMap,
			boolean updatePreferences) {
		this.solutionMap = solutionMap;

		if (updatePreferences) {
			String json = gson.toJson(solutionMap);
			editor.putString("solution_map", json);
			editor.commit();
		}
	}

	public Map<String, String> getCompetitorMap() {
		return competitorMap;
	}

	public void setCompetitorMap(Map<String, String> competitorMap,
			boolean updatePreferences) {
		this.competitorMap = competitorMap;
		if (updatePreferences) {
			String json = gson.toJson(competitorMap);
			editor.putString("competitor_map", json);
			editor.commit();
		}
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;

		// Toast.makeText(this, loginUserId, Toast.LENGTH_SHORT).show();
		System.out.println("!@!@  " + loginUserId);

		editor.putString("login_user_id", loginUserId);
		editor.commit();
	}

	public List<MenuItem> getMenuItemList() {
		return menuItemList;
	}

	public void setMenuItemList(List<MenuItem> menuItemList) {
		this.menuItemList = menuItemList;
	}

	public Typeface getTypefaceRegularSans() {
		return typefaceRegularSans;
	}

	public void setTypefaceRegularSans(Typeface typefaceRegularSans) {
		this.typefaceRegularSans = typefaceRegularSans;
	}

	public Typeface getTypefaceBoldSans() {
		return typefaceBoldSans;
	}

	public void setTypefaceBoldSans(Typeface typefaceBoldSans) {
		this.typefaceBoldSans = typefaceBoldSans;
	}

	public Map<String, String> getCountryMap() {
		return countryMap;
	}

	public void setCountryMap(Map<String, String> countryMap) {
		this.countryMap = countryMap;
	}

	public Map<String, String> getAccountCategoryMap() {
		return accountCategoryMap;
	}

	public void setAccountCategoryMap(Map<String, String> accountCategoryMap) {
		this.accountCategoryMap = accountCategoryMap;
	}

	public Map<String, String> getDorMap() {
		return dorMap;
	}

	public void setDorMap(Map<String, String> dorMap) {
		this.dorMap = dorMap;
	}

	public Map<String, String> getInteractionTypeMap() {
		return interactionTypeMap;
	}

	public void setInteractionTypeMap(Map<String, String> interactionTypeMap) {
		this.interactionTypeMap = interactionTypeMap;
	}

	public Map<String, String> getLeadSourceMap() {
		return leadSourceMap;
	}

	public void setLeadSourceMap(Map<String, String> leadSourceMap) {
		this.leadSourceMap = leadSourceMap;
	}

	public Map<String, String> getLobMap() {
		return lobMap;
	}

	public void setLobMap(Map<String, String> lobMap) {
		this.lobMap = lobMap;
	}

	public Map<String, String> getOpportunityLostMap() {
		return opportunityLostMap;
	}

	public void setOpportunityLostMap(Map<String, String> opportunityLostMap) {
		this.opportunityLostMap = opportunityLostMap;
	}

	public Map<String, String> getOpportunityWonMap() {
		return opportunityWonMap;
	}

	public void setOpportunityWonMap(Map<String, String> opportunityWonMap) {
		this.opportunityWonMap = opportunityWonMap;
	}

	public Map<String, String> getPriorityLevelMap() {
		return priorityLevelMap;
	}

	public void setPriorityLevelMap(Map<String, String> priorityLevelMap) {
		this.priorityLevelMap = priorityLevelMap;
	}

	public Map<String, String> getProbabilityMap() {
		return probabilityMap;
	}

	public void setProbabilityMap(Map<String, String> probabilityMap) {
		this.probabilityMap = probabilityMap;
	}

	public Map<String, String> getSalesStageMap() {
		return salesStageMap;
	}

	public void setSalesStageMap(Map<String, String> salesStageMap) {
		this.salesStageMap = salesStageMap;
	}

	public Map<String, String> getSectorMap() {
		return sectorMap;
	}

	public void setSectorMap(Map<String, String> sectorMap) {
		this.sectorMap = sectorMap;
	}

	public Map<String, String> getStatusMap() {
		return statusMap;
	}

	public void setStatusMap(Map<String, String> statusMap) {
		this.statusMap = statusMap;
	}

	public Map<String, String> getSubLobMap() {
		return subLobMap;
	}

	public void setSubLobMap(Map<String, String> subLobMap) {
		this.subLobMap = subLobMap;
	}

	public Map<String, String> getSupportTypeMap() {
		return supportTypeMap;
	}

	public void setSupportTypeMap(Map<String, String> supportTypeMap) {
		this.supportTypeMap = supportTypeMap;
	}

	public static String getPerfectString(String string) {
		// Does both of 1. removes Quotes 2. Decrypt Data
		// return MyApp.removeQuotesFromString(MyApp.decryptData(string));

		String s = MyApp.decryptData(string);
		System.out.println((s == null) + "   After decryption : " + s
				+ "  asdsd  :  " + s.length());

		String s2 = MyApp.removeQuotesFromString(s);
		System.out.println("After removing quotes  : " + s2);

		return s2;
	}

	public static String removeQuotesFromString(String string) {
		if (string != null && string.length() > 0) {
			return string.substring(1, string.length() - 1);
		} else {
			// return string;
			return null;
		}
	}

	public static String decryptData(String string) {
		System.out.println("!@!@before" + string);
		String aa = new Encrypter(null, null).decrypt(string);
		System.out.println("!@!@" + aa);

		return new Encrypter(null, null).decrypt(string);
	}

	public static String encryptData(String string) {

		// String aa = new Encrypter(null, null).decrypt(string);
		// System.out.println("!@!@" + aa);
		return new Encrypter(null, null).encrypt(string);
	}

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

	public void readDataFromExcel() {
		readAccountCategoryExcel();
		readCountryExcel();
		readDorExcel();
		readInteractionTypeExcel();
		readLeadSourceExcel();
		readLobExcel();
		readOpportunityLostExcel();
		readOpportunityWonExcel();
		readPriorityLevelExcel();
		readProbabilityExcel();
		readSalesStageExcel();
		readSectorExcel();
		readStatusExcel();
		readSubLobExcel();
		readSupportTypeExcel();
	}

	private Workbook getWorkbookFromAssets(String path, String errorMsg) {
		/**
		 * http://stackoverflow.com/questions/5428794/biffexception-while-
		 * reading-an-excel-sheet
		 **/
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(getAssets().open(path));
			return w;
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			Toast.makeText(this, errorMsg, Toast.LENGTH_LONG).show();
			return null;
		}
	}

	public void readAccountCategoryExcel() {
		Workbook w = getWorkbookFromAssets(Constant.ACCOUNT_CATEGORY_PATH,
				Constant.ACCOUNT_CATEGORY_ERROR);
		if (w == null) {
			return;
		}
		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			accountCategoryMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	public void readCountryExcel() {
		Workbook w = getWorkbookFromAssets(Constant.COUNTRY_PATH,
				Constant.COUNTRY_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			countryMap.put(sheet.getCell(0, i).getContents(),
					sheet.getCell(1, i).getContents());
		}

	}

	public void readDorExcel() {
		Workbook w = getWorkbookFromAssets(Constant.DOR_PATH,
				Constant.DOR_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			dorMap.put(sheet.getCell(0, i).getContents(), sheet.getCell(1, i)
					.getContents());
		}

	}

	public void readInteractionTypeExcel() {
		Workbook w = getWorkbookFromAssets(Constant.INTERACTION_TYPE_PATH,
				Constant.INTERACTION_TYPE_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			interactionTypeMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	private void readLeadSourceExcel() {
		Workbook w = getWorkbookFromAssets(Constant.LEAD_SOURCE_PATH,
				Constant.LEAD_SOURCE_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			leadSourceMap.put(sheet.getCell(0, i).getContents(),
					sheet.getCell(1, i).getContents());
		}
	}

	public void readLobExcel() {
		Workbook w = getWorkbookFromAssets(Constant.LOB_PATH,
				Constant.LOB_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			lobMap.put(sheet.getCell(0, i).getContents(), sheet.getCell(1, i)
					.getContents());
		}

	}

	public void readOpportunityLostExcel() {
		Workbook w = getWorkbookFromAssets(Constant.OPPORTUNITY_LOST_PATH,
				Constant.OPPORTUNITY_LOST_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			opportunityLostMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	public void readOpportunityWonExcel() {
		Workbook w = getWorkbookFromAssets(Constant.OPPORTUNITY_WON_PATH,
				Constant.OPPORTUNITY_WON_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			opportunityWonMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	public void readPriorityLevelExcel() {
		Workbook w = getWorkbookFromAssets(Constant.PRIORITY_LEVEL_PATH,
				Constant.PRIORITY_LEVEL_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			priorityLevelMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	public void readProbabilityExcel() {
		Workbook w = getWorkbookFromAssets(Constant.PROBABILITY_PATH,
				Constant.PROBABILITY_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			probabilityMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	public void readSalesStageExcel() {
		Workbook w = getWorkbookFromAssets(Constant.SALES_STAGE_PATH,
				Constant.SALES_STAGE_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			salesStageMap.put(sheet.getCell(0, i).getContents(),
					sheet.getCell(1, i).getContents());
		}

	}

	public void readSectorExcel() {
		Workbook w = getWorkbookFromAssets(Constant.SECTOR_PATH,
				Constant.SECTOR_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			sectorMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	public void readStatusExcel() {
		Workbook w = getWorkbookFromAssets(Constant.STATUS_PATH,
				Constant.STATUS_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			statusMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	public void readSubLobExcel() {
		Workbook w = getWorkbookFromAssets(Constant.SUB_LOP_PATH,
				Constant.SUB_LOP_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			subLobMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	public void readSupportTypeExcel() {
		Workbook w = getWorkbookFromAssets(Constant.SUPPORT_TYPE_PATH,
				Constant.SUPPORT_TYPE_ERROR);
		if (w == null) {
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			supportTypeMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	public String getContactName(Contact tempContact) {
		String temp = null;
		if (tempContact.getLastName() != null
				&& tempContact.getLastName().length() > 0) {
			temp = tempContact.getLastName();
		}
		if (tempContact.getFirstName() != null
				&& tempContact.getFirstName().length() > 0) {
			if (temp != null) {
				temp = temp + ", " + tempContact.getFirstName();
			} else {
				temp = tempContact.getFirstName();
			}
		}
		return temp;
	}

	public Account getAccountById(String id) {
		System.out.println("1111  " + id);
		for (int i = 0; i < accountList.size(); i++) {
			// System.out.println("2222  " + accountList.get(i).getAccountId());
			if (accountList.get(i).getAccountId().equalsIgnoreCase(id)) {
				return accountList.get(i);
			}
		}
		return null;
	}

	public int getIndexFromKeyAccountMap(String key) {
		// System.out.println("1111  " + key);
		List<String> aa = new ArrayList<String>(accountCategoryMap.keySet());
		for (int i = 0; i < aa.size(); i++) {
			// System.out.println("2222  " + accountList.get(i).getAccountId());
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyCountryMap(String key) {
		// System.out.println("1111  " + key);
		List<String> aa = new ArrayList<String>(countryMap.keySet());
		for (int i = 0; i < aa.size(); i++) {
			// System.out.println("2222  " + aa.get(i));
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyDORMap(String key) {
		List<String> aa = new ArrayList<String>(dorMap.keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyInteractionMap(String key) {
		List<String> aa = new ArrayList<String>(interactionTypeMap.keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyLobMap(String key) {
		List<String> aa = new ArrayList<String>(lobMap.keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyProbabilityMap(String key) {
		List<String> aa = new ArrayList<String>(probabilityMap.keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeySalesMap(String key) {
		List<String> aa = new ArrayList<String>(salesStageMap.keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeySectorMap(String key) {
		List<String> aa = new ArrayList<String>(sectorMap.keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyStatusMap(String key) {
		List<String> aa = new ArrayList<String>(statusMap.keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeySubLobMap(String key) {
		List<String> aa = new ArrayList<String>(subLobMap.keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyUserMap(String key) {
		// System.out.println("1111  " + key);
		List<String> aa = new ArrayList<String>(userMap.keySet());
		for (int i = 0; i < aa.size(); i++) {
			// System.out.println("2222  " + aa.get(i));
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * {"Id":"da7bb1a7-8095-e411-96e8-5cf3fc3f502a","LogicalName":"account",
	 * "Name":"Mascot Click","ExtensionData":{}} {"Value":1,"ExtensionData":{}}
	 * **/
	public Integer getIntValueFromStringJSON(String x) {
		/** Used for all mapping values like lob, sub_lob & all the excels **/
		try {
			return new JSONObject(x).getInt("Value");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getStringNameFromStringJSON(String x) {
		System.out.println("getStringFromStringJSON  :  " + x);
		try {
			return new JSONObject(x).getString("Name");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
		// catch (Exception e) {
		// e.printStackTrace();
		// return null;
		// }
	}

	public String getStringIdFromStringJSON(String x) {
		// System.out.println("getStringIdFromStringJSON  :  " + x);
		try {
			return new JSONObject(x).getString("Id");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		}
	}

	public int getAccountIndexFromAccountId(String id) {
		// Toast.makeText(this, "size : " +accountList.size(),
		// Toast.LENGTH_SHORT).show();
		for (int i = 0; i < accountList.size(); i++) {
			if (accountList.get(i).getAccountId().equalsIgnoreCase(id)) {
				return i;
			}
		}
		return 0;
	}

	public int getAppointmentIndexFromAppointmentId(String id) {
		for (int i = 0; i < appointmentList.size(); i++) {
			if (appointmentList.get(i).getId().equalsIgnoreCase(id)) {
				return i;
			}
		}
		return 0;
	}

	public int getContactIndexFromContactId(String id) {
		// Toast.makeText(this, "size : " +contactList.size(),
		// Toast.LENGTH_SHORT).show();
		for (int i = 0; i < contactList.size(); i++) {
			if (contactList.get(i).getContactId().equalsIgnoreCase(id)) {
				return i;
			}
		}
		return 0;
	}

	public int getOpportunityIndexFromOpportunityId(String id) {
		for (int i = 0; i < opportunityList.size(); i++) {
			if (opportunityList.get(i).getOpportunityId().equalsIgnoreCase(id)) {
				return i;
			}
		}
		return 0;
	}

	public AlertDialog handleError(CreateDialog createDialog,
			VolleyError error, String title) {
		System.out.println("ERROR  : " + error.getMessage());
		error.printStackTrace();

		if (error instanceof NetworkError) {
			System.out.println("NetworkError");
			alertDialogBuilder = createDialog.createAlertDialog(title,
					"NetworkError.", false);
		}
		if (error instanceof NoConnectionError) {
			System.out.println("NoConnectionError you are now offline.");
			alertDialogBuilder = createDialog.createAlertDialog(title,
					"Internet Unavailable. You are offline.", false);
		}
		if (error instanceof ServerError) {
			System.out.println("ServerError");
			alertDialogBuilder = createDialog.createAlertDialog(title,
					"ServerError.", false);
		}
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});

		alertDialog = alertDialogBuilder.create();
		return alertDialog;
	}

	public Intent getIntenWithPreviousSearch(Intent previousIntent) {
		Intent intent = new Intent();
		if (previousIntent.hasExtra("search_text")) {
			intent.putExtra("search_text",
					previousIntent.getStringExtra("search_text"));
		}
		return intent;
	}

	public AlertDialog.Builder addOKToAlertDialogBuilder(
			AlertDialog.Builder alertDialogBuilder) {
		alertDialogBuilder.setPositiveButton("OK",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.dismiss();
					}
				});
		return alertDialogBuilder;
	}

	int noOfTimesDateCalled = 0;
	int noOfTimesTimeCalled = 0;

	String dateTimeString;

	public void onPickDate(final View view) {

		final TimePickerDialog tPicker = new TimePickerDialog(this,
				new TimePickerDialog.OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker view2, int hourOfDay,
							int minute) {
						if (noOfTimesTimeCalled % 2 == 0) {
							// System.out.println(hourOfDay + ":" + minute);

							String timeString = "";

							if (hourOfDay < 10) {
								timeString = "0" + hourOfDay;
							} else {
								timeString = "" + hourOfDay;
							}
							if (minute < 10) {
								timeString = timeString + ":0" + minute;
							} else {
								timeString = timeString + ":" + minute;
							}
							dateTimeString = dateTimeString + ", " + timeString;
							
							view.setTag(dateTimeString);
							// getDateTimeString(view, timeString, false);
						}
						noOfTimesTimeCalled++;
					}
				}, 12, 00, true);
		tPicker.setCancelable(false);

		final Calendar c = Calendar.getInstance();
		int mYear = c.get(Calendar.YEAR);
		int mMonth = c.get(Calendar.MONTH);
		int mDay = c.get(Calendar.DAY_OF_MONTH);

		DatePickerDialog dPicker = new DatePickerDialog(this,
				new DatePickerDialog.OnDateSetListener() {
					/**
					 * http://stackoverflow.com/questions/19836210/method-called
					 * -twice-in-datepicker
					 **/
					@Override
					public void onDateSet(DatePicker view2, int year,
							int monthOfYear, int dayOfMonth) {
						if (noOfTimesDateCalled % 2 == 0) {
							// System.out.println(dayOfMonth + "-"
							// + (monthOfYear + 1) + "-" + year);

							String dateString = "";
							if (dayOfMonth < 10) {
								dateString = "0" + dayOfMonth;
							} else {
								dateString = "" + dayOfMonth;
							}

							if (monthOfYear < 10) {
								dateString = dateString + "-0"
										+ (monthOfYear + 1);
							} else {
								dateString = dateString + "-"
										+ (monthOfYear + 1);
							}
							dateString = dateString + "-" + year;
							dateTimeString = dateString;
							// getDateTimeString(view, dateString, true);
							tPicker.show();
						}
						noOfTimesDateCalled++;
					}
				}, mYear, mMonth, mDay);
		dPicker.setCancelable(false);
		dPicker.show();
	}
}
