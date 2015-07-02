package com.kinwae.popularmovies.views.bus;

import com.kinwae.popularmovies.data.Movie;

/**
 * A communication listener between activities and fragments that want to be notified when a movie is
 * selected from any source.
 * An activity that includes with it a fragment that can trigger the movieSelected event should implement
 * this interface.
 *
 * Any other fragment that's going to be contained in an activity that responses to this event who wants
 * to be notified by the activity when the source fragment fires this event should also implement this
 * listener
 * Created by Kembene on 6/24/2015.
 */
public interface OnMovieSelectedListener {
    void onMovieSelected(Movie movie);
}
