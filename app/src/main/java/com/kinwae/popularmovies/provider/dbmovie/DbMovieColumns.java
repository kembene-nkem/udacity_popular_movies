package com.kinwae.popularmovies.provider.dbmovie;

import android.net.Uri;
import android.provider.BaseColumns;

import com.kinwae.popularmovies.provider.PopularMovieProvider;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieColumns;
import com.kinwae.popularmovies.provider.dbreview.DbReviewColumns;
import com.kinwae.popularmovies.provider.dbtrailer.DbTrailerColumns;

/**
 * Keeps a list of all favorited movies.
 */
public class DbMovieColumns implements BaseColumns {
    public static final String TABLE_NAME = "db_movie";
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
     * Movie title
     */
    public static final String TITLE = "title";

    /**
     * Original Title
     */
    public static final String ORIGINAL_TITLE = "original_title";

    public static final String POSTER_PATH = "poster_path";

    public static final String VOTE_AVERAGE = "vote_average";

    public static final String OVERVIEW = "overview";

    public static final String RELEASE_DATE = "release_date";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            MOVIE_ID,
            TITLE,
            ORIGINAL_TITLE,
            POSTER_PATH,
            VOTE_AVERAGE,
            OVERVIEW,
            RELEASE_DATE
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(MOVIE_ID) || c.contains("." + MOVIE_ID)) return true;
            if (c.equals(TITLE) || c.contains("." + TITLE)) return true;
            if (c.equals(ORIGINAL_TITLE) || c.contains("." + ORIGINAL_TITLE)) return true;
            if (c.equals(POSTER_PATH) || c.contains("." + POSTER_PATH)) return true;
            if (c.equals(VOTE_AVERAGE) || c.contains("." + VOTE_AVERAGE)) return true;
            if (c.equals(OVERVIEW) || c.contains("." + OVERVIEW)) return true;
            if (c.equals(RELEASE_DATE) || c.contains("." + RELEASE_DATE)) return true;
        }
        return false;
    }

}
