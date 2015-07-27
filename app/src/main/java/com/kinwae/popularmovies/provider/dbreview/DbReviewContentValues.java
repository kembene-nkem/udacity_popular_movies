package com.kinwae.popularmovies.provider.dbreview;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.data.MovieReview;
import com.kinwae.popularmovies.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code db_review} table.
 */
public class DbReviewContentValues extends AbstractContentValues {

    public int bulkInsertReviewss(Movie movie, ContentResolver contentResolver) {
        List<MovieReview> reviewList = movie.getReviews();
        ContentValues[] values = null;
        if(reviewList != null){
            values = new ContentValues[reviewList.size()];
            for(int i=0; i<reviewList.size(); i++){
                MovieReview review = reviewList.get(i);
                DbReviewContentValues contentValues = new DbReviewContentValues();
                contentValues.putMovieId(Long.toString(movie.getId()));
                contentValues.putReview(review.getContent());
                contentValues.putReviewer(review.getAuthor());
                contentValues.putReviewId(review.getReviewId());
                values[i] = contentValues.values();
            }
        }
        if(values != null){
            return bulkInsert(contentResolver, values);
        }
        return 0;
    }

    @Override
    public Uri uri() {
        return DbReviewColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable DbReviewSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param context The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable DbReviewSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * themoviedb movie id
     */
    public DbReviewContentValues putMovieId(@Nullable String value) {
        mContentValues.put(DbReviewColumns.MOVIE_ID, value);
        return this;
    }

    public DbReviewContentValues putMovieIdNull() {
        mContentValues.putNull(DbReviewColumns.MOVIE_ID);
        return this;
    }

    /**
     * Reviewer name
     */
    public DbReviewContentValues putReviewer(@Nullable String value) {
        mContentValues.put(DbReviewColumns.REVIEWER, value);
        return this;
    }

    public DbReviewContentValues putReviewerNull() {
        mContentValues.putNull(DbReviewColumns.REVIEWER);
        return this;
    }

    /**
     * Review
     */
    public DbReviewContentValues putReview(@Nullable String value) {
        mContentValues.put(DbReviewColumns.REVIEW, value);
        return this;
    }

    public DbReviewContentValues putReviewNull() {
        mContentValues.putNull(DbReviewColumns.REVIEW);
        return this;
    }

    /**
     * Review Id
     */
    public DbReviewContentValues putReviewId(@Nullable String value) {
        mContentValues.put(DbReviewColumns.REVIEW_ID, value);
        return this;
    }

    public DbReviewContentValues putReviewIdNull() {
        mContentValues.putNull(DbReviewColumns.REVIEW_ID);
        return this;
    }
}
