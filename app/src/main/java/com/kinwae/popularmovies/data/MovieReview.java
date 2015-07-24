package com.kinwae.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Kembene on 7/21/2015.
 */
public class MovieReview implements Parcelable {
    private String reviewId;
    private String author;
    private String content;

    public MovieReview() {
    }

    public MovieReview(String author, String content) {
        this.author = author;
        this.content = content;
    }

    private MovieReview(Parcel in) {
        reviewId = in.readString();
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<MovieReview> CREATOR = new Creator<MovieReview>() {
        @Override
        public MovieReview createFromParcel(Parcel in) {
            return new MovieReview(in);
        }

        @Override
        public MovieReview[] newArray(int size) {
            return new MovieReview[size];
        }
    };

    public String getReviewId() {
        return reviewId;
    }

    public MovieReview setReviewId(String reviewId) {
        this.reviewId = reviewId;
        return this;
    }

    public String getAuthor() {
        return author;
    }

    public MovieReview setAuthor(String author) {
        this.author = author;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MovieReview setContent(String content) {
        this.content = content;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(reviewId);
        dest.writeString(author);
        dest.writeString(content);
    }
}
