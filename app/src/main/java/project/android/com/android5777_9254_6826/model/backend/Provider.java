package project.android.com.android5777_9254_6826.model.backend;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;

public class Provider extends ContentProvider {

    public static final String currentUri = "content://" + "project.android.com.android5777_9254_6826.model.backend.Provider";
    //public static final String currentUri = "content://" + ".model.backend.Provider";
    Uri thisUri = Uri.parse(currentUri);
    //this is a sample of a Uri to the database :
    //currentUri+"/Business/1";
    //currentUri+"/Attractions/1";
    //currentUri+"/Accounts/1";
    static UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
    Backend db = FactoryDatabase.getDatabase();
    final int ACCOUNTS = 0, BUSINESS = 1, ATTRACTIONS = 2, ACCOUNTS_ID = 3, BUSINESS_ID = 4, ATTRACTIONS_ID = 5;


    @Override
    public boolean onCreate() {
        initMatcher();
        return true;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        int id = -1;
        try {
            switch (matcher.match(uri)) {
                case ACCOUNTS:
                    Account curr = ContentValuesSerializer.contentValuesToAccount(values);
                    id = db.addNewAccount(curr);
                    break;
                case BUSINESS:
                    Business buss = ContentValuesSerializer.contentValuesToBusiness(values);
                    id = db.addNewBusiness(buss);
                    break;
                case ATTRACTIONS:
                    Attraction att = ContentValuesSerializer.contentValuesToAttraction(values);
                    id = db.addNewAttraction(att);
                    break;
            }
        } catch (Exception e) {
            return Uri.withAppendedPath(uri, "-1");
        }
        return Uri.withAppendedPath(uri, String.valueOf(id));
    }

