package com.gknayzeh.popularmovies.app;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.gknayzeh.popularmovies.app.data.MovieContract.MovieEntry;
import com.gknayzeh.popularmovies.app.data.MovieData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by gknayzeh on 08/09/15.
 */
public class FetchMoviesTask extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    private final Context mContext;
//    private final PosterViewAdapter mPosterViewAdapter;

    public FetchMoviesTask(Context context, PosterViewAdapter posterViewAdapter) {
        mContext = context;
//        mPosterViewAdapter = posterViewAdapter;

    }

    private void getMovieData(String moviesJsonStr, String sortBy) throws JSONException {

        final String TMDB_RESULTS = "results";

        JSONObject moviesJson = new JSONObject(moviesJsonStr);
        JSONArray popularMoviesJson = moviesJson.getJSONArray(TMDB_RESULTS);

        Vector<ContentValues> cVVector = new Vector<ContentValues>(popularMoviesJson.length());
        for (int i = 0; i < popularMoviesJson.length(); i++) {
            MovieData movie = new MovieData(popularMoviesJson.getJSONObject(i), sortBy, i);
            cVVector.add(movie.getContentValues());

        }

        int inserted = 0;

        if ( cVVector.size() > 0 ) {
            ContentValues[] cvArray = new ContentValues[cVVector.size()];
            cVVector.toArray(cvArray);
            inserted = mContext.getContentResolver().bulkInsert(MovieEntry.CONTENT_URI, cvArray);
        }

        Log.d(LOG_TAG, "FetchMoviesTask Complete. " + inserted + " Inserted");

    }

    @Override
    protected Void doInBackground(String... params) {

        if (params.length == 0) {
            return null;
        }

        String sortByQuery = params[0];

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
                    .appendQueryParameter("sort_by", sortByQuery)
                    .appendQueryParameter("api_key", mContext.getResources().getString(R.string.api_key)).build();
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
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }

        try {
            getMovieData(popularMoviesJsonStr, sortByQuery);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        return null;
    }

//    @Override
//    protected void onPostExecute(List<MovieData> movieDatas) {
//        if (movieDatas != null) {
//            Log.i(LOG_TAG, "Strings: " + movieDatas);
//            MainActivity.mMovieDatas.clear();
//            MainActivity.mMovieDatas.addAll(movieDatas);
//
////            mPosterViewAdapter.notifyDataSetChanged();
//        }
//    }
}
