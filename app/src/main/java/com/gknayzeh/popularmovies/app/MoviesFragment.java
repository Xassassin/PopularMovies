package com.gknayzeh.popularmovies.app;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.gknayzeh.popularmovies.app.data.MovieContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private final String LOG_TAG = MoviesFragment.class.getSimpleName();

    private static final int MOVIES_LOADER = 0;

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

    PosterViewAdapter mPosterViewAdapter;

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movies_fragment, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_refresh) {
            updateMovies();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        mPosterViewAdapter = new PosterViewAdapter(getActivity(), null, 0);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView mGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        mGridView.setAdapter(mPosterViewAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // CursorAdapter returns a cursor at the correct position for getItem(), or null
                // if it cannot seek to that position.
                Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                if (cursor != null) {
                    String sortBy = Utility.getPreferredSortBy(getActivity());
                    Intent intent = new Intent(getActivity(), DetailsActivity.class)
                            .setData(MovieContract.MovieEntry.buildMoviesSortByAndOrder(
                                    sortBy, cursor.getInt(COL_SORT_ORDER)
                            ));
                    startActivity(intent);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(MOVIES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    void onSortByChanged( ) {
        updateMovies();
        getLoaderManager().restartLoader(MOVIES_LOADER, null, this);
    }

    private void updateMovies() {
        FetchMoviesTask task = new FetchMoviesTask(getActivity());
        String sortOrder = Utility.getPreferredSortBy(getActivity());
        task.execute(sortOrder);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String sortBy = Utility.getPreferredSortBy(getActivity());

        String sortOrder = MovieContract.MovieEntry.COL_SORT_ORDER + " ASC";
        Uri moviesSortBy = MovieContract.MovieEntry.buildMoviesSortBy(sortBy);

        Log.i(LOG_TAG, "URI: " + moviesSortBy.toString());

        return new CursorLoader(getActivity(),
                moviesSortBy,
                MOVIE_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        Log.i(LOG_TAG, "onLoadFinished");
        mPosterViewAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {
        Log.i(LOG_TAG, "onLoaderReset");
        mPosterViewAdapter.swapCursor(null);
//        mPosterViewAdapter.notifyDataSetChanged();
    }

}
