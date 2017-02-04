package com.rdfmobileapps.jobtracker;

import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rich on 1/24/17.
 */

public class AdapterWorklogs extends BaseAdapter {
    private Vibrator mVibe;
    private Context mContext;
    private MyDB mDBHelper;
    private ArrayList<RDWorklog> mDataList;
    private int mSelectedRow = RDConstants.NOSELECTION;

    /*private view holder class*/
    private class ViewHolder {
        TextView txvItemText;
    }

    public AdapterWorklogs(Context pContext,
                           ArrayList<RDWorklog> pDataList) {
        doSetup(pContext, pDataList);
    }

    private void doSetup(Context pContext,
                         ArrayList<RDWorklog> pDataList) {
        mContext = pContext;
        mDataList = pDataList;
        mVibe = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public View getView(int pPosition, View pConvertView, ViewGroup pParent) {
        AdapterWorklogs.ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final RDWorklog dataRec = (RDWorklog) getItem(pPosition);
        if ( pConvertView == null ) {
            pConvertView = mInflater.inflate(R.layout.single_item_tall, pParent, false);
            if ( dataRec != null ) {
                holder = new AdapterWorklogs.ViewHolder();
                holder.txvItemText = (TextView)pConvertView.findViewById(R.id.txvSingleItem);
                pConvertView.setTag(holder);
            }
        }
        else {
            holder = (AdapterWorklogs.ViewHolder)pConvertView.getTag();
        }
        if ( holder != null ) {
            if ( mSelectedRow == pPosition ) {
                pConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.lightRed));
                pConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.lightRed));
            } else {
                if ( (pPosition % 2) == 1 ) {
                    pConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.lighterSkyBlue));
                    pConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.lighterSkyBlue));
                } else {
                    pConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                    pConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                }
            }
            holder.txvItemText.setText(dataRec.getStartTime());
        }
        return pConvertView;
    }

    public void refreshList(ArrayList<RDWorklog> pDataList) {
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
