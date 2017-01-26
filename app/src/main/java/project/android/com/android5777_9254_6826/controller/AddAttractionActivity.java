package project.android.com.android5777_9254_6826.controller;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appdatasearch.GetRecentContextCall;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.backend.Backend;
import project.android.com.android5777_9254_6826.model.backend.FactoryDatabase;
import project.android.com.android5777_9254_6826.model.backend.Provider;
import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

import static android.R.attr.type;

@RequiresApi(api = Build.VERSION_CODES.N)
public class AddAttractionActivity extends AppCompatActivity {


    Backend db;

    TextView attractionName;
    TextView Country;
    TextView StartDate;
    TextView EndDate;
    TextView Description;
    TextView Price;
    Spinner spinner;
    String type;
    Attraction att;
    int TextIDClicked;
    Calendar myCalendar = Calendar.getInstance();
    Account currentacoount;

    Context homeactivity;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = FactoryDatabase.getDatabase();
        homeactivity = this;
        setContentView(R.layout.activity_add_attraction);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        final Business currentbusiness = getBusinessFromIntent();

        attractionName = (EditText) findViewById(R.id.AttractionName);
        Country = (EditText) findViewById(R.id.AttractionCountry);

        StartDate = (EditText) findViewById(R.id.AttStartDate);
        EndDate = (EditText) findViewById(R.id.AttEndDate);

        Description = (EditText) findViewById(R.id.AttDescription);
        Price = (EditText) findViewById(R.id.AttPrice);
        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                Properties.getTypes());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                type = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                type = parent.getItemAtPosition(0).toString();

            }
        });
        datePickerPopup();


        // popup for calendar
        /** StartDate.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override public void onClick(View v) {
        TextIDClicked = v.getId();
        //showPopup(AddAttractionActivity.this);
        datePickerPopup();

        }
        });
         EndDate.setOnClickListener(new View.OnClickListener() {
        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override public void onClick(View v) {
        TextIDClicked = v.getId();
        //showPopup(AddAttractionActivity.this);
        datePickerPopup();

        }
        });
         */
        final FloatingActionButton addatt = (FloatingActionButton) findViewById(R.id.AddAttButton);
        addatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if ( restIsFilledOut() && datesAreOK() ) {
                    final Properties.AttractionType ty =Properties.AttractionType.valueOf(type);
                    final String attname =attractionName.getText().toString();
                    final String country =Country.getText().toString();
                    final String Std =StartDate.getText().toString();
                    final String End =EndDate.getText().toString();
                    final Float price =  Float.valueOf(Price.getText().toString());
                    final String disc = Description.getText().toString();
                    final String id = currentbusiness.getBusinessID();

                    new AsyncTask<Void, Void, Void>() {
                        ProgressDialog pd = LoginActivity.getProgressInstance(AddAttractionActivity.this);

                        @Override
                        protected Void doInBackground(Void... params) {
                            //this function disables the add button
                            publishProgress();
                            db.addNewAttraction(ty,attname,country,Std,End,price,disc,id);
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
                            LoginActivity.stopProgressAnimation(pd);
                            Toast.makeText(homeactivity, "Attraction Added!", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        protected void onPreExecute() {
                            super.onPreExecute();
                            LoginActivity.showLoadingAnimation(pd,"Adding Attraction",ProgressDialog.STYLE_SPINNER);

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

    /**
     * check if the details for a attraction that has been filled out are acceptable
     * @return
     */
    private boolean restIsFilledOut() {
        return
                type.length() > 0 &&
                attractionName.getText().toString().length() > 0 &&
                Country.getText().toString().length() > 0 &&
                StartDate.getText().toString().length() > 0 &&
                EndDate.getText().toString().length() > 0 &&
                Price.getText().toString().length() > 0 &&
                Description.getText().toString().length() > 0;

    }

    /**
     * check if the dates inserted are acceptable
     * @return
     */
    private boolean datesAreOK() {
        String[] strt = StartDate.getText().toString().split("/");
        String[] end = EndDate.getText().toString().split("/");
        java.text.DateFormat format = new java.text.SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        Date start = null,ende = null;
        try {
            start = format.parse(StartDate.getText().toString());
            ende = format.parse(EndDate.getText().toString());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Date d = new Date();
        try {
            if(ende.after(start) || ende.equals(start))
                if(start.after(d))
                    return true;
            return false;
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    private void showPopup(Activity context) {

        // Inflate the popup_layout.xml
        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout;
        layout = layoutInflater.inflate(R.layout.content_add_attraction, null, false);
        // Creating the PopupWindow
        final PopupWindow popupWindow = new PopupWindow(
                layout, 400, 400);

        popupWindow.setContentView(layout);
        popupWindow.setHeight(500);
        popupWindow.setOutsideTouchable(false);
        // Clear the default translucent background
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAtLocation(layout, Gravity.TOP, 5, 170);
    }

    /**
     * PICK UR dates from a date picker
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void datePickerPopup() {
        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(TextIDClicked);
            }

        };

        StartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddAttractionActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                TextIDClicked = StartDate.getId();
            }
        });
        EndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                new DatePickerDialog(AddAttractionActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                TextIDClicked = EndDate.getId();
            }
        });
    }
    /**
     * dates from picker to textview
     */
    private
    @RequiresApi(api = Build.VERSION_CODES.N)
  void updateLabel(int TextViewID) {

        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        if (TextViewID == StartDate.getId())
            StartDate.setText(sdf.format(myCalendar.getTime()));
        else
            EndDate.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * go back to businesses activity
     * @return
     */
    private Business getBusinessFromIntent() {
        Intent intent = getIntent();
        Business toReturn = (Business) intent.getSerializableExtra("business");
        currentacoount = (Account) intent.getSerializableExtra("account");
        return toReturn;
    }
}


