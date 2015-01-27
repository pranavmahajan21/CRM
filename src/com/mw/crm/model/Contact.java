package com.mw.crm.model;

import java.io.Serializable;

public class Contact implements Serializable {

	private static final long serialVersionUID = 8908670710075500832L;

	/*TODO: update this list
	 * As per the JSON received:- ownerid : Internal Connect, parentcustomerid :
	 * Organization, customertypecode : Degree of Relationship, pcl_designation
	 * : designation
	 */

	String firstName;
	String lastName;
	String email;
	String designation;
	String mobilePhone;
	String telephone;

	String internalConnect;
	String organization;
	String degreeOfRelation;
	String contactId;

	public Contact(String internalConnect, String organization,
			String degreeOfRelation, String contactId, String lastName) {
		super();
		this.internalConnect = internalConnect;
		this.organization = organization;
		this.degreeOfRelation = degreeOfRelation;
		this.contactId = contactId;
		this.lastName = lastName;
	}

	
	
	public Contact(String firstName, String lastName, String email,
			String designation, String mobilePhone, String telephone,
			String internalConnect, String organization,
			String degreeOfRelation, String contactId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.designation = designation;
		this.mobilePhone = mobilePhone;
		this.telephone = telephone;
		this.internalConnect = internalConnect;
		this.organization = organization;
		this.degreeOfRelation = degreeOfRelation;
		this.contactId = contactId;
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

	public String getDegreeOfRelation() {
		return degreeOfRelation;
	}

	public void setDegreeOfRelation(String degreeOfRelation) {
		this.degreeOfRelation = degreeOfRelation;
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

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDesignation() {
		return designation;
	}

	public void setDesignation(String designation) {
		this.designation = designation;
	}

	public String getMobilePhone() {
		return mobilePhone;
	}

	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	public String getTelephone() {
		return telephone;
	}

	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	@Override
	public String toString() {
		return "Contact [firstName=" + firstName + ", lastName=" + lastName
				+ ", email=" + email + ", designation=" + designation
				+ ", mobilePhone=" + mobilePhone + ", telephone=" + telephone
				+ ", internalConnect=" + internalConnect + ", organization="
				+ organization + ", degreeOfRelation=" + degreeOfRelation
				+ ", contactId=" + contactId + "]";
	}

}
