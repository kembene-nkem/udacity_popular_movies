package com.kinwae.popularmovies.views.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kinwae.popularmovies.MovieDetailActivity;
import com.kinwae.popularmovies.R;
import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.data.Trailer;
import com.kinwae.popularmovies.util.Utility;
import com.kinwae.popularmovies.views.adapters.MovieDetailAdapter;

import java.util.ArrayList;

public class MovieDetailFragment extends Fragment {

    public static final String ARG_MOVIE = "movie_detail_movie";

    private Movie mMovie;
    private MovieDetailAdapter mAdapter;
    RecyclerView mRecyclerView;
    private ImageButton mPlayButton;
    private ShareActionProvider mShareActionProvider;



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
        setHasOptionsMenu(true);
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
            mAdapter = new MovieDetailAdapter(mMovie, getActivity());
        }
        else{
            mAdapter = new MovieDetailAdapter(null, null);
        }

        mRecyclerView = (RecyclerView)inflate.findViewById(R.id.movie_details_listing_wrapper);
        if(mRecyclerView != null){
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }
        mPlayButton = (ImageButton)inflate.findViewById(R.id.play_trailer_button);
        mPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTrailerDialog();
            }
        });
        View tap_help = inflate.findViewById(R.id.tap_to_show_details);
        if(mMovie != null){
            tap_help.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            mPlayButton.setVisibility(View.VISIBLE);
        }
        else{
            tap_help.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
            mPlayButton.setVisibility(View.GONE);
        }
        return inflate;
    }

    private void showTrailerDialog(){
        DialogFragment dialog = new TrailerDialogFragment();
        Bundle args = new Bundle();
        args.putParcelable(TrailerDialogFragment.BUNDLE_LIST_KEY, mMovie);
        dialog.setArguments(args);
        dialog.show(getFragmentManager(), "trailer-dialog");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(mMovie != null && mMovie.getTrailers() != null && mMovie.getTrailers().size() > 0){
            inflater.inflate(R.menu.movie_detail_fragment, menu);

            MenuItem menuItem = menu.findItem(R.id.action_share);

            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
            if (mMovie != null) {
                mShareActionProvider.setShareIntent(createShareMovieIntent());
            }
        }

    }

    private Intent createShareMovieIntent() {
        Trailer trailer = mMovie.getTrailers().get(0);
        String url = "http://www.youtube.com/watch?v="+trailer.getSource();
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/*");
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, mMovie.getTitle()+" movie trailer");
        shareIntent.putExtra(Intent.EXTRA_TEXT, url);
        return shareIntent;
    }

}
