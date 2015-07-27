package com.kinwae.popularmovies.provider.dbtrailer;

import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.data.Trailer;
import com.kinwae.popularmovies.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code db_trailer} table.
 */
public class DbTrailerContentValues extends AbstractContentValues {

    public DbTrailerContentValues() {
    }

    public int bulkInsertTrailers(Movie movie, ContentResolver contentResolver) {
        List<Trailer> trailers = movie.getTrailers();
        ContentValues[] values = null;
        if(trailers != null){
            values = new ContentValues[trailers.size()];
            for(int i=0; i<trailers.size(); i++){
                Trailer trailer = trailers.get(i);
                DbTrailerContentValues contentValues = new DbTrailerContentValues();
                contentValues.putMovieId(Long.toString(movie.getId()));
                contentValues.putName(trailer.getName());
                contentValues.putYoutubeId(trailer.getSource());
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
        return DbTrailerColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable DbTrailerSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param context The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable DbTrailerSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * themoviedb movie id
     */
    public DbTrailerContentValues putMovieId(@Nullable String value) {
        mContentValues.put(DbTrailerColumns.MOVIE_ID, value);
        return this;
    }

    public DbTrailerContentValues putMovieIdNull() {
        mContentValues.putNull(DbTrailerColumns.MOVIE_ID);
        return this;
    }

    /**
     * Trailer name
     */
    public DbTrailerContentValues putName(@Nullable String value) {
        mContentValues.put(DbTrailerColumns.NAME, value);
        return this;
    }

    public DbTrailerContentValues putNameNull() {
        mContentValues.putNull(DbTrailerColumns.NAME);
        return this;
    }

    /**
     * Youtube video id
     */
    public DbTrailerContentValues putYoutubeId(@Nullable String value) {
        mContentValues.put(DbTrailerColumns.YOUTUBE_ID, value);
        return this;
    }

    public DbTrailerContentValues putYoutubeIdNull() {
        mContentValues.putNull(DbTrailerColumns.YOUTUBE_ID);
        return this;
    }
}
