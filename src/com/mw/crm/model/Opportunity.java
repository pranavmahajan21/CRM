package com.mw.crm.model;

import java.io.Serializable;

public class Opportunity implements Serializable {

	private static final long serialVersionUID = -1622121450891551591L;

	String name;
	String totalAmount;
	String ownerId;
	String transactionCurrencyId; 
	String customerId;
	String kpmgStatus;
	String opportunityId;

	public Opportunity() {
		super();
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getTransactionCurrencyId() {
		return transactionCurrencyId;
	}

	public void setTransactionCurrencyId(String transactionCurrencyId) {
		this.transactionCurrencyId = transactionCurrencyId;
	}

	public String getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKpmgStatus() {
		return kpmgStatus;
	}

	public void setKpmgStatus(String kpmgStatus) {
		this.kpmgStatus = kpmgStatus;
	}

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public Opportunity(String ownerId, String transactionCurrencyId,
			String totalAmount, String customerId, String name,
			String kpmgStatus, String opportunityId) {
		super();
		this.ownerId = ownerId;
		this.transactionCurrencyId = transactionCurrencyId;
		this.totalAmount = totalAmount;
		this.customerId = customerId;
		this.name = name;
		this.kpmgStatus = kpmgStatus;
		this.opportunityId = opportunityId;
	}

	@Override
	public String toString() {
		return "Opportunity [name=" + name + ", totalAmount=" + totalAmount
				+ ", ownerId=" + ownerId + ", transactionCurrencyId="
				+ transactionCurrencyId + ", customerId=" + customerId
				+ ", kpmgStatus=" + kpmgStatus + ", opportunityId="
				+ opportunityId + "]";
	}

	
	
}
