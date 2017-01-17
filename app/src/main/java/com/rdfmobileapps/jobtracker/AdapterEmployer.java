package com.rdfmobileapps.jobtracker;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AdapterEmployer extends BaseAdapter {
	private Vibrator mVibe;
	private Context mContext;
	private MyDB mDBHelper;
	private ArrayList<RDEmployer> mDataList;
	private int mSelectedRow = RDConstants.NOSELECTION;
	
	/*private view holder class*/
    private class ViewHolder {
    	TextView txvItemText;
    }
 
	public AdapterEmployer(Context pContext,
			ArrayList<RDEmployer> pDataList) {
		doSetup(pContext, pDataList);
	}
	
	private void doSetup(Context pContext,
			ArrayList<RDEmployer> pDataList) {
		mContext = pContext;
		mDataList = pDataList;
		mVibe = (Vibrator)mContext.getSystemService(Context.VIBRATOR_SERVICE);
	}
	
	@Override
	public View getView(int pPosition, View pConvertView, ViewGroup pParent) {
        ViewHolder holder = null;
        LayoutInflater mInflater = (LayoutInflater)mContext.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final RDEmployer dataRec = (RDEmployer)getItem(pPosition);
        if ( pConvertView == null ) {
            pConvertView = mInflater.inflate(R.layout.elv_list_single_item, pParent, false);
            if ( dataRec != null ) {
            	holder = new ViewHolder();
            	holder.txvItemText = (TextView)pConvertView.findViewById(R.id.lblListItem);
            	pConvertView.setTag(holder);
            }
        }
        else {
           holder = (ViewHolder)pConvertView.getTag();
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
        	holder.txvItemText.setText(dataRec.getEmployerName());
        }
        return pConvertView;
    }

	public void refreshList(ArrayList<RDEmployer> pDataList) {
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
