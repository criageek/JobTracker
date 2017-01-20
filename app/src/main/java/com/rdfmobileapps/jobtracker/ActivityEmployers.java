package com.rdfmobileapps.jobtracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import java.util.ArrayList;

public class ActivityEmployers extends Activity {

    private MyDB mDBHelper;
    private AdapterEmployer mAdapter;
    private ArrayList<RDEmployer> mEmployers;
    private ListView mListView;
    private CheckBox mShowAllCheckBox;

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
        setContentView(R.layout.activity_employers);
        mDBHelper = MyDB.getInstance(this, RDConstants.DBNAME);
//        getEmployersList();
        mAdapter = new AdapterEmployer(this, mEmployers);
        setupScreenControls();
    }

    private void setupScreenControls() {
        setupCheckBox();
        setupListView();
    }

    private void getEmployersList() {
        mEmployers = RDEmployer.employersList(mDBHelper, !mShowAllCheckBox.isChecked());
    }

    private void setupCheckBox() {
        mShowAllCheckBox = (CheckBox)findViewById(R.id.chkEmpsShowAll);
        mShowAllCheckBox.setChecked(false);
    }

    private void setupListView() {
        mListView = (ListView)findViewById(R.id.lsvEmployers);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                showEmployer(position);
            }
        });
    }

    private void showEmployer(int pRowNum) {
        RDEmployer employer = mEmployers.get(pRowNum);
        showEmployerActivity(employer);
    }

    public void onNewEmployerClick(View pButton) {
        RDEmployer newEmployer = new RDEmployer();
        showEmployerActivity(newEmployer);
    }

    public void onShowAllClick(View pButton) {
        getEmployersList();
        mAdapter.refreshList(mEmployers);
    }

    private void showEmployerActivity(RDEmployer pEmployer) {
        Intent intent = new Intent(this, ActivityEmployer.class);
        intent.putExtra(RDConstants.EXTRAKEY_EMPLOYER, pEmployer);
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getEmployersList();
        mAdapter.refreshList(mEmployers);
    }
}
