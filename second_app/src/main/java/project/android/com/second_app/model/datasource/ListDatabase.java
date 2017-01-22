package project.android.com.second_app.model.datasource;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;

import project.android.com.second_app.controller.StartingActivity;
import project.android.com.second_app.model.backend.Backend;
import project.android.com.second_app.model.entities.Address;
import project.android.com.second_app.model.entities.Attraction;
import project.android.com.second_app.model.entities.Business;
import project.android.com.second_app.model.entities.Properties;

/**
 * Created by Yair on 2017-01-15.
 */

public class ListDatabase implements Backend {
    static ArrayList<Attraction> attractionList;
    static ArrayList<Business> businessList;
    ContentResolver resolver;
    public static final String currentUri = "content://" + "project.android.com.android5777_9254_6826.model.backend.Provider";

    public ListDatabase(){
        resolver = StartingActivity.ctx.getContentResolver();
    }

    @Override
    public ArrayList<Business> getBusinessList() {
        return businessList;
    }

    private ArrayList<Business> getAsyncListBusineesses() {
        Uri uri = Uri.parse(currentUri+"/"+Business.BUSINESS);
        Cursor Businesses;
        Businesses = resolver.query(uri,null,null,null,null);
        ArrayList<Business> list = getBusinessListFromCursor(Businesses);
        return list;
    }

    @Override
    public ArrayList<Attraction> getAttractionList() {
        return attractionList;
    }

    private ArrayList<Attraction> getAsyncListAttractions() {
        Uri uri = Uri.parse(currentUri+"/"+Attraction.ATTRACTION);
        Cursor atts;
        atts = resolver.query(uri,null,null,null,null);
        ArrayList<Attraction> list = getAttractionListFromCursor(atts);
        return list;
    }

    @Override
    public ArrayList<Business> getBusinessList(String Country) {
        ArrayList<Business> list = businessList;
        ArrayList<Business> toReturn = new ArrayList<>();
        for (Business att:list) {
            if(att.getBusinessAddress().getCountry().equals(Country))
                toReturn.add(att);
        }
        return toReturn;
    }

    @Override
    public ArrayList<Attraction> getAttractionList(Business business) {
        ArrayList<Attraction> list = attractionList;
        ArrayList<Attraction> toReturn = new ArrayList<>();
        for (Attraction att:list) {
            if(att.getBusinessID().equals(business.getBusinessID()))
                toReturn.add(att);
        }
        return toReturn;
    }

    @Override
    public void setUpDatabase() {
        attractionList = getAsyncListAttractions();
        businessList = getAsyncListBusineesses();
    }

    private ArrayList<Attraction> getAttractionListFromCursor(Cursor list) {
        ArrayList<Attraction> toReturn = new ArrayList<>();
        try {
            Cursor ab = list;
            String id,country,desc,bID;
            Properties.AttractionType type;
            String start,end,name;
            float price;

            for(ab.moveToFirst(); !ab.isAfterLast(); ab.moveToNext()) {
                // The Cursor is now set to the right position
                id = ab.getString(ab.getColumnIndex(Attraction.ID));
                type = Properties.Valueof(ab.getString(ab.getColumnIndex(Attraction.TYPE)));
                country = ab.getString(ab.getColumnIndex(Attraction.COUNTRY));
                start = ab.getString(ab.getColumnIndex(Attraction.STARTDATE));
                end = ab.getString(ab.getColumnIndex(Attraction.ENDDATE));
                price = Float.parseFloat(ab.getString(ab.getColumnIndex(Attraction.PRICE)));
                desc = ab.getString(ab.getColumnIndex(Attraction.DESCRIPITION));
                bID = ab.getString(ab.getColumnIndex(Attraction.BUSINESSID));
                name = ab.getString(ab.getColumnIndex(Attraction.NAME));
                toReturn.add(new Attraction(id,type,name,country,start,end,price,desc,bID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
    public ArrayList<Business> getBusinessListFromCursor(Cursor list) {
        ArrayList<Business> toReturn = new ArrayList<>();

        try {
            Cursor ab = list;
            String AccountID;
            String BusinessID;
            String BusinessName;
            Address BusinessAddress;
            String City,Country,Street;
            String Email;
            String Website;

            for(ab.moveToFirst(); !ab.isAfterLast(); ab.moveToNext()) {
                // The Cursor is now set to the right position
                AccountID = ab.getString(ab.getColumnIndex(Business.ACCOUNTID));
                BusinessID = ab.getString(ab.getColumnIndex(Business.ID));
                BusinessName = ab.getString(ab.getColumnIndex(Business.NAME));
                City = ab.getString(ab.getColumnIndex(Address.CITY));
                Country = ab.getString(ab.getColumnIndex(Address.COUNTRY));
                Street = ab.getString(ab.getColumnIndex(Address.STREET));
                Email = ab.getString(ab.getColumnIndex(Business.EMAIL));
                Website = ab.getString(ab.getColumnIndex(Business.WEBSITE));
                BusinessAddress = new Address(Country,City,Street);
                toReturn.add(new Business(AccountID,BusinessID,BusinessName,BusinessAddress,Email,Website));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
