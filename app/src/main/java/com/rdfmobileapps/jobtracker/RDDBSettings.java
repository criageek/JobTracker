package com.rdfmobileapps.jobtracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rich on 7/13/16.
 */
public class RDDBSettings {
    private int mId;
    private float mDBSchema;
    private String mEffectiveDate;
    private String mNotes;

    private MyDB mDB;

    private static String[] allColumns = { MyDB.COL_DBSETTINGS_ID, MyDB.COL_DBSETTINGS_DBSCHEMA,
            MyDB.COL_DBSETTINGS_EFFECTIVEDATE, MyDB.COL_DBSETTINGS_NOTES };

//  Constructors
    public RDDBSettings() {
        super();
        setDefaults();
    }

    public RDDBSettings(MyDB pDB,
                        boolean pReadCurrent) {
        super();
        setDefaults();
        mDB = pDB;
        if ( pReadCurrent ) {
            readCurrent();
        }
    }

    public RDDBSettings(MyDB pDB,
                        int pId,
                        float pDBSchema,
                        String pEffectiveDate,
                        String pNotes) {
        super();
        setDefaults();
        mDB = pDB;
        mId = pId;
        mDBSchema = pDBSchema;
        mEffectiveDate = pEffectiveDate;
        mNotes = pNotes;
    }

//  Getters

    public int getId() {
        return mId;
    }

    public float getDBSchema() {
        return mDBSchema;
    }

    public String getEffectiveDate() {
        return mEffectiveDate;
    }

    public String getNotes() {
        return mNotes;
    }

//  Setters

    public void setId(int pId) {
        mId = pId;
    }

    public void setDBSchema(float pDBSchema) {
        mDBSchema = pDBSchema;
    }

    public void setEffectiveDate(String pEffectiveDate) {
        mEffectiveDate = pEffectiveDate;
    }

    public void setNotes(String pNotes) {
        mNotes = pNotes;
    }

//  Private Methods

//  rdfrahm  2016-09-25  1.3.1  Add method to check to see if a particular
//      schema exists in the db settings table
    private int schemaExists(float pSchema) {
        int schemaId = RDConstants.NOSELECTION;
        if ( RDFunctions.tableExists(mDB, MyDB.TBL_DBSETTINGS) ) {
            String tables = MyDB.TBL_DBSETTINGS;
            String[] columns = { MyDB.COL_DBSETTINGS_ID };
            String where = MyDB.COL_DBSETTINGS_DBSCHEMA + " = ?";
            String[] whereArgs = { String.valueOf(pSchema) };
            String groupBy = "";
            String having = "";
            String orderBy = "";
            SQLiteDatabase database = mDB.getWritableDatabase();
            Cursor cursor = database.query(tables, columns, where, whereArgs,
                    groupBy, having, orderBy);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                schemaId = cursor.getInt(0);
            }
            cursor.close();
//  rdfrahm  2016-09-18  1.3.1  Close database connection
            database.close();
//  rdfrahm  2016-09-18  1.3.1  End
        }
        return schemaId;
    }
