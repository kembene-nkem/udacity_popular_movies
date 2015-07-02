package com.kinwae.popularmovies.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.kinwae.popularmovies.R;

/**
 * Created by Kembene on 6/24/2015.
 */
public class Utility {

    public static final String MOVIE_DB_BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    public static String getPreferredMovieSortOrder(Context context){
        SharedPreferences prefManager = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultSort = context.getString(R.string.pref_movie_sort_default);
        return prefManager.getString(
                context.getString(R.string.pref_movie_sort_key),
                defaultSort
        );
    }

    public static int getPreferredGridColumns(Context context){
        int default_value = Integer.parseInt(context.getString(R.string.pref_grid_columns_default));
        int value = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(context.getString(R.string.pref_grid_columns_name), default_value);
        return value;
    }

    public static String getPosterURL(ImageSize imageSize, String posterURL){
        return MOVIE_DB_BASE_IMAGE_URL + imageSize.getSize() + posterURL;
    }

}
