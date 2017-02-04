package com.rdfmobileapps.jobtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.ArrayList;
import java.util.Iterator;

public class ActivityJob extends AppCompatActivity implements TextWatcher, DatePickerDialog.OnDateSetListener {

    private MyDB mDBHelper;
    private Vibrator mVibe;
    private AdapterEmployer mAdapter;
    private RDJob mJob;
    private int mMonth, mDay, mYear;
    private int mMonthFinal, mDayFinal, mYearFinal;
    private static final int mDialogId = 0;
    private String mDateDialogType;
    private boolean mEditing = false;
    private boolean mLoading = true;
    private boolean mDialogShowing = false;
    private ArrayList<RDEmployer> mEmployers;
    //    private int mSelectedRow = RDConstants.NOSELECTION;
    private RDEmployer mSelectedEmployer = null;
    private int mEmployerId = RDConstants.NOSELECTION;

    private EditText mJobNameEdit;
    private EditText mEmployerEdit;
    private TextView mStartDateTV;
    private TextView mEndDateTV;
    private EditText mHourlyRateEdit;
    private EditText mNotesEdit;
    private EditText mStatusEdit;
    private Button mCustomButton;
    private RDTopButtons mTopButtons;
    //    private RelativeLayout mEmployerPrompt;
    private ListView mEmployersListView;
    private RadioButton mActiveRadioButton;
    private RadioButton mCompleteRadioButton;
    private RadioButton mCanceledRadioButton;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient mClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doSetup();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        mClient = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void doSetup() {
        if (ViewConfiguration.get(this).hasPermanentMenuKey()) {
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        setContentView(R.layout.activity_job);
        mJob = (RDJob) getIntent().getParcelableExtra(RDConstants.EXTRAKEY_JOB);
        mDBHelper = MyDB.getInstance(this, RDConstants.DBNAME);
        mVibe = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);
        getEmployersList();
        mAdapter = new AdapterEmployer(this, mEmployers);
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
//        setupVersionLabel();
        setupTopButtons();
        setupJobName();
        setupEmployer();
        setupStartDate();
        setupEndDate();
        setupHourlyRate();
        setupNotes();
        setupStatus();
        setupEmployerPrompt();
        if (mJob.getId() >= 0) {
            mEditing = false;
        } else {
            mEditing = true;
        }
        loadData();
        mTopButtons.setEditing(mEditing);
    }

    private void setupTopButtons() {
        mTopButtons = (RDTopButtons)findViewById(R.id.rdtbJob);
        mTopButtons.setCustomButtonVisible(false);
        mTopButtons.setOnRDTBClickedListener(new RDTopButtons.OnRDTBClickedListener() {
            @Override
            public void onCancelClick() {
                loadData();
                mEditing = false;
                mTopButtons.setEditing(mEditing);
            }

            @Override
            public void onSaveClick() {
                checkEmployer();
            }

            @Override
            public void onCustomClick() {

            }
        });
    }

    /*    private void setupVersionLabel() {
            TextView tv = (TextView)findViewById(R.id.txvJobVersion);
            RDVersion versionInfo = new RDVersion(this);
            tv.setText(versionInfo.getVersionNum(true));
        }
    */
    private void setupJobName() {
        mJobNameEdit = (EditText) findViewById(R.id.txtJobJobNameVal);
        mJobNameEdit.addTextChangedListener(this);
    }

    private String lookupEmployerName(String pString) {
        String name = "";
        Iterator<RDEmployer> iterEmployers = mEmployers.iterator();
        while (iterEmployers.hasNext() && name.isEmpty()) {
            RDEmployer employer = iterEmployers.next();
            if (employer.getEmployerName().toLowerCase().startsWith(pString.toLowerCase())) {
                name = employer.getEmployerName();
            }
        }
        return name;
    }

    private void setupEmployer() {
        mEmployerEdit = (EditText) findViewById(R.id.txtJobEmployerVal);
        mEmployerEdit.setOnTouchListener(new TextView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                promptForEmployer();
                return false;
            }
        });
        mEmployerEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
