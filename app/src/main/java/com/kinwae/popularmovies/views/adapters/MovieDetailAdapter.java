package com.kinwae.popularmovies.views.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.kinwae.popularmovies.R;
import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.data.MovieReview;
import com.kinwae.popularmovies.data.Trailer;
import com.kinwae.popularmovies.util.Utility;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kembene on 7/11/2015.
 */
public class MovieDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<MovieReview> reviewList = new ArrayList<>();
    private Movie mMovie;

    private MovieReview noReview = new MovieReview("", "No Reviews");

    private final int HEADER_VIEW = 0;
    private final int REVIEWS_VIEW = 1;

    private final int REVIEWS_TITLE_VIEW = 3;

    public MovieDetailAdapter(Movie movie, Context context) {
        this.mMovie = movie;
        getMovieDetails(context);
    }

    private void getMovieDetails(Context context){
        if(this.mMovie != null){

            Movie.MovieDetailLoadCallback movieDetailLoadCallback = new Movie.MovieDetailLoadCallback() {
                @Override
                public void movieDetailsLoaded(Movie movie) {
                    // by this time, the movie's trailer and review list has already been updated
                    reviewList = movie.getReviews();
                    MovieDetailAdapter.this.notifyDataSetChanged();
                }
            };

            this.mMovie.loadMovieDetails(movieDetailLoadCallback, context);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        switch (viewType){
            case HEADER_VIEW:
                View view = LayoutInflater.from(context)
                        .inflate(R.layout.movie_detail_card, parent, false);
                return new HeaderViewHolder(view);
            case REVIEWS_VIEW:
                View review = LayoutInflater.from(context)
                        .inflate(R.layout.movie_review_card, parent, false);
                return new ReviewsViewHolder(review);
            case REVIEWS_TITLE_VIEW:
                View reviewTitle = LayoutInflater.from(context)
                        .inflate(R.layout.movie_detail_title_card, parent, false);
                return new TitleViewHolder(reviewTitle);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType){
            case HEADER_VIEW:
                ((HeaderViewHolder)holder).initView(mMovie);
                break;
            case REVIEWS_VIEW:
                MovieReview review = getReviewAtPosition(position);
                ((ReviewsViewHolder)holder).reviewTextTitle.setText(review.getAuthor());
                ((ReviewsViewHolder)holder).reviewText.setText(review.getContent());
                if(position == getItemCount() - 1){
                    TextView reviewText = ((ReviewsViewHolder) holder).reviewText;
                    reviewText.setPadding(reviewText.getPaddingLeft(), reviewText.getPaddingTop(),
                            reviewText.getPaddingRight(), 40);
                }
                break;
            case REVIEWS_TITLE_VIEW:
                ((TitleViewHolder)holder).titleView.setText(R.string.movie_review_listing_title);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mMovie == null)
            return 0;
        int reviewCount = reviewList.size();

        if (reviewCount == 0){
            reviewCount = 1;
        }

        return reviewCount + 2;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return HEADER_VIEW;
        }
        else if(isReviewHeader(position)){
            return REVIEWS_TITLE_VIEW;
        }
        return REVIEWS_VIEW;

    }

    @Override
    public void onViewRecycled(RecyclerView.ViewHolder holder) {
    }

    private MovieReview getReviewAtPosition(int position){
        if (reviewList.size() == 0){
            return noReview;
        }
        return reviewList.get(position - 2);
    }

    private boolean isWithinReviewsList(int position){
        return position > 1 && position < getReviewListCount() + 2;
    }

    private boolean isReviewHeader(int position){
        return position == 1;
    }

    private int getReviewListCount(){
        if(reviewList.size() == 0){
            return 1;
        }
        return reviewList.size();
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder{

        TextView textView;
        TextView ratingView;
        TextView dateView;
        TextView plotView;
        ImageView imageView;
        ImageButton imageButton;


        public HeaderViewHolder(View itemView) {
            super(itemView);
            textView = (TextView)itemView.findViewById(R.id.movie_detail_title);
            ratingView = (TextView)itemView.findViewById(R.id.movie_detail_vote_rate);
            dateView = (TextView)itemView.findViewById(R.id.movie_detail_date);
            plotView = (TextView)itemView.findViewById(R.id.movie_detail_plot);
            imageView = (ImageView)itemView.findViewById(R.id.movie_detail_poster);
            imageButton = (ImageButton)itemView.findViewById(R.id.rate_button);
        }

        public void initView(final Movie movie){
            if(movie != null){
                Context context = this.itemView.getContext();
                textView.setText(mMovie.getOriginalTitle());
                Utility.loadImageIntoView(imageView, mMovie, context);
                ratingView.setText(Double.toString(mMovie.getVoteAverage()));
                dateView.setText(mMovie.getReleaseDateFormatted(context));
                plotView.setText(mMovie.getOverview());

                imageButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ImageButton button = (ImageButton)v;
                        Object tag = button.getTag();
                        Boolean tagState = false;
                        if(tag != null){
                            tagState = Boolean.valueOf(tag.toString());
                        }
                        Boolean oldState = tagState;
                        tagState = !tagState;
                        Utility.setFavoritedStatus(tagState, button, movie, true);
                    }
                });
                Utility.setFavoritedStatus(mMovie.isFavorited(), imageButton, movie, false);
            }
        }
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder{
        private TextView reviewText;
        private TextView reviewTextTitle;
        public ReviewsViewHolder(View itemView) {
            super(itemView);
            reviewText = (TextView)itemView.findViewById(R.id.movie_review_text);
            reviewTextTitle = (TextView)itemView.findViewById(R.id.movie_review_text_name);
        }
    }

    class TitleViewHolder extends RecyclerView.ViewHolder{
        private TextView titleView;
        public TitleViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView)itemView.findViewById(R.id.title_text);
        }
    }
}
