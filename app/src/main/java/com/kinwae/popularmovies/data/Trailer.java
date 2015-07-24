package com.kinwae.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kembene on 7/21/2015.
 */
public class Trailer implements Parcelable {
    private String name;
    private String source;

    public Trailer() {

    }

    public Trailer(String name, String source) {
        this.name = name;
        this.source = source;
    }

    private Trailer(Parcel in) {
        name = in.readString();
        source = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public String getName() {
        return name;
    }

    public Trailer setName(String name) {
        this.name = name;
        return this;
    }

    public String getSource() {
        return source;
    }

    public Trailer setSource(String source) {
        this.source = source;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(source);
    }
}
