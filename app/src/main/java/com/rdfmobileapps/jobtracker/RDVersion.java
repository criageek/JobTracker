package com.rdfmobileapps.jobtracker;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Created by rich on 7/2/16.
 */
public class RDVersion {
    private Context mContext;
    private String mVersionNum;
    private String mBuildNum;
    private String mBuildDate;
    private String mDBSchema;


    public RDVersion(Context pContext) {
        this.mContext = pContext;
        doSetup();
    }

    private void doSetup() {
        findVersionNum();
        findBuildDate();
    }

    private void findVersionNum() {
        PackageManager packageManager = this.mContext.getPackageManager();
        String packageName = this.mContext.getPackageName();
        this.mVersionNum = ""; // initialize String
        try {
            this.mVersionNum = packageManager.getPackageInfo(packageName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void findBuildDate() {
        try {
            PackageManager packageManager = this.mContext.getPackageManager();
            String packageName = this.mContext.getPackageName();
//  rdfrahm  2016-09-25  1.3.1  Change the way we look up build datetime
//            ApplicationInfo ai = packageManager.getApplicationInfo(packageName, 0);
//            ZipFile zf = new ZipFile(ai.sourceDir);
//            ZipEntry ze = zf.getEntry("classes.dex");
//            long time = ze.getTime();
            PackageInfo pi = packageManager.getPackageInfo(packageName, 0);
            long time = pi.lastUpdateTime;
//  rdfrahm  2016-09-25  1.3.1  End
            SimpleDateFormat df = new SimpleDateFormat(RDConstants.DATEFMT_MM_DD_YYYY_HH_MM_SS);
            Date myDate = new java.util.Date(time);
            this.mBuildDate = df.format(myDate);
//  rdfrahm  2016-09-25  1.3.1  Not needed after changing build datetime routine
//            zf.close();
//  rdfrahm  2016-09-25  1.3.1  End
        } catch(Exception e){
        }
    }

    //  Getters
    public String getVersionNum(boolean pWithV) {
        if ( pWithV ) {
            return "v" + this.mVersionNum;
        } else {
            return this.mVersionNum;
        }
    }

    public String getBuildDate() {
        return this.mBuildDate;
    }
}
