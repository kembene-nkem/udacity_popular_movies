package com.kinwae.popularmovies.loaders.delegates;

import com.kinwae.popularmovies.data.Movie;

/**
 * Created by Kembene on 7/26/2015.
 */
public interface MovieLoaderDataDelegate {

    int getPageCount();

    int getNumberOfItems();

    Movie getMovieAt(int position);
}
