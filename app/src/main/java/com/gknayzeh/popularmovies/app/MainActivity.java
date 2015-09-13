package com.gknayzeh.popularmovies.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private final String MOVIESFRAGMENT_TAG = "MFTAG";
    private String mSortBy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSortBy = Utility.getPreferredSortBy(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MoviesFragment(), MOVIESFRAGMENT_TAG)
                    .commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String sortBy = Utility.getPreferredSortBy(this);
        // update the sortBy in our second pane using the fragment manager
        if (sortBy != null && !sortBy.equals(mSortBy)) {
            MoviesFragment mf = (MoviesFragment)getSupportFragmentManager().findFragmentByTag(MOVIESFRAGMENT_TAG);
            if ( null != mf ) {
                mf.onSortByChanged();
            }
            mSortBy = sortBy;
        }
    }
}
