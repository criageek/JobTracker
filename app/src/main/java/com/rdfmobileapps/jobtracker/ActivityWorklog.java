package com.rdfmobileapps.jobtracker;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ActivityWorklog extends AppCompatActivity implements TextWatcher,
        DatePickerDialog.OnDateSetListener,
        TimePickerDialog.OnTimeSetListener {

    private MyDB mDBHelper;
    private Vibrator mVibe;
    private RDWorklog mWorklog;
    private RDJob mJob;
    private boolean mLoading = true;
    private boolean mEditing = true;
    private RDSpeechCommand mCurrentCommand = RDSpeechCommand.None;
    private GestureDetector mGestureDetector;
    private RDDialogType mDialogType = RDDialogType.None;

    private TextView mStartDateTV;
    private TextView mStartTimeTV;
    private TextView mEndDateTV;
    private TextView mEndTimeTV;
    private TextView mTotalTimeTV;
    private EditText mStartMilesEdit;
    private EditText mEndMilesEdit;
    private EditText mTotalMilesEdit;
    private EditText mNotesEdit;
    private TextView mSelectedTV = null;
    private RadioButton mActiveRadioButton;
    private RadioButton mCompleteRadioButton;
    private RadioButton mCanceledRadioButton;
    private RDTopButtons mTopButtons;

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
        setContentView(R.layout.activity_worklog);
        mDBHelper = MyDB.getInstance(this, RDConstants.DBNAME);
        mVibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        mWorklog = (RDWorklog) getIntent().getParcelableExtra(RDConstants.EXTRAKEY_WORKLOG);
        mJob = (RDJob) getIntent().getParcelableExtra(RDConstants.EXTRAKEY_JOB);
        setupGestureDesector();
        setupCustomActionBar();
        setupScreenControls();
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
        TextView jobName = (TextView)findViewById(R.id.txvWLDJobName);
        if (mWorklog != null) {
            jobName.setText(mJob.getJobName());
        }
        mStartDateTV = (TextView)findViewById(R.id.txvWLDStartDateVal);
        mStartDateTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSelectedTV = mStartDateTV;
                mDialogType = RDDialogType.StartDate;
                mGestureDetector.onTouchEvent(event);
                return true;
//                return false;
            }
        });
        mStartTimeTV = (TextView)findViewById(R.id.txvWLDStartTimeVal);
        mStartTimeTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSelectedTV = mStartTimeTV;
                mDialogType = RDDialogType.StartTime;
                mGestureDetector.onTouchEvent(event);
                return false;
            }
        });
        mEndDateTV = (TextView)findViewById(R.id.txvWLDEndDateVal);
        mEndDateTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSelectedTV = mEndDateTV;
                mDialogType = RDDialogType.EndDate;
                mGestureDetector.onTouchEvent(event);
                return true;
