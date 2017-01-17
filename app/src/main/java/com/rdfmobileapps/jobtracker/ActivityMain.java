package com.rdfmobileapps.jobtracker;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class ActivityMain extends Activity {
    private MyDB mDBHelper;
    private Vibrator mVibe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doSetup();
    }

    private void doSetup() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
//		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        mVibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
//		RDProgramSettings pgmSettings = RDProgramSettings.getInstance(this);
        mDBHelper = MyDB.getInstance(this, RDConstants.DBNAME);
        RDDBManager dbManager = new RDDBManager(mDBHelper, true, true);
//  Check to see if this is the first
//      run of the app.  If so, we won't show a dialog if permissions to write
//      to external storage are not granted
//  Check permissions if
//      we're running Marshmallow or above
        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            checkPermissions();
        }
        setupScreenControls();
    }

    private void setupScreenControls() {
        setupListView();
        setupVersionLabel();
    }

    private void setupListView() {
/*
//  rdfrahm  2016-07-13  1.2.3  Pass true to pActiveOnly parameter
//        mVehiclesList = RDVehicle.vehiclesList(mDBHelper, true);
        mVehiclesList = RDVehicle.vehiclesList(mDBHelper, true, true);
//  rdfrahm  2016-07-13  1.2.3  End
        mAdapter = new AdapterMain(this, mDBHelper, mVehiclesList);
        mListView = (ListView)findViewById(R.id.lsvMainVehicles);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mVibe.vibrate(RDConstants.BUTTON_VIBRATE_DURATION);
                showChanges(position);
            }
        }); */
    }

    private void setupVersionLabel() {
        TextView tv = (TextView)findViewById(R.id.txvMainVersion);
        RDVersion versionInfo = new RDVersion(this);
        tv.setText(versionInfo.getVersionNum(true));
    }

    private void checkPermissions() {
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                RDConstants.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE);
    }

    public void checkPermission(String pPermission,
                                int pPermissionRequest) {
        if ( !RDFunctions.permissionGranted(this, pPermission)) {
// Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    pPermission)) {
// Show an explanation to the user *asynchronously* -- don't block
// this thread waiting for the user's response! After the user
// sees the explanation, try again to request the permission.
            } else {
// No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{pPermission},
                        pPermissionRequest);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch ( requestCode ) {
            case RDConstants.PERMISSION_REQUEST_WRITE_EXTERNAL_STORAGE: {
// If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
// permission denied, boo! Disable the
// functionality that depends on this permission.
                }
                return;
            }
        }
    }

    public void infoButtonClick(View pView) {
        Intent intent = new Intent(this, ActivityAbout.class);
        startActivity(intent);
    }

    public void newJobClick(View pView) {
        RDJob newJob = new RDJob();
        Intent intent = new Intent(this, ActivityJob.class);
        intent.putExtra(RDConstants.EXTRAKEY_JOB, newJob);
        startActivity(intent);
    }

    public void onEmployersClick(View pButton) {
        Intent intent = new Intent(this, ActivityEmployers.class);
        startActivity(intent);
    }

}
