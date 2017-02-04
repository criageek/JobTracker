package com.rdfmobileapps.jobtracker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rich on 1/29/17.
 */

public enum RDDialogType {
    None("None", 0),
    StartDate("Start Date", 1),
    EndDate("End Date", 2),
    StartTime("Start Time", 3),
    EndTime("End Time", 4);

    private String stringValue;
    private int intValue;
    private static Map<Integer, RDDialogType> map = new HashMap<Integer, RDDialogType>();

    private RDDialogType(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    static {
        for (RDDialogType dialogEnum : RDDialogType.values()) {
            map.put(dialogEnum.intValue, dialogEnum);
        }
    }

    private RDDialogType(final int dialog) {
        intValue = dialog;
    }

    public static RDDialogType valueOf(int dialog) {
        return map.get(dialog);
    }

    public int getValue() {
        return intValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

}
