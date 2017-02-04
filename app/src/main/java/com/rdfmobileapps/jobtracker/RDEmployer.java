package com.rdfmobileapps.jobtracker;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by rich on 1/14/17.
 */

public class RDEmployer implements Parcelable {
    private int mId;
    private String mEmployerName;
    private String mContactName;
    private String mStreetAddress;
    private String mCity;
    private String mState;
    private String mZipCode;
    private String mPhoneCell;
    private String mPhoneAlt;
    private String mEmail1;
    private String mEmail2;
    private String mWebsite;
    private RDStatus mStatus;
    private String mNotes;

    private static String[] allColumns = { MyDB.COL_EMPLOYERS_ID, MyDB.COL_EMPLOYERS_EMPLOYERNAME,
        MyDB.COL_EMPLOYERS_CONTACTNAME, MyDB.COL_EMPLOYERS_STREETADDRESS, MyDB.COL_EMPLOYERS_CITY,
        MyDB.COL_EMPLOYERS_STATE, MyDB.COL_EMPLOYERS_ZIPCODE, MyDB.COL_EMPLOYERS_PHONECELL,
        MyDB.COL_EMPLOYERS_PHONEALT, MyDB.COL_EMPLOYERS_EMAIL1, MyDB.COL_EMPLOYERS_EMAIL2,
        MyDB.COL_EMPLOYERS_WEBSITE, MyDB.COL_EMPLOYERS_STATUS, MyDB.COL_EMPLOYERS_NOTES };

//  Constructors

    public RDEmployer() {
        super();
        doSetup();
    }

//  Public Methods

    public boolean save(MyDB pDB) {
        boolean retVal = false;
        if ( mId != RDConstants.NOSELECTION ) {
            retVal = update(pDB);
        } else {
            retVal = insert(pDB);
        }
        return retVal;
    }

    public boolean delete(MyDB pDB) {
        SQLiteDatabase database = pDB.getWritableDatabase();
        String tables = MyDB.TBL_EMPLOYERS;
        String where = MyDB.COL_EMPLOYERS_ID + " = ?";
        String[] whereArgs = { Integer.toString(mId) };
        boolean retVal = false;
        try {
            database.delete(tables, where, whereArgs);
            retVal = true;
        } catch (SQLiteException e) {
            Log.e("RDEmployer.delete", e.getMessage());
        }
        return retVal;
    }

//  Private Methods
    
    private void doSetup() {
        setDefaults();
    }
    
    private void setDefaults() {
        mId = RDConstants.NOSELECTION;
        mEmployerName = "";
        mContactName = "";
        mStreetAddress = "";
        mCity = "";
        mState = "";
        mZipCode = "";
        mPhoneCell = "";
        mPhoneAlt = "";
        mEmail1 = "";
        mEmail2 = "";
        mWebsite = "";
        mStatus = RDStatus.Active;
        mNotes = "";
    }

    private ContentValues addContentValues(boolean pIncludeKey) {
        ContentValues values = new ContentValues();
        if ( pIncludeKey ) {
            values.put(MyDB.COL_EMPLOYERS_ID, mId);
        }
        values.put(MyDB.COL_EMPLOYERS_EMPLOYERNAME, mEmployerName);
        values.put(MyDB.COL_EMPLOYERS_CONTACTNAME, mContactName);
        values.put(MyDB.COL_EMPLOYERS_STREETADDRESS, mStreetAddress);
        values.put(MyDB.COL_EMPLOYERS_CITY, mCity);
        values.put(MyDB.COL_EMPLOYERS_STATE, mState);
        values.put(MyDB.COL_EMPLOYERS_ZIPCODE, mZipCode);
        values.put(MyDB.COL_EMPLOYERS_ZIPCODE, mPhoneCell);
        values.put(MyDB.COL_EMPLOYERS_ZIPCODE, mPhoneAlt);
        values.put(MyDB.COL_EMPLOYERS_ZIPCODE, mEmail1);
        values.put(MyDB.COL_EMPLOYERS_ZIPCODE, mEmail2);
        values.put(MyDB.COL_EMPLOYERS_ZIPCODE, mWebsite);
        values.put(MyDB.COL_EMPLOYERS_STATUS, mStatus.getValue());
        values.put(MyDB.COL_EMPLOYERS_NOTES, mNotes);
        return values;
    }

