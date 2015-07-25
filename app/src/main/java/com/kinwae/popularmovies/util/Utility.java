package com.kinwae.popularmovies.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kinwae.popularmovies.R;
import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.events.MovieFavoritedEvent;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieColumns;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieContentValues;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieSelection;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by Kembene on 6/24/2015.
 */
public class Utility {

    public static final String MOVIE_DB_BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    private static Bus sharedBus;

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

    public static void loadImageIntoView(ImageView imageView, Movie movie, Context context){
        RequestCreator picassoCreator = null;
        if(movie.getPosterPath() == null){
            picassoCreator = Picasso.with(context)
                    .load(R.drawable.poster_placeholder);
        }
        else{
            String posterURL = Utility.getPosterURL(ImageSize.DEFAULT, movie.getPosterPath());

            picassoCreator = Picasso.with(context)
                    .load(posterURL);
        }
        picassoCreator
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder)
                        //.centerCrop()
                .into(imageView);
    }

    public static void setFavoritedStatus(boolean favorited, ImageButton button, Movie mMovie){
        button.setTag(favorited);
        if(favorited){
            button.setImageResource(R.drawable.ic_star_on);
        }
        else{
            button.setImageResource(R.drawable.ic_star_off);
        }
        if (favorited != mMovie.isFavorited()){
            mMovie.setFavorited(favorited);
            getSharedEventBus().post(new MovieFavoritedEvent(favorited, mMovie));
            Context context = button.getContext();
            ContentResolver contentResolver = context.getContentResolver();
            if(favorited){
                DbMovieContentValues values = new DbMovieContentValues(mMovie);
                Uri insert = contentResolver.insert(DbMovieColumns.CONTENT_URI, values.values());
            }
            else{
                DbMovieSelection dbselection = new DbMovieSelection();
                String movieId = Long.toString(mMovie.getId());
                String selection = dbselection.movieId(movieId).sel();
                contentResolver.delete(DbMovieColumns.CONTENT_URI, selection, new String[]{movieId});
            }
        }

    }

    public static Bus getSharedEventBus(){
        if (sharedBus == null){
            sharedBus = new Bus();
        }
        return sharedBus;
    }

}
