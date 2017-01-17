package com.rdfmobileapps.jobtracker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class ActivityJob extends Activity implements TextWatcher {

    private MyDB mDBHelper;
    private AdapterEmployer mAdapter;
    private RDJob mJob;
    private int mMonth, mDay, mYear;
    private static final int mDialogId = 0;
    private String mDateDialogType;
    private boolean mEditing = false;
    private boolean mLoading = true;
    private int mSelectedRowSave;
    private ArrayList<RDEmployer> mEmployers;

    private EditText mJobNameEdit;
    private EditText mEmployerEdit;
    private EditText mStartDateEdit;
    private EditText mEndDateEdit;
    private EditText mHourlyRateEdit;
    private EditText mNotesEdit;
    private EditText mStatusEdit;
    private Button mSaveButton;
    private Button mCancelButton;
    private Button mAddWorklogButton;
    private Button mChooseStartDateButton;
    private Button mChooseEndDateButton;
//    private RelativeLayout mEmployerPrompt;
    private ListView mEmployersListView;

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
        setContentView(R.layout.activity_job);
        mJob = (RDJob)getIntent().getParcelableExtra(RDConstants.EXTRAKEY_JOB);
        mDBHelper = MyDB.getInstance(this, RDConstants.DBNAME);
        getEmployersList();
        mAdapter = new AdapterEmployer(this, mEmployers);
        setupScreenControls();
    }

    private void setupScreenControls() {
        setupVersionLabel();
        setupJobName();
        setupEmployer();
        setupStartDate();
        setupEndDate();
        setupHourlyRate();
        setupNotes();
        setupStatus();
        setupButtons();
        setupEmployerPrompt();
        RDFunctions.sendViewToBack(mChooseStartDateButton);
        RDFunctions.sendViewToBack(mChooseEndDateButton);
        if (mJob.getId() >= 0) {
            mEditing = false;
        } else {
            mEditing = true;
        }
        loadData();
        setButtons();
    }

    private void setupVersionLabel() {
        TextView tv = (TextView)findViewById(R.id.txvJobVersion);
        RDVersion versionInfo = new RDVersion(this);
        tv.setText(versionInfo.getVersionNum(true));
    }

    private void setupJobName() {
        mJobNameEdit = (EditText)findViewById(R.id.txtJobJobNameVal);
        mJobNameEdit.addTextChangedListener(this);
    }

    private String lookupEmployerName(String pString) {
        String name = "";
        Iterator<RDEmployer> iterEmployers = mEmployers.iterator();
        while ( iterEmployers.hasNext() && name.isEmpty() ) {
            RDEmployer employer = iterEmployers.next();
            if (employer.getEmployerName().toLowerCase().startsWith(pString.toLowerCase())) {
                name = employer.getEmployerName();
            }
        }
        return name;
    }

    private void setupEmployer() {
        mEmployerEdit = (EditText)findViewById(R.id.txtJobEmployerVal);
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
        mStartDateEdit = (EditText)findViewById(R.id.txtJobStartDateVal);
        mStartDateEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupEndDate() {
        mEndDateEdit = (EditText)findViewById(R.id.txtJobEndDateVal);
        mEndDateEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupHourlyRate() {
        mHourlyRateEdit = (EditText)findViewById(R.id.txtJobHourlyRateVal);
        mHourlyRateEdit.addTextChangedListener(this);
    }

    private void setupNotes() {
        mNotesEdit = (EditText)findViewById(R.id.txtJobNotesVal);
        mNotesEdit.addTextChangedListener(this);
    }

    private void setupStatus() {
//        mStatusEdit = (EditText)findViewById(R.id.txtJobStatusVal);
//        mStatusEdit.setText(mJob.getStatus().toString());
    }

    private void loadData() {
        mLoading = true;
        mJobNameEdit.setText(mJob.getJobName());
//        mEmployerEdit.setText(mJob.getJobName());
        mStartDateEdit.setText(RDFunctions.convertDateFormat(mJob.getStartDate(),
                RDConstants.DATE_FORMAT_SHORT_YEARFIRST,
                RDConstants.DATE_FORMAT_SHORT));
        mEndDateEdit.setText(RDFunctions.convertDateFormat(mJob.getEndDate(),
                RDConstants.DATE_FORMAT_SHORT_YEARFIRST,
                RDConstants.DATE_FORMAT_SHORT));
        mHourlyRateEdit.setText(RDFunctions.numberToStr(mJob.getHourlyRate(), "%.2f"));
        mNotesEdit.setText(mJob.getNotes());
        mLoading = false;
    }

    private void setupButtons() {
        mSaveButton = (Button)findViewById(R.id.btnJobSave);
        mCancelButton = (Button)findViewById(R.id.btnJobCancel);
        mAddWorklogButton = (Button)findViewById(R.id.btnJobAddWorkLog);
        mChooseStartDateButton = (Button)findViewById(R.id.btnJobChooseStartDate);
        mChooseEndDateButton = (Button)findViewById(R.id.btnJobChooseEndDate);
    }

    private void setupEmployerPrompt() {
//        mEmployerPrompt = (RelativeLayout)findViewById(R.id.rloEmployerPrompt);
//        mEmployerPrompt.bringToFront();
//        mEmployerPrompt.setVisibility(View.INVISIBLE);
    }

    private void getEmployersList() {
        mEmployers = RDEmployer.employersList(mDBHelper, true);
    }

    public void chooseStartDateClick(View pButton) {
        mDateDialogType = "START";
        Date myDate = RDFunctions.dateFromString(mStartDateEdit.getText().toString(), RDConstants.DATE_FORMAT_SHORT);
        final Calendar cal = Calendar.getInstance();
        mYear = myDate.getYear();
        mMonth = myDate.getMonth();
        mDay = myDate.getDay();
        showDialog(mDialogId);
    }

    public void chooseEndDateClick(View pButton) {
        mDateDialogType = "END";
        Date myDate = RDFunctions.dateFromString(mEndDateEdit.getText().toString(), RDConstants.DATE_FORMAT_SHORT);
        final Calendar cal = Calendar.getInstance();
        mYear = myDate.getYear();
        mMonth = myDate.getMonth();
        mDay = myDate.getDay();
        showDialog(mDialogId);
    }

    @Override
    protected Dialog onCreateDialog(int pId) {
        if ( pId == mDialogId )
            return new DatePickerDialog(this,
                                        datePickerListener,
                                        mYear,
                                        mMonth,
                                        mDay);
        else
            return null;
    }

    private DatePickerDialog.OnDateSetListener datePickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    mYear = year;
                    mMonth = monthOfYear;
                    mDay = dayOfMonth;
                    Date d = new Date(mYear, mMonth, mDay);
                    SimpleDateFormat dateFormatter = new SimpleDateFormat("MM-dd-yyyy");
                    if (mDateDialogType == "START")
                        mStartDateEdit.setText(dateFormatter.format(d));
                    else
                        mEndDateEdit.setText(dateFormatter.format(d));
                }
    };

    private void setButtons() {
        if (mEditing || mJob.getId() < 0) {
            mSaveButton.setVisibility(View.VISIBLE);
            mCancelButton.setVisibility(View.VISIBLE);
            mAddWorklogButton.setVisibility(View.INVISIBLE);
        } else {
            mSaveButton.setVisibility(View.INVISIBLE);
            mCancelButton.setVisibility(View.INVISIBLE);
            mAddWorklogButton.setVisibility(View.VISIBLE);
        }
    }

    private boolean dataOk() {
        boolean retVal = true;
        if (mJobNameEdit.getText().toString().isEmpty()) {
            retVal = false;
            Toast.makeText(getApplicationContext(), "Enter a Job Name before saving", Toast.LENGTH_LONG).show();
        } else {
            if (mStartDateEdit.getText().toString().isEmpty()) {
                retVal = false;
                Toast.makeText(getApplicationContext(), "Enter a Start Date before saving", Toast.LENGTH_LONG).show();
            } else {
                if (mStartDateEdit.getText().toString().isEmpty()) {
                    retVal = false;
                    Toast.makeText(getApplicationContext(), "Enter an End Date before saving", Toast.LENGTH_LONG).show();
                }
            }
        }
        return retVal;
    }

    private void promptForEmployer() {
//        mEmployerPrompt.setVisibility(View.VISIBLE);
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Unknown Employer");
        LayoutInflater inflater = this.getLayoutInflater();
        RelativeLayout rlo = (RelativeLayout)inflater.inflate(R.layout.rlo_employer_promp, null);
        ListView employersLV = (ListView)rlo.findViewById(R.id.lsvJobEmployers);
        employersLV.setAdapter(mAdapter);
        final EditText empName = (EditText)rlo.findViewById(R.id.txtEPNewEmployerName);
        empName.setText(mEmployerEdit.getText().toString());
        employersLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                    mVibe.vibrate(RDConstants.BUTTON_VIBRATE_DURATION);
                    mSelectedRowSave = position;
//                    selectItem(position);
            }
        });
        alertDialog.setView(rlo)
                // Add action buttons
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        final AlertDialog dialog = alertDialog.create();
        Button btnEPAdd = (Button)rlo.findViewById(R.id.btnEPAdd);
        btnEPAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RDEmployer newEmployer = new RDEmployer();
                newEmployer.setEmployerName(empName.getText().toString());
                newEmployer.save(mDBHelper);
                getEmployersList();
                mAdapter.refreshList(mEmployers);
                mEmployerEdit.setText(empName.getText().toString());
                dialog.dismiss();
            }
        });
        dialog.show();
    }

