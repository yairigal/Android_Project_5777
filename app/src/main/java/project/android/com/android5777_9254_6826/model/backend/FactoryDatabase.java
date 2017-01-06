package project.android.com.android5777_9254_6826.model.backend;

import project.android.com.android5777_9254_6826.model.datasource.IDatabase;
import project.android.com.android5777_9254_6826.model.entities.Delegate;

/**
 * Created by Yair on 2016-11-27.
 */

public class FactoryDatabase {

    private static Backend instance = null;
    static Delegate<Backend> currentDatabase = new Delegate() {
        @Override
        public Backend function() {
            return getSQLDatabase();
        }
    };

    public static Backend getDatabase(){
        return currentDatabase.function();
    }
    private static Backend getListDatabase(){
        if (instance == null)
            instance = new ListDatabase();
        return instance;
    }
    private static Backend getSQLDatabase(){
        if (instance == null)
            instance = new SQLDatabase();
        return instance;
    }

}
