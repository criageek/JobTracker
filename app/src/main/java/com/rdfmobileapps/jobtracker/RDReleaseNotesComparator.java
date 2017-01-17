package com.rdfmobileapps.jobtracker;

/**
 * Created by rich on 2/2/15.
 */
public class RDReleaseNotesComparator extends RDGenericComparator {

    public RDReleaseNotesComparator(boolean pAscending, int pSortColumn) {
        super(pAscending, pSortColumn);
    }

    @Override
    public int compare(Object lhs, Object rhs) {
        RDReleaseNote left = (RDReleaseNote)lhs;
        RDReleaseNote right = (RDReleaseNote)rhs;
        int retVal = 0;
        switch ( mSortColumn ) {
            case 0:
                if ( mAscending ) {
                    retVal = left.getVersion().compareTo(right.getVersion());
                } else {
                    retVal = right.getVersion().compareTo(left.getVersion());
                }
                break;
        }
        return retVal;
    }

}