//                return false;
            }
        });
        mEndTimeTV = (TextView)findViewById(R.id.txvWLDEndTimeVal);
        mEndTimeTV.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSelectedTV = mEndTimeTV;
                mDialogType = RDDialogType.EndTime;
                mGestureDetector.onTouchEvent(event);
                return false;
            }
        });
        mTotalTimeTV = (TextView)findViewById(R.id.txvWLDTotalTimeVal);
        mStartMilesEdit = (EditText)findViewById(R.id.txtWLDStartMilesVal);
        mStartMilesEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mLoading) {
                    handleMilesDataEntered(RDFieldEntered.StartMiles);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEndMilesEdit = (EditText)findViewById(R.id.txtWLDEndMilesVal);
        mEndMilesEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mLoading) {
                    handleMilesDataEntered(RDFieldEntered.EndMiles);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTotalMilesEdit = (EditText)findViewById(R.id.txtWLDTotalMilesVal);
        mTotalMilesEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mLoading) {
                    handleMilesDataEntered(RDFieldEntered.TotalMiles);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mNotesEdit = (EditText)findViewById(R.id.txtWLDNotes);
        mNotesEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!mLoading) {
                    mEditing = true;
                    setButtons();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        setupStatus();
        setupTopButtons();
        loadData();
        setButtons();
    }

    private void setupTopButtons() {
        mTopButtons = (RDTopButtons)findViewById(R.id.rdtbWorklog);
        mTopButtons.setSaveButtonVisible(true);
        mTopButtons.setCustomButtonVisible(true);
        mTopButtons.setCustomButtonVisible(false);
        mTopButtons.setOnRDTBClickedListener(new RDTopButtons.OnRDTBClickedListener() {
            @Override
            public void onCancelClick() {
                loadData();
                mEditing = false;
                setButtons();
            }

            @Override
            public void onSaveClick() {
            }

            @Override
            public void onCustomClick() {

            }
        });
    }

    private void setupStatus() {
        mActiveRadioButton = (RadioButton) findViewById(R.id.radWLDActive);
        mActiveRadioButton.setText(RDStatus.Active.toString());
        mCompleteRadioButton = (RadioButton) findViewById(R.id.radWLDComplete);
        mCompleteRadioButton.setText(RDStatus.Complete.toString());
        mCanceledRadioButton = (RadioButton) findViewById(R.id.radWLDCancel);
        mCanceledRadioButton.setText(RDStatus.Canceled.toString());
    }

    private boolean hasValue(EditText pEditText) {
        boolean retVal = true;
        if (pEditText.getText().toString().isEmpty() || (Double.valueOf(pEditText.getText().toString()) == 0)) {
            retVal = false;
        }
        return retVal;
    }

    private void calcTotalMiles() {
        double totalMiles = RDFunctions.stringToDouble(mEndMilesEdit.getText().toString()) -
                RDFunctions.stringToDouble(mStartMilesEdit.getText().toString());
        mLoading = true;
        mTotalMilesEdit.setText(RDFunctions.numberToStr(totalMiles, "%.1f"));
        mLoading = false;
    }

    private void calcEndMiles() {
        double startMiles = RDFunctions.stringToDouble(mStartMilesEdit.getText().toString());
        double totalMiles = RDFunctions.stringToDouble(mTotalMilesEdit.getText().toString());
        double endMiles = startMiles + totalMiles;
//        double endMiles = RDFunctions.stringToDouble(mStartMilesEdit.getText().toString()) +
//                RDFunctions.stringToDouble(mTotalMilesEdit.getText().toString());
        mLoading = true;
        mEndMilesEdit.setText(RDFunctions.numberToStr(endMiles, "%.1f"));
        mLoading = false;
    }

    private void calcStartMiles() {
        double startMiles = RDFunctions.stringToDouble(mEndMilesEdit.getText().toString()) -
                RDFunctions.stringToDouble(mTotalMilesEdit.getText().toString());

        mLoading = true;
        mStartMilesEdit.setText(RDFunctions.numberToStr(startMiles, "%.1f"));
        mLoading = false;
    }

    private void handleMilesDataEntered(RDFieldEntered pFieldEntered) {
        switch (pFieldEntered) {
            case StartMiles: {
                if (hasValue(mEndMilesEdit)) {
                    calcTotalMiles();
                } else {
                    if (hasValue(mTotalMilesEdit)) {
                        calcEndMiles();
                    }
                }
                break;
            }
            case EndMiles: {
                if (hasValue(mStartMilesEdit)) {
                    calcTotalMiles();
                } else {
                    if (hasValue(mTotalMilesEdit)) {
                        calcStartMiles();
                    }
                }
                break;
            }
            case TotalMiles: {
                if (hasValue(mStartMilesEdit) && hasValue(mEndMilesEdit)) {
                    calcEndMiles();
                } else {
                    if (hasValue(mStartMilesEdit)) {
                        calcEndMiles();
                    } else {
                        if (hasValue(mEndMilesEdit)) {
                            calcStartMiles();
                        }
                    }
                }
                break;
            }
        }
    }

    private void loadData() {
        mLoading = true;
        if (mWorklog.getStartTime().isEmpty()) {
            mStartDateTV.setText(RDFunctions.currentDateStr(RDConstants.DATE_FORMAT_SHORT));
            mStartTimeTV.setText(RDFunctions.currentDateStr("hh:mm"));
            mEndDateTV.setText("");
            mEndTimeTV.setText("");
        } else {
            mStartDateTV.setText(RDFunctions.dateFromDateTimeStr(mWorklog.getStartTime()));
            mStartTimeTV.setText(RDFunctions.timeFromDateTimeStr(mWorklog.getStartTime()));
            mEndDateTV.setText(RDFunctions.dateFromDateTimeStr(mWorklog.getEndTime()));
            mEndTimeTV.setText(RDFunctions.timeFromDateTimeStr(mWorklog.getEndTime()));
        }
// TODO: 1/31/17 Need to handle this
        mTotalTimeTV.setText(RDFunctions.numberToStr(mWorklog.getTotalTime(), "%d"));
        mStartMilesEdit.setText(RDFunctions.numberToStr(mWorklog.getStartMileage(), "%.1f"));
        mEndMilesEdit.setText(RDFunctions.numberToStr(mWorklog.getEndMileage(), "%.1f"));
        mTotalMilesEdit.setText(RDFunctions.numberToStr(mWorklog.getTotalMileage(), "%.1f"));
        if (mWorklog.getStatus() == RDStatus.Active) {
            mActiveRadioButton.setChecked(true);
        } else {
            if (mWorklog.getStatus() == RDStatus.Complete) {
                mCompleteRadioButton.setChecked(true);
            } else {
                if (mWorklog.getStatus() == RDStatus.Canceled) {
                    mCanceledRadioButton.setChecked(true);
                }
            }
        }
        mNotesEdit.setText(mWorklog.getNotes());
        mEditing = false;
        mLoading = false;
    }

    private void setButtons() {
        mTopButtons.setEditing(mEditing);
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

    private void showDatePickerDialog(String [] pDateParts) {
        String monthStr = pDateParts[0];
        String dayStr = pDateParts[1];
        String yearStr = pDateParts[2];
        DatePickerDialog dpd = new DatePickerDialog(ActivityWorklog.this,
                ActivityWorklog.this,
                Integer.valueOf(yearStr),
                Integer.valueOf(monthStr) - 1,
                Integer.valueOf(dayStr));
        dpd.show();
    }

    private void showTimePickerDialog(String[] pTimeParts) {
        String hourStr = pTimeParts[0];
        String minuteStr = pTimeParts[1];
        TimePickerDialog tpd = new TimePickerDialog(ActivityWorklog.this,
                ActivityWorklog.this,
                Integer.valueOf(hourStr),
                Integer.valueOf(minuteStr),
                false);
        tpd.show();
    }

    public void onSpeakStartTimeClick(View pButton) {
        mCurrentCommand = RDSpeechCommand.StartTime;
        promptForVoiceCommand("Speak Start Time");
    }

    public void onSpeakEndTimeClick(View pButton) {
        mCurrentCommand = RDSpeechCommand.EndTime;
        promptForVoiceCommand("Speak End Time");
    }

    public void onSpeakTotalTimeClick(View pButton) {
        mCurrentCommand = RDSpeechCommand.TotalTime;
        promptForVoiceCommand("Speak Total Time");
    }

    public void onSpeakStartMilesClick(View pButton) {
        mCurrentCommand = RDSpeechCommand.StartMiles;
        promptForVoiceCommand("Speak Start Miles");
    }

    public void onSpeakEndMilesClick(View pButton) {
        mCurrentCommand = RDSpeechCommand.EndMiles;
        promptForVoiceCommand("Speak End Miles");
    }

    public void onSpeakTotalMilesClick(View pButton) {
        mCurrentCommand = RDSpeechCommand.TotalMiles;
        promptForVoiceCommand("Speak Start Miles");
    }

    public void onSpeakClick(View pButton) {
        mCurrentCommand = RDSpeechCommand.None;
        promptForVoiceCommand("Speak Command Followed by Data");
    }

    private void promptForVoiceCommand(String pPrompt) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, pPrompt);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 12);
        startActivityForResult(intent, RQS_RECOGNITION);
    }

