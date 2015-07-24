package com.kinwae.popularmovies.views.fragments;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kinwae.popularmovies.MovieDetailActivity;
import com.kinwae.popularmovies.R;
import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.util.Utility;
import com.kinwae.popularmovies.views.adapters.MovieDetailAdapter;

public class MovieDetailFragment extends Fragment {

    public static final String ARG_MOVIE = "movie_detail_movie";

    private Movie mMovie;
    private MovieDetailAdapter mAdapter;
    RecyclerView mRecyclerView;


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
        //mMovie = getActivity().getIntent().getParcelableExtra(MovieDetailActivity.BUNDLE_EXTRA_NAME);
        if (getArguments() != null) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflate = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 1);

        if(mMovie != null){
            mAdapter = new MovieDetailAdapter(mMovie);
        }
        else{
            mAdapter = new MovieDetailAdapter(null);
        }

        mRecyclerView = (RecyclerView)inflate.findViewById(R.id.movie_details_listing_wrapper);
        if(mRecyclerView != null){
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }
        
        return inflate;
    }

}