//  rdfrahm  2016-09-25  1.3.1  End

    private void setDefaults() {
        mId = RDConstants.NOSELECTION;
        mDBSchema = -99.9f;
        mEffectiveDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());;
        mNotes = "";
        mDB = null;
    }

    private void readCurrent() {
        mId = RDConstants.NOSELECTION;
        mDBSchema = 0.0f;
        mEffectiveDate = "2000-01-01";
        mNotes = "";
        if ( RDFunctions.tableExists(mDB, MyDB.TBL_DBSETTINGS) ) {
            String tables = MyDB.TBL_DBSETTINGS;
            String[] columns = RDDBSettings.allColumns;
            String where = "";
            String[] whereArgs = {};
            String groupBy = "";
            String having = "";
            String orderBy = MyDB.COL_DBSETTINGS_EFFECTIVEDATE + " desc, " +
                    MyDB.COL_DBSETTINGS_DBSCHEMA + " desc";
            SQLiteDatabase database = mDB.getWritableDatabase();
            Cursor cursor = database.query(tables, columns, where, whereArgs,
                    groupBy, having, orderBy);
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                mId = cursor.getInt(0);
                mDBSchema = cursor.getFloat(1);
                mEffectiveDate = cursor.getString(2);
                mNotes = cursor.getString(3);
            }
            cursor.close();
//  rdfrahm  2016-09-18  1.3.1  Close database connection
            database.close();
//  rdfrahm  2016-09-18  1.3.1  End
        }
    }

    private ContentValues addContentValues(boolean pIncludeKey) {
        ContentValues values = new ContentValues();
        if ( pIncludeKey ) {
            values.put(MyDB.COL_DBSETTINGS_ID, mId);
        }
        values.put(MyDB.COL_DBSETTINGS_DBSCHEMA, mDBSchema);
        values.put(MyDB.COL_DBSETTINGS_EFFECTIVEDATE, mEffectiveDate);
        values.put(MyDB.COL_DBSETTINGS_NOTES, mNotes);
        return values;
    }

    private boolean add() {
        SQLiteDatabase database = mDB.getWritableDatabase();
        String tables = MyDB.TBL_DBSETTINGS;
        ContentValues values = addContentValues(false);
//  rdfrahm  2016-09-25  1.3.1  Change this code to catch exception
//        long newId = database.insertOrThrow(tables, null, values);
//        mId = ((int)newId);
////  rdfrahm  2016-09-18  1.3.1  Close database connection
//        database.close();
////  rdfrahm  2016-09-18  1.3.1  End
//        return mId > 0;

        try {
            long newId = database.insertOrThrow(tables, null, values);
            mId = ((int)newId);
//  rdfrahm  2016-09-18  1.3.1  Close database connection
//  rdfrahm  2016-09-18  1.3.1  End
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("RDDBSettings.add", "ERROR adding record to db settings table:  " + e.getMessage());
        } finally {
            database.close();
        }
        return mId > 0;
//  rdfrahm  2016-09-25  1.3.1  End
    }

    private boolean update() {
        ContentValues values = addContentValues(false);
        String tables = MyDB.TBL_DBSETTINGS;
        String where = MyDB.COL_DBSETTINGS_ID + " = ?";
        String[] whereArgs = { Integer.toString(mId) };
        SQLiteDatabase database = mDB.getWritableDatabase();
        int numUpdated = database.update(tables, values, where, whereArgs);
//  rdfrahm  2016-09-18  1.3.1  Close database connection
        database.close();
//  rdfrahm  2016-09-18  1.3.1  End
        return (numUpdated == 1);
    }

//  Public Methods

    public boolean save() {
        boolean retVal = false;
        if ( mDB != null ) {
//  rdfrahm  2016-09-25  1.3.1  Change this to read the db settings table for
//      the schema in mDBSchema to see if it already exists
//            if (mId != RDConstants.NOSELECTION) {
//                retVal = update();
//            } else {
//                retVal = add();
//            }
            mId = schemaExists(mDBSchema);
            if ( mId > 0 ) {
                retVal = update();
            } else {
                retVal = add();
            }
//  rdfrahm  2016-09-25  1.3.1  End

        }
        return retVal;
    }

    public static ArrayList<RDDBSettings> readAllDBSettings(MyDB pDB) {
        ArrayList<RDDBSettings> settingsList = new ArrayList<RDDBSettings>();
        if ( RDFunctions.tableExists(pDB, MyDB.TBL_DBSETTINGS) ) {
            String tables = MyDB.TBL_DBSETTINGS;
            String[] columns = RDDBSettings.allColumns;
            String where = "";
            String[] whereArgs = {};
            String groupBy = "";
            String having = "";
            String orderBy = MyDB.COL_DBSETTINGS_EFFECTIVEDATE + " desc";
            SQLiteDatabase database = pDB.getWritableDatabase();
            Cursor cursor = database.query(tables, columns, where, whereArgs,
                    groupBy, having, orderBy);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                RDDBSettings dbs = new RDDBSettings();
                dbs.setId(cursor.getInt(0));
                dbs.setDBSchema(cursor.getFloat(1));
                dbs.setEffectiveDate(cursor.getString(2));
                dbs.setNotes(cursor.getString(3));
                settingsList.add(dbs);
                cursor.moveToNext();
            }
            cursor.close();
//  rdfrahm  2016-09-18  1.3.1  Close database connection
            database.close();
//  rdfrahm  2016-09-18  1.3.1  End
        }
        return settingsList;
    }

}
