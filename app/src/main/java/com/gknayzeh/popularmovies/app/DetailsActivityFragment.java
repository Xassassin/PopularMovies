package com.gknayzeh.popularmovies.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.ShareActionProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gknayzeh.popularmovies.app.data.MovieContract;
import com.squareup.picasso.Picasso;


/**
 * A placeholder fragment containing a simple view.
 */
public class DetailsActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final static String LOG_TAG = DetailsActivityFragment.class.getSimpleName();

    private final static String POPULAR_MOVIES_HASH_TAG = " #PopularMovies";

    private ShareActionProvider mShareActionProvider;
    private String mMovieString;

    private static final int MOVIE_DETAILS_LOADER = 0;

    private static final String[] MOVIE_COLUMNS = {
            // In this case the id needs to be fully qualified with a table name, since
            // the content provider joins the location & weather tables in the background
            // (both have an _id column)
            // On the one hand, that's annoying.  On the other, you can search the weather table
            // using the location set by the user, which is only in the Location table.
            // So the convenience is worth it.
            MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COL_POSTER_PATH,
            MovieContract.MovieEntry.COL_BACKDROP_PATH,
            MovieContract.MovieEntry.COL_MOVIE_TITLE,
            MovieContract.MovieEntry.COL_OVERVIEW,
            MovieContract.MovieEntry.COL_RELEASE_DATE,
            MovieContract.MovieEntry.COL_VOTE_AVERAGE,
            MovieContract.MovieEntry.COL_SORT_BY,
            MovieContract.MovieEntry.COL_SORT_ORDER
    };

    static final int COL_MOVIE_ID = 0;
    static final int COL_POSTER_PATH = 1;
    static final int COL_BACKDROP_PATH = 2;
    static final int COL_MOVIE_TITLE = 3;
    static final int COL_OVERVIEW = 4;
    static final int COL_RELEASE_DATE = 5;
    static final int COL_VOTE_AVERAGE = 6;
    static final int COL_SORT_BY = 7;
    static final int COL_SORT_ORDER = 8;

    private ImageView mBackdropView;
    private TextView mTitleView;
    private TextView mOverviewView;
    private TextView mRatingView;
    private TextView mReleaseDateView;


    public DetailsActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_details, container, false);

        mBackdropView = (ImageView) rootView.findViewById(R.id.detail_backdrop);
        mTitleView = (TextView) rootView.findViewById(R.id.detail_title);
        mOverviewView = (TextView) rootView.findViewById(R.id.detail_overview);
        mRatingView = (TextView) rootView.findViewById(R.id.detail_rating);
        mReleaseDateView = (TextView) rootView.findViewById(R.id.detail_release_date);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.details_fragment, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);

        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareMovieDetailsIntent());
        } else {
            Log.d(LOG_TAG, "Share Action Provider is null?");
        }
    }

    private Intent createShareMovieDetailsIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, mMovieString + POPULAR_MOVIES_HASH_TAG);
        return shareIntent;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIE_DETAILS_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.v(LOG_TAG, "In onCreateLoader");
        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

        // Now create and return a CursorLoader that will take care of
        // creating a Cursor for the data being displayed.
        return new CursorLoader(
                getActivity(),
                intent.getData(),
                MOVIE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        Log.v(LOG_TAG, "In onLoadFinished");
        if (!data.moveToFirst()) {
            return;
        }

        String title = data.getString(COL_MOVIE_TITLE);
        mTitleView.setText(title);
        String overview = data.getString(COL_OVERVIEW);
        mOverviewView.setText(overview);
        String releaseDate = data.getString(COL_RELEASE_DATE);
        mReleaseDateView.setText(releaseDate);
        String voteAverage = Double.toString(data.getDouble(COL_VOTE_AVERAGE));
        mRatingView.setText(voteAverage);
        String backdropPath = data.getString(COL_BACKDROP_PATH);
        if (!backdropPath.equalsIgnoreCase("null")) {
            Uri uri = new Uri.Builder()
                    .scheme("http")
                    .authority("image.tmdb.org")
                    .appendPath("t")
                    .appendPath("p")
                    .appendPath("w500")
                    .appendPath(backdropPath).build();

            Log.i(LOG_TAG, "Get View Called: " + uri.toString() + "; Title:" + title);

            Picasso.with(getActivity()).load(uri.toString()).into(mBackdropView);
        } else {
            mBackdropView.setImageResource(R.drawable.blank);
        }

        mMovieString = String.format("Movie:%s; Rating:%s", title, voteAverage, backdropPath);

        // If onCreateOptionsMenu has already happened, we need to update the share intent now.
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareMovieDetailsIntent());
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
