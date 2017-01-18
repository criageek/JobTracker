package com.rdfmobileapps.jobtracker;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityEmployer extends Activity implements TextWatcher {

    private MyDB mDBHelper;
    private RDEmployer mEmployer;
    private boolean mLoading = true;
    private boolean mEditing = false;

    private TextView mId;
    private EditText mEmployerNameEdit;
    private EditText mContactEdit;
    private EditText mStreetEdit;
    private EditText mCityEdit;
    private EditText mStateEdit;
    private EditText mZipcodeEdit;
    private EditText mPhoneCellEdit;
    private EditText mPhoneAltEdit;
    private EditText mEmail1Edit;
    private EditText mEmail2Edit;
    private EditText mWebsiteEdit;
    private EditText mNotesEdit;
    private Button mSaveButton;
    private Button mCancelButton;

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
        setContentView(R.layout.activity_employer);
        mDBHelper = MyDB.getInstance(this, RDConstants.DBNAME);
        mEmployer = (RDEmployer)getIntent().getParcelableExtra(RDConstants.EXTRAKEY_EMPLOYER);
        setupScreenControls();
    }

    private void setupScreenControls() {
        mId = (TextView)findViewById(R.id.txvEmpId);
        mEmployerNameEdit = (EditText)findViewById(R.id.txtEmpEmployerNameVal);
        mContactEdit = (EditText)findViewById(R.id.txtEmpContactVal);
        mStreetEdit = (EditText)findViewById(R.id.txtEmpStreetVal);
        mCityEdit = (EditText)findViewById(R.id.txtEmpCityVal);
        mStateEdit = (EditText)findViewById(R.id.txtEmpStateVal);
        mZipcodeEdit = (EditText)findViewById(R.id.txtEmpZipcodeVal);
        mPhoneCellEdit = (EditText)findViewById(R.id.txtEmpPhoneCellVal);
        mPhoneAltEdit = (EditText)findViewById(R.id.txtEmpPhoneCellVal);
        mEmail1Edit = (EditText)findViewById(R.id.txtEmpEmail1Val);
        mEmail2Edit = (EditText)findViewById(R.id.txtEmpEmail2Val);
        mWebsiteEdit = (EditText)findViewById(R.id.txtEmpWebsiteVal);
        mNotesEdit = (EditText)findViewById(R.id.txtEmpNotesVal);
        mEmployerNameEdit.addTextChangedListener(this);
        mContactEdit.addTextChangedListener(this);
        mStreetEdit.addTextChangedListener(this);
        mCityEdit.addTextChangedListener(this);
        mStateEdit.addTextChangedListener(this);
        mZipcodeEdit.addTextChangedListener(this);
        mPhoneCellEdit.addTextChangedListener(this);
        mPhoneAltEdit.addTextChangedListener(this);
        mEmail1Edit.addTextChangedListener(this);
        mEmail2Edit.addTextChangedListener(this);
        mWebsiteEdit.addTextChangedListener(this);
        mNotesEdit.addTextChangedListener(this);
        setupButtons();
        loadData();
        setButtons();
    }

    private void setupButtons() {
        mSaveButton = (Button)findViewById(R.id.btnEmployerSave);
        mCancelButton = (Button)findViewById(R.id.btnEmployerCancel);
    }

    private void loadData() {
        mLoading = true;
        if (mEmployer.getId() >= 0)
            mId.setText(RDFunctions.numberToStr(mEmployer.getId(), "%d"));
        else
            mId.setText("");
        mEmployerNameEdit.setText(mEmployer.getEmployerName());
        mContactEdit.setText(mEmployer.getContactName());
        mStreetEdit.setText(mEmployer.getStreetAddress());
        mCityEdit.setText(mEmployer.getCity());
        mStateEdit.setText(mEmployer.getState());
        mZipcodeEdit.setText(mEmployer.getZipCode());
        mPhoneCellEdit.setText(mEmployer.getPhoneCell());
        mPhoneAltEdit.setText(mEmployer.getPhoneAlt());
        mEmail1Edit.setText(mEmployer.getEmail1());
        mEmail2Edit.setText(mEmployer.getEmail2());
        mWebsiteEdit.setText(mEmployer.getWebsite());
        mNotesEdit.setText(mEmployer.getNotes());
        mLoading = false;
    }

    private void setButtons() {
/*        if (mEditing || mEmployer.getId() < 0) {
            mSaveButton.setVisibility(View.VISIBLE);
            mCancelButton.setVisibility(View.VISIBLE);
        } else {
            mSaveButton.setVisibility(View.INVISIBLE);
            mCancelButton.setVisibility(View.INVISIBLE);
        }
*/
        if (mEditing) {
            mSaveButton.setVisibility(View.VISIBLE);
            mCancelButton.setVisibility(View.VISIBLE);
        } else {
            mSaveButton.setVisibility(View.INVISIBLE);
            mCancelButton.setVisibility(View.INVISIBLE);
        }
    }

    private boolean dataOk() {
        return true;
    }

    public void onSaveClick(View pButton) {
        if (dataOk()) {
            mEmployer.setEmployerName(mEmployerNameEdit.getText().toString());
            mEmployer.setContactName(mContactEdit.getText().toString());
            mEmployer.setStreetAddress(mStreetEdit.getText().toString());
            mEmployer.setCity(mCityEdit.getText().toString());
            mEmployer.setState(mStateEdit.getText().toString());
            mEmployer.setZipCode(mZipcodeEdit.getText().toString());
            mEmployer.setPhoneCell(mPhoneCellEdit.getText().toString());
            mEmployer.setPhoneAlt(mPhoneAltEdit.getText().toString());
            mEmployer.setEmail1(mEmail1Edit.getText().toString());
            mEmployer.setEmail2(mEmail2Edit.getText().toString());
            mEmployer.setWebsite(mWebsiteEdit.getText().toString());
            mEmployer.setNotes(mNotesEdit.getText().toString());
            mEmployer.save(mDBHelper);
            finish();
        }
    }

    public void onCancelClick(View pButton) {
        loadData();
        mEditing = false;
        setButtons();
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
