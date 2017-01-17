package com.rdfmobileapps.jobtracker;

import java.util.Comparator;

public class RDGenericComparator implements Comparator<Object> {
	protected boolean mAscending;
	protected int mSortColumn;
	
	public RDGenericComparator(boolean pAscending, int pSortColumn) {
		super();
		mAscending = pAscending;
		mSortColumn = pSortColumn;
	}
	
	@Override
	public int compare(Object lhs, Object rhs) {
		return 0;
	}

	public void setAscending(boolean pAscending) {
		mAscending = pAscending;
	}

	public boolean isAscending() {
		return mAscending;
	}
	
	public void setSortColumn(int pSortColumn) {
		mSortColumn = pSortColumn;
	}

	public int getSortColumn() {
		return mSortColumn;
	}
	
}
