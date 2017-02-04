package com.rdfmobileapps.jobtracker;

/**
 * Created by rich on 1/26/17.
 */

public class RDSpeechCommandResult {

    private RDSpeechCommand mSpeechCommand;
    private String[] mData;

//  Constructors

    public RDSpeechCommandResult() {
        super();
    }

    public RDSpeechCommandResult(RDSpeechCommand pCommand,
                                 String[] pData) {
        super();
        mSpeechCommand = pCommand;
        mData = pData;
    }

//  Public Methods

//  Private Methods

//  Getters

    public RDSpeechCommand getSpeechCommand() {
        return mSpeechCommand;
    }

    public String[] getData() {
        return mData;
    }

//  Setters

    public void setData(String[] pData) {
        mData = pData;
    }

    public void setSpeechCommand(RDSpeechCommand pSpeechCommand) {
        mSpeechCommand = pSpeechCommand;
    }

//  Static Methods

//  Parcelable Implementation
/*
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(this.mId);
    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<<CLASSNAME>> CREATOR = new Parcelable.Creator<<CLASSNAME>>() {
        public <CLASSNAME> createFromParcel(Parcel in) {
            return new <CLASSNAME>(in);
        }

        public <CLASSNAME>[] newArray(int size) {
            return new <CLASSNAME>[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    private <CLASSNAME>(Parcel in) {
        this.mId = in.readInt();
    }
*/
}
