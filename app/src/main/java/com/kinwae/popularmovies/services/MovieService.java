package com.kinwae.popularmovies.services;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.data.MovieDetailResponse;
import com.kinwae.popularmovies.data.MovieRequestResponse;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Headers;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Created by Kembene on 7/8/2015.
 */
public interface MovieService {
    @Headers({
            "Accept: application/vnd.github.v3.full+json",
            "User-Agent: Kinwae-The-Movie-App-v1"
    })
    @GET("/discover/movie")
    MovieRequestResponse getMovies(@Query("sort_by") String sort, @Query("page") int page);
    @GET("/movie/{movie_id}")
    void getMovieDetail(@Path("movie_id")long movieId,
                        @Query("append_to_response") String append_to_response,
                        Callback<MovieDetailResponse> callback);
}
