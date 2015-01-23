package com.mw.crm.model;

import java.io.Serializable;

public class Country implements Serializable {

	private static final long serialVersionUID = 4059493468921724874L;

	String name;
	int id;

	public Country(String name, int id) {
		super();
		this.name = name;
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
