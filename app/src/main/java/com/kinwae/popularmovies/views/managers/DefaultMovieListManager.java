package com.kinwae.popularmovies.views.managers;

import android.content.Context;
import android.database.DataSetObserver;

import com.kinwae.popularmovies.events.MovieListRefreshRequiredEvent;
import com.kinwae.popularmovies.events.MovieLoaderLoadCompleteEvent;
import com.kinwae.popularmovies.loaders.MovieLoaderDataProvider;
import com.kinwae.popularmovies.util.Utility;
import com.kinwae.popularmovies.views.adapters.MoviePagerAdapter;
import com.squareup.otto.Subscribe;

/**
 * Created by Kembene on 6/27/2015.
 */
public class DefaultMovieListManager{
    private MoviePagerAdapter mPagerAdapter;
    private boolean notifyLoaders = false;
    private int lastLoadType;

    //an observer that will listen for dataset changes on the PagerAdapter
    private DataSetObserver mPagerObserver = new DataSetObserver() {

        @Override
        public void onChanged() {
            if(notifyLoaders)
                return;
            //notify all Loaders that a configuration change has been made thus they need to
            // reload their view
            Utility.getSharedEventBus().post(new MovieListRefreshRequiredEvent());
        }

        @Override
        public void onInvalidated() {

        }
    };

    public DefaultMovieListManager(Context context, MoviePagerAdapter pagerAdapter) {
        this.mPagerAdapter = pagerAdapter;
        this.mPagerAdapter.registerDataSetObserver(mPagerObserver);
        Utility.getSharedEventBus().register(this);
    }

    public void swapPagerAdapter(MoviePagerAdapter adapter){
        MoviePagerAdapter oldAdapter = this.mPagerAdapter;
        this.mPagerAdapter = adapter;
        if(oldAdapter != null){
            oldAdapter.unregisterDataSetObserver(mPagerObserver);
        }
        this.mPagerAdapter.registerDataSetObserver(mPagerObserver);
    }

    /**
     * This loader has successfully loaded the data required
     */
    @Subscribe
    public void loaderLoadComplete(MovieLoaderLoadCompleteEvent event) {

        synchronized (this){
            MovieLoaderDataProvider dataProvider = event.getDataProvider();

            if(this.lastLoadType != dataProvider.getLoaderType() || mPagerAdapter.getCount() != dataProvider.getDataDelegate().getPageCount()){
                mPagerAdapter.setPageCount(dataProvider.getDataDelegate().getPageCount());
                this.notifyLoaders = true;
                mPagerAdapter.notifyDataSetChanged();
                this.notifyLoaders = false;
            }
            this.lastLoadType = dataProvider.getLoaderType();
        }

    }

    public int getLastLoadType() {
        return lastLoadType;
    }

    public DefaultMovieListManager setLastLoadType(int lastLoadType) {
        this.lastLoadType = lastLoadType;
        return this;
    }


    public interface MovieListManagerHolder{
        DefaultMovieListManager getMovieListManager();
    }
}