package com.kinwae.popularmovies.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.events.MovieListRefreshRequiredEvent;
import com.kinwae.popularmovies.util.Utility;
import com.kinwae.popularmovies.views.adapters.MoviePaginator;
import com.kinwae.popularmovies.views.bus.MovieListManager;
import com.squareup.otto.Subscribe;

import java.util.List;

/**
 * Loaders loads movies that are displayed in a fragment and return to the fragment. But because
 * Loaders are fragment dependant and we are paginating our list of movies, we have to delegate
 * the loading of the movies to another component (a MoviePaginator). The movie paginator is responsible
 * for actually finding the list of movies for a particular page. It goes further to caching the last five
 * pages so as to reduce the amount of network calls that will be made.
 *
 * Fragments might not have access to the MainActivities MoviePaginator (which lives through out the
 * lifespan of the activity itself) so they get associated with a paginator via the DefaultMovieListManager
 * Created by Kembene on 6/27/2015.
 */
public class MovieListNetworkLoader extends AsyncTaskLoader<List<Movie>>{

    List<Movie> mMovieList;
    UpdateListObserver mObserver;
    int mPagerPosition;
    MoviePaginator mMoviePaginator;
    MovieListManager mMovieListManager;


    public MovieListNetworkLoader(Context context, MoviePaginator mMoviePaginator) {
        this(context, 1, mMoviePaginator);
    }

    public MovieListNetworkLoader(Context context, int pageNumber, MoviePaginator mMoviePaginator) {
        this(context, pageNumber, mMoviePaginator, null);
    }

    public MovieListNetworkLoader(Context context, int page, MoviePaginator mMoviePaginator, List<Movie> movies) {
        super(context);
        this.mMovieList = movies;
        this.mPagerPosition = page;
        this.mMoviePaginator = mMoviePaginator;
    }

    public void setMovieListManager(MovieListManager movieListManager){
        this.mMovieListManager = movieListManager;
    }

    public MovieListNetworkLoader setMoviePaginator(MoviePaginator moviePaginator) {
        this.mMoviePaginator = moviePaginator;
        return this;
    }

    @Override
    public List<Movie> loadInBackground() {
        /**
         * we delegate the loading of movies for the specified page to the MoviePaginator who
         * either makes a network call or returns a cached copy
         */
        String sortOrder = Utility.getPreferredMovieSortOrder(getContext());
        return mMoviePaginator.getMovies(mPagerPosition, sortOrder);
    }

    /**
     * Called when there is new data to deliver to the client.  The
     * super class will take care of delivering it; the implementation
     * here just adds a little more logic.
     */
    @Override public void deliverResult(List<Movie> movies) {
        if (isReset()) {
            // An async query came in while the loader is stopped.  We
            // don't need the result.
            if (movies != null) {
                onReleaseResources(movies);
            }
            return;
        }

        List<Movie> oldMovies = movies;
        mMovieList = movies;

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
        if (mMovieList != null) {
            deliverResult(mMovieList);
        }

        // start watching for changes made on shared preferences
        if(mObserver == null){
            mObserver = new UpdateListObserver();
            mObserver.startObserving();
        }

        // if movielist is null or if content has changed (in our case, setting preference has changed)
        if (takeContentChanged() || mMovieList == null) {
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
    public void onCanceled(List<Movie> movieList) {
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
        if (mMovieList != null) {
            onReleaseResources(mMovieList);
            mMovieList = null;
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
    protected void onReleaseResources(List<Movie> movies) {
        // For a simple List<> there is nothing to do.  For something
        // like a Cursor, we would close it here.
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