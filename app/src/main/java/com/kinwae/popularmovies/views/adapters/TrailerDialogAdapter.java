package com.kinwae.popularmovies.views.adapters;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kinwae.popularmovies.R;
import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.data.Trailer;

import java.util.List;

/**
 * Created by Kembene on 7/27/2015.
 */
public class TrailerDialogAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Trailer> mTrailers;
    private Trailer noTrailer = new Trailer("No Trailers", null);
    private PlayTrailerClickListener playTrailerListener;

    public TrailerDialogAdapter(Movie movie, Context context) {

        Movie.MovieDetailLoadCallback movieDetailLoadCallback = new Movie.MovieDetailLoadCallback() {
            @Override
            public void movieDetailsLoaded(Movie movie) {
                mTrailers = movie.getTrailers();
            }
        };

        if(movie != null){
            movie.loadMovieDetails(movieDetailLoadCallback, context);
        }

        playTrailerListener = new PlayTrailerClickListener();
    }

    private Trailer getTrailerAtPosition(int position){
        if (mTrailers == null || mTrailers.size() == 0){
            return noTrailer;
        }
        return mTrailers.get(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View trailerTitle = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_trailer_card, parent, false);
        return new TrailersViewHolder(trailerTitle);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Trailer trailer = getTrailerAtPosition(position);
        ((TrailersViewHolder) holder).initView(trailer);
    }

    @Override
    public int getItemCount() {
        if(mTrailers != null && mTrailers.size() > 0){
            return mTrailers.size();
        }
        return 1;
    }

    class TrailersViewHolder extends RecyclerView.ViewHolder{
        private TextView titleView;
        public TrailersViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView)itemView.findViewById(R.id.title_text);
            titleView.setOnClickListener(playTrailerListener);
        }

        public void initView(Trailer trailer) {
            if (trailer != null) {
                titleView.setText(trailer.getName());
                if(trailer.getSource() == null){
                    titleView.setCompoundDrawables(null, null, null, null);
                }
                // since we do not have access
                titleView.setTag(trailer);
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
