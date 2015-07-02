package com.kinwae.popularmovies.views.bus;

import android.database.DataSetObserver;

import com.kinwae.popularmovies.views.adapters.MovieListPagerAdapter;
import com.kinwae.popularmovies.views.adapters.MoviePaginator;

/**
 * Created by Kembene on 7/1/2015.
 */
public interface MovieListManager {
    void registerChangeObserver(DataSetObserver observer);
    void unregisterChangeObserver(DataSetObserver observer);
    MoviePaginator getMoviePaginator();
    MovieListPagerAdapter getPagerAdapter();
}
