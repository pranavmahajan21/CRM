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
}
