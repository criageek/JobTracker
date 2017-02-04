package com.rdfmobileapps.jobtracker;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rich on 1/26/17.
 */

public enum RDSpeechCommand {
    None("None", 0),
    StartDate("start date", 1),
    EndDate("end date", 2),
    StartTime("start time", 3),
    EndTime("end time", 4),
    TotalTime("total time", 5),
    StartMiles("start miles", 6),
    EndMiles("end miles", 7),
    TotalMiles("total miles", 8);

    private String stringValue;
    private int intValue;
    private static Map<Integer, RDSpeechCommand> map = new HashMap<Integer, RDSpeechCommand>();

    private RDSpeechCommand(String toString, int value) {
        stringValue = toString;
        intValue = value;
    }

    static {
        for (RDSpeechCommand statusEnum : RDSpeechCommand.values()) {
            map.put(statusEnum.intValue, statusEnum);
        }
    }

    private RDSpeechCommand(final int status) {
        intValue = status;
    }

    public static RDSpeechCommand valueOf(int status) {
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
