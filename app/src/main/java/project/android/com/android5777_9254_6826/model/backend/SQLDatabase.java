package project.android.com.android5777_9254_6826.model.backend;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.webkit.URLUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Address;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

import static project.android.com.android5777_9254_6826.model.backend.Constraints.WEB_URL;
import static project.android.com.android5777_9254_6826.model.backend.Constraints.addAccount;
import static project.android.com.android5777_9254_6826.model.backend.Constraints.addAttraction;
import static project.android.com.android5777_9254_6826.model.backend.Constraints.addBusiness;
import static project.android.com.android5777_9254_6826.model.backend.Constraints.getAccounts;
import static project.android.com.android5777_9254_6826.model.backend.Constraints.getAttractions;

/**
 * Created by Arele-PC on 12/27/2016.
 */

public class SQLDatabase implements Backend {

    private boolean latelyAddedNewAttraction = false;
    private boolean latelyAddedNewBusiness = false;

    public SQLDatabase(){

    }

    //region Interface Functions
    @Override
    public int addNewAccount(String UserName, String Password) {
        try {
            Map<String, Object> params = new LinkedHashMap<>();

            params.put(Account.ID, null);
            params.put(Account.USERNAME, UserName);
            params.put(Account.PASSWORD, Password);
            String results = POST(WEB_URL + addAccount, params);
            if(results.equals("")){
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        Account a = null;
        try {
            a = getAccount(UserName);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return Integer.parseInt(String.valueOf(a.getAccountNumber()));
    }
    @Override
    public int addNewAccount(Account toInsert) {
        return addNewAccount(toInsert.getUserName(),toInsert.getPassword());
    }
    @Override
    public ArrayList<Account> getAccountList() {
        ArrayList<Account> toReturn = new ArrayList<>();
        try {
            Cursor ab = getAccountCursor();
            String un,pw;
            long id;

            ArrayList<Account> mArrayList = new ArrayList<Account>();
            for(ab.moveToFirst(); !ab.isAfterLast(); ab.moveToNext()) {
                // The Cursor is now set to the right position
                un = ab.getString(ab.getColumnIndex(Account.USERNAME));
                pw = ab.getString(ab.getColumnIndex(Account.PASSWORD));
                id = Long.parseLong(ab.getString(ab.getColumnIndex(Account.ID)));
                toReturn.add(new Account(id,un,pw));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
    @Override
    public Cursor getAccountCursor() throws Exception {
        MatrixCursor accountCursor = new MatrixCursor(new String[]{Account.ID,Account.USERNAME,Account.PASSWORD });

//        for (int i =0; i < accountList.size();i++){
//            acc = accountList.get(i);
//            accountCursor.addRow(new Object[]{acc.getAccountNumber(),acc.getUserName(),acc.getPassword()});
//        }
//        return accountCursor;
//        MatrixCursor agenciesCursor = new MatrixCursor(new String[]{"_ID", "Name", "Country", "City", "Street", "HouseNumber", "PhoneNumber", "Email"});
//
        JSONArray array = new JSONObject(GET(WEB_URL+getAccounts)).getJSONArray(Account.ACCOUNT);
        for (int i = 0; i < array.length(); i++) {
            JSONObject account = array.getJSONObject(i);
            accountCursor.addRow(new Object[]{
                    account.getString(Account.ID),
                    account.getString(Account.USERNAME),
                    account.getString(Account.PASSWORD)});
        }
        return accountCursor;
    }
    @Override
    public Account getAccount(long id) throws Exception {
        ArrayList<Account> list = getAccountList();
        for (Account curr:list) {
            if(curr.getAccountNumber() == id){
                return curr;
            }

        }
        throw new Exception("Account not found");
    }
    @Override
    public Account getAccount(String username) throws Exception {
        Cursor accounts = getAccountCursor();
        accounts.moveToFirst();
        while (!accounts.isAfterLast()) {
            String accountsString = accounts.getString(1);
            if(accountsString.equals(username)){
                return new Account(Long.parseLong(accounts.getString(0)),accounts.getString(1),accounts.getString(2));
            }
            accounts.moveToNext();
        }
        return null;
    }
    @Override
    public boolean isRegistered(String userName) {
        try {
            Account curr = getAccount(userName);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    @Override
    public Account verifyPassword(String userName, String passToCheck) throws Exception {
        Account account= getAccount(userName);
        if(account.getPassword().equals(passToCheck))
            return account;
        throw new Exception("Password is incorrect");
    }
    @Override
    public int removeAccount(String username) {
        //TODO need to implement removeAccount
        return 0;
    }
    @Override
    public int removeAccount(int rowID) {
        //TODO need to implement removeAccount
        return 0;
    }
    @Override
    public Uri insert(Account ac) {
        int id = addNewAccount(ac);
        String a = String.valueOf(id);
        return Uri.parse(a);
    }
    @Override
    public int addNewAttraction(Properties.AttractionType Type, String AttractionName, String Country, String StartDate, String EndDate, float Price, String Description, String BusinessID) {
        String id = null;
        Attraction insert = new Attraction(id,Type,AttractionName,Country,StartDate,EndDate,Price,Description,BusinessID);
        return addNewAttraction(insert);
    }
    @Override
    public int addNewAttraction(Attraction toInsert) {
        try {
            Map<String, Object> params = new LinkedHashMap<>();

            params.put(Attraction.TYPE, toInsert.getType());
            params.put(Attraction.COUNTRY, toInsert.getCountry());
            params.put(Attraction.STARTDATE, toInsert.getEndDate());
            params.put(Attraction.ENDDATE, toInsert.getEndDate());
            params.put(Attraction.PRICE, toInsert.getPrice());
            params.put(Attraction.DESCRIPITION, toInsert.getDescription());
            params.put(Attraction.BUSINESSID, toInsert.getBusinessID());
            params.put(Attraction.ID, toInsert.getAttractionID());
            params.put(Attraction.NAME, toInsert.getAttractionName());
            String results = POST(WEB_URL + addAttraction, params);
            if(results.equals("")){
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        latelyAddedNewAttraction = true;

        Attraction a = null;

        try {
            a = getAttraction(toInsert.getBusinessID(),toInsert.getAttractionName());
        } catch (Exception e) {
            return -1;
        }
        //returns attraction id
        return Integer.parseInt(a.getAttractionID());
    }
    @Override
    public ArrayList<Attraction> getAttractionList() {
        ArrayList<Attraction> toReturn = new ArrayList<>();
        try {
            Cursor ab = getAttractionCursor();
            String id,country,desc,bID;
            Properties.AttractionType type;
            String start,end,name;
            float price;

            for(ab.moveToFirst(); !ab.isAfterLast(); ab.moveToNext()) {
                // The Cursor is now set to the right position
                id = ab.getString(ab.getColumnIndex(Attraction.ID));
                type = Properties.Valueof(ab.getString(ab.getColumnIndex(Attraction.TYPE)));
                country = ab.getString(ab.getColumnIndex(Attraction.COUNTRY));
                start = ab.getString(ab.getColumnIndex(Attraction.STARTDATE));
                end = ab.getString(ab.getColumnIndex(Attraction.ENDDATE));
                price = Float.parseFloat(ab.getString(ab.getColumnIndex(Attraction.PRICE)));
                desc = ab.getString(ab.getColumnIndex(Attraction.DESCRIPITION));
                bID = ab.getString(ab.getColumnIndex(Attraction.BUSINESSID));
                name = ab.getString(ab.getColumnIndex(Attraction.NAME));
                toReturn.add(new Attraction(id,type,name,country,start,end,price,desc,bID));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
    @Override
    public ArrayList<Attraction> getAttractionList(String BusinessID) {
        ArrayList<Attraction> list = getAttractionList();
        ArrayList<Attraction> toReturn = new ArrayList<>();
        for (Attraction curr:list) {
            if(curr.getBusinessID().equals(BusinessID))
                toReturn.add(curr);
        }
        return toReturn;
    }
    @Override
    public Cursor getAttractionCursor() throws Exception {

        MatrixCursor attractionCursor = new MatrixCursor(
                new String[]{
                        Attraction.ID,
                        Attraction.TYPE,
                        Attraction.COUNTRY,
                        Attraction.STARTDATE,
                        Attraction.ENDDATE,
                        Attraction.PRICE,
                        Attraction.DESCRIPITION,
                        Attraction.BUSINESSID,
                        Attraction.NAME});

        JSONArray array = new JSONObject(GET(WEB_URL + getAttractions)).getJSONArray(Attraction.ATTRACTION);
        for (int i = 0; i < array.length(); i++) {
            JSONObject attraction = array.getJSONObject(i);
            attractionCursor.addRow(new Object[]{
                    attraction.getString(Attraction.ID),
                    attraction.getString(Attraction.TYPE),
                    attraction.getString(Attraction.COUNTRY),
                    attraction.getString(Attraction.STARTDATE),
                    attraction.getString(Attraction.ENDDATE),
                    attraction.getString(Attraction.PRICE),
                    attraction.getString(Attraction.DESCRIPITION),
                    attraction.getString(Attraction.BUSINESSID),
                    attraction.getString(Attraction.NAME)
            });
        }
        return attractionCursor;
    }
    @Override
    public Attraction getAttraction(String attractionID) throws Exception {
        ArrayList<Attraction> list = getAttractionList();
        for (Attraction a:list) {
            if(a.getAttractionID().equals(attractionID))
                return a;
        }
        throw new Exception("No Attraction Found");
    }
    @Override
    public Attraction getAttraction(String BusinessID, String AttrationName) throws Exception {
        ArrayList<Attraction> list = getAttractionList();
        for (Attraction curr:list) {
            if(curr.getBusinessID().equals(BusinessID) && curr.getAttractionName().equals(AttrationName))
                return curr;

        }
        throw new Exception("No Attraction Found");
    }
    @Override
    public boolean ifNewAttractionAdded() {
        if (latelyAddedNewAttraction) {
            latelyAddedNewAttraction = false;
            return true;
        }
        return false;
    }
    @Override
    public int removeAttraction(String attractionID) {
        //TODO need to implement removeAttraction
        return 0;
    }
    @Override
    public int removeAttraction(int rowID) {
        //TODO need to implement removeAttraction
        return 0;
    }
    @Override
    public Uri insert(Attraction ac) {
        int id = addNewAttraction(ac);
        String a = String.valueOf(id);
        return Uri.parse(a);
    }
    @Override
    public int addNewBusiness(String accountID, String Name, Address address, String Email, String Website) {
        try {
            Map<String, Object> params = new LinkedHashMap<>();

            params.put(Business.ACCOUNTID, accountID);
            params.put(Business.NAME, Name);
            params.put(Address.CITY, address.getCity());
            params.put(Address.COUNTRY, address.getCountry());
            params.put(Address.STREET, address.getStreet());
            params.put(Business.EMAIL, Email);
            params.put(Business.WEBSITE, Website);
            params.put(Business.ID,null);

            String results = POST(WEB_URL + addBusiness, params);
            if(results.equals("")){
                throw new Exception("An error occurred on the server's side");
            }
            if (results.substring(0, 5).equalsIgnoreCase("error")) {
                throw new Exception(results.substring(5));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        Business a = null;
        try {
            a = getBusiness(accountID,Name);
        } catch (Exception e) {
            e.printStackTrace();
        }

        latelyAddedNewBusiness = true;
        //returns attraction id
        return Integer.parseInt(String.valueOf(a.getBusinessID()));
    }

    /**
     *
     * @param accountID
     * @param name
     * @return Business from database with the params
     * @throws Exception if business not found
     */
    private Business getBusiness(String accountID, String name) throws Exception {
        ArrayList<Business> list = getBusinessList();
        for (Business curr:list) {
            if(curr.getAccountID().equals(accountID) && curr.getBusinessName().equals(name))
                return curr;
        }
        throw new Exception("Business Not Found");
    }

    @Override
    public int addNewBusiness(Business toInsert) {
        return addNewBusiness(
                toInsert.getAccountID(),
                toInsert.getBusinessName(),
                toInsert.getBusinessAddress(),
                toInsert.getEmail(),
                toInsert.getWebsite());
    }
    @Override
    public ArrayList<Business> getBusinessList() {
        ArrayList<Business> toReturn = new ArrayList<>();

        try {
            Cursor ab = getBusinessCursor();
            String AccountID;
            String BusinessID;
            String BusinessName;
            Address BusinessAddress;
            String City,Country,Street;
            String Email;
            String Website;

            for(ab.moveToFirst(); !ab.isAfterLast(); ab.moveToNext()) {
                // The Cursor is now set to the right position
                AccountID = ab.getString(ab.getColumnIndex(Business.ACCOUNTID));
                BusinessID = ab.getString(ab.getColumnIndex(Business.ID));
                BusinessName = ab.getString(ab.getColumnIndex(Business.NAME));
                City = ab.getString(ab.getColumnIndex(Address.CITY));
                Country = ab.getString(ab.getColumnIndex(Address.COUNTRY));
                Street = ab.getString(ab.getColumnIndex(Address.STREET));
                Email = ab.getString(ab.getColumnIndex(Business.EMAIL));
                Website = ab.getString(ab.getColumnIndex(Business.WEBSITE));
                BusinessAddress = new Address(Country,City,Street);
                toReturn.add(new Business(AccountID,BusinessID,BusinessName,BusinessAddress,Email,Website));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
    @Override
    public Cursor getBusinessCursor() throws Exception {
        Business bus;
        MatrixCursor businessCursor = new MatrixCursor(
                new String[]{Business.ACCOUNTID,Business.ID,Business.NAME, Address.CITY,Address.COUNTRY,Address.STREET,
                        Business.EMAIL,Business.WEBSITE});

        JSONArray array = new JSONObject(GET(WEB_URL + Constraints.getBusinesses)).getJSONArray(Business.BUSINESS);
        for (int i = 0; i < array.length(); i++) {
            JSONObject agency = array.getJSONObject(i);
            businessCursor.addRow(new Object[]{
                    agency.getString(Business.ACCOUNTID),
                    agency.getString(Business.ID),
                    agency.getString(Business.NAME),
                    agency.getString(Address.CITY),
                    agency.getString(Address.COUNTRY),
                    agency.getString(Address.STREET),
                    agency.getString(Business.EMAIL),
                    agency.getString(Business.WEBSITE)
            });
        }
        return businessCursor;
    }
    @Override
    public boolean ifNewBusinessAdded() {
        if (latelyAddedNewBusiness) {
            latelyAddedNewBusiness = false;
            return true;
        }
        return false;
    }
    @Override
    public Business getBusiness(String businessID) throws Exception {
        ArrayList<Business> list = getBusinessList();
        for (Business curr:list) {
            if(curr.getBusinessID().equals(businessID))
                return curr;
        }
        throw new Exception("Business Not Found");
    }
    @Override
    public int removeBusiness(String businessID) {
        //TODO need to implement removeBusiness
        return 0;
    }
    @Override
    public int removeBusiness(int rowID) {
        //TODO need to implement removeBusiness
        return 0;
    }
    @Override
    public Uri insert(Business ac) {
        int id = addNewBusiness(ac);
        String a = String.valueOf(id);
        return Uri.parse(a);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        //TODO need to implement delete
        return 0;
    }
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        //TODO need to implement update
        return 0;
    }
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //TODO need to implement query
        return null;
    }
    @Override
    public ArrayList<Business> getBusinessList(String AcID) throws Exception {
        ArrayList<Business> toReturn = new ArrayList<>();

        try {
            Cursor ab = getBusinessCursor();
            String AccountID;
            String BusinessID;
            String BusinessName;
            Address BusinessAddress;
            String City,Country,Street;
            String Email;
            String Website;

            for(ab.moveToFirst(); !ab.isAfterLast(); ab.moveToNext()) {
                // The Cursor is now set to the right position
                AccountID = ab.getString(ab.getColumnIndex(Business.ACCOUNTID));
                BusinessID = ab.getString(ab.getColumnIndex(Business.ID));
                BusinessName = ab.getString(ab.getColumnIndex(Business.NAME));
                City = ab.getString(ab.getColumnIndex(Address.CITY));
                Country = ab.getString(ab.getColumnIndex(Address.COUNTRY));
                Street = ab.getString(ab.getColumnIndex(Address.STREET));
                Email = ab.getString(ab.getColumnIndex(Business.EMAIL));
                Website = ab.getString(ab.getColumnIndex(Business.WEBSITE));
                BusinessAddress = new Address(Country,City,Street);
                if(AccountID.equals(AcID))
                    toReturn.add(new Business(AccountID,BusinessID,BusinessName,BusinessAddress,Email,Website));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
    //endregion
    //region other functions
    public Cursor getAttractionCursor(String BusinessID) throws Exception {
        MatrixCursor attractionCursor = new MatrixCursor(
                new String[]{Attraction.ID,Attraction.TYPE, Attraction.COUNTRY,
                        Attraction.STARTDATE,Attraction.ENDDATE,Attraction.PRICE,Attraction.DESCRIPITION,Attraction.BUSINESSID,Attraction.NAME});
        Cursor attractionlist = getAttractionCursor();
        attractionlist.moveToFirst();
        while (!attractionlist.isAfterLast()) {
            String accountIdString = attractionlist.getString(7);
            if(accountIdString.equals(BusinessID)){
                attractionCursor.addRow(new Object[]{
                        attractionlist.getString(0),
                        attractionlist.getString(1),
                        attractionlist.getString(2),
                        attractionlist.getString(3),
                        attractionlist.getString(4),
                        attractionlist.getString(5),
                        attractionlist.getString(6),
                        attractionlist.getString(7),
                        attractionlist.getString(8)
                });
            }
            attractionlist.moveToNext();
        }
        return attractionCursor;


    }
    public Cursor CgetBusinessList(String AccountID) throws Exception {
        MatrixCursor businescursor = new MatrixCursor(  new String[]{Business.ACCOUNTID,Business.ID,Business.NAME, Address.CITY,Address.COUNTRY,Address.STREET,
                Business.EMAIL,Business.WEBSITE});
        Cursor businessList = getBusinessCursor();
        businessList.moveToFirst();
        while (!businessList.isAfterLast()) {
            String accountIdString = businessList.getString(0);
            if(accountIdString.equals(AccountID)){
                businescursor.addRow(new Object[]{
                        businessList.getString(0),
                        businessList.getString(1),
                        businessList.getString(2),
                        businessList.getString(3),
                        businessList.getString(4),
                        businessList.getString(5),
                        businessList.getString(6),
                        businessList.getString(7)
                });
            }
            businessList.moveToNext();
        }
        return businescursor;
    }
    //endregion
    //region Web Connection Functions

    /**
     *
     * @param url to connenct to the php server
     * @return the output of the php function called
     * @throws Exception when no connection
     */
    private static String GET(String url) throws Exception {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setDoOutput(true);
        if (con.getResponseCode() == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            return response.toString();
        } else {
            return "";
        }
    }

    /**
     *
     * @param url  to connenct to the php server
     * @param params an object to store in the database
     * @return string that successfull or error
     * @throws IOException
     */
    private static String POST(String url, Map<String,Object> params) throws IOException {

        //Convert Map<String,Object> into key=value&key=value pairs.
        StringBuilder postData = new StringBuilder();
        for (Map.Entry<String,Object> param : params.entrySet()) {
            if (postData.length() != 0) postData.append('&');
            postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
            postData.append('=');
            postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
        }

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");

        // For POST only - START
        con.setDoOutput(true);
        OutputStream os = con.getOutputStream();
        os.write(postData.toString().getBytes("UTF-8"));
        os.flush();
        os.close();
        // For POST only - END

        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            return response.toString();
        }
        else return "";
    }
    //endregion
}
