package project.android.com.android5777_9254_6826.controller;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.zip.Inflater;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.backend.Backend;
import project.android.com.android5777_9254_6826.model.backend.FactoryDatabase;
import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Address;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

public class BusinessesActivity extends AppCompatActivity {

    Backend db;
    LinearLayout layout;
    Account currentAccount;
    static Business[] BusinessArray = new Business[0];
    boolean cameFromSplashScreen = false;
    ArrayAdapter<Business> adapter = null;

    //region Override functions
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //startActivity(new Intent(this,SplashScreen.class));
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_businesses);
        getAccountfromIntent();
        //getBusinessesFromIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        CollapsingToolbarLayout cbar = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        setSupportActionBar(toolbar);
        db = FactoryDatabase.getDatabase();
        layout = (LinearLayout) findViewById(R.id.nestedScrollView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToAddBusinessActivity();
            }
        });
        cbar.setTitle("Businesses");
        initItemByListView();
        //tempAddBusinessses();
        //initItemByListView();  <-- in onResume
        //onResume ^^
    }

    @Override
    protected void onResume() {
        super.onResume();
        getBusinessesListAsyncTask();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    //endregion

    //region private functions
    private void getAccountfromIntent() {
        currentAccount = (Account) getIntent().getSerializableExtra("account");
    }

    private void getBusinessesFromIntent(){
        BusinessArray = (Business[]) getIntent().getSerializableExtra("array");
    }

    private void moveToAddBusinessActivity() {
        Intent toBuss = new Intent(getBaseContext(), AddBusinessActivity.class);
        toBuss.putExtra("account", currentAccount);
        startActivity(toBuss);
        cameFromSplashScreen = false;
    }

    private void moveToTheNextActivity(Business currBuss) {
        Intent toBuss = new Intent(getBaseContext(), BusinessActivity.class);
        startActivity(toBuss);
    }

    void initItemByListView() {
        final Business[] myItemList = BusinessArray;
/*        if (myItemList.length == 0) {
            Toast.makeText(getApplicationContext(), "No Businesses Found", Toast.LENGTH_LONG).show();
            return;
        }*/
        ListView lv = (ListView) BusinessesActivity.this.findViewById(R.id.itemsLV);
        //lv.setDivider(null);
        //lv.setDividerHeight(0);
        adapter = new ArrayAdapter<Business>(this, R.layout.single_business_layout, myItemList) {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public View getView(final int position, View convertView, ViewGroup parent) {
                if (convertView == null)
                    convertView = View.inflate(BusinessesActivity.this, R.layout.single_business_layout, null);

                setBusinessFields(position, convertView, myItemList);
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        moveToBusinessActivity(myItemList[position]);
                    }
                });
                return convertView;
            }
        };
        lv.setAdapter(adapter);

        /*LayoutInflater inflater = LayoutInflater.from(this);
        for (int i = 0;i<myItemList.length;i++) {
            Business item = myItemList[i];
            View view  = inflater.inflate(R.layout.single_business_layout, layout, false);
            TextView Name = (TextView) view.findViewById(R.id.tvName);
            TextView ID = (TextView) view.findViewById(R.id.tvID);
            Name.setText(myItemList[i].getBusinessName());
            ID.setText(myItemList[i].getBusinessID());
            layout.addView(view);
        }*/
        Log.d("InitITemByListView","END");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void setBusinessFields(final int position, final View convertView, final Business[] myItemList) {
        Business curr = myItemList[position];
        /**TextView Name = (TextView) convertView.findViewById(R.id.tvName);
         TextView address = (TextView) convertView.findViewById(R.id.tvaddre);
         TextView email = (TextView) convertView.findViewById(R.id.tvEmail);
         Name.setText(curr.getBusinessName());
         address.setText(curr.getBusinessAddress().toString());
         email.setText(curr.getEmail());*/
        TextView name = (TextView) convertView.findViewById(R.id.notification_title);
        TextView Description = (TextView) convertView.findViewById(R.id.notification_text);
        name.setText(curr.getBusinessName());
        Description.setText(curr.getBusinessAddress().toString());

        //convertView.findViewById(R.id.custom_notification).setBackgroundColor(getRandomColor());

        Button remove = (Button) convertView.findViewById(R.id.removeBtn);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteThisCurrentBusiness(myItemList[position], convertView);
            }
        });
    }

    private int getRandomColor() {
        Random r = new Random();
        switch (r.nextInt(3)) {
            case 0:
                return Color.RED;
            case 1:
                return Color.CYAN;
            case 2:
                return Color.RED;
            default:
                return Color.GREEN;
        }
    }

    private void deleteThisCurrentBusiness(final Business curr, final View v) {
        new AsyncTask<Void, Void, Void>() {
            ProgressDialog pd = LoginActivity.getProgressInstance(BusinessesActivity.this);

            @Override
            protected Void doInBackground(Void... params) {
                db.removeBusiness(curr.getBusinessID());
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                LoginActivity.showLoadingAnimation(pd, "Deleting business", ProgressDialog.STYLE_SPINNER);
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                LoginActivity.stopProgressAnimation(pd);
                Snackbar.make(v, "Business Deleted", Snackbar.LENGTH_SHORT).show();
                initItemByListView();
            }
        }.execute();
    }

    private void getBusinessesListAsyncTask() {
        Business[] toReturn = null;
        AsyncTask<Void, Void,Void> as = new AsyncTask<Void, Void,Void>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Log.d("BusinessesAsyncTask","PreExecute - START");
                //LoginActivity.showLoadingAnimation(pd, "Loading Businesses...", ProgressDialog.STYLE_SPINNER);
                //StaticDeclarations.showSplashScreen(BusinessesActivity.this,"Loading Businesses...");
  /*              Intent inte = new Intent(getBaseContext(),SplashScreen.class);
                inte.putExtra("text","Loading Businesses...");
                startActivityForResult(inte,5);*/
                //StaticDeclarations.showLoadingScreen(BusinessesActivity.this,"Loading Businesses...");
                (findViewById(R.id.itemsLV)).setVisibility(View.GONE);
                (findViewById(R.id.progressBar)).setVisibility(View.VISIBLE);
                cameFromSplashScreen = true;
                Log.d("BusinessesAsyncTask","PreExecute - END");

            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d("BusinessesAsyncTask","PostExecute - START");
                //LoginActivity.stopProgressAnimation(pd);
                //Properties.animateView(progressOverlay, View.GONE, 0, 200);
                //StaticDeclarations.hideProgress();
                //StaticDeclarations.hideSplashScreen(BusinessesActivity.this,R.layout.activity_businesses);
                //finishActivity(5);

                //initItemByListView();
                if(BusinessArray.length != 0)
                    initItemByListView();

                //StaticDeclarations.hideLoadingScreen();
                (findViewById(R.id.progressBar)).setVisibility(View.GONE);
                (findViewById(R.id.itemsLV)).setVisibility(View.VISIBLE);
                cameFromSplashScreen = true;
                Log.d("BusinessesAsyncTask","PostExecute - END");

            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    Log.d("BusinessesAsyncTask","doInBackground - START");
                    setArray(getList(db.getBusinessList(Long.toString(currentAccount.getAccountNumber()))));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Log.d("BusinessesAsyncTask","doInBackground - END");
                return null;

            }
        };
        as.execute();
    }

    private Business[] getList(ArrayList<Business> bs) {
        Business[] toReturn = new Business[bs.size()];
        for (int i = 0; i < bs.size(); i++) {
            toReturn[i] = bs.get(i);
        }
        return toReturn;
    }

    private void tempAddBusinessses() {
        db.addNewBusiness(Long.toString(currentAccount.getAccountNumber()), "Moti Luhim ", new Address("israel", "israel", "rishon"), "adaw@gamil.com", null);
        db.addNewBusiness(Long.toString(currentAccount.getAccountNumber()), "Asaf Lots", new Address("israel", "israel", "rishon"), "adaw@gamil.com", null);
        db.addNewBusiness(Long.toString(currentAccount.getAccountNumber()), "Sami Saviv", new Address("israel", "israel", "rishon"), "adaw@gamil.com", null);
        db.addNewBusiness(Long.toString(currentAccount.getAccountNumber()), "Avi Ron", new Address("israel", "israel", "rishon"), "adaw@gamil.com", null);
        db.addNewBusiness(Long.toString(currentAccount.getAccountNumber()), "Eli Kopter", new Address("israel", "israel", "rishon"), "adaw@gamil.com", null);
        db.addNewBusiness(Long.toString(currentAccount.getAccountNumber()), "Simha Mutsim", new Address("israel", "israel", "rishon"), "adaw@gamil.com", null);
    }

    private void moveToBusinessActivity(Business toSend) {
        Intent intent = new Intent(getBaseContext(), BusinessDeatilsActivity.class);
        intent.putExtra("business", toSend);
        intent.putExtra("account", currentAccount);
        startActivity(intent);
        cameFromSplashScreen = false;
    }
    //endregion

    public static void setArray(Business [] lst){
        BusinessArray = lst;
    }

}
