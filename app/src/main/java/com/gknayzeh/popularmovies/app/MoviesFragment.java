package com.gknayzeh.popularmovies.app;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.gknayzeh.popularmovies.app.data.MovieContract;


/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragment extends Fragment {

    private final String LOG_TAG = MoviesFragment.class.getSimpleName();

    //    ArrayAdapter<String> mPopularMoviesAdapter;
    PosterViewAdapter mPosterViewAdapter;
    GridView mGridView;

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
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    private void updateMovies() {
        FetchMoviesTask task = new FetchMoviesTask(getActivity(), mPosterViewAdapter);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_popular));
        task.execute(sortOrder);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String sortBy = Utility.getPreferredSortBy(getActivity());

        String sortOrder = MovieContract.MovieEntry.COL_SORT_ORDER + " ASC";
        Uri moviesSortBy = MovieContract.MovieEntry.buildMoviesSortBy(sortBy);

        Cursor cur = getActivity().getContentResolver().query(moviesSortBy, null, null, null, sortOrder);

        mPosterViewAdapter = new PosterViewAdapter(getActivity(), cur, 0);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mGridView = (GridView) rootView.findViewById(R.id.gridview_movies);
        mGridView.setAdapter(mPosterViewAdapter);

//        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                MovieData movieData = (MovieData) mPosterViewAdapter.getItem(position);
//                Intent intent = new Intent(getActivity(), DetailsActivity.class)
//                        .putExtra(Intent.EXTRA_TEXT, movieData.toString());
//                startActivity(intent);
//            }
//        });


        return rootView;
    }

}
