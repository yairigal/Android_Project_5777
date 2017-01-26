package project.android.com.second_app.model.backend;

import java.util.ArrayList;

import project.android.com.second_app.model.entities.Attraction;
import project.android.com.second_app.model.entities.Business;

/**
 * Created by Yair on 2017-01-18.
 */

/**
 * filters the businesses by the search parameters
 */
public class BusinessFilter extends Filter<Business> {

    public BusinessFilter(String input, ArrayList<Business> data) {
        super(input, data);
    }

    @Override
    protected ArrayList<Business> SumFilter(String i, ArrayList<Business> otherRaw) throws Exception {
        ArrayList<Business> sum = new ArrayList<>();
        //clone list
        ArrayList<Business> toRunOn = new ArrayList<>();
        toRunOn.addAll(otherRaw);
        //clone
        sum.addAll(FilterAttributes(i,toRunOn));
        //sum.addAll(TryParseRange(i,toRunOn));
        return sum;
    }

    @Override
    protected ArrayList<Business> FilterAttributes(String i, ArrayList<Business> toRunOn) throws Exception {
        ArrayList<Business> toReturn = new ArrayList<>();
        String[] cols = Business.getColumns();
        for (Business item:toRunOn)
            for (String val:cols)
                if(item.getValue(val).contains(i))
                    toReturn.add(item);
        return toReturn;
    }

/*
    @Override
    protected void TryParse(String i, ArrayList<Predicate<Business>> toReturn) throws Exception {
        TryParseAttribute(i,toReturn);
    }

    @Override
    protected boolean TryParseAttribute(String i, ArrayList<Predicate<Business>> toReturn) {
        return false;
    }*/
}
