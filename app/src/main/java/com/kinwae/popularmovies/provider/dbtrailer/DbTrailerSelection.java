package com.kinwae.popularmovies.provider.dbtrailer;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.kinwae.popularmovies.provider.base.AbstractSelection;

/**
 * Selection for the {@code db_trailer} table.
 */
public class DbTrailerSelection extends AbstractSelection<DbTrailerSelection> {
    @Override
    protected Uri baseUri() {
        return DbTrailerColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code DbTrailerCursor} object, which is positioned before the first entry, or null.
     */
    public DbTrailerCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new DbTrailerCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public DbTrailerCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code DbTrailerCursor} object, which is positioned before the first entry, or null.
     */
    public DbTrailerCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new DbTrailerCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public DbTrailerCursor query(Context context) {
        return query(context, null);
    }


    public DbTrailerSelection id(long... value) {
        addEquals("db_trailer." + DbTrailerColumns._ID, toObjectArray(value));
        return this;
    }

    public DbTrailerSelection idNot(long... value) {
        addNotEquals("db_trailer." + DbTrailerColumns._ID, toObjectArray(value));
        return this;
    }

    public DbTrailerSelection orderById(boolean desc) {
        orderBy("db_trailer." + DbTrailerColumns._ID, desc);
        return this;
    }

    public DbTrailerSelection orderById() {
        return orderById(false);
    }

    public DbTrailerSelection movieId(String... value) {
        addEquals(DbTrailerColumns.MOVIE_ID, value);
        return this;
    }

    public DbTrailerSelection movieIdNot(String... value) {
        addNotEquals(DbTrailerColumns.MOVIE_ID, value);
        return this;
    }

    public DbTrailerSelection movieIdLike(String... value) {
        addLike(DbTrailerColumns.MOVIE_ID, value);
        return this;
    }

    public DbTrailerSelection movieIdContains(String... value) {
        addContains(DbTrailerColumns.MOVIE_ID, value);
        return this;
    }

    public DbTrailerSelection movieIdStartsWith(String... value) {
        addStartsWith(DbTrailerColumns.MOVIE_ID, value);
        return this;
    }

    public DbTrailerSelection movieIdEndsWith(String... value) {
        addEndsWith(DbTrailerColumns.MOVIE_ID, value);
        return this;
    }

    public DbTrailerSelection orderByMovieId(boolean desc) {
        orderBy(DbTrailerColumns.MOVIE_ID, desc);
        return this;
    }

    public DbTrailerSelection orderByMovieId() {
        orderBy(DbTrailerColumns.MOVIE_ID, false);
        return this;
    }

    public DbTrailerSelection name(String... value) {
        addEquals(DbTrailerColumns.NAME, value);
        return this;
    }

    public DbTrailerSelection nameNot(String... value) {
        addNotEquals(DbTrailerColumns.NAME, value);
        return this;
    }

    public DbTrailerSelection nameLike(String... value) {
        addLike(DbTrailerColumns.NAME, value);
        return this;
    }

    public DbTrailerSelection nameContains(String... value) {
        addContains(DbTrailerColumns.NAME, value);
        return this;
    }

    public DbTrailerSelection nameStartsWith(String... value) {
        addStartsWith(DbTrailerColumns.NAME, value);
        return this;
    }

    public DbTrailerSelection nameEndsWith(String... value) {
        addEndsWith(DbTrailerColumns.NAME, value);
        return this;
    }

    public DbTrailerSelection orderByName(boolean desc) {
        orderBy(DbTrailerColumns.NAME, desc);
        return this;
    }

    public DbTrailerSelection orderByName() {
        orderBy(DbTrailerColumns.NAME, false);
        return this;
    }

    public DbTrailerSelection youtubeId(String... value) {
        addEquals(DbTrailerColumns.YOUTUBE_ID, value);
        return this;
    }

    public DbTrailerSelection youtubeIdNot(String... value) {
        addNotEquals(DbTrailerColumns.YOUTUBE_ID, value);
        return this;
    }

    public DbTrailerSelection youtubeIdLike(String... value) {
        addLike(DbTrailerColumns.YOUTUBE_ID, value);
        return this;
    }

    public DbTrailerSelection youtubeIdContains(String... value) {
        addContains(DbTrailerColumns.YOUTUBE_ID, value);
        return this;
    }

    public DbTrailerSelection youtubeIdStartsWith(String... value) {
        addStartsWith(DbTrailerColumns.YOUTUBE_ID, value);
        return this;
    }

    public DbTrailerSelection youtubeIdEndsWith(String... value) {
        addEndsWith(DbTrailerColumns.YOUTUBE_ID, value);
        return this;
    }

    public DbTrailerSelection orderByYoutubeId(boolean desc) {
        orderBy(DbTrailerColumns.YOUTUBE_ID, desc);
        return this;
    }

    public DbTrailerSelection orderByYoutubeId() {
        orderBy(DbTrailerColumns.YOUTUBE_ID, false);
        return this;
    }
}