    /**
     * @param uri           - URI/ACCOUNTS = accounts with selection
     *                      - URI/ACCOUNTS/ID = specific account with that id.
     * @param projection    - not implemented
     * @param selection     - COLUMNNAME,COLUMNANAME,...
     * @param selectionArgs - "test,"test"test" - as the order of the selection
     * @param sortOrder     - not implemented
     * @return - Cursor with the objects
     * selection[i] == selectionArgs[i];
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d("Provider","InQuery");
        try {
            int a = getLastSegment(uri);
            switch (a) {
                case ACCOUNTS:
                    return getMultipleAccountCursor(selection, selectionArgs);
                case BUSINESS:
                    return getMultipleBusinessCursor(selection, selectionArgs);
                case ATTRACTIONS:
                    return getMultipleAttractionCursor(selection, selectionArgs);
                //1 account
                case ACCOUNTS_ID:
                    return getSingleAccountCursor(uri);
                //1 business
                case BUSINESS_ID:
                    return getSingleBusinessCursor(uri);
                //1 attraction
                case ATTRACTIONS_ID:
                    return getSingleAttractionCursor(uri);
                default:
                    return null;
            }
        } catch (Exception e) {
                return null;
        }
//        String token = uri.getLastPathSegment();
//        switch (matcher.match(uri)) {
//            case ACCOUNTS:
//                return db.CgetAccountList();
//            case BUSINESS:
//                return db.CgetBusinessList();
//            case ATTRACTIONS:
//                return db.CgetAttractionList();
//            case ACCOUNTS_ID:
//                try {
//
//                    Account acc = db.getAccount(token);
//                    MatrixCursor acccursor = new MatrixCursor(new String[]{"AccountNumber", "UserName", "Password"});
//                    acccursor.addRow(new Object[]{acc.getAccountNumber(), acc.getUserName(), acc.getPassword()});
//                } catch (Exception e) {
//                }
//                break;
//            case BUSINESS_ID:
//
//                try {
//                    Business bus = db.getBusiness(token);
//
//                    MatrixCursor buscursor = new MatrixCursor(
//                            new String[]{"BusinessID", "BusinessName", "BusinessAddress",
//                                    "Email", "Website"});
//                    buscursor.addRow(new Object[]{bus.getBusinessID(), bus.getBusinessName(),
//                            bus.getBusinessAddress(), bus.getEmail(), bus.getWebsite()});
//                    return buscursor;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            case ATTRACTIONS_ID:
//
//                Attraction att = null;
//                try {
//                    att = db.getAttraction(token);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                MatrixCursor attcursor = new MatrixCursor(
//                        new String[]{"AttractionID", "Type", "Country",
//                                "StartDate", "EndDate", "Price", "Description", "BusinessID"});
//                attcursor.addRow(new Object[]{att.getAttractionID(), att.getType(),
//                        att.getCountry(), att.getStartDate(), att.getEndDate(),
//                        att.getPrice(), att.getDescription(), att.getBusinessID()});
//                return attcursor;
//            default:
//                return null;
//        }
    }

    @NonNull
    private Cursor getMultipleAttractionCursor(String selection, String[] selectionArgs) throws Exception {
        MatrixCursor matrix;
        ArrayList<Attraction> list = db.getAttractionList();
        //return all
        if(selection == (null) || selectionArgs == (null)){
            matrix = new MatrixCursor(Attraction.getColumns());
            for (Attraction cur:list)
                matrix.addRow(cur.getAttributes());
            return matrix;
        }
        String[] selec = selection.split(",");
        matrix = new MatrixCursor(Attraction.getColumns());
        for (Attraction curr : list)
            if (passSelection(selec, selectionArgs, curr))
                matrix.addRow(curr.getAttributes());
        return matrix;
    }

    @NonNull
    private Cursor getMultipleBusinessCursor(String selection, String[] selectionArgs) throws Exception {
        MatrixCursor matrix;
        ArrayList<Business> list = db.getBusinessList();
        //return all
        if(selection == null || selectionArgs == (null)){
            matrix = new MatrixCursor(Business.getColumns());
            for (Business cur:list) {
                matrix.addRow(cur.getAttributes());
            }
            return matrix;
        }
        String[] selec = selection.split(",");
        matrix = new MatrixCursor(Business.getColumns());
        for (Business curr : list)
            if (passSelection(selec, selectionArgs, curr))
                matrix.addRow(curr.getAttributes());
        return matrix;
    }

    @NonNull
    private Cursor getMultipleAccountCursor(String selection, String[] selectionArgs) throws Exception {
        MatrixCursor matrix;
        ArrayList<Account> list = db.getAccountList();
        //return all
        if(selection == null || selectionArgs == null){
            matrix = new MatrixCursor(Account.getColumns());
            for (Account cur:list) {
                matrix.addRow(cur.getAttributes());
            }
            return matrix;
        }

        String[] selec = selection.split(",");
        matrix = new MatrixCursor(Account.getColumns());
        for (Account curr : list)
            if (passSelection(selec, selectionArgs, curr))
                matrix.addRow(curr.getAttributes());
        return matrix;
    }

    private boolean passSelection(String[] selec, String[] selectionArgs, Account curr) throws Exception {
        if (selec.length != selectionArgs.length)
            throw new Exception("Number of selection arguments should be the same");

        for (int i = 0; i < selec.length; i++)
            //if that object column != the parameter -> didnt pass selection
            if (!curr.getValue(selec[i]).equals(selectionArgs[i]))
                return false;
        //if finished all conditions -> passed selection
        return true;
    }

    private boolean passSelection(String[] selec, String[] selectionArgs, Business curr) throws Exception {
        if (selec.length != selectionArgs.length)
            throw new Exception("Number of selection arguments should be the same");

        for (int i = 0; i < selec.length; i++)
            //if that object column != the parameter -> didnt pass selection
            if (!curr.getValue(selec[i]).equals(selectionArgs[i]))
                return false;
        //if finished all conditions -> passed selection
        return true;
    }

    private boolean passSelection(String[] selec, String[] selectionArgs, Attraction curr) throws Exception {
        if (selec.length != selectionArgs.length)
            throw new Exception("Number of selection arguments should be the same");

        for (int i = 0; i < selec.length; i++)
            //if that object column != the parameter -> didnt pass selection
            if (!curr.getValue(selec[i]).equals(selectionArgs[i]))
                return false;
        //if finished all conditions -> passed selection
        return true;
    }

    @NonNull
    private Cursor getSingleAttractionCursor(Uri uri) throws Exception {
        MatrixCursor matrix;
        matrix = new MatrixCursor(Attraction.getColumns());
        String id = (uri.getLastPathSegment());
        Attraction thisAccount = db.getAttraction(id);
        matrix.addRow(thisAccount.getAttributes());
        return matrix;
    }

    @NonNull
    private Cursor getSingleBusinessCursor(Uri uri) throws Exception {
        MatrixCursor matrix;
        matrix = new MatrixCursor(Business.getColumns());
        String id = (uri.getLastPathSegment());
        Business thisAccount = db.getBusiness(id);
        matrix.addRow(thisAccount.getAttributes());
        return matrix;
    }

    @NonNull
    private Cursor getSingleAccountCursor(Uri uri) throws Exception {
        MatrixCursor matrix;
        matrix = new MatrixCursor(Account.getColumns());
        int id = Integer.parseInt(uri.getLastPathSegment());
        Account thisAccount = db.getAccount(id);
        matrix.addRow(thisAccount.getAttributes());
        return matrix;
    }

    private void initMatcher() {
        matcher.addURI(currentUri, "Accounts", ACCOUNTS);
        matcher.addURI(currentUri, "Business", BUSINESS);
        matcher.addURI(currentUri, "Attractions", ATTRACTIONS);
        matcher.addURI(currentUri, "Accounts/*", ACCOUNTS_ID);
        matcher.addURI(currentUri, "Business/*", BUSINESS_ID);
        matcher.addURI(currentUri, "Attractions/*", ATTRACTIONS_ID);
    }

    private int getLastSegment(Uri uri) throws Exception {
        String last = uri.toString();
        String[] vars = last.split("/");
            switch (vars[3]) {
                case Account.ACCOUNT:
                    if (vars.length == 4)
                        return ACCOUNTS;
                    else
                        return ACCOUNTS_ID;
                case Business.BUSINESS:
                    if (vars.length == 4)
                        return BUSINESS;
                    else
                        return BUSINESS_ID;
                case Attraction.ATTRACTION:
                    if (vars.length == 4)
                        return ATTRACTIONS;
                    else
                        return ATTRACTIONS_ID;
                    default:
                        throw new Exception("Match ERROR");
            }
    }



    //region no need to implement yet
    //no need to implement yet
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        int match = matcher.match(uri);
        int rowID = 0;
        int rowNum;
        switch (match) {
            case ATTRACTIONS_ID:
                rowNum = Integer.parseInt(uri.getLastPathSegment());
                rowID = db.removeAttraction(rowNum);
                break;
            case BUSINESS_ID:
                rowNum = Integer.parseInt(uri.getLastPathSegment());
                rowID = db.removeBusiness(rowNum);
                break;
            case ACCOUNTS_ID:
                rowNum = Integer.parseInt(uri.getLastPathSegment());
                rowID = db.removeAccount(rowNum);
                break;
            default:
                return -1;
        }
        return rowID;

    }

    //no need to implement yet
    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //no need to implement yet
    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        delete(uri, selection, selectionArgs);
        Uri newUri = Uri.parse(uri.getPath());
        Uri ret = insert(newUri, values);
        int row = Integer.parseInt(ret.getLastPathSegment());
        return row;
    }
    //endregion


}
