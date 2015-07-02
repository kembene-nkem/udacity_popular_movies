package com.kinwae.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kembene on 6/24/2015.
 */
public class Movie implements Parcelable {
    private long mId;
    private String mTitle;
    private String mOriginalTitle;
    private String mPosterPath;
    private String mBackdropPath;
    private double mRating;
    private String mPlotSynopsis;
    private String mReleaseDate;

    public Movie() {
    }

    public Movie(String mTitle, String mPosterPath, long mId) {
        this.mTitle = mTitle;
        this.mPosterPath = mPosterPath;
        this.mId = mId;
    }


    public long getId() {
        return mId;
    }

    public void setId(long mId) {
        this.mId = mId;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public Movie setReleaseDate(String releaseDate) {
        this.mReleaseDate = releaseDate;
        return this;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getBackdropPath() {
        return mBackdropPath;
    }

    public void setBackdropPath(String mBackdropPath) {
        this.mBackdropPath = mBackdropPath;
    }

    public double getRating() {
        return mRating;
    }

    public void setRating(double mRating) {
        this.mRating = mRating;
    }

    public String getPlotSynopsis() {
        return mPlotSynopsis;
    }

    public void setPlotSynopsis(String mPlotSynopsis) {
        this.mPlotSynopsis = mPlotSynopsis;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "mTitle='" + mTitle + '\'' +
                ", mPosterPath='" + mPosterPath + '\'' +
                ", mId=" + mId +
                '}';
    }

    private Movie(Parcel in) {
        mId = in.readLong();
        mTitle = in.readString();
        mOriginalTitle = in.readString();
        mPosterPath = in.readString();
        mBackdropPath = in.readString();
        mRating = in.readDouble();
        mPlotSynopsis = in.readString();
        mReleaseDate = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(mId);
        dest.writeString(mTitle);
        dest.writeString(mOriginalTitle);
        dest.writeString(mPosterPath);
        dest.writeString(mBackdropPath);
        dest.writeDouble(mRating);
        dest.writeString(mPlotSynopsis);
        dest.writeString(mReleaseDate);
    }
}