    private boolean insert(MyDB pDB) {
        SQLiteDatabase database = pDB.getWritableDatabase();
        String tables = MyDB.TBL_EMPLOYERS;
        ContentValues values = addContentValues(false);
        long newId = database.insertOrThrow(tables, null, values);
        if ( newId > 0 ) {
            mId = ((int)newId);
        }
        return (mId > 0);
    }

    private boolean update(MyDB pDB) {
        ContentValues values = addContentValues(false);
        String tables = MyDB.TBL_EMPLOYERS;
        String where = MyDB.COL_EMPLOYERS_ID + " = ?";
        String[] whereArgs = { Integer.toString(mId) };
        SQLiteDatabase database = pDB.getWritableDatabase();
        int numUpdated = database.update(tables, values, where, whereArgs);
        return (numUpdated == 1);
    }

    private void loadNewEmployerData (Cursor pCursor) {
        this.setId(pCursor.getInt(0));
        this.setEmployerName(pCursor.getString(1));
        this.setContactName(pCursor.getString(2));
        this.setStreetAddress(pCursor.getString(3));
        this.setCity(pCursor.getString(4));
        this.setState(pCursor.getString(5));
        this.setZipCode(pCursor.getString(6));
        this.setPhoneCell(pCursor.getString(7));
        this.setPhoneAlt(pCursor.getString(8));
        this.setEmail1(pCursor.getString(9));
        this.setEmail2(pCursor.getString(10));
        this.setWebsite(pCursor.getString(11));
        this.setStatus(RDStatus.valueOf(pCursor.getInt(12)));
        this.setNotes(pCursor.getString(13));
    }

//  Getters

    public int getId() {
        return mId;
    }

    public String getEmployerName() {
        return mEmployerName;
    }

    public String getContactName() {
        return mContactName;
    }

    public String getStreetAddress() {
        return mStreetAddress;
    }

    public String getCity() {
        return mCity;
    }

    public String getState() {
        return mState;
    }

    public String getZipCode() {
        return mZipCode;
    }

    public String getPhoneCell() {
        return mPhoneCell;
    }

    public String getPhoneAlt() {
        return mPhoneAlt;
    }

    public String getEmail1() {
        return mEmail1;
    }

    public String getEmail2() {
        return mEmail2;
    }

    public String getWebsite() {
        return mWebsite;
    }

    public RDStatus getStatus() {
        return mStatus;
    }

    public String getNotes() {
        return mNotes;
    }

//  Setters

    public void setId(int pId) {
        mId = pId;
    }

    public void setEmployerName(String pEmployerName) {
        mEmployerName = pEmployerName;
    }

    public void setContactName(String pContactName) {
        mContactName = pContactName;
    }

    public void setStreetAddress(String pStreetAddress) {
        mStreetAddress = pStreetAddress;
    }

    public void setCity(String pCity) {
        mCity = pCity;
    }

    public void setState(String pState) {
        mState = pState;
    }

    public void setZipCode(String pZipCode) {
        mZipCode = pZipCode;
    }

    public void setPhoneCell(String pPhoneCell) {
        mPhoneCell = pPhoneCell;
    }

    public void setPhoneAlt(String pPhoneAlt) {
        mPhoneAlt = pPhoneAlt;
    }

    public void setEmail1(String pEmail1) {
        mEmail1 = pEmail1;
    }

    public void setEmail2(String pEmail2) {
        mEmail2 = pEmail2;
    }

    public void setWebsite(String pWebsite) {
        mWebsite = pWebsite;
    }

    public void setStatus(RDStatus pStatus) {
        mStatus = pStatus;
    }

    public void setNotes(String pNotes) {
        mNotes = pNotes;
    }

//  Static Methods

