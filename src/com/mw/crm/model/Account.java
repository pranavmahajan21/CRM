package com.mw.crm.model;

import java.io.Serializable;

public class Account implements Serializable {

	private static final long serialVersionUID = -4458542878397893790L;

	String name;
	String accountId;
	String country;
	String subLob;
	String lob;

	String accountCategory;
	String sector;
	String leadPartner;
	
	String corridor;
	
	public Account(String name, String accountId, String country,
			String corridor, String subLob) {
		super();
		this.name = name;
		this.accountId = accountId;
		this.country = country;
		this.corridor = corridor;
		this.subLob = subLob;
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

	public String getCorridor() {
		return corridor;
	}

	public void setCorridor(String corridor) {
		this.corridor = corridor;
	}

	public String getSubLob() {
		return subLob;
	}

	public void setSubLob(String subLob) {
		this.subLob = subLob;
	}

	@Override
	public String toString() {
		return "Account [name=" + name + ", accountId=" + accountId
				+ ", country=" + country + ", corridor=" + corridor
				+ ", subLob=" + subLob + "]";
	}

}
