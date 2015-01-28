package com.mw.crm.extra;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
	 * Used for Internal Connect in CONTACT screen & owner in APPOINTMENT screen
	 * & LeadPartner in ACCOUNT screen
	 */
	public static String USER = "/PostUser";

	List<MenuItem> menuItemList = new ArrayList<MenuItem>();

	List<Opportunity> opportunityList;// = new ArrayList<Opportunity>();
	List<Contact> contactList;// = new ArrayList<Contact>();
	List<Appointment> appointmentList;// = new ArrayList<Appointment>();
	List<Account> accountList;// = new ArrayList<Account>();

	List<InternalConnect> internalConnectList;// = new
												// ArrayList<InternalConnect>();

	private static String ACCOUNT_CATEGORY_PATH = "excels/account_category.xls";
	private static String COUNTRY_PATH = "excels/country.xls";
	private static String DOR_PATH = "excels/dor.xls";
	private static String INTERACTION_TYPE_PATH = "excels/interaction_type.xls";
	private static String LOB_PATH = "excels/lob.xls";
	private static String PROBABILITY_PATH = "excels/probability.xls";
	private static String SALES_STAGE_PATH = "excels/sales_stage.xls";
	private static String SECTOR_PATH = "excels/sector.xls";
	private static String STATUS_PATH = "excels/status.xls";
	private static String SUB_LOP_PATH = "excels/sub_lob.xls";

	/** Coming from excels **/
	Map<String, String> accountCategoryMap;
	Map<String, String> countryMap;
	Map<String, String> dorMap;
	Map<String, String> interactionTypeMap;
	Map<String, String> lobMap;
	Map<String, String> probabilityMap;
	Map<String, String> salesStageMap;
	Map<String, String> sectorMap;
	Map<String, String> statusMap;
	Map<String, String> subLobMap;

	/** Coming from excels **/
	Map<String, String> userMap;
	// Map<String, String> countryMap;

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

		accountCategoryMap = new LinkedHashMap<String, String>();
		countryMap = new LinkedHashMap<String, String>();
		dorMap = new LinkedHashMap<String, String>();
		interactionTypeMap = new LinkedHashMap<String, String>();
		lobMap = new LinkedHashMap<String, String>();
		probabilityMap = new LinkedHashMap<String, String>();
		salesStageMap = new LinkedHashMap<String, String>();
		sectorMap = new LinkedHashMap<String, String>();
		statusMap = new LinkedHashMap<String, String>();
		subLobMap = new LinkedHashMap<String, String>();

		userMap = new LinkedHashMap<String, String>();

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

		// if (sharedPreferences.contains("internal_connect_list")) {
		// String value = sharedPreferences.getString("internal_connect_list",
		// null);
		// if (value != null) {
		// Type listType = (Type) new TypeToken<ArrayList<InternalConnect>>() {
		// }.getType();
		// setInternalConnectList((List<InternalConnect>) gson.fromJson(
		// value, listType));
		// System.out.println("InternalConnect size  : "
		// + getInternalConnectList().size());
		// }
		// }

		if (sharedPreferences.contains("user_map")) {
			String value = sharedPreferences.getString("user_map", null);
			if (value != null) {
				Type mapType = (Type) new TypeToken<Map<String, String>>() {
				}.getType();
				// setInternalConnectList((List<InternalConnect>) gson.fromJson(
				// value, mapType));
				setUserMap((Map<String, String>) gson.fromJson(value, mapType));
				System.out.println("User map size  : " + getUserMap().size());
			}
		}

//		loadUnloadedData();
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
						null, null, null, null));
		opportunityList
				.add(new Opportunity("Abhishek, A", null,
						"Blue Print for development o...", "On Hold", null,
						null, null, null, null));
		opportunityList.add(new Opportunity("Abraham, Anil Alex", null,
				"IvyCap Trust DT-FS-13-14", "Scrapped", null, null, null, null, null));
		opportunityList
				.add(new Opportunity("Abraham Philomena", null,
						"LLP conversion, Profit repat....", "Active", null,
						null, null, null, null));
		opportunityList.add(new Opportunity("Adhikari Sawant, Rupali", null,
				"Macquarie Group Services-IES...", "Delete", null, null, null, null, null));
		opportunityList.add(new Opportunity("Adhikari, Pratik", null,
				"Meher Pudumjee, Pheroz Pudumj...", "Scrapped", null, null,
				null, null, null));
		opportunityList
				.add(new Opportunity("Adhikary, Subhendu", null,
						"Macquarie Group Services IES...", "On Hold", null,
						null, null, null, null));
		opportunityList.add(new Opportunity("Administrator, CRM", null,
				"Regulatory compliance under F...", "Scrapped", null, null,
				null, null, null));
		opportunityList.add(new Opportunity("Advani, Harish", null,
				"Responing to Queries from ta...", "Active", null, null, null, null, null));
		opportunityList
				.add(new Opportunity("Advani, Vikram", null,
						"Security Audit of application...", "Delete", null,
						null, null, null, null));
		opportunityList
				.add(new Opportunity("Agarwal, Abhijit", null,
						"Blue Print for development o...", "On Hold", null,
						null, null, null, null));
		opportunityList.add(new Opportunity("**Agarwal, Abhishek**", null,
				"Macquarie Group Services-IES...", "Delete", null, null, null, null, null));

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

		appointmentList.add(new Appointment("",
				"Discuss on Access Bank Project", null, null, null, null, null,
				new Date(), new Date()));
		appointmentList.add(new Appointment("",
				"Wedding of Shivali and mahendra", null, null, null, null,
				null, new Date(), new Date()));
		appointmentList.add(new Appointment("",
				"Balanced Score Card Demo:OBIEE", null, null, null, null, null,
				new Date(), new Date()));
		appointmentList.add(new Appointment("",
				"Conference Room Pilot - Advisory Function", null, null, null,
				null, null, new Date(), new Date()));
		appointmentList.add(new Appointment("",
				"Conference Room Pilot(CRP) - CRM Application Tax...", null,
				null, null, null, null, new Date(), new Date()));
		appointmentList.add(new Appointment("", "VC with the Promoters", null,
				null, null, null, null, new Date(), new Date()));
		appointmentList.add(new Appointment("", "**Srikanth meeting**", null,
				null, null, null, null, new Date(), new Date()));

