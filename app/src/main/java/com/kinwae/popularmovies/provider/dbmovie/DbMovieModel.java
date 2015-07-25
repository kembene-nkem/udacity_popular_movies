package com.kinwae.popularmovies.provider.dbmovie;

import com.kinwae.popularmovies.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Keeps a list of all favorited movies.
 */
public interface DbMovieModel extends BaseModel {

    /**
     * themoviedb movie id
     * Can be {@code null}.
     */
    @Nullable
    String getMovieId();

    /**
     * Movie title
     * Can be {@code null}.
     */
    @Nullable
    String getTitle();

    /**
     * Original Title
     * Can be {@code null}.
     */
    @Nullable
    String getOriginalTitle();

    /**
     * Get the {@code poster_path} value.
     * Can be {@code null}.
     */
    @Nullable
    String getPosterPath();

    /**
     * Get the {@code vote_average} value.
     * Can be {@code null}.
     */
    @Nullable
    String getVoteAverage();

    /**
     * Get the {@code overview} value.
     * Can be {@code null}.
     */
    @Nullable
    String getOverview();

    /**
     * Get the {@code release_date} value.
     * Can be {@code null}.
     */
    @Nullable
    String getReleaseDate();
}
