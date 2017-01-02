package project.android.com.android5777_9254_6826.controller;

import android.database.Cursor;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.backend.Backend;
import project.android.com.android5777_9254_6826.model.backend.FactoryDatabase;
import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Address;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

public class Test extends AppCompatActivity {

    Backend DB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        DB = FactoryDatabase.getDatabase();

        AsyncTask<Void,Void,Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            Account a;
            @Override
            protected Void doInBackground(Void... params) {
                try {

                    DB.addNewAccount(new Account(1,"test","test"));
                    DB.addNewBusiness(new Business("test","test","test",new Address("test","test","test"),"test","test"));
                    DB.addNewAttraction(new Attraction("test", Properties.AttractionType.Airline,"test","test","test","test",123,"test","test"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return  null;
            }
        };
        asyncTask.execute();

    }
}
