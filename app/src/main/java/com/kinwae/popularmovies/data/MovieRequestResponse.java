package com.kinwae.popularmovies.data;

import java.util.List;

/**
 * Created by Kembene on 6/27/2015.
 */
public class MovieRequestResponse {
    List<Movie> movies;
    int page;
    int numberOfPages;

    public List<Movie> getMovies() {
        return movies;
    }

    public MovieRequestResponse setMovies(List<Movie> movies) {
        this.movies = movies;
        return this;
    }

    public int getPage() {
        return page;
    }

    public MovieRequestResponse setPage(int page) {
        this.page = page;
        return this;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public MovieRequestResponse setNumberOfPages(int numberOfPages) {
        this.numberOfPages = numberOfPages;
        return this;
    }
}
