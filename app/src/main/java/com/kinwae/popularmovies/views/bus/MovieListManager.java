package com.kinwae.popularmovies.views.bus;

import com.kinwae.popularmovies.views.adapters.MoviePagerAdapter;
import com.kinwae.popularmovies.views.adapters.MoviePaginator;

/**
 * Created by Kembene on 7/1/2015.
 */
public interface MovieListManager {
    MoviePaginator getMoviePaginator();
    MoviePagerAdapter getPagerAdapter();
}
