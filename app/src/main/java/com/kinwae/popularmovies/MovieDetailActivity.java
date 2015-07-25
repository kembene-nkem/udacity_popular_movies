package com.kinwae.popularmovies;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.views.fragments.MovieDetailFragment;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String BUNDLE_EXTRA_NAME = "movie_detail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        if(savedInstanceState == null){
            Intent intent = getIntent();
            Movie movie = intent.getParcelableExtra(BUNDLE_EXTRA_NAME);
            MovieDetailFragment detailFragment = MovieDetailFragment.newInstance(movie);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, detailFragment)
                    .commit();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }
}
