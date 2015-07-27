package com.kinwae.popularmovies.provider.dbtrailer;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kinwae.popularmovies.data.Trailer;
import com.kinwae.popularmovies.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code db_trailer} table.
 */
public class DbTrailerCursor extends AbstractCursor implements DbTrailerModel {
    public DbTrailerCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(DbTrailerColumns._ID);
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
        String res = getStringOrNull(DbTrailerColumns.MOVIE_ID);
        return res;
    }

    /**
     * Trailer name
     * Can be {@code null}.
     */
    @Nullable
    public String getName() {
        String res = getStringOrNull(DbTrailerColumns.NAME);
        return res;
    }

    /**
     * Youtube video id
     * Can be {@code null}.
     */
    @Nullable
    public String getYoutubeId() {
        String res = getStringOrNull(DbTrailerColumns.YOUTUBE_ID);
        return res;
    }

    public Trailer getTrailer(){
        Trailer trailer = new Trailer(getName(), getYoutubeId());
        return trailer;
    }
}
