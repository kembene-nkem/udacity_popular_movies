package com.kinwae.popularmovies.services;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.data.MovieRequestResponse;

import java.util.List;

import retrofit.http.GET;
import retrofit.http.Query;

/**
 * Created by Kembene on 7/8/2015.
 */
public interface MovieService {
    @GET("/discover/movie")
    MovieRequestResponse getMovies(@Query("sort_by") String sort, @Query("page") int page);
}
