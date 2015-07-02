package com.kinwae.popularmovies.views.bus;

import android.content.Context;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * implementation found on http://sapandiwakar.in/recycler-view-item-click-handler
 * Created by Kembene on 6/24/2015.
 */
public class RecyclerViewPosterClickListener implements RecyclerView.OnItemTouchListener {

    private OnItemClickListener mListener;
    private GestureDetector mGestureDetector;


    public RecyclerViewPosterClickListener(Context context, OnItemClickListener listener) {
        this.mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        boolean isSingleTap = mGestureDetector.onTouchEvent(e);
        if(mListener != null && isSingleTap) {
            View childView = view.findChildViewUnder(e.getX(), e.getY());
            mListener.onItemClick(view.getChildViewHolder(childView).getAdapterPosition(), childView);
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent e) {


    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    public interface OnItemClickListener{
        void onItemClick(int position, View view);
    }
}
