package project.android.com.android5777_9254_6826.model.backend;

import android.content.ContentValues;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Address;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

/**
 * Created by Yair on 2016-12-05.
 */

public class ContentValuesSerializer {
    /**
     * Converts a contentValue to an Account object
     * @param values
     * @return
     */
    public static Account contentValuesToAccount(ContentValues values){
        Account toReturn = new Account(values.getAsLong("accountnumber"),values.getAsString("username"),
                values.getAsString("password"));
        return toReturn;
    }
    /**
     * Converts an account object to ConventValue object
     * @param account
     * @return
     */
    public static ContentValues accountToContentValues(Account account){
        ContentValues values = new ContentValues();
        values.put("username",account.getUserName());
        values.put("password",account.getPassword());
        values.put("accountnumber",account.getAccountNumber());
        return values;
    }
    /**
     * Converts a contentValue to an Attraction object
     * @param values
     * @return
     */
    public static Attraction contentValuesToAttraction(ContentValues values){
        DateFormat format = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Date dates = null,datee = null;

        Attraction toReturn = new Attraction(
                values.getAsString("attractionid"),
                (Properties.AttractionType.valueOf(values.getAsString("type"))),
                values.getAsString("attractionname"),
                values.getAsString("country"),
                values.getAsString("startdate"),
                values.getAsString("startdate"),
                values.getAsFloat("price"),
                values.getAsString("description"),
                values.getAsString("businessid"));
        return toReturn;
    }
    /**
     * Converts an Attraction object to ConventValue object
     * @param attraction
     * @return
     */
    public static ContentValues attractionToContentValues(Attraction attraction){
        ContentValues values = new ContentValues();
        values.put("attractionid",attraction.getAttractionID());
        //saving the enum as String
        values.put("type",attraction.getType().toString());
        values.put("country",attraction.getCountry());
        values.put("startdate",attraction.getStartDate().toString());
        values.put("enddate",attraction.getEndDate().toString());
        values.put("price",attraction.getPrice());
        values.put("description",attraction.getDescription());
        values.put("businessid",attraction.getBusinessID());
        return values;
    }
    /**
     * Converts a contentValue to an Business object
     * @param values
     * @return
     */
    public static Business contentValuesToBusiness(ContentValues values) throws MalformedURLException {
        Address adr = new Address();
        adr.setCity(values.getAsString("city"));
        adr.setCountry(values.getAsString("country"));
        adr.setStreet(values.getAsString("street"));

        String url = values.getAsString("url");

        Business toReturn = new Business(
                values.getAsString("accountID"),
                values.getAsString("id"),
                values.getAsString("name"),
                adr,
                values.getAsString("email"),
                url);
        return toReturn;
    }
    /**
     * Converts an Business object to ConventValue object
     * @param business
     * @return
     */
    public static ContentValues businessToContentValues(Business business){
        ContentValues values = new ContentValues();
        values.put("accountID",business.getAccountID());
        values.put("id",business.getBusinessID());
        values.put("name",business.getBusinessName());
        values.put("country",business.getBusinessAddress().getCountry());
        values.put("city",business.getBusinessAddress().getCity());
        values.put("street",business.getBusinessAddress().getStreet());
        values.put("email",business.getEmail());
        values.put("url",business.getWebsite().toString());
        return values;
    }

    //region not used
    public static String getValue(Business current,String Col) throws Exception {
        switch (Col){
            case Business.ACCOUNTID:
                return current.getAccountID();
            case Address.COUNTRY:
                return current.getBusinessAddress().getCountry();
            case Address.CITY:
                return current.getBusinessAddress().getCity();
            case Address.STREET:
                return current.getBusinessAddress().getStreet();
            case Business.ID:
                return current.getBusinessID();
            case Business.EMAIL:
                return current.getEmail();
            case Business.NAME:
                return current.getBusinessName();
            case Business.WEBSITE:
                return current.getWebsite();
            default:
                throw new Exception("Column doesn't Exist");
        }
    }
    public static String getValue(Attraction current,String Col) throws Exception {
        switch (Col){
            case Attraction.BUSINESSID:
                return current.getBusinessID();
            case Attraction.TYPE:
                return current.getType().toString();
            case Attraction.COUNTRY:
                return current.getCountry();
            case Attraction.STARTDATE:
                return current.getStartDate();
            case Attraction.ENDDATE:
                return current.getEndDate();
            case Attraction.PRICE:
                return String.valueOf(current.getPrice());
            case Attraction.DESCRIPITION:
                return current.getDescription();
            case Attraction.ID:
                return current.getAttractionID();
            case Attraction.NAME:
                return current.getAttractionName();
            default:
                throw new Exception("Column doesn't Exist");
        }
    }
    public static String getValue(Account current,String Col) throws Exception {
        switch (Col){
            case Account.USERNAME:
                return current.getUserName();
            case Account.PASSWORD:
                return current.getPassword();
            case Account.ID:
                return String.valueOf(current.getAccountNumber());
            default:
                throw new Exception("Column doesn't Exist");
        }
    }
    //endregion
}
