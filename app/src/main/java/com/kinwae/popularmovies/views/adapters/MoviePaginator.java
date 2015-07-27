package com.kinwae.popularmovies.views.adapters;

import android.util.Log;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.data.MovieRequestResponse;
import com.kinwae.popularmovies.net.NetworkRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit.RetrofitError;

/**
 * A paginator is responsible for doing the actual loading of movies for a particular page. It either makes
 * network calls or returns a cached record set. Paginators caches result for at most five(5) pages
 * Created by Kembene on 6/27/2015.
 */
public class MoviePaginator {

    private String LOGGER = MoviePaginator.class.getName();

    private int numberOfPages = 1;
    //don't know if we can set the number of items to retrieve from themoviedb, but by default,
    // they return 20 items
    private int itemsPerPage = 20;


    private int mMaxCacheSize = 5;

    private ArrayList<MovieRequestResponse> mCache = new ArrayList<>();

    public MoviePaginator() {
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
            try{
                //We don't have a cache of this response, so lets request it
                decodeResponse = NetworkRequest.getRestService().getMovies(sortOrder, page);
                if(this.numberOfPages == 1){
                    this.numberOfPages = decodeResponse.getTotalPages();
                }
            }
            catch(RetrofitError ex){
                Log.v(LOGGER, ex.getMessage());
                ex.printStackTrace();
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


}