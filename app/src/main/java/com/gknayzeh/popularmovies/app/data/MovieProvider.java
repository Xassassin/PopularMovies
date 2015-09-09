package com.gknayzeh.popularmovies.app.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;

/**
 * Created by gknayzeh on 07/09/15.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MovieDBHelper mOpenHelper;

    static final int MOVIES = 100;
    static final int MOVIES_WITH_SORT_BY = 101;
    static final int MOVIES_WITH_SORT_BY_AND_ORDER = 102;

    private static final SQLiteQueryBuilder sMovieBySortByQueryBuilder;

    static {
        sMovieBySortByQueryBuilder = new SQLiteQueryBuilder();

        sMovieBySortByQueryBuilder.setTables(MovieContract.MovieEntry.TABLE_NAME);
    }

    private static final String sMovieBySortByWithOrder =
            MovieContract.MovieEntry.TABLE_NAME+
                    "." + MovieContract.MovieEntry.COL_SORT_BY + " = ? AND " +
                    MovieContract.MovieEntry.COL_SORT_ORDER + " = ? ";

    private static final String sMovieBySortBy =
            MovieContract.MovieEntry.TABLE_NAME+
                    "." + MovieContract.MovieEntry.COL_SORT_BY + " = ? ";

    private Cursor getMovieBySortByAndOrder(
            Uri uri, String[] projection, String sortOrder) {
        String sortBy = MovieContract.MovieEntry.getSortByFromUri(uri);
        int order = MovieContract.MovieEntry.getOrderFromUri(uri);

        return sMovieBySortByQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieBySortByWithOrder,
                new String[]{sortBy, Integer.toString(order)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieBySortBy(
            Uri uri, String[] projection, String sortOrder) {
        String sortBy = MovieContract.MovieEntry.getSortByFromUri(uri);

        return sMovieBySortByQueryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sMovieBySortBy,
                new String[]{sortBy},
                null,
                null,
                sortOrder
        );
    }


    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, MovieContract.PATH_MOVIES, MOVIES);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/*", MOVIES_WITH_SORT_BY);
        matcher.addURI(authority, MovieContract.PATH_MOVIES + "/*/#", MOVIES_WITH_SORT_BY_AND_ORDER);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDBHelper(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            // "movies/*/*"
            case MOVIES_WITH_SORT_BY_AND_ORDER:
            {
                retCursor = getMovieBySortByAndOrder(uri, projection, sortOrder);
                break;
            }
            // "movies/*"
            case MOVIES_WITH_SORT_BY: {
                retCursor = getMovieBySortBy(uri, projection, sortOrder);
                break;
            }
            // "movies"
            case MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIES_WITH_SORT_BY:
                return MovieContract.MovieEntry.CONTENT_TYPE;
            case MOVIES_WITH_SORT_BY_AND_ORDER:
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case MOVIES: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                if ( _id > 0 )
                    returnUri = MovieContract.MovieEntry.buildMoviesUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if ( null == selection ) selection = "1";
        switch (match) {
            case MOVIES:
                rowsDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case MOVIES:
                rowsUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIES:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }
    }
}
