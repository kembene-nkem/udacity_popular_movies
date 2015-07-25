package com.kinwae.popularmovies.provider.dbreview;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kinwae.popularmovies.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code db_review} table.
 */
public class DbReviewContentValues extends AbstractContentValues {
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
     * @param contentResolver The content resolver to use.
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
