package project.android.com.android5777_9254_6826.controller;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.IntentService;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import project.android.com.android5777_9254_6826.R;
import project.android.com.android5777_9254_6826.model.backend.Backend;
import project.android.com.android5777_9254_6826.model.backend.FactoryDatabase;
import project.android.com.android5777_9254_6826.model.backend.service;
import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    /**
     * shared preferences to save login details
     */
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    public static final String Password = "passKey";
    public static final String Email = "emailKey";
    public static ProgressDialog progressDialog;
    String email, password;
    Account currentAccount;
    Backend DB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        DB = FactoryDatabase.getDatabase();
        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);

        sharedpreferences = getSharedPreferences(mypreference,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(Password)) {
            mPasswordView.setText(sharedpreferences.getString(Password, ""));
        }
        if (sharedpreferences.contains(Email)) {
            mEmailView.setText(sharedpreferences.getString(Email, ""));

        }

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    new UserLoginTask(mEmailView.getText().toString(), mPasswordView.getText().toString()).execute();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                new UserLoginTask(mEmailView.getText().toString(), mPasswordView.getText().toString()).execute();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
        //add this to the main activity.
        startService(new Intent(getBaseContext(), service.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        email = mEmailView.getText().toString();
        password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);
        }
    }

    /**
     * checks if the email contains a @
     * @param email
     * @return
     */
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        //ping email domain to check if valid.
        return email.contains("@");
    }

    /**
     * checks password
     * @param password
     * @return
     */
    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;
        private String toToast;
        private boolean flag = true;
        Business[] listarr;
        SplashScreen splashScreen;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("LoginAsyncTask", "preExecute");
            //progressDialog = LoginActivity.getProgressInstance(LoginActivity.this);
            //showLoadingAnimation(progressDialog,"Loading...",ProgressDialog.STYLE_SPINNER);
/*            Intent inte = new Intent(LoginActivity.this,SplashScreen.class);
            inte.putExtra("text","Logging In...");
            startActivity(inte);*/
            //StaticDeclarations.showLoadingScreen(LoginActivity.this,"Logging in...");
            //StaticDeclarations.showSplashScreen(LoginActivity.this,"Loading...");
            (findViewById(R.id.pBarLogin)).setVisibility(View.VISIBLE);

        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            Toast.makeText(getApplicationContext(), toToast, Toast.LENGTH_SHORT).show();
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

//            try {
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
//
//            for (String credential : DUMMY_CREDENTIALS) {
//                String[] pieces = credential.split(":");
//                if (pieces[0].equals(mEmail)) {
//                    // Account exists, return true if the password matches.
//                    return pieces[1].equals(mPassword);
//                }
//            }

            // TODO: register the new account here
            //calling Login();
            Log.d("LoginAsyncTask", "doInBackground");
            switch (Login2(mEmail, mPassword)) {
                //OK
                case 1:
                    try {
                        currentAccount = DB.getAccount(mEmail);
                    } catch (Exception e) {
                        toToast = "Error adding account";
                        flag = false;
                    }
                    toToast = "- Logged in -";
                    //publishProgress();
                    flag = true;
                    break;
                //FAIL TO LOGIN
                case 0:
                    toToast = "- Wrong password -";
                    //publishProgress();
                    flag = false;
                    break;
                //NOT REGISTERED
                default:
                    try {
                        DB.addNewAccount(mEmail, mPassword);
                    } catch (Exception ex) {
                        Snackbar.make(getCurrentFocus(), "Check your internet conncetion", Snackbar.LENGTH_LONG).show();
                    }

                    try {
                        currentAccount = DB.getAccount(mEmail);
                    } catch (Exception e) {
                        //Toast.makeText(getApplicationContext(), "Error Adding Account", Toast.LENGTH_SHORT).show();
                        toToast = "Error adding account";
                        flag = false;
                    }
                    break;
            }
