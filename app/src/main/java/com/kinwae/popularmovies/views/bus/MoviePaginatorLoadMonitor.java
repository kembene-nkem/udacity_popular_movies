package com.kinwae.popularmovies.views.bus;

import com.kinwae.popularmovies.loaders.MovieListNetworkLoader;

/**
 * Created by Kembene on 6/27/2015.
 */
public interface MoviePaginatorLoadMonitor {
    void paginatorLoadComplete(MovieListNetworkLoader paginator);
    void bindLoader(MovieListNetworkLoader loader);
}
