package project.android.com.android5777_9254_6826.model.backend;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import project.android.com.android5777_9254_6826.model.datasource.IAccountDatabase;
import project.android.com.android5777_9254_6826.model.datasource.IAttractionDatabase;
import project.android.com.android5777_9254_6826.model.datasource.IBusinessDatabase;
import project.android.com.android5777_9254_6826.model.entities.Account;
import project.android.com.android5777_9254_6826.model.entities.Address;
import project.android.com.android5777_9254_6826.model.entities.Attraction;
import project.android.com.android5777_9254_6826.model.entities.Business;
import project.android.com.android5777_9254_6826.model.entities.Properties;

/**
 * Created by Yair on 2016-11-27.
 */

public class ListDatabase implements Backend {

    private static ArrayList<Account> accountList = new ArrayList<Account>();
    private static ArrayList<Business> businessList = new ArrayList<Business>();
    private static ArrayList<Attraction> attractionsList = new ArrayList<Attraction>();
    private static long AccountNumber = 0;
    private boolean latelyAddedNewAttraction;
    private boolean latelyAddedNewBusiness;


    @Override
    public int addNewAccount(String UserName, String Password) {
        Account a = new Account(++AccountNumber, UserName, Password);
        accountList.add(a);
        return accountList.indexOf(a);
    }

    @Override
    public int addNewAccount(Account toInsert) {
        accountList.add(toInsert);
        return accountList.indexOf(toInsert);
    }

    @Override
    public ArrayList<Account> getAccountList() {
        return accountList;
    }

    @Override
    public Account getAccount(long id) throws Exception {
        Account curr = null;
        //running on the list trying to find.
        for (int i = 0; i < accountList.size(); i++) {
            curr = accountList.get(i);
            if (curr.AccountNumber == id)
                break;
        }
        if (curr == null)
            throw new Exception("Cannot find this Account");
        return curr;
    }

    @Override
    public Account getAccount(String username) throws Exception {
        Account curr = null;
        //running on the list trying to find.
        for (int i = 0; i < accountList.size(); i++) {
            curr = accountList.get(i);
            if (curr.UserName.equals(username))
                break;
        }
        if (curr == null)
            throw new Exception("Cannot find this Account");
        return curr;
    }

    @Override
    public boolean isRegistered(String userName) {
        Account curr = null;
        //running on the list trying to find.
        for (int i = 0; i < accountList.size(); i++) {
            curr = accountList.get(i);
            if (curr.UserName.equals(userName))
                return true;
        }
        return false;
    }

    @Override
    public boolean verifyPassword(String userName, String passToCheck) throws Exception {
        Account curr = getAccount(userName);
        if (curr.Password.equals(passToCheck))
            return true;
        return false;
    }

    @Override
    public int removeAccount(String username) {
        return 0;
    }

    @Override
    public int removeAccount(int rowID) {
        try {
            accountList.remove(rowID);
            return rowID;
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * @param Type
     * @param Country
     * @param StartDate
     * @param EndDate
     * @param Price
     * @param Description
     * @param BusinessID
     * @return returns true if succeeded , else false;
     */
    @Override
    public int addNewAttraction(String ID, Properties.AttractionType Type, String Country, Date StartDate, Date EndDate, float Price, String Description, String BusinessID) {
        Attraction a = new Attraction(ID, Type, Country, StartDate, EndDate, Price, Description, BusinessID);
        attractionsList.add(a);
        latelyAddedNewAttraction = true;
        return attractionsList.indexOf(a);
    }

    @Override
    public int addNewAttraction(Attraction toInsert) {
        attractionsList.add(toInsert);
        return attractionsList.indexOf(toInsert);
    }

    /**
     * @return list of the attraction in the database
     */
    @Override
    public ArrayList<Attraction> getAttractionList() {
        return attractionsList;
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
        return 0;
    }

    @Override
    public int removeAttraction(int rowID) {
        try {
            attractionsList.remove(rowID);
            return rowID;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public int addNewBusiness(String ID, String Name, Address address, String Email, URL Website) {
        Business a = new Business(ID, Name, address, Email, Website);
        businessList.add(a);
        latelyAddedNewBusiness = true;
        return businessList.indexOf(a);
    }

    @Override
    public int addNewBusiness(Business toInsert) {
        businessList.add(toInsert);
        return businessList.indexOf(toInsert);
    }

    @Override
    public ArrayList<Business> getBusinessList() {
        return businessList;
    }

    /**
     * checks if lately a new Business has been added.
     *
     * @return
     */
    @Override
    public boolean ifNewBusinessAdded() {
        if (latelyAddedNewBusiness) {
            latelyAddedNewBusiness = false;
            return true;
        }
        return false;
    }

    @Override
    public int removeBusiness(String businessID) {
        return 0;
    }

    @Override
    public int removeBusiness(int rowID) {
        try {
            businessList.remove(rowID);
            return rowID;
        } catch (Exception e) {
            throw e;
        }
    }

}
