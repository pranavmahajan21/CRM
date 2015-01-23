package com.mw.crm.model;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

public class InternalConnect implements Serializable {

	private static final long serialVersionUID = -1501352588375597416L;

	@SerializedName("lastname")
	String lastName;
	
	@SerializedName("systemuserid")
	String systemUserId;

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSystemUserId() {
		return systemUserId;
	}

	public void setSystemUserId(String systemUserId) {
		this.systemUserId = systemUserId;
	}
}