    public static ArrayList<RDEmployer> employersList(MyDB pDB,
                                                      boolean pActiveOnly) {
        ArrayList<RDEmployer> list = new ArrayList<RDEmployer>();
        String tables = MyDB.TBL_EMPLOYERS;
        String[] columns = allColumns;
        String where = "";
        if ( pActiveOnly ) {
            where = MyDB.COL_EMPLOYERS_STATUS + " = " + RDStatus.Active.getValue();
        }
        String[] whereArgs = { };
        String groupBy = "";
        String having = "";
        String orderBy = "";
        orderBy = MyDB.COL_EMPLOYERS_EMPLOYERNAME + " asc";
        SQLiteDatabase database = pDB.getWritableDatabase();
        Cursor cursor = database.query(tables, columns, where, whereArgs,
                groupBy, having, orderBy);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RDEmployer employer = new RDEmployer();
            employer.setId(cursor.getInt(0));
            employer.setEmployerName(cursor.getString(1));
            employer.setContactName(cursor.getString(2));
            employer.setStreetAddress(cursor.getString(3));
            employer.setCity(cursor.getString(4));
            employer.setState(cursor.getString(5));
            employer.setZipCode(cursor.getString(6));
            employer.setPhoneCell(cursor.getString(7));
            employer.setPhoneAlt(cursor.getString(8));
            employer.setEmail1(cursor.getString(9));
            employer.setEmail2(cursor.getString(10));
            employer.setWebsite(cursor.getString(11));
            employer.setStatus(RDStatus.valueOf(cursor.getInt(12)));
            employer.setNotes(cursor.getString(13));
            list.add(employer);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public static RDEmployer lookupByName(MyDB pDB,
                                          String pName) {
        RDEmployer employer = null;
        String tables = MyDB.TBL_EMPLOYERS;
        String[] columns = allColumns;
        String where = MyDB.COL_EMPLOYERS_EMPLOYERNAME + " = ?";
        String[] whereArgs = { pName };
        String groupBy = "";
        String having = "";
        String orderBy = "";
        SQLiteDatabase database = pDB.getWritableDatabase();
        Cursor cursor = database.query(tables, columns, where, whereArgs,
                groupBy, having, orderBy);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            employer = new RDEmployer();
            employer = new RDEmployer();
            employer.loadNewEmployerData(cursor);
        }
        cursor.close();
        return employer;
    }

    public static RDEmployer lookupById(MyDB pDB,
                                        int pEmployerId) {
        RDEmployer employer = null;
        String tables = MyDB.TBL_EMPLOYERS;
        String[] columns = allColumns;
        String where = MyDB.COL_EMPLOYERS_ID + " = ?";
        String[] whereArgs = { String.valueOf(pEmployerId) };
        String groupBy = "";
        String having = "";
        String orderBy = "";
        SQLiteDatabase database = pDB.getWritableDatabase();
        Cursor cursor = database.query(tables, columns, where, whereArgs,
                groupBy, having, orderBy);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            employer = new RDEmployer();
            employer.loadNewEmployerData(cursor);
        }
        cursor.close();
        return employer;
    }

    public static String lookupEmployerName(MyDB pDB,
                                            int pEmployerId) {
        String empName = "";
        String tables = MyDB.TBL_EMPLOYERS;
        String[] columns = { MyDB.COL_EMPLOYERS_EMPLOYERNAME };
        String where = MyDB.COL_EMPLOYERS_ID + " = ?";
        String[] whereArgs = { String.valueOf(pEmployerId) };
        String groupBy = "";
        String having = "";
        String orderBy = "";
        SQLiteDatabase database = pDB.getWritableDatabase();
        Cursor cursor = database.query(tables, columns, where, whereArgs,
                groupBy, having, orderBy);
        cursor.moveToFirst();
        if (!cursor.isAfterLast()) {
            empName = cursor.getString(0);
        }
        cursor.close();
        return empName;
    }

//  Parcelable Implementation

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mId);
        out.writeString(this.mEmployerName);
        out.writeString(this.mContactName);
        out.writeString(this.mStreetAddress);
        out.writeString(this.mCity);
        out.writeString(this.mState);
        out.writeString(this.mZipCode);
        out.writeString(this.mPhoneCell);
        out.writeString(this.mPhoneAlt);
        out.writeString(this.mEmail1);
        out.writeString(this.mEmail2);
        out.writeString(this.mWebsite);
        out.writeInt(this.mStatus.getValue());
        out.writeString(this.mNotes);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<RDEmployer> CREATOR = new Parcelable.Creator<RDEmployer>() {
        public RDEmployer createFromParcel(Parcel in) {
            return new RDEmployer(in);
        }

        public RDEmployer[] newArray(int size) {
            return new RDEmployer[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private RDEmployer(Parcel in) {
        this.mId = in.readInt();
        this.mEmployerName = in.readString();
        this.mContactName = in.readString();
        this.mStreetAddress = in.readString();
        this.mCity = in.readString();
        this.mState = in.readString();
        this.mZipCode = in.readString();
        this.mPhoneCell = in.readString();
        this.mPhoneAlt = in.readString();
        this.mEmail1 = in.readString();
        this.mEmail2 = in.readString();
        this.mWebsite = in.readString();
        this.mStatus = RDStatus.valueOf(in.readInt());
        this.mNotes = in.readString();
    }

}
