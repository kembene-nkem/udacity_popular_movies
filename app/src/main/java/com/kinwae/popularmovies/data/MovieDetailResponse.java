package com.kinwae.popularmovies.data;

import java.util.List;

/**
 * Created by Kembene on 7/22/2015.
 */
public class MovieDetailResponse {
    private MovieTrailersWrapper trailers;
    private MovieReviewWrapper reviews;

    public MovieReviewWrapper getReviews() {
        return reviews;
    }

    public MovieDetailResponse setReviews(MovieReviewWrapper reviews) {
        this.reviews = reviews;
        return this;
    }

    public MovieTrailersWrapper getTrailers() {
        return trailers;
    }

    public MovieDetailResponse setTrailers(MovieTrailersWrapper trailers) {
        this.trailers = trailers;
        return this;
    }

    public static class MovieReviewWrapper{
        private List<MovieReview> results;

        public List<MovieReview> getResults() {
            return results;
        }

        public MovieReviewWrapper setResults(List<MovieReview> results) {
            this.results = results;
            return this;
        }
    }

    public static class MovieTrailersWrapper{
        private List<Trailer> youtube;

        public List<Trailer> getYoutube() {
            return youtube;
        }

        public MovieTrailersWrapper setYoutube(List<Trailer> youtube) {
            this.youtube = youtube;
            return this;
        }
    }
}
