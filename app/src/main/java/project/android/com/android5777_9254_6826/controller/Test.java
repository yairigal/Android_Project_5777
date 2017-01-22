package project.android.com.android5777_9254_6826.controller;

import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.net.Proxy;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.backend.Backend;
import project.android.com.android5777_9254_6826.model.backend.FactoryDatabase;
import project.android.com.android5777_9254_6826.model.backend.Provider;
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
        Button button = (Button) findViewById(R.id.testBtn);
        final TextView tv = (TextView) findViewById(R.id.TestTV);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 final Uri uri = Uri.parse(Provider.currentUri+"/Accounts");
                final ContentResolver provider = getContentResolver();
                try {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... params) {
                            Cursor account = null;
                            account = provider.query(uri,null,"UserName",new String[] {"yair@igal"},null);
                            return null;
                        }
                    }.execute();

                } catch (Exception e) {
                    Log.d("Error",e.getMessage());
                }
                //tv.setText(account.getString(account.getColumnIndex("Password")));
            }
        });


    }
}