/*            if(flag)
                try {
                    listarr = getList(DB.getBusinessList(Long.toString(currentAccount.getAccountNumber())));
                } catch (Exception e) {
                    listarr = null;
                }*/
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            Log.d("LoginAsyncTask", "postExecute");
            //showProgress(false);
            //if logged in
            if (flag) {
                SaveSharedpreferences();
                //stopProgressAnimation(progressDialog);
                //StaticDeclarations.hideProgress();
                //StaticDeclarations.hideSplashScreen(LoginActivity.this,R.layout.activity_login);
                //SplashScreen.hideSplashScreen();
                //StaticDeclarations.hideLoadingScreen();
                (findViewById(R.id.pBarLogin)).setVisibility(View.GONE);
                IntentNextActivity(listarr);
                Toast.makeText(getApplicationContext(), toToast, Toast.LENGTH_SHORT).show();
                return;
            } else {
                //stopProgressAnimation(progressDialog);
                //StaticDeclarations.hideProgress();
                //StaticDeclarations.hideSplashScreen(LoginActivity.this,R.layout.activity_login);
                //SplashScreen.hideSplashScreen();
                //StaticDeclarations.hideLoadingScreen();
                (findViewById(R.id.pBarLogin)).setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), toToast, Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            //showProgress(false);
        }

        private void Login(String email, String pass) {

            try {
                //AccountListDB DB = AccountListDB.getDB();

                //if registered - log in
                //Thread.sleep(6000);
                currentAccount = DB.verifyPassword(email, pass);
                if (currentAccount != null) {
                    toToast = "- Logged in -";
                    //publishProgress();
                    flag = true;
                } else {
                    toToast = "- Wrong password -";
                    //publishProgress();
                    flag = false;
                }
            } catch (Exception ex) {
                //ast.makeText(getApplicationContext(),ex.toString(),Toast.LENGTH_SHORT);
                //if couldn't find the account - register
                DB.addNewAccount(email, pass);
                try {
                    currentAccount = DB.getAccount(email);
                } catch (Exception e) {
                    //Toast.makeText(getApplicationContext(), "Error Adding Account", Toast.LENGTH_SHORT).show();
                    toToast = "Error adding account";
                    flag = false;
                    return;
                }
                flag = true;
                toToast = "- Registered -";
                //publishProgress();
            }
        }

        //returns 1 - OK
        //        0 - WRONG PASSWORD
        //       -1 - NOT REGISTERED
        private int Login2(String email, String pass) {
            try {
                Account thisAccount = DB.getAccount(email);
                //if pass is ok
                if (thisAccount.getPassword().equals(pass))
                    return 1;
                else
                    return 0;
            } catch (Exception e) {
                //we got here only if the account is not registered
                return -1;
            }
        }

        private void SaveSharedpreferences() {
            String n = mPasswordView.getText().toString();
            String e = mEmailView.getText().toString();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(Password, n);
            editor.putString(Email, e);
            editor.commit();
        }

    }

    /**
     * go to next activity
     * @param arr
     */
    private void IntentNextActivity(Business[] arr) {

//        AsyncTask<Void,Void,Void> as = new AsyncTask<Void, Void, Void>() {
//            @Override
//            protected Void doInBackground(Void... params) {
        try {
            Intent Bus = new Intent(LoginActivity.this, BusinessesActivity.class);
            Bus.putExtra("array", arr);
            Bus.putExtra("account", currentAccount);
            startActivity(Bus);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //return null;
//            }
//        };
//        as.execute();

    }

    private Business[] getBusinessesListAsyncTask() {
        Business[] toReturn = null;
        AsyncTask<Void, Void, Business[]> as = new AsyncTask<Void, Void, Business[]>() {
            //View progressOverlay;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //LoginActivity.showLoadingAnimation(progressDialog, "Loading Businesses...", ProgressDialog.STYLE_SPINNER);
                //progressOverlay = findViewById(R.id.progress_overlay);
                //Properties.animateView(progressOverlay, View.VISIBLE, 0.4f, 200);
                StaticDeclarations.showProgress(LoginActivity.this, "Loading...");
            }

            @Override
            protected void onPostExecute(Business[] businesses) {
                super.onPostExecute(businesses);
                //LoginActivity.stopProgressAnimation(progressDialog);
                //Properties.animateView(progressOverlay, View.GONE, 0, 200);
                StaticDeclarations.hideProgress();
            }

            @Override
            protected Business[] doInBackground(Void... params) {
                try {
                    Thread.sleep(5000);
                    return getList(DB.getBusinessList(Long.toString(currentAccount.getAccountNumber())));
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
        as.execute();
        try {
            toReturn = as.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(this, service.class));
    }

    /**
     * get list of businesses
     * @param bs
     * @return
     */
    private Business[] getList(ArrayList<Business> bs) {
        Business[] toReturn = new Business[bs.size()];
        for (int i = 0; i < bs.size(); i++) {
            toReturn[i] = bs.get(i);
        }
        return toReturn;
    }

    public static void showLoadingAnimation(ProgressDialog progDailog, String msg, int style) {
        progDailog.setMessage(msg);
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(style);
        progDailog.setCancelable(false);
        progDailog.show();
    }

    public static ProgressDialog getProgressInstance(Context currentActivity) {
        return new ProgressDialog(currentActivity);
    }

    public static void stopProgressAnimation(ProgressDialog progDailog) {
        progDailog.dismiss();
    }
}




