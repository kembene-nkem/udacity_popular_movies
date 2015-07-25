package com.kinwae.popularmovies.views.adapters;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.kinwae.popularmovies.views.fragments.MovieListFragment;

/**
 * A ViewPager adapter that will be used by the viewpager of an activity
 * Created by Kembene on 6/27/2015.
 */
public class MoviePagerAdapter extends FragmentStatePagerAdapter {



    private int pageCount = 1;

    public MoviePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return MovieListFragment.newInstance(position);
    }


    @Override
    public int getCount() {
        return pageCount;
    }

    public MoviePagerAdapter setPageCount(int pageCount) {
        this.pageCount = pageCount;
        return this;
    }
}
