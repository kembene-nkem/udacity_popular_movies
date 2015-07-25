package com.kinwae.popularmovies.provider.dbreview;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kinwae.popularmovies.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code db_review} table.
 */
public class DbReviewCursor extends AbstractCursor implements DbReviewModel {
    public DbReviewCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(DbReviewColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * themoviedb movie id
     * Can be {@code null}.
     */
    @Nullable
    public String getMovieId() {
        String res = getStringOrNull(DbReviewColumns.MOVIE_ID);
        return res;
    }

    /**
     * Reviewer name
     * Can be {@code null}.
     */
    @Nullable
    public String getReviewer() {
        String res = getStringOrNull(DbReviewColumns.REVIEWER);
        return res;
    }

    /**
     * Review
     * Can be {@code null}.
     */
    @Nullable
    public String getReview() {
        String res = getStringOrNull(DbReviewColumns.REVIEW);
        return res;
    }

    /**
     * Review Id
     * Can be {@code null}.
     */
    @Nullable
    public String getReviewId() {
        String res = getStringOrNull(DbReviewColumns.REVIEW_ID);
        return res;
    }
}
