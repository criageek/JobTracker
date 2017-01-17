package com.rdfmobileapps.jobtracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

/**
 * Created by rich on 1/12/17.
 */

public class RDJob implements Parcelable {
    private int mId;
    private String mJobName;
    private int mEmployerId;
    private String mStartDate;
    private String mEndDate;
    private double mHourlyRate;
    private String mNotes;
    private RDStatus mStatus;

//  Constructors

    public RDJob() {
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
        String tables = MyDB.TBL_JOBS;
        String where = MyDB.COL_JOBS_ID + " = ?";
        String[] whereArgs = { Integer.toString(mId) };
        boolean retVal = false;
        try {
            database.delete(tables, where, whereArgs);
            retVal = true;
        } catch (SQLiteException e) {
            Log.e("RDJob.delete", e.getMessage());
        }
        return retVal;
    }

//  Private Methods

    private void doSetup() {
        setDefaults();
    }

    private void setDefaults() {
        mId = RDConstants.NOSELECTION;
        mJobName = "";
        mEmployerId = RDConstants.NOSELECTION;
        mStartDate = RDFunctions.currentDateStr(RDConstants.DATE_FORMAT_SHORT_YEARFIRST);
        mEndDate = RDFunctions.currentDateStr(RDConstants.DATE_FORMAT_SHORT_YEARFIRST);
        mHourlyRate = 10.00;
        mNotes = "";
        mStatus = RDStatus.Started;
    }

    private ContentValues addContentValues(boolean pIncludeKey) {
        ContentValues values = new ContentValues();
        if ( pIncludeKey ) {
            values.put(MyDB.COL_JOBS_ID, mId);
        }
        values.put(MyDB.COL_JOBS_JOBNAME, mJobName);
        values.put(MyDB.COL_JOBS_EMPLOYERID, mEmployerId);
        values.put(MyDB.COL_JOBS_STARTDATE, mStartDate);
        values.put(MyDB.COL_JOBS_ENDDATE, mEndDate);
        values.put(MyDB.COL_JOBS_HOURLYRATE, mHourlyRate);
        values.put(MyDB.COL_JOBS_NOTES, mNotes);
        values.put(MyDB.COL_JOBS_STATUS, mStatus.getValue());
        return values;
    }

    private boolean insert(MyDB pDB) {
        SQLiteDatabase database = pDB.getWritableDatabase();
        String tables = MyDB.TBL_JOBS;
        ContentValues values = addContentValues(false);
        long newId = database.insertOrThrow(tables, null, values);
        if ( newId > 0 ) {
            mId = ((int)newId);
        }
        return (mId > 0);
    }

    private boolean update(MyDB pDB) {
        ContentValues values = addContentValues(false);
        String tables = MyDB.TBL_JOBS;
        String where = MyDB.COL_JOBS_ID + " = ?";
        String[] whereArgs = { Integer.toString(mId) };
        SQLiteDatabase database = pDB.getWritableDatabase();
        int numUpdated = database.update(tables, values, where, whereArgs);
        return (numUpdated == 1);
    }

//  Getters

    public int getId() {
        return mId;
    }

    public String getJobName() {
        return mJobName;
    }

    public int getEmployerId() {
        return mEmployerId;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public double getHourlyRate() {
        return mHourlyRate;
    }

    public String getNotes() {
        return mNotes;
    }

    public RDStatus getStatus() {
        return mStatus;
    }

//  Setters

    public void setId(int pId) {
        mId = pId;
    }

    public void setJobName(String pJobName) {
        mJobName = pJobName;
    }

    public void setEmployerId(int pEmployerId) {
        mEmployerId = pEmployerId;
    }

    public void setStartDate(String pStartDate) {
        mStartDate = pStartDate;
    }

    public void setEndDate(String pEndDate) {
        mEndDate = pEndDate;
    }

    public void setHourlyRate(double pHourlyRate) {
        mHourlyRate = pHourlyRate;
    }

    public void setNotes(String pNotes) {
        mNotes = pNotes;
    }

    public void setStatus(RDStatus pStatus) {
        mStatus = pStatus;
    }

//  Parcelable Implementation

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mId);
        out.writeString(this.mJobName);
        out.writeInt(this.mEmployerId);
        out.writeString(this.mStartDate);
        out.writeString(this.mEndDate);
        out.writeDouble(this.mHourlyRate);
        out.writeString(this.mNotes);
        out.writeInt(this.mStatus.getValue());
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<RDJob> CREATOR = new Parcelable.Creator<RDJob>() {
        public RDJob createFromParcel(Parcel in) {
            return new RDJob(in);
        }

        public RDJob[] newArray(int size) {
            return new RDJob[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private RDJob(Parcel in) {
        this.mId = in.readInt();
        this.mJobName = in.readString();
        this.mEmployerId = in.readInt();
        this.mStartDate = in.readString();
        this.mEndDate = in.readString();
        this.mHourlyRate = in.readDouble();
        this.mNotes = in.readString();
        this.mStatus = RDStatus.valueOf(in.readInt());
    }

}
