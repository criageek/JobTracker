package com.rdfmobileapps.jobtracker;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rich on 1/21/17.
 */

public class AdapterJobs extends BaseAdapter {
    private Vibrator mVibe;
    private Context mContext;
    private MyDB mDBHelper;
    private ArrayList<RDJob> mDataList;
    private int mSelectedRow = RDConstants.NOSELECTION;

    /*private view holder class*/
    private class ViewHolder {
        TextView txvItemText;
    }

    public AdapterJobs(Context pContext,
                       ArrayList<RDJob> pDataList) {
        doSetup(pContext, pDataList);
    }

    private void doSetup(Context pContext,
                         ArrayList<RDJob> pDataList) {
        mContext = pContext;
        mDataList = pDataList;
        mVibe = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public View getView(int pPosition, View pConvertView, ViewGroup pParent) {
        AdapterJobs.ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final RDJob dataRec = (RDJob)getItem(pPosition);
        if ( pConvertView == null ) {
            pConvertView = mInflater.inflate(R.layout.single_item_tall, pParent, false);
            if ( dataRec != null ) {
                holder = new AdapterJobs.ViewHolder();
                holder.txvItemText = (TextView)pConvertView.findViewById(R.id.txvSingleItem);
                pConvertView.setTag(holder);
            }
        }
        else {
            holder = (AdapterJobs.ViewHolder)pConvertView.getTag();
        }
        if ( holder != null ) {
            if ( mSelectedRow == pPosition ) {
                pConvertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lightRed));
            } else {
                if ( (pPosition % 2) == 1 ) {
                    pConvertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.lighterSkyBlue));
                } else {
                    pConvertView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.moneyGreen));
                }
            }
            holder.txvItemText.setText(dataRec.getJobName());
        }
        return pConvertView;
    }

    public void refreshList(ArrayList<RDJob> pDataList) {
        mDataList = pDataList;
        notifyDataSetChanged();
    }

    public void setSelectedRow(int pRow) {
        mSelectedRow = pRow;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if ( mDataList != null ) {
            return mDataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return mDataList.indexOf(getItem(position));
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int arg0) {
        return true;
    }

}
