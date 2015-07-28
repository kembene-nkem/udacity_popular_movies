package com.kinwae.popularmovies.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.kinwae.popularmovies.R;
import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.events.MovieFavoritedEvent;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieColumns;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieContentValues;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieSelection;
import com.kinwae.popularmovies.provider.dbreview.DbReviewColumns;
import com.kinwae.popularmovies.provider.dbreview.DbReviewContentValues;
import com.kinwae.popularmovies.provider.dbreview.DbReviewSelection;
import com.kinwae.popularmovies.provider.dbtrailer.DbTrailerColumns;
import com.kinwae.popularmovies.provider.dbtrailer.DbTrailerContentValues;
import com.kinwae.popularmovies.provider.dbtrailer.DbTrailerSelection;
import com.squareup.otto.Bus;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

/**
 * Created by Kembene on 6/24/2015.
 */
public class Utility {

    public static final String MOVIE_DB_BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";

    private static Bus sharedBus;


    public static final int SORT_CATEGORY_NETWORK = 1;
    public static final int SORT_CATEGORY_CURSOR = 2;
    private static int totalMoviePageFromNetwork = 1;

    public static String getPreferredMovieSortOrder(Context context){
        SharedPreferences prefManager = PreferenceManager.getDefaultSharedPreferences(context);
        String defaultSort = context.getString(R.string.pref_movie_sort_default);
        return prefManager.getString(
                context.getString(R.string.pref_movie_sort_key),
                defaultSort
        );
    }

    public static int getTotalMoviePageFromNetwork(){
        return totalMoviePageFromNetwork;
    }

    public static void setTotalMoviePageFromNetwork(int pagesCount){
        totalMoviePageFromNetwork = pagesCount;
    }

    public static int getPreferredGridColumns(Context context){
        int default_value = Integer.parseInt(context.getString(R.string.pref_grid_columns_default));
        int value = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(context.getString(R.string.pref_grid_columns_name), default_value);
        return value;
    }

    public static int getPreferredMoviesPerPage(Context context){
        int default_value = Integer.parseInt(context.getString(R.string.pref_movies_per_page));
        int value = PreferenceManager.getDefaultSharedPreferences(context)
                .getInt(context.getString(R.string.pref_movies_per_page), default_value);
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

    public static void setFavoritedStatus(final boolean favorited, ImageButton button, final Movie mMovie, boolean broadcast){
        button.setTag(favorited);
        if(favorited){
            button.setImageResource(R.drawable.ic_star_on);
        }
        else{
            button.setImageResource(R.drawable.ic_star_off);
        }
        if(broadcast){
            if (favorited != mMovie.isFavorited()){
                mMovie.setFavorited(favorited);
                getSharedEventBus().post(new MovieFavoritedEvent(favorited, mMovie));
                Context context = button.getContext();
                final ContentResolver contentResolver = context.getContentResolver();
                new AsyncTask<Void, Void, Void>(){

                    @Override
                    protected Void doInBackground(Void... params) {
                        if(favorited){
                            insert();
                        }
                        else {
                            delete();
                        }

                        return null;
                    }

                    private void insert(){
                        DbMovieContentValues values = new DbMovieContentValues(mMovie);
                        DbTrailerContentValues tValues = new DbTrailerContentValues();
                        DbReviewContentValues rValues = new DbReviewContentValues();

                        values.insert(contentResolver);
                        tValues.bulkInsertTrailers(mMovie, contentResolver);
                        rValues.bulkInsertReviewss(mMovie, contentResolver);
                    }
                    private void delete(){
                        DbMovieSelection movieSelection = new DbMovieSelection();
                        movieSelection.movieId(Long.toString(mMovie.getId()));

                        DbTrailerSelection trailerSelection = new DbTrailerSelection();
                        trailerSelection.movieId(Long.toString(mMovie.getId()));

                        DbReviewSelection reviewSelection = new DbReviewSelection();
                        reviewSelection.movieId(Long.toString(mMovie.getId()));

                        contentResolver.delete(DbMovieColumns.CONTENT_URI, movieSelection.sel(), movieSelection.args());
                        contentResolver.delete(DbTrailerColumns.CONTENT_URI, trailerSelection.sel(), trailerSelection.args());
                        contentResolver.delete(DbReviewColumns.CONTENT_URI, reviewSelection.sel(), reviewSelection.args());
                    }
                }.execute();
            }
        }

    }

    public static Bus getSharedEventBus(){
        if (sharedBus == null){
            sharedBus = new Bus();
        }
        return sharedBus;
    }

    public static int getSortCategory(Context context, String sortType){
        if (sortType == null){
            sortType = getPreferredMovieSortOrder(context);
        }
        String popularity = context.getString(R.string.pref_sort_type_popularity);
        String vote = context.getString(R.string.pref_sort_type_vote);
        if (sortType.equals(popularity) || sortType.equals(vote)){
            return SORT_CATEGORY_NETWORK;
        }
        return SORT_CATEGORY_CURSOR;
    }

}
