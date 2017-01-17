package com.rdfmobileapps.jobtracker;


public class RDConstants {

	public static final String DBNAME = "jobtracker.db";

	public static final int NOSELECTION = -99999; 

	public static final long BUTTON_VIBRATE_DURATION = 40;
	
	public static final String PROGRAM_FOLDER = "JobTracker";
	public static final String EXPORT_FOLDER = PROGRAM_FOLDER + "/Export";
	public static final String IMPORT_FOLDER = PROGRAM_FOLDER + "/Import";
	public static final String ARCHIVE_FOLDER = PROGRAM_FOLDER + "/Archive";
	public static final String RELEASENOTES_FILENAME = "releasenotes.txt";

/*******************************************************************/
/*  Intent Extra Key constants                                     */
/*******************************************************************/
	public static final String EXTRAKEY_JOB = "JOB";

/*******************************************************************/
/*  Intent Request constants                                       */
/*******************************************************************/
	public static final int REQUEST_SELECTFILTER = 0;
	public static final int REQUEST_SELECTOIL = 1;

/***********************************************************/
/*  Date/time constants                                    */
/***********************************************************/
	public static final String DATETIME_FORMAT_LONG = "yyyy-MM-dd HH:mm:ss zzz";
	public static final String DATETIME_FORMAT_LONGWOZONE = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_NIL = "1900-01-01 00:00:00";
	public static final String DATE_FORMAT_MMDD = "MM/dd";
	public static final String DATE_FORMAT_MDD = "M/dd";
	public static final String DATE_FORMAT_SHORT = "MM/dd/yyyy";
	public static final String DATE_FORMAT_SHORT_YEARFIRST = "yyyy-MM-dd";
	public static final String DATE_FORMAT_SHORT_YEARFIRSTNODASHES = "yyyyMMdd";
	public static final String MAXDATE = "2099-12-31";
	public static final String DATEFMT_MM_DD_YYYY_HH_MM_SS = "MM/dd/yyyy HH:mm:ss";
	public static final String DATEFMT_HH_MM = "HH:mm";
	public static final String DATEFMT_HH_MM_SS = "HH:mm:ss";
	public static final String DATE_FORMAT_ROUNDDATE = "yyyy-MM-dd";

/***********************************************************/
/*  Release Notes constants                                */
/***********************************************************/
	public static final String RELEASENOTES_COLNAME_VERSION = "Version";

	public static final int PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE = 1001;

/***********************************************************/
/*  Preferences constants                                  */
/***********************************************************/
	public static final String PREFERENCENAME_FIRSTRUN = "FirstRun";

/***********************************************************/
/*  Text/Background color constants                        */
/***********************************************************/
	public static final int TEXT_COLOR = 0;
	public static final int BACKGROUND_COLOR = 1;

	public static final String COPYRIGHT = "\u00A9" + "2017  RDF Mobile Apps";

}


