package com.gknayzeh.popularmovies.app;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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

    public class FetchMoviesTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String popularMoviesJsonStr = null;

            try {
                // Construct the URL for the popular movies query
                Uri uri = new Uri.Builder()
                        .scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("discover")
                        .appendPath("movie")
                        .appendQueryParameter("sort_by", "popularity.desc")
                        .appendQueryParameter("api_key", getResources().getString(R.string.api_key)).build();
                Log.i(LOG_TAG, uri.toString());

                URL url = new URL(uri.toString());

                Log.i(LOG_TAG, url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                popularMoviesJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return null;
        }
    }
}
