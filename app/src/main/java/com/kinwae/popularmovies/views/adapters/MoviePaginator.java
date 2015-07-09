package com.kinwae.popularmovies.views.adapters;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.data.MovieRequestResponse;
import com.kinwae.popularmovies.services.MovieService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.converter.GsonConverter;

/**
 * A pagenator is responsible for doing the actual loading of movies for a particular page. It either makes
 * network calls or returns a cached record set. Paginators caches result for at most five(5) pages
 * Created by Kembene on 6/27/2015.
 */
public class MoviePaginator {

    public static final String API_KEY = "54d33209a6e7e146aad6b7ce16875a32";

    public static final String BASE_URL = "http://api.themoviedb.org/3";

    private String LOGGER = MoviePaginator.class.getName();

    private int numberOfPages = 1;
    //don't know if we can set the number of items to retrieve from themoviedb, but by default,
    // they return 20 items
    private int itemsPerPage = 20;


    private int mMaxCacheSize = 5;

    private ArrayList<MovieRequestResponse> mCache = new ArrayList<>();

    private RequestInterceptor mRequestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestInterceptor.RequestFacade request) {
            request.addQueryParam("api_key", API_KEY);
            request.addHeader("User-Agent", "Retrofit-Sample-App");
        }
    };

    private MovieService mMovieService;

    public MoviePaginator() {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .setDateFormat(Movie.DATE_FORMAT)
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .setRequestInterceptor(mRequestInterceptor)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();
        mMovieService = restAdapter.create(MovieService.class);

    }

    /**
     * this is an expensive task so ensure its always called from a background thread and
     * not the main UI thread
     * @param pageNumber
     * @return
     */
    public List<Movie> getMovies(int pageNumber, String sortOrder){
        int page = pageNumber + 1;
        MovieRequestResponse decodeResponse = null;
        for(MovieRequestResponse response : this.mCache){
            if(response.getPage() == page){
                decodeResponse = response;
                break;
            }
        }
        if(decodeResponse == null){
            //We don't have a cache of this response, so lets request it
            /*Uri.Builder uriBuilder = NetworkRequest.RequestBuilder.discoveryUri(sortOrder);
            // page number is zero based, but themoviedb is 1 based
            uriBuilder.appendQueryParameter("page", Integer.toString(page));
            Uri uri = uriBuilder.build();
            NetworkRequest networkRequest = NetworkRequest.RequestBuilder.sharedInstance();
            NetworkResponse movieResponse = networkRequest.<List<Movie>>execute(uri);
            if(movieResponse.isSuccessful()){
                decodeResponse = movieListDecoder.decodeResponse(movieResponse);
                this.numberOfPages = decodeResponse.getNumberOfPages();
            }*/
            try{
                decodeResponse = mMovieService.getMovies(sortOrder, page);
            }
            catch(RetrofitError ex){
                Log.v(LOGGER, ex.getMessage());
            }

            if(decodeResponse != null){
                //now we have a response, add this to the cache
                this.mCache.add(decodeResponse);
                if(this.mCache.size() > mMaxCacheSize){
                    this.mCache.remove(0);
                }
            }
        }
        if(decodeResponse != null){
            return decodeResponse.getMovies();
        }
        // Don't knoe of a better solution yet, but we are returning an empty arraylist here
        // because of the way Loaders work. If we always
        // return null, the LoaderInfo#onLoadComplete method used by our Loader (see LoaderManager)
        // will not recall our Fragment's #onLoadFinished method if the previously loaded value
        // equals the newly loaded value (null == null), but we need #onLoadFinished to always be
        // called even if network could not be reached so we can change our graphics appropriately
        return new ArrayList<>();
    }

    public void resetCache(){
        this.mCache.clear();
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }
}
