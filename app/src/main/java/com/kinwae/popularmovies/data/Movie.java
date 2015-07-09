package com.kinwae.popularmovies.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Kembene on 6/24/2015.
 */
public class Movie implements Parcelable {
    private long id;
    private String title;
    private String originalTitle;
    private String posterPath;
    private String backdropPath;
    private double voteAverage;
    private String overview;
    private Date releaseDate;
    private String releaseDateFormatted;

    public static final String DATE_FORMAT = "yyyy-MM-dd";


    public Movie() {

    }

    public Movie(String mTitle, String mPosterPath, long mId) {
        this.title = mTitle;
        this.posterPath = mPosterPath;
        this.id = mId;
    }


    private Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        this.releaseDateFormatted = in.readString();
        if(this.releaseDateFormatted != null){
            try {
                this.releaseDate = SimpleDateFormat.getDateInstance().parse(this.releaseDateFormatted);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
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

    public long getId() {
        return id;
    }

    public Movie setId(long id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public Movie setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Movie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Movie setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public Movie setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public Movie setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getReleaseDateFormatted(Context context){
        if(this.releaseDateFormatted == null)
            if(this.releaseDate != null)
                this.releaseDateFormatted = DateFormat.getDateFormat(context).format(this.releaseDate);
        return this.releaseDateFormatted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeDouble(voteAverage);
        dest.writeString(overview);
        dest.writeString(this.releaseDateFormatted);
    }
}
