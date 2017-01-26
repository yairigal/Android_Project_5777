package project.android.com.second_app.model.backend;

import java.util.ArrayList;

import project.android.com.second_app.model.entities.Attraction;
import project.android.com.second_app.model.entities.Business;

/**
 * Created by Yair on 2017-01-15.
 */


public interface Backend {
    ArrayList<Business> getBusinessList();
    ArrayList<Attraction> getAttractionList();
    ArrayList<Business> getBusinessList(String Country);
    ArrayList<Attraction> getAttractionList(Business business);
    void setUpDatabase();
}
