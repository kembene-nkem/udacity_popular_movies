package com.kinwae.popularmovies.events;

import com.kinwae.popularmovies.data.Movie;

/**
 * Created by Kembene on 7/22/2015.
 */
public class MovieDetailLoadedEvent {
    private Movie movie;

    public MovieDetailLoadedEvent() {
        this(null);
    }

    public MovieDetailLoadedEvent(Movie movie) {
        this.movie = movie;
    }

    public Movie getMovie() {
        return movie;
    }

    public MovieDetailLoadedEvent setMovie(Movie movie) {
        this.movie = movie;
        return this;
    }
}
