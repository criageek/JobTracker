package com.rdfmobileapps.jobtracker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rich on 1/28/17.
 */

public enum RDDataFormat {
    None("None", 0),
    Int("Int", 1),
    Double("Double", 2),
    Time("Time", 3);

    private String stringValue;
    private int intValue;
    private static Map<Integer, RDDataFormat> map = new HashMap<Integer, RDDataFormat>();

    private RDDataFormat(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    static {
        for (RDDataFormat formatEnum : RDDataFormat.values()) {
            map.put(formatEnum.intValue, formatEnum);
        }
    }

    private RDDataFormat(final int format) {
        intValue = format;
    }

    public static RDDataFormat valueOf(int format) {
        return map.get(format);
    }

    public int getValue() {
        return intValue;
    }

    @Override
    public String toString() {
        return stringValue;
    }

}
