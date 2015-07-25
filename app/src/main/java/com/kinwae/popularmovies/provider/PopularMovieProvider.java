package com.kinwae.popularmovies.provider;

import java.util.Arrays;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.kinwae.popularmovies.BuildConfig;
import com.kinwae.popularmovies.provider.base.BaseContentProvider;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieColumns;
import com.kinwae.popularmovies.provider.dbreview.DbReviewColumns;
import com.kinwae.popularmovies.provider.dbtrailer.DbTrailerColumns;

public class PopularMovieProvider extends BaseContentProvider {
    private static final String TAG = PopularMovieProvider.class.getSimpleName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";

    public static final String AUTHORITY = "com.kinwae.popularmovies";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;

    private static final int URI_TYPE_DB_MOVIE = 0;
    private static final int URI_TYPE_DB_MOVIE_ID = 1;

    private static final int URI_TYPE_DB_REVIEW = 2;
    private static final int URI_TYPE_DB_REVIEW_ID = 3;

    private static final int URI_TYPE_DB_TRAILER = 4;
    private static final int URI_TYPE_DB_TRAILER_ID = 5;



    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, DbMovieColumns.TABLE_NAME, URI_TYPE_DB_MOVIE);
        URI_MATCHER.addURI(AUTHORITY, DbMovieColumns.TABLE_NAME + "/#", URI_TYPE_DB_MOVIE_ID);
        URI_MATCHER.addURI(AUTHORITY, DbReviewColumns.TABLE_NAME, URI_TYPE_DB_REVIEW);
        URI_MATCHER.addURI(AUTHORITY, DbReviewColumns.TABLE_NAME + "/#", URI_TYPE_DB_REVIEW_ID);
        URI_MATCHER.addURI(AUTHORITY, DbTrailerColumns.TABLE_NAME, URI_TYPE_DB_TRAILER);
        URI_MATCHER.addURI(AUTHORITY, DbTrailerColumns.TABLE_NAME + "/#", URI_TYPE_DB_TRAILER_ID);
    }

    @Override
    protected SQLiteOpenHelper createSqLiteOpenHelper() {
        return PopularMovieSQLiteOpenHelper.getInstance(getContext());
    }

    @Override
    protected boolean hasDebug() {
        return DEBUG;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_DB_MOVIE:
                return TYPE_CURSOR_DIR + DbMovieColumns.TABLE_NAME;
            case URI_TYPE_DB_MOVIE_ID:
                return TYPE_CURSOR_ITEM + DbMovieColumns.TABLE_NAME;

            case URI_TYPE_DB_REVIEW:
                return TYPE_CURSOR_DIR + DbReviewColumns.TABLE_NAME;
            case URI_TYPE_DB_REVIEW_ID:
                return TYPE_CURSOR_ITEM + DbReviewColumns.TABLE_NAME;

            case URI_TYPE_DB_TRAILER:
                return TYPE_CURSOR_DIR + DbTrailerColumns.TABLE_NAME;
            case URI_TYPE_DB_TRAILER_ID:
                return TYPE_CURSOR_ITEM + DbTrailerColumns.TABLE_NAME;

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        return super.insert(uri, values);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.update(uri, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
                    + " groupBy=" + uri.getQueryParameter(QUERY_GROUP_BY) + " having=" + uri.getQueryParameter(QUERY_HAVING) + " limit=" + uri.getQueryParameter(QUERY_LIMIT));
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected QueryParams getQueryParams(Uri uri, String selection, String[] projection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_DB_MOVIE:
            case URI_TYPE_DB_MOVIE_ID:
                res.table = DbMovieColumns.TABLE_NAME;
                res.idColumn = DbMovieColumns._ID;
                res.tablesWithJoins = DbMovieColumns.TABLE_NAME;
                res.orderBy = DbMovieColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_DB_REVIEW:
            case URI_TYPE_DB_REVIEW_ID:
                res.table = DbReviewColumns.TABLE_NAME;
                res.idColumn = DbReviewColumns._ID;
                res.tablesWithJoins = DbReviewColumns.TABLE_NAME;
                res.orderBy = DbReviewColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_DB_TRAILER:
            case URI_TYPE_DB_TRAILER_ID:
                res.table = DbTrailerColumns.TABLE_NAME;
                res.idColumn = DbTrailerColumns._ID;
                res.tablesWithJoins = DbTrailerColumns.TABLE_NAME;
                res.orderBy = DbTrailerColumns.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_DB_MOVIE_ID:
            case URI_TYPE_DB_REVIEW_ID:
            case URI_TYPE_DB_TRAILER_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = res.table + "." + res.idColumn + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = res.table + "." + res.idColumn + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }
}
