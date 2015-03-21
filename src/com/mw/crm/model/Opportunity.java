package com.mw.crm.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Opportunity implements Serializable {

	private static final long serialVersionUID = -1622121450891551591L;

	/**
	 * Order of attributes is same as the order of UI. Do not change.
	 * **/

	/** unknown **/
	String crmId;
	
	/** opportunityid **/
	String opportunityId;

	/** name **/
	String description;

	/** customerid **/
	/** API: PostAccounts **/
	String customerId;
	// String clientName;

	/** pcl_confendential **/
	String isConfidential;

	/** pcl_leadsourcenew **/
	String leadSource;

	/** salesstagecode **/
	String salesStage;

	/** opportunityratingcode **/
	String probability;

	/** pcl_kpmgstatus **/
	String kpmgStatus;

	/** estimatedclosedate **/
	Date expectedClosureDate;

	/** estimatedvalue **/
	String totalProposalValue;

	/** pcl_noofsolutionsrequired **/
	String noOfSolutionRequired;

	List<Solution> solutionList;

	public Opportunity(String opportunityId, String description,
			String customerId, String isConfidential, String leadSource,
			String salesStage, String probability, String kpmgStatus,
			Date expectedClosureDate, String totalProposalValue,
			String noOfSolutionRequired, List<Solution> solutionList) {
		super();
		this.opportunityId = opportunityId;
		this.description = description;
		this.customerId = customerId;
		this.isConfidential = isConfidential;
		this.leadSource = leadSource;
		this.salesStage = salesStage;
		this.probability = probability;
		this.kpmgStatus = kpmgStatus;
		this.expectedClosureDate = expectedClosureDate;
		this.totalProposalValue = totalProposalValue;
		this.noOfSolutionRequired = noOfSolutionRequired;
		this.solutionList = solutionList;
	}

	public Opportunity() {
		super();
	}

	public String getCrmId() {
		return crmId;
	}

	public void setCrmId(String crmId) {
		this.crmId = crmId;
	}

	public String getOpportunityId() {
		return opportunityId;
	}

	public void setOpportunityId(String opportunityId) {
		this.opportunityId = opportunityId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getIsConfidential() {
		return isConfidential;
	}

	public void setIsConfidential(String isConfidential) {
		this.isConfidential = isConfidential;
	}

	public String getLeadSource() {
		return leadSource;
	}

	public void setLeadSource(String leadSource) {
		this.leadSource = leadSource;
	}

	public String getSalesStage() {
		return salesStage;
	}

	public void setSalesStage(String salesStage) {
		this.salesStage = salesStage;
	}

	public String getProbability() {
		return probability;
	}

	public void setProbability(String probability) {
		this.probability = probability;
	}

	public String getKpmgStatus() {
		return kpmgStatus;
	}

	public void setKpmgStatus(String kpmgStatus) {
		this.kpmgStatus = kpmgStatus;
	}

	public Date getExpectedClosureDate() {
		return expectedClosureDate;
	}

	public void setExpectedClosureDate(Date expectedClosureDate) {
		this.expectedClosureDate = expectedClosureDate;
	}

	public String getTotalProposalValue() {
		return totalProposalValue;
	}

	public void setTotalProposalValue(String totalProposalValue) {
		this.totalProposalValue = totalProposalValue;
	}

	public String getNoOfSolutionRequired() {
		return noOfSolutionRequired;
	}

	public void setNoOfSolutionRequired(String noOfSolutionRequired) {
		this.noOfSolutionRequired = noOfSolutionRequired;
	}

	public List<Solution> getSolutionList() {
		return solutionList;
	}

	public void setSolutionList(List<Solution> solutionList) {
		this.solutionList = solutionList;
	}

	@Override
	public String toString() {
		return "Opportunity [opportunityId=" + opportunityId + ", description="
				+ description + ", customerId=" + customerId
				+ ", isConfidential=" + isConfidential + ", leadSource="
				+ leadSource + ", salesStage=" + salesStage + ", probability="
				+ probability + ", kpmgStatus=" + kpmgStatus
				+ ", expectedClosureDate=" + expectedClosureDate
				+ ", totalProposalValue=" + totalProposalValue
				+ ", noOfSolutionRequired=" + noOfSolutionRequired
				+ ", solutionList=" + solutionList + "]";
	}

}