//		accountList.add(new Account("South Asia Clean Energy Fund....",
//				"4440158", null, null, null));
//		accountList.add(new Account("(n)Code Solutions", "4440152", null, null,
//				null));
//		accountList.add(new Account("1 MG", "4440157", null, null, null));
//		accountList.add(new Account("1 MG Road", "4440156", null, null, null));
//		accountList.add(new Account("10C India Internet India private Ltd",
//				"5550158", null, null, null));
//		accountList.add(new Account("120 Media Collective Private Ltd",
//				"5550152", null, null, null));
//		accountList.add(new Account("1FB Support Services Private Ltd",
//				"5550153", null, null, null));
//		accountList.add(new Account("**2 Degrees**", "5550155", null, null,
//				null));
	}

	@Override
	public void onCreate() {
		super.onCreate();

		initThings();
		loadMenuItems();
		// createCountryData();

		readDataFromExcel();
		// Intent intent2 = new Intent(this, OpportunityService.class);
		// startService(intent2);

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
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
		// countryMap.put("", "");
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

	public List<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(List<Contact> contactList) {
		this.contactList = contactList;
		String json = gson.toJson(contactList);
		editor.putString("contact_list", json);
		editor.commit();
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

	public List<Opportunity> getOpportunityList() {
		return opportunityList;
	}

	public void setOpportunityList(List<Opportunity> opportunityList) {
		this.opportunityList = opportunityList;
		String json = gson.toJson(opportunityList);
		editor.putString("opportunity_list", json);
		editor.commit();
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, String> userMap) {
		this.userMap = userMap;
		String json = gson.toJson(userMap);
		editor.putString("user_map", json);
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

	public Map<String, String> getLobMap() {
		return lobMap;
	}

	public void setLobMap(Map<String, String> lobMap) {
		this.lobMap = lobMap;
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

	public static String getPerfectString(String string) {
		// Does both of 1. removes Quotes 2. Decrypt Data 
//		return MyApp.removeQuotesFromString(MyApp.decryptData(string));
		
		String s = MyApp.decryptData(string);
		System.out.println("After decryption : " + s.length() +"  asdsd  :  " + (s==null));
		String s2 = MyApp.removeQuotesFromString(s);
		System.out.println("After removing quotes  : " + s2);
		
		return s2;
	}
	
	public static String removeQuotesFromString(String string) {
		return string.substring(1, string.length() - 1);
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

	

	public void readDataFromExcel() {
		readAccountCategoryExcel();
		readCountryExcel();
		readDorExcel();
		readInteractionTypeExcel();
		readLobExcel();
		readProbabilityExcel();
		readSalesStageExcel();
		readSectorExcel();
		readStatusExcel();
		readSubLopExcel();
	}

	public void readAccountCategoryExcel() {
		/**
		 * http://stackoverflow.com/questions/5428794/biffexception-while-
		 * reading-an-excel-sheet
		 **/
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(getAssets().open(ACCOUNT_CATEGORY_PATH));
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to load Account Category data.",
					Toast.LENGTH_LONG).show();
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
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(getAssets().open(COUNTRY_PATH));
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to load Country data.",
					Toast.LENGTH_LONG).show();
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
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(getAssets().open(DOR_PATH));
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to load DOR data.", Toast.LENGTH_LONG)
					.show();
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
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(getAssets().open(INTERACTION_TYPE_PATH));
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to load Interaction Type data.",
					Toast.LENGTH_LONG).show();
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			interactionTypeMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	public void readLobExcel() {
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(getAssets().open(LOB_PATH));
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to load lob data.", Toast.LENGTH_LONG)
					.show();
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			lobMap.put(sheet.getCell(0, i).getContents(), sheet.getCell(1, i)
					.getContents());
		}

	}

	public void readProbabilityExcel() {
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(getAssets().open(PROBABILITY_PATH));
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to load Probability data.",
					Toast.LENGTH_LONG).show();
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
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(getAssets().open(SALES_STAGE_PATH));
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to load Sales Stage data.",
					Toast.LENGTH_LONG).show();
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
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(getAssets().open(SECTOR_PATH));
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to load Sector data.",
					Toast.LENGTH_LONG).show();
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
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(getAssets().open(STATUS_PATH));
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to load Status data.",
					Toast.LENGTH_LONG).show();
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			statusMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

	public void readSubLopExcel() {
		Workbook w = null;
		try {
			w = Workbook.getWorkbook(getAssets().open(SUB_LOP_PATH));
		} catch (BiffException | IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Unable to load Sub Lop data.",
					Toast.LENGTH_LONG).show();
			return;
		}

		/** Get the first sheet **/
		Sheet sheet = w.getSheet(0);

		for (int i = 0; i < sheet.getRows(); i++) {
			subLobMap.put(sheet.getCell(0, i).getContents(), sheet
					.getCell(1, i).getContents());
		}

	}

}
