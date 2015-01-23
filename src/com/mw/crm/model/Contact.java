package com.mw.crm.model;

import java.io.Serializable;

public class Contact implements Serializable {

	private static final long serialVersionUID = 8908670710075500832L;

	/*
	 * As per the JSON received:- ownerid : Internal Connect, parentcustomerid :
	 * Organization, customertypecode : Degree of Relationship, pcl_designation : designation
	 */

	String firstName;
	String lastName;
	String email;
	String designation;
	String mobilePhone;
	String telephone;

	String internalConnect;
	String organization;
	String degreeOfRealation;
	String contactId;

	public Contact(String internalConnect, String organization,
			String degreeOfRealation, String contactId, String lastName) {
		super();
		this.internalConnect = internalConnect;
		this.organization = organization;
		this.degreeOfRealation = degreeOfRealation;
		this.contactId = contactId;
		this.lastName = lastName;
	}

	public String getInternalConnect() {
		return internalConnect;
	}

	public void setInternalConnect(String internalConnect) {
		this.internalConnect = internalConnect;
	}

	public String getOrganization() {
		return organization;
	}

	public void setOrganization(String organization) {
		this.organization = organization;
	}

	public String getDegreeOfRealation() {
		return degreeOfRealation;
	}

	public void setDegreeOfRealation(String degreeOfRealation) {
		this.degreeOfRealation = degreeOfRealation;
	}

	public String getContactId() {
		return contactId;
	}

	public void setContactId(String contactId) {
		this.contactId = contactId;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Override
	public String toString() {
		return "Contact [internalConnect=" + internalConnect
				+ ", organization=" + organization + ", degreeOfRealation="
				+ degreeOfRealation + ", contactId=" + contactId
				+ ", lastName=" + lastName + "]";
	}

}
