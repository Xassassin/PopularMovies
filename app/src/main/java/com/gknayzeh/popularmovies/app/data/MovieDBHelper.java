package com.gknayzeh.popularmovies.app.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.gknayzeh.popularmovies.app.data.MovieContract.MovieEntry;

/**
 * Created by gknayzeh on 03/09/15.
 */
public class MovieDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "movie.db";

    public MovieDBHelper(Context contex) {
        super(contex, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final StringBuilder SQL_CREATE_MOVIE_TABLE = new StringBuilder("CREATE TABLE ");
        SQL_CREATE_MOVIE_TABLE.append(MovieEntry.TABLE_NAME).append("(");

        SQL_CREATE_MOVIE_TABLE.append(MovieEntry._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,");

        SQL_CREATE_MOVIE_TABLE.append(MovieEntry.COL_MOVIE_TITLE).append(" TEXT NOT NULL,");

        SQL_CREATE_MOVIE_TABLE.append(MovieEntry.COL_BACKDROP_PATH).append(" TEXT,");

        SQL_CREATE_MOVIE_TABLE.append(MovieEntry.COL_POSTER_PATH).append(" TEXT,");

        SQL_CREATE_MOVIE_TABLE.append(MovieEntry.COL_OVERVIEW).append(" TEXT NOT NULL,");

        SQL_CREATE_MOVIE_TABLE.append(MovieEntry.COL_VOTE_AVERAGE).append(" REAL NOT NULL,");

        SQL_CREATE_MOVIE_TABLE.append(MovieEntry.COL_RELEASE_DATE).append(" TEXT NOT NULL,");

        SQL_CREATE_MOVIE_TABLE.append(MovieEntry.COL_SORT_BY).append(" TEXT NOT NULL,");

        SQL_CREATE_MOVIE_TABLE.append(MovieEntry.COL_SORT_ORDER).append(" INTEGER NOT NULL");

        SQL_CREATE_MOVIE_TABLE.append(");");

        db.execSQL(SQL_CREATE_MOVIE_TABLE.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(db);
    }


}
