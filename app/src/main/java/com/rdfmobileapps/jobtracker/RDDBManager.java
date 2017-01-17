package com.rdfmobileapps.jobtracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by rich on 7/13/16.
 */
public class RDDBManager {

    private MyDB mDB;

    public static final float CURRENT_DB_SCHEMA = 1.0f;

//  Constructors
    public RDDBManager(MyDB pDB,
                       boolean pCheckDB,
                       boolean pAutoUpdate) {
        super();
        mDB = pDB;
        if ( pCheckDB ) {
            checkDB(pAutoUpdate);
        }
    }

//  Private Methods

    private void updateDBSchema(float pDBSchema) {
//  rdfrahm  2016-09-18  1.3.1  Change the logic in this method
//        if ( pDBSchema != -99.9f ) {
//            if ( (pDBSchema == 0.0) && (pCurrentDBSchema == CURRENT_DB_SCHEMA) ) {
//                update0_0To1_1();
////  rdfrahm  2016-09-12  1.3.0  Add check for db Schema 1.2
////            }
//            } else {
//                if ( (pDBSchema == 1.1f) && (pCurrentDBSchema == CURRENT_DB_SCHEMA) ) {
//                    update1_1To1_2();
//                }
//            }
////  rdfrahm  2016-09-12  1.3.0  End
//        }
        if ( pDBSchema != -99.9f ) {
            if ( pDBSchema == 0.0 ) {
                if ( CURRENT_DB_SCHEMA == 1.1f ) {
                    update0_0To1_1();
                } else {
                    if ( CURRENT_DB_SCHEMA == 1.2f ) {
                        update0_0To1_2();
                    }
                }
            } else {
                if ( pDBSchema == 1.1f ) {
                    if ( CURRENT_DB_SCHEMA == 1.2f ) {
                        update1_1To1_2();
                    }
                }
            }
        }
//  rdfrahm  2016-09-18  1.3.1  End
    }

//  rdfrahm  2016-09-16  1.3.1  Remove me
//  rdfrahm  2016-09-16  1.3.1  End

    private void update0_0To1_1() {
        if ( createDBSettingsTable() ) {
            if ( addActiveField() ) {
                updateActiveField();
//  rdfrahm  2016-09-12  1.3.0  Pass message as a parameter
//                updateDBSettings(CURRENT_DB_SCHEMA);
                updateDBSettings(1.1f,
                                 "Upgrade db Schema from 0.0 to 1.1");
//  rdfrahm  2016-09-12  1.3.0  End
            }
        }
    }

    private boolean createDBSettingsTable() {
        if ( RDFunctions.tableExists(mDB, MyDB.TBL_DBSETTINGS) ) {
            return true;
        } else {
            String sqlStr = "CREATE TABLE dbSettings (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "dbSchema REAL UNIQUE ON CONFLICT ROLLBACK, " +
                    "effectiveDate TEXT, notes TEXT)";
            SQLiteDatabase database = mDB.getWritableDatabase();
            try {
                database.execSQL(sqlStr);
                Log.i("RDDBManager", "New dbSettings table created for schema 1.1");
                return true;
            } catch (android.database.SQLException e) {
                e.printStackTrace();
                Log.e("RDDBManager", e.getMessage());
                return false;
            } finally {
                database.close();
            }
        }
    }

    private boolean addActiveField() {
        SQLiteDatabase database = mDB.getWritableDatabase();
        try {
            database.execSQL("alter table vehicles add column active integer");
            Log.i("addActiveField", "active field added to vehicles table in " + mDB.getDBName() + ".");
            return true;
        } catch (android.database.SQLException e) {
            e.printStackTrace();
            Log.e("addActiveField", e.getMessage());
            return false;
        } finally {
            database.close();
        }
    }

    private void updateActiveField() {
        SQLiteDatabase database = mDB.getWritableDatabase();
        try {
            database.execSQL("update vehicles set active = 1");
            Log.i("updateActiveField", "active field in vehicles table updated to 1.");
        } catch (android.database.SQLException e) {
            e.printStackTrace();
            Log.e("updateActiveField", e.getMessage());
        } finally {
            database.close();
        }
    }

//  rdfrahm  2016-09-12  1.3.0  Add routines to upgrade db from schema 1.1 to 1.2
    private void update1_1To1_2() {
        if ( addDrainPlugField() ) {
            updateDrainPlugField();
//  rdfrahm  2016-09-12  1.3.0  Pass message as a parameter
//            updateDBSettings(CURRENT_DB_SCHEMA);
            updateDBSettings(1.2f,
                             "Upgrade db Schema from 1.1 to 1.2;  Added drainPlugSize field");
//  rdfrahm  2016-09-12  1.3.0  End
        }
    }

    private boolean addDrainPlugField() {
        SQLiteDatabase database = mDB.getWritableDatabase();
        try {
            database.execSQL("alter table vehicles add column drainPlugSize text");
            Log.i("addDrainPlugField", "drainPlugSize field added to vehicles table in " + mDB.getDBName() + ".");
            return true;
        } catch (android.database.SQLException e) {
            e.printStackTrace();
            Log.e("addDrainPlugField", e.getMessage());
            return false;
        } finally {
            database.close();
        }
    }

    private void updateDrainPlugField() {
        SQLiteDatabase database = mDB.getWritableDatabase();
        try {
            database.execSQL("update vehicles set drainPlugSize = '- -'");
            Log.i("updateDrainPlugField", "drainPlugSize field in vehicles table updated to '- -'.");
        } catch (android.database.SQLException e) {
            e.printStackTrace();
            Log.e("updateDrainPlugField", e.getMessage());
        } finally {
            database.close();
        }
    }

//  rdfrahm  2016-09-12  1.3.0  End

//  rdfrahm  2016-09-18  1.3.1  Add methods to update from 0.0 to 1.2
    private void update0_0To1_2() {
        update0_0To1_1();
        update1_1To1_2();
        Log.i("RDDBManager","Finished update0_0To1_2");
    }
//  rdfrahm  2016-09-18  1.3.1  End

//  rdfrahm  2016-09-12  1.3.0  Pass text message as a parameter
//    private void updateDBSettings(float pDBSchema) {
    private void updateDBSettings(float pDBSchema,
                                  String pMessage) {
//  rdfrahm  2016-09-12  1.3.0  End
        RDDBSettings dbSettings = new RDDBSettings(mDB,
                                                   RDConstants.NOSELECTION,
                                                   pDBSchema,
                                                   RDFunctions.currentDateStr(RDConstants.DATE_FORMAT_SHORT_YEARFIRST),
//  rdfrahm  2016-09-12  1.3.0  Use passed-in message instead of hard-coded message
//                                                   "Upgrade db Schema from 0.0 to 1.1");
                                                   pMessage);
//  rdfrahm  2016-09-12  1.3.0  End
        dbSettings.save();
    }

//  Public Methods

    public void checkDB(boolean pAutoUpdate) {
        RDDBSettings dbSettings = new RDDBSettings(mDB, true);
        if ( dbSettings.getDBSchema() != CURRENT_DB_SCHEMA ) {
            if ( pAutoUpdate ) {
                updateDBSchema(dbSettings.getDBSchema());
            }
        }
        Log.i("RDDBManager", "Exit checkDB");
    }
}
