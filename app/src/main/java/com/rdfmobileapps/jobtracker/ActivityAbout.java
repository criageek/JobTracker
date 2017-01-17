package com.rdfmobileapps.jobtracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

public class ActivityAbout extends Activity {

    private RDVersion versionInfo;

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
        setContentView(R.layout.activity_about);
        this.versionInfo = new RDVersion(this);
        setupData();
    }

    private void setupData() {
        TextView txv = (TextView)findViewById(R.id.txvAboutVersionVal);
        txv.setText(this.versionInfo.getVersionNum(false));
        txv = (TextView)findViewById(R.id.txvAboutBuildDateVal);
        txv.setText(this.versionInfo.getBuildDate());
        TextView txv2 = (TextView)findViewById(R.id.txvAboutCopyright);
        txv2.setText("Copyright  " + RDConstants.COPYRIGHT);
//  rdfrahm  2016-07-13  1.2.3  Add value for DB Schema
        txv = (TextView)findViewById(R.id.txvAboutDBSchemaVal);
        txv.setText(RDFunctions.numberToStr(RDDBManager.CURRENT_DB_SCHEMA, "%.1f"));
//  rdfrahm  2016-07-13  1.2.3  End
    }

    protected void sendEmail() {
        Log.i("Send email", "");
        String[] TO = {"rdfmobileapps@mediacombb.net"};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Job Tracker Feedback");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Finished sending email.", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(ActivityAbout.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void onFeedbackClicked(View pButton) {
        sendEmail();
    }

    public void onActivityClick(View pButton) {
        Intent intent = new Intent(this, ActivityReleaseNotes.class);
        startActivity(intent);
    }

}
