//
//
//  Generated by StarUML(tm) Java Add-In
//
//  @ Project : Untitled
//  @ File Name : Attraction.java
//  @ Date : 2016-11-21
//  @ Author : 
//
//
package project.android.com.android5777_9254_6826.model.entities;
import java.io.Serializable;
import java.util.Date;

import project.android.com.android5777_9254_6826.model.entities.Properties;


public class Attraction implements Serializable {
	private Properties.AttractionType Type;
	private String Country;
	private Date StartDate;
	private Date EndDate;
	private float Price;
	private String Description;
	private String BusinessID;
	private String AttractionID;

	public Attraction(String attractionID,Properties.AttractionType type, String country, Date startDate, Date endDate, float price, String description, String businessID) {
		Type = type;
		Country = country;
		StartDate = startDate;
		EndDate = endDate;
		Price = price;
		Description = description;
		BusinessID = businessID;
		AttractionID = attractionID;
	}

	public Properties.AttractionType getType() {
		return Type;
	}

	public void setType(Properties.AttractionType type) {
		Type = type;
	}

	public String getCountry() {
		return Country;
	}

	public void setCountry(String country) {
		Country = country;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public Date getEndDate() {
		return EndDate;
	}

	public void setEndDate(Date endDate) {
		EndDate = endDate;
	}

	public float getPrice() {
		return Price;
	}

	public void setPrice(float price) {
		Price = price;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getBusinessID() {
		return BusinessID;
	}

	public void setBusinessID(String businessID) {
		BusinessID = businessID;
	}

	public String getAttractionID() {
		return AttractionID;
	}

	public void setAttractionID(String attractionID) {
		AttractionID = attractionID;
	}
}
