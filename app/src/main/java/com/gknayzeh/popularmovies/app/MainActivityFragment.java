package com.gknayzeh.popularmovies.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private final String LOG_TAG = MainActivityFragment.class.getSimpleName();

    ArrayAdapter<String> mPopularMoviesAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Create some dummy data for the ListView.  Here's a sample weekly forecast
        String[] data = {
                "Ant Man",
                "Mr. Holmes",
                "Trainwreck",
                "Mission Impossible: Rogue Nation",
                "Hitman: Agent 47",
                "Sinister 2",
                "American Ultra",
                "Straight Outta Compton",
                "Mad Max: Fury Road",
                "Aloha",
                "Far From Madding Crowd",
                "Unfriended",
                "The Man From Uncle",
                "Pixels",
                "Fantastic Four",
                "Inside Out",
                "Jurassic World",
                "Magic Mike XXL",
                "Minions",
                "Paper Towns",
                "Ricki and the Flash",
                "Shaun the Sheep Movie",
                "Southpaw",
                "The Gift",
                "Vacation"

        };

        List<String> popularMovies = new ArrayList<String>(Arrays.asList(data));

        mPopularMoviesAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_movie, R.id.list_item_movie_textview, popularMovies);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        gridView.setAdapter(mPopularMoviesAdapter);

        return rootView;
    }
}
