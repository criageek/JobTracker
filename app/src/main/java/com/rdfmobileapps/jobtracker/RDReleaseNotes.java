package com.rdfmobileapps.jobtracker;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;

import com.rdfmobileapps.jobtracker.RDConstants;
import com.rdfmobileapps.jobtracker.RDReleaseNote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Created by rich on 2/2/15.
 */
public class RDReleaseNotes {
    private Context mContext;

    private ArrayList<RDReleaseNote> mReleaseNotesList;

//  Constructors
    public RDReleaseNotes(Context pContext,
                          boolean pReadAll) {
        mContext = pContext;
        doDefaultSetup();
        if ( pReadAll ) {
            readReleaseNotes();
        }
    }

    private void doDefaultSetup() {
        mReleaseNotesList = new ArrayList<RDReleaseNote>();
    }

//  Getters
    public ArrayList<RDReleaseNote> getReleaseNotesList() {
        return mReleaseNotesList;
    }

    private void readReleaseNotes() {
        AssetManager assetManager = mContext.getAssets();
        InputStream in = null;
        try {
            in = assetManager.open(RDConstants.RELEASENOTES_FILENAME);
            if ( in != null ) {
                InputStreamReader tmp = new InputStreamReader(in);
                BufferedReader reader=new BufferedReader(tmp);
                String str;
                while ( (str = reader.readLine()) != null ) {
                    parseStr(str);
                }
                in.close();
            }
        } catch ( IOException e ) {
            Log.e("tag", "Exception reading release notes file (" + RDConstants.RELEASENOTES_FILENAME + "):  " + e.toString());
        }
//        Collections.sort(mReleaseNotesList, new NotesComparator(true, 0));
        Collections.sort(mReleaseNotesList, new RDReleaseNotesComparator(true, 0));
    }

    private void parseStr(String pString) {
        StringTokenizer tokenizer = new StringTokenizer(pString, "|");
        int count = 0;
        RDReleaseNote rn = new RDReleaseNote();
        while ( tokenizer.hasMoreElements() ) {
            switch ( count ) {
                case 0:
                    rn.setVersion(tokenizer.nextToken());
                    break;
                case 1:
                    rn.setDate(tokenizer.nextToken());
                    break;
                case 2:
                    rn.setTitle(tokenizer.nextToken());
                    break;
                case 3:
                    rn.setDescription(tokenizer.nextToken());
                    break;
                case 4:
                    rn.parseAffectedFilesStr(tokenizer.nextToken());
                    break;
            }
            count++;
        }
        if ( !rn.getVersion().equals(RDConstants.RELEASENOTES_COLNAME_VERSION) ) {
            mReleaseNotesList.add(rn);
        }
    }

//  Public Methods
    public ArrayList<String> versionsList() {
        ArrayList<String> list = new ArrayList<String>();
        Iterator<RDReleaseNote> iterRN = mReleaseNotesList.iterator();
        return list;
    }

    public HashMap<String, ArrayList<RDReleaseNote>> releaseNotesDict() {
        HashMap<String, ArrayList<RDReleaseNote>> dict = new HashMap<String, ArrayList<RDReleaseNote>>();
        String lastHeader = "";
        ArrayList<RDReleaseNote> rnList = null;
        Iterator<RDReleaseNote> iterRN = mReleaseNotesList.iterator();
        while ( iterRN.hasNext() ) {
            RDReleaseNote rn = iterRN.next();
            if ( lastHeader.length() == 0 ) {
                lastHeader = rn.getVersion();
                rnList = new ArrayList<RDReleaseNote>();
            }
            if ( !rn.getVersion().equals(lastHeader) ) {
                dict.put(lastHeader, rnList);
                lastHeader = rn.getVersion();
                rnList = new ArrayList<RDReleaseNote>();
            }
            rnList.add(rn);
        }
        if ( rnList.size() > 0 ) {
            dict.put(lastHeader, rnList);
        }
        return dict;
    }
}
