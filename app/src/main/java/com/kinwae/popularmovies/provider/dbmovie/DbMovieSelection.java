package com.kinwae.popularmovies.provider.dbmovie;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.kinwae.popularmovies.provider.base.AbstractSelection;

/**
 * Selection for the {@code db_movie} table.
 */
public class DbMovieSelection extends AbstractSelection<DbMovieSelection> {
    @Override
    protected Uri baseUri() {
        return DbMovieColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code DbMovieCursor} object, which is positioned before the first entry, or null.
     */
    public DbMovieCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new DbMovieCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public DbMovieCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code DbMovieCursor} object, which is positioned before the first entry, or null.
     */
    public DbMovieCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new DbMovieCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public DbMovieCursor query(Context context) {
        return query(context, null);
    }


    public DbMovieSelection id(long... value) {
        addEquals("db_movie." + DbMovieColumns._ID, toObjectArray(value));
        return this;
    }

    public DbMovieSelection idNot(long... value) {
        addNotEquals("db_movie." + DbMovieColumns._ID, toObjectArray(value));
        return this;
    }

    public DbMovieSelection orderById(boolean desc) {
        orderBy("db_movie." + DbMovieColumns._ID, desc);
        return this;
    }

    public DbMovieSelection orderById() {
        return orderById(false);
    }

    public DbMovieSelection movieId(String... value) {
        addEquals(DbMovieColumns.MOVIE_ID, value);
        return this;
    }

    public DbMovieSelection movieIdNot(String... value) {
        addNotEquals(DbMovieColumns.MOVIE_ID, value);
        return this;
    }

    public DbMovieSelection movieIdLike(String... value) {
        addLike(DbMovieColumns.MOVIE_ID, value);
        return this;
    }

    public DbMovieSelection movieIdContains(String... value) {
        addContains(DbMovieColumns.MOVIE_ID, value);
        return this;
    }

    public DbMovieSelection movieIdStartsWith(String... value) {
        addStartsWith(DbMovieColumns.MOVIE_ID, value);
        return this;
    }

    public DbMovieSelection movieIdEndsWith(String... value) {
        addEndsWith(DbMovieColumns.MOVIE_ID, value);
        return this;
    }

    public DbMovieSelection orderByMovieId(boolean desc) {
        orderBy(DbMovieColumns.MOVIE_ID, desc);
        return this;
    }

    public DbMovieSelection orderByMovieId() {
        orderBy(DbMovieColumns.MOVIE_ID, false);
        return this;
    }

    public DbMovieSelection title(String... value) {
        addEquals(DbMovieColumns.TITLE, value);
        return this;
    }

    public DbMovieSelection titleNot(String... value) {
        addNotEquals(DbMovieColumns.TITLE, value);
        return this;
    }

    public DbMovieSelection titleLike(String... value) {
        addLike(DbMovieColumns.TITLE, value);
        return this;
    }

    public DbMovieSelection titleContains(String... value) {
        addContains(DbMovieColumns.TITLE, value);
        return this;
    }

    public DbMovieSelection titleStartsWith(String... value) {
        addStartsWith(DbMovieColumns.TITLE, value);
        return this;
    }

    public DbMovieSelection titleEndsWith(String... value) {
        addEndsWith(DbMovieColumns.TITLE, value);
        return this;
    }

    public DbMovieSelection orderByTitle(boolean desc) {
        orderBy(DbMovieColumns.TITLE, desc);
        return this;
    }

    public DbMovieSelection orderByTitle() {
        orderBy(DbMovieColumns.TITLE, false);
        return this;
    }

    public DbMovieSelection originalTitle(String... value) {
        addEquals(DbMovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public DbMovieSelection originalTitleNot(String... value) {
        addNotEquals(DbMovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public DbMovieSelection originalTitleLike(String... value) {
        addLike(DbMovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public DbMovieSelection originalTitleContains(String... value) {
        addContains(DbMovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public DbMovieSelection originalTitleStartsWith(String... value) {
        addStartsWith(DbMovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public DbMovieSelection originalTitleEndsWith(String... value) {
        addEndsWith(DbMovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public DbMovieSelection orderByOriginalTitle(boolean desc) {
        orderBy(DbMovieColumns.ORIGINAL_TITLE, desc);
        return this;
    }

    public DbMovieSelection orderByOriginalTitle() {
        orderBy(DbMovieColumns.ORIGINAL_TITLE, false);
        return this;
    }

    public DbMovieSelection posterPath(String... value) {
        addEquals(DbMovieColumns.POSTER_PATH, value);
        return this;
    }

    public DbMovieSelection posterPathNot(String... value) {
        addNotEquals(DbMovieColumns.POSTER_PATH, value);
        return this;
    }

    public DbMovieSelection posterPathLike(String... value) {
        addLike(DbMovieColumns.POSTER_PATH, value);
        return this;
    }

    public DbMovieSelection posterPathContains(String... value) {
        addContains(DbMovieColumns.POSTER_PATH, value);
        return this;
    }

    public DbMovieSelection posterPathStartsWith(String... value) {
        addStartsWith(DbMovieColumns.POSTER_PATH, value);
        return this;
    }

    public DbMovieSelection posterPathEndsWith(String... value) {
        addEndsWith(DbMovieColumns.POSTER_PATH, value);
        return this;
    }

    public DbMovieSelection orderByPosterPath(boolean desc) {
        orderBy(DbMovieColumns.POSTER_PATH, desc);
        return this;
    }

    public DbMovieSelection orderByPosterPath() {
        orderBy(DbMovieColumns.POSTER_PATH, false);
        return this;
    }

    public DbMovieSelection voteAverage(String... value) {
        addEquals(DbMovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public DbMovieSelection voteAverageNot(String... value) {
        addNotEquals(DbMovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public DbMovieSelection voteAverageLike(String... value) {
        addLike(DbMovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public DbMovieSelection voteAverageContains(String... value) {
        addContains(DbMovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public DbMovieSelection voteAverageStartsWith(String... value) {
        addStartsWith(DbMovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public DbMovieSelection voteAverageEndsWith(String... value) {
        addEndsWith(DbMovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public DbMovieSelection orderByVoteAverage(boolean desc) {
        orderBy(DbMovieColumns.VOTE_AVERAGE, desc);
        return this;
    }

    public DbMovieSelection orderByVoteAverage() {
        orderBy(DbMovieColumns.VOTE_AVERAGE, false);
        return this;
    }

    public DbMovieSelection overview(String... value) {
        addEquals(DbMovieColumns.OVERVIEW, value);
        return this;
    }

    public DbMovieSelection overviewNot(String... value) {
        addNotEquals(DbMovieColumns.OVERVIEW, value);
        return this;
    }

    public DbMovieSelection overviewLike(String... value) {
        addLike(DbMovieColumns.OVERVIEW, value);
        return this;
    }

    public DbMovieSelection overviewContains(String... value) {
        addContains(DbMovieColumns.OVERVIEW, value);
        return this;
    }

    public DbMovieSelection overviewStartsWith(String... value) {
        addStartsWith(DbMovieColumns.OVERVIEW, value);
        return this;
    }

    public DbMovieSelection overviewEndsWith(String... value) {
        addEndsWith(DbMovieColumns.OVERVIEW, value);
        return this;
    }

    public DbMovieSelection orderByOverview(boolean desc) {
        orderBy(DbMovieColumns.OVERVIEW, desc);
        return this;
    }

    public DbMovieSelection orderByOverview() {
        orderBy(DbMovieColumns.OVERVIEW, false);
        return this;
    }

    public DbMovieSelection releaseDate(String... value) {
        addEquals(DbMovieColumns.RELEASE_DATE, value);
        return this;
    }

    public DbMovieSelection releaseDateNot(String... value) {
        addNotEquals(DbMovieColumns.RELEASE_DATE, value);
        return this;
    }

    public DbMovieSelection releaseDateLike(String... value) {
        addLike(DbMovieColumns.RELEASE_DATE, value);
        return this;
    }

    public DbMovieSelection releaseDateContains(String... value) {
        addContains(DbMovieColumns.RELEASE_DATE, value);
        return this;
    }

    public DbMovieSelection releaseDateStartsWith(String... value) {
        addStartsWith(DbMovieColumns.RELEASE_DATE, value);
        return this;
    }

    public DbMovieSelection releaseDateEndsWith(String... value) {
        addEndsWith(DbMovieColumns.RELEASE_DATE, value);
        return this;
    }

    public DbMovieSelection orderByReleaseDate(boolean desc) {
        orderBy(DbMovieColumns.RELEASE_DATE, desc);
        return this;
    }

    public DbMovieSelection orderByReleaseDate() {
        orderBy(DbMovieColumns.RELEASE_DATE, false);
        return this;
    }
}
