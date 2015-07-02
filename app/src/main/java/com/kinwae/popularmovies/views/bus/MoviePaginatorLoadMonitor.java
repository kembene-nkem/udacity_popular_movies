package com.kinwae.popularmovies.views.bus;

import android.content.Context;

import com.kinwae.popularmovies.loaders.MovieListLoader;

/**
 * Created by Kembene on 6/27/2015.
 */
public interface MoviePaginatorLoadMonitor {
    void paginatorLoadComplete(MovieListLoader paginator);
    void bindLoader(MovieListLoader loader);
}
