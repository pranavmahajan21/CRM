package com.mw.crm.application;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.mw.crm.extra.Constant;
import com.mw.crm.extra.CreateDialog;
import com.mw.crm.extra.Encrypter;
import com.mw.crm.model.Account;
import com.mw.crm.model.Appointment;
import com.mw.crm.model.Contact;
import com.mw.crm.model.Opportunity;

public class MyApp extends Application {

	/*
	 * Used for Internal Connect in CONTACT screen & owner in APPOINTMENT screen
	 * & LeadPartner in ACCOUNT screen
	 */

	String loginUserId;

	final public static int NOTHING_ELSE_MATTERS = 55;

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
	// private ImageLoader mImageLoader;

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

	@Override
	public void onCreate() {
		super.onCreate();

		Toast.makeText(this, "dskhf", Toast.LENGTH_SHORT).show();
		System.out.println("hello everyboooty");

		initThings();
		readDataFromExcel();

		// Intent intent2 = new Intent(this, OpportunityService.class);
		// startService(intent2);

		// if (sharedPreferences.contains("is_user_login")
		// && sharedPreferences.getBoolean("is_user_login", false)) {
		// System.out.println("hello   1");
		// } else {
		// System.out.println("hello   2");
		// }

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

	// public ImageLoader getImageLoader() {
	// getRequestQueue();
	// if (mImageLoader == null) {
	// mImageLoader = new ImageLoader(this.mRequestQueue,
	// new LruBitmapCache());
	// }
	// return this.mImageLoader;
	// }

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

	public List<Account> getAccountList() {
		return accountList;
	}

	public void setAccountList(List<Account> accountList,
			boolean updatePreferences, String syncTime) {
		this.accountList = accountList;
		if (updatePreferences) {
			String json = gson.toJson(accountList);
			editor.putString(Constant.PREF_ACCOUNT_LIST, json);
			if (syncTime != null) {
				editor.putString(Constant.PREF_LAST_SYNC_DATE_ACCOUNT, syncTime);
			}
			editor.commit();
		}
	}

	public List<Appointment> getAppointmentList() {
		return appointmentList;
	}

	public void setAppointmentList(List<Appointment> appointmentList,
			boolean updatePreferences, String syncTime) {
		this.appointmentList = appointmentList;
		if (updatePreferences) {
			String json = gson.toJson(appointmentList);
			editor.putString(Constant.PREF_APPOINTMENT_LIST, json);
			if (syncTime != null) {
				editor.putString(Constant.PREF_LAST_SYNC_DATE_APPOINTMENT, syncTime);
			}
			editor.commit();
		}
	}

	public List<Contact> getContactList() {
		return contactList;
	}

	public void setContactList(List<Contact> contactList,
			boolean updatePreferences, String syncTime) {
		this.contactList = contactList;
		if (updatePreferences) {
			String json = gson.toJson(contactList);
			editor.putString(Constant.PREF_CONTACT_LIST, json);
			if (syncTime != null) {
				editor.putString(Constant.PREF_LAST_SYNC_DATE_CONTACT, syncTime);
			}
			editor.commit();
		}
	}
	
	public List<Opportunity> getOpportunityList() {
		return opportunityList;
	}

	public void setOpportunityList(List<Opportunity> opportunityList,
			boolean updatePreferences, String syncTime) {
		this.opportunityList = opportunityList;
		if (updatePreferences) {
			String json = gson.toJson(opportunityList);
			editor.putString(Constant.PREF_OPPORTUNITY_LIST, json);
			if (syncTime != null) {
				editor.putString(Constant.PREF_LAST_SYNC_DATE_OPPORTUNITY, syncTime);
			}
			editor.commit();
		}
	}

	public Map<String, String> getUserMap() {
		return userMap;
	}

	public void setUserMap(Map<String, String> userMap,
			boolean updatePreferences) {
		this.userMap = userMap;

		if (updatePreferences) {
			String json = gson.toJson(userMap);
			editor.putString(Constant.PREF_USER_MAP, json);
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
			editor.putString(Constant.PREF_PRODUCT_MAP, json);
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
			editor.putString(Constant.PREF_PROFIT_CENTER_MAP, json);
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
			editor.putString(Constant.PREF_SOLUTION_MAP, json);
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
			editor.putString(Constant.PREF_COMPETITOR_MAP, json);
			editor.commit();
		}
	}

	public String getLoginUserId() {
		return loginUserId;
	}

	public void setLoginUserId(String loginUserId) {
		this.loginUserId = loginUserId;
		System.out.println("!@!@  " + loginUserId);

		editor.putString(Constant.PREF_LOGIN_USER_ID, loginUserId);
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

	// public int getIndexFromKeyAccountCategoryMap(String key) {
	// // System.out.println("1111  " + key);
	// List<String> aa = new ArrayList<String>(accountCategoryMap.keySet());
	// for (int i = 0; i < aa.size(); i++) {
	// // System.out.println("2222  " + accountList.get(i).getAccountId());
	// if (aa.get(i).equalsIgnoreCase(key)) {
	// return i;
	// }
	// }
	// return 0;
	// }
	//
	// public int getIndexFromKeyCountryMap(String key) {
	// // System.out.println("1111  " + key);
	// List<String> aa = new ArrayList<String>(countryMap.keySet());
	// for (int i = 0; i < aa.size(); i++) {
	// // System.out.println("2222  " + aa.get(i));
	// if (aa.get(i).equalsIgnoreCase(key)) {
	// return i;
	// }
	// }
	// return 0;
	// }
	//
	// public int getIndexFromKeyDORMap(String key) {
	// List<String> aa = new ArrayList<String>(dorMap.keySet());
	// for (int i = 0; i < aa.size(); i++) {
	// if (aa.get(i).equalsIgnoreCase(key)) {
	// return i;
	// }
	// }
	// return 0;
	// }
	//
	// public int getIndexFromKeyInteractionMap(String key) {
	// List<String> aa = new ArrayList<String>(interactionTypeMap.keySet());
	// for (int i = 0; i < aa.size(); i++) {
	// if (aa.get(i).equalsIgnoreCase(key)) {
	// return i;
	// }
	// }
	// return 0;
	// }
	//
	// public int getIndexFromKeyLobMap(String key) {
	// List<String> aa = new ArrayList<String>(lobMap.keySet());
	// for (int i = 0; i < aa.size(); i++) {
	// if (aa.get(i).equalsIgnoreCase(key)) {
	// return i;
	// }
	// }
	// return 0;
	// }
	//
	// public int getIndexFromKeyProbabilityMap(String key) {
	// List<String> aa = new ArrayList<String>(probabilityMap.keySet());
	// for (int i = 0; i < aa.size(); i++) {
	// if (aa.get(i).equalsIgnoreCase(key)) {
	// return i;
	// }
	// }
	// return 0;
	// }
	//
	// public int getIndexFromKeySalesMap(String key) {
	// List<String> aa = new ArrayList<String>(salesStageMap.keySet());
	// for (int i = 0; i < aa.size(); i++) {
	// if (aa.get(i).equalsIgnoreCase(key)) {
	// return i;
	// }
	// }
	// return 0;
	// }
	//
	// public int getIndexFromKeySectorMap(String key) {
	// List<String> aa = new ArrayList<String>(sectorMap.keySet());
	// for (int i = 0; i < aa.size(); i++) {
	// if (aa.get(i).equalsIgnoreCase(key)) {
	// return i;
	// }
	// }
	// return 0;
	// }
	//
	// public int getIndexFromKeyStatusMap(String key) {
	// List<String> aa = new ArrayList<String>(statusMap.keySet());
	// for (int i = 0; i < aa.size(); i++) {
	// if (aa.get(i).equalsIgnoreCase(key)) {
	// return i;
	// }
	// }
	// return 0;
	// }
	//
	// public int getIndexFromKeySubLobMap(String key) {
	// List<String> aa = new ArrayList<String>(subLobMap.keySet());
	// for (int i = 0; i < aa.size(); i++) {
	// if (aa.get(i).equalsIgnoreCase(key)) {
	// return i;
	// }
	// }
	// return 0;
	// }
	//
	// public int getIndexFromKeyUserMap(String key) {
	// // System.out.println("1111  " + key);
	// List<String> aa = new ArrayList<String>(userMap.keySet());
	// for (int i = 0; i < aa.size(); i++) {
	// // System.out.println("2222  " + aa.get(i));
	// if (aa.get(i).equalsIgnoreCase(key)) {
	// return i;
	// }
	// }
	// return -1;
	// }

	/**
	 * Type1:
	 * {"Id":"da7bb1a7-8095-e411-96e8-5cf3fc3f502a","LogicalName":"account",
	 * "Name":"Mascot Click","ExtensionData":{}} Type2:
	 * {"Value":1,"ExtensionData":{}}
	 * **/
	public Integer getIntValueFromStringJSON(String x) {
		/** Used for all mapping values like lob, sub_lob & all the excels **/
		try {
			return new JSONObject(x).getInt("Value");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Double getDoubleValueFromStringJSON(String x) {
		try {
			return new JSONObject(x).getDouble("Value");
		} catch (JSONException e) {
			e.printStackTrace();
			return null;
		} catch (Exception e) {
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
			System.out.println("getIntenWithPreviousSearch");
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

}
