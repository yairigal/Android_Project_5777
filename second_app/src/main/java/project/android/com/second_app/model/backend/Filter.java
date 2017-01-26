package project.android.com.second_app.model.backend;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import project.android.com.second_app.model.entities.Attraction;

/**
 * Created by Yair on 2017-01-18.
 * The abstract class for filtering content
 */

public abstract class Filter<Input> {
    String searchInput;
    ArrayList<Input> rawData;
    ArrayList<Input> output;

    public Filter(String input, ArrayList<Input> data) {
        searchInput = input;
        rawData = data;
    }

    public ArrayList<Input> Filter() {
        ArrayList<Input> finalResults = AnalyzeInputAndFilter();
        //remove Duplicates
        removeDuplicates(finalResults);
        return finalResults;
    }

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
            }
        return toReturn;
    }

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

    protected abstract ArrayList<Input> SumFilter(String i, ArrayList<Input> otherRaw) throws Exception;

    protected abstract ArrayList<Input> FilterAttributes(String i,ArrayList<Input> toRunOn) throws Exception;
}