//  TextWatcher Methods

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if ( !mLoading ) {
            mEditing = true;
            setButtons();
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    private RDSpeechCommandResult checkSpeechForCommand(String pWordStr) {
        String[] words = pWordStr.split(" ");
        String[] dataWords;
        if ( words.length >= 2) {
             dataWords = new String[words.length - 2];
        } else {
            dataWords = new String[words.length];
        }
        RDSpeechCommandResult scr = new RDSpeechCommandResult(RDSpeechCommand.None, dataWords);
        String firstWord = "";
        String secondWord = "";
        String thirdWord = "";
        if (words.length >= 1) {
            firstWord = words[0];
        }
        if (words.length >= 2) {
            secondWord = words[1];
        }
        if (words.length >= 3) {
            thirdWord = words[2];
        }
        if ( secondWord.equals("date") ) {
            if ( firstWord.equals("start") ) {
                scr.setSpeechCommand(RDSpeechCommand.StartDate);
            } else {
                if (firstWord.equals("end") || firstWord.equals("and")) {
                    scr.setSpeechCommand(RDSpeechCommand.EndDate);
                }
            }
        } else {
            if (secondWord.equals("time")) {
                if (firstWord.equals("start")) {
                    scr.setSpeechCommand(RDSpeechCommand.StartTime);
                } else {
                    if (firstWord.equals("end") || firstWord.equals("and")) {
                        scr.setSpeechCommand(RDSpeechCommand.EndTime);
                    } else {
                        if (firstWord.equals("total")) {
                            scr.setSpeechCommand(RDSpeechCommand.TotalTime);
                        }
                    }
                }
            } else {
                if (secondWord.equals("miles") || secondWord.equals("mileage")) {
                    if (firstWord.equals("start")) {
                        scr.setSpeechCommand(RDSpeechCommand.StartMiles);
                    } else {
                        if (firstWord.equals("end") || firstWord.equals("and")) {
                            scr.setSpeechCommand(RDSpeechCommand.EndMiles);
                        } else {
                            if (firstWord.equals("total")) {
                                scr.setSpeechCommand(RDSpeechCommand.TotalMiles);
                            }
                        }
                    }
                }
            }
        }
        if (scr.getSpeechCommand() != RDSpeechCommand.None) {
            for (int index = 2; index < words.length; index++) {
                dataWords[index - 2] = words[index];
            }
            scr.setData(dataWords);
        }
        return scr;
    }

    private String parseForDate(String[] pData) {
        String myDate = RDConstants.INVALID;
        int index = 0;
        if (pData.length >= 2) {
            int monthNum = RDFunctions.monthNameToNum(pData[0]);
            if (monthNum != RDConstants.NOSELECTION) {
                int dayNum = RDFunctions.dayWordToNum(pData[1]);
                if (dayNum != RDConstants.NOSELECTION) {
                    if (pData.length >= 3) {
                        dayNum = dayNum + RDFunctions.dayWordToNum(pData[2]);
                    }
                }
                if (dayNum != RDConstants.NOSELECTION) {
                    myDate = RDFunctions.numberToStr(monthNum, "%02d") + "/" +
                            RDFunctions.numberToStr(dayNum, "%02d") + "/" +
                            RDFunctions.currentYearStr();
                }
            }
        }
        return myDate;
    }

    private String parseForTime(String[] pData) {
        String myTime = RDConstants.INVALID;
        if (pData.length >= 1) {
            String timeStr = pData[0];
            if (RDFunctions.isValidTime(timeStr)) {
                String[] timeParts = timeStr.split(":");
                int hourNum = Integer.valueOf(timeParts[0]);
                int minuteNum = Integer.valueOf(timeParts[1]);
                String ampm = "";
                if (pData.length >= 2) {
                    ampm = RDFunctions.formatAMPMStr(pData[1]);
                }
                if ((hourNum > 12) && (ampm.isEmpty())) {
                    hourNum = hourNum - 12;
                    ampm = "PM";
                } else {
                    if ((hourNum < 12) && (ampm.isEmpty())) {
                        ampm = "AM";
                    }
                }
                myTime = RDFunctions.formatTimeStr(hourNum,
                                                   minuteNum,
                                                   ampm);
            }
        }
        return myTime;
    }

    private String parseAllMatchesForTime(String[] pData) {
        String timeStr = RDConstants.INVALID;
        int index = 0;
        while ((index < pData.length) &&(timeStr.equals(RDConstants.INVALID))) {
            String wordsList = pData[index];
            String[] words = wordsList.split(" ");
            timeStr = parseForTime(words);
            index++;
        }
        return timeStr;
    }

    private double parseForDouble(ArrayList<String> pMatches) {
        double myDouble = -1.0;
        String doubleStr = "";
        String word = "";
        boolean invalid = false;
        int index = 0;
        while ((index < pMatches.size()) && doubleStr.equals("")) {
            String wordStr = pMatches.get(index);
            String[] words = wordStr.split(" ");
            int index2 = 0;
            doubleStr = "";
            invalid = false;
            while ((index2 < words.length) && !invalid) {
                word = words[index2];
                if (RDFunctions.isValidDouble(word) || word.equals(".")) {
                    doubleStr = doubleStr + word;
                    index2++;
                } else {
                    doubleStr = "";
                    invalid = true;
                }
            }
            index++;
        }
        if (RDFunctions.isValidDouble(doubleStr)) {
            myDouble = RDFunctions.stringToDouble(doubleStr);
        }
        return myDouble;
    }

    private String parseForMiles(String[] pData) {
        int index = 0;
        String doubleStr = "";
        boolean invalid = false;
        while ((index < pData.length) && !invalid) {
            String word = pData[index];
            if (RDFunctions.isValidDouble(word) || word.equals(".")) {
                doubleStr = doubleStr + word;
                index++;
            } else {
                doubleStr = "";
                invalid = true;
            }
        }
        if (!RDFunctions.isValidDouble(doubleStr)) {
            doubleStr = RDConstants.INVALID;
        }
        return doubleStr;
    }

    private double parseForDouble(String[] pData) {
        double myDouble = -1.0;
        return myDouble;
    }

    private boolean voiceCommandProcessed(RDSpeechCommandResult pSCR) {
        boolean processed = false;
        String dataStr = "";
        RDSpeechCommand command = pSCR.getSpeechCommand();
        switch (command) {
            case StartDate: {
                dataStr = parseForDate(pSCR.getData());
                if (dataStr != RDConstants.INVALID) {
                    mStartDateTV.setText(dataStr);
                    processed = true;
                }
                break;
            }
            case EndDate: {
                dataStr = parseForDate(pSCR.getData());
                if (dataStr != RDConstants.INVALID) {
                    mEndDateTV.setText(dataStr);
                    processed = true;
                }
                break;
            }
            case StartTime: {
                dataStr = parseForTime(pSCR.getData());
                if (dataStr != RDConstants.INVALID) {
                    mStartTimeTV.setText(dataStr);
                    processed = true;
                }
                break;
            }
            case EndTime: {
                dataStr = parseForTime(pSCR.getData());
                if (dataStr != RDConstants.INVALID) {
                    mEndTimeTV.setText(dataStr);
                    processed = true;
                }
                break;
            }
            case TotalTime: {
// TODO: 2/1/17 Need to figure out how to handle this
                break;
            }
            case StartMiles: {
                dataStr = parseForMiles(pSCR.getData());
                if (dataStr != RDConstants.INVALID) {
                    mStartMilesEdit.setText(dataStr);
                    processed = true;
                }
                break;
            }
            case EndMiles: {
                dataStr = parseForMiles(pSCR.getData());
                if (dataStr != RDConstants.INVALID) {
                    mEndMilesEdit.setText(dataStr);
                    processed = true;
                }
                break;
            }
            case TotalMiles: {
                dataStr = parseForMiles(pSCR.getData());
                if (dataStr != RDConstants.INVALID) {
                    mTotalMilesEdit.setText(dataStr);
                    processed = true;
                }
                break;
            }
        }
        return processed;
    }

    @Override
    protected void onActivityResult(int pRequestCode, int pResultCode, Intent pData) {
        String wordStr = null;
        if ( (pRequestCode == RQS_RECOGNITION) & (pResultCode == RESULT_OK) ) {
            ArrayList<String> matches = pData.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            int index = 0;
            if (mCurrentCommand == RDSpeechCommand.None) {
                RDSpeechCommandResult scr = new RDSpeechCommandResult(RDSpeechCommand.None, new String[12]);
                while ((index < matches.size()) & (scr.getSpeechCommand() == RDSpeechCommand.None)) {
                    wordStr = matches.get(index);
                    scr = checkSpeechForCommand(wordStr);
                    if (scr.getSpeechCommand() != RDSpeechCommand.None) {
                        if (!voiceCommandProcessed(scr)) {
                            scr.setSpeechCommand(RDSpeechCommand.None);
                        }
                    }
                    index++;
                }
                if (scr.getSpeechCommand() == RDSpeechCommand.None) {
                    Toast.makeText(getApplicationContext(), "Command not recognized",
                            Toast.LENGTH_LONG).show();
                }
            } else {
                handleSpeechCommand(RDFunctions.arrayListToStringArray(matches));
            }
        }
    }

    private String parseForDataStr(ArrayList<String> pMatches,
                                   RDDataFormat pDataFormat) {
        String numberStr = "";
        String wordStr = null;
        int index = 0;
        boolean found = false;
        while (!found & (index < pMatches.size())) {
            wordStr = pMatches.get(index);
            String[] words = wordStr.split(" ");
            String firstWord = null;
            String secondWord = null;
            String thirdWord = null;
            if (words.length > 0) {
                firstWord = words[0];
                if (words.length > 1) {
                    secondWord = words[1];
                    if (words.length > 2) {
                        thirdWord = words[2];
                    }
                }
            }
            if (pDataFormat == RDDataFormat.Time) {
                if (RDFunctions.isValidTime(firstWord)) {
                    numberStr = firstWord;
                    found = true;
                }
            } else {
                if (pDataFormat == RDDataFormat.Double) {
                    if (RDFunctions.isValidDouble(firstWord)) {
                        numberStr = firstWord;
                        found = true;
                    }
                }
            }
            index++;
        }
        return numberStr;
    }

    private String AddCurrentDateToTime(String pTimeStr) {
        return RDFunctions.currentDateStr(RDConstants.DATE_FORMAT_SHORT_YEARFIRST) + " " + pTimeStr;
    }

    private void handleSpeechCommand(String[] pData) {
        String numberStr = "";
        double myDouble = 0.0;
        switch (mCurrentCommand) {
            case StartTime: {
                numberStr = parseAllMatchesForTime(pData);
                if (!numberStr.equals(RDConstants.INVALID)) {
                    mStartTimeTV.setText(numberStr);
                }
                break;
            }
            case EndTime: {
                numberStr = parseAllMatchesForTime(pData);
                if (!numberStr.equals(RDConstants.INVALID)) {
                    mEndTimeTV.setText(numberStr);
                }
                break;
            }
            case TotalTime: {
                numberStr = parseAllMatchesForTime(pData);
                if (!numberStr.equals(RDConstants.INVALID)) {
                    mTotalTimeTV.setText(numberStr);
                }
                break;
            }
            case StartMiles: {
                numberStr = parseForMiles(pData);
                if (!numberStr.equals(RDConstants.INVALID)) {
                    mStartMilesEdit.setText(RDFunctions.numberToStr(myDouble, "%.1f"));
                }
                break;
            }
            case EndMiles: {
                numberStr = parseForMiles(pData);
                if (!numberStr.equals(RDConstants.INVALID)) {
                    mEndMilesEdit.setText(RDFunctions.numberToStr(myDouble, "%.1f"));
                }
                break;
            }
            case TotalMiles: {
                numberStr = parseForMiles(pData);
                if (!numberStr.equals(RDConstants.INVALID)) {
                    mTotalMilesEdit.setText(RDFunctions.numberToStr(myDouble, "%.1f"));
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    private void setData(RDSpeechCommand pCommand,
                         String[] pData) {
        String dataStr = "";
        double myDouble = 0.0;
        switch (pCommand) {
            case StartDate: {
                dataStr = parseForDate(pData);
                mStartTimeTV.setText(dataStr);
                break;
            }
            case EndDate: {
                dataStr = parseForDate(pData);
                mEndTimeTV.setText(dataStr);
                break;
            }
            case StartTime: {
                dataStr = parseForTime(pData);
                mStartTimeTV.setText(dataStr);
                break;
            }
            case EndTime: {
                dataStr = parseForTime(pData);
                mEndTimeTV.setText(dataStr);
                break;
            }
            case TotalTime: {
                dataStr = parseForTime(pData);
                mTotalTimeTV.setText(dataStr);
                break;
            }
            case StartMiles: {
                myDouble = parseForDouble(pData);
                if (myDouble >= 0) {
                    mStartMilesEdit.setText(RDFunctions.numberToStr(myDouble, "%.1f"));
                } else {
                    mStartMilesEdit.setText("");
                }
                break;
            }
            case EndMiles: {
                myDouble = parseForDouble(pData);
                if (myDouble >= 0) {
                    mEndMilesEdit.setText(RDFunctions.numberToStr(myDouble, "%.1f"));
                } else {
                    mStartMilesEdit.setText("");
                }
                break;
            }
            case TotalMiles: {
                myDouble = parseForDouble(pData);
                if (myDouble >= 0) {
                    mTotalMilesEdit.setText(RDFunctions.numberToStr(myDouble, "%.1f"));
                } else {
                    mStartMilesEdit.setText("");
                }
                break;
            }
            default: {
                break;
            }
        }
    }

    private String[] getTimeParts(String pTimeStr,
                                  boolean pAMPMIndicator) {
        String[] timeParts;
        String pAMPM = "AM";
        if (pAMPMIndicator) {
            if (pTimeStr.length() >= 8) {
                pAMPM = pTimeStr.substring(6, 8);
                if (pAMPM.equals("PM")) {
                    int hour = Integer.valueOf(pTimeStr.substring(0, 2));
                    hour = hour + 12;
                    pTimeStr = RDFunctions.numberToStr(hour, "%02d") + pTimeStr.substring(2, 5);
                }
            }
        }
        timeParts = pTimeStr.split(":");
        return timeParts;
    }

// initialize the Gesture Detector
    private void setupGestureDesector() {
        mGestureDetector = new GestureDetector(this, new GestureDetector.OnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                                    float distanceY) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                String timeParts[] = {};
                String dateParts[] = {};
                String dataStr = "";
                boolean okToProcess = true;
                switch (mDialogType) {
                    case StartDate: {
                        dataStr = mStartDateTV.getText().toString();
                        break;
                    }
                    case EndDate: {
                        dataStr = mEndDateTV.getText().toString();
                        break;
                    }
                    case StartTime: {
                        dataStr = mStartTimeTV.getText().toString();
                        break;
                    }
                    case EndTime: {
                        dataStr = mEndTimeTV.getText().toString();
                        break;
                    }
                    default: {
                        break;
                    }
                }
                if ( okToProcess ) {
                    if ((mDialogType == RDDialogType.StartDate) || (mDialogType == RDDialogType.EndDate)) {
                        if (dataStr.isEmpty()) {
                            String currentDate = RDFunctions.currentDateStr(RDConstants.DATE_FORMAT_SHORT);
                            dateParts = currentDate.split("/");
                        } else {
                            if (RDFunctions.isValidDate(dataStr, RDConstants.DATE_FORMAT_SHORT)) {
                                dateParts = dataStr.split("/");
                            } else {
                                String currentDate = RDFunctions.currentDateStr(RDConstants.DATE_FORMAT_SHORT);
                                dateParts = currentDate.split("/");
                            }
                        }
                        showDatePickerDialog(dateParts);
                    } else {
                        if (dataStr.isEmpty()) {
                            String currentTime = RDFunctions.currentDateStr("hh:mm a");
                            timeParts = getTimeParts(currentTime, true);
                        } else {
                            if (RDFunctions.isValidTime(dataStr)) {
                                timeParts = getTimeParts(dataStr, true);
                            } else {
                                String currentTime = RDFunctions.currentDateStr("hh:mm a");
                                timeParts = getTimeParts(currentTime, true);
                            }
                        }
                        showTimePickerDialog(timeParts);
                    }
                }
            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                                   float velocityY) {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
            }
        });

// set the on Double tap listener
        mGestureDetector.setOnDoubleTapListener(new GestureDetector.OnDoubleTapListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {

                return false;
            }

            @Override
            public boolean onDoubleTapEvent(MotionEvent e) {
                // if the second tap hadn't been released and it's being moved
                if ((mSelectedTV == mStartDateTV) || (mSelectedTV == mStartTimeTV)){
                    mStartDateTV.setText(RDFunctions.currentDateStr(RDConstants.DATE_FORMAT_SHORT));
                    mStartTimeTV.setText(RDFunctions.currentDateStr("hh:mm"));
                } else {
                    if ((mSelectedTV == mEndDateTV) || (mSelectedTV == mEndTimeTV)) {
                        mEndDateTV.setText(RDFunctions.currentDateStr(RDConstants.DATE_FORMAT_SHORT));
                        mEndTimeTV.setText(RDFunctions.currentDateStr("hh:mm"));
                    }
                }
                mSelectedTV = null;
                return true;
//                return false;
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // TODO Auto-generated method stub
                return false;
            }

        });
    }

    @Override
    public void onDateSet(DatePicker pDatePicker,
                          int pYear,
                          int pMonth,
                          int pDay) {
        String dateStr = RDFunctions.numberToStr(pMonth + 1, "%02d") + "/" +
                RDFunctions.numberToStr(pDay, "%02d") + "/" +
                String.valueOf(pYear);
        switch (mDialogType) {
            case StartDate: {
                mStartDateTV.setText(dateStr);
                break;
            }
            case EndDate: {
                mEndDateTV.setText(dateStr);
                break;
            }
        }
        mEditing = true;
        setButtons();
    }

    @Override
    public void onTimeSet(TimePicker pTimePicker,
                          int pHour,
                          int pMinute) {
        String timeStr = RDFunctions.numberToStr(pHour, "%02d") + ":" +
                RDFunctions.numberToStr(pMinute, "%02d");
        switch (mDialogType) {
            case StartTime: {
                mStartTimeTV.setText(timeStr);
                break;
            }
            case EndTime: {
                mEndTimeTV.setText(timeStr);
                break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_worklog, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        //respond to menu item selection
        switch (item.getItemId()) {
            case R.id.action_delete:
                handleDelete();
                return true;
            case R.id.action_show_job:
                showJob();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void handleDelete() {

    }

    private void showJob() {
        Intent intent = new Intent(this, ActivityJob.class);
        intent.putExtra(RDConstants.EXTRAKEY_JOB, mJob);
        startActivity(intent);
    }

    public void onRadioButtonClick(View pButton) {
        if ( !mLoading ) {
            mEditing = true;
            setButtons();
        }
    }

//  End Button click handlers

//  RDFieldEntered enum implementation

    public enum RDFieldEntered {
        None("None", 0),
        StartDate("Start Date", 1),
        EndDate("End Date", 2),
        StartTime("Start Time", 3),
        EndTime("End Time", 4),
        TotalTime("Total Time", 5),
        StartMiles("Start Miles", 6),
        EndMiles("End Miles", 7),
        TotalMiles("Total Miles", 8);

        private String stringValue;
        private int intValue;
        private static Map<Integer, RDFieldEntered> map = new HashMap<Integer, RDFieldEntered>();

        private RDFieldEntered(String toString, int value) {
            stringValue = toString;
            intValue = value;
        }

        static {
            for (RDFieldEntered fieldEnum : RDFieldEntered.values()) {
                map.put(fieldEnum.intValue, fieldEnum);
            }
        }

        private RDFieldEntered(final int pField) {
            intValue = pField;
        }

        public static RDFieldEntered valueOf(int pField) {
            return map.get(pField);
        }

        public int getValue() {
            return intValue;
        }

        @Override
        public String toString() {
            return stringValue;
        }

    }

//  End RDFieldEntered enum implementation
}
