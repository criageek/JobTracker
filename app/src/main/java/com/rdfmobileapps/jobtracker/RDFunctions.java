package com.rdfmobileapps.jobtracker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;

import android.Manifest;
import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class RDFunctions {
	
	public static String convertDateFormat(String pDate, String pFromFormat, String pToFormat) {
		String retDate = "";
		if ( pDate != null ) {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pFromFormat, Locale.getDefault());
			Date convertedDate = new Date();
			try {
				convertedDate = dateFormat.parse(pDate);
				SimpleDateFormat dateFormat2 = new SimpleDateFormat(pToFormat, Locale.getDefault());
				retDate = dateFormat2.format(convertedDate);
			} catch (ParseException e) {
				Log.e("convertDateFormat", e.getMessage());
				e.printStackTrace();
			}
		}
		return retDate;
	}

	public static String dateToString(Date pDate, String pFormat){
		SimpleDateFormat dateFormat = new SimpleDateFormat(pFormat, Locale.US);
		return dateFormat.format(pDate);
	}
	
	public static Date dateFromString(String pStrDate, String pFormat) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(pFormat, Locale.getDefault());
		Date convertedDate = null;
		try {
			convertedDate = dateFormat.parse(pStrDate);
		} catch (ParseException e) {
			Log.e("dateFromString", e.getMessage());
		}
		return convertedDate;
	}
	
	public static String currentDateTimeStr(String pDateFormat) {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		SimpleDateFormat sdf = new SimpleDateFormat(pDateFormat + " hh:mm", Locale.getDefault());
		return sdf.format(cal.getTime());
	}

	public static String currentDateStr(String pFormat) {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		SimpleDateFormat sdf = new SimpleDateFormat(pFormat, Locale.getDefault());
		return sdf.format(cal.getTime());
	}

	public static String currentYearStr() {
		String myDateStr = currentDateStr(RDConstants.DATE_FORMAT_SHORT_YEARFIRST);
		return myDateStr.substring(0, 4);
	}

	public static Date currentDate() {
		Calendar cal = Calendar.getInstance(TimeZone.getDefault());
		return cal.getTime();
	}
	
	public static Date addMonthsToDate(int pMonths, Date pDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(pDate);
		cal.add(Calendar.MONTH, pMonths);
		return cal.getTime();
	}

	public static Date addDaysToDate(int pDays, Date pDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(pDate);
		cal.add(Calendar.DAY_OF_MONTH, pDays);
		return cal.getTime();
	}

	public static String addDaysToDate(int pDays,
									   String pDateStr,
									   String pFormat) {
		Date myDate = RDFunctions.dateFromString(pDateStr,
												 pFormat);
		Calendar cal = Calendar.getInstance();
		cal.setTime(myDate);
		cal.add(Calendar.DAY_OF_MONTH, pDays);
		return RDFunctions.dateToString(cal.getTime(),
										pFormat);
	}

	public static String addDateTimeToString(String pString,
											 String pSeparator) {
		return pString + pSeparator + currentDateStr(RDConstants.DATE_FORMAT_SHORT_YEARFIRST) +
				"_" + currentDateStr(RDConstants.DATEFMT_HH_MM_SS);
	}

	public static boolean permissionGranted(Context pContext,
											String pPermission) {
		return (ContextCompat.checkSelfPermission(pContext, pPermission) == PackageManager.PERMISSION_GRANTED);
	}

	public static boolean createFolders(Context pContext) {
		boolean result = false;
		if ( permissionGranted(pContext, Manifest.permission.WRITE_EXTERNAL_STORAGE ) ) {
			if ( directoryExists(Environment.getExternalStorageDirectory() + "/" + RDConstants.PROGRAM_FOLDER) ) {
				if ( directoryExists(Environment.getExternalStorageDirectory() + "/" + RDConstants.ARCHIVE_FOLDER) ) {
					if ( directoryExists(Environment.getExternalStorageDirectory() + "/" + RDConstants.EXPORT_FOLDER) ) {
						if ( directoryExists(Environment.getExternalStorageDirectory() + "/" + RDConstants.IMPORT_FOLDER) ) {
							result = true;
						} else {
							showAlertDialog(pContext,
									"Error creating folder",
									"An error occurred while trying to create the import folder");
						}
					} else {
						showAlertDialog(pContext,
								"Error creating folder",
								"An error occurred while trying to create the export folder");
					}
				} else {
					showAlertDialog(pContext,
							"Error creating folder",
							"An error occurred while trying to create the archive folder");
				}
			} else {
				showAlertDialog(pContext,
						"Error creating folder",
						"An error occurred while trying to create the RDCarManager folder");
			}
		} else {
			showAlertDialog(pContext,
					"No Write Permission",
					"Permission has not been granted to write to external storage so you " +
							"will not be able to import or export files or copy images" +
							" into the app.  To correct this go into settings, application " +
							"manager, RDCarMananger, Permissions and turn on the Storage permission.");
		}
		return result;
	}

	public static String dateFromDateTimeStr(String pDateTimeStr) {
//  rdfrahm	 20170128  I'm going to assume that the date format is always
//		either yyyy-MM-dd or MM/dd/yyyy, so it will always be 10 characters
		if (pDateTimeStr.length() >= 10) {
			return new String(pDateTimeStr.substring(0, 10));
		} else {
			return "";
		}
	}

	public static String timeFromDateTimeStr(String pDateTimeStr) {
		if (pDateTimeStr.length() >= 16) {
			return new String(pDateTimeStr.substring(11, 16));
		} else {
			return "";
		}
	}

	public static boolean directoryExists(String pPath) {
		boolean exists = false;
		File folder = new File(pPath);
		if ( !folder.exists() ) {
//  Create directory
			exists = folder.mkdir();
		} else {
			exists = true;
		}
		return exists;
	}

	public static void showAlertDialog(Context pContext,
									   String pTitle,
									   String pMessage) {
		AlertDialog alertDialog = new AlertDialog.Builder(pContext).create();
		alertDialog.setTitle(pTitle);
		alertDialog.setMessage(pMessage);
		alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		// Set the Icon for the Dialog
		alertDialog.setIcon(R.drawable.jobtracker_icon);
		alertDialog.show();
	}
	public static String buildFullFilename(File pPath, String pSubFolder, String pFilename) {
		return pPath + "/" + pSubFolder + "/" + pFilename;
	}

	public static boolean exportDB(String pFilename,
								   String pOutputFilename,
								   Context pContext) {
		boolean result = false;
		if ( createFolders(pContext) ) {
			InputStream in = null;
			OutputStream out = null;
			String outFilename = "";
			try {
				File inFile = pContext.getDatabasePath(pFilename);
				File outFile = new File(pOutputFilename);
				in = new FileInputStream(inFile);
				out = new FileOutputStream(outFile);
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
					} catch (IOException e) {
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
					}
				}
				result = true;
			}
		}
		return result;
	}

	public static boolean tableExists(MyDB pDB,
									  String pTableName) {
		SQLiteDatabase db = pDB.getWritableDatabase();
		Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" +
				MyDB.TBL_DBSETTINGS + "'", null);
		if ( cursor != null ) {
			if ( cursor.getCount() > 0 ) {
				cursor.close();
				return true;
			}
			cursor.close();
		}
		return false;
	}

	public static String numberToStr(float pVal, String pFormat) {
		return String.format(pFormat,  pVal);
	}

	public static String numberToStr(double pVal, String pFormat) {
		return String.format(pFormat,  pVal);
	}

	public static String numberToStr(int pVal, String pFormat) {
		return String.format(pFormat,  pVal);
	}

	public static String fileExtension(String pFilename) {
		String ext = pFilename.substring((pFilename.lastIndexOf(".") + 1), pFilename.length());
		return ext;
	}

	public static double stringToDouble(String pString) {
		if (!isValidDouble(pString)) {
			return 0.0;
		} else {
			return Double.valueOf(pString);
		}
	}

	public static Boolean isValidDouble(String pString) {
		try {
			double val = Double.valueOf(pString);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static boolean isValidTime(String pString) {
		SimpleDateFormat df = new SimpleDateFormat("hh:mm");
		try {
			df.parse(pString);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static boolean isValidDate(String pString,
									  String pFormat) {
		SimpleDateFormat df = new SimpleDateFormat(pFormat);
		try {
			df.parse(pString);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static boolean isValidDateTime(String pString,
										  String pDateFormat,
										  String pTimeFormat) {
		SimpleDateFormat df = new SimpleDateFormat(pDateFormat + " " + pTimeFormat);
		try {
			df.parse(pString);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}

	public static String formatAMPMStr(String pString) {
		if (pString.toLowerCase().equals("a.m.") || pString.toLowerCase().equals("am")) {
			return "AM";
		} else {
			if (pString.toLowerCase().equals("p.m.") || pString.toLowerCase().equals("pm")) {
				return "PM";
			} else {
				return "";
			}
		}
	}

	public static String formatTimeStr(int pHourNum,
									   int pMinuteNum,
									   String pAMPM) {
		String timeStr = numberToStr(pHourNum, "%02d") +
							":" +
							numberToStr(pMinuteNum, "%02d");
		if (!pAMPM.isEmpty()) {
			timeStr = timeStr + " " + pAMPM;
		}
		return timeStr;
	}

	public static String[] arrayListToStringArray(ArrayList<String> pArrayList) {
		String[] myStringArray = new String[pArrayList.size()];
		for (int index = 0; index < pArrayList.size(); index++) {
			myStringArray[index] = pArrayList.get(index);
		}
		return myStringArray;
	}

	public static boolean isValidDBExtension(String pFilename) {
		String extension = RDFunctions.fileExtension(pFilename).toLowerCase();
		return ( (extension.equals("db")) || (extension.equals("sqlite")) );
	}

	public static float getDBSchema(Context pContext, String pDBName) {
		float schema = 0.0f;
		MyDB myDB = new MyDB(pContext, pDBName);
		if ( RDFunctions.tableExists(myDB, MyDB.TBL_DBSETTINGS) ) {
			String tables = MyDB.TBL_DBSETTINGS;
			String[] columns = {"max(" + MyDB.COL_DBSETTINGS_DBSCHEMA + ")"};
			String where = "";
			String[] whereArgs = {};
			String groupBy = "";
			String having = "";
			String orderBy = "";
			SQLiteDatabase database = myDB.getWritableDatabase();
			Cursor cursor = database.query(tables, columns, where, whereArgs,
					groupBy, having, orderBy);
			cursor.moveToFirst();
			if (!cursor.isAfterLast()) {
				schema = cursor.getFloat(0);
			}
			cursor.close();
			myDB.close();
		}
		return schema;
	}

	public static void sendViewToBack(final View child) {
		final ViewGroup parent = (ViewGroup)child.getParent();
		if (null != parent) {
			parent.removeView(child);
			parent.addView(child, 0);
		}
	}

	public static int monthNameToNum(String pString) {
		pString = pString.toLowerCase();
		switch (pString) {
			case "january": {
				return 1;
			}
			case "february": {
				return 2;
			}
			case "march": {
				return 3;
			}
			case "april": {
				return 4;
			}
			case "may": {
				return 5;
			}
			case "june": {
				return 6;
			}
			case "july": {
				return 7;
			}
			case "august": {
				return 8;
			}
			case "september": {
				return 9;
			}
			case "october": {
				return 10;
			}
			case "november": {
				return 11;
			}
			case "december": {
				return 12;
			}
			default: {
				return RDConstants.NOSELECTION;
			}
		}
	}

	public static int dayWordToNum(String pString) {
		pString = pString.toLowerCase();
		switch (pString) {
			case "first": {
				return 1;
			}
			case "second": {
				return 2;
			}
			case "third": {
				return 3;
			}
			case "fourth": {
				return 4;
			}
			case "fifth": {
				return 5;
			}
			case "sixth": {
				return 6;
			}
			case "seventh": {
				return 7;
			}
			case "eighth": {
				return 8;
			}
			case "ninth": {
				return 9;
			}
			case "tenth": {
				return 10;
			}
			case "eleventh": {
				return 11;
			}
			case "twelfth": {
				return 12;
			}
			case "thirteenth": {
				return 13;
			}
			case "fourteenth": {
				return 14;
			}
			case "fifteenth": {
				return 15;
			}
			case "sixteenth": {
				return 16;
			}
			case "seventeenth": {
				return 17;
			}
			case "eighteenth": {
				return 18;
			}
			case "nineteenth": {
				return 19;
			}
			case "twentieth": {
				return 20;
			}
			case "twenty": {
				return 20;
			}
			case "1st": {
				return 1;
			}
			case "2nd": {
				return 2;
			}
			case "3rd": {
				return 3;
			}
			case "4th": {
				return 4;
			}
			case "5th": {
				return 5;
			}
			case "6th": {
				return 6;
			}
			case "7th": {
				return 7;
			}
			case "8th": {
				return 8;
			}
			case "9th": {
				return 9;
			}
			case "10th": {
				return 10;
			}
			case "11th": {
				return 11;
			}
			case "12th": {
				return 12;
			}
			case "13th": {
				return 13;
			}
			case "14th": {
				return 14;
			}
			case "15th": {
				return 15;
			}
			case "16th": {
				return 16;
			}
			case "17th": {
				return 17;
			}
			case "18th": {
				return 18;
			}
			case "19th": {
				return 19;
			}
			case "20th": {
				return 20;
			}
			case "21st": {
				return 21;
			}
			case "22nd": {
				return 22;
			}
			case "23rd": {
				return 23;
			}
			case "24th": {
				return 24;
			}
			case "25th": {
				return 25;
			}
			case "26th": {
				return 26;
			}
			case "27th": {
				return 27;
			}
			case "28th": {
				return 28;
			}
			case "29th": {
				return 29;
			}
			case "30th": {
				return 30;
			}
			case "31st": {
				return 31;
			}
			default: {
				return RDConstants.NOSELECTION;
			}
		}
	}
}