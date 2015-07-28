package com.kinwae.popularmovies.loaders.delegates;

import android.database.ContentObserver;
import android.database.Cursor;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieCursor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kembene on 7/26/2015.
 */
public class CursorLoaderDelegate implements MovieLoaderDataDelegate  {
    private DbMovieCursor mMovieCursor;
    private ContentObserver mContentObserver;
    private Map<Integer, Movie> loadedMovies;

    public CursorLoaderDelegate(Cursor cursor, ContentObserver contentObserver) {
        mMovieCursor = new DbMovieCursor(cursor);
        mContentObserver = contentObserver;
        loadedMovies = new HashMap<>();
    }

    @Override
    public int getPageCount() {
        return 1;
    }

    @Override
    public int getNumberOfItems() {
        if(mMovieCursor != null){
            return mMovieCursor.getCount();
        }
        return 0;
    }

    @Override
    public Movie getMovieAt(int position) {
        if(loadedMovies.containsKey(position)){
            return loadedMovies.get(position);
        }
        if(mMovieCursor != null){
            mMovieCursor.moveToPosition(position);
            Movie movie = mMovieCursor.getMovie();
            loadedMovies.put(position, movie);
            return movie;
        }
        return null;
    }

    public boolean isClosed(){
        return mMovieCursor.isClosed();
    }

    public void close(){
        //ensure we remove registered listener first before closing
        this.mMovieCursor.unregisterContentObserver(mContentObserver);
        mContentObserver = null;
        this.mMovieCursor.close();
        loadedMovies.clear();
        loadedMovies = null;
    }

    public List<Movie> getLoadedMovieList() {
        return new ArrayList<>(loadedMovies.values());
    }

    public void setLoadedMovieList(List<Movie> movies) {
        loadedMovies = new HashMap<>();
        if(movies != null){
            for(int i=0; i<movies.size(); i++){
                Movie movie = movies.get(i);
                loadedMovies.put(i, movie);
            }
        }
    }


}
