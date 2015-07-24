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
    List<Trailer> trailerList = new ArrayList<>();
    private Movie mMovie;

    private MovieReview noReview = new MovieReview("", "No Reviews");
    private Trailer noTrailer = new Trailer("No Trailers", null);

    private final int HEADER_VIEW = 0;
    private final int REVIEWS_VIEW = 1;
    private final int TRAILERS_VIEW = 2;

    private final int REVIEWS_TITLE_VIEW = 3;
    private final int TRAILER_TITLE_VIEW = 4;

    private PlayTrailerClickListener trailerPlayListener;

    public MovieDetailAdapter(Movie movie) {
        this.mMovie = movie;
        this.trailerPlayListener = new PlayTrailerClickListener();
        Log.i(MovieDetailAdapter.class.getName(), "Trailers: "+movie.getTrailers());
        getMovieDetails();
    }

    public void getMovieDetails(){
        if(this.mMovie != null){
            this.mMovie.loadMovieDetails(new Movie.MovieDetailLoadCallback() {
                @Override
                public void movieDetailsLoaded(Movie movie) {
                    // by this time, the movie's trailer and review list has already been updated
                    reviewList = movie.getReviews();
                    trailerList = movie.getTrailers();
                    MovieDetailAdapter.this.notifyDataSetChanged();
                }
            });
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
            case TRAILER_TITLE_VIEW:
                View trailerTitle = LayoutInflater.from(context)
                        .inflate(R.layout.movie_detail_title_card, parent, false);
                return new TitleViewHolder(trailerTitle);
            case TRAILERS_VIEW:
                View trailer = LayoutInflater.from(context)
                        .inflate(R.layout.movie_trailer_card, parent, false);
                return new TrailersViewHolder(trailer);
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
                break;
            case REVIEWS_TITLE_VIEW:
                ((TitleViewHolder)holder).titleView.setText(R.string.movie_review_listing_title);
                break;
            case TRAILER_TITLE_VIEW:
                ((TitleViewHolder)holder).titleView.setText(R.string.movie_trailer_listing_title);
                break;
            case TRAILERS_VIEW:
                Trailer trailer = getTrailerAtPosition(position);
                TrailersViewHolder trailersViewHolder = (TrailersViewHolder) holder;
                trailersViewHolder.initView(trailer);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (mMovie == null)
            return 0;
        int reviewCount = reviewList.size();
        int trailerCount = trailerList.size();

        if (reviewCount == 0){
            reviewCount = 1;
        }

        if (trailerCount == 0){
            trailerCount = 1;
        }

        //the 3 there represent item for the detail, review title and trailer title
        return reviewCount + trailerCount + 3;
    }

    @Override
    public int getItemViewType(int position) {
        if(position == 0){
            return HEADER_VIEW;
        }
        else if(isWithinTrailerHeader(position)){
            return TRAILER_TITLE_VIEW;
        }
        else if(isWithinTrailersList(position)) {
            return TRAILERS_VIEW;
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
        return reviewList.get(position - (getTrailerListCount() + 3));
    }

    private Trailer getTrailerAtPosition(int position){
        if (trailerList.size() == 0){
            return noTrailer;
        }
        return trailerList.get(position - 2);
    }

    private boolean isWithinReviewsList(int position){
        return position > getTrailerListCount() + 3;
    }

    private boolean isReviewHeader(int position){
        return position == getTrailerListCount() + 2;
    }

    private boolean isWithinTrailerHeader(int position){
        return position == 1;
    }

    private boolean isWithinTrailersList(int position){
        return position > 1 && position < getTrailerListCount() + 2;
    }

    private int getTrailerListCount(){
        if(trailerList.size() == 0){
            return 1;
        }
        return trailerList.size();
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
                        Utility.setFavoritedStatus(tagState, button, movie);
                    }
                });
                Utility.setFavoritedStatus(mMovie.isFavorited(), imageButton, movie);
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

    class TrailersViewHolder extends RecyclerView.ViewHolder{
        private TextView titleView;
        private ImageButton playButton;
        public TrailersViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView)itemView.findViewById(R.id.title_text);
            playButton = (ImageButton)itemView.findViewById(R.id.play_trailer_button);
            playButton.setOnClickListener(trailerPlayListener);
        }

        public void initView(Trailer trailer) {
            if (trailer != null) {
                titleView.setText(trailer.getName());
                if(trailer.getSource() == null){
                    playButton.setVisibility(View.GONE);
                }
                else{
                    playButton.setVisibility(View.VISIBLE);
                }
                // since we do not have access
                playButton.setTag(trailer);
            }
        }
    }

    private static class PlayTrailerClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Object tag = v.getTag();
            if(tag != null){
                Trailer trailer = (Trailer)tag;
                if(trailer.getSource() != null){
                    // if a youtube or an equivalent app is installed, use it
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getSource()));
                    try{
                        v.getContext().startActivity(intent);
                    }
                    catch (ActivityNotFoundException ex){
                        // youtube or equivalent app not installed, fallback to browser
                        intent=new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v="+trailer.getSource()));
                        v.getContext().startActivity(intent);
                    }
                }
            }
        }
    }
}
