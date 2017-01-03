package project.android.com.android5777_9254_6826.controller;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.backend.Backend;
import project.android.com.android5777_9254_6826.model.backend.FactoryDatabase;
//import project.android.com.android5777_9254_6826.model.backend.LoadingTask;
import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Address;
import project.android.com.android5777_9254_6826.model.entities.Properties;

public class AddBusinessActivity extends AppCompatActivity {

    Backend db;
    TextView BusinessName;
    TextView Street;
    TextView City;
    TextView Country;
    TextView Website;
    TextView CompanyEmail;
    Account currentAccount;
    Context homeactivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_business);
        db = FactoryDatabase.getDatabase();
        BusinessName = (EditText) findViewById(R.id.BusinessNameID);
        Street = (EditText) findViewById(R.id.StreetId);
        City = (EditText) findViewById(R.id.CityId);
        Country = (EditText) findViewById(R.id.CountryId);
        Website = (EditText) findViewById(R.id.WebId);
        CompanyEmail = (EditText) findViewById(R.id.EmailId);
        homeactivity = this;
        currentAccount = (Account) getIntent().getSerializableExtra("account");


        final FloatingActionButton addatt = (FloatingActionButton) findViewById(R.id.AddBusinessbutton);
        addatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (restIsFilledOut()) {
                    final String bname = BusinessName.getText().toString();
                    final String street = Street.getText().toString();
                    final String city = City.getText().toString();
                    final String country = Country.getText().toString();
                    final String w = Website.getText().toString();
                    final String email = CompanyEmail.getText().toString();

                    /**new LoadingTask<Void,Void,Void>(){
                    @Override protected Void backgroundAction(Void... params) {
                    publishProgress();
                    db.addNewBusiness(Long.toString(currentAccount.getAccountNumber()), bname, new Address(country, city, street), email, w);
                    try {
                    Thread.sleep(6000);
                    } catch (InterruptedException e) {
                    e.printStackTrace();
                    }
                    return null;
                    }

                    @Override protected void onProgress() {
                    addatt.setEnabled(false);
                    }

                    @Override protected void preAction() {

                    }

                    @Override protected void postAction() {
                    Toast.makeText(homeactivity, "Business Added!", Toast.LENGTH_SHORT).show();
                    finish();
                    }

                    @Override protected Context getApplicationContext() {
                    return AddBusinessActivity.this;
                    }
                    }.execute();
                     */

                    new AsyncTask<Void, Void, Void>() {
                        ProgressDialog pd = LoginActivity.getProgressInstance(AddBusinessActivity.this);
                        @Override
                        protected Void doInBackground(Void... params) {
                            //this function disables the add button
                            publishProgress();
                            db.addNewBusiness(Long.toString(currentAccount.getAccountNumber()), bname, new Address(country, city, street), email, w);
                            return null;
                        }

                        @Override
                        protected void onProgressUpdate(Void... values) {
                            super.onProgressUpdate(values);
                            addatt.setEnabled(false);
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            super.onPostExecute(aVoid);
                            Toast.makeText(homeactivity, "Business Added!", Toast.LENGTH_SHORT).show();
                            finish();
                            LoginActivity.stopProgressAnimation(pd);
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            LoginActivity.showLoadingAnimation(pd,"Adding Business To Database",ProgressDialog.STYLE_SPINNER);
                        }
                    }.execute();
                    //Intent intent = new Intent(getBaseContext(), BusinessesActivity.class);
                    //intent.putExtra("business", currentbusiness);
                    //intent.putExtra("account", currentacoount);

                } else {
                    Snackbar.make(v, "Your input is not compatible, please check!", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }


    private boolean restIsFilledOut() {

        return BusinessName.getText().toString().length() > 0 &&
                Street.getText().toString().length() > 0 &&
                City.getText().toString().length() > 0 &&
                Country.getText().toString().length() > 0 &&
                Website.getText().toString().length() > 0 &&
                CompanyEmail.getText().toString().length() > 0 &&
                CompanyEmail.getText().toString().contains("@");
    }
}
