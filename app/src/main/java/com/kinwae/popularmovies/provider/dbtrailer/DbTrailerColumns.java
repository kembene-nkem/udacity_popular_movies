package com.kinwae.popularmovies.provider.dbtrailer;

import android.net.Uri;
import android.provider.BaseColumns;

import com.kinwae.popularmovies.provider.PopularMovieProvider;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieColumns;
import com.kinwae.popularmovies.provider.dbreview.DbReviewColumns;
import com.kinwae.popularmovies.provider.dbtrailer.DbTrailerColumns;

/**
 * Trailers associated with a particular movie
 */
public class DbTrailerColumns implements BaseColumns {
    public static final String TABLE_NAME = "db_trailer";
    public static final Uri CONTENT_URI = Uri.parse(PopularMovieProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    /**
     * themoviedb movie id
     */
    public static final String MOVIE_ID = "movie_id";

    /**
     * Trailer name
     */
    public static final String NAME = "name";

    /**
     * Youtube video id
     */
    public static final String YOUTUBE_ID = "youtube_id";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            MOVIE_ID,
            NAME,
            YOUTUBE_ID
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(MOVIE_ID) || c.contains("." + MOVIE_ID)) return true;
            if (c.equals(NAME) || c.contains("." + NAME)) return true;
            if (c.equals(YOUTUBE_ID) || c.contains("." + YOUTUBE_ID)) return true;
        }
        return false;
    }

}
