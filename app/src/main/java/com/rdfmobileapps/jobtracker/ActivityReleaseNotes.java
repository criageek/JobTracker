package com.rdfmobileapps.jobtracker;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;


public class ActivityReleaseNotes extends Activity {

    private HashMap<String, ArrayList<RDReleaseNote>> mRNDict;
    private ArrayList<String> mHeaders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_notes);
        doSetup();
    }

    private void doSetup() {
        readReleaseNotes();
        setupListView();
    }

    private void readReleaseNotes() {
        RDReleaseNotes rn = new RDReleaseNotes(this, true);
        mRNDict = rn.releaseNotesDict();
        mHeaders = new ArrayList<String>();
        for ( String version: mRNDict.keySet() ) {
            mHeaders.add(version);
        }
        Collections.sort(mHeaders);
    }

    private void setupListView() {
        AdapterReleaseNotes adapter = new AdapterReleaseNotes(this, mHeaders, mRNDict);
        ExpandableListView elv = (ExpandableListView)findViewById(R.id.elvReleaseNotes);
        elv.setAdapter(adapter);
        elv.setGroupIndicator(null);
        if ( mHeaders.size() > 0 ) {
            elv.expandGroup(mHeaders.size() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_release_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
