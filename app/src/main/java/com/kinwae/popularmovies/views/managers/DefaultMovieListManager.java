package com.kinwae.popularmovies.views.managers;

import android.content.Context;
import android.database.DataSetObservable;
import android.database.DataSetObserver;

import com.kinwae.popularmovies.loaders.MovieListLoader;
import com.kinwae.popularmovies.views.adapters.MovieListPagerAdapter;
import com.kinwae.popularmovies.views.adapters.MoviePaginator;
import com.kinwae.popularmovies.views.bus.MovieListManager;
import com.kinwae.popularmovies.views.bus.MovieListManagerAware;
import com.kinwae.popularmovies.views.bus.MoviePaginatorLoadMonitor;

/**
 * Created by Kembene on 6/27/2015.
 */
public class DefaultMovieListManager implements MoviePaginatorLoadMonitor, MovieListManager {
    private MovieListPagerAdapter mPagerAdapter;
    private MoviePaginator moviePaginator;
    private boolean pageCountUpdated = false;
    private MoviePagerAndLoaderBridgeObservable mDatasetObservable;
    private boolean mNotifyTriggeredByManager = false;

    //an observer that will listen for dataset changes on the PagerAdapter
    private DataSetObserver mPagerObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            if(mNotifyTriggeredByManager)
                return;
            //User preference has changed thats why we got notified. Before we notify all
            // registered loaders, lets have the paginator clear it cache
            moviePaginator.resetCache();
            //notify all Loaders that a configuration change has been made thus they need to
            // reload their view
            mDatasetObservable.notifyChanged();
        }

        @Override
        public void onInvalidated() {
            mDatasetObservable.notifyInvalidated();
        }
    };

    public DefaultMovieListManager(Context context, MoviePaginator paginator, MovieListPagerAdapter pagerAdapter) {
        this.moviePaginator = paginator;
        this.mPagerAdapter = pagerAdapter;
        this.mDatasetObservable = new MoviePagerAndLoaderBridgeObservable();
        this.mPagerAdapter.registerDataSetObserver(mPagerObserver);
    }

    public void cleanUp(){
        this.mPagerAdapter.unregisterDataSetObserver(mPagerObserver);
    }

    public void registerChangeObserver(DataSetObserver observer){
        mDatasetObservable.registerObserver(observer);
    }

    public void unregisterChangeObserver(DataSetObserver observer){
        mDatasetObservable.unregisterObserver(observer);
    }

    public MoviePaginator getMoviePaginator() {
        return moviePaginator;
    }

    public MovieListPagerAdapter getPagerAdapter() {
        return mPagerAdapter;
    }

    @Override
    /**
     * This loader has successfully loaded the data from the Paginator we gave to it
     */
    public void paginatorLoadComplete(MovieListLoader loader) {
        // We do not know before hand now many pages there are to be loaded, not until an initial
        // request is made to themoviedb. So when the first loader returns we want change the number
        // of pages reported by the PagerAdapter and trigger a datasetChanged event.
        // This dataset change event will be picked up by our own Observer (#mPagerObserver) who
        // by default notifies all loaders about the changes so they can reload themselves.
        // but since Loaders are only meant to be notifies only when preference settings changes
        // (MainActivity#onStart) to prevent this particular notificationChange from hitting the
        // loaders again, we set a #mNotifyTriggeredByManager flag. If that flag is true, we do not
        // notify the registered Loaders of the changes that has just occured
        synchronized (this.moviePaginator){
            if(!pageCountUpdated){
                mPagerAdapter.setPageCount(moviePaginator.getNumberOfPages());
                this.mNotifyTriggeredByManager = true;
                mPagerAdapter.notifyDataSetChanged();
                this.mNotifyTriggeredByManager = false;
                pageCountUpdated = true;
            }
        }

    }

    public DefaultMovieListManager setPageCountUpdated(boolean pageCountUpdated) {
        this.pageCountUpdated = pageCountUpdated;
        return this;
    }

    @Override
    /**
     * Ok! Our fragment has just created its loader, it wants me to attach to it the paginator it will
     * be using to get data
     */
    public void bindLoader(MovieListLoader loader) {
        loader.setMoviePaginator(moviePaginator);
        if(loader instanceof MovieListManagerAware){
            ((MovieListManagerAware)loader).setMovieListManager(this);
        }
    }


    public interface MovieListManagerHolder{
        DefaultMovieListManager getMovieListManager();
    }

    public static class MoviePagerAndLoaderBridgeObservable extends DataSetObservable{}

    public static abstract class PagerAndLoaderBridgeObserver extends DataSetObserver{
        private MovieListManager listManager;

        public PagerAndLoaderBridgeObserver(MovieListManager listManager) {
            this.listManager = listManager;
        }

        public void startObserving(){
            if(this.listManager != null)
                this.listManager.registerChangeObserver(this);
        }

        public void stopObserving(){
            if(this.listManager != null)
                this.listManager.unregisterChangeObserver(this);
        }
    }

}