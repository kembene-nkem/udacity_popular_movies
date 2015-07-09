package com.kinwae.popularmovies.data;

import java.util.List;

/**
 * Created by Kembene on 6/27/2015.
 */
public class MovieRequestResponse {
    List<Movie> results;
    int page;
    int totalPages;

    public List<Movie> getMovies() {
        return results;
    }

    public List<Movie> getResults() {
        return results;
    }

    public MovieRequestResponse setResults(List<Movie> results) {
        this.results = results;
        return this;
    }


    public int getPage() {
        return page;
    }

    public MovieRequestResponse setPage(int page) {
        this.page = page;
        return this;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public MovieRequestResponse setTotalPages(int totalPages) {
        this.totalPages = totalPages;
        return this;
    }
}
