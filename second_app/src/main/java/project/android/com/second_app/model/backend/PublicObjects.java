package project.android.com.second_app.model.backend;

import project.android.com.second_app.controller.AttractionsListFragment;
import project.android.com.second_app.controller.BusinessesListFragment;
import project.android.com.second_app.controller.StartingActivity;

/**
 * Created by Yair on 2017-01-16.
 */

public class PublicObjects {

    public static StartingActivity start = null;
    public static AttractionsListFragment AttFrag = null;
    public static BusinessesListFragment BussFrag = null;

    public static AttractionsListFragment getAttractionFragment(){
        if(AttFrag == null)
            AttFrag = new AttractionsListFragment();
        return AttFrag;
    }
    public static BusinessesListFragment getBusinessFragment(){
        if(BussFrag == null)
            BussFrag = new BusinessesListFragment();
        return BussFrag;
    }
}
