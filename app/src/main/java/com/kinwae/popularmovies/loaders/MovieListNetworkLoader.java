package com.kinwae.popularmovies.loaders;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.data.MovieRequestResponse;
import com.kinwae.popularmovies.events.MovieListRefreshRequiredEvent;
import com.kinwae.popularmovies.loaders.delegates.NetworkLoaderDelegate;
import com.kinwae.popularmovies.net.NetworkRequest;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieColumns;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieCursor;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieSelection;
import com.kinwae.popularmovies.util.Utility;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit.RetrofitError;

/**
 * Loaders loads movies that are displayed in a fragment and return to the fragment over the network
 * Created by Kembene on 6/27/2015.
 */
public class MovieListNetworkLoader extends AsyncTaskLoader<MovieLoaderDataProvider>{

    UpdateListObserver mObserver;
    int mPagerPosition;
    MovieLoaderDataProvider mMovieProvider;
    List<Movie> mMovieList;
    private String LOGGER = MovieListNetworkLoader.class.getName();
    private static int mTotalPages = 1;

    public MovieListNetworkLoader(Context context) {
        this(context, 1, null);
    }

    public MovieListNetworkLoader(Context context, int pageNumber, List<Movie> movieList) {
        super(context);
        this.mPagerPosition = pageNumber;
        this.mMovieList = movieList;
    }


    @Override
    public MovieLoaderDataProvider loadInBackground() {
        NetworkLoaderDelegate delegate = new NetworkLoaderDelegate();
        MovieLoaderDataProvider movieProvider = new MovieLoaderDataProvider(delegate,
                MovieLoaderDataProvider.LOADER_TYPE_NETWORK);
        List<Movie> movies = null;

        if(mMovieList == null){

            String sortOrder = Utility.getPreferredMovieSortOrder(getContext());
            movies = loadMoviesFromNetwork(mPagerPosition, sortOrder);

            //keep a map of movie_ids to index position
            HashMap<Long, Integer> movieIdMap = new HashMap<>();
            String[] movieIds = new String[movies.size()];
            for(int i=0; i < movies.size(); i++){
                Movie movie = movies.get(i);
                movieIds[i] = Long.toString(movie.getId());
                movieIdMap.put(movie.getId(), i);
            }

            if(movies.size() > 0){
                DbMovieSelection where = new DbMovieSelection();
                where.movieIdContains(movieIds);
                ContentResolver contentResolver = getContext().getContentResolver();
                String sel = where.sel();
                Cursor cursor = contentResolver.query(DbMovieColumns.CONTENT_URI, new String[]{"movie_id"}, sel, where.args(), null);
                DbMovieCursor movieCursor = new DbMovieCursor(cursor);
                while (movieCursor.moveToNext()){
                    Long movieId = Long.valueOf(movieCursor.getMovieId());
                    Integer index = movieIdMap.get(movieId);
                    if(index != null){
                        movies.get(index).setFavorited(true);
                    }
                }
                movieCursor.close();
            }
        }
        else{
            movies = new ArrayList<>(mMovieList);
            mMovieList = null;
        }
        int pageSize = Utility.getTotalMoviePageFromNetwork();
        delegate.setPageCount(pageSize);
        delegate.setMovieList(movies);

        return movieProvider;
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override public void deliverResult(MovieLoaderDataProvider movies) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (movies != null) {
                onReleaseResources(movies);
            }
            return;
        }

        MovieLoaderDataProvider oldMovies = this.mMovieProvider;
        this.mMovieProvider = movies;

        if (isStarted()) {
            // If the Loader is currently started, we can immediately
            // deliver its results.
            super.deliverResult(movies);
        }

        // At this point we can release the resources associated with
        // 'oldApps' if needed; now that the new result is delivered we
        // know that it is no longer in use.
        if (oldMovies != null) {
            onReleaseResources(oldMovies);
        }
    }

    /**
     * Starts an asynchronous load of the contacts list data. When the result is ready the callbacks
     * will be called on the UI thread. If a previous load has been completed and is still valid
     * the result may be passed to the callbacks immediately.
     *
     * Must be called from the UI thread
     */
    @Override
    protected void onStartLoading() {
        //if a movielist was initially sent in, deliver the result straight to the client
        if (this.mMovieProvider != null) {
            deliverResult(this.mMovieProvider);
        }

        // start watching for changes made on shared preferences
        if(mObserver == null){
            mObserver = new UpdateListObserver();
            mObserver.startObserving();
        }

        // if movielist is null or if content has changed (in our case, setting preference has changed)
        if (takeContentChanged() || this.mMovieProvider == null) {
            forceLoad();
        }
    }

    /**
     * Must be called from the UI thread
     */
    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(MovieLoaderDataProvider movieList) {
        super.onCanceled(movieList);

        // At this point we can release the resources associated with 'apps'
        // if needed.
        onReleaseResources(movieList);
    }

    @Override
    protected void onReset() {
        // Ensure the loader has been stopped.
        onStopLoading();

        // At this point we can release the resources associated with 'mData'.
        if (this.mMovieProvider != null) {
            onReleaseResources(this.mMovieProvider);
            this.mMovieProvider = null;
        }

        // The Loader is being reset, so we should stop monitoring for changes.
        if (mObserver != null) {
            mObserver.stopObserving();
            mObserver = null;
        }
    }

    /**
     * Helper function to take care of releasing resources associated
     * with an actively loaded data set.
     */
    protected void onReleaseResources(MovieLoaderDataProvider movies) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
    }

    private List<Movie> loadMoviesFromNetwork(int pageNumber, String sortOrder){
        int page = pageNumber + 1;
        try{
            //We don't have a cache of this response, so lets request it
            MovieRequestResponse decodeResponse = NetworkRequest.getRestService().getMovies(sortOrder, page);
            if(Utility.getTotalMoviePageFromNetwork() == 1){
                Utility.setTotalMoviePageFromNetwork(decodeResponse.getTotalPages());
            }
            return decodeResponse.getMovies();
        }
        catch(RetrofitError ex){
            Log.v(LOGGER, ex.getMessage());
        }
        return new ArrayList<>();
    }

    class UpdateListObserver{

        @Subscribe
        public void movieListRequiredEvent(MovieListRefreshRequiredEvent event){
            onContentChanged();
        }

        void startObserving(){
            Utility.getSharedEventBus().register(this);
        }

        void stopObserving(){
            Utility.getSharedEventBus().unregister(this);
        }
    }

}