package project.android.com.second_app.model.backend;

import android.content.ContentResolver;

import java.util.ArrayList;

import project.android.com.second_app.model.entities.Attraction;
import project.android.com.second_app.model.entities.Business;

/**
 * Created by Yair on 2017-01-15.
 */

public class ListDatabase implements Backend {
    static ArrayList<Attraction> attractionList = new ArrayList<>();
    static ArrayList<Business> businessList = new ArrayList<>();
    ContentResolver resolver;

    @Override
    public ArrayList<Business> getBusinessList() {

    }

    @Override
    public ArrayList<Attraction> getAttractionList() {
        return null;
    }

    @Override
    public ArrayList<Attraction> getAttractionList(String Country) {
        return null;
    }

    @Override
    public ArrayList<Attraction> getAttractionList(Business business) {
        return null;
    }

    @Override
    public void setUpDatabase() {

    }
}
