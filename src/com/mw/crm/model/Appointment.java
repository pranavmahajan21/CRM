package com.mw.crm.model;

import java.io.Serializable;
import java.util.Date;

public class Appointment implements Serializable {

	private static final long serialVersionUID = 6850430895663499470L;

	/**
	 * As per the JSON received:- subject : purposeOfMeeting,,, description :
	 * description,,, pcl_typeofmeeting : typeOfMeeting,,,
	 * pcl_nameoftheclientofficial : nameOfTheClientOfficial,,,
	 * pcl_designationofclientofficial : designationOfClientOfficial,,,
	 * scheduledstart : startTime,,, scheduledend : endTime
	 */

	String id;
	String purposeOfMeeting;
	String account;// PostAccounts
	String nameOfTheClientOfficial;
	String designationOfClientOfficial;
	String description;
	String typeOfMeeting;
	String owner;
	String organizer;
	Date startTime;
	Date endTime;

	public Appointment(String id, String purposeOfMeeting, String account,
			String nameOfTheClientOfficial, String designationOfClientOfficial,
			String description, String typeOfMeeting, String owner,
			String organizer, Date startTime, Date endTime) {
		super();
		this.id = id;
		this.purposeOfMeeting = purposeOfMeeting;
		this.account = account;
		this.nameOfTheClientOfficial = nameOfTheClientOfficial;
		this.designationOfClientOfficial = designationOfClientOfficial;
		this.description = description;
		this.typeOfMeeting = typeOfMeeting;
		this.owner = owner;
		this.organizer = organizer;
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public String getPurposeOfMeeting() {
		return purposeOfMeeting;
	}

	public void setPurposeOfMeeting(String purposeOfMeeting) {
		this.purposeOfMeeting = purposeOfMeeting;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNameOfTheClientOfficial() {
		return nameOfTheClientOfficial;
	}

	public void setNameOfTheClientOfficial(String nameOfTheClientOfficial) {
		this.nameOfTheClientOfficial = nameOfTheClientOfficial;
	}

	public String getTypeOfMeeting() {
		return typeOfMeeting;
	}

	public void setTypeOfMeeting(String typeOfMeeting) {
		this.typeOfMeeting = typeOfMeeting;
	}

	public String getDesignationOfClientOfficial() {
		return designationOfClientOfficial;
	}

	public void setDesignationOfClientOfficial(
			String designationOfClientOfficial) {
		this.designationOfClientOfficial = designationOfClientOfficial;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getOrganizer() {
		return organizer;
	}

	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", purposeOfMeeting="
				+ purposeOfMeeting + ", description=" + description
				+ ", nameOfTheClientOfficial=" + nameOfTheClientOfficial
				+ ", typeOfMeeting=" + typeOfMeeting
				+ ", designationOfClientOfficial="
				+ designationOfClientOfficial + ", startTime=" + startTime
				+ ", endTime=" + endTime + "]";
	}

}
