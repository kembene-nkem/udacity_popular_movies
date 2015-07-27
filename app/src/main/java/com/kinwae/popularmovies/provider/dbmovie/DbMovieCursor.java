package com.kinwae.popularmovies.provider.dbmovie;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code db_movie} table.
 */
public class DbMovieCursor extends AbstractCursor implements DbMovieModel {
    public DbMovieCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(DbMovieColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * themoviedb movie id
     * Can be {@code null}.
     */
    @Nullable
    public String getMovieId() {
        String res = getStringOrNull(DbMovieColumns.MOVIE_ID);
        return res;
    }

    /**
     * Movie title
     * Can be {@code null}.
     */
    @Nullable
    public String getTitle() {
        String res = getStringOrNull(DbMovieColumns.TITLE);
        return res;
    }

    /**
     * Original Title
     * Can be {@code null}.
     */
    @Nullable
    public String getOriginalTitle() {
        String res = getStringOrNull(DbMovieColumns.ORIGINAL_TITLE);
        return res;
    }

    /**
     * Get the {@code poster_path} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getPosterPath() {
        String res = getStringOrNull(DbMovieColumns.POSTER_PATH);
        return res;
    }

    /**
     * Get the {@code vote_average} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getVoteAverage() {
        String res = getStringOrNull(DbMovieColumns.VOTE_AVERAGE);
        return res;
    }

    /**
     * Get the {@code overview} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getOverview() {
        String res = getStringOrNull(DbMovieColumns.OVERVIEW);
        return res;
    }

    /**
     * Get the {@code release_date} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getReleaseDate() {
        String res = getStringOrNull(DbMovieColumns.RELEASE_DATE);
        return res;
    }

    public Movie getMovie(){
        Movie movie = new Movie();
        movie.set_id(this.getId());
        movie.setId(Long.valueOf(this.getMovieId()));
        //only favourited movies are in the database
        movie.setFavorited(true);
        movie.setOriginalTitle(this.getOriginalTitle());
        movie.setOverview(this.getOverview());
        movie.setPosterPath(this.getPosterPath());
        movie.setTitle(this.getTitle());
        double vote_average = 0.0;
        String voteAverage = this.getVoteAverage();
        if(voteAverage != null && voteAverage.trim().length() > 0){
            vote_average = Double.valueOf(voteAverage);
        }
        movie.setVoteAverage(vote_average);
        movie.setReleaseDate(this.getReleaseDate());
        return movie;
    }

    public Movie nextMovie(){
        if(this.moveToNext()){
            return getMovie();
        }
        return null;
    }
}
