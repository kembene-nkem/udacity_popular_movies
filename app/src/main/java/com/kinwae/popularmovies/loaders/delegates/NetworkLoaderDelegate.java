package com.kinwae.popularmovies.loaders.delegates;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.views.adapters.MoviePaginator;

import java.util.List;

/**
 * Created by Kembene on 7/26/2015.
 */
public class NetworkLoaderDelegate implements MovieLoaderDataDelegate {
    private int pageCount;
    private int numberOfItems;
    private List<Movie> movieList;
    private MoviePaginator paginator;

    public MoviePaginator getPaginator() {
        return paginator;
    }

    public NetworkLoaderDelegate setPaginator(MoviePaginator paginator) {
        this.paginator = paginator;
        return this;
    }

    @Override
    public int getPageCount() {
        return pageCount;
    }

    public NetworkLoaderDelegate setPageCount(int pageCount) {
        this.pageCount = pageCount;
        return this;
    }

    @Override
    public int getNumberOfItems() {
        return numberOfItems;
    }

    @Override
    public Movie getMovieAt(int position) {
        if(movieList != null && position >= 0 && position < movieList.size()){
            return movieList.get(position);
        }
        return null;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public NetworkLoaderDelegate setMovieList(List<Movie> movieList) {
        this.movieList = movieList;
        this.numberOfItems = 0;
        if(this.movieList != null){
            this.numberOfItems = this.movieList.size();
        }
        return this;
    }

}
