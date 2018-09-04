package com.solo.movinfo.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.Group;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.solo.movinfo.R;
import com.solo.movinfo.data.DataManager;
import com.solo.movinfo.ui.movies.list.MoviesListActivity;

import javax.inject.Inject;

import timber.log.Timber;

/**
 * Fragment to manage splash view
 */

public class SplashFragment extends Fragment {
    @Inject
    DataManager mDataManager;

    @Override

    public void onCreate(@Nullable Bundle savedInstanceState) {
        // Inject member variables with objects
        ((SplashActivity) requireActivity()).getActivityComponent().inject(this);

        // Move onto movies list if splash screen has been seen before
        if (mDataManager.wasSplashScreenSeen()) {
            openMoviesList();
        }

        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.splash_fragment, container, false);

        initUI(v);

        return v;
    }

    private void initUI(View view) {
        Group startViews = view.findViewById(R.id.startGroup);

        for (int id : startViews.getReferencedIds()) {
            view.findViewById(id).setOnClickListener((v) -> openMoviesList());
        }
    }

    private void openMoviesList() {
        Timber.d("Opening movies list");
        Intent intent = new Intent(requireContext(), MoviesListActivity.class);
        startActivity(intent);
        requireActivity().finish();
    }
}
