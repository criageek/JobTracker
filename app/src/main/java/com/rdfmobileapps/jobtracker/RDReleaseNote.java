package com.rdfmobileapps.jobtracker;

import android.content.Context;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by rich on 2/2/15.
 */
public class RDReleaseNote {
    private String mVersion;
    private String mDate;
    private String mTitle;
    private String mDescription;
    private ArrayList<String> mAffectedFiles;

//  Constructors
    public RDReleaseNote() {
        doDefaultSetup();
    }

    private void doDefaultSetup() {
        mVersion = "";
        mDate = "";
        mTitle = "";
        mDescription = "";
        mAffectedFiles = new ArrayList<String>();
    }

//  Getters
    public String getVersion() {
        return mVersion;
    }

    public String getDate() {
        return mDate;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public ArrayList<String> getAffectedFiles() {
        return mAffectedFiles;
    }

//  Setters
    public void setVersion(String pVersion) {
        this.mVersion = pVersion;
    }

    public void setDate(String pDate) {
        this.mDate = pDate;
    }
    public void setTitle(String pTitle) {
        this.mTitle = pTitle;
    }

    public void setDescription(String pDescription) {
        this.mDescription = pDescription;
    }

    public void setAffectedFiles(ArrayList<String> pAffectedFiles) {
        this.mAffectedFiles = pAffectedFiles;
    }

//  Public methods
    public void parseAffectedFilesStr(String pAffectedFilesStr) {
        String[] strList = pAffectedFilesStr.split(",");
        for (String str : strList) {
            mAffectedFiles.add(str);
        }
    }

    public String getAffectedFilesStr() {
        String str = "";
        for ( String filename: mAffectedFiles ) {
            if ( str.length() == 0 ) {
                str = filename;
            } else {
                str = str + ", " + filename;
            }
        }
        return str;
    }

}
