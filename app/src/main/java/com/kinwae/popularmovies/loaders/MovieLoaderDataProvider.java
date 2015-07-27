package com.kinwae.popularmovies.loaders;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.loaders.delegates.MovieLoaderDataDelegate;

/**
 * Created by Kembene on 7/26/2015.
 */
public class MovieLoaderDataProvider {
    private MovieLoaderDataDelegate dataDelegate;
    private int loaderType;

    public static final int LOADER_TYPE_CURSOR = 1;
    public static final int LOADER_TYPE_NETWORK = 2;

    public MovieLoaderDataProvider(MovieLoaderDataDelegate dataDelegate) {
        this.dataDelegate = dataDelegate;
    }

    public MovieLoaderDataProvider(MovieLoaderDataDelegate dataDelegate, int loaderType) {
        this.dataDelegate = dataDelegate;
        this.loaderType = loaderType;
    }

    public Movie getMovieAt(int position){
        return dataDelegate.getMovieAt(position);
    }

    public int getItemsCount() {
        return dataDelegate.getNumberOfItems();
    }

    public MovieLoaderDataDelegate getDataDelegate() {
        return dataDelegate;
    }

    public int getLoaderType() {
        return loaderType;
    }

    public MovieLoaderDataProvider setLoaderType(int loaderType) {
        this.loaderType = loaderType;
        return this;
    }
}
