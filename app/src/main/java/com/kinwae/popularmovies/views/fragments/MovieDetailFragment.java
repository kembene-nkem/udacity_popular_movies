package com.kinwae.popularmovies.views.fragments;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kinwae.popularmovies.R;
import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.util.ImageSize;
import com.kinwae.popularmovies.util.Utility;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class MovieDetailFragment extends Fragment {

    public static final String ARG_MOVIE = "movie_detail_movie";

    private Movie mMovie;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie Parameter 1.
     * @return A new instance of fragment MovieDetailFragment.
     */
    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment fragment = new MovieDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_MOVIE, movie);
        fragment.setArguments(args);
        return fragment;
    }

    public MovieDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        TextView textView = (TextView)inflate.findViewById(R.id.movie_detail_title);
        TextView ratingView = (TextView)inflate.findViewById(R.id.movie_detail_vote_rate);
        TextView dateView = (TextView)inflate.findViewById(R.id.movie_detail_date);
        TextView plotView = (TextView)inflate.findViewById(R.id.movie_detail_plot);
        ImageView imageView = (ImageView)inflate.findViewById(R.id.movie_detail_poster);
        textView.setText(mMovie.getOriginalTitle());
        loadImageIntoView(imageView, mMovie);
        ratingView.setText(Double.toString(mMovie.getRating()));
        dateView.setText(mMovie.getReleaseDate());
        plotView.setText(mMovie.getPlotSynopsis());

        return inflate;
    }

    private void loadImageIntoView(ImageView imageView, Movie movie){
        RequestCreator picassoCreator = null;
        if(movie.getPosterPath() == null){
            picassoCreator = Picasso.with(getActivity())
                    .load(R.drawable.poster_placeholder);
        }
        else{
            String posterURL = Utility.getPosterURL(ImageSize.DEFAULT, movie.getPosterPath());

            picassoCreator = Picasso.with(getActivity())
                    .load(posterURL);
        }
        picassoCreator
                .placeholder(R.drawable.poster_placeholder)
                .error(R.drawable.poster_placeholder)
                        //.centerCrop()
                .into(imageView);
    }

}