/*                if (!mLoading) {
                    String name = lookupEmployerName(mEmployerEdit.getText().toString());
                    if (!name.isEmpty()) {
                        boolean loadingSave = mLoading;
                        mLoading = true;
                        mEmployerEdit.setText(name);
                        mLoading = loadingSave;
                    }
                }
*/
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupStartDate() {
        mStartDateTV = (TextView) findViewById(R.id.txvJobStartDateVal);
    }

    private void setupEndDate() {
        mEndDateTV = (TextView) findViewById(R.id.txvJobEndDateVal);
    }

    private void setupHourlyRate() {
        mHourlyRateEdit = (EditText) findViewById(R.id.txtJobHourlyRateVal);
        mHourlyRateEdit.addTextChangedListener(this);
    }

    private void setupNotes() {
        mNotesEdit = (EditText) findViewById(R.id.txtJobNotesVal);
        mNotesEdit.addTextChangedListener(this);
    }

    private void setupStatus() {
        mActiveRadioButton = (RadioButton) findViewById(R.id.radJobActive);
        mActiveRadioButton.setText(RDStatus.Active.toString());
        mCompleteRadioButton = (RadioButton) findViewById(R.id.radJobComplete);
        mCompleteRadioButton.setText(RDStatus.Complete.toString());
        mCanceledRadioButton = (RadioButton) findViewById(R.id.radJobCanceled);
        mCanceledRadioButton.setText(RDStatus.Canceled.toString());
    }

    private void loadData() {
        mLoading = true;
        mJobNameEdit.setText(mJob.getJobName());
        mSelectedEmployer = RDEmployer.lookupById(mDBHelper, mJob.getEmployerId());
        if (mSelectedEmployer != null)
            mEmployerEdit.setText(mSelectedEmployer.getEmployerName());
        else
            mEmployerEdit.setText("");
        mStartDateTV.setText(RDFunctions.convertDateFormat(mJob.getStartDate(),
                RDConstants.DATE_FORMAT_SHORT_YEARFIRST,
                RDConstants.DATE_FORMAT_SHORT));
        mEndDateTV.setText(RDFunctions.convertDateFormat(mJob.getEndDate(),
                RDConstants.DATE_FORMAT_SHORT_YEARFIRST,
                RDConstants.DATE_FORMAT_SHORT));
        mHourlyRateEdit.setText(RDFunctions.numberToStr(mJob.getHourlyRate(), "%.2f"));
        mNotesEdit.setText(mJob.getNotes());
        if (mJob.getStatus() == RDStatus.Active) {
            mActiveRadioButton.setChecked(true);
        } else {
            if (mJob.getStatus() == RDStatus.Complete) {
                mCompleteRadioButton.setChecked(true);
            } else {
                if (mJob.getStatus() == RDStatus.Canceled) {
                    mCanceledRadioButton.setChecked(true);
                }
            }
        }
        mLoading = false;
    }

    private void setupEmployerPrompt() {
//        mEmployerPrompt = (RelativeLayout)findViewById(R.id.rloEmployerPrompt);
//        mEmployerPrompt.bringToFront();
//        mEmployerPrompt.setVisibility(View.INVISIBLE);
    }

    private void getEmployersList() {
        mEmployers = RDEmployer.employersList(mDBHelper, true);
    }

    public void onStartDateClick(View pButton) {
        mDateDialogType = "START";
        String[] dateParts = mStartDateTV.getText().toString().split("/");
        showDatePickerDialog(dateParts);
    }

    public void onEndDateClick(View pButton) {
        mDateDialogType = "END";
        String[] dateParts = mEndDateTV.getText().toString().split("/");
        showDatePickerDialog(dateParts);
    }

    private void showDatePickerDialog(String[] pDateParts) {
        String monthStr = pDateParts[0];
        String dayStr = pDateParts[1];
        String yearStr = pDateParts[2];
        DatePickerDialog dpd = new DatePickerDialog(ActivityJob.this,
                ActivityJob.this,
                Integer.valueOf(yearStr),
                Integer.valueOf(monthStr) - 1,
                Integer.valueOf(dayStr));
        dpd.show();
    }

    private boolean dataOk() {
        boolean retVal = true;
        if (mJobNameEdit.getText().toString().isEmpty()) {
            retVal = false;
            Toast.makeText(getApplicationContext(), "Enter a Job Name before saving", Toast.LENGTH_LONG).show();
        } else {
            if (mStartDateTV.getText().toString().isEmpty()) {
                retVal = false;
                Toast.makeText(getApplicationContext(), "Enter a Start Date before saving", Toast.LENGTH_LONG).show();
            } else {
                if (mStartDateTV.getText().toString().isEmpty()) {
                    retVal = false;
                    Toast.makeText(getApplicationContext(), "Enter an End Date before saving", Toast.LENGTH_LONG).show();
                }
            }
        }
        return retVal;
    }

    private void promptForEmployer() {
        if (!mDialogShowing) {
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            //        mSelectedRow = RDConstants.NOSELECTION;
            //        alertDialog.setTitle("Unknown Employer");
            LayoutInflater inflater = this.getLayoutInflater();
            RelativeLayout rlo = (RelativeLayout) inflater.inflate(R.layout.rlo_employer_promp, null);
            ListView employersLV = (ListView) rlo.findViewById(R.id.lsvJobEmployers);
            employersLV.setAdapter(mAdapter);
            final EditText empName = (EditText) rlo.findViewById(R.id.txtEPNewEmployerName);
            empName.setText(mEmployerEdit.getText().toString());
            employersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    mVibe.vibrate(RDConstants.BUTTON_VIBRATE_DURATION);
                    //                    mSelectedRow = position;
                    mSelectedEmployer = mEmployers.get(position);
                    empName.setText(mSelectedEmployer.getEmployerName());
                }
            });
            alertDialog.setView(rlo)
                    // Add action buttons
                    .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            if (!empName.getText().toString().isEmpty()) {
                                if (!empName.getText().toString().equals(mSelectedEmployer.getEmployerName())) {
                                    mSelectedEmployer = new RDEmployer();
                                    mSelectedEmployer.setEmployerName(empName.getText().toString());
                                    mSelectedEmployer.save(mDBHelper);
                                    getEmployersList();
                                    mAdapter.refreshList(mEmployers);
                                }
                                mDialogShowing = false;
                                selectEmployer();
                            }
                            //                    if (mSelectedRow != RDConstants.NOSELECTION) {
                            //                        selectEmployer(mSelectedRow);
                            //                        dialog.dismiss();
                            //                    }
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            mDialogShowing = false;
                            dialog.cancel();
                        }
                    });

            final AlertDialog dialog = alertDialog.create();
    /*        Button btnEPAdd = (Button)rlo.findViewById(R.id.btnEPAdd);
            btnEPAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ( !empName.getText().toString().isEmpty() ) {
                        RDEmployer newEmployer = new RDEmployer();
                        newEmployer.setEmployerName(empName.getText().toString());
                        newEmployer.save(mDBHelper);
                        getEmployersList();
                        mEmployerId = newEmployer.getId();
                        mAdapter.refreshList(mEmployers);
                        mEmployerTV.setText(empName.getText().toString());
                        dialog.dismiss();
                    }
                    }
            }); */
            mDialogShowing = true;
            dialog.show();
        }
    }

    private void selectEmployer() {
        mEmployerId = mSelectedEmployer.getId();
        mLoading = true;
        mEmployerEdit.setText(mSelectedEmployer.getEmployerName());
        mEditing = true;
        mTopButtons.setEditing(mEditing);
        mLoading = false;
    }

    private RDStatus getSelectedStatus() {
        if (mActiveRadioButton.isChecked()) {
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
        }
    }

    private void validateData() {
        if (dataOk()) {
            mJob.setEmployerId(mEmployerId);
            mJob.setJobName(mJobNameEdit.getText().toString());
            mJob.setStartDate(RDFunctions.convertDateFormat(mStartDateTV.getText().toString(),
                    RDConstants.DATE_FORMAT_SHORT,
                    RDConstants.DATE_FORMAT_SHORT_YEARFIRST));
            mJob.setEndDate(RDFunctions.convertDateFormat(mEndDateTV.getText().toString(),
                    RDConstants.DATE_FORMAT_SHORT,
                    RDConstants.DATE_FORMAT_SHORT_YEARFIRST));
            mJob.setHourlyRate(Double.parseDouble(mHourlyRateEdit.getText().toString()));
            mJob.setNotes(mNotesEdit.getText().toString());
            mJob.setStatus(getSelectedStatus());
            mJob.save(mDBHelper);
            mEditing = false;
            mTopButtons.setEditing(mEditing);
        }
    }

    private void checkEmployer() {
        if (mEmployerEdit.getText().toString().isEmpty()) {
            Toast.makeText(getApplicationContext(), "Enter an Employer before saving", Toast.LENGTH_LONG).show();
        } else {
            RDEmployer employer = RDEmployer.lookupByName(mDBHelper,
                    mEmployerEdit.getText().toString());
            if (employer == null) {
                promptForEmployer();
            } else {
                validateData();
            }
        }
    }

    public void onRadioButtonClick(View pButton) {
        if (!mLoading) {
            mEditing = true;
            mTopButtons.setEditing(mEditing);
        }
    }

    public void onEmployerClick(View pButton) {
        promptForEmployer();
    }

//  TextWatcher Methods

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (!mLoading) {
            mEditing = true;
            mTopButtons.setEditing(mEditing);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onDateSet(DatePicker pDatePicker,
                          int pYear,
                          int pMonth,
                          int pDay) {
        String dateStr = RDFunctions.numberToStr(pMonth + 1, "%02d") + "/" +
                RDFunctions.numberToStr(pDay, "%02d") + "/" +
                String.valueOf(pYear);
        if (mDateDialogType.equals("START")) {
            mStartDateTV.setText(dateStr);
        } else {
            mEndDateTV.setText(dateStr);
        }
        mEditing = true;
        mTopButtons.setEditing(mEditing);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
/*    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("ActivityJob Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        mClient.connect();
        AppIndex.Ap
        pIndexApi.start(mClient, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(mClient, getIndexApiAction());
        mClient.disconnect();
    }
*/
}

