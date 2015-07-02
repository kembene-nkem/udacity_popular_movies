package com.kinwae.popularmovies.net.reader;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.data.MovieRequestResponse;
import com.kinwae.popularmovies.net.NetworkResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Kembene on 6/25/2015.
 */
public class MovieListDecoder implements ResponseDecoder<MovieRequestResponse> {

    public static final String PAGE_NAME = "page";
    public static final String TOTAL_PAGES_NAME = "total_pages";
    public static final String LIST_NAME = "results";
    public static final String MOVIE_ID = "id";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String TITLE = "title";
    public static final String POSTAL_PATH = "poster_path";
    public static final String BACKDROP_PATH = "backdrop_path";
    public static final String RATING = "vote_average";
    public static final String OVERVIEW = "overview";
    public static final String RELEASE_DATE = "release_date";


    public ArrayList<Movie> extractMovie(JSONArray movieListArray) throws JSONException {

        ArrayList<Movie> movies = new ArrayList<Movie>();
        for(int i=0; i<movieListArray.length(); i++){
            JSONObject movieJson = movieListArray.getJSONObject(i);
            Movie movie = getMovie(movieJson);
            movies.add(movie);
        }
        //System.out.println(movieListArray);

        return movies;
    }

    public MovieRequestResponse getMovieResponse(String movieDetailString)throws JSONException {
        MovieRequestResponse response = new MovieRequestResponse();
        JSONObject jsonObject = new JSONObject(movieDetailString);
        JSONArray movieListArray = jsonObject.getJSONArray(LIST_NAME);
        response.setMovies(extractMovie(movieListArray));
        int page = jsonObject.getInt(PAGE_NAME);
        int pages = jsonObject.getInt(TOTAL_PAGES_NAME);
        response.setNumberOfPages(pages);
        response.setPage(page);
        return response;
    }

    protected Movie getMovie(JSONObject movieObject) throws JSONException {
        Movie movie = new Movie();
        movie.setBackdropPath(getString(BACKDROP_PATH, movieObject, true));
        movie.setId(movieObject.getLong(MOVIE_ID));
        movie.setOriginalTitle(getString(ORIGINAL_TITLE, movieObject, true));
        movie.setPosterPath(getString(POSTAL_PATH, movieObject, true));
        movie.setRating(movieObject.getDouble(RATING));
        movie.setTitle(getString(TITLE, movieObject, true));
        movie.setPlotSynopsis(getString(OVERVIEW, movieObject, true));
        movie.setReleaseDate(getString(RELEASE_DATE, movieObject, true));
        return movie;
    }

    /**
     * This helper method is here because I realised that themoviedb sometime returns the json
     * null for some of the movie properties like movie poster. This helper standardised such occurencies to
     * the Java null
     * @param propName
     * @param jsonObject
     * @param nullable
     * @return
     * @throws JSONException
     */
    protected String getString(String propName, JSONObject jsonObject, boolean nullable) throws JSONException {
        String item = jsonObject.getString(propName);
        if(nullable && ("null".equals(item))){
            item = null;
        }
        return item;
    }

    @Override
    public MovieRequestResponse decodeResponse(NetworkResponse response) {
        //this is the response text
        MovieRequestResponse movieResponse = new MovieRequestResponse();
        try {
            return getMovieResponse(response.getResponseBody());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieResponse;
    }
}
