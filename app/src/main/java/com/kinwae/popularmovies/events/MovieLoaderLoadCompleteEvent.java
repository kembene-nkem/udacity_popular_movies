package com.kinwae.popularmovies.events;

import com.kinwae.popularmovies.loaders.MovieLoaderDataProvider;

/**
 * Created by Kembene on 7/26/2015.
 */
public class MovieLoaderLoadCompleteEvent {
    private MovieLoaderDataProvider dataProvider;

    public MovieLoaderLoadCompleteEvent(MovieLoaderDataProvider dataProvider) {
        this.dataProvider = dataProvider;
    }

    public MovieLoaderDataProvider getDataProvider() {
        return dataProvider;
    }

    public MovieLoaderLoadCompleteEvent setDataProvider(MovieLoaderDataProvider dataProvider) {
        this.dataProvider = dataProvider;
        return this;
    }
}
