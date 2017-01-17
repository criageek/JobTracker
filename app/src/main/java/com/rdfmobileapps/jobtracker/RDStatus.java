package com.rdfmobileapps.jobtracker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rich on 1/12/17.
 */

public enum RDStatus {
    None("None", 0),
    Started("Started", 1),
    Complete("Complete", 2),
    Cancel("Cancel", 3),
    Active("Active", 4),
    InActive("Inactive", 5);

    private String stringValue;
    private int intValue;
    private static Map<Integer, RDStatus> map = new HashMap<Integer, RDStatus>();

    private RDStatus(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    static {
        for (RDStatus statusEnum : RDStatus.values()) {
            map.put(statusEnum.intValue, statusEnum);
        }
    }

    private RDStatus(final int status) {
        intValue = status;
    }

    public static RDStatus valueOf(int status) {
        return map.get(status);
    }

    public int getValue() {
        return intValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

}
