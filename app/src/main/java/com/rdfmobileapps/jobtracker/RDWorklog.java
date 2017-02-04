package com.rdfmobileapps.jobtracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by rich on 1/23/17.
 */

public class RDWorklog implements Parcelable {
    
    private int mId;
    private int mJobId;
    private String mStartTime;
    private String mEndTime;
    private int mTotalTime;  //  minutes
    private double mStartMileage;
    private double mEndMileage;
    private double mTotalMileage;
    private RDStatus mStatus;
    private String mNotes;

    private static String[] allColumns = { MyDB.COL_WORKLOG_ID, MyDB.COL_WORKLOG_JOBID,
            MyDB.COL_WORKLOG_STARTTIME, MyDB.COL_WORKLOG_ENDTIME, MyDB.COL_WORKLOG_TOTALTIME,
            MyDB.COL_WORKLOG_STARTMILEAGE, MyDB.COL_WORKLOG_ENDMILEAGE, MyDB.COL_WORKLOG_TOTALMILEAGE,
            MyDB.COL_WORKLOG_STATUS, MyDB.COL_WORKLOG_NOTES };
    
//  Constructors
    
    public RDWorklog() {
        super();
        doSetup();
    }
    
//  Public Methods
    
//  Private Methods

    private void setDefaults() {
        mId = RDConstants.NOSELECTION;
        mJobId = RDConstants.NOSELECTION;
        mStartTime = "";
        mEndTime = "";
        mTotalTime = 0;
        mStartMileage = 0.0;
        mEndMileage = 0.0;
        mTotalMileage = 0.0;
        mStatus = RDStatus.Active;
        mNotes = "";
    }

    private void doSetup() {
        setDefaults();
    }
    
//  Getters

    public int getId() {
        return mId;
    }

    public int getJobId() {
        return mJobId;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public int getTotalTime() {
        return mTotalTime;
    }

    public double getStartMileage() {
        return mStartMileage;
    }

    public double getEndMileage() {
        return mEndMileage;
    }

    public double getTotalMileage() {
        return mTotalMileage;
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

    public void setJobId(int pJobId) {
        mJobId = pJobId;
    }

    public void setStartTime(String pStartTime) {
        mStartTime = pStartTime;
    }

    public void setEndTime(String pEndTime) {
        mEndTime = pEndTime;
    }

    public void setTotalTime(int pTotalTime) {
        mTotalTime = pTotalTime;
    }

    public void setStartMileage(double pStartMileage) {
        mStartMileage = pStartMileage;
    }

    public void setEndMileage(double pEndMileage) {
        mEndMileage = pEndMileage;
    }

    public void setTotalMileage(double pTotalMileage) {
        mTotalMileage = pTotalMileage;
    }

    public void setStatus(RDStatus pStatus) {
        mStatus = pStatus;
    }

    public void setNotes(String pNotes) {
        mNotes = pNotes;
    }

//  Static Methods

    public static ArrayList<RDWorklog> worklogList(MyDB pDB,
                                                   int pJobId,
                                                   boolean pActiveOnly) {
        ArrayList<RDWorklog> list = new ArrayList<RDWorklog>();
        String tables = MyDB.TBL_WORKLOG;
        String[] columns = allColumns;
        String where = "";
        if ( pActiveOnly ) {
            where = MyDB.COL_WORKLOG_JOBID + " = ? and " +
                    MyDB.COL_WORKLOG_STATUS + " = " + RDStatus.Active.getValue();
        }
        String[] whereArgs = { String.valueOf(pJobId) };
        String groupBy = "";
        String having = "";
        String orderBy = "";
//  rdfrahm  20170121  order by date instead??
        orderBy = MyDB.COL_WORKLOG_STARTTIME + " desc";
        SQLiteDatabase database = pDB.getWritableDatabase();
        Cursor cursor = database.query(tables, columns, where, whereArgs,
                groupBy, having, orderBy);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            RDWorklog worklog = new RDWorklog();
            worklog.setId(cursor.getInt(0));
            worklog.setJobId(cursor.getInt(1));
            worklog.setStartTime(cursor.getString(2));
            worklog.setEndTime(cursor.getString(3));
            worklog.setTotalTime(cursor.getInt(4));
            worklog.setStartMileage(cursor.getDouble(5));
            worklog.setEndMileage(cursor.getDouble(6));
            worklog.setTotalMileage(cursor.getDouble(7));
            worklog.setStatus(RDStatus.valueOf(cursor.getInt(8)));
            worklog.setNotes(cursor.getString(9));
            list.add(worklog);
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

//  Parcelable Implementation

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mId);
        out.writeInt(this.mJobId);
        out.writeString(this.mStartTime);
        out.writeString(this.mEndTime);
        out.writeInt(this.mTotalTime);
        out.writeDouble(this.mStartMileage);
        out.writeDouble(this.mEndMileage);
        out.writeDouble(this.mTotalMileage);
        out.writeInt(this.mStatus.getValue());
        out.writeString(this.mNotes);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<RDWorklog> CREATOR = new Parcelable.Creator<RDWorklog>() {
        public RDWorklog createFromParcel(Parcel in) {
            return new RDWorklog(in);
        }

        public RDWorklog[] newArray(int size) {
            return new RDWorklog[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private RDWorklog(Parcel in) {
        this.mId = in.readInt();
        this.mJobId = in.readInt();
        this.mStartTime = in.readString();
        this.mEndTime = in.readString();
        this.mTotalTime = in.readInt();
        this.mStartMileage = in.readDouble();
        this.mEndMileage = in.readDouble();
        this.mTotalMileage = in.readDouble();
        this.mStatus = RDStatus.valueOf(in.readInt());
        this.mNotes = in.readString();
    }

}
