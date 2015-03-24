package com.mw.crm.extra;

import java.util.ArrayList;
import java.util.List;

import com.mw.crm.application.MyApp;

import android.content.Context;

public class SearchEngine {

	MyApp myApp;
	
	public SearchEngine() {
		super();
	}

	public SearchEngine(Context context) {
		super();
		myApp = (MyApp) context.getApplicationContext();
	}
	
	public int getIndexFromKeyLeadSourceMap(String key) {
		List<String> aa = new ArrayList<String>(myApp.getSalesStageMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyProfitCenterMap(String key) {
		List<String> aa = new ArrayList<String>(myApp.getProfitCenterMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeySolutionMap(String key) {
		List<String> aa = new ArrayList<String>(myApp.getSolutionMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}
	
	
	
	public int getIndexFromKeyAccountCategoryMap(String key) {
		// System.out.println("1111  " + key);
		List<String> aa = new ArrayList<String>(myApp.getAccountCategoryMap().keySet());
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
		List<String> aa = new ArrayList<String>(myApp.getCountryMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			// System.out.println("2222  " + aa.get(i));
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyDORMap(String key) {
		List<String> aa = new ArrayList<String>(myApp.getDorMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyInteractionMap(String key) {
		List<String> aa = new ArrayList<String>(myApp.getInteractionTypeMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyLobMap(String key) {
		List<String> aa = new ArrayList<String>(myApp.getLobMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyProbabilityMap(String key) {
		List<String> aa = new ArrayList<String>(myApp.getProbabilityMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeySalesMap(String key) {
		List<String> aa = new ArrayList<String>(myApp.getSalesStageMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeySectorMap(String key) {
		List<String> aa = new ArrayList<String>(myApp.getSectorMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyStatusMap(String key) {
		List<String> aa = new ArrayList<String>(myApp.getStatusMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeySubLobMap(String key) {
		List<String> aa = new ArrayList<String>(myApp.getSubLobMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return 0;
	}

	public int getIndexFromKeyUserMap(String key) {
		// System.out.println("1111  " + key);
		List<String> aa = new ArrayList<String>(myApp.getUserMap().keySet());
		for (int i = 0; i < aa.size(); i++) {
			// System.out.println("2222  " + aa.get(i));
			if (aa.get(i).equalsIgnoreCase(key)) {
				return i;
			}
		}
		return -1;
	}
}
