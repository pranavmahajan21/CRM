package com.mw.crm.model;

import android.graphics.drawable.Drawable;

public class MenuItem {

	 String text;
	Drawable drawable;

	// boolean isArrowVisible;

	public MenuItem() {
		super();
	}

	public MenuItem(String text, Drawable drawable) {
		super();
		this.text = text;
		this.drawable = drawable;
	}

	public Drawable getDrawable() {
		return drawable;
	}

	public void setDrawable(Drawable drawable) {
		this.drawable = drawable;
	}

	 public String getText() {
	 return text;
	 }
	
	 public void setText(String text) {
	 this.text = text;
	 }
	//
	// public boolean isArrowVisible() {
	// return isArrowVisible;
	// }
	//
	// public void setArrowVisible(boolean isArrowVisible) {
	// this.isArrowVisible = isArrowVisible;
	// }

}
