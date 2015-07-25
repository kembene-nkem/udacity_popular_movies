package com.kinwae.popularmovies.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.kinwae.popularmovies.BuildConfig;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieColumns;
import com.kinwae.popularmovies.provider.dbreview.DbReviewColumns;
import com.kinwae.popularmovies.provider.dbtrailer.DbTrailerColumns;

public class PopularMovieSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = PopularMovieSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "popularmovies.db";
    private static final int DATABASE_VERSION = 1;
    private static PopularMovieSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final PopularMovieSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_DB_MOVIE = "CREATE TABLE IF NOT EXISTS "
            + DbMovieColumns.TABLE_NAME + " ( "
            + DbMovieColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DbMovieColumns.MOVIE_ID + " TEXT, "
            + DbMovieColumns.TITLE + " TEXT, "
            + DbMovieColumns.ORIGINAL_TITLE + " TEXT, "
            + DbMovieColumns.POSTER_PATH + " TEXT, "
            + DbMovieColumns.VOTE_AVERAGE + " TEXT, "
            + DbMovieColumns.OVERVIEW + " TEXT, "
            + DbMovieColumns.RELEASE_DATE + " TEXT "
            + " );";

    public static final String SQL_CREATE_TABLE_DB_REVIEW = "CREATE TABLE IF NOT EXISTS "
            + DbReviewColumns.TABLE_NAME + " ( "
            + DbReviewColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DbReviewColumns.MOVIE_ID + " TEXT, "
            + DbReviewColumns.REVIEWER + " TEXT, "
            + DbReviewColumns.REVIEW + " TEXT, "
            + DbReviewColumns.REVIEW_ID + " TEXT "
            + " );";

    public static final String SQL_CREATE_TABLE_DB_TRAILER = "CREATE TABLE IF NOT EXISTS "
            + DbTrailerColumns.TABLE_NAME + " ( "
            + DbTrailerColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + DbTrailerColumns.MOVIE_ID + " TEXT, "
            + DbTrailerColumns.NAME + " TEXT, "
            + DbTrailerColumns.YOUTUBE_ID + " TEXT "
            + " );";

    // @formatter:on

    public static PopularMovieSQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static PopularMovieSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static PopularMovieSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new PopularMovieSQLiteOpenHelper(context);
    }

    private PopularMovieSQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new PopularMovieSQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static PopularMovieSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new PopularMovieSQLiteOpenHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private PopularMovieSQLiteOpenHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new PopularMovieSQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_DB_MOVIE);
        db.execSQL(SQL_CREATE_TABLE_DB_REVIEW);
        db.execSQL(SQL_CREATE_TABLE_DB_TRAILER);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        if (!db.isReadOnly()) {
            setForeignKeyConstraintsEnabled(db);
        }
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    private void setForeignKeyConstraintsEnabled(SQLiteDatabase db) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setForeignKeyConstraintsEnabledPreJellyBean(db);
        } else {
            setForeignKeyConstraintsEnabledPostJellyBean(db);
        }
    }

    private void setForeignKeyConstraintsEnabledPreJellyBean(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void setForeignKeyConstraintsEnabledPostJellyBean(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
