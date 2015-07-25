package com.kinwae.popularmovies.provider.dbtrailer;

import com.kinwae.popularmovies.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Trailers associated with a particular movie
 */
public interface DbTrailerModel extends BaseModel {

    /**
     * themoviedb movie id
     * Can be {@code null}.
     */
    @Nullable
    String getMovieId();

    /**
     * Trailer name
     * Can be {@code null}.
     */
    @Nullable
    String getName();

    /**
     * Youtube video id
     * Can be {@code null}.
     */
    @Nullable
    String getYoutubeId();
}
