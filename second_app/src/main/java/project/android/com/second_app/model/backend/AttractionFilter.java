package project.android.com.second_app.model.backend;

import android.hardware.camera2.TotalCaptureResult;

import java.util.ArrayList;

import project.android.com.second_app.model.entities.Attraction;
import project.android.com.second_app.model.entities.Business;

/**
 * Created by Yair on 2017-01-18.
 */

public class AttractionFilter extends Filter<Attraction> {

    public AttractionFilter(String input, ArrayList<Attraction> data) {
        super(input, data);
    }

    @Override
    protected ArrayList<Attraction> SumFilter(String i,ArrayList<Attraction> exceptList) throws Exception {
        ArrayList<Attraction> sum = new ArrayList<>();
        //clone list
        ArrayList<Attraction> toRunOn = new ArrayList<>();
        toRunOn.addAll(exceptList);
        //clone
        sum.addAll(FilterAttributes(i,toRunOn));
        sum.addAll(TryParseRange(i,toRunOn));
        return sum;
    }

    @Override
    protected ArrayList<Attraction> FilterAttributes(String i,ArrayList<Attraction> toRunOn) throws Exception {
        ArrayList<Attraction> toReturn = new ArrayList<>();
        String[] cols = Attraction.getColumns();
        for (Attraction item:toRunOn)
            for (String val:cols)
                if(item.getValue(val).contains(i))
                    toReturn.add(item);
        return toReturn;
    }

    /**
     * Try to parse the String i to a range argument
     * @param i the input to parse
     * @param toRunOn this is the output array , if succeeded the answer will be added
     * @return returns if the parse has succeeded
     */
    protected ArrayList<Attraction> TryParseRange(String i,ArrayList<Attraction> toRunOn) throws Exception {
        ArrayList<Attraction> toReturn = new ArrayList<>();
        try {
            String[] range = i.split("-");
            if(range.length > 2)
                throw new Exception("Error parsing");
            final int low = Integer.parseInt(range[0]);
            final int high = Integer.parseInt(range[1]);
            for (Attraction item:toRunOn) {
                float price = item.getPrice();
                if(price>=low && price<=high)
                    toReturn.add(item);
            }
        }
        catch (Exception ex){
            return toReturn;
        }
        return toReturn;
    }
}
