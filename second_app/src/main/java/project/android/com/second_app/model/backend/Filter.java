package project.android.com.second_app.model.backend;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Yair on 2017-01-18.
 * The abstract class for filtering content
 */

public abstract class Filter<Input> {
    String searchInput;
    ArrayList<Input> rawData;
    ArrayList<Input> output;

    /**
     * Constructor
     * @param input the query input
     * @param data the data to filter
     */
    public Filter(String input, ArrayList<Input> data) {
        searchInput = input;
        rawData = data;
    }

    /**
     * The main filter function
     * @return the filtered data
     */
    public ArrayList<Input> Filter() {
        ArrayList<Input> finalResults = AnalyzeInputAndFilter();
        //remove Duplicates
        removeDuplicates(finalResults);
        return finalResults;
    }

    /**
     * Removing duplicates from the finalResults
     * @param finalResults the duplicates to remove from
     */
    private void removeDuplicates(ArrayList<Input> finalResults) {
        Set<String> hs = new HashSet<>();
        hs.addAll((Collection<? extends String>) finalResults);
        finalResults.clear();
        finalResults.addAll((Collection<? extends Input>) hs);
    }

    /**
     * Abstract Filtering function
     *
     * eachItemFilter the function to filter like
     * @return the array after all was filtered.
     */
    public ArrayList<Input> filterOr(String[] input) {
        ArrayList<Input> toReturn = new ArrayList<>();
        //output.addAll(rawData);
        for (String i : input)
            try {
                toReturn.addAll(SumFilter(i,rawData));
            } catch (Exception e) {
                String mes = e.getMessage();
            }
        return toReturn;
    }

    /**
     * Filtering the data with AND between the query rules
     * @param input
     * @return
     */
    public ArrayList<Input> filterAnd(String[] input) {
        ArrayList<Input> toReturn = new ArrayList<>();
        ArrayList<Input> filteredList;
        boolean firstRun = true;
        //toReturn.addAll(rawData);
        for (String i : input)
            try {
                if(firstRun){
                    filteredList = SumFilter(i,rawData);
                    toReturn.addAll(filteredList);
                    firstRun = false;
                }
                else {
                    filteredList = SumFilter(i,toReturn);
                    toReturn.clear();
                    toReturn.addAll(filteredList);
                }
            } catch (Exception e) {
            }
        return toReturn;
    }

    /**
     * Analyzes the search input and filters the raw data.
     *
     * @return array of the filtered data corresponds to the input
     */
    private ArrayList<Input> AnalyzeInputAndFilter() {
        String[] input;
        searchInput.trim();
        if (searchInput.contains(",")) {
            input = searchInput.split(",");
            return filterAnd(input);
        } else if (searchInput.contains("|")) {
            input = searchInput.split("|");
            return filterOr(input);
        }
        else {
            return filterOr(new String[]{searchInput});
        }
    }

    /**
     * Filters the otherRaw data with all kinds of filtering methods
     * @param i the query with the filtering commands
     * @param otherRaw the data to filter on
     * @return the filtered data
     * @throws Exception
     */
    protected abstract ArrayList<Input> SumFilter(String i, ArrayList<Input> otherRaw) throws Exception;

    /**
     * Filtering the attributes from the query
     * @param i the search query
     * @param toRunOn the raw data to run filter on
     * @return the filtered data by attributes
     * @throws Exception
     */
    protected abstract ArrayList<Input> FilterAttributes(String i,ArrayList<Input> toRunOn) throws Exception;
}
