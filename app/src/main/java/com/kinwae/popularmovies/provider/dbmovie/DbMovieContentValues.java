package com.kinwae.popularmovies.provider.dbmovie;

import java.util.Date;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code db_movie} table.
 */
public class DbMovieContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return DbMovieColumns.CONTENT_URI;
    }

    public DbMovieContentValues() {
    }

    public DbMovieContentValues(Movie movie) {
        putMovie(movie);
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable DbMovieSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param context The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable DbMovieSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * themoviedb movie id
     */
    public DbMovieContentValues putMovieId(@Nullable String value) {
        mContentValues.put(DbMovieColumns.MOVIE_ID, value);
        return this;
    }

    public DbMovieContentValues putMovieIdNull() {
        mContentValues.putNull(DbMovieColumns.MOVIE_ID);
        return this;
    }

    /**
     * Movie title
     */
    public DbMovieContentValues putTitle(@Nullable String value) {
        mContentValues.put(DbMovieColumns.TITLE, value);
        return this;
    }

    public DbMovieContentValues putTitleNull() {
        mContentValues.putNull(DbMovieColumns.TITLE);
        return this;
    }

    /**
     * Original Title
     */
    public DbMovieContentValues putOriginalTitle(@Nullable String value) {
        mContentValues.put(DbMovieColumns.ORIGINAL_TITLE, value);
        return this;
    }

    public DbMovieContentValues putOriginalTitleNull() {
        mContentValues.putNull(DbMovieColumns.ORIGINAL_TITLE);
        return this;
    }

    public DbMovieContentValues putPosterPath(@Nullable String value) {
        mContentValues.put(DbMovieColumns.POSTER_PATH, value);
        return this;
    }

    public DbMovieContentValues putPosterPathNull() {
        mContentValues.putNull(DbMovieColumns.POSTER_PATH);
        return this;
    }

    public DbMovieContentValues putVoteAverage(@Nullable String value) {
        mContentValues.put(DbMovieColumns.VOTE_AVERAGE, value);
        return this;
    }

    public DbMovieContentValues putVoteAverageNull() {
        mContentValues.putNull(DbMovieColumns.VOTE_AVERAGE);
        return this;
    }

    public DbMovieContentValues putOverview(@Nullable String value) {
        mContentValues.put(DbMovieColumns.OVERVIEW, value);
        return this;
    }

    public DbMovieContentValues putOverviewNull() {
        mContentValues.putNull(DbMovieColumns.OVERVIEW);
        return this;
    }

    public DbMovieContentValues putReleaseDate(@Nullable String value) {
        mContentValues.put(DbMovieColumns.RELEASE_DATE, value);
        return this;
    }

    public DbMovieContentValues putReleaseDateNull() {
        mContentValues.putNull(DbMovieColumns.RELEASE_DATE);
        return this;
    }

    public DbMovieContentValues putMovie(Movie movie){
        putMovieId(Long.toString(movie.getId()));
        putOriginalTitle(movie.getOriginalTitle());
        putOverview(movie.getOverview());
        putPosterPath(movie.getPosterPath());
        putReleaseDate(movie.getReleaseDate());
        putTitle(movie.getTitle());
        putVoteAverage(Double.toString(movie.getVoteAverage()));
        return this;
    }
}
