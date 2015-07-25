package com.kinwae.popularmovies.views.managers;

import android.content.Context;
import android.database.DataSetObserver;

import com.kinwae.popularmovies.events.MovieListRefreshRequiredEvent;
import com.kinwae.popularmovies.loaders.MovieListNetworkLoader;
import com.kinwae.popularmovies.util.Utility;
import com.kinwae.popularmovies.views.adapters.MoviePagerAdapter;
import com.kinwae.popularmovies.views.adapters.MoviePaginator;
import com.kinwae.popularmovies.views.bus.MoviePaginatorLoadMonitor;

/**
 * Created by Kembene on 6/27/2015.
 */
public class DefaultMovieListManager implements MoviePaginatorLoadMonitor {
    private MoviePagerAdapter mPagerAdapter;
    private MoviePaginator moviePaginator;
    private boolean pageCountUpdated = false;
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
            Utility.getSharedEventBus().post(new MovieListRefreshRequiredEvent());
        }

        @Override
        public void onInvalidated() {
            //mDatasetObservable.notifyInvalidated();
        }
    };

    public DefaultMovieListManager(Context context, MoviePaginator paginator, MoviePagerAdapter pagerAdapter) {
        this.moviePaginator = paginator;
        this.mPagerAdapter = pagerAdapter;
        this.mPagerAdapter.registerDataSetObserver(mPagerObserver);
    }

    public void swapAdapter(MoviePagerAdapter adapter){
        if(this.mPagerAdapter != null)
            this.mPagerAdapter.unregisterDataSetObserver(mPagerObserver);
        this.mPagerAdapter = adapter;
        if(this.mPagerAdapter != null){
            this.mPagerAdapter.registerDataSetObserver(mPagerObserver);
        }
    }

    @Override
    /**
     * This loader has successfully loaded the data from the Paginator we gave to it
     */
    public void paginatorLoadComplete(MovieListNetworkLoader loader) {
        // We do not know before hand now many pages there are to be loaded, not until an initial
        // request is made to themoviedb. So when the first loader returns we want change the number
        // of pages reported by the PagerAdapter and trigger a datasetChanged event.
        // This dataset change event will be picked up by our own Observer (#mPagerObserver) who
        // by default notifies all loaders about the changes so they can reload themselves.
        // but since Loaders are only meant to be notifies only when preference settings changes
        // (MainActivity#onStart) to prevent this particular notificationChange from hitting the
        // loaders again, we set a #mNotifyTriggeredByManager flag. If that flag is true, we do not
        // notify the registered Loaders of the changes that has just occured
        synchronized (this){
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
    public void bindLoader(MovieListNetworkLoader loader) {
        loader.setMoviePaginator(moviePaginator);
    }


    public interface MovieListManagerHolder{
        DefaultMovieListManager getMovieListManager();
    }

}