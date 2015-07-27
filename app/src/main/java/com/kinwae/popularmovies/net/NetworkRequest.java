package com.kinwae.popularmovies.net;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kinwae.popularmovies.services.MovieService;
import com.squareup.okhttp.OkHttpClient;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.Client;
import retrofit.client.OkClient;
import retrofit.converter.GsonConverter;


/**
 * Created by Kembene on 6/24/2015.
 */
public class NetworkRequest {

    public static final String API_KEY = "54d33209a6e7e146aad6b7ce16875a32";
    public static final String BASE_URL = "http://api.themoviedb.org/3/";

    private static final String LOG_TAG = NetworkRequest.class.getName();
    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();


    private static MovieService movieService;

    static {
        //HTTP_CLIENT.networkInterceptors().add(new StethoInterceptor());
    }

    private static RequestInterceptor requestInterceptor = new RequestInterceptor() {
        @Override
        public void intercept(RequestInterceptor.RequestFacade request) {
            request.addQueryParam("api_key", API_KEY);
            request.addQueryParam("language", "en");
            request.addHeader("User-Agent", "Retrofit-Sample-App");
        }
    };

    public static MovieService getRestService(){
        if (movieService == null){
            movieService = createMovieService();
        }
        return movieService;
    }

    private static MovieService createMovieService(){
        Gson gson = new GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
            .setEndpoint(BASE_URL)
            .setConverter(new GsonConverter(gson))
            .setRequestInterceptor(requestInterceptor)
            .setLogLevel(RestAdapter.LogLevel.FULL)
            .setClient(getClient())
            .build();
        return restAdapter.create(MovieService.class);
    }

    private static Client getClient(){
        //return new NetMock();
        return new OkClient(HTTP_CLIENT);
    }

}
