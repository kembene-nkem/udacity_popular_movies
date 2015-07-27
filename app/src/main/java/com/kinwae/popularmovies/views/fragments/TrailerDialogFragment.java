package com.kinwae.popularmovies.views.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.kinwae.popularmovies.R;
import com.kinwae.popularmovies.data.Movie;
import com.kinwae.popularmovies.views.adapters.TrailerDialogAdapter;

/**
 * Created by Kembene on 7/27/2015.
 */
public class TrailerDialogFragment extends DialogFragment {

    public final static String BUNDLE_LIST_KEY = "mMovie";
    private Movie mMovie;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null && arguments.containsKey(BUNDLE_LIST_KEY)){
            mMovie = arguments.getParcelable(BUNDLE_LIST_KEY);
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View inflatedView = inflater.inflate(R.layout.trailer_dialog, null);
        RecyclerView recyclerView = (RecyclerView)inflatedView.findViewById(R.id.movie_trailer_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 1);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new TrailerDialogAdapter(mMovie, getActivity()));
        alertBuilder.setView(inflatedView);
        alertBuilder.setPositiveButton(R.string.abc_action_mode_done, null);
        return alertBuilder.create();
    }


}
