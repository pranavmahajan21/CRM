package com.mw.crm.extra;

public class Constant {

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

	/** Used to get lookups **/
	/** Used for **/
	public static String USER = "/PostUser";
	/** Used for taxonomy **/
	public static String PRODUCT = "/PostProduct";
	/** Used for Profit Center **/
	public static String PROFIT_CENTER = "/PostProfitCenter";
	/** Used for Solution Name **/
	public static String SOLUTION = "/PostSolution";
	public static String COMPETITOR = "/PostCompetitor";

	/** Path of Excels **/
	public static String ACCOUNT_CATEGORY_PATH = "excels/account_category.xls";
	public static String COUNTRY_PATH = "excels/country.xls";
	public static String DOR_PATH = "excels/dor.xls";
	public static String INTERACTION_TYPE_PATH = "excels/interaction_type.xls";
	public static String LEAD_SOURCE_PATH = "excels/lead_source.xls";
	public static String LOB_PATH = "excels/lob.xls";
	public static String OPPORTUNITY_LOST_PATH = "excels/opportunity_lost.xls";
	public static String OPPORTUNITY_WON_PATH = "excels/opportunity_won.xls";
	public static String PRIORITY_LEVEL_PATH = "excels/priority_level.xls";
	public static String PROBABILITY_PATH = "excels/probability.xls";
	public static String SALES_STAGE_PATH = "excels/sales_stage.xls";
	public static String SECTOR_PATH = "excels/sector.xls";
	public static String STATUS_PATH = "excels/status.xls";
	public static String SUB_LOP_PATH = "excels/sub_lob.xls";
	public static String SUPPORT_TYPE_PATH = "excels/support_type.xls";

	/** Error Message for of Excels **/
	public static String ACCOUNT_CATEGORY_ERROR = "Error while loading Account Category.";
	public static String COUNTRY_ERROR = "Error while loading Country.";
	public static String DOR_ERROR = "Error while loading DOR.";
	public static String INTERACTION_TYPE_ERROR = "Error while loading Interaction Type.";
	public static String LEAD_SOURCE_ERROR = "Error while loading Lead Source.";
	public static String LOB_ERROR = "Error while loading LOB.";
	public static String OPPORTUNITY_LOST_ERROR = "Error while loading Opportunity Lost excel.";
	public static String OPPORTUNITY_WON_ERROR = "Error while loading Opportunity Won excel.";
	public static String PRIORITY_LEVEL_ERROR = "Error while loading Priority Level.";
	public static String PROBABILITY_ERROR = "Error while loading Probability.";
	public static String SALES_STAGE_ERROR = "Error while loading Sales Stage.";
	public static String SECTOR_ERROR = "Error while loading Sector.";
	public static String STATUS_ERROR = "Error while loading Status.";
	public static String SUB_LOP_ERROR = "Error while loading Sub LOP.";
	public static String SUPPORT_TYPE_ERROR = "Error while loading Support Type.";
	/** Error Message for of Excels **/

	/** Error Messages **/
	public static String ERR_SOL1_AMOUNT = "For SOLUTION1 Fee should be greater than (PY NFR + CY NFR + CY1 NFR + CY2 NFR).";
	public static String ERR_SOL2_AMOUNT = "For SOLUTION2 Fee should be greater than (PY NFR + CY NFR + CY1 NFR + CY2 NFR).";
	public static String ERR_SOL3_AMOUNT = "For SOLUTION3 Fee should be greater than (PY NFR + CY NFR + CY1 NFR + CY2 NFR).";
	public static String ERR_SOL4_AMOUNT = "For SOLUTION4 Fee should be greater than (PY NFR + CY NFR + CY1 NFR + CY2 NFR).";
	/** Error Messages **/

	public static String OPPORTUNITY_LOST = "798330003";
	public static String OPPORTUNITY_WON = "798330004";

	/** Preferences **/
	public static String PREF_IS_USER_LOGIN = "is_user_login";
	public static String PREF_LOGIN_USER_ID = "login_user_id";
	
	public static String PREF_ACCOUNT_LIST = "account_list";
	public static String PREF_APPOINTMENT_LIST = "appointment_list";
	public static String PREF_CONTACT_LIST = "contact_list";
	public static String PREF_OPPORTUNITY_LIST = "opportunity_list";
	
	public static String PREF_LAST_SYNC_DATE_ACCOUNT = "last_sync_date_account";
	public static String PREF_LAST_SYNC_DATE_APPOINTMENT = "last_sync_date_appointment";
	public static String PREF_LAST_SYNC_DATE_CONTACT = "last_sync_date_contact";
	public static String PREF_LAST_SYNC_DATE_OPPORTUNITY = "last_sync_date_opportunity";
	
	public static String PREF_USER_MAP = "user_map";
	public static String PREF_PRODUCT_MAP = "product_map";
	public static String PREF_PROFIT_CENTER_MAP = "profit_center_map";
	public static String PREF_SOLUTION_MAP = "solution_map";
	public static String PREF_COMPETITOR_MAP = "competitor_map";
	/** Preferences **/	
	
	/** Search User **/
	public static final int USER_SOLUTION_MANAGER1 = 500;
	public static final int USER_SOLUTION_PARTNER1 = 501;
	public static final int USER_SOLUTION_MANAGER2 = 502;
	public static final int USER_SOLUTION_PARTNER2 = 503;
	public static final int USER_SOLUTION_MANAGER3 = 504;
	public static final int USER_SOLUTION_PARTNER3 = 505;
	public static final int USER_SOLUTION_MANAGER4 = 506;
	public static final int USER_SOLUTION_PARTNER4 = 507;
	public static int USER_ = 500;

	/*
	 * public static final int SOLUTION_SOLUTION_NAME1 = 520; public static
	 * final int SOLUTION_SOLUTION_NAME2 = 521; public static final int
	 * SOLUTION_SOLUTION_NAME3 = 522; public static final int
	 * SOLUTION_SOLUTION_NAME4 = 523;
	 */

	/** To check which solution tab is visible **/
	public static final int SOLUTION1_VISIBLE = 600;
	public static final int SOLUTION2_VISIBLE = 601;
	public static final int SOLUTION3_VISIBLE = 602;
	public static final int SOLUTION4_VISIBLE = 603;

	/** Search Tags **/
	final public static int SEARCH_SECTOR = 100;
	final public static int SEARCH_HQ_COUNTRY = 101;
	final public static int SEARCH_SUB_LOB = 102;
	final public static int SEARCH_USER = 103;
	final public static int SEARCH_ACCOUNT = 104;
	public static final int SEARCH_SOLUTION = 105;
	public static final int SEARCH_PRODUCT = 106;
	public static final int SEARCH_PROFIT_CENTER = 107;
	public static final int SEARCH_COMPETITOR = 108;

	final public static int DETAILS_ACCOUNT = 1100;
	final public static int DETAILS_APPOINTMENT = 1100;
	final public static int DETAILS_CONTACT = 1100;
	final public static int DETAILS_OPPORTUNITY = 1100;

	
	/** Default value of lookups **/
	public static final int DEFAULT_SALES_STAGE_INDEX = 0;
	public static final int DEFAULT_PROBABILITY_INDEX = 2;
	public static final int DEFAULT_STATUS_INDEX = 0;
	public static final int DEFAULT_NO_OF_SOLUTION_INDEX = 0;
}
