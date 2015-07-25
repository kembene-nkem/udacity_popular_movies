package com.kinwae.popularmovies.events;

import com.kinwae.popularmovies.data.Movie;

/**
 * Created by Kembene on 7/25/2015.
 */
public class MovieFavoritedEvent {
    boolean favorited;
    Movie movie;

    public MovieFavoritedEvent(boolean favorited, Movie movie) {
        this.favorited = favorited;
        this.movie = movie;
    }

    public boolean wasFavorited() {
        return favorited;
    }
    public Movie getMovie() {
        return movie;
    }
}
