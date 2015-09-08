package com.gknayzeh.popularmovies.app.data;

import android.provider.BaseColumns;

/**
 * Created by gknayzeh on 03/09/15.
 */
public class MovieContract {

    public static final class MovieEntry implements BaseColumns {
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

    }
}
