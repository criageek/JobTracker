package com.rdfmobileapps.jobtracker;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MyDB extends SQLiteOpenHelper implements Serializable {
    private static final long serialVersionUID = -3373286295767499708L;

	private String dbName;
	
//  employers table
	public static final String TBL_EMPLOYERS = "employers";
	public static final String COL_EMPLOYERS_ID = "_id";
	public static final String COL_EMPLOYERS_EMPLOYERNAME = "employerName";
	public static final String COL_EMPLOYERS_CONTACTNAME = "contactName";
	public static final String COL_EMPLOYERS_STREETADDRESS = "streetAddress";
	public static final String COL_EMPLOYERS_CITY = "city";
	public static final String COL_EMPLOYERS_STATE = "state";
	public static final String COL_EMPLOYERS_ZIPCODE = "zipCode";
	public static final String COL_EMPLOYERS_PHONECELL = "phoneCell";
	public static final String COL_EMPLOYERS_PHONEALT = "phoneAlt";
	public static final String COL_EMPLOYERS_EMAIL1 = "email1";
	public static final String COL_EMPLOYERS_EMAIL2 = "email2";
	public static final String COL_EMPLOYERS_WEBSITE = "website";
	public static final String COL_EMPLOYERS_STATUS = "status";
	public static final String COL_EMPLOYERS_NOTES = "notes";

//  jobs table
	public static final String TBL_JOBS = "jobs";
	public static final String COL_JOBS_ID = "_id";
	public static final String COL_JOBS_JOBNAME = "jobName";
	public static final String COL_JOBS_EMPLOYERID = "employerId";
	public static final String COL_JOBS_STARTDATE = "startDate";
	public static final String COL_JOBS_ENDDATE = "endDate";
	public static final String COL_JOBS_HOURLYRATE = "hourlyRate";
	public static final String COL_JOBS_NOTES = "notes";
	public static final String COL_JOBS_STATUS = "status";

//  worklog table
	public static final String TBL_WORKLOG = "workLog";
	public static final String COL_WORKLOG_ID = "_id";
	public static final String COL_WORKLOG_JOBID = "jobId";
	public static final String COL_WORKLOG_STARTTIME = "startTime";
	public static final String COL_WORKLOG_ENDTIME = "endTime";
	public static final String COL_WORKLOG_TOTALTIME = "totalTime";
	public static final String COL_WORKLOG_STARTMILEAGE = "startMileage";
	public static final String COL_WORKLOG_ENDMILEAGE = "endMileage";
	public static final String COL_WORKLOG_TOTALMILEAGE = "totalMileage";
	public static final String COL_WORKLOG_STATUS = "status";
	public static final String COL_WORKLOG_NOTES = "notes";

//  materials table
	public static final String TBL_MATERIALS = "materials";
	public static final String COL_MATERIALS_ID = "_id";
	public static final String COL_MATERIALS_WORKLOGID = "worklogId";
	public static final String COL_MATERIALS_MATERIALNAME = "materialName";
	public static final String COL_MATERIALS_SOURCE = "source";
	public static final String COL_MATERIALS_MYCOST = "myCost";
	public static final String COL_MATERIALS_BILLEDCOST = "billedCost";
	public static final String COL_MATERIALS_NOTES = "notes";

//  dbsettings table
	public static final String TBL_DBSETTINGS = "dbSettings";
	public static final String COL_DBSETTINGS_ID = "_id";
	public static final String COL_DBSETTINGS_DBSCHEMA = "dbSchema";
	public static final String COL_DBSETTINGS_EFFECTIVEDATE = "effectiveDate";
	public static final String COL_DBSETTINGS_NOTES = "notes";

	private static final int VERSION = 1;
	private static File DATABASE_FILE;
// This is an indicator if we need to copy the
// database file.
	private boolean invalidDatabaseFile = false;
	private boolean isUpgraded = false;
	private Context context;
/**
* number of users of the database connection.
* */
	private int openConnections = 0;
	private static MyDB instance;

	synchronized static public MyDB getInstance(Context pContext,
												String pDBName) {
	    if ( instance == null ) {
	    	instance = new MyDB(pContext.getApplicationContext(), pDBName);
	    }
	    return instance;
	}
	
	public MyDB(Context pContext, String pDBName) {
	    super(pContext, pDBName, null, VERSION);
	    this.dbName = pDBName;
	    this.context = pContext;
	    SQLiteDatabase db = null;
	    try {
	        try {
				db = getReadableDatabase();
			} catch (Exception e) {
				e.printStackTrace();
				Log.i("MyDB.constructor", e.getMessage());
			}
	        if (db != null) {
	            db.close();
	        }
	        DATABASE_FILE = this.context.getDatabasePath(this.dbName);
	        if ( invalidDatabaseFile ) {
	            copyDatabase();
//	            copyDatabase2();
	        }
	        if ( isUpgraded ) {
	            doUpgrade();
	        }
	    } catch (SQLiteException e) {
	    } finally {
	        if (db != null && db.isOpen()) {
	            db.close();
	        }
	    }
	}

//  Getters
	public String getDBName() {
		return this.dbName;
	}
	
//  Setters
	public void setDBName(String pDBName) {
		this.dbName = pDBName;
	}

	private void closeAllConnections() {
		while ( openConnections > 0 ) {
			close();
		}
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
	    invalidDatabaseFile = true;
	}
	@Override
	public void onUpgrade(SQLiteDatabase pDB, int pOldVersion, int pNewVersion) {
	    invalidDatabaseFile = true;
	    isUpgraded = true;
	}

/**
* called if a database upgrade is needed
*/
	private void doUpgrade() {
// implement the database upgrade here.
	}

	@Override
	public synchronized void onOpen(SQLiteDatabase db) {
	    super.onOpen(db);
	    // increment the number of users of the database connection.
	    openConnections++;
	    if (!db.isReadOnly()) {
	        // Enable foreign key constraints
	        db.execSQL("PRAGMA foreign_keys=ON;");
	    }
	}

/**
* implementation to avoid closing the database connection while it is in
* use by others.
*/
	@Override
	public synchronized void close() {
	    openConnections--;
	    if ( openConnections == 0 ) {
	        super.close();
	    }
	}
	
	private void copyDatabase() {
	    AssetManager assetManager = context.getResources().getAssets();
	    InputStream in = null;
	    OutputStream out = null;
	    try {
	        in = assetManager.open(this.dbName);
	        out = new FileOutputStream(DATABASE_FILE);
	        byte[] buffer = new byte[1024];
	        int read = 0;
	        while ((read = in.read(buffer)) != -1) {
	            out.write(buffer, 0, read);
	        }
	    } catch (IOException e) {
	    } finally {
	        if (in != null) {
	            try {
	                in.close();
	            } catch (IOException e) {}
	        }
	        if (out != null) {
	            try {
	                out.close();
	            } catch (IOException e) {}
	        }
	    }
	    setDatabaseVersion();
	    invalidDatabaseFile = false;
	}
	
/*	private void copyDatabase2() {
	    AssetManager assetManager = context.getResources().getAssets();
	    InputStream in = null;
	    OutputStream out = null;
	    try {
	        in = assetManager.open("smdb_2013.db");
	        File dbFile;
	        dbFile = this.context.getDatabasePath("smdb_2013.db");
	        out = new FileOutputStream(dbFile);
	        byte[] buffer = new byte[1024];
	        int read = 0;
	        while ((read = in.read(buffer)) != -1) {
	            out.write(buffer, 0, read);
	        }
	    } catch (IOException e) {
	    } finally {
	        if (in != null) {
	            try {
	                in.close();
	            } catch (IOException e) {}
	        }
	        if (out != null) {
	            try {
	                out.close();
	            } catch (IOException e) {}
	        }
	    }
//	    setDatabaseVersion();
//	    invalidDatabaseFile = false;
	}
*/
	private void setDatabaseVersion() {
	    SQLiteDatabase db = null;
	    try {
	        db = SQLiteDatabase.openDatabase(DATABASE_FILE.getAbsolutePath(), null,
	                                         SQLiteDatabase.OPEN_READWRITE);
	        db.execSQL("PRAGMA user_version = " + VERSION);
	    } catch (SQLiteException e ) {
	    } finally {
	        if (db != null && db.isOpen()) {
	            db.close();
	        }
	    }
	}

	public int getNumConnections() {
		return this.openConnections;
	}
}