/*    private void selectItem(int pItemNum) {
        mAdapter.setSelectedRow(pItemNum);
        if ( pItemNum != RDConstants.NOSELECTION ) {
            if ( mEmployers.size() > 0 ) {
                mSelectedOil = mDataList.get(pItemNum);
            } else {
                mSelectedOil = new RDOil();
            }
        } else {
            mSelectedOil = new RDOil();
        }
        mLoading = true;
        mEditTextBrand.setText(mSelectedOil.getBrand());
        mEditTextViscosity.setText(mSelectedOil.getViscosity());
        mEditTextNotes.setText(mSelectedOil.getNotes());
        mSelectedRow = pItemNum;
        mLoading = false;
        mDataChanged = false;
    }
*/
    private void validateData() {
        if (dataOk()) {
            mJob.setJobName(mJobNameEdit.getText().toString());
            mJob.setStartDate(mStartDateEdit.getText().toString());
            mJob.setEndDate(mEndDateEdit.getText().toString());
            mJob.setHourlyRate(Double.parseDouble(mHourlyRateEdit.getText().toString()));
            mJob.setNotes(mNotesEdit.getText().toString());
            mJob.save(mDBHelper);
            mEditing = false;
            setButtons();
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

    public void onSaveClick(View pSaveButton) {
        checkEmployer();
    }

    public void onCancelClick(View pCancelButton) {
        loadData();
        mEditing = false;
        setButtons();
    }

    public void onAddWorklogClick(View pAddWorklogButton) {

    }

    public void onChooseEmployerClick(View pButton) {
        promptForEmployer();
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

}
