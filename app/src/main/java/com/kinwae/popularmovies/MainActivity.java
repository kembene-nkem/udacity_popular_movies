package com.kinwae.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.events.MovieFavoritedEvent;
import com.kinwae.popularmovies.util.Utility;
import com.kinwae.popularmovies.views.adapters.MoviePagerAdapter;
import com.kinwae.popularmovies.views.fragments.MovieDetailFragment;
import com.kinwae.popularmovies.views.fragments.MovieListFragment;
import com.kinwae.popularmovies.views.managers.DefaultMovieListManager;
import com.squareup.otto.Subscribe;

public class MainActivity extends AppCompatActivity
        implements MovieListFragment.OnFragmentInteractionListener,
        DefaultMovieListManager.MovieListManagerHolder{

    private final static String BUNDLE_NAME_ADAPTER_ITEM_COUNT = "adap_ite_cnt";
    private final static String BUNDLE_NAME_ADAPTER_LOADER_TYPE = "adap_ld_typ";
    private final static String LOG_TAG = MainActivity.class.getName();

    private MoviePagerAdapter mPagerAdapter;
    private ViewPager mPager;
    private DefaultMovieListManager mListManager;
    private String mCurrentSorting;
    private boolean mTwoPane;
    private static String DETAIL_FRAGMENT_TAG = "DETAIL_FRAG";
    TextView view_type_label;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPagerAdapter = new MoviePagerAdapter(getSupportFragmentManager());
        mListManager = new DefaultMovieListManager(this, mPagerAdapter);

        View detailContainer = findViewById(R.id.movie_detail_container);
        view_type_label = (TextView) findViewById(R.id.view_type_label);
        preparePagerAdapter(mPagerAdapter, savedInstanceState);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(mPagerAdapter);
        mCurrentSorting = Utility.getPreferredMovieSortOrder(this);

        //todo provide integration to chrome debugger. Remove on production
        /*Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(
                                Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(
                                Stetho.defaultInspectorModulesProvider(this))
                        .build());*/

        if(detailContainer != null){
            mTwoPane = true;
            MovieDetailFragment detailFragment = MovieDetailFragment.newInstance(null);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,detailFragment,DETAIL_FRAGMENT_TAG)
                    .commit();
        }
        else{
            mTwoPane = false;
        }

    }

    private void preparePagerAdapter(MoviePagerAdapter adapter, Bundle savedInstanceState){
        int adapterPageCount = 1;
        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(BUNDLE_NAME_ADAPTER_LOADER_TYPE)){
                mListManager.setLastLoadType(savedInstanceState.getInt(BUNDLE_NAME_ADAPTER_LOADER_TYPE));
            }
            if(savedInstanceState.containsKey(BUNDLE_NAME_ADAPTER_ITEM_COUNT)){
                adapterPageCount = savedInstanceState.getInt(BUNDLE_NAME_ADAPTER_ITEM_COUNT);
            }
        }
        // we have to set the pageCount on the adapter before we set the adapter on the mPager else
        // we could get an exception telling us that we changed the pageCount without calling
        // notifyDatasetChanged
        adapter.setPageCount(adapterPageCount);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(BUNDLE_NAME_ADAPTER_ITEM_COUNT, mPagerAdapter.getCount());
        outState.putInt(BUNDLE_NAME_ADAPTER_LOADER_TYPE, mListManager.getLastLoadType());
    }

    @Override
    protected void onStart() {
        super.onStart();
        Utility.getSharedEventBus().register(this);
        String sort = Utility.getPreferredMovieSortOrder(this);
        view_type_label.setText(getSortLabel(sort));


        if(sort != null && !sort.equals(mCurrentSorting)){
            if(mTwoPane){
                MovieDetailFragment detailFragment = MovieDetailFragment.newInstance(null);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, detailFragment, DETAIL_FRAGMENT_TAG)
                        .commit();
            }
            //the user has changed the sorting preferences, so tell the PagerAdapter that
            // data set has changed
            if(mPagerAdapter != null){
                int currentSortCat = Utility.getSortCategory(this, mCurrentSorting);
                int newSortCat = Utility.getSortCategory(this, sort);
                mCurrentSorting = sort;
                if(currentSortCat != newSortCat){
                    mPagerAdapter = new MoviePagerAdapter(getSupportFragmentManager());
                    preparePagerAdapter(mPagerAdapter, null);
                    mListManager.swapPagerAdapter(mPagerAdapter);
                    mPager.setAdapter(mPagerAdapter);
                }
                else{
                    mPagerAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    private String getSortLabel(String sorttype){
        sorttype = sorttype.replace('.','_');
        sorttype = "label_sort_type_"+sorttype;
        int sortLabelId = getResources().getIdentifier(sorttype, "string", getPackageName());
        return getResources().getString(sortLabelId);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utility.getSharedEventBus().unregister(this);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentMovieSelected(Movie movie) {
        //callback called by MovieListFragment to notify this activity that a movie has been selected
        showMovieDetails(movie);
    }


    @Override
    public DefaultMovieListManager getMovieListManager() {
        return mListManager;
    }

    protected void showMovieDetails(Movie movie){
        if(mTwoPane){
            MovieDetailFragment detailFragment = MovieDetailFragment.newInstance(movie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container,detailFragment,DETAIL_FRAGMENT_TAG)
                    .commit();
        }
        else{
            Intent intent = new Intent(this, MovieDetailActivity.class);

            intent.putExtra(MovieDetailActivity.BUNDLE_EXTRA_NAME, movie);
            startActivity(intent);
        }
    }

    @Subscribe
    public void movieWasFavoritedListener(MovieFavoritedEvent event){

        if(mTwoPane && !event.wasFavorited()){
            MovieDetailFragment detailFragment = MovieDetailFragment.newInstance(null);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, detailFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        }
    }
}
