package com.mw.crm.extra;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.example.crm.activity.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mw.crm.activity.MenuActivity;
import com.mw.crm.model.Account;
import com.mw.crm.model.Appointment;
import com.mw.crm.model.Contact;
import com.mw.crm.model.InternalConnect;
import com.mw.crm.model.MenuItem;
import com.mw.crm.model.Opportunity;
import com.mw.crm.service.AccountService;
import com.mw.crm.service.AppointmentService;
import com.mw.crm.service.ContactService;
import com.mw.crm.service.InternalConnectService;
import com.mw.crm.service.OpportunityService;

@SuppressLint("SimpleDateFormat")
public class MyApp extends Application {

	// public static String URL = "https://www.kpmgapps.kpmg.in/crmproxy/api";
	public static String URL = "https://www.kpmgapps.kpmg.in/CRMProxyTest/api";

	/* Used to get List<T> */
	public static String APPOINTMENTS_DATA = "/PostAppointments";
	public static String ACCOUNTS_DATA = "/PostAccounts";
	public static String CONTACTS_DATA = "/PostContacts";
	public static String OPPORTUNITY_DATA = "/PostOpportunities";

	/* Used to create <T> */
	public static String APPOINTMENTS_ADD = "/appointments";
	public static String ACCOUNTS_ADD = "/accounts";
	public static String CONTACTS_ADD = "/contacts";
	public static String OPPORTUNITY_ADD = "/opportunities";
	
	/* Used to update <T> */
	public static String APPOINTMENTS_UPDATE = "/PostAppointmentUpdate";
	public static String ACCOUNTS_UPDATE = "/PostAccountUpdate";
	public static String CONTACTS_UPDATE = "/PostContactUpdate";
	public static String OPPORTUNITY_UPDATE = "/PostContactUpdate";
	
	/*
	 * Used for Internal Connect in Contacts screen & owner in Appointments
	 * screen
	 */
	public static String USER = "/PostUser";

	List<MenuItem> menuItemList = new ArrayList<MenuItem>();

	List<Opportunity> opportunityList;// = new ArrayList<Opportunity>();
	List<Contact> contactList;// = new ArrayList<Contact>();
	List<Appointment> appointmentList;// = new ArrayList<Appointment>();
	List<Account> accountList;// = new ArrayList<Account>();

	List<InternalConnect> internalConnectList;// = new
												// ArrayList<InternalConnect>();
	Map<String, String> countryMap;

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editor;

	Typeface typefaceRegularSans;
	Typeface typefaceBoldSans;

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

