package com.kinwae.popularmovies.provider.dbreview;

import com.kinwae.popularmovies.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Reviews associated with this movie
 */
public interface DbReviewModel extends BaseModel {

    /**
     * themoviedb movie id
     * Can be {@code null}.
     */
    @Nullable
    String getMovieId();

    /**
     * Reviewer name
     * Can be {@code null}.
     */
    @Nullable
    String getReviewer();

    /**
     * Review
     * Can be {@code null}.
     */
    @Nullable
    String getReview();

    /**
     * Review Id
     * Can be {@code null}.
     */
    @Nullable
    String getReviewId();
}
