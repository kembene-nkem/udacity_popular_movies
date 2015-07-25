package com.kinwae.popularmovies.provider.dbreview;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.kinwae.popularmovies.provider.base.AbstractSelection;

/**
 * Selection for the {@code db_review} table.
 */
public class DbReviewSelection extends AbstractSelection<DbReviewSelection> {
    @Override
    protected Uri baseUri() {
        return DbReviewColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code DbReviewCursor} object, which is positioned before the first entry, or null.
     */
    public DbReviewCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new DbReviewCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public DbReviewCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code DbReviewCursor} object, which is positioned before the first entry, or null.
     */
    public DbReviewCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new DbReviewCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public DbReviewCursor query(Context context) {
        return query(context, null);
    }


    public DbReviewSelection id(long... value) {
        addEquals("db_review." + DbReviewColumns._ID, toObjectArray(value));
        return this;
    }

    public DbReviewSelection idNot(long... value) {
        addNotEquals("db_review." + DbReviewColumns._ID, toObjectArray(value));
        return this;
    }

    public DbReviewSelection orderById(boolean desc) {
        orderBy("db_review." + DbReviewColumns._ID, desc);
        return this;
    }

    public DbReviewSelection orderById() {
        return orderById(false);
    }

    public DbReviewSelection movieId(String... value) {
        addEquals(DbReviewColumns.MOVIE_ID, value);
        return this;
    }

    public DbReviewSelection movieIdNot(String... value) {
        addNotEquals(DbReviewColumns.MOVIE_ID, value);
        return this;
    }

    public DbReviewSelection movieIdLike(String... value) {
        addLike(DbReviewColumns.MOVIE_ID, value);
        return this;
    }

    public DbReviewSelection movieIdContains(String... value) {
        addContains(DbReviewColumns.MOVIE_ID, value);
        return this;
    }

    public DbReviewSelection movieIdStartsWith(String... value) {
        addStartsWith(DbReviewColumns.MOVIE_ID, value);
        return this;
    }

    public DbReviewSelection movieIdEndsWith(String... value) {
        addEndsWith(DbReviewColumns.MOVIE_ID, value);
        return this;
    }

    public DbReviewSelection orderByMovieId(boolean desc) {
        orderBy(DbReviewColumns.MOVIE_ID, desc);
        return this;
    }

    public DbReviewSelection orderByMovieId() {
        orderBy(DbReviewColumns.MOVIE_ID, false);
        return this;
    }

    public DbReviewSelection reviewer(String... value) {
        addEquals(DbReviewColumns.REVIEWER, value);
        return this;
    }

    public DbReviewSelection reviewerNot(String... value) {
        addNotEquals(DbReviewColumns.REVIEWER, value);
        return this;
    }

    public DbReviewSelection reviewerLike(String... value) {
        addLike(DbReviewColumns.REVIEWER, value);
        return this;
    }

    public DbReviewSelection reviewerContains(String... value) {
        addContains(DbReviewColumns.REVIEWER, value);
        return this;
    }

    public DbReviewSelection reviewerStartsWith(String... value) {
        addStartsWith(DbReviewColumns.REVIEWER, value);
        return this;
    }

    public DbReviewSelection reviewerEndsWith(String... value) {
        addEndsWith(DbReviewColumns.REVIEWER, value);
        return this;
    }

    public DbReviewSelection orderByReviewer(boolean desc) {
        orderBy(DbReviewColumns.REVIEWER, desc);
        return this;
    }

    public DbReviewSelection orderByReviewer() {
        orderBy(DbReviewColumns.REVIEWER, false);
        return this;
    }

    public DbReviewSelection review(String... value) {
        addEquals(DbReviewColumns.REVIEW, value);
        return this;
    }

    public DbReviewSelection reviewNot(String... value) {
        addNotEquals(DbReviewColumns.REVIEW, value);
        return this;
    }

    public DbReviewSelection reviewLike(String... value) {
        addLike(DbReviewColumns.REVIEW, value);
        return this;
    }

    public DbReviewSelection reviewContains(String... value) {
        addContains(DbReviewColumns.REVIEW, value);
        return this;
    }

    public DbReviewSelection reviewStartsWith(String... value) {
        addStartsWith(DbReviewColumns.REVIEW, value);
        return this;
    }

    public DbReviewSelection reviewEndsWith(String... value) {
        addEndsWith(DbReviewColumns.REVIEW, value);
        return this;
    }

    public DbReviewSelection orderByReview(boolean desc) {
        orderBy(DbReviewColumns.REVIEW, desc);
        return this;
    }

    public DbReviewSelection orderByReview() {
        orderBy(DbReviewColumns.REVIEW, false);
        return this;
    }

    public DbReviewSelection reviewId(String... value) {
        addEquals(DbReviewColumns.REVIEW_ID, value);
        return this;
    }

    public DbReviewSelection reviewIdNot(String... value) {
        addNotEquals(DbReviewColumns.REVIEW_ID, value);
        return this;
    }

    public DbReviewSelection reviewIdLike(String... value) {
        addLike(DbReviewColumns.REVIEW_ID, value);
        return this;
    }

    public DbReviewSelection reviewIdContains(String... value) {
        addContains(DbReviewColumns.REVIEW_ID, value);
        return this;
    }

    public DbReviewSelection reviewIdStartsWith(String... value) {
        addStartsWith(DbReviewColumns.REVIEW_ID, value);
        return this;
    }

    public DbReviewSelection reviewIdEndsWith(String... value) {
        addEndsWith(DbReviewColumns.REVIEW_ID, value);
        return this;
    }

    public DbReviewSelection orderByReviewId(boolean desc) {
        orderBy(DbReviewColumns.REVIEW_ID, desc);
        return this;
    }

    public DbReviewSelection orderByReviewId() {
        orderBy(DbReviewColumns.REVIEW_ID, false);
        return this;
    }
}
