package com.mw.crm.extra;

public class Constant {

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
	
	/*public static final int SOLUTION_SOLUTION_NAME1 = 520;
	public static final int SOLUTION_SOLUTION_NAME2 = 521;
	public static final int SOLUTION_SOLUTION_NAME3 = 522;
	public static final int SOLUTION_SOLUTION_NAME4 = 523;*/


	/** To check which solution tab is visible **/
	public static final int SOLUTION1_VISIBLE = 600;
	public static final int SOLUTION2_VISIBLE = 601;
	public static final int SOLUTION3_VISIBLE = 602;
	public static final int SOLUTION4_VISIBLE = 603;
	
	/** Search Tags **/
	public static final int SEARCH_SOLUTION = 13;
	public static final int SEARCH_PRODUCT = 14;
	public static final int SEARCH_PROFIT_CENTER = 15;
	public static final int SEARCH_COMPETITOR = 16;
	
	/** Default value of lookups **/
	public static final int DEFAULT_SALES_STAGE_INDEX = 0;
	public static final int DEFAULT_PROBABILITY_INDEX = 2;
	public static final int DEFAULT_STATUS_INDEX = 0;
	public static final int DEFAULT_NO_OF_SOLUTION_INDEX = 0;
}