		gson = new Gson();
	}

	private void fetchPreferences() {
		if (sharedPreferences.contains("account_list")) {
			String value = sharedPreferences.getString("account_list", null);
			if (value != null) {
				Type listType = (Type) new TypeToken<ArrayList<Account>>() {
				}.getType();
				setAccountList((List<Account>) gson.fromJson(value, listType));
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
				setAppointmentList((List<Appointment>) gson.fromJson(value,
						listType));
				System.out.println("Appointment size  : "
						+ getAppointmentList().size());
			}
		}

		if (sharedPreferences.contains("contact_list")) {
			String value = sharedPreferences.getString("contact_list", null);
			if (value != null) {
				Type listType = (Type) new TypeToken<ArrayList<Contact>>() {
				}.getType();
				setContactList((List<Contact>) gson.fromJson(value, listType));
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
				setOpportunityList((List<Opportunity>) gson.fromJson(value,
						listType));
				System.out.println("Opportunity size  : "
						+ getOpportunityList().size());
			}
		}

		if (sharedPreferences.contains("internal_connect_list")) {
			String value = sharedPreferences.getString("internal_connect_list",
					null);
			if (value != null) {
				Type listType = (Type) new TypeToken<ArrayList<InternalConnect>>() {
				}.getType();
				setInternalConnectList((List<InternalConnect>) gson.fromJson(
						value, listType));
				System.out.println("InternalConnect size  : "
						+ getInternalConnectList().size());
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
		if (internalConnectList == null) {
			isUnloadedDataThere = true;
			intent = new Intent(this, InternalConnectService.class);
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

	private void staticData() {
		opportunityList
				.add(new Opportunity("A, Pradeep", null,
						"Meher Pudumjee, Pheroz Pudumj...", "Active", null,
						null, null));
		opportunityList
				.add(new Opportunity("Abhishek, A", null,
						"Blue Print for development o...", "On Hold", null,
						null, null));
		opportunityList.add(new Opportunity("Abraham, Anil Alex", null,
				"IvyCap Trust DT-FS-13-14", "Scrapped", null, null, null));
		opportunityList
				.add(new Opportunity("Abraham Philomena", null,
						"LLP conversion, Profit repat....", "Active", null,
						null, null));
		opportunityList.add(new Opportunity("Adhikari Sawant, Rupali", null,
				"Macquarie Group Services-IES...", "Delete", null, null, null));
		opportunityList.add(new Opportunity("Adhikari, Pratik", null,
				"Meher Pudumjee, Pheroz Pudumj...", "Scrapped", null, null,
				null));
		opportunityList
				.add(new Opportunity("Adhikary, Subhendu", null,
						"Macquarie Group Services IES...", "On Hold", null,
						null, null));
		opportunityList.add(new Opportunity("Administrator, CRM", null,
				"Regulatory compliance under F...", "Scrapped", null, null,
				null));
		opportunityList.add(new Opportunity("Advani, Harish", null,
				"Responing to Queries from ta...", "Active", null, null, null));
		opportunityList
				.add(new Opportunity("Advani, Vikram", null,
						"Security Audit of application...", "Delete", null,
						null, null));
		opportunityList
				.add(new Opportunity("Agarwal, Abhijit", null,
						"Blue Print for development o...", "On Hold", null,
						null, null));
		opportunityList.add(new Opportunity("**Agarwal, Abhishek**", null,
				"Macquarie Group Services-IES...", "Delete", null, null, null));

		contactList.add(new Contact("NTPC Limited", null, null, null,
				"A A Sheikh"));
		contactList.add(new Contact("Saraswat Bank", null, null, null,
				"A A Vijayakar"));
		contactList.add(new Contact("Tata Motors Limited", null, null, null,
				"A Abhay Phalke"));
		contactList.add(new Contact("RBS Bank N.V", null, null, null,
				"A Aditya Sharma"));
		contactList.add(new Contact("GE Group", null, null, null,
				"A Afzal Modhak"));
		contactList.add(new Contact("Sterlite Industries", null, null, null,
				"A Agarwal"));
		contactList.add(new Contact("TPG Capital L.P.", null, null, null,
				"A Agarwal"));
		contactList.add(new Contact("Hindalco Industries Limited", null, null,
				null, "Agarwala"));
		contactList.add(new Contact("Aditya Birla Management Corporation",
				null, null, null, "Ajaysimha"));
		contactList.add(new Contact("Arisglobal Software Private Limited",
				null, null, null, "**A Agarwal**"));

		appointmentList.add(new Appointment("","Discuss on Access Bank Project",
				null, null, null, null, new Date(), new Date()));
		appointmentList.add(new Appointment("","Wedding of Shivali and mahendra",
				null, null, null, null, new Date(), new Date()));
		appointmentList.add(new Appointment("","Balanced Score Card Demo:OBIEE",
				null, null, null, null, new Date(), new Date()));
		appointmentList.add(new Appointment("",
				"Conference Room Pilot - Advisory Function", null, null, null,
				null, new Date(), new Date()));
		appointmentList.add(new Appointment("",
				"Conference Room Pilot(CRP) - CRM Application Tax...", null,
				null, null, null, new Date(), new Date()));
		appointmentList.add(new Appointment("","VC with the Promoters", null,
				null, null, null, new Date(), new Date()));
		appointmentList.add(new Appointment("","**Srikanth meeting**", null, null,
				null, null, new Date(), new Date()));

		accountList.add(new Account("South Asia Clean Energy Fund....",
				"4440158", null, null, null));
		accountList.add(new Account("(n)Code Solutions", "4440152", null, null,
				null));
		accountList.add(new Account("1 MG", "4440157", null, null, null));
		accountList.add(new Account("1 MG Road", "4440156", null, null, null));
		accountList.add(new Account("10C India Internet India private Ltd",
				"5550158", null, null, null));
		accountList.add(new Account("120 Media Collective Private Ltd",
				"5550152", null, null, null));
		accountList.add(new Account("1FB Support Services Private Ltd",
				"5550153", null, null, null));
		accountList.add(new Account("**2 Degrees**", "5550155", null, null,
				null));
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initThings();
		loadMenuItems();
		createCountryData();

//		Intent intent2 = new Intent(this, OpportunityService.class);
//		startService(intent2);
		
		if (sharedPreferences.contains("is_user_login")
				&& sharedPreferences.getBoolean("is_user_login", false)) {
			fetchPreferences();

			Intent intent = new Intent(this, MenuActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);
			startActivity(intent);
		}
		// staticData();

	}

	private void createCountryData() {
		countryMap = new LinkedHashMap<>();
		countryMap.put("15001", "Afghanistan");
		countryMap.put("1001", "Africa");
		countryMap.put("1002", "Algeria");
		countryMap.put("2001", "Argentina");
		countryMap.put("4001", "Australia");
		countryMap.put("8001", "Austria");
		countryMap.put("15002", "Bahrain");
		countryMap.put("15003", "Bangladesh");
		countryMap.put("5001", "Belgium");
		countryMap.put("24005", "Bermuda");
		countryMap.put("17002", "Bhutan");
		countryMap.put("2002", "Brazil");
		countryMap.put("17003", "Brunei");
		countryMap.put("8002", "Bulgaria");
		countryMap.put("17004", "Burma");
		countryMap.put("3001", "Cambodia");
		countryMap.put("1003", "Cameroon");
		countryMap.put("6001", "Canada");
		countryMap.put("17005", "Cayman Islands");
		countryMap.put("2003", "Chile");
		countryMap.put("7001", "China");
		countryMap.put("2004", "Colombia");
		countryMap.put("1004", "Congo");
		countryMap.put("15004", "Cyprus");
		countryMap.put("8003", "Czech Republic");
		countryMap.put("20001", "Denmark");
		countryMap.put("15005", "Dubai");
		countryMap.put("15006", "Egypt");
		countryMap.put("1005", "Ethiopia");
		countryMap.put("8004", "Finland");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
//		countryMap.put("", "");
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return params;
	}
	
	public List<Opportunity> getOpportunityList() {
		return opportunityList;
	}

	public void setOpportunityList(List<Opportunity> opportunityList) {
		this.opportunityList = opportunityList;
		String json = gson.toJson(opportunityList);
		editor.putString("opportunity_list", json);
		editor.commit();
	}

	public List<MenuItem> getMenuItemList() {
		return menuItemList;
	}

	public void setMenuItemList(List<MenuItem> menuItemList) {
		this.menuItemList = menuItemList;
	}

	public List<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
		String json = gson.toJson(contactList);
		editor.putString("contact_list", json);
		editor.commit();
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

	public List<Appointment> getAppointmentList() {
		return appointmentList;
	}

	public void setAppointmentList(List<Appointment> appointmentList) {
		this.appointmentList = appointmentList;
		String json = gson.toJson(appointmentList);
		editor.putString("appointment_list", json);
		editor.commit();
	}

	public List<Account> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Account> accountList) {
		this.accountList = accountList;
		String json = gson.toJson(accountList);
		editor.putString("account_list", json);
		editor.commit();
	}

	public static String decryptData(String string) {

		// String aa = new Encrypter(null, null).decrypt(string);
		// System.out.println("!@!@" + aa);
		// TODO create an Encrypt object at class level
		return new Encrypter(null, null).decrypt(string);
	}

	public static String encryptData(String string) {

		// String aa = new Encrypter(null, null).decrypt(string);
		// System.out.println("!@!@" + aa);
		return new Encrypter(null, null).encrypt(string);
	}

	public List<InternalConnect> getInternalConnectList() {
		return internalConnectList;
	}

	public void setInternalConnectList(List<InternalConnect> internalConnectList) {
		this.internalConnectList = internalConnectList;
		String json = gson.toJson(internalConnectList);
		editor.putString("internal_connect_list", json);
		editor.commit();
	}

	
	public String formatDateToString(Date date) {

		SimpleDateFormat formatter = new SimpleDateFormat("MMM dd");

		String dateStr = formatter.format(date);
		System.out.println(">><<><><><" + dateStr);
		return dateStr;
	}
	
	public String formatDateToString2(Date date) {

		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");

		String dateStr = formatter.format(date);
		System.out.println(">><<><><><" + dateStr);
		return dateStr;
	}
	
	public String formatDateToString3(Date date) {

		SimpleDateFormat formatter = new SimpleDateFormat("dd MMM, yyyy HH:mm");

		String dateStr = formatter.format(date);
		System.out.println(">><<><><><" + dateStr);
		return dateStr;
	}

	public Date formatStringToDate(String dateString) {

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
}
