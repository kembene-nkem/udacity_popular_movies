package com.kinwae.popularmovies.data;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.format.DateFormat;
import android.util.Log;

import com.kinwae.popularmovies.events.MovieDetailLoadedEvent;
import com.kinwae.popularmovies.net.NetworkRequest;
import com.kinwae.popularmovies.util.Utility;
import com.squareup.otto.Bus;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Kembene on 6/24/2015.
 */
public class Movie implements Parcelable {
    /**
     * DB id column
     */
    private long _id;
    private long id;
    private String title;
    private String originalTitle;
    private String posterPath;
    private String backdropPath;
    private double voteAverage;
    private String overview;
    private String releaseDate;
    private String releaseDateFormatted;
    private boolean favorited;
    private List<Trailer> trailers;
    private List<MovieReview> reviews;
    private boolean loadedExtra;
    private static String LOGGER = Movie.class.getName();


    public Movie() {
        this(null, null, 0);
    }

    public Movie(String mTitle, String mPosterPath, long mId) {
        this.title = mTitle;
        this.posterPath = mPosterPath;
        this.id = mId;
        trailers = new ArrayList<>();
        reviews = new ArrayList<>();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public long get_id() {
        return _id;
    }

    public Movie set_id(long _id) {
        this._id = _id;
        return this;
    }

    public long getId() {
        return id;
    }

    public Movie setId(long id) {
        this.id = id;
        return this;
    }

    public boolean isLoadedExtra() {
        return loadedExtra;
    }

    public Movie setLoadedExtra(boolean loadedExtra) {
        this.loadedExtra = loadedExtra;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public Movie setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public Movie setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
        return this;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public Movie setPosterPath(String posterPath) {
        this.posterPath = posterPath;
        return this;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public Movie setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
        return this;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public Movie setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
        return this;
    }

    public String getOverview() {
        return overview;
    }

    public Movie setOverview(String overview) {
        this.overview = overview;
        return this;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Movie setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
        return this;
    }

    public String getReleaseDateFormatted(Context context){
        if(this.releaseDateFormatted == null)
            if(this.releaseDate != null){
                try {
                    Date parse = SimpleDateFormat.getDateInstance().parse(this.releaseDate);
                    this.releaseDateFormatted = DateFormat.getDateFormat(context).format(parse);
                } catch (ParseException e) {
                    Log.d(LOGGER, "Error parsing date "+e.getMessage());
                    this.releaseDateFormatted = this.releaseDate;
                }
            }
        return this.releaseDateFormatted;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public Movie setFavorited(boolean favorited) {
        this.favorited = favorited;
        return this;
    }

    public List<Trailer> getTrailers() {
        return trailers;
    }

    public Movie setTrailers(List<Trailer> trailers) {
        this.trailers = trailers;
        return this;
    }

    public List<MovieReview> getReviews() {
        return reviews;
    }

    public Movie setReviews(List<MovieReview> reviews) {
        this.reviews = reviews;
        return this;
    }

    /**
     * loads details (trailers, reviews) of a movie in the background and calls the callback on the
     * main thread after the data has been loaded successfully
     * @param callback
     */
    public void loadMovieDetails(final MovieDetailLoadCallback callback){
        if(loadedExtra){
            callback.movieDetailsLoaded(this);
            return;
        }
        else{
            // check if we can gather the details from the database. This only happen if the user
            // has favourited the movie
            NetworkRequest.getRestService().getMovieDetail(this.getId(), "trailers,reviews",
                    new Callback<MovieDetailResponse>() {
                        @Override
                        public void success(MovieDetailResponse movieDetailResponse, Response response) {
                            movieDetailContentReceived(movieDetailResponse);
                            callback.movieDetailsLoaded(Movie.this);
                            Utility.getSharedEventBus().post(new MovieDetailLoadedEvent(Movie.this));
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            //we don't do anything on failure
                        }
                    }
            );
        }
    }

    private void movieDetailContentReceived(MovieDetailResponse detailResponse){
        MovieDetailResponse.MovieReviewWrapper reviews = detailResponse.getReviews();
        MovieDetailResponse.MovieTrailersWrapper trailers = detailResponse.getTrailers();
        if(reviews != null && reviews.getResults().size() > 0){
            this.reviews.clear();
            if(reviews.getResults().size() > 5){
                //we do not want to store more than 5 reviews
                for (int i=0; i<=5; i++){
                    this.reviews.add(reviews.getResults().get(i));
                }

            }
            else{
                this.reviews.addAll(reviews.getResults());
            }
        }
        if(trailers != null && trailers.getYoutube().size() > 0){
            this.trailers.clear();
            this.trailers.addAll(trailers.getYoutube());
        }
        loadedExtra = true;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(originalTitle);
        dest.writeString(posterPath);
        dest.writeString(backdropPath);
        dest.writeDouble(voteAverage);
        dest.writeString(overview);
        dest.writeString(this.releaseDate);
        dest.writeInt((this.favorited ? 1: 0));
        dest.writeInt((this.loadedExtra ? 1: 0));
        dest.writeTypedList(this.reviews);
        dest.writeTypedList(this.trailers);
    }

    private Movie(Parcel in) {
        this.reviews = new ArrayList<>();
        this.trailers = new ArrayList<>();
        id = in.readLong();
        title = in.readString();
        originalTitle = in.readString();
        posterPath = in.readString();
        backdropPath = in.readString();
        voteAverage = in.readDouble();
        overview = in.readString();
        this.releaseDate = in.readString();
        int favorited = in.readInt();
        int detailsLoaded = in.readInt();

        in.readTypedList(reviews, MovieReview.CREATOR);
        in.readTypedList(trailers, Trailer.CREATOR);


        this.favorited = favorited != 0;
        this.loadedExtra = detailsLoaded != 0;


    }

    public interface MovieDetailLoadCallback{
        void movieDetailsLoaded(Movie movie);
    }
}
