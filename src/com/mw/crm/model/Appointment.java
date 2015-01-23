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
	String description;
	String nameOfTheClientOfficial;
	String typeOfMeeting;
	String designationOfClientOfficial;
	Date startTime;
	Date endTime;

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

	public Appointment(String id, String purposeOfMeeting, String description,
			String nameOfTheClientOfficial, String typeOfMeeting,
			String designationOfClientOfficial, Date startTime, Date endTime) {
		super();
		this.id = id;
		this.purposeOfMeeting = purposeOfMeeting;
		this.description = description;
		this.nameOfTheClientOfficial = nameOfTheClientOfficial;
		this.typeOfMeeting = typeOfMeeting;
		this.designationOfClientOfficial = designationOfClientOfficial;
		this.startTime = startTime;
		this.endTime = endTime;
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
