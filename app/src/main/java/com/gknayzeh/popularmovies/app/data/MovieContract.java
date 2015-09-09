package com.gknayzeh.popularmovies.app.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by gknayzeh on 03/09/15.
 */
public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.gknayzeh.popularmovies.app";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIES = "movies";

    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIES;

        public static final String TABLE_NAME = "movies";

        // The original movie title
        public static final String COL_MOVIE_TITLE = "original_title";

        // The final part of the url for the backdrop
        public static final String COL_BACKDROP_PATH = "backdrop_path";

        // The final part of the url for the poster
        public static final String COL_POSTER_PATH = "poster_path";

        // The movie synopsis
        public static final String COL_OVERVIEW = "overview";

        // The voter average in double
        public static final String COL_VOTE_AVERAGE = "vote_average";

        // The release date in string format
        public static final String COL_RELEASE_DATE = "release_date";

        // The order
        public static final String COL_SORT_ORDER = "sort_order";

        // The order
        public static final String COL_SORT_BY = "sort_by";

        public static Uri buildMoviesUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildMoviesSortBy(String sortBy) {
            return CONTENT_URI.buildUpon().appendPath(sortBy).build();
        }

        public static String getSortByFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static int getOrderFromUri(Uri uri) {
            return Integer.parseInt(uri.getPathSegments().get(2));
        }

    }
}
