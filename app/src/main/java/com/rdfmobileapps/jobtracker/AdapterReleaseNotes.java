package com.rdfmobileapps.jobtracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rdfmobileapps.jobtracker.RDConstants;
import com.rdfmobileapps.jobtracker.RDReleaseNote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rich on 2/2/15.
 */
public class AdapterReleaseNotes extends BaseExpandableListAdapter {
    private Context mContext;
    private ArrayList<String> mHeadersList;
    private HashMap<String, ArrayList<RDReleaseNote>> mDataDict;
    private int mSelectedRow = RDConstants.NOSELECTION;

    /*private view holder class*/
    private class ViewHolder {
        TextView txvVersion;
        TextView txvDate;
        TextView txvTitle;
        TextView txvDescription;
        TextView txvAffectedFiles;
    }

    public AdapterReleaseNotes(Context pContext, ArrayList<String> pHeaderList,
                HashMap<String, ArrayList<RDReleaseNote>> pDataDict) {
        mContext = pContext;
        mHeadersList = pHeaderList;
        mDataDict = pDataDict;
    }

    @Override
    public Object getChild(int pGroupPosition, int pChildPosition) {
        return mDataDict.get(mHeadersList.get(pGroupPosition)).get(pChildPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }


    @Override
    public View getChildView(int pGroupPosition, final int pChildPosition,
                             boolean pIsLastChild, View pConvertView, ViewGroup pParent) {
        ViewHolder holder = null;
        final RDReleaseNote rn = (RDReleaseNote)getChild(pGroupPosition, pChildPosition);

        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if ( pConvertView == null ) {
            pConvertView = mInflater.inflate(R.layout.releasenotes_item, pParent, false);
            if ( rn != null ) {
                holder = new ViewHolder();
                holder.txvVersion = (TextView)pConvertView.findViewById(R.id.txvRNIVersion);
                holder.txvDate = (TextView)pConvertView.findViewById(R.id.txvRNIDate);
                holder.txvTitle = (TextView)pConvertView.findViewById(R.id.txvRNITitle);
                holder.txvDescription = (TextView)pConvertView.findViewById(R.id.txvRNIDescription);
                holder.txvAffectedFiles = (TextView)pConvertView.findViewById(R.id.txvRNIAffectedFiles);
                pConvertView.setTag(holder);
            }
        }
        else {
            holder = (ViewHolder)pConvertView.getTag();
        }
        if ( holder != null ) {
            holder.txvVersion.setText(rn.getVersion());
            holder.txvDate.setText(rn.getDate());
            holder.txvTitle.setText(rn.getTitle());
            holder.txvDescription.setText(rn.getDescription());
            holder.txvAffectedFiles.setText(rn.getAffectedFilesStr());
            if ( pChildPosition == mSelectedRow ) {
                pConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.lightRed));
            } else {
                if ( (pChildPosition % 2) == 1 ) {
                    pConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.lighterSkyBlue));
                } else {
                    pConvertView.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                }
            }
        }
        return pConvertView;
    }

    @Override
    public int getChildrenCount(int pGroupPosition) {
        String key = mHeadersList.get(pGroupPosition);
        ArrayList<RDReleaseNote> list = mDataDict.get(key);
        int count = list.size();
        return count;
    }

    @Override
    public Object getGroup(int pGroupPosition) {
        return mHeadersList.get(pGroupPosition);
    }

    @Override
    public int getGroupCount() {
        int count = mHeadersList.size();
        return count;
    }

    @Override
    public long getGroupId(int pGroupPosition) {
        return pGroupPosition;
    }

    @Override
    public View getGroupView(int pGroupPosition, boolean pIsExpanded,
                             View pConvertView, ViewGroup pParent) {
        String headerTitle = (String)getGroup(pGroupPosition);
        if ( pConvertView == null ) {
            LayoutInflater inflater = (LayoutInflater)mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            pConvertView = inflater.inflate(R.layout.elv_list_group_header, null);
        }
        TextView lblListHeader = (TextView)pConvertView.findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return pConvertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
