package com.kinwae.popularmovies.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kinwae.popularmovies.R;
import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.events.MovieDetailLoadedEvent;
import com.kinwae.popularmovies.events.MovieFavoritedEvent;
import com.kinwae.popularmovies.util.ImageSize;
import com.kinwae.popularmovies.util.Utility;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Kembene on 6/24/2015.
 */
public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder>{

    private List<Movie> mMovieList;
    private Map<Long, Integer> movieIndexes = new HashMap<>();

    private static final String LOG_TAG = MovieListAdapter.class.getName();

    public MovieListAdapter() {
        Utility.getSharedEventBus().register(this);
    }

    public MovieListAdapter(List<Movie> movieList) {
        this.mMovieList = movieList;
        Utility.getSharedEventBus().register(this);
    }

    public void updateMovies(List<Movie> movies){
        List<Movie> oldSet = this.mMovieList;
        if(movies != this.mMovieList){
            this.mMovieList = movies;
            this.notifyDataSetChanged();
        }
    }

    public List<Movie> getMovies(){
        return this.mMovieList;
    }

    public Movie getMovieAtPosition(int position){
        if(this.mMovieList != null && position < this.mMovieList.size()){
            return mMovieList.get(position);
        }
        return null;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();

        View cardHolderView = LayoutInflater.from(context)
                .inflate(R.layout.movie_item_card, parent, false);

        return new ViewHolder(cardHolderView, context);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mMovieList != null && position < mMovieList.size()){
            Movie movie = mMovieList.get(position);
            movieIndexes.put(movie.getId(), position);
            RequestCreator picassoCreator = null;
            if(movie.getPosterPath() == null){
                Log.v(LOG_TAG, "No poster for movie: " + movie.getTitle());
                picassoCreator = Picasso.with(holder.context)
                        .load(R.drawable.poster_placeholder);
            }
            else{
                String posterURL = Utility.getPosterURL(ImageSize.DEFAULT, movie.getPosterPath());
                Log.v(LOG_TAG, "Fetching poster image from: " + posterURL + " for movie: " + movie.getTitle());
                picassoCreator = Picasso.with(holder.context)
                        .load(posterURL);
            }
            picassoCreator
                    .placeholder(R.drawable.poster_placeholder)
                    .error(R.drawable.poster_placeholder).fit()
                    //.centerCrop()
                    .into(holder.mPosterView);
            holder.mPosterView.setContentDescription(movie.getTitle());
            holder.titleView.setText(movie.getTitle());
        }

    }

    @Override
    public int getItemCount() {
        return mMovieList != null ? mMovieList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView mPosterView;
        private TextView titleView;
        /**
         * Storing the context as a member of the view holder because picasso needs a reference to
         * the context, can't find a better way to handle this
         */
        private Context context;
        public ViewHolder(View itemView, Context context) {
            super(itemView);
            this.context = context;
            mPosterView = (ImageView) itemView.findViewById(R.id.movie_poster);
            titleView = (TextView)itemView.findViewById(R.id.movie_poster_title);

        }
    }

    @Subscribe
    public void movieWasFavoritedListener(MovieFavoritedEvent event){
        Movie movie = event.getMovie();
        if(movie != null){
            Integer mIndex = movieIndexes.get(movie.getId());
            if(mIndex != null){
                Movie movieAtPosition = getMovieAtPosition(mIndex);
                movieAtPosition.setFavorited(movie.isFavorited());
            }
        }
    }

    @Subscribe
    public void movieDetailLoadedEventListener(MovieDetailLoadedEvent event){
        Movie movie = event.getMovie();
        if(movie != null){
            Integer mIndex = movieIndexes.get(movie.getId());
            if(mIndex != null){
                Movie movieAtPosition = getMovieAtPosition(mIndex);
                if(movieAtPosition != null){
                    movieAtPosition.setTrailers(movie.getTrailers());
                    movieAtPosition.setReviews(movie.getReviews());
                        movieAtPosition.setLoadedExtra(movie.isLoadedExtra());
                }
            }
        }
    }
}
