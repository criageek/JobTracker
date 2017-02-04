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
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ActivityMain extends AppCompatActivity {
    private MyDB mDBHelper;
    private Vibrator mVibe;
    private AdapterJobs mAdapter;
    private ListView mListView;
    private ArrayList<RDJob> mJobsList;
    private boolean mLoading = true;
    private RDTopButtons mTopButtons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doSetup();
    }

    private void doSetup() {
        if ( ViewConfiguration.get(this).hasPermanentMenuKey() ) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_main);
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
        setupCustomActionBar();
    }

    private void setupCustomActionBar() {
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar_layout);
        View view =getSupportActionBar().getCustomView();
        TextView version = (TextView)findViewById(R.id.txvABVersion);
        RDVersion versionInfo = new RDVersion(this);
        version.setText(versionInfo.getVersionNum(true));
    }

    private void setupScreenControls() {
        setupTopButtons();
        setupListView();
    }

    private void setupTopButtons() {
        mTopButtons = (RDTopButtons)findViewById(R.id.rdtbMain);
        mTopButtons.setOnRDTBClickedListener(new RDTopButtons.OnRDTBClickedListener() {
            @Override
            public void onCancelClick() {

            }

            @Override
            public void onSaveClick() {

            }

            @Override
            public void onCustomClick() {
                RDJob newJob = new RDJob();
                showJobActivity(newJob);
            }
        });
    }

    private void setupListView() {
        mAdapter = new AdapterJobs(this, mJobsList);
        mListView = (ListView)findViewById(R.id.lsvMainJobs);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mVibe.vibrate(RDConstants.BUTTON_VIBRATE_DURATION);
                showWorklogs(position);
//                showJob(position);
            }
        });
    }

    private void getJobsList() {
//        mJobs = RDJob.jobsList(mDBHelper, !mShowAllCheckBox.isChecked());
        mJobsList = RDJob.jobsList(mDBHelper, false);
    }

    private void showJob(int pRowNum) {
        RDJob job = mJobsList.get(pRowNum);
        showJobActivity(job);
    }

    private void showWorklogs(int pRowNum) {
        RDJob job = mJobsList.get(pRowNum);
        Intent intent = new Intent(this, ActivityWorklogs.class);
        intent.putExtra(RDConstants.EXTRAKEY_JOB, job);
        startActivity(intent);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.action_about:
                showAboutActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    private void showAboutActivity() {
        Intent intent = new Intent(this, ActivityAbout.class);
        startActivity(intent);
    }

    public void infoButtonClick(View pView) {
        showAboutActivity();
    }

    private void showJobActivity(RDJob pJob) {
        Intent intent = new Intent(this, ActivityJob.class);
        intent.putExtra(RDConstants.EXTRAKEY_JOB, pJob);
        startActivity(intent);
    }

    public void onEmployersClick(View pButton) {
        Intent intent = new Intent(this, ActivityEmployers.class);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
//        if (!mLoading) {
            getJobsList();
            mAdapter.refreshList(mJobsList);
//        }
        mLoading = false;
    }
}
