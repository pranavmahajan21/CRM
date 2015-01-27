package com.mw.crm.model;

import java.io.Serializable;

public class Account implements Serializable {

	private static final long serialVersionUID = -4458542878397893790L;

	String name;
	String accountId;
	String country;
	String lob;
	String subLob;

	String accountCategory;
	String sector;
	String leadPartner;

	// String corridor;

	// public Account(String name, String accountId, String country,
	// String corridor, String subLob) {
	// super();
	// this.name = name;
	// this.accountId = accountId;
	// this.country = country;
	// this.corridor = corridor;
	// this.subLob = subLob;
	// }

	public Account(String name, String accountId, String country, String lob,
			String subLob, String accountCategory, String sector,
			String leadPartner) {
		super();
		this.name = name;
		this.accountId = accountId;
		this.country = country;
		this.lob = lob;
		this.subLob = subLob;
		this.accountCategory = accountCategory;
		this.sector = sector;
		this.leadPartner = leadPartner;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSubLob() {
		return subLob;
	}

	public void setSubLob(String subLob) {
		this.subLob = subLob;
	}

	public String getLob() {
		return lob;
	}

	public void setLob(String lob) {
		this.lob = lob;
	}

	public String getAccountCategory() {
		return accountCategory;
	}

	public void setAccountCategory(String accountCategory) {
		this.accountCategory = accountCategory;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getLeadPartner() {
		return leadPartner;
	}

	public void setLeadPartner(String leadPartner) {
		this.leadPartner = leadPartner;
	}

	@Override
	public String toString() {
		return "Account [name=" + name + ", accountId=" + accountId
				+ ", country=" + country + ", lob=" + lob + ", subLob="
				+ subLob + ", accountCategory=" + accountCategory + ", sector="
				+ sector + ", leadPartner=" + leadPartner + "]";
	}

}
