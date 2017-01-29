package project.android.com.second_app.model.backend;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import project.android.com.second_app.model.entities.Attraction;

/**
 * Created by Yair on 2017-01-18.
 */

/**
 * filters the attractions by the search input
 */
public class AttractionFilter extends Filter<Attraction> {

    public AttractionFilter(String input, ArrayList<Attraction> data) {
        super(input, data);
    }

    @Override
    protected ArrayList<Attraction> SumFilter(String i, ArrayList<Attraction> exceptList) throws Exception {
        ArrayList<Attraction> sum = new ArrayList<>();
        //clone list
        ArrayList<Attraction> toRunOn = new ArrayList<>();
        toRunOn.addAll(exceptList);
        //clone
        sum.addAll(FilterAttributes(i, toRunOn));
        sum.addAll(TryParseRange(i, toRunOn));
        sum.addAll(TryParseDate(i, toRunOn));
        return sum;
    }

    @Override
    protected ArrayList<Attraction> FilterAttributes(String i, ArrayList<Attraction> toRunOn) throws Exception {
        ArrayList<Attraction> toReturn = new ArrayList<>();
        String[] cols = Attraction.getColumns();
        for (Attraction item : toRunOn)
            for (String val : cols)
                if (item.getValue(val).toLowerCase().contains(i.toLowerCase()))
                    toReturn.add(item);
        return toReturn;
    }

    /**
     * Try to parse the String i to a range argument
     *
     * @param i       the input to parse
     * @param toRunOn this is the output array , if succeeded the answer will be added
     * @return returns if the parse has succeeded
     */
    protected ArrayList<Attraction> TryParseRange(String i, ArrayList<Attraction> toRunOn) throws Exception {
        ArrayList<Attraction> toReturn = new ArrayList<>();
        try {
            String[] range = i.split("-");
            if (range.length > 2)
                throw new Exception("Error parsing");
            final int low = Integer.parseInt(range[0]);
            final int high = Integer.parseInt(range[1]);
            for (Attraction item : toRunOn) {
                float price = item.getPrice();
                if (price >= low && price <= high)
                    toReturn.add(item);
            }
        } catch (Exception ex) {
            return toReturn;
        }
        return toReturn;
    }

    /**
     * Parsing the input and trying to filter it as a date range (dd/mm/yy - dd/mm/yyy)
     * @param i the input query
     * @param toRunOn the list of data to run on
     * @return the filtered list
     * @throws Exception
     */
    protected ArrayList<Attraction> TryParseDate(String i, ArrayList<Attraction> toRunOn) throws Exception {
        String[] dates = i.split("-");
        ArrayList<Attraction> toReturn = new ArrayList<>();
        SimpleDateFormat Simp = new SimpleDateFormat("dd/mm/yy", Locale.US);
        SimpleDateFormat simpe2 = new SimpleDateFormat("dd/mm/yyyy", Locale.US);

        try {
            if (dates.length == 2 && !dates[0].equals("") && !dates[1].equals("")) {
                java.util.Date first = Simp.parse(dates[0]);
                java.util.Date second = Simp.parse(dates[1]);
                for (Attraction item : toRunOn) {
                    java.util.Date s = simpe2.parse(item.getStartDate());
                    java.util.Date e = simpe2.parse(item.getEndDate());
                    if ((first.before(s) || first.compareTo(s) == 0) && (e.before(second) || e.compareTo(second) == 0))
                        toReturn.add(item);
                }
            } else {
                if (dates[0].equals("")) {
                    java.util.Date second = Simp.parse(dates[1]);
                    for (Attraction item : toRunOn) {
                        java.util.Date e = simpe2.parse(item.getEndDate());
                        if ((e.before(second) || e.compareTo(second) == 0))
                            toReturn.add(item);
                    }
                } else {
                    try {
                        java.util.Date first = Simp.parse(dates[0]);
                        for (Attraction item : toRunOn) {
                            java.util.Date s = simpe2.parse(item.getStartDate());
                            if ((first.before(s) || first.compareTo(s) == 0))
                                toReturn.add(item);
                        }
                    } catch (Exception e) {
                        return new ArrayList<>();
                    }
                }
            }
        }catch (Exception e){

        }
        return toReturn;
    }
}
