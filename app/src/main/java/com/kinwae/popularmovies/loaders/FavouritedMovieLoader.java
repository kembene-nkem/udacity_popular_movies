package com.kinwae.popularmovies.loaders;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.CursorLoader;
import android.support.v4.widget.CursorAdapter;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.loaders.delegates.CursorLoaderDelegate;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieColumns;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieSelection;
import com.kinwae.popularmovies.util.Utility;

import java.util.List;

/**
 * Created by Kembene on 7/26/2015.
 */
public class FavouritedMovieLoader extends AsyncTaskLoader<MovieLoaderDataProvider> {

    /**
     * the condition that will be used to load movie details from the database
     */
    DbMovieSelection uriSelection;
    /**
     * Because we need this loader to return a MovieLoaderDataProvider instance, we can't directly
     * use the CursorLoader so we adapt our implementation to the CursorLoader, delegating all query
     * and necessary house keeping responsibility to this cursorloader
     */
    CursorLoader cursorLoaderDelegate;
    MovieLoaderDataProvider mDataProvider;
    ContentObserver mChangeObserver;
    List<Movie> mSavedMovies;

    public FavouritedMovieLoader(Context context, DbMovieSelection selection, List<Movie> savedMovies) {
        super(context);
        this.uriSelection = selection;
        cursorLoaderDelegate = new CursorLoader(context);
        // for some reason exceptions are thrown if this ContentObserver was created in the
        // intended place (i.e CursorLoaderDelegate). So we create it here and give it to the
        // CursorLoaderDelegate so it can unregister it from its list of listeners when we are
        // freeing resources (i.e closing the associated Cursor)
        mChangeObserver = new ChangeObserver();
        this.mSavedMovies = savedMovies;
    }

    @Override
    public MovieLoaderDataProvider loadInBackground() {
        String selection = uriSelection.sel();
        String[] args = uriSelection.args();
        String order = uriSelection.order();
        // set respective info for our adapted cursorloader
        cursorLoaderDelegate.setUri(DbMovieColumns.CONTENT_URI);
        cursorLoaderDelegate.setSelection(selection);
        cursorLoaderDelegate.setSelectionArgs(args);
        cursorLoaderDelegate.setSortOrder(order);

        Cursor cursor = cursorLoaderDelegate.loadInBackground();
        cursor.registerContentObserver(mChangeObserver);
        CursorLoaderDelegate loaderDelegate = new CursorLoaderDelegate(cursor, mChangeObserver);
        if(mSavedMovies != null){
            loaderDelegate.setLoadedMovieList(mSavedMovies);
            mSavedMovies = null;
        }
        return new MovieLoaderDataProvider(loaderDelegate);

    }

    @Override
    public void deliverResult(MovieLoaderDataProvider data) {
        CursorLoaderDelegate dataDelegate = (CursorLoaderDelegate) data.getDataDelegate();
        if (isReset()) {
            // An async query came in while the loader is stopped
            if (dataDelegate != null) {
                dataDelegate.close();
            }
            return;
        }


        if (isStarted()) {
            super.deliverResult(data);
        }
        // release resources from a previously loaded cursorloader via our MovieLoaderDataProvider
        MovieLoaderDataProvider oldDataProvider = mDataProvider;
        if (oldDataProvider != null && oldDataProvider != data) {
            CursorLoaderDelegate oldDataDelegate = (CursorLoaderDelegate) oldDataProvider.getDataDelegate();
            if(!oldDataDelegate.isClosed())
                oldDataDelegate.close();
        }
    }

    @Override
    protected void onStartLoading() {
        if (this.mDataProvider != null) {
            deliverResult(this.mDataProvider);
        }

        if (takeContentChanged() || this.cursorLoaderDelegate.takeContentChanged() || this.mDataProvider == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        // Attempt to cancel the current load task if possible.
        cancelLoad();
    }

    @Override
    public void onCanceled(MovieLoaderDataProvider dataProvider) {
        //release necessary resources
        if (dataProvider != null) {
            CursorLoaderDelegate dataDelegate = (CursorLoaderDelegate) dataProvider.getDataDelegate();
            if(!dataDelegate.isClosed()){
                dataDelegate.close();
            }
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        // Ensure the loader is stopped
        onStopLoading();

        if (this.mDataProvider != null ) {
            CursorLoaderDelegate dataDelegate = (CursorLoaderDelegate) mDataProvider.getDataDelegate();
            if(!dataDelegate.isClosed()){
                dataDelegate.close();
            }
        }
        this.mDataProvider = null;
    }

    private class ChangeObserver extends ContentObserver {
        public ChangeObserver() {
            super(new Handler());
        }

        @Override
        public boolean deliverSelfNotifications() {
            return true;
        }

        @Override
        public void onChange(boolean selfChange) {
            onContentChanged();
        }
    }
}
