package project.android.com.second_app.model.backend;

import project.android.com.second_app.model.datasource.ListDatabase;

/**
 * Created by Yair on 2017-01-15.
 */

public class BackendFactory {
    static Backend instance = null;
    public static Backend getFactoryDatabase(){
        if(instance == null)
            instance = new ListDatabase();
        return instance;
    }
}
