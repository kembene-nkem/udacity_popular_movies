package com.kinwae.popularmovies.views.bus;

import com.kinwae.popularmovies.views.managers.DefaultMovieListManager;

/**
 * Created by Kembene on 7/1/2015.
 */
public interface MovieListManagerAware {
    void setMovieListManager(MovieListManager movieListManager);
}
