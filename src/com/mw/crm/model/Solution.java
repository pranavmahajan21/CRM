package com.mw.crm.model;

import java.io.Serializable;

public class Solution implements Serializable {

	private static final long serialVersionUID = 5847839169649367677L;
	/**  **/

	/** ownerid **/
	String solutionManager;

	/** pcl_partnersolution1 **/
	String solutionPartner;

	/** pcl_solutionsolution1 **/
	String solutionName;

	/** pcl_profitcentersolution1 **/
	String profitCenter;

	/** pcl_taxonomysolution1 **/
	String taxonomy;

	/** pcl_feessolution1 **/
	String fee;

	/** pcl_nfrpreviousyearsolution1 **/
	String pyNfr;

	/** pcl_nfrcurrentyearsolution1 **/
	String cyNfr;

	/** pcl_nfrfy1solution1 **/
	String cyNfr1;

	/** pcl_nfrfy2solution1 **/
	String cyNfr2;

	public Solution(String solutionManager, String solutionPartner,
			String solutionName, String profitCenter, String taxonomy,
			String fee, String pyNfr, String cyNfr, String cyNfr1, String cyNfr2) {
		super();
		this.solutionManager = solutionManager;
		this.solutionPartner = solutionPartner;
		this.solutionName = solutionName;
		this.profitCenter = profitCenter;
		this.taxonomy = taxonomy;
		this.fee = fee;
		this.pyNfr = pyNfr;
		this.cyNfr = cyNfr;
		this.cyNfr1 = cyNfr1;
		this.cyNfr2 = cyNfr2;
	}

	public String getSolutionManager() {
		return solutionManager;
	}

	public void setSolutionManager(String solutionManager) {
		this.solutionManager = solutionManager;
	}

	public String getSolutionPartner() {
		return solutionPartner;
	}

	public void setSolutionPartner(String solutionPartner) {
		this.solutionPartner = solutionPartner;
	}

	public String getSolutionName() {
		return solutionName;
	}

	public void setSolutionName(String solutionName) {
		this.solutionName = solutionName;
	}

	public String getProfitCenter() {
		return profitCenter;
	}

	public void setProfitCenter(String profitCenter) {
		this.profitCenter = profitCenter;
	}

	public String getTaxonomy() {
		return taxonomy;
	}

	public void setTaxonomy(String taxonomy) {
		this.taxonomy = taxonomy;
	}

	public String getFee() {
		return fee;
	}

	public void setFee(String fee) {
		this.fee = fee;
	}

	public String getPyNfr() {
		return pyNfr;
	}

	public void setPyNfr(String pyNfr) {
		this.pyNfr = pyNfr;
	}

	public String getCyNfr() {
		return cyNfr;
	}

	public void setCyNfr(String cyNfr) {
		this.cyNfr = cyNfr;
	}

	public String getCyNfr1() {
		return cyNfr1;
	}

	public void setCyNfr1(String cyNfr1) {
		this.cyNfr1 = cyNfr1;
	}

	public String getCyNfr2() {
		return cyNfr2;
	}

	public void setCyNfr2(String cyNfr2) {
		this.cyNfr2 = cyNfr2;
	}

	@Override
	public String toString() {
		return "Solution [solutionManager=" + solutionManager
				+ ", solutionPartner=" + solutionPartner + ", solutionName="
				+ solutionName + ", profitCenter=" + profitCenter
				+ ", taxonomy=" + taxonomy + ", fee=" + fee + ", pyNfr="
				+ pyNfr + ", cyNfr=" + cyNfr + ", cyNfr1=" + cyNfr1
				+ ", cyNfr2=" + cyNfr2 + "]";
	}

}
