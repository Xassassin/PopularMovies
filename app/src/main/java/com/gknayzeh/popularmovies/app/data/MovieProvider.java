package com.gknayzeh.popularmovies.app.data;

import android.content.ContentProvider;
import android.content.UriMatcher;
import android.database.sqlite.SQLiteQueryBuilder;

/**
 * Created by gknayzeh on 07/09/15.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sURI_MATCHER = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    static final int MOVIES = 100;
    static final int MOVIES_WITH_SORT_BY = 101;
    static final int MOVIES_WITH_SORT_BY_AND_ORDER = 102;

    private static final SQLiteQueryBuilder sWeatherByLocationSettingQueryBuilder;

}
