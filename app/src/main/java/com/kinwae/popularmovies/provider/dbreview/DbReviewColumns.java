package com.kinwae.popularmovies.provider.dbreview;

import android.net.Uri;
import android.provider.BaseColumns;

import com.kinwae.popularmovies.provider.PopularMovieProvider;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieColumns;
import com.kinwae.popularmovies.provider.dbreview.DbReviewColumns;
import com.kinwae.popularmovies.provider.dbtrailer.DbTrailerColumns;

/**
 * Reviews associated with this movie
 */
public class DbReviewColumns implements BaseColumns {
    public static final String TABLE_NAME = "db_review";
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
     * Reviewer name
     */
    public static final String REVIEWER = "reviewer";

    /**
     * Review
     */
    public static final String REVIEW = "review";

    /**
     * Review Id
     */
    public static final String REVIEW_ID = "review_id";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            MOVIE_ID,
            REVIEWER,
            REVIEW,
            REVIEW_ID
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(MOVIE_ID) || c.contains("." + MOVIE_ID)) return true;
            if (c.equals(REVIEWER) || c.contains("." + REVIEWER)) return true;
            if (c.equals(REVIEW) || c.contains("." + REVIEW)) return true;
            if (c.equals(REVIEW_ID) || c.contains("." + REVIEW_ID)) return true;
        }
        return false;
    }

}
