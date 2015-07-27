package com.kinwae.popularmovies.views.fragments;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.kinwae.popularmovies.R;
import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.events.MovieLoaderLoadCompleteEvent;
import com.kinwae.popularmovies.loaders.FavouritedMovieLoader;
import com.kinwae.popularmovies.loaders.MovieListNetworkLoader;
import com.kinwae.popularmovies.loaders.MovieLoaderDataProvider;
import com.kinwae.popularmovies.provider.dbmovie.DbMovieSelection;
import com.kinwae.popularmovies.util.Utility;
import com.kinwae.popularmovies.views.adapters.MovieListAdapter;
import com.kinwae.popularmovies.views.bus.RecyclerViewPosterClickListener;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieListFragment extends Fragment implements LoaderManager.LoaderCallbacks<MovieLoaderDataProvider> {

    private final static String LOG_TAG = MovieListFragment.class.getName();
    public static final String MOVIE_FRAGMENT_PAGER_BUNDLE_NAME = "frag_page";
    public final static int NETWORK_MOVIE_LIST_LOADER_ID = 0;
    public final static int CURSOR_MOVIE_LIST_LOADER_ID = 1;
    private int mLastLoaderId = -1;

    private RecyclerView mRecyclerView;
    private View mLoadingView;
    private ImageView mNoConnectionView;
    private OnFragmentInteractionListener mListener;

    private MovieListAdapter mAdapter;
    private Loader<MovieLoaderDataProvider> mMovieLoader;

    private int mPagerPosition = -1;
    private int mShortAnimationDuration;



    public static MovieListFragment newInstance(int pagerPosition) {
        Bundle bundle = new Bundle();
        bundle.putInt(MovieListFragment.MOVIE_FRAGMENT_PAGER_BUNDLE_NAME, pagerPosition);
        MovieListFragment fragment = new MovieListFragment();

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_movie_list, container, false);
        this.mRecyclerView = (RecyclerView) view.findViewById(R.id.movie_listing);
        this.mLoadingView = view.findViewById(R.id.loading_spinner);
        this.mNoConnectionView = (ImageView)view.findViewById(R.id.no_connection_found);

        //an a click listener to our no-connection found ImageView so the user can Tap to retry
        this.mNoConnectionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mLastLoaderId == CURSOR_MOVIE_LIST_LOADER_ID){
                    mNoConnectionView.setImageResource(R.drawable.no_data);
                }
                else{
                    mNoConnectionView.setImageResource(R.drawable.no_network);
                }
                // no need to re-request for content if we are on data that is loaded from the
                // database
                if(mLastLoaderId == CURSOR_MOVIE_LIST_LOADER_ID){
                    return;
                }
                crossfade(mLoadingView, mNoConnectionView, mRecyclerView);

                if(mMovieLoader != null){
                    //Loader has been initialized, inform the loader that it should reload its
                    // content
                    mMovieLoader.onContentChanged();
                }
                else{
                    crossfade(mNoConnectionView, mLoadingView, mRecyclerView);
                }
            }
        });


        mShortAnimationDuration = getResources().getInteger(android.R.integer.config_shortAnimTime);

        this.mRecyclerView.setVisibility(View.GONE);
        this.mNoConnectionView.setVisibility(View.GONE);

        if(savedInstanceState != null && savedInstanceState.containsKey(MOVIE_FRAGMENT_PAGER_BUNDLE_NAME)){
            mPagerPosition = savedInstanceState.getInt(MOVIE_FRAGMENT_PAGER_BUNDLE_NAME);
        }
        else{
            Bundle bundle = getArguments();
            if(bundle != null){
                mPagerPosition = bundle.getInt(MOVIE_FRAGMENT_PAGER_BUNDLE_NAME);
            }
        }

        int colCount = Utility.getPreferredGridColumns(getActivity());

        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), colCount);
        mAdapter = new MovieListAdapter(null);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        RecyclerViewPosterClickListener.OnItemClickListener clickListener =
                new RecyclerViewPosterClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                __movieItemClicked(view, position);
            }
        };

        mRecyclerView.addOnItemTouchListener(new RecyclerViewPosterClickListener(getActivity(), clickListener));

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        int sortCategory = Utility.getSortCategory(getActivity(), null);
        int loaderId;
        if(sortCategory == Utility.SORT_CATEGORY_NETWORK){
            loaderId = NETWORK_MOVIE_LIST_LOADER_ID;
        }
        else{
            loaderId = CURSOR_MOVIE_LIST_LOADER_ID;
        }
        getLoaderManager().initLoader(loaderId, null, this);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        //lets save the page this fragment loaded
        outState.putInt(MOVIE_FRAGMENT_PAGER_BUNDLE_NAME, mPagerPosition);
        super.onSaveInstanceState(outState);
    }

    private void __movieItemClicked(View view, int position){
        Movie movie = mAdapter.getMovieAtPosition(position);

        if(movie != null){
            //this is done just to initialise the releaseDateFormatted value which is used by our Parcelable
            // implementation to store the release date
            movie.getReleaseDateFormatted(getActivity());
            mListener.onFragmentMovieSelected(movie);
        }
    }

    @Override
    public android.support.v4.content.Loader<MovieLoaderDataProvider> onCreateLoader(int id, Bundle args) {
        //we only create a loader when we have a page to work with
        if(mPagerPosition >= 0){
            if(mMovieLoader != null){
                mAdapter.updateDataProvider(null);
            }
            // depending on the category of the data we want to load (network fetched or
            // database fetched), create the appropriate loader
            if(id == NETWORK_MOVIE_LIST_LOADER_ID){
                mMovieLoader = new MovieListNetworkLoader(getActivity(), mPagerPosition);
            }
            else{
                DbMovieSelection selection = new DbMovieSelection();
                mMovieLoader = new FavouritedMovieLoader(getActivity(), selection);
            }
            mLastLoaderId = id;
            return mMovieLoader;
        }
        return null;
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<MovieLoaderDataProvider> loader,
                               MovieLoaderDataProvider data) {
        if(mLastLoaderId == CURSOR_MOVIE_LIST_LOADER_ID){
            mNoConnectionView.setImageResource(R.drawable.no_data);
        }
        else{
            mNoConnectionView.setImageResource(R.drawable.no_network);
        }
        if(data != null && data.getItemsCount() > 0){
            //data has finished loading, lets update our adapter
            mAdapter.updateDataProvider(data);
            MovieLoaderLoadCompleteEvent event = new MovieLoaderLoadCompleteEvent(data);
            Utility.getSharedEventBus().post(event);
            crossfade(mRecyclerView, mLoadingView, mNoConnectionView);
        }
        else{
            crossfade(mNoConnectionView,  mLoadingView, mRecyclerView);
        }

    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<MovieLoaderDataProvider> loader) {
        if(loader.getId() == mLastLoaderId)
            mAdapter.updateDataProvider(null);
    }

    /**
     * animation gotten from google's android training
     * http://developer.android.com/training/animation/crossfade.html
     */
    private void crossfade(final View viewToShow, final View viewToHide1, final View viewToHide2) {

        // Set the content view to 0% opacity but visible, so that it is visible
        // (but fully transparent) during the animation.
        viewToShow.setAlpha(0f);
        viewToShow.setVisibility(View.VISIBLE);

        // Animate the content view to 100% opacity, and clear any animation
        // listener set on the view.
        viewToShow.animate()
                .alpha(1f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewToShow.setVisibility(View.VISIBLE);
                    }
                });

        // Animate the loading view to 0% opacity. After the animation ends,
        // set its visibility to GONE as an optimization step (it won't
        // participate in layout passes, etc.)
        viewToHide1.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewToHide1.setVisibility(View.GONE);
                    }
                });

        viewToHide2.animate()
                .alpha(0f)
                .setDuration(mShortAnimationDuration)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        viewToHide2.setVisibility(View.GONE);
                    }
                });

    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentMovieSelected(Movie movie);
    }
}
