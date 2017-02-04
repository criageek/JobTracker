package com.rdfmobileapps.jobtracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ActivityWorklogs extends AppCompatActivity /* implements TextToSpeech.OnInitListener */ {

    private TextToSpeech mTTS;

    private MyDB mDBHelper;
    private Vibrator mVibe;
    private RDJob mJob;
    private ArrayList<RDWorklog> mWorklogs;
    private AdapterWorklogs mAdapter;
    private boolean mLoading = true;
    private boolean mEditing = true;

    private ListView mListView;

    private static final int RQS_RECOGNITION = 1;

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
        setContentView(R.layout.activity_worklogs);
        mDBHelper = MyDB.getInstance(this, RDConstants.DBNAME);
        mVibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        mJob = (RDJob)getIntent().getParcelableExtra(RDConstants.EXTRAKEY_JOB);
        getWorklogList();
//        mAdapter = new AdapterWorklog(this, mWorklogs);
        setupSpeechRecognition();
//        mTTS = new TextToSpeech(this, this);
        setupCustomActionBar();
        setupScreenControls();
    }

    private void setupSpeechRecognition() {

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
        TextView jobName = (TextView)findViewById(R.id.txvWLJobName);
        if (mJob != null) {
            jobName.setText(mJob.getJobName());
        }
        setupListView();
        loadData();
        setButtons();
    }

    private void setupListView() {
        mAdapter = new AdapterWorklogs(this, mWorklogs);
        mListView = (ListView)findViewById(R.id.lsvWLWorklogs);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mVibe.vibrate(RDConstants.BUTTON_VIBRATE_DURATION);
//                showWorklog(position);
            }
        });
    }

    /*    private void setupStatus() {
        mActiveRadioButton = (RadioButton)findViewById(R.id.radJobActive);
        mActiveRadioButton.setText(RDStatus.Active.toString());
        mCompleteRadioButton = (RadioButton)findViewById(R.id.radJobComplete);
        mCompleteRadioButton.setText(RDStatus.Complete.toString());
        mCanceledRadioButton = (RadioButton)findViewById(R.id.radJobCanceled);
        mCanceledRadioButton.setText(RDStatus.Canceled.toString());
    }
*/
    private void loadData() {
        mLoading = true;
        mLoading = false;
    }

    private void setupButtons() {
//        mSaveButton = (Button)findViewById(R.id.btnJobSave);
//        mCancelButton = (Button)findViewById(R.id.btnJobCancel);
//        mAddWorklogButton = (Button)findViewById(R.id.btnJobAddWorkLog);
    }

    private void getWorklogList() {
        mWorklogs = RDWorklog.worklogList(mDBHelper, mJob.getId(), true);
    }

    private void setButtons() {
/*        if (mEditing || mJob.getId() < 0) {
            mSaveButton.setVisibility(View.VISIBLE);
            mCancelButton.setVisibility(View.VISIBLE);
            mAddWorklogButton.setVisibility(View.INVISIBLE);
        } else {
            mSaveButton.setVisibility(View.INVISIBLE);
            mCancelButton.setVisibility(View.INVISIBLE);
            mAddWorklogButton.setVisibility(View.VISIBLE);
        }  */
    }

    private boolean dataOk() {
        boolean retVal = true;

        return retVal;
    }

    private RDStatus getSelectedStatus() {
/*        if (mActiveRadioButton.isChecked()) {
            return RDStatus.Active;
        } else {
            if (mCompleteRadioButton.isChecked()) {
                return RDStatus.Complete;
            } else {
                if (mCanceledRadioButton.isChecked()) {
                    return RDStatus.Canceled;
                } else {
                    return RDStatus.None;
                }
            }
        } */
        return RDStatus.Active;
    }

    private void validateData() {
        if (dataOk()) {
            mEditing = false;
            setButtons();
        }
    }

    public void onRadioButtonClick(View pButton) {
        if ( !mLoading ) {
            mEditing = true;
            setButtons();
        }
    }

    public void onSaveClick(View pSaveButton) {
//        checkEmployer();
    }

    public void onCancelClick(View pCancelButton) {
        loadData();
        mEditing = false;
        setButtons();
    }

    public void onAddWorklogClick(View pButton) {
        RDWorklog worklog = new RDWorklog();
        worklog.setJobId(mJob.getId());
        showWorklog(worklog);
    }

    public void onSpeakClick(View pButton) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speech to Recognize");
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        startActivityForResult(intent, RQS_RECOGNITION);
    }

    private void showWorklog(RDWorklog pWorklog) {
        Intent intent = new Intent(this, ActivityWorklog.class);
        intent.putExtra(RDConstants.EXTRAKEY_WORKLOG, pWorklog);
        intent.putExtra(RDConstants.EXTRAKEY_JOB, mJob);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_worklogs, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.action_show_job:
                showJob();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showJob() {
        Intent intent = new Intent(this, ActivityJob.class);
        intent.putExtra(RDConstants.EXTRAKEY_JOB, mJob);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int pRequestCode, int pResultCode, Intent pData) {
        String wordStr = null;
        String[] words = null;
        String firstWord = null;
        String secondWord = null;
        String thirdWord = null;
        if ( (pRequestCode == RQS_RECOGNITION) & (pResultCode == RESULT_OK) ) {
            ArrayList<String> matches = pData.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            int index = 0;
            boolean found = false;
            while ( (index < matches.size()) & !found) {
                wordStr = matches.get(index);
                words = wordStr.split(" ");
                firstWord = words[0];
                secondWord = words[1];
                thirdWord = words[2];
                if ((firstWord.equals("add")) & (secondWord.equals("work")) & (thirdWord.equals("log"))) {
                    found = true;
                }
            }
            if (found)
                onAddWorklogClick(null);
        }
    }

}
